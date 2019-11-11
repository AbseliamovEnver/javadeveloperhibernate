package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.GenreDaoImpl;
import com.abseliamov.cinemaservice.model.GenericModel;
import com.abseliamov.cinemaservice.model.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GenreService {
    private GenreDaoImpl genreDao;

    public GenreService(GenreDaoImpl genreDao) {
        this.genreDao = genreDao;
    }

    public void createGenre(String genreName) {
        Genre newGenre = null;
        List<Genre> genreList = genreDao.getAll();
        if (!genreList.isEmpty()) {
            newGenre = genreList
                    .stream()
                    .filter(genre -> genre.getName().equalsIgnoreCase(genreName))
                    .findFirst()
                    .orElse(null);
        }
        if (newGenre == null) {
            genreDao.add(new Genre(0, genreName));
            System.out.println("\nGenre with name \'" + genreName + "\' successfully added.");
        } else {
            System.out.println("\nSuch a genre already exists.");
        }
    }

    public Genre getById(long genreId) {
        return genreDao.getById(genreId);
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    public List<Genre> printGenre() {
        List<Genre> genreList = genreDao.getAll();
        if (!genreList.isEmpty()) {
            List<Genre> sortedGenreList = genreList
                    .stream()
                    .sorted(Comparator.comparingLong(GenericModel::getId))
                    .collect(Collectors.toList());
            System.out.println("\n|------------------------|");
            System.out.printf("%-6s%-1s\n", " ", "LIST OF GENRES");
            System.out.println("|------------------------|");
            System.out.printf("%-3s%-11s%-1s\n", " ", "ID", "GENRE");
            System.out.println("|-------|----------------|");
            sortedGenreList.forEach(genre -> System.out.printf("%-2s%-8s%-1s\n%-1s\n",
                    " ", genre.getId(), genre.getName(), "|-------|----------------|"));
        } else {
            System.out.println("\nGenre list is empty.");
        }
        return genreList;
    }

    public void updateGenre(long genreId, String updateGenreName) {
        List<Genre> genres = genreDao.getAll();
        Genre genre = genres.stream()
                .filter(genreItem -> genreItem.getName().equalsIgnoreCase(updateGenreName))
                .findFirst()
                .orElse(null);
        if (genre == null) {
            if (genreDao.update(genreId, new Genre(genreId, updateGenreName))) {
                System.out.println("\nUpdate successfully.");
            }
        } else {
            System.out.println("\nGenre with name \'" + updateGenreName + "\' already exists.");
        }
    }

    public void delete(long genreId) {
        if (genreDao.delete(genreId)) {
            System.out.println("\nGenre with id \'" + genreId + "\' deleted.");
        } else {
            System.out.println("\nGenre with id \'" + genreId + "\' doesn't delete.");
        }
    }
}
