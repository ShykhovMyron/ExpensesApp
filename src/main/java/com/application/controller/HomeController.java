package com.application.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    final static Logger logger = Logger.getLogger(HomeController.class);

    @GetMapping("/")
    public String greeting(Model model) {
        logger.info("Come to main-page");
        return "home";
    }

}
