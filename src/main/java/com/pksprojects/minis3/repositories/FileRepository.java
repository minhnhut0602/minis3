package com.pksprojects.minis3.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by PKS on 4/8/17.
 */
@Repository
public interface FileRepository{

    public void writeFile(String fileName, InputStream in, boolean isLarge) throws IOException;
    public FileSystemResource readFileAsStream(String filePath);
    public boolean createDirectory(String path);
    public void delete(String Id);
}
