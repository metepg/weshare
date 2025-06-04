package com.weshare.controller;

import com.weshare.security.NoAuthorization;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    @NoAuthorization
    public String handleError() {
        return "redirect: logout?error";
    }

}
