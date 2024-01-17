package com.example.books.dto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
    @Controller
    public class About {

        @GetMapping("/About")
        public String aboutPage() {
            return "about";
        }
    }
