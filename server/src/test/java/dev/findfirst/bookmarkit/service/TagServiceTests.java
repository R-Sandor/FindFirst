package dev.findfirst.bookmarkit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.junit.jupiter.api.Test;
import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.model.TagCntRecord;

@SpringBootTest
public class TagServiceTests {
     @Autowired private TagService tagService;

     @Test
     void getNumberOfBookmarksThatBelongToEachTag() { 
        // TODO: test our results 
        tagService.getTagsWithCnt();
     }
}
