package com.pksprojects.minis3.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface providing API's for File operations like read, write, delete.
 * Created by PKS on 4/8/17.
 */
@Repository
public interface FileRepository{

    /**
     * Writes stream of file on to disk with provided name.
     * @param fileName full path + filename of file.
     * @param in InputStream of file.
     * @param isLarge boolean flag set true if file size > 2GB
     * @throws IOException
     */
    public void writeFile(String fileName, InputStream in, boolean isLarge) throws IOException;

    /**
     * Reads file at specified path.
     * @param filePath full path the file.
     * @return Streamable FileSystemResource
     */
    public FileSystemResource readFileAsStream(String filePath);

    /**
     * Creates directory at the given path.
     * @param path to create directory.
     * @return true if successful else false
     */
    public boolean createDirectory(String path);

    /**
     * Delete's file at the specified path.
     * @param fullPath to the file.
     */
    public void delete(String fullPath);
}
