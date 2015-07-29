package ro.teamnet.bootstrap.web.rest.dto;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Radu.Hoaghe on 7/20/2015.
 */

/**
 * Clasa prin care se creeaza obiecte immutable care prin care se retin informatiile
 * aferente unui token (calea fisierului salvat pe server si data de expirare a token-ului)
 */
public final class UploadFileLogDTO {

    private String filePath;
    private DateTime expiringDate;

    public UploadFileLogDTO(String filePath, DateTime expiringDate){
        this.filePath = filePath;
        this.expiringDate = expiringDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public DateTime getExpiringDate() {
        return expiringDate;
    }
}
