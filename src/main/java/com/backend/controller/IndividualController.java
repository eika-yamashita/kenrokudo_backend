package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.IndividualsApi;
import com.backend.model.Individual;
import com.backend.service.IndividualService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class IndividualController implements IndividualsApi{

    private final IndividualService individualService;

    public IndividualController(IndividualService individualService) {
        this.individualService = individualService;
    }

    @Override
    public ResponseEntity<List<Individual>> individualsGet() {
        List<Individual> individuals = individualService.getIndividuals();
        return ResponseEntity.ok(individuals);
    }
    
	@Override
	public ResponseEntity<Void> individualsSpeciesCdIdDelete(String speciesCd, String id) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public ResponseEntity<Void> individualsSpeciesCdIdPut(String speciesCd, String id,
			com.backend.model.@Valid Individual individual) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
