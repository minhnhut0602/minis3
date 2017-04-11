package com.pksprojects.minis3.controllers;

import com.pksprojects.minis3.models.metadata.MetaData;
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
 * Created by PKS on 4/8/17.
 */
@RestController
@RequestMapping("/api/v1/meta/")
public class MetaDataController {

    private static final Logger logger = LogManager.getLogger(MetaDataController.class);

    @Autowired
    private FileMetaDataService fileMetaDataService;

    @Autowired
    private UsersService usersService;

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
        String userId = usersService.getCurrentUserId();
        List<MetaData> metaDataList = fileMetaDataService.get(userId, offset, limit);
        List<MetaDataView> metaDataViewList = metaDataList.stream()
                .map(MetaDataView::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(metaDataViewList);
    }

    @RequestMapping(value = "{Id}" ,method = GET)
    public ResponseEntity<MetaDataView> get(@PathVariable String Id) {
        return ResponseEntity.ok(new MetaDataView(fileMetaDataService.get(Id)));
    }

    @RequestMapping(value = "/", method = POST)
    public ResponseEntity<String> upload(@RequestBody MetaDataView metaData) {
        String Id = fileMetaDataService.save(metaData);
        if(Id != null) return ResponseEntity.ok(Id);
        return ResponseEntity.badRequest().body("Something went wrong! Please, try again later.");
    }

    @RequestMapping(value = "/", method = PUT)
    public void update(@RequestBody MetaDataView metaData) {
        try {
            fileMetaDataService.update(metaData);
        } catch (NotFoundException e) {
            logger.error("Unable to locate file metaData with Id: " + metaData.getId());
        }
    }

    @RequestMapping(value = "/{Id}", method = DELETE)
    public void delete(@PathVariable String Id) {
        fileMetaDataService.delete(Id);
    }
}
