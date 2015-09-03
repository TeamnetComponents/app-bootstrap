package ro.teamnet.bootstrap.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.UploadFileLog;

import ro.teamnet.bootstrap.extend.AppRepository;
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
public class UploadFileLogServiceImpl extends AbstractServiceImpl<UploadFileLog,Long> implements UploadFileLogService {


    public static final String TOKENS = "tokens";
    public static final int EXP_MIN = 3;
    public static final long DEF_STATUS = 0l;
    private UploadFileLogRepository uploadFileLogRepository;

    private TokenGenerator tokenGenerator = new TokenGenerator();



    /**
     * Un {@link ConcurrentHashMap} care retine perechi de cheie-valoare continand in cheie token-ul aferent unui fisier
     * incarcat, iar in valoare detalii despre acest fisier (calea catre fisier si timpul de expirare al token-ului)
     */
    @Inject
    @Qualifier("tokens")
    private Map<String, Object> tokensCache;

    @Inject
    public UploadFileLogServiceImpl(UploadFileLogRepository uploadFileLogRepository) {
        super(uploadFileLogRepository);
        this.uploadFileLogRepository=uploadFileLogRepository;

    }

    @Transactional
    @Override
    public String saveInUploadLog(String filePath) throws Exception {

        UploadFileLog uploadFileLog = new UploadFileLog();
        uploadFileLog.setFileLocation(filePath);
        uploadFileLog.setToken(tokenGenerator.nextSessionId());
        uploadFileLog.setUploadDate(new Date());
        uploadFileLog.setStatus(DEF_STATUS);
        uploadFileLogRepository.save(uploadFileLog);

        DateTime expiringDate = new DateTime();
        expiringDate = expiringDate.plusMinutes(EXP_MIN);
        //noinspection unchecked
        ((ConcurrentHashMap) tokensCache.get(TOKENS)).put(uploadFileLog.getToken(), // se adauga in Map-ul care retine toti tokenii
                new UploadFileLogDTO(filePath, expiringDate));

        return uploadFileLog.getToken();
    }

    @Transactional
    @Override
    public void deleteExpiredFiles() {

        List<String> listOfTokensToBeDeleted = new ArrayList<>();
        for(Object entry : ((ConcurrentHashMap) tokensCache.get(TOKENS)).keySet()){

            String token = (String) entry;

            UploadFileLogDTO uploadFileLogDTO = (UploadFileLogDTO)
                    ((ConcurrentHashMap) tokensCache.get(TOKENS)).get(token);

            if(uploadFileLogDTO.getExpiringDate().isBeforeNow()){
                listOfTokensToBeDeleted.add(token);
                deleteFile(uploadFileLogDTO.getFilePath(), token);
            }
        }

        for(String token : listOfTokensToBeDeleted){
            ((ConcurrentHashMap) tokensCache.get(TOKENS)).remove(token);
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
    public boolean deleteFile(String path, String token) {
        File file = new File(path);
        if(file.exists()){
            if(file.delete()){
                uploadFileLogRepository.delete(uploadFileLogRepository.findByToken(token));
                return true;
            }
        }
        return false;
    }

    public UploadFileLog findByToken(String token){
        return uploadFileLogRepository.findByToken(token);
    }


}
