
package dev.renegade.bookmarkit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import dev.renegade.bookmarkit.model.Bookmark;
import dev.renegade.bookmarkit.model.Tag;
import dev.renegade.bookmarkit.repository.BookmarkRepository;
import dev.renegade.bookmarkit.repository.TagRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired 
    private TagRepository tagRepository;

    public List<Bookmark> list() {
        return bookmarkRepository.findAll();
    }

    public Bookmark getById(Long id) {
        return bookmarkRepository.getById(id);
    }

    public void addBookmark(Bookmark bookmark){
        bookmarkRepository.saveAndFlush(bookmark);
    }

    public void addBookmarks(List<Bookmark> bookmarks){
        bookmarkRepository.saveAllAndFlush(bookmarks);
    }

    public void deleteBookmark(Bookmark bookmark) {
        bookmarkRepository.delete(bookmark);
    }

    public void deleteAllBookmarks(){
        bookmarkRepository.deleteAll();
    }

    public void deleteById(Long id){
        bookmarkRepository.deleteById(id);
    }

    public ResponseEntity<Tag> addTag(Long bookmarkId, Tag tagRequest){
        // TODO: CHECK if the tag already exists

        Optional<Bookmark> bkmkOpt = bookmarkRepository.findById(bookmarkId);
        if (bkmkOpt.isPresent()){
        Bookmark bookmark = bkmkOpt.get();
       long tagId = tagRequest.getId();

       // tag is existed
       if (tagId != 0L) {
         Tag _tag = tagRepository.findById(tagId)
             .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id = " + tagId));
         bookmark.addTag(_tag);
         bookmarkRepository.save(bookmark);
         return new ResponseEntity<>(_tag, HttpStatus.CREATED);
       }
    
       tagRequest.setBookmarks(new HashSet<>(Arrays.asList(bookmark)));
       // add and create new Tag
       bookmark.addTag(tagRequest);
       tagRepository.save(tagRequest);
    }    
    return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}