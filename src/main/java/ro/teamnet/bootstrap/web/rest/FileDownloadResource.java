package ro.teamnet.bootstrap.web.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.teamnet.bootstrap.domain.FileItem;
import ro.teamnet.bootstrap.plugin.upload.FileServicePlugin;
import ro.teamnet.bootstrap.plugin.upload.FileUploadType;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/app/rest/downloadFile")
public class FileDownloadResource {

    @Inject
    @Qualifier("fileServicePluginRegistry")
    private PluginRegistry<FileServicePlugin, FileUploadType> fileServicePlugins;

    @RequestMapping(value = "/fileInfo/{fileMasterId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FileItem> getAllFiles( @PathVariable("fileMasterId")Long fileMasterId){
        return fileServicePlugins.getPluginFor(FileUploadType.USER_SESSION).getAllFiles(fileMasterId);
    }


    @RequestMapping(value = "/doDownload/{groupId}/{token}", method = RequestMethod.GET)
    public void uploadFileHandler(HttpServletResponse response,
                                  @PathVariable("groupId") String groupId,
                                  @PathVariable("token") String token) {
        FileServicePlugin fileServicePlugin = fileServicePlugins.getPluginFor(FileUploadType.USER_SESSION);
        FileItem fileItem = fileServicePlugin.downloadFile(token, groupId);
        response.setContentType(fileItem.getType());
        response.setContentLength(fileItem.getSize().intValue());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                fileItem.getName());
        response.setHeader(headerKey, headerValue);

        try (
                OutputStream os = response.getOutputStream();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileItem.getContent())
        ) {
            org.apache.commons.io.IOUtils.copy(byteArrayInputStream, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
