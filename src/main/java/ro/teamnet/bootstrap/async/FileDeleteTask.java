package ro.teamnet.bootstrap.async;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.teamnet.bootstrap.service.UploadFileLogService;

import javax.inject.Inject;

/**
 * Created by Radu.Hoaghe on 7/28/2015.
 *
 * Task folosit pentru stergerea fisierelor expirate de pe sistemul de fisiere al serverului.
 * Acesta ruleaza o data la 10 minute si sterge toate fisierele care indeplinesc conditia mentionata anterior.
 */
@Component
public class FileDeleteTask {

    @Inject
    private UploadFileLogService uploadFileLogService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void deleteFiles(){
        uploadFileLogService.deleteExpiredFiles();
    }
}
