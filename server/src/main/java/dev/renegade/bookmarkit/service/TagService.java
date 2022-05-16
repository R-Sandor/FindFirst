package dev.renegade.bookmarkit.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import dev.renegade.bookmarkit.model.Tag;
import dev.renegade.bookmarkit.repository.TagRepository;

public class TagService {

    @Autowired TagRepository tagRepository;

    public void addTag(Tag tag){
        tagRepository.saveAndFlush(tag);
    }

    public void addAll(List<Tag> tags){
        tagRepository.saveAllAndFlush(tags);
    }

    public void deleteAll(){
        tagRepository.deleteAll();
    }

    public void deleteTag(Tag tag){
        tagRepository.delete(tag);
    }

    public List<Tag> getTags(){
        return tagRepository.findAll();
    }
}
