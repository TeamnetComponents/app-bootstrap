package ro.teamnet.bootstrap.extend;

import org.springframework.util.Assert;

import javax.servlet.MultipartConfigElement;

/**
 * Clasa de tip fabrica pentru a crea configurarea Multipart
 */
public class MultiPartConfigFactory {

    private String location;

    private long maxFileSize = -1;

    private long maxRequestSize = -1;

    private int fileSizeThreshold = 0;

    /**
     * Seteaza directorul unde vor fi stocate fisierele
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Seteaza marimea maxima a fisierelor incarcate
     * @see #setMaxFileSize(String)
     */
    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * Seteaza marimea maxima a fisierelor incarcate. Valorile pot fi sufixate cu "MB"
     * sau "KB" pentru a indica marimile Megabyte sau Kilobyte.
     * @see #setMaxFileSize(long)
     */
    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = parseSize(maxFileSize);
    }

    /**
     * Seteaza marimea maxima a cererilor de tip multipart/form-data
     * @see #setMaxRequestSize(String)
     */
    public void setMaxRequestSize(long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    /**
     * Seteaza marimea maxima a cererilor de tip multipart/form-data. Valorile pot fi sufixate cu "MB"
     * sau "KB" pentru a indica marimile Megabyte sau Kilobyte.
     * @see #setMaxRequestSize(long)
     */
    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = parseSize(maxRequestSize);
    }

    /**
     * Seteaza pragul marimii dupa care fisierele for fi scrise pe disc.
     * @see #setFileSizeThreshold(String)
     */
    public void setFileSizeThreshold(int fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
    }

    /**
     * Seteaza pragul marimii dupa care fisierele for fi scrise pe disc. Valorile pot fi sufixate cu "MB"
     * sau "KB" pentru a indica marimile Megabyte sau Kilobyte.
     * @see #setFileSizeThreshold(int)
     */
    public void setFileSizeThreshold(String fileSizeThreshold) {
        this.fileSizeThreshold = (int) parseSize(fileSizeThreshold);
    }

    /**
     * Analizeaza marimea trimisa ca sir de caractere
     * @param size marimea ca sir de caractere
     * @return marimea ca numar
     */
    private long parseSize(String size) {
        Assert.hasLength(size, "Size must not be empty");
        size = size.toUpperCase();
        if (size.endsWith("KB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024;
        }
        if (size.endsWith("MB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024 * 1024;
        }
        return Long.valueOf(size);
    }

    /**
     * Creaza o instanta noua de {@link javax.servlet.MultipartConfigElement}.
     */
    public MultipartConfigElement createMultipartConfig() {
        return new MultipartConfigElement(this.location, this.maxFileSize,
                this.maxRequestSize, this.fileSizeThreshold);
    }

}
