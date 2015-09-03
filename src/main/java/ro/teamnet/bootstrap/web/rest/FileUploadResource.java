package ro.teamnet.bootstrap.web.rest;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.teamnet.bootstrap.plugin.upload.FileServicePlugin;
import ro.teamnet.bootstrap.plugin.upload.FileUploadType;

import javax.inject.Inject;


@RestController
@RequestMapping("/app/rest/uploadFile")
public class FileUploadResource<T>{



    @Inject
    @Qualifier("fileServicePluginRegistry")
    private PluginRegistry<FileServicePlugin, FileUploadType> fileServicePlugins;



    @RequestMapping(value = "/doUpload/{groupId}", method = RequestMethod.POST,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadFileHandler(@RequestParam("file") MultipartFile file,@PathVariable("groupId")String groupId) {
        FileServicePlugin fileServicePlugin = fileServicePlugins.getPluginFor(FileUploadType.USER_SESSION);
        String token= fileServicePlugin.uploadFile(file,groupId);
        return new ResponseEntity<>("{\"token\":\""+token + "\"}", HttpStatus.OK);
    }

    @RequestMapping(value = "/clean/{groupId}",method = RequestMethod.DELETE)
    public ResponseEntity clean(@RequestBody String token,@PathVariable("groupId")String groupId){
        FileServicePlugin fileServicePlugin = fileServicePlugins.getPluginFor(FileUploadType.USER_SESSION);
        fileServicePlugin.cleanUp(token,groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
