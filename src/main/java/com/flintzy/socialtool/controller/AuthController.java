package com.flintzy.socialtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/oauth2/redirect")
    public String oauth2Redirect(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "oauth-success";
    }
}
