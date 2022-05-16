package dev.renegade.bookmarkit.controller;

import java.util.List;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dev.renegade.bookmarkit.model.Bookmark;
import dev.renegade.bookmarkit.service.BookmarkService;

@RestController
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;
    
    @RequestMapping(value ="/bookmarks", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Bookmark> getAllBookmarks() {
        return  bookmarkService.list();
    }

    @RequestMapping(value="/bookmark/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Bookmark getBookmarkById(@RequestParam Long id) {
        return bookmarkService.getById(id);
    }

    @PostMapping(value ="/addBookmark")
    public void addBookmarks(@RequestBody Bookmark bookmark) {
        bookmarkService.addBookmark(bookmark);
    }

    @PostMapping(value="/addBookMarks")
    public void postMethodName(@RequestBody List<Bookmark> bookmarks) {
        bookmarkService.addBookmarks(bookmarks);
    }

    @PostMapping(value = "/deleteAll")
    public void deleteAll() {
        bookmarkService.deleteAllBookmarks();
    }

    @PostMapping(value = "/delete/{id}")
    public void deleteById(@PathVariable Long id){
        bookmarkService.deleteById(id);
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


