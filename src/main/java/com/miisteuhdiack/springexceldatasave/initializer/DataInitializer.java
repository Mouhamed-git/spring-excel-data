package com.miisteuhdiack.springexceldatasave.initializer;

import com.miisteuhdiack.springexceldatasave.model.entities.Person;
import com.miisteuhdiack.springexceldatasave.model.enums.Gender;
import com.miisteuhdiack.springexceldatasave.model.enums.MaritalStatus;
import com.miisteuhdiack.springexceldatasave.model.validation.RequestValidator;
import com.miisteuhdiack.springexceldatasave.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class DataInitializer {
    private static final String EXCEL_FILE_PATH = "static/users.xlsx";
    private final RequestValidator validator;
    private InputStream getFilePath() throws IOException {
        return new ClassPathResource(EXCEL_FILE_PATH).getInputStream();
    }

    @Bean
    public CommandLineRunner initData(PersonRepository repository) {
        return args -> {
           log.info("*** SAVE PERSONS ***");
            saveExcelData(repository);
        };
    }
    public void saveExcelData(PersonRepository repository) {

        Set<Person> persons = new HashSet<>();

        try {

            Workbook workbook = WorkbookFactory.create(getFilePath());

            workbook.getSheetAt(0).forEach(row -> {
                String firstname = row.getCell(0).getStringCellValue();
                String lastname = row.getCell(1).getStringCellValue();
                String gender = row.getCell(2).getStringCellValue();
                String email = row.getCell(3).getStringCellValue();
                String occupation =  row.getCell(4).getStringCellValue();
                String maritalStatus = row.getCell(5).getStringCellValue();
                double numberOfChildren = row.getCell(6).getNumericCellValue();

                Person person = Person.builder()
                        .firstname(firstname)
                        .lastname(lastname)
                        .gender(Gender.valueOf(gender.toUpperCase()))
                        .email(email)
                        .occupation(occupation)
                        .maritalStatus(MaritalStatus.valueOf(maritalStatus.toUpperCase()))
                        .numberOfChildren((int) numberOfChildren)
                        .build();

                validator.check(person);

                persons.add(person);
            });

            repository.saveAll(persons);

            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
