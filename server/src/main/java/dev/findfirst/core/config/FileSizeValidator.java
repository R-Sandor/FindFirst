package dev.findfirst.core.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
  @Value("${findfirst.upload.max-file-size:}")
  private int maxFileSize;

  @Override
  public void initialize(FileSize constraintAnnotation) {}

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (file == null || file.isEmpty()) {
      return true;
    }
    return file.getSize() <= maxFileSize;
  }
}
