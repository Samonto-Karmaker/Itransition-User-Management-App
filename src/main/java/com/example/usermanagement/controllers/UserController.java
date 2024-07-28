package com.example.usermanagement.controllers;

import com.example.usermanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/admin-panel")
    public String getAdminPanelPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-management";
    }

    @PutMapping("/block-users")
    public String blockUsers(@RequestBody List<Long> userIds) {
        if (userService.blockUsers(userIds)) return "redirect:/login";
        return "redirect:/users/admin-panel";
    }

    @PutMapping("/unblock-users")
    public String unblockUsers(@RequestBody List<Long> userIds) {
        userService.unblockUsers(userIds);
        return "redirect:/users/admin-panel";
    }

    @DeleteMapping("/delete-users")
    public String deleteUsers(@RequestBody List<Long> userIds) {
        System.out.println("deleteUsers");
        if (userService.deleteUsers(userIds)) return "redirect:/login";
        return "redirect:/users/admin-panel";
    }

}
