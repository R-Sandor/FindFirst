package dev.findfirst.core.validation;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * Validator class to ensure that the uploaded bookmark file meets the required criteria.
 * It checks for file size, file type, and the structural validity of the HTML content.
 */
public class BookmarkImportFileValidator {

    private static final long MAX_FILE_SIZE_BYTES = 250L * 1024 * 1024; // 250 MB
    private static final String HTML_PATTERN = ".*<html.*?>.*</html>.*";

    /**
     * Validates the uploaded bookmark file by checking its size, type, and HTML structure.
     *
     * @param file MultipartFile uploaded by the user
     * @return True if the file is valid; False otherwise
     */
    public boolean isFileValid(MultipartFile file) throws IOException {
        return isFileSizeValid(file) && isFileTypeValid(file) && isFileStructureValid(file);
    }

    /**
     * Checks if the uploaded file is of a valid type (i.e., HTML).
     *
     * @param file MultipartFile uploaded by the user
     * @return True if the file type is "text/html"; False otherwise
     */
    private boolean isFileTypeValid(MultipartFile file) throws IOException {

        String fileType = file.getContentType();
        return fileType != null && fileType.equals("text/html");
    }

    /**
     * Checks if the uploaded file size does not exceed the maximum allowed limit.
     *
     * @param file MultipartFile uploaded by the user
     * @return True if the file size is less than or equal to 250 MB; False otherwise
     */
    private boolean isFileSizeValid(MultipartFile file) throws IOException {

        long fileSize = file.getSize();
        return fileSize <= MAX_FILE_SIZE_BYTES;
    }

    /**
     * Validates the structural integrity of the HTML content in the uploaded file.
     * It ensures that the content contains both opening and closing <html> tags.
     *
     * @param file MultipartFile uploaded by the user
     * @return True if the HTML structure is valid; False otherwise
     */
    private boolean isFileStructureValid(MultipartFile file) throws IOException {

        var fBytes = file.getBytes();
        String docStr = new String(fBytes, StandardCharsets.UTF_8);

        return docStr.matches(HTML_PATTERN);
    }
}
