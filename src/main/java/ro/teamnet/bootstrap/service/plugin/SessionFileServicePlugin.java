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
public class SessionFileServicePlugin implements FileServicePlugin {

    @Inject
    private SessionHolder sessionHolder;

    private TokenGenerator tokenGenerator = new TokenGenerator();

    @Inject
    private FileItemRepository fileItemRepository;

    @Inject
    private FileMasterRepository fileMasterRepository;




    @Override
    public String uploadFile(MultipartFile multipartFile,String groupId) {
        String token="";
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        try {
            token=tokenGenerator.nextSessionId();
            FileItem fileItem = FileItem.from(multipartFile, token);
            FileMaster fileMaster;
            if(sessionHolder.get(localGroupId)==null){
                fileMaster =new FileMaster();
                fileMaster.setGroup(groupId);
                fileMaster.setFileItem(new CopyOnWriteArrayList<FileItem>());
                sessionHolder.put(localGroupId, fileMaster);
            }else{
                fileMaster = (FileMaster) sessionHolder.get(localGroupId);
                if(fileMaster.getFileItem()==null){
                    fileMaster.setFileItem(new CopyOnWriteArrayList<FileItem>());
                }
            }
            fileItem.setFileMaster(fileMaster);
            fileMaster.getFileItem().add(fileItem);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public FileMaster getFilesForGroup(String groupId) {

        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        if(sessionHolder.containsKey(localGroupId)){
            return (FileMaster) sessionHolder.get(localGroupId);
        }else{
            return null;
        }

    }



    @Override
    public void cleanUpForGroup(String groupId) {
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        sessionHolder.remove(localGroupId);
    }

    @Override
    public void cleanUp(String token,String groupId){
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster = (FileMaster) sessionHolder.get(localGroupId);
        List<FileItem> fileItems = fileMaster.getFileItem();
        for (FileItem fileItem : fileItems) {
            if(fileItem.getToken().equals(token)){
                fileItems.remove(fileItem);
                return;
            }
        }
    }

    @Transactional
    public Serializable save(Serializable entity,String groupId,BaseFileService baseFileService){
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster =getFilesForGroup(localGroupId);
        fileMaster.setCreated(new Date());
        FileMasterReflection.setFileMasterValue(entity, fileMaster);
        baseFileService.save(entity);
        cleanUpForGroup(groupId);
        return entity;

    }
    @Transactional
    public Serializable update(Serializable entity,String groupId,BaseFileService baseFileService){
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster =getFilesForGroup(localGroupId);
        FileMaster fileMasterFromDb=fileMasterRepository.findOne(FileMasterReflection.getFileMasterValue(entity).getId());
        FileMasterReflection.setFileMasterValue(entity,fileMasterFromDb);
        if(fileMaster==null){
            baseFileService.save(entity);
        }else{
            List<FileItem> persistedFileItems=new ArrayList<>();
            if(fileMaster.getFileItem()!=null){
                for (FileItem ff : fileMaster.getFileItem()) {
                    ff.setFileMaster(fileMasterFromDb);
                }
                persistedFileItems=fileItemRepository.save(fileMaster.getFileItem());
            }
            FileMasterReflection.updateFileMasterWithFiles(entity, persistedFileItems);
            baseFileService.save(entity);
            cleanUpForGroup(groupId);
        }

        return entity;

    }

    @Override
    @Transactional(readOnly = true)
    public FileItem downloadFile(String token, String groupId) {

        //finding file in user session
        FileMaster fileMaster=getFilesForGroup(groupId);
        if(fileMaster!=null){
            for (FileItem fileItem : fileMaster.getFileItem()) {
                if(fileItem.getToken().equals(token)){
                    return fileItem;
                }
            }
        }

        //finding file in repository
        return fileItemRepository.findByTokenAndGroup(token,groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileItem> getAllFiles(Long fileMasterId) {
        return fileItemRepository.findByMasterId(fileMasterId);
    }


    @Override
    public boolean supports(FileUploadType delimiter) {
        return delimiter==FileUploadType.USER_SESSION;
    }
}
