package ro.teamnet.bootstrap.async;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.teamnet.bootstrap.service.UploadFileLogService;

import javax.inject.Inject;

/**
 * Task used to delete outdated files on the server filesystem.
 * It runs every 10 minutes and delete all files that meet the above mentioned condition.
 *
 * @author Radu.Hoaghe
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
