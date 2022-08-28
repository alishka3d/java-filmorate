package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {

    @Min(value = 1)
    private int id;
    @NonNull
    @Email
    private String email;
    @NonNull
    @NotBlank
    private String login;
    @NonNull
    private String name;
    @NonNull
    private LocalDate birthday;
}