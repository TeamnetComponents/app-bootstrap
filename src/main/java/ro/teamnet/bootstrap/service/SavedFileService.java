package ro.teamnet.bootstrap.service;

/**
 * Created by Radu.Hoaghe on 7/24/2015.
 */
public interface SavedFileService {

    /**
     * Metoda prin care se salveaza un fisier care a fost in prealabil incarcat pe server in baza de date.
     *
     * @param token Token-ul aferent fisierului incarcat pe server.
     * @return Un sir de caractere care contine mesajul rezultat in urma incercarii de salvare in baza de date. Acest
     *         mesaj poate sa fie unul dintre cele din {@link ro.teamnet.bootstrap.util.ResponseMessageEnum}.
     */
    String saveUploadedFileToDB(String token);

    /**
     * Metoda prin care se incearca sa se stearga un fisier de pe sistemul de fisiere al server-ului.
     *
     * @param path Calea absoluta a fisierului de pe server.
     * @param token Token-ul aferent fisierului incarcat pe server.
     * @return {@code true} in cazul in care fisierul a fost sters de pe sistemul de fisiere al server-ului sau
     *         {@code false} in caz contrar.
     */
    boolean deleteFile(String path, String token);
}
