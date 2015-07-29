package ro.teamnet.bootstrap.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Radu.Hoaghe on 7/23/2015.
 */
@Entity
@Table(name = "T_SAVED_FILE_DETAILS")
public class SavedFileDetails {

    @Id
    @Column(name = "id_saved_file_details")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "fk_saved_file", referencedColumnName = "id_saved_file")
    private SavedFile fkSavedFile;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "created_date")
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SavedFile getFkSavedFile() {
        return fkSavedFile;
    }

    public void setFkSavedFile(SavedFile fkSavedFile) {
        this.fkSavedFile = fkSavedFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
