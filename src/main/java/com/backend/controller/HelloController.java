package com.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.BirthStone;

@RestController
@CrossOrigin(origins = "*") 
public class HelloController {
	
    @GetMapping("/api")
    @ResponseBody
    public BirthStone getBirthStone() {
        BirthStone birthStone = new BirthStone();
        birthStone.setMonth("3月");
        birthStone.setName("アメジスト");
        birthStone.setColor("紫");
        return birthStone;
    }
}
