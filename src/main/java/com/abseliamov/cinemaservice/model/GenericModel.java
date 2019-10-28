package com.abseliamov.cinemaservice.model;

import javax.persistence.*;

@MappedSuperclass
public class GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    public GenericModel() {
    }

    public GenericModel(long id) {
        this.id = id;
    }

    public GenericModel(String name) {
        this.name = name;
    }

    public GenericModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-1s\n%-1s",
                " ", getId(), getName(), "|-------|----------------|");
    }
}
