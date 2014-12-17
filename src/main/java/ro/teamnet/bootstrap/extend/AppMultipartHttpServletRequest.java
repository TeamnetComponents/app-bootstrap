package ro.teamnet.bootstrap.extend;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Collection;

/**
 * Created by Viorelt on 16.07.2014.
 */
public class AppMultipartHttpServletRequest extends StandardMultipartHttpServletRequest {

    private static final String CONTENT_DISPOSITION = "content-disposition";
    private static final String FILE_ID = "fileid";

    private static final String FILENAME_KEY = "filename=";



    private String extractFilename(String contentDisposition) {
        if (contentDisposition == null) {
            return null;
        }
        // TODO: can only handle the typical case at the moment
        int startIndex = contentDisposition.indexOf(FILENAME_KEY);
        if (startIndex == -1) {
            return null;
        }
        String filename = contentDisposition.substring(startIndex + FILENAME_KEY.length());
        if (filename.startsWith("\"")) {
            int endIndex = filename.indexOf("\"", 1);
            if (endIndex != -1) {
                return filename.substring(1, endIndex);
            }
        }
        else {
            int endIndex = filename.indexOf(";");
            if (endIndex != -1) {
                return filename.substring(0, endIndex);
            }
        }
        return filename;
    }


    /**
     * Create a new StandardMultipartHttpServletRequest wrapper for the given request.
     *
     * @param request the servlet request to wrap
     * @throws org.springframework.web.multipart.MultipartException if parsing failed
     */
    public AppMultipartHttpServletRequest(HttpServletRequest request) throws MultipartException {
        super(request);
        try {
            Collection<Part> parts = request.getParts();
            MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap<>(parts.size());
            for (Part part : parts) {
                String filename = extractFilename(part.getHeader(CONTENT_DISPOSITION));
                String fileId=part.getHeader(FILE_ID);
                String fileDescription=request.getParameter("description");
                if(fileId==null){
                    fileId=request.getParameter(FILE_ID);
                }
                if (filename != null) {
                    files.add(part.getName(), new AppMultipartFile(part, filename,fileId,fileDescription));
                }
            }
            setMultipartFiles(files);
        }
        catch (Exception ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }
}
