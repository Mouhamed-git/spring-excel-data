    package com.miisteuhdiack.springexceldatasave.service.query;


    import com.miisteuhdiack.springexceldatasave.model.entities.Person;

    import java.util.List;
    import java.util.UUID;

    public interface PersonQueryService {
        List<Person> find();
        Person find(UUID uuid);
    }
