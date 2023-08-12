package com.miisteuhdiack.springexceldatasave.service.query.impl;

import com.miisteuhdiack.springexceldatasave.exceptions.NotFoundException;
import com.miisteuhdiack.springexceldatasave.model.entities.Person;
import com.miisteuhdiack.springexceldatasave.repository.PersonRepository;
import com.miisteuhdiack.springexceldatasave.service.query.PersonQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonQueryServiceImpl implements PersonQueryService {

    private final PersonRepository repository;

    @Override
    public List<Person> find() {
        return repository.findAll();
    }

    @Override
    public Person find(UUID uuid) {
        return repository.findById(uuid).orElseThrow(NotFoundException::new);
    }
}
