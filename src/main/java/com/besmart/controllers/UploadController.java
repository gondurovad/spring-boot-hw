package com.besmart.controllers;

import com.besmart.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.besmart.services.CSVFileHandler.parseCSVtoUsers;
import static com.besmart.services.CSVFileHandler.writeUserToCSV;

@Controller
public class UploadController {
    public static final String CSV_UPLOAD = "src/main/resources/files/upload.csv";

    @GetMapping("/uploadFile")
    public String showUploadFileForm() {
        return "uploadFile";
    }

    @PostMapping("/uploadFile")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        if(file.isEmpty()){
            return "uploadFileIsEmpty";
        }
        Path path = Paths.get(CSV_UPLOAD);
        try{
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<User> users = parseCSVtoUsers(CSV_UPLOAD);

        writeUserToCSV(users.get(0));
        deleteFile(path);
        model.addAttribute("file", file);
        return "successfulUpload";
    }

    public void deleteFile(Path path){
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
