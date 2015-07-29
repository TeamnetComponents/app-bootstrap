package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.UploadFileLog;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Created by Radu.Hoaghe on 7/16/2015.
 */
public interface UploadFileLogRepository extends AppRepository<UploadFileLog, Long> {

    /**
     * Metoda prin care se aduc din baza de date detalii despre un fisier incarcat.
     *
     * @param token Token-ul aferent fisierului incarcat.
     * @return Un obiect {@link UploadFileLog} care contine detalii despre fisier.
     */
    @Query("select ufl from UploadFileLog ufl where ufl.token = :token")
    UploadFileLog findByToken(@Param("token") String token);
}
