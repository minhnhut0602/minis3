package com.pksprojects.minis3.controllers;

import com.pksprojects.minis3.models.metadata.MetaData;
import com.pksprojects.minis3.models.user.User;
import com.pksprojects.minis3.models.view.metadata.MetaDataView;
import com.pksprojects.minis3.services.FileMetaDataService;
import com.pksprojects.minis3.services.UsersService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Controller provides GET POST PUT DELETE API's for metadata operations.
 * Created by PKS on 4/8/17.
 */
@RestController
@RequestMapping("/api/v1/meta")
public class MetaDataController {

    private static final Logger logger = LogManager.getLogger(MetaDataController.class);

    @Autowired
    private FileMetaDataService fileMetaDataService;

    @Autowired
    private UsersService usersService;

    /**
     * GET API to get all files for current user in the range of offset.
     * @param offset page number to get.
     * @param limit size of page requested.
     * @return List of Metadata in range offset - limit
     */
    @RequestMapping(value = "/", method = GET)
    public ResponseEntity<List<MetaDataView>> getAll(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit
    ) {
        if(offset == null) {
            offset = 0;
        }
        if(limit == null) {
            limit = 10;
        }
        User user = usersService.getCurrentUser();
        List<MetaData> metaDataList = fileMetaDataService.get(user, offset, limit);
        List<MetaDataView> metaDataViewList = metaDataList.stream()
                .map(MetaDataView::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(metaDataViewList);
    }

    /**
     * GET API to get single metadata for the given Id.
     * @param Id of metadata to get.
     * @return MetaData corresponding to Id
     */
    @RequestMapping(value = "/{Id}" ,method = GET)
    public ResponseEntity<MetaDataView> get(@PathVariable String Id) {
        return ResponseEntity.ok(new MetaDataView(fileMetaDataService.get(Id)));
    }

    /**
     * POST API to create/upload new MetaData
     * @param metaData to be stored.
     * @return Id of metaData stored if success else a error message.
     */
    @RequestMapping(value = "/", method = POST)
    public ResponseEntity<String> upload(@RequestBody MetaDataView metaData) {
        String Id = fileMetaDataService.save(metaData);
        if(Id != null) return ResponseEntity.ok(Id);
        return ResponseEntity.badRequest().body("Something went wrong! Please, try again later.");
    }

    /**
     * PUT API to update stored metadata.
     * @param metaData updated value to be stored.
     */
    @RequestMapping(value = "/", method = PUT)
    public void update(@RequestBody MetaDataView metaData) {
        try {
            fileMetaDataService.update(metaData);
        } catch (NotFoundException e) {
            logger.error("Unable to locate file metaData with Id: " + metaData.getId());
        }
    }

    /**
     * DELETE API to delete metadata and file stored for given Id.
     * @param Id of metadata to be deleted.
     */
    @RequestMapping(value = "/{Id}", method = DELETE)
    public void delete(@PathVariable String Id) {
        fileMetaDataService.delete(Id);
    }
}
