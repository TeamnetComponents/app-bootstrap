package ro.teamnet.bootstrap.web.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.teamnet.bootstrap.service.AbstractService;
import ro.teamnet.bootstrap.service.SaveUploadedFileService;
import ro.teamnet.bootstrap.service.SavedFileService;
import ro.teamnet.bootstrap.util.ResponseMessageEnum;

import javax.inject.Inject;
import java.nio.file.Path;


/**
 * Created by cristian.uricec on 3/11/2015.
 */
@RestController
@RequestMapping("/upload")
public class FileUploadResource{

    @Inject
    private SaveUploadedFileService saveUploadedFileService;

    @Inject
    private SavedFileService savedFileService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadFileHandler(@RequestParam("file") MultipartFile file) {

        String token = saveUploadedFileService.saveFile( file);
        return new ResponseEntity<>("{\"token\":\""+token + "\"}", HttpStatus.OK);
    }

    @RequestMapping(value = "/sendToken", method = RequestMethod.POST, headers = {"content-type=application/json;charset=UTF-8"})
    public ResponseEntity sendToken(@RequestBody String token){

        String filePath = savedFileService.saveUploadedFileToDB(token);
        if(filePath != null) {
            if(filePath.equals(ResponseMessageEnum.FILE_TOKEN_EXPIRED.getMessage())
                    || filePath.equals(ResponseMessageEnum.FILE_WAS_SAVED.getMessage()))
                return new ResponseEntity<>("{\"response\": \"" + filePath + "\"}", HttpStatus.CONFLICT);

            else {
                if (savedFileService.deleteFile(filePath, token))
                    return new ResponseEntity<>("{\"response\": \"" + ResponseMessageEnum.FILE_SAVED.getMessage() + "\"}", HttpStatus.OK);

                return new ResponseEntity<>("{\"response\": \"" + ResponseMessageEnum.FILE_NOT_DELETED_FROM_SERVER.getMessage() + "\"}",
                        HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>("{\"response\": \"" + ResponseMessageEnum.FILE_NOT_FOUND_ON_SERVER.getMessage() + "\"}",
                    HttpStatus.CONFLICT);
        }
    }
}
