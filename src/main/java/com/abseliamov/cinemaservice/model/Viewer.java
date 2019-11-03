package com.abseliamov.cinemaservice.model;

import com.abseliamov.cinemaservice.model.enums.Role;
import com.abseliamov.cinemaservice.model.enums.RoleConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viewers")
public class Viewer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "viewer_id", nullable = false, updatable = false)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "role", nullable = false)
    @Convert(converter = RoleConverter.class)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "viewer", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>(0);

    public Viewer() {
    }

    public Viewer(long id, String firstName, String lastName, LocalDate birthday) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public Viewer(long id, String firstName, String lastName, String password,
                  LocalDate birthday, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthday = birthday;
        this.role = role;
    }

    public Viewer(long id, String firstName, String lastName, String password,
                  LocalDate birthday, Role role, List<Ticket> tickets) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthday = birthday;
        this.role = role;
        this.tickets = tickets;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-22s%-24s%-14s%-1s\n%-1s",
                " ", getId(), getFirstName(), getLastName(), getRole(), getBirthday(),
                "|-------|---------------------|----------------------|--------------|--------------|");
    }
}
