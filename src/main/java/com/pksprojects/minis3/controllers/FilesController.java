package com.pksprojects.minis3.controllers;

import com.pksprojects.minis3.models.metadata.MetaData;
import com.pksprojects.minis3.services.FileMetaDataService;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller provides GET and POST request to upload and download file.
 * Created by PKS on 4/8/17.
 */
@RestController
@RequestMapping("/api/v1/files")
public class FilesController {

    private static final Logger logger = LogManager.getLogger(FilesController.class);

    @Autowired
    private FileMetaDataService fileMetaDataService;

    @Autowired
    private ServletFileUpload servletFileUpload;

    /**
     * GET API to download single file as stream.
     * @param Id of file to be downloaded
     * @return File as stream.
     */
    @RequestMapping(value = "/{Id}", method = GET)
    public ResponseEntity<FileSystemResource> get(@PathVariable String Id) {
        HttpHeaders headers = fileMetaDataService.getHeaders(Id);
        FileSystemResource isr = fileMetaDataService.getFile(Id);
        return new ResponseEntity<>(isr, headers, HttpStatus.OK);
    }

    /**
     * POST API to upload single file by metadata Id
     * @param Id of metadata for the particular file.
     * @param request
     * @return Success if file is stored successfully or an appropriate error msg.
     */
    @RequestMapping(value = "/{Id}", method = POST)
    public ResponseEntity<String> upload(@PathVariable String Id, HttpServletRequest request) {
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                return new ResponseEntity<>( "Not a multipart request.",
                        HttpStatus.BAD_REQUEST);
            }
            Long length = request.getContentLengthLong();
            FileItemIterator iter = servletFileUpload.getItemIterator(request);
            fileMetaDataService.saveFile(Id, iter);
        } catch (FileUploadException e) {
            logger.error(e);
            return new ResponseEntity<>("File upload error", HttpStatus.PRECONDITION_FAILED);
        } catch (IOException e) {
            logger.error(e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
