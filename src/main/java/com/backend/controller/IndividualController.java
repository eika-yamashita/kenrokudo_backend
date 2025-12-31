package com.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.Individual;

@RestController
@CrossOrigin(origins = "*") 
public class IndividualController {
	
    @GetMapping("/individuals")
    @ResponseBody
    public List<Individual> getIndividualInfo() {
        // 1つ目の個体
        Individual individual1 = new Individual();
        individual1.setSpeciesCd("0001");
        individual1.setId("25A001");
        individual1.setName("AAAA");
        individual1.setGender("0");

        // 2つ目の個体
        Individual individual2 = new Individual();
        individual2.setSpeciesCd("0002");
        individual2.setId("25A002");
        individual2.setName("BBBB");
        individual2.setGender("1");

        // 3つ目の個体
        Individual individual3 = new Individual();
        individual3.setSpeciesCd("0003");
        individual3.setId("25A003");
        individual3.setName("CCCC");
        individual3.setGender("0");
        
        // 2つ目の個体
        Individual individual4 = new Individual();
        individual4.setSpeciesCd("0004");
        individual4.setId("25A004");
        individual4.setName("DDDD");
        individual4.setGender("1");

        // リストに追加
        List<Individual> list = new ArrayList<>();
        list.add(individual1);
        list.add(individual2);
        list.add(individual3);
        list.add(individual4);

        return list;
    }
    
    // 1件取得
    @GetMapping("/individuals/{speciesCd}/{id}")
    public Individual getIndividual(@PathVariable  String speciesCd, @PathVariable String id) {
        Individual individual2 = new Individual();
        individual2.setSpeciesCd(speciesCd);
        individual2.setId(id);
        individual2.setName("BBBB");
        individual2.setGender("1");
        
        return individual2;
    }
}
