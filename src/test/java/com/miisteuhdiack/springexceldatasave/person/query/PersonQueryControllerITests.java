package com.miisteuhdiack.springexceldatasave.person.query;


import com.miisteuhdiack.springexceldatasave.SpringExcelDataSaveApplicationTests;
import com.miisteuhdiack.springexceldatasave.controlller.query.PersonQueryController;
import com.miisteuhdiack.springexceldatasave.exceptions.NotFoundException;
import com.miisteuhdiack.springexceldatasave.model.entities.Person;
import com.miisteuhdiack.springexceldatasave.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PersonQueryControllerITests extends SpringExcelDataSaveApplicationTests {
    private final PersonRepository repository;

    @Autowired
    public PersonQueryControllerITests(MockMvc mockMvc, PersonRepository repository) {
        super(mockMvc);
        this.repository = repository;
    }
    @Test
    void itShouldReturnAllPersons() throws Exception{
        mockMvc.perform(get(PersonQueryController.PERSON_QUERY_ROUTE))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    void itShouldReturnPersonById() throws Exception{

        UUID uuid = repository.findAll().stream().findFirst().map(Person::getId).orElseThrow(NotFoundException::new);

        mockMvc.perform(get(PersonQueryController.PERSON_QUERY_ROUTE.concat("/" + uuid)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
 }
