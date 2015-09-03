package ro.teamnet.bootstrap.service;

import ro.teamnet.bootstrap.plugin.upload.BaseFileService;
import ro.teamnet.bootstrap.repository.BaseFileRepository;

import java.io.Serializable;


public abstract class BaseFileServiceImpl<T extends Serializable> extends AbstractServiceImpl<T, Long> implements BaseFileService<T> {


    private BaseFileRepository baseFileRepository;

    public BaseFileServiceImpl(BaseFileRepository<T> baseFileRepository) {
        super(baseFileRepository);
    }
}
