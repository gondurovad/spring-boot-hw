package com.besmart.controllers;

import com.besmart.model.User;
import com.besmart.services.MailSenderService;
import com.besmart.services.CSVFileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.besmart.services.CSVFileHandler.parseCSVtoUsers;

@Controller
public class UserController {

    @Autowired
    private MailSenderService sender;

    @GetMapping("/user")
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "enterUserInfo";
    }

    @PostMapping("/user")
    public String userSubmit(@ModelAttribute User user) {
        CSVFileHandler.writeUserToCSV(user);
        return "successfulSubmit";
    }

    @RequestMapping(value="/sendMsg")
    public String sendMsgToUser() {
        List<User> users = parseCSVtoUsers(CSVFileHandler.SCV_USERS);
        User user = users.get(users.size()-1);
        if (!StringUtils.isEmpty(user.getEmail())) {
            sendMessage(user);
            return "successfulSendMsg";
        } else {
            return "emailNotFound";
        }
    }

    public void sendMessage(User user){
        String message = String.format("Hello, %s! You are registered in our system.", user.getName());
        sender.sendMessage(user.getEmail(), "Registration message", message);
    }
}
