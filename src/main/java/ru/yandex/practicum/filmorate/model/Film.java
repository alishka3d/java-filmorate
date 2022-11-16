package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {

    private int id;

    @NotNull(message = "Не может быть null")
    private String name;

    @NotNull(message = "Не может быть null")
    private String description;

    @NotNull(message = "Не может быть null")
    private LocalDate releaseDate;

    @NotNull
    private Integer duration;

    private Mpa mpa;

    private Set<Integer> likes = new HashSet<>();

    private Set<Genre> genres = new LinkedHashSet<>();
}
