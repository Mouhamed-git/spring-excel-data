package com.miisteuhdiack.springexceldatasave.controlller.query;

import com.miisteuhdiack.springexceldatasave.model.entities.Person;
import com.miisteuhdiack.springexceldatasave.service.query.PersonQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping( PersonQueryController.PERSON_QUERY_ROUTE)
@RequiredArgsConstructor
public class PersonQueryController {
    public static final String PERSON_QUERY_ROUTE = "/query/person";
    private final PersonQueryService service;

    @GetMapping
    public ResponseEntity<List<Person>> find() {
        return new ResponseEntity<>(service.find(), HttpStatus.OK);
    }

    @GetMapping(value = "/{uuid}" )
    public ResponseEntity<Person> find(@PathVariable(value = "uuid") UUID uuid) {
        return new ResponseEntity<>(service.find(uuid), HttpStatus.OK);
    }
}
