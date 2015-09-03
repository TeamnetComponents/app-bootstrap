package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.UploadFileLog;

/**
 * @author cristian.uricec
 */
public interface UploadFileLogService extends AbstractService<UploadFileLog,Long>{

    /**
     * Metoda prin care se salveaza fisierul intr-o tabela de log si prin care se genereaza un token
     * care va fi stocat intr-un {@link java.util.concurrent.ConcurrentHashMap} impreuna cu calea catre fisier
     * si data expirarii token-ului.
     *
     * @param filePath Calea fisierului salvat pe server
     * @return Token-ul aferent fisierului
     * @throws Exception
     */
    String saveInUploadLog(String filePath) throws  Exception;

    /**
     * Metoda prin care se sterg toate fisierele salvate pe sistemul de fisiere al server-ului daca token-ul
     * aferent acestora a expirat. Aceasta metoda este apelata o data la 10 minute.
     */
    void deleteExpiredFiles();

    boolean deleteFile(String path, String token);

    UploadFileLog findByToken(String token);

}
