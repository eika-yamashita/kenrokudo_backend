package com.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.IndividualsApi;
import com.backend.model.Individual;
import com.backend.service.IndividualService;

import jakarta.validation.Valid;

@RestController
public class IndividualController implements IndividualsApi{

    private final IndividualService individualService;

    public IndividualController(IndividualService individualService) {
        this.individualService = individualService;
    }

    /**
     * GET /individuals : 個体情報一覧を取得
     *
     * @return 一覧取得成功 (status code 200)
     */
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
	public ResponseEntity<Individual> individualsPost(@Valid Individual individual) {
		Individual created = individualService.createIndividual(individual);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@Override
	public ResponseEntity<Void> individualsSpeciesCdIdPut(String speciesCd, String id, @Valid Individual individual) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
