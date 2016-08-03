package ro.teamnet.bootstrap.service.plugin;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ro.teamnet.bootstrap.domain.FileItem;
import ro.teamnet.bootstrap.domain.FileMaster;
import ro.teamnet.bootstrap.holder.SessionHolder;
import ro.teamnet.bootstrap.plugin.upload.BaseFileService;
import ro.teamnet.bootstrap.plugin.upload.FileMasterReflection;
import ro.teamnet.bootstrap.plugin.upload.FileServicePlugin;
import ro.teamnet.bootstrap.plugin.upload.FileUploadType;
import ro.teamnet.bootstrap.repository.FileItemRepository;
import ro.teamnet.bootstrap.repository.FileMasterRepository;
import ro.teamnet.bootstrap.repository.TokenGenerator;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unchecked")
@Service
public class SessionFileServicePlugin extends AbsrtractFileServicePlugin {

    @Inject
    private SessionHolder sessionHolder;

    private TokenGenerator tokenGenerator = new TokenGenerator();

    @Inject
    private FileItemRepository fileItemRepository;

    @Inject
    private FileMasterRepository fileMasterRepository;


    @Override
    public String uploadFile(MultipartFile multipartFile, String groupId) {
        String token = "";
        String localGroupId = FileUploadServiceUtil.getGroupFor(groupId);
        try {
            token = tokenGenerator.nextSessionId();
            FileItem fileItem = FileItem.from(multipartFile, token);
            FileMaster fileMaster = getFileMaster(groupId, localGroupId);
            fileItem.setFileMaster(fileMaster);
            fileMaster.getFileItem().add(fileItem);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }



    @Override
    public FileMaster getFilesForGroup(String groupId) {

        String localGroupId = FileUploadServiceUtil.getGroupFor(groupId);
        if (sessionHolder.containsKey(localGroupId)) {
            return (FileMaster) sessionHolder.get(localGroupId);
        } else {
            return null;
        }

    }


    @Override
    public void cleanUpForGroup(String groupId) {
        String localGroupId = FileUploadServiceUtil.getGroupFor(groupId);
        sessionHolder.remove(localGroupId);
    }

    @Override
    public void cleanUp(String token, String groupId) {
        String localGroupId = FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster = (FileMaster) sessionHolder.get(localGroupId);
        List<FileItem> fileItems = fileMaster.getFileItem();
        for (FileItem fileItem : fileItems) {
            if (fileItem.getToken().equals(token)) {
                fileItems.remove(fileItem);
                return;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FileItem downloadFile(String token, String groupId) {

        //finding file in user session
        FileMaster fileMaster = getFilesForGroup(groupId);
        if (fileMaster != null) {
            for (FileItem fileItem : fileMaster.getFileItem()) {
                if (fileItem.getToken().equals(token)) {
                    return fileItem;
                }
            }
        }

        //finding file in repository
        return fileItemRepository.findByTokenAndGroup(token, groupId);
    }




    @Override
    public boolean supports(FileUploadType delimiter) {
        return delimiter == FileUploadType.USER_SESSION;
    }
}
