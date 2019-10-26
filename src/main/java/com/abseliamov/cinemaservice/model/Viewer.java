package com.abseliamov.cinemaservice.model;

import java.time.LocalDate;

public class Viewer extends GenericModel {
    private String lastName;
    private String password;
    private Role role;
    private LocalDate birthday;

    public Viewer() {
    }

    public Viewer(long id, String name, String lastName, LocalDate birthday) {
        super(id, name);
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public Viewer(long id, String name, String lastName, String password, Role role, LocalDate birthday) {
        super(id, name);
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.birthday = birthday;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Viewer viewer = (Viewer) o;

        if (lastName != null ? !lastName.equals(viewer.lastName) : viewer.lastName != null) return false;
        if (password != null ? !password.equals(viewer.password) : viewer.password != null) return false;
        if (role != viewer.role) return false;
        return birthday != null ? birthday.equals(viewer.birthday) : viewer.birthday == null;

    }

    @Override
    public int hashCode() {
        int result = lastName != null ? lastName.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-22s%-24s%-14s%-1s\n%-1s",
                " ", getId(), getName(), getLastName(), getRole(), getBirthday(),
                "|-------|---------------------|----------------------|--------------|--------------|");
    }
}
