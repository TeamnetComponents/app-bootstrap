package ro.teamnet.bootstrap.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Radu.Hoaghe
 */
@Entity
@Table(name = "T_UPLOAD_FILE_LOG")
public class UploadFileLog implements Serializable{

    @Id
    @Column(name = "id_upload_file_log")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "file_location")
    private String fileLocation;

    @Column(name = "token")
    private String token;

    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "status")
    private Long status;

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }
}
