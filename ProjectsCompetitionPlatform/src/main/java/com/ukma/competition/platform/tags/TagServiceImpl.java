package com.ukma.competition.platform.tags;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends GenericServiceImpl<TagEntity, String, TagRepository> implements TagService {

    @Autowired
    public TagServiceImpl(TagRepository repository) {
        super(repository);
    }
}
