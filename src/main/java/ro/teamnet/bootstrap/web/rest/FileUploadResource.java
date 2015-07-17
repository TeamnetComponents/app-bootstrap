package ro.teamnet.bootstrap.web.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ro.teamnet.bootstrap.service.SaveUploadedFileService;

import javax.inject.Inject;


/**
 * Created by cristian.uricec on 3/11/2015.
 */
@RestController
@RequestMapping("/upload")
public class FileUploadResource {

    @Inject
    private SaveUploadedFileService saveUploadedFileService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadFileHandler(@RequestParam("file") MultipartFile file) {

        String token = saveUploadedFileService.saveFile( file);
        return new ResponseEntity<>("{\"token\":\""+token + "\"}", HttpStatus.OK);
    }
}
