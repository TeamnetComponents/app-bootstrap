package ro.teamnet.bootstrap.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.SavedFile;
import ro.teamnet.bootstrap.domain.SavedFileDetails;
import ro.teamnet.bootstrap.repository.SavedFileDetailsRepository;
import ro.teamnet.bootstrap.repository.SavedFileRepository;
import ro.teamnet.bootstrap.repository.UploadFileLogRepository;
import ro.teamnet.bootstrap.util.ResponseMessageEnum;
import ro.teamnet.bootstrap.web.rest.dto.UploadFileLogDTO;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by Radu.Hoaghe on 7/24/2015.
 */
@Service
public class SavedFileServiceImpl implements SavedFileService {

    @Inject
    private SavedFileRepository savedFileRepository;

    @Inject
    private SavedFileDetailsRepository savedFileDetailsRepository;

    @Inject
    private UploadFileLogRepository uploadFileLogRepository;

    /**
     * Un {@link ConcurrentHashMap} care retine perechi de cheie-valoare continand in cheie token-ul aferent unui fisier
     * incarcat, iar in valoare detalii despre acest fisier (calea catre fisier si timpul de expirare al token-ului)
     */
    @Inject
    @Qualifier("tokens")
    private Map<String, Object> tokensCache;

    /**
     * Metoda prin care se incearca sa se afle calea absoluta a unui fisier aflat pe sistemul de fisiere al server-ului.
     *
     * @param token Token-ul aferent fisierlui incarcat.
     * @return Calea absoluta a fisierului incarcat sau un mesaj de eroare prin care se detaliaza motivul pentru care
     *         fisierul nu a putut fi gasit pe sistemul de fisiere al server-ului.
     */
    private String getUploadedFileByToken(String token){
        UploadFileLogDTO fileDetails = (UploadFileLogDTO) ((ConcurrentHashMap) tokensCache.get("tokens")).get(token);

        if(fileDetails == null)
            return ResponseMessageEnum.FILE_WAS_SAVED.getMessage();

        if(fileDetails.getExpiringDate().isBeforeNow()){
            return ResponseMessageEnum.FILE_TOKEN_EXPIRED.getMessage();
        }

        return fileDetails.getFilePath();
    }

    @Override
    @Transactional
    public String saveUploadedFileToDB(String token){

        String filePath = getUploadedFileByToken(token);

        if(!filePath.equals(ResponseMessageEnum.FILE_WAS_SAVED.getMessage())
                && !filePath.equals(ResponseMessageEnum.FILE_TOKEN_EXPIRED.getMessage())){

            Path path = Paths.get(filePath);
            try {
                byte[] fileBytes = Files.readAllBytes(path);
                SavedFile savedFile = new SavedFile();
                savedFile.setFileContent(fileBytes);
                savedFileRepository.save(savedFile);

                SavedFileDetails savedFileDetails = new SavedFileDetails();
                savedFileDetails.setFkSavedFile(savedFile);
                savedFileDetails.setCreatedDate(new Date());

                String[] splitFilePath = filePath.split(Pattern.quote(File.separator));

                savedFileDetails.setFileName(splitFilePath[splitFilePath.length - 1]);

                String fileType = Files.probeContentType(path);
                if(fileType == null){
                    String[] fileNameAndFileType = splitFilePath[splitFilePath.length - 1].split(Pattern.quote("."));
                    if(fileNameAndFileType.length > 1)
                        fileType = fileNameAndFileType[fileNameAndFileType.length - 1];
                }
                savedFileDetails.setFileType(fileType);

                savedFileDetailsRepository.save(savedFileDetails);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return path.toString();

        }else
            return filePath;
    }

    @Override
    @Transactional
    public boolean deleteFile(String path, String token) {
        File file = new File(path);
        if(file.exists()){
            if(file.delete()){
                uploadFileLogRepository.delete(uploadFileLogRepository.findByToken(token));
                ((ConcurrentHashMap) tokensCache.get("tokens")).remove(token);
                return true;
            }
        }
        return false;
    }
}
