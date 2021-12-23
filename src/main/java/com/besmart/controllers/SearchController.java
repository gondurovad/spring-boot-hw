package com.besmart.controllers;

import com.besmart.model.User;
import com.besmart.services.CSVFileHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.besmart.services.CSVFileHandler.findByFullName;
import static com.besmart.services.CSVFileHandler.parseCSVtoUsers;

@Controller
public class SearchController {
    @GetMapping("/searchUser")
    public String showSearchForm(Model model) {
        model.addAttribute("user", new User());
        return "searchUser";
    }

    @PostMapping("/searchUser")
    public String getUserData(User user, Model model) {
        List<User> users = parseCSVtoUsers(CSVFileHandler.SCV_USERS);
        int index = findByFullName(users, user.getName(), user.getSurname());
        if(index >= 0){
            model.addAttribute("user", users.get(index));
            return "userInfo";
        }
        return "userNotFound";
    }
}
