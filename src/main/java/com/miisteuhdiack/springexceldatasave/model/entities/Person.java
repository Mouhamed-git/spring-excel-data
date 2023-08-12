package com.miisteuhdiack.springexceldatasave.model.entities;

import com.miisteuhdiack.springexceldatasave.model.enums.Gender;
import com.miisteuhdiack.springexceldatasave.model.enums.MaritalStatus;
import com.miisteuhdiack.springexceldatasave.model.validation.groups.Create;
import com.miisteuhdiack.springexceldatasave.model.validation.PatternValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

import static com.miisteuhdiack.springexceldatasave.model.validation.messages.ErrorMessage.MALFORMED_FIELD;
import static com.miisteuhdiack.springexceldatasave.model.validation.messages.ErrorMessage.REQUIRED_FIELD;

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
