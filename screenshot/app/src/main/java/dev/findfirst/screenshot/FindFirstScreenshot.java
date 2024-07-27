package dev.findfirst.screenshot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FindFirstScreenshot {
    public static void main(String[] args) {
        SpringApplication.run(FindFirstScreenshot.class, args);
    }
}