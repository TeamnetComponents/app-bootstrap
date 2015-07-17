package ro.teamnet.bootstrap.service;


/**
 * Created by cristian.uricec on 3/17/2015.
 */
public interface UploadFileLogService {

    public String saveInUploadLog(String filePath) throws  Exception;
    public void fileToDelete();

}
