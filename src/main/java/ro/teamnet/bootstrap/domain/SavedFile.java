package ro.teamnet.bootstrap.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by Radu.Hoaghe on 7/23/2015.
 */
@Entity
@Table(name = "T_SAVED_FILE")
public class SavedFile {

    @Id
    @Column(name = "id_saved_file")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Basic
    @Column(name = "content")
    private byte[] fileContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
