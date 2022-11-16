package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDaoStorage;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDaoStorage mpaDaoStorage;

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable Integer id) {
        return mpaDaoStorage.getMpaById(id);
    }

    @GetMapping
    public List<Mpa> findAll() {
        return mpaDaoStorage.getAllMpa();
    }
}