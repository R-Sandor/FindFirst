package dev.renegade.bookmarkit.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import dev.renegade.bookmarkit.model.Bookmark;
import dev.renegade.bookmarkit.model.Tag;
import dev.renegade.bookmarkit.repository.BookmarkRepository;
import dev.renegade.bookmarkit.repository.TagRepository;
import dev.renegade.bookmarkit.service.BookmarkService;

@RestController
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;
    
    @RequestMapping(value ="/bookmarks", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Bookmark> getAllBookmarks() {
        List<Bookmark> bList =  bookmarkService.list();
        return bList;
    }
}

// @Autowired
// private BookmarkRepository bookmarkRepository;

// @GetMapping("/bookmarks/{bookmarkId}/tags")
// public ResponseEntity<List<Tag>> getAllCommentsByTutorialId(@PathVariable(value = "bookmarkId")
// Long bookmarkId) {
// System.out.println("HERE");
// if (!bookmarkRepository.existsById(bookmarkId)) {
// throw new ResourceNotFoundException("Not found Tutorial with id = " + bookmarkId);
// }
// System.out.println(bookmarkId);

// List<Tag> tags = tagRepository.findByBookmarkId(bookmarkId);
// System.out.println(tags);
// return new ResponseEntity<>(tags, HttpStatus.OK);
// }

// @GetMapping("/tags/{id}")
// public ResponseEntity<Tag> getTagByBookmarkId(@PathVariable(value = "id") Long id) {
// Tag tag = tagRepository.findById(id)
// .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));

// return new ResponseEntity<>(tag, HttpStatus.OK);
// }


