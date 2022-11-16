package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    private Integer id;

    @Email
    private String email;

    @NotNull(message = "Не может быть null")
    @Pattern(regexp = "^\\S*$")
    private String login;

    @NotNull(message = "Не может быть null")
    private String name;

    @NotNull(message = "Не может быть null")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();
}