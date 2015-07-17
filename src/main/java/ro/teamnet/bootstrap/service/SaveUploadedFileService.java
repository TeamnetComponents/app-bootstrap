package ro.teamnet.bootstrap.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * Created by cristian.uricec on 3/18/2015.
 */


public interface SaveUploadedFileService {

    public String saveFile(MultipartFile file);
}
