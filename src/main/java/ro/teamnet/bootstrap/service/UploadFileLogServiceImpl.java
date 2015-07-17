package ro.teamnet.bootstrap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.UploadFileLog;
import ro.teamnet.bootstrap.repository.TokenGenerator;
import ro.teamnet.bootstrap.repository.UploadFileLogRepository;

import javax.inject.Inject;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by cristian.uricec on 3/17/2015.
 */
@Service
public class UploadFileLogServiceImpl implements UploadFileLogService {

    @Inject
    private UploadFileLogRepository uploadFileLogRepository;

    private TokenGenerator tokenGenerator = new TokenGenerator();

    @Transactional
    @Override
    public String saveInUploadLog(String filePath) throws Exception {


        UploadFileLog uploadFileLog = new UploadFileLog();
        uploadFileLog.setFileLocation(filePath);
        uploadFileLog.setToken(tokenGenerator.nextSessionId());
        uploadFileLog.setUploadDate(new Date());
        uploadFileLog.setStatus(0l);
        uploadFileLogRepository.save(uploadFileLog);
        return uploadFileLog.getToken();


    }

    @Transactional
    @Override
    public void fileToDelete() {

        List<UploadFileLog> listOfLogs = uploadFileLogRepository.findAll(); //findByTimeStamp
        for(UploadFileLog log : listOfLogs){
            if(log.getUploadDate() != null ){ //de modificat
             try{
                 File file = new File(log.getFileLocation());
                 if(file.exists())
                    file.delete();
                 uploadFileLogRepository.delete(log);
             }catch(Exception e){

                e.printStackTrace();

            }
            }
        }

    }

}
