package dev.findfirst.core.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.findfirst.core.dto.BookmarkOnly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for verifying that ExportBookmark correctly constructs an HTML bookmark file based on the
 * provided TagBookmarks data.
 */
class ExportBookmarkTest {

  /**
   * Ensures ExportBookmark outputs the minimal skeleton when given no tags.
   */
  @Test
  @DisplayName("ExportBookmark -> no tags yields minimal skeleton with closing </DL>")
  void testExportBookmarkWithNoTags() {
    // Arrange
    List<TagBookmarks> emptyTagList = new ArrayList<>();

    // Act
    ExportBookmark exporter = new ExportBookmark(emptyTagList);
    String htmlResult = exporter.toString();

    // Assert
    assertTrue(htmlResult.contains("<TITLE>FindFirst Bookmarks</TITLE>"),
        "Should contain the main HTML skeleton with 'FindFirst Bookmarks' title.");
    assertTrue(htmlResult.endsWith("\n</DL>"), "Should end with the closing </DL> tag.");
  }

  /**
   * Ensures ExportBookmark outputs a category
   * <H3>and <A> links for multiple bookmarks under a single Tag.
   */
  @Test
  @DisplayName("ExportBookmark -> single tag with multiple bookmarks yields correct HTML blocks")
  void testExportBookmarkWithOneTagAndBookmarks() {
    // Arrange
    var dateNow = new Date();
    var bkmk1 = new BookmarkOnly(1, "First Bookmark", "http://example.com", "", // screenshotUrl
        true, // scrapable
        dateNow, dateNow);
    var bkmk2 = new BookmarkOnly(2, "Second Bookmark", "https://another-example.org", "", true,
        dateNow, dateNow);

    var cookingTag = new TagBookmarks("Cooking", List.of(bkmk1, bkmk2));
    List<TagBookmarks> data = List.of(cookingTag);

    // Act
    ExportBookmark exporter = new ExportBookmark(data);
    String htmlResult = exporter.toString();

    // Assert
    assertTrue(htmlResult.contains("<H3 ADD_DATE=\""), "Should contain the H3 tag with ADD_DATE.");
    assertTrue(htmlResult.contains("Cooking</H3>"),
        "Should contain the Tag Title in the category header.");
    // The Links
    assertTrue(htmlResult.contains("<A HREF=\"http://example.com\""),
        "Should contain a link for the first bookmark");
    assertTrue(htmlResult.contains("<A HREF=\"https://another-example.org\""),
        "Should contain a link for the second bookmark");
    // Full skeleton checks
    assertTrue(htmlResult.contains("<TITLE>FindFirst Bookmarks</TITLE>"),
        "Should contain the main HTML skeleton");
  }
}
