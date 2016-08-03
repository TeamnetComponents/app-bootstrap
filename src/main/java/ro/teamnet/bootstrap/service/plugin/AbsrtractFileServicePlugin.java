package ro.teamnet.bootstrap.service.plugin;

import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.FileItem;
import ro.teamnet.bootstrap.domain.FileMaster;
import ro.teamnet.bootstrap.holder.SessionHolder;
import ro.teamnet.bootstrap.plugin.upload.BaseFileService;
import ro.teamnet.bootstrap.plugin.upload.FileMasterReflection;
import ro.teamnet.bootstrap.plugin.upload.FileServicePlugin;
import ro.teamnet.bootstrap.repository.FileItemRepository;
import ro.teamnet.bootstrap.repository.FileMasterRepository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Oana.Mihai on 8/2/2016.
 */
public abstract class AbsrtractFileServicePlugin implements FileServicePlugin {

    @Inject
    SessionHolder sessionHolder;

    @Inject
    private FileMasterRepository fileMasterRepository;

    @Inject
    private FileItemRepository fileItemRepository;

    FileMaster getFileMaster(String groupId, String localGroupId) {
        FileMaster fileMaster;
        if (sessionHolder.containsKey(localGroupId)) {
            fileMaster = (FileMaster) sessionHolder.get(localGroupId);
            if (fileMaster.getFileItem() == null) {
                fileMaster.setFileItem(new CopyOnWriteArrayList<FileItem>());
            }
        } else {
            fileMaster = createFileMaster(groupId, localGroupId);
        }
        return fileMaster;
    }

    FileMaster createFileMaster(String groupId, String localGroupId) {
        FileMaster fileMaster = new FileMaster();
        fileMaster.setGroup(groupId);
        fileMaster.setFileItem(new CopyOnWriteArrayList<FileItem>());
        sessionHolder.put(localGroupId, fileMaster);
        return fileMaster;
    }

    @Override
    @Transactional
    public Serializable save(Serializable entity, String groupId, BaseFileService baseFileService) {
        String localGroupId = FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster = getFilesForGroup(localGroupId);
        if (fileMaster == null) {
            fileMaster = createFileMaster(groupId, localGroupId);
        }
        fileMaster.setCreated(new Date());
        FileMasterReflection.setFileMasterValue(entity, fileMaster);
        baseFileService.save(entity);
        cleanUpForGroup(groupId);
        return entity;
    }

    @Override
    @Transactional
    public Serializable update(Serializable entity, String groupId, BaseFileService baseFileService) {
        String localGroupId = FileUploadServiceUtil.getGroupFor(groupId);
        FileMaster fileMaster = getFilesForGroup(localGroupId);
        FileMaster fileMasterFromDb = fileMasterRepository.findOne(FileMasterReflection.getFileMasterValue(entity).getId());
        FileMasterReflection.setFileMasterValue(entity, fileMasterFromDb);
        if (fileMaster == null) {
            baseFileService.save(entity);
        } else {
            List<FileItem> persistedFileItems = new ArrayList<>();
            if (fileMaster.getFileItem() != null) {
                for (FileItem ff : fileMaster.getFileItem()) {
                    ff.setFileMaster(fileMasterFromDb);
                }
                persistedFileItems = fileItemRepository.save(fileMaster.getFileItem());
            }
            FileMasterReflection.updateFileMasterWithFiles(entity, persistedFileItems);
            baseFileService.save(entity);
            cleanUpForGroup(groupId);
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileItem> getAllFiles(Long fileMasterId) {
        return fileItemRepository.findByMasterId(fileMasterId);
    }

}
