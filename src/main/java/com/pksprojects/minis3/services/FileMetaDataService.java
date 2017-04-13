package com.pksprojects.minis3.services;

import com.pksprojects.minis3.models.metadata.MetaData;
import com.pksprojects.minis3.models.user.User;
import com.pksprojects.minis3.models.view.metadata.MetaDataView;
import com.pksprojects.minis3.repositories.FileRepository;
import com.pksprojects.minis3.repositories.MetaDataRepository;
import com.pksprojects.minis3.repositories.OnlyNameAndUserId;
import javassist.NotFoundException;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provies Service API's for both file and metadata
 * Created by PKS on 4/8/17.
 */
@Service
public class FileMetaDataService {

    private static final Logger logger = LogManager.getLogger(FileMetaDataService.class);

    @Autowired
    private MetaDataRepository metaDataRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileLocationService locationService;

    @Autowired
    private UsersService usersService;

    /**
     * Generates metadata from metadataview and stores in the database if file is storable.
     * @param view MetaDataView for uploaded metadata
     * @return
     */
    public String save(MetaDataView view) {
        if(locationService.isFileStorable(view.getSize())) {
            if(locationService.reserveSpace(view.getSize())) {
                User user = usersService.getCurrentUser();
                MetaData metaData = new MetaData(view, user);
                if(metaDataRepository.findOneByUserAndName(user, metaData.getName()) == null) {
                    String fullPath = locationService.getPathToStoreFile(user.getId(), metaData.getId());
                    metaData.setFullPath(fullPath);
                    MetaData saved = metaDataRepository.saveAndFlush(metaData);
                    if (saved != null) return saved.getId();
                }
            }
        }
        return null;
    }

    /**
     * Updates existing metadata, Accepts MetadataView and generates updated MetaData by merging view and old metadata.
     * @param metaData MetaDataView consisting updated value.
     * @return ture if success else false
     * @throws NotFoundException if invalid MetadataView is passed.
     */
    public boolean update(MetaDataView metaData) throws NotFoundException {
        MetaData old = metaDataRepository.findOne(metaData.getId());
        if(old == null)
            throw new NotFoundException("cannot find metadata with id: " + metaData.getId());
        MetaData updated = getUpdated(old, metaData);
        return metaDataRepository.saveAndFlush(updated) != null;
    }

    /**
     * Gets metadata corresponding to Id
     * @param Id of metadta
     * @return metadata
     */
    public MetaData get(String Id) {
        return metaDataRepository.getOne(Id);
    }

    public List<MetaData> get(User user, int offSet, int limit) {
        return metaDataRepository.findAllByUserOrderByTimeCreatedDesc(user, new PageRequest(offSet, limit));
    }

    /**
     * Delete's metadata given Id.
     * @param Id of metadata
     */
    public void delete(String Id) {
        metaDataRepository.delete(Id);
        fileRepository.delete(Id);
    }

    /**
     * Generated Http Headers to send while downloading file.
     * @param metaId corresponding to file requested.
     * @return HttpHeaders.
     */
    public HttpHeaders getHeaders(String metaId) {
        MetaData metaData = get(metaId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + metaData.getName()+"\"");
        headers.add("Content-Type", metaData.getContentType());
        headers.add("Content-Length", metaData.getSize().toString());
        headers.add("Cache-Control", metaData.getCacheControl());
        headers.add("Content-MD5", metaData.getMd5Hash());
        if(metaData.getContentEncoding() != null)
            headers.add("Content-Encoding", metaData.getContentEncoding());
        if(metaData.getContentLanguage() != null)
            headers.add("Content-Encoding", metaData.getContentLanguage());
        if(metaData.getContentEncoding() != null)
            headers.add("Content-Encoding", metaData.getContentEncoding());
        metaData.getCustomMetaData().forEach(headers::add);
        return headers;
    }

    /**
     * Gets All the files uploaded on past hour and maps them to users
     * @return MapList<String, List<String>> mapping user email to file names
     */
    public Map<String, List<String>> getAllNewIdsAndUserEmailForNotification() {
        List<OnlyNameAndUserId> results = metaDataRepository.
                findAllByTimeCreatedBetweenOrderByUserAsc(LocalDateTime.now().minusHours(1),
                        LocalDateTime.now());
        Map<String, List<String>> userIdToFileNameMap = new HashMap<>();
        results.forEach(result -> {
            if(!userIdToFileNameMap.containsKey(result.getUser().getEmail())) {
                userIdToFileNameMap.put(result.getUser().getEmail(), new ArrayList<String>());
                userIdToFileNameMap.get(result.getUser().getEmail()).add(result.getName());
            } else {
                userIdToFileNameMap.get(result.getUser().getEmail()).add(result.getName());
            }
        });
        return userIdToFileNameMap;
    }

    /**
     * saves file to disk given parameters
     * @param metaId corresponding to files.
     * @param itemIterator FileItemIterator processed from request
     * @throws IOException
     * @throws FileUploadException
     */
    public void saveFile(String metaId, FileItemIterator itemIterator) throws IOException, FileUploadException {
        MetaData metaData = get(metaId);
        String filePath = metaData.getFullPath();
        while (itemIterator.hasNext()) {
            FileItemStream item = itemIterator.next();
            InputStream stream = item.openStream();
            if (!item.isFormField()) {
                fileRepository.writeFile(filePath, stream, isLarge(metaData.getSize()));
                locationService.freeReservedSpace(metaData.getSize());
            }
        }
    }

    /**
     * Reads file ad FileSystemResource
     * @param Id of file to be read
     * @return FileSystemResource i.e. file as stream
     */
    public FileSystemResource getFile(String Id) {
        String filePath = get(Id).getFullPath();
        return fileRepository.readFileAsStream(filePath);
    }

    /**
     * Merges the Old MetaData and New MetaDataView objects to get updated object.
     * @param o Old Metadata
     * @param n new MetaDataview
     * @return
     */
    private MetaData getUpdated(MetaData o, MetaDataView n) {
        o.setName(n.getName());
        o.setCacheControl(n.getCacheControl());
        o.setContentDisposition(n.getContentDisposition());
        o.setContentEncoding(n.getContentEncoding());
        o.setContentLanguage(n.getContentLanguage());
        o.setContentType(n.getContentType());
        o.setCustomMetaData(n.getCustomMetaData());
        o.setUpdated(LocalDateTime.now());
        return o;
    }

    /**
     * Checks if file is greated than 2 GB
     * @param size of file to be checked
     * @return ture if file  > 2GB else false
     */
    private boolean isLarge(long size) {
        return size > 2 * 1024 * 1024;
    }
}
