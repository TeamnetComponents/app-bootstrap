package ro.teamnet.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by cristian.uricec on 3/18/2015.
 */

@Service
public class SaveUploadedFileServiceImpl implements  SaveUploadedFileService {

    private final Logger log = LoggerFactory.getLogger(SaveUploadedFileServiceImpl.class);

    @Inject
    private UploadFileLogService uploadFileLogService;

    @Override
    public String saveFile( MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                File dir = createDirectory();
                String absolutePath = createFile(dir,file);
                String token = uploadFileLogService.saveInUploadLog(absolutePath);
                return token;
            } catch (Exception e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getOriginalFilename()
                + " because the file was empty.";
        }
    }

    /**
     * Metoda prin care se creeaza un director pe sistemul de fisiere al server-ului.
     * @return Referinta catre director.
     * @throws Exception
     */
    private File createDirectory() throws Exception{

        String rootPath = System.getProperty("user.dir");

        File dir = new File(rootPath + File.separator + "uploadedFiles");
        if (!dir.exists())
            dir.mkdirs();

        return dir;
    }

    /**
     * Metoda prin care se creeaza un fisier pe sistemul de fisiere al server-ului.
     *
     * @param dir Directorul in care se va crea fisierul.
     * @param file Fisierul care se va crea pe sistemul de fisiere al server-ului.
     * @return Calea absoluta a fisierului.
     * @throws Exception
     */
    private String createFile(File dir, MultipartFile file ) throws Exception{

        byte[] bytes = file.getBytes();
        File serverFile = new File(dir.getAbsolutePath()
            + File.separator + file.getOriginalFilename()); // add ( timespam/ + user ) => hashcode - pentru download
        BufferedOutputStream stream = new BufferedOutputStream(
            new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        log.info("Server File Location="
            + serverFile.getAbsolutePath());

        return serverFile.getAbsolutePath();
    }
}
