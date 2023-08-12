# SPRING - POSTGRESQL - EXCEL 

## This repository shows how to save data from Excel file to PostgreSQL database using Spring Boot.

![My Image](/src/main/resources/images/banner.png)

### Context:
I have an Excel file with almost 100 people with their information (firstname, last name, gender, email, occupation and marital status) which I need to register on a PostgreSQL database using Spring Boot.

** 🔵 Project Architecture 🔵 **

```bash
├── HELP.md
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── spring-excel-data-save.iml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── miisteuhdiack
│   │   │           └── springexceldatasave
│   │   │               ├── SpringExcelDataSaveApplication.java
│   │   │               ├── controlller
│   │   │               │   └── query
│   │   │               │       └── PersonQueryController.java
│   │   │               ├── exceptions
│   │   │               │   ├── CoreException.java
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── NotFoundException.java
│   │   │               │   └── RequestNotValidException.java
│   │   │               ├── initializer
│   │   │               │   └── DataInitializer.java
│   │   │               ├── model
│   │   │               │   ├── entities
│   │   │               │   │   └── Person.java
│   │   │               │   ├── enums
│   │   │               │   │   ├── Gender.java
│   │   │               │   │   └── MaritalStatus.java
│   │   │               │   └── validation
│   │   │               │       ├── PatternValidator.java
│   │   │               │       ├── RequestValidator.java
│   │   │               │       ├── groups
│   │   │               │       │   ├── Create.java
│   │   │               │       │   └── ValidationGroup.java
│   │   │               │       └── messages
│   │   │               │           └── ErrorMessage.java
│   │   │               ├── repository
│   │   │               │   ├── ExcelDataRepository.java
│   │   │               │   └── PersonRepository.java
│   │   │               ├── service
│   │   │               │   └── query
│   │   │               │       ├── PersonQueryService.java
│   │   │               │       └── impl
│   │   │               │           └── PersonQueryServiceImpl.java
│   │   │               └── utils
│   │   │                   ├── ExceptionMessage.java
│   │   │                   └── ExceptionResponse.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── banner.txt
│   │       ├── images
│   │       │   ├── banner.png
│   │       │   ├── findAll.png
│   │       │   └── findOne.png
│   │       ├── static
│   │       │   └── users.xlsx
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── miisteuhdiack
│                   └── springexceldatasave
│                       ├── SpringExcelDataSaveApplicationTests.java
│                       └── person
│                           └── query
│                               └── PersonQueryControllerITests.java
```
** 🔵 Save Excel Data 🔵 **

1. Configure DataSource
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false
    generate-ddl: true
  datasource:
    url: jdbc:postgresql://localhost:5432/springexceldb
    username: springexcel
    password: miisteuh_d1@ck
    driver-class-name: org.postgresql.Driver
    hikari:
      auto-commit: false

```

2. Define a model 

```java
@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Person {
    @Id @GeneratedValue
    private UUID id;

    @NotNull(message = REQUIRED_FIELD, groups = Create.class)
    @Column(nullable = false)
    private String firstname;

    @NotNull(message = REQUIRED_FIELD, groups = Create.class)
    @Column(nullable = false)
    private String lastname;

    @NotNull(message = REQUIRED_FIELD, groups = Create.class)
    @Column(nullable = false, unique = true)
    @Pattern(regexp = PatternValidator.EMAIL, message = MALFORMED_FIELD, groups = Create.class)
    private String email;

    @NotNull(message = REQUIRED_FIELD, groups = Create.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NotNull(message = REQUIRED_FIELD, groups = Create.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritalStatus maritalStatus;

    @NotNull(message = REQUIRED_FIELD, groups = Create.class)
    @Column(nullable = false)
    private String occupation;

    @NotNull(message = REQUIRED_FIELD, groups = Create.class)
    @Column(nullable = false)
    private int numberOfChildren;

}

```

3. Create repository

```java
@NoRepositoryBean
public interface ExcelDataRepository<T> extends JpaRepository<T, UUID> {
}
```
```java
public interface PersonRepository extends ExcelDataRepository<Person>{
}
```
4. Validate request
```java
@Component
@RequiredArgsConstructor
public class RequestValidator {
    private final Validator validator;

    public <T> void check(T request) {
        check(request, Create.class);
    }

    public <T> void check(T document, Class<? extends ValidationGroup> groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(document, groups);
        if (!violations.isEmpty()) {
            throw new RequestNotValidException(violations);
        }
    }
}
```

```java
public class RequestNotValidException extends ConstraintViolationException {
    public <T> RequestNotValidException(Set<ConstraintViolation<T>> constraintViolations) {
        super(constraintViolations);
    }
}
```
5. Create Exception
```java
public class CoreException extends RuntimeException {
    public CoreException(String message) {
        super(message);
    }
}
```
```java
public class NotFoundException extends CoreException {
    public NotFoundException() {
        super(ExceptionMessage.NOT_FOUND);
    }
}
```
```java
public record ExceptionResponse(HttpStatus status, String message) {
}
```
```java
@ControllerAdvice(annotations = RestController.class, basePackages = "com.miisteuhdiack.springexceldatasave.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFound(NotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
```

6. Import dependency apache-poi
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>
```

7. Create class DataInitializer
```java
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

```

8. Service
```java
    public interface PersonQueryService {
        List<Person> find();
        Person find(UUID uuid);
    }
```
```java
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
```
9. Controller
```java
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
```
10. Testing
```java
@SpringBootTest
@AutoConfigureMockMvc
public class SpringExcelDataSaveApplicationTests {
    protected MockMvc mockMvc;
    public SpringExcelDataSaveApplicationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
}
```
```java
public class PersonQueryControllerITests extends SpringExcelDataSaveApplicationTests {
    private final PersonRepository repository;

    @Autowired
    public PersonQueryControllerITests(MockMvc mockMvc, PersonRepository repository) {
        super(mockMvc);
        this.repository = repository;
    }

    @Test
    public void itShouldReturnAllPersons() throws Exception{
        mockMvc.perform(get(PersonQueryController.PERSON_QUERY_ROUTE))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void itShouldReturnPersonById() throws Exception{

        UUID uuid = repository.findAll().stream().findFirst().map(Person::getId).orElseThrow(NotFoundException::new);

        mockMvc.perform(get(PersonQueryController.PERSON_QUERY_ROUTE.concat("/" + uuid)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
 }
```
![My Image](/src/main/resources/images/findAll.png)

![My Image](/src/main/resources/images/findOne.png)


##### Thank you ! 🤗

*Follow me:*
* LinkedIn: https://www.linkedin.com/in/mouhamad-diack-b0b1541a3/
* Discord: https://discord.com/users/845331863018274836
* medium: https://medium.com/@rootsn221/spring-boot-postgresql-excel-84ea2b61ccfa
* Portfolio: https://md-portfolio.carrd.co/
