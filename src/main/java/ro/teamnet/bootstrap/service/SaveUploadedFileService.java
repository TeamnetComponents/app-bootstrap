package ro.teamnet.bootstrap.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * Created by cristian.uricec on 3/18/2015.
 */


public interface SaveUploadedFileService {

    /**
     * Metoda prin care se salveaza un fisier pe sistemul de fisiere al server-ului.
     *
     * @param file Continutul fisierului care trebuie incarcat.
     * @return Un mesaj prin care se specifica daca fisierul a fost incarcat sau nu.
     */
    String saveFile(MultipartFile file);
}
