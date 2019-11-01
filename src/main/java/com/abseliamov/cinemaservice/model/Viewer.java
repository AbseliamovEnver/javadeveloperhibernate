package com.abseliamov.cinemaservice.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "viewers")
@AttributeOverride(name = "id", column = @Column(name = "viewer_id"))
@AttributeOverride(name = "name", column = @Column(name = "first_name"))
public class Viewer extends GenericModel {

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "role_id", nullable = false)
    @Convert(converter = RoleConverter.class)
    private Role role;

    @OneToMany(mappedBy = "viewer", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    public Viewer() {
    }

    public Viewer(long id, String firstName, String lastName, LocalDate birthday) {
        super(id, firstName);
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public Viewer(long id, String firstName, String lastName, String password, LocalDate birthday, Role role) {
        super(id, firstName);
        this.lastName = lastName;
        this.password = password;
        this.birthday = birthday;
        this.role = role;
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
                " ", getId(), getName(), getLastName(), getRole(), getBirthday(),
                "|-------|---------------------|----------------------|--------------|--------------|");
    }
}
