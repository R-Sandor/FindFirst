package dev.findfirst.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.findfirst.core.dto.BookmarkOnly;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ExportBookmarkTest {

    @Test
     void testExportBookmarkWithNoTags() {

        List<TagBookmarks> emptyTagList = new ArrayList<>();

        ExportBookmark exporter = new ExportBookmark(emptyTagList);
        String htmlResult = exporter.toString();

        // Assert
        assertTrue(
                htmlResult.contains("<TITLE>FindFirst Bookmarks</TITLE>"),
                "Should contain the main HTML skeleton with 'FindFirst Bookmarks' title."
        );
        // Validate we ended with </DL> etc. and no extra content
        assertTrue(
                htmlResult.endsWith("\n</DL>"),
                "Should end with the closing </DL> tag"
        );
    }

    @Test
    void testExportBookmarkWithOneTagAndBookmarks() {
        // Arrange
        var dateNow = new Date();
        // Create a few sample bookmarks
        var bkmk1 = new BookmarkOnly(
                1,
                "First Bookmark",
                "http://example.com",
                "", // screenshotUrl
                true,  // scrapable
                dateNow,
                dateNow
        );
        var bkmk2 = new BookmarkOnly(
                2,
                "Second Bookmark",
                "https://another-example.org",
                "",
                true,
                dateNow,
                dateNow
        );

        var cookingTag = new TagBookmarks("Cooking", List.of(bkmk1, bkmk2));
        List<TagBookmarks> data = List.of(cookingTag);

        ExportBookmark exporter = new ExportBookmark(data);
        String htmlResult = exporter.toString();

        // Verify that the category was inserted
        assertTrue(
                htmlResult.contains("<H3 ADD_DATE=\""),
                "Should contain the H3 tag with ADD_DATE."
        );
        assertTrue(
                htmlResult.contains("Cooking</H3>"),
                "Should contain the Tag Title in the category header."
        );
        // The Links
        assertTrue(
                htmlResult.contains("<A HREF=\"http://example.com\""),
                "Should contain a link for the first bookmark"
        );
        assertTrue(
                htmlResult.contains("<A HREF=\"https://another-example.org\""),
                "Should contain a link for the second bookmark"
        );
        // Ensure the text has the full HTML skeleton
        assertTrue(
                htmlResult.contains("<TITLE>FindFirst Bookmarks</TITLE>"),
                "Should contain the main HTML skeleton"
        );
    }
}
