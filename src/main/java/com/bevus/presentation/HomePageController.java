package com.bevus.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomePageController {
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity index() {
        return ResponseEntity.ok("Hello world");
    }
}
