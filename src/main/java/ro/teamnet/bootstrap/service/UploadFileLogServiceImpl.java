package ro.teamnet.bootstrap.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.UploadFileLog;
import ro.teamnet.bootstrap.repository.TokenGenerator;
import ro.teamnet.bootstrap.repository.UploadFileLogRepository;
import ro.teamnet.bootstrap.web.rest.dto.UploadFileLogDTO;

import javax.inject.Inject;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cristian.uricec on 3/17/2015.
 */
@Service
public class UploadFileLogServiceImpl implements UploadFileLogService {

    @Inject
    private UploadFileLogRepository uploadFileLogRepository;

    private TokenGenerator tokenGenerator = new TokenGenerator();

    @Inject
    private SavedFileService savedFileService;

    /**
     * Un {@link ConcurrentHashMap} care retine perechi de cheie-valoare continand in cheie token-ul aferent unui fisier
     * incarcat, iar in valoare detalii despre acest fisier (calea catre fisier si timpul de expirare al token-ului)
     */
    @Inject
    @Qualifier("tokens")
    private Map<String, Object> tokensCache;

    @Transactional
    @Override
    public String saveInUploadLog(String filePath) throws Exception {

        UploadFileLog uploadFileLog = new UploadFileLog();
        uploadFileLog.setFileLocation(filePath);
        uploadFileLog.setToken(tokenGenerator.nextSessionId());
        uploadFileLog.setUploadDate(new Date());
        uploadFileLog.setStatus(0l);
        uploadFileLogRepository.save(uploadFileLog);

        DateTime expiringDate = new DateTime();
        expiringDate = expiringDate.plusMinutes(3);
        ((ConcurrentHashMap) tokensCache.get("tokens")).put(uploadFileLog.getToken(), // se adauga in Map-ul care retine toti tokenii
                new UploadFileLogDTO(filePath, expiringDate));

        return uploadFileLog.getToken();
    }

    @Transactional
    @Override
    public void deleteExpiredFiles() {

        List<String> listOfTokensToBeDeleted = new ArrayList<>();
        for(Object entry : ((ConcurrentHashMap) tokensCache.get("tokens")).keySet()){

            String token = (String) entry;

            UploadFileLogDTO uploadFileLogDTO = (UploadFileLogDTO)
                    ((ConcurrentHashMap) tokensCache.get("tokens")).get(token);

            if(uploadFileLogDTO.getExpiringDate().isBeforeNow()){
                listOfTokensToBeDeleted.add(token);
                deleteFile(uploadFileLogDTO.getFilePath(), token);
            }
        }

        for(String token : listOfTokensToBeDeleted){
            ((ConcurrentHashMap) tokensCache.get("tokens")).remove(token);
        }
    }

    /**
     * Metoda prin care se incearca sa se stearga un fisier de pe sistemul de fisiere al server-ului.
     *
     * @param path Calea absoluta a fisierului de pe server.
     * @param token Token-ul aferent fisierului incarcat pe server.
     * @return {@code true} in cazul in care fisierul a fost sters de pe sistemul de fisiere al server-ului sau
     *         {@code false} in caz contrar.
     */
    private boolean deleteFile(String path, String token) {
        File file = new File(path);
        if(file.exists()){
            if(file.delete()){
                uploadFileLogRepository.delete(uploadFileLogRepository.findByToken(token));
                return true;
            }
        }
        return false;
    }
}
