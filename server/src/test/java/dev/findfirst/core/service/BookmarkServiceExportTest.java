package dev.findfirst.core.service;


import dev.findfirst.core.dto.BookmarkOnly;
import dev.findfirst.core.dto.TagDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 *  Example test for verifying the export() method in the BookmarkService.
 */
@ExtendWith(MockitoExtension.class)
class BookmarkServiceExportTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private BookmarkService bookmarkService;


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testExportEmpty() {

        when(tagService.getTags()).thenReturn(new ArrayList<>()); // No tags

        String exportResult = bookmarkService.export();

        assertTrue(
                exportResult.contains("<TITLE>FindFirst Bookmarks</TITLE>"),
                "Export should contain the main skeleton with 'FindFirst Bookmarks'."
        );
        assertTrue(
                exportResult.endsWith("\n</DL>"),
                "Should end properly with the last </DL> tag."
        );
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testExportWithSampleTagsAndBookmarks() {
        // Arrange
        var dateNow = new Date();

        // Create some Bookmarks
        BookmarkOnly bkmk1 = new BookmarkOnly(
                101L,
                "Cheesecake Recipe",
                "https://cheesecake.example.com",
                "",
                true,
                dateNow,
                dateNow
        );
        BookmarkOnly bkmk2 = new BookmarkOnly(
                102L,
                "Dark Mode Guide",
                "https://blog.example.com/dark-mode",
                "",
                true,
                dateNow,
                dateNow
        );

        // Tag #1
        List<BookmarkOnly> cookingBkmks = List.of(bkmk1);
        TagDTO cookingTag = new TagDTO(
                1L,
                "Cooking",
                cookingBkmks // Typically this is List<BookmarkOnly>
        );

        // Tag #2
        List<BookmarkOnly> webdevBkmks = List.of(bkmk2);
        TagDTO webdevTag = new TagDTO(
                2L,
                "web_dev",
                webdevBkmks
        );

        // Return them from the tagService
        when(tagService.getTags()).thenReturn(List.of(cookingTag, webdevTag));

        // Act
        String exportResult = bookmarkService.export();

        // Assert
        // Basic checks
        assertTrue(
                exportResult.contains("<TITLE>FindFirst Bookmarks</TITLE>"),
                "Should contain the main HTML skeleton"
        );
        assertTrue(
                exportResult.contains("Cooking</H3>"),
                "Should have the 'Cooking' tag category"
        );
        assertTrue(
                exportResult.contains("web_dev</H3>"),
                "Should have the 'web_dev' tag category"
        );
        assertTrue(
                exportResult.contains("https://cheesecake.example.com"),
                "Should list the Cheesecake link"
        );
        assertTrue(
                exportResult.contains("https://blog.example.com/dark-mode"),
                "Should list the Dark Mode link"
        );
    }
}
