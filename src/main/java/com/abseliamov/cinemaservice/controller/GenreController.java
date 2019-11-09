package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.Genre;
import com.abseliamov.cinemaservice.service.GenreService;

import java.util.List;

public class GenreController {
    private GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    public List<Genre> getAll() {
        return genreService.getAll();
    }

    public List<Genre> printGenre() {
        return genreService.printGenre();
    }

//    ADMIN MENU METHODS

    public void createGenre(String genreName) {
        genreService.createGenre(genreName);
    }

    public Genre getById(long genreId) {
        return genreService.getById(genreId);
    }

    public void updateGenre(long genreId, String updateGenreName) {
        genreService.updateGenre(genreId, updateGenreName);
    }

    public void deleteGenre(long genreId) {
        genreService.delete(genreId);
    }
}
