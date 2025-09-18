package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.User;
import com.biketrip.biketrip_planner.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/auth")
public class AuthPageController {
    private final AuthService auth;

    public AuthPageController(AuthService auth) { this.auth = auth; }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required=false) String next, Model model) {
        model.addAttribute("next", next == null ? "/ui/routes" : next);
        return "auth/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String next,
                          HttpSession session, RedirectAttributes ra) {
        try {
            auth.login(email, password, session);
            ra.addFlashAttribute("ok", "Zalogowano");
            return "redirect:" + (next == null || next.isBlank() ? "/ui/routes" : next);
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("err", ex.getMessage());
            return "redirect:/ui/auth/login?next=" + (next == null ? "/ui/routes" : next);
        }
    }

    @GetMapping("/register")
    public String regForm() { return "auth/register"; }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             //@RequestParam double weight,
                             HttpSession session, RedirectAttributes ra) {
        try {
            User u = auth.register(username, email, password);
            auth.login(email, password, session);
            ra.addFlashAttribute("ok", "Konto utworzone");
            return "redirect:/ui/routes";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("err", ex.getMessage());
            return "redirect:/ui/auth/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        auth.logout(session);
        ra.addFlashAttribute("ok", "Wylogowano");
        return "redirect:/ui/auth/login";
    }
}


