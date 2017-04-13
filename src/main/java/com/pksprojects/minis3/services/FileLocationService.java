package com.pksprojects.minis3.services;

import org.apache.commons.io.FileSystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple service to provide location/path where to store file and check if file can be stored.
 * Created by PKS on 4/9/17.
 */
@Service
public class FileLocationService {

    private static final Logger logger = LogManager.getLogger(FileLocationService.class);

    private static long freeSpace;

    private static long reservedSpace;

    @Autowired
    private Path baseDirectory;

    FileLocationService() {
        reservedSpace = 102400; //reserved 100MB for system.
        refreshSpace();
    }

    @PostConstruct
    void init() {
        logger.info("Available free space in disk is:" + freeSpace +"KB");
    }

    /**
     * Check if enough space is available to store file.
     * @param filesize size of the file to be stored
     * @return true is storable else false
     */
    public boolean isFileStorable(long filesize) {
        refreshSpace();
        return filesize < getAvailableSpace();
    }

    /**
     * Checks and return available space.
     * @return Long available space.
     */
    public long getAvailableSpace() {
        return freeSpace - reservedSpace;
    }

    /**
     * Reserves space for file whose meta data is stored
     * @param size of space to be reserved
     * @return true if reserved successfully else false
     */
    public boolean reserveSpace(long size) {
        if(isFileStorable(size)) {
            reservedSpace += size;
            return true;
        }
        return false;
    }

    /**
     * frees reserved space once file is uploaded successfully
     * @param size of space to be freed
     * @return true if success else false.
     */
    public boolean freeReservedSpace(long size) {
        if(reservedSpace - size < 0)
            return false;
        reservedSpace -= size;
        return true;
    }

    /**
     * Checks and updates available free space variable
     */
    private void refreshSpace() {
        try {
            freeSpace = FileSystemUtils.freeSpaceKb();
        } catch (IOException e) {
            logger.error("Unable to check available space on disk", e);
        }
    }

    /**
     * Generates path to stored file based on user's Id and metadata Id
     * @param userId Id of the user uploading file
     * @param metaDataId Id of metadata corresponding to file.
     * @return String path to location for storing file.
     */
    public String getPathToStoreFile(String userId, String metaDataId) {
        return Paths.get(baseDirectory.toString(), userId, metaDataId).toAbsolutePath().normalize().toString();
    }
}
