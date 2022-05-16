package dev.renegade.bookmarkit.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.renegade.bookmarkit.model.Tag;
import dev.renegade.bookmarkit.service.TagService;

@RestController
public class TagController {

    @Autowired TagService tagService;

    @PostMapping(value = "/addTag/{tag}")
    public void addTag(@PathVariable Tag tag){
        tagService.addTag(tag);   
    }
    
    @PostMapping(value =  "/addTags/{tags}")
    public void addTags(List<Tag> tags){
        tagService.addAll(tags);
    }

    @PostMapping(value = "/deleteAll")
    public void deleteAll(){
        tagService.deleteAll();
    }

    @GetMapping(value = "/tags") 
    public List<Tag> getTags(){
        return tagService.getTags();
    }

}