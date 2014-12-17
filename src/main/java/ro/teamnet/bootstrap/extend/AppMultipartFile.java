package ro.teamnet.bootstrap.extend;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class AppMultipartFile implements MultipartFile {
    private final Part part;

    private final String filename;

    private final String id;

    private String description;

    public AppMultipartFile(Part part, String filename,String id) {
        this.part = part;
        this.filename = filename;
        this.id=id;
    }

    public AppMultipartFile(Part part, String filename, String id, String description) {
        this.part = part;
        this.filename = filename;
        this.id = id;
        this.description = description;
    }

    @Override
    public String getName() {
        return this.part.getName();
    }

    @Override
    public String getOriginalFilename() {
        return this.filename;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getContentType() {
        return this.part.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return (this.part.getSize() == 0);
    }

    @Override
    public long getSize() {
        return this.part.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return FileCopyUtils.copyToByteArray(this.part.getInputStream());
    }

    public String getDescription() {
        return description;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.part.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        this.part.write(dest.getPath());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppMultipartFile that = (AppMultipartFile) o;

        if (filename != null ? !filename.equals(that.filename) : that.filename != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
