package com.weshare.controller;

import com.weshare.security.NoAuthorization;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @NoAuthorization
    public String handleError() {
        return "redirect: logout?error";
    }

}
