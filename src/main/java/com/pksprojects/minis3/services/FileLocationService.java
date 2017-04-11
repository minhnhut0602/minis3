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

    public boolean isFileStorable(long filesize) {
        refreshSpace();
        return filesize < getAvailableSpace();
    }

    public long getAvailableSpace() {
        return freeSpace - reservedSpace;
    }

    public boolean reserveSpace(long size) {
        if(isFileStorable(size)) {
            reservedSpace += size;
            return true;
        }
        return false;
    }

    public boolean freeReservedSpace(long size) {
        if(reservedSpace - size < 0)
            return false;
        reservedSpace -= size;
        return true;
    }

    private void refreshSpace() {
        try {
            freeSpace = FileSystemUtils.freeSpaceKb();
        } catch (IOException e) {
            logger.error("Unable to check available space on disk", e);
        }
    }

    public String getPathToStoreFile(String userId, String metaDataId) {
        return Paths.get(baseDirectory.toString(), userId, metaDataId).toAbsolutePath().normalize().toString();
    }
}
