package ro.teamnet.bootstrap.service.plugin;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ro.teamnet.bootstrap.domain.FileItem;
import ro.teamnet.bootstrap.domain.FileMaster;
import ro.teamnet.bootstrap.domain.UploadFileLog;
import ro.teamnet.bootstrap.holder.SessionHolder;
import ro.teamnet.bootstrap.plugin.upload.BaseFileService;
import ro.teamnet.bootstrap.plugin.upload.FileMasterReflection;
import ro.teamnet.bootstrap.plugin.upload.FileServicePlugin;
import ro.teamnet.bootstrap.plugin.upload.FileUploadType;
import ro.teamnet.bootstrap.repository.FileItemRepository;
import ro.teamnet.bootstrap.repository.FileMasterRepository;
import ro.teamnet.bootstrap.service.UploadFileLogService;

import javax.inject.Inject;
import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unchecked")
@Service
public class FileSystemServicePlugin implements FileServicePlugin {

    private final Logger log = LoggerFactory.getLogger(FileSystemServicePlugin.class);

    @Inject
    private UploadFileLogService uploadFileLogService;

    @Inject
    private SessionHolder sessionHolder;

    @Inject
    private FileItemRepository fileItemRepository;

    @Inject
    private FileMasterRepository fileMasterRepository;


    @Override
    @Transactional
    public String uploadFile(MultipartFile file,String groupId) {
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);

        if (!file.isEmpty()) {
            try {
                File dir = createDirectory();
                String absolutePath = createFile(dir,file);

                String token= uploadFileLogService.saveInUploadLog(absolutePath);
                FileMaster fileMaster;
                if(sessionHolder.containsKey(localGroupId)){
                    fileMaster =(FileMaster)sessionHolder.get(localGroupId);
                    if(fileMaster.getFileItem()==null){
                        fileMaster.setFileItem(new CopyOnWriteArrayList<FileItem>());
                    }


                }else{
                    fileMaster =new FileMaster();
                    fileMaster.setGroup(groupId);
                    fileMaster.setFileItem(new CopyOnWriteArrayList<FileItem>());
                    sessionHolder.put(localGroupId, fileMaster);
                }

                FileItem fileItem =new FileItem();
                fileItem.setToken(token);

                fileMaster.getFileItem().add(fileItem);

                return token;

            } catch (Exception e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getOriginalFilename()
                    + " because the file was empty.";
        }
    }

    @Override
    @Transactional
    public FileMaster getFilesForGroup(String groupId) {
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster =null;
        if(sessionHolder.containsKey(localGroupId)){
            fileMaster = (FileMaster) sessionHolder.get(localGroupId);
            List<FileItem> fileItems = fileMaster.getFileItem();
            List<FileItem> newFileItems =new CopyOnWriteArrayList<>();
            for (FileItem fileItem : fileItems) {
                FileItem fileItem1 =getFile(fileItem.getToken());
                fileItem1.setFileMaster(fileMaster);
                newFileItems.add(fileItem1);
            }
            fileMaster.setFileItem(newFileItems);
        }

        return fileMaster;
    }


    private FileItem getFile(String token) {
        UploadFileLog uploadFileLog=uploadFileLogService.findByToken(token);
        FileItem fileItem =new FileItem();
        File file=new File(uploadFileLog.getFileLocation());
        if(!file.exists()){
            return null;
        }
        String contentType;
        fileItem.setName(file.getName());
        byte[] content;
        try {
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            content=IOUtils.toByteArray(is);
            contentType = URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileItem.setType(contentType);
        fileItem.setContent(content);
        fileItem.setSize((long) content.length);
        fileItem.setToken(token);
        return fileItem;
    }

    public void cleanUp(String token,String groupId){
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster = (FileMaster) sessionHolder.get(localGroupId);
        List<FileItem> fileItems = fileMaster.getFileItem();
        for (FileItem fileItem : fileItems) {
            if(fileItem.getToken().equals(token)){
                fileItems.remove(fileItem);
                UploadFileLog uploadFileLog=uploadFileLogService.findByToken(token);
                File file=new File(uploadFileLog.getFileLocation());
                boolean deleted=file.delete();
                if(!deleted){
                    throw new RuntimeException("The file cannot be deleted");
                }
                uploadFileLogService.delete(uploadFileLog.getId());
                return;
            }
        }
    }


    private void cleanUpAll(String groupId) {
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster = (FileMaster) sessionHolder.get(localGroupId);
        List<FileItem> fileItems = fileMaster.getFileItem();
        for (FileItem fileItem : fileItems) {
           String token= fileItem.getToken();
           UploadFileLog uploadFileLog=uploadFileLogService.findByToken(token);
            File file=new File(uploadFileLog.getFileLocation());
            boolean deleted=file.delete();
            if(!deleted){
                throw new RuntimeException("The file cannot be deleted");
            }
            uploadFileLogService.delete(uploadFileLog.getId());
        }

    }

    @Override
    public void cleanUpForGroup(String groupId) {
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        cleanUpAll(localGroupId);
        sessionHolder.remove(localGroupId);
    }

    @Override
    @Transactional
    public Serializable save(Serializable entity, String groupId, BaseFileService baseFileService) {
        String localGroupId=FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster =getFilesForGroup(localGroupId);
        fileMaster.setCreated(new Date());
        FileMasterReflection.setFileMasterValue(entity, fileMaster);
        baseFileService.save(entity);
        cleanUpForGroup(groupId);
        return entity;
    }

    @Override
    @Transactional
    public Serializable update(Serializable entity, String groupId, BaseFileService baseFileService) {
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
                    FileItem fileItem1= getFile(token);
                    if(fileItem1!=null){
                        return fileItem1;
                    }else{
                        break;
                    }
                }
            }
        }



        return fileItemRepository.findByTokenAndGroup(token,groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileItem> getAllFiles(Long fileMasterId) {
        return fileItemRepository.findByMasterId(fileMasterId);
    }


    /**
     * Metoda prin care se creeaza un director pe sistemul de fisiere al server-ului.
     * @return Referinta catre director.
     * @throws Exception
     */
    private File createDirectory() throws Exception{

        String rootPath = System.getProperty("user.dir");

        File dir = new File(rootPath + File.separator + "uploadedFiles");
        if (!dir.exists())
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();

        return dir;
    }

    /**
     * Metoda prin care se creeaza un fisier pe sistemul de fisiere al server-ului.
     *
     * @param dir Directorul in care se va crea fisierul.
     * @param file Fisierul care se va crea pe sistemul de fisiere al server-ului.
     * @return Calea absoluta a fisierului.
     * @throws Exception
     */
    private String createFile(File dir, MultipartFile file ) throws Exception{

        byte[] bytes = file.getBytes();
        File serverFile = new File(dir.getAbsolutePath()
                + File.separator + file.getOriginalFilename()); // add ( timespam/ + user ) => hashcode - pentru download
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        log.info("Server File Location="
                + serverFile.getAbsolutePath());

        return serverFile.getAbsolutePath();
    }

    @Override
    public boolean supports(FileUploadType delimiter) {
        return delimiter==FileUploadType.FILE_SYSTEM;
    }


}
