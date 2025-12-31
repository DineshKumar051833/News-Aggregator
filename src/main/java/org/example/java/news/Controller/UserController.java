package org.example.java.news.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.java.news.Model.User;
import org.example.java.news.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession httpSession;

    public UserController(UserService userService, HttpSession httpSession){
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "Login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model){
        model.addAttribute("user",new User());
        return "SignUp";
    }

    @PostMapping("/loginUser")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model){
        var user = userService.getUserByEmailAndPassword(email,password);
        if(user.isEmpty()){
            model.addAttribute("Error", "Invalid Credentials");
            return "Login";
        }
        httpSession.setAttribute("user",user.get());
        return "redirect:/Home.html";
    }

    @PostMapping("/signupUser")
    public String signupUser(@ModelAttribute("user") User user, Model model) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("Error", "Email already registered");
            return "SignUp";
        }
        User savedUser = userService.registerUser(user);
        httpSession.setAttribute("user", savedUser);
        return "redirect:/Home.html";
    }

    @GetMapping("/logout")
    public String logout(){
        httpSession.invalidate();
        return "redirect:/";
    }
}