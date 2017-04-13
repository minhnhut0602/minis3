package com.pksprojects.minis3.repositories;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implements FileRepository interface.
 * Created by PKS on 4/9/17.
 */
@Repository
class FileRepositoryImpl implements FileRepository {

    private static final Logger logger = LogManager.getLogger(FileRepository.class);

    @Autowired
    private Path baseDirectory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFile(String filePath, InputStream in, boolean isLarge) throws IOException {
        OutputStream out = new FileOutputStream(filePath);
        if(isLarge) IOUtils.copyLarge(in, out);
        else IOUtils.copy(in, out);
        in.close();
        out.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileSystemResource readFileAsStream(String filePath) {
        return new FileSystemResource(filePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createDirectory(String path) {
        Path fullpath = Paths.get(baseDirectory.toString(), path);
        if(!Files.exists(fullpath)) {
            try {
                Files.createDirectory(fullpath);
                return true;
            } catch (IOException e) {
                logger.error("Unable to create directory with path: " + fullpath, e);
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String fullPath) {
        Path path = Paths.get(fullPath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("Error! while deleting file at path: " + fullPath, e);
        }
    }
}
