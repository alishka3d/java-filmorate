package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @Min(value = 1)
    private int id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @Size(max = 200)
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private long duration;
    private Set<Integer> likes = new HashSet<>();
}
