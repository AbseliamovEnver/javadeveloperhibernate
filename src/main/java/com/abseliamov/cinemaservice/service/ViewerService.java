package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.TicketDaoImpl;
import com.abseliamov.cinemaservice.dao.ViewerDaoImpl;
import com.abseliamov.cinemaservice.model.enums.Role;
import com.abseliamov.cinemaservice.model.Viewer;
import com.abseliamov.cinemaservice.utils.CurrentViewer;
import com.google.common.collect.Multimap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ViewerService {
    private static final String ERROR_NAME_OR_PASSWORD =
            "Please enter correct username and password or enter \'0\' to exit:";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH);
    private CurrentViewer currentViewer;
    private ViewerDaoImpl viewerDao;
    private TicketDaoImpl ticketDao;

    public ViewerService(ViewerDaoImpl viewerDao, CurrentViewer currentViewer, TicketDaoImpl ticketDao) {
        this.viewerDao = viewerDao;
        this.currentViewer = currentViewer;
        this.ticketDao = ticketDao;
    }

    public boolean createViewer(String firstName, String lastName, String password, Role role, LocalDate birthday) {
        List<Viewer> viewers = viewerDao.getAll();
        Viewer viewer = viewers
                .stream()
                .filter(viewerItem -> viewerItem.getFirstName().equalsIgnoreCase(firstName) &&
                        viewerItem.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
        if (viewer == null) {
            viewerDao.add(new Viewer(0, firstName, lastName, password, birthday, role));
            System.out.println("\nViewer with name \'" + firstName + "\' successfully added.");
            return true;
        } else {
            System.out.println("\nSuch viewer already exists.");
        }
        return false;
    }

    public boolean authorization(String name, String password) {
        boolean checkUser = false;
        Viewer viewerExist;
        if (name.isEmpty() || password.isEmpty()) {
            System.out.println(ERROR_NAME_OR_PASSWORD);
            return checkUser;
        } else {
            viewerExist = viewerDao.getAll().stream()
                    .filter(viewer -> viewer.getFirstName().equals(name))
                    .filter(viewer -> viewer.getPassword().equals(password))
                    .findFirst()
                    .orElse(null);
            if (viewerExist != null) {
                currentViewer.setViewer(viewerExist);
                checkUser = true;
            } else {
                System.out.println("User with name \'" + name + "\' and password \'" + password + "\' does not exist.");
                System.out.println(ERROR_NAME_OR_PASSWORD);
            }
        }
        return checkUser;
    }

    public Viewer getById(long viewerId) {
        return viewerDao.getById(viewerId);
    }

    public List<Viewer> getAll() {
        return viewerDao.getAll();
    }

    public void update(long viewerId, String firstName, String lastName,
                       String password, LocalDate birthday, Role role) {
        List<Viewer> viewers = viewerDao.getAll();
        Viewer updateViewer = new Viewer(viewerId, firstName, lastName, password, birthday, role);
        Viewer viewer = viewers
                .stream()
                .filter(viewerItem -> viewerItem.equals(updateViewer))
                .findFirst()
                .orElse(null);
        if (viewer == null) {
            if (viewerDao.update(viewerId, updateViewer)) {
                System.out.println("\nUpdate successfully.");
            }
        }else {
            System.out.println("\nSuch user already exist.");
        }
    }

    public void delete(long viewerId) {
        if (viewerDao.delete(viewerId)) {
            System.out.println("Viewer with id \'" + viewerId + "\' deleted.");
        }
    }

    public List<Viewer> printAllViewer() {
        Role role = currentViewer.getViewer().getRole();
        List<Viewer> viewers = viewerDao.getAll();
        if (role == Role.ADMIN) {
            printViewerForAdmin(viewers);
        } else if (role == Role.USER) {
            printViewerForUser(viewers);
        }
        return viewers;
    }

    private void printViewerForAdmin(List<Viewer> viewers) {
        if (!viewers.isEmpty()) {
            System.out.println("\n|-------------------------------------------------------------------" +
                    "-----------------------------------|");
            System.out.printf("%-45s%-1s\n", " ", "LIST OF VIEWERS");
            System.out.println("|---------------------------------------------------------------------" +
                    "---------------------------------|");
            System.out.printf("%-3s%-12s%-23s%-22s%-19s%-13s%-1s\n",
                    " ", "ID", "FIRST NAME", "LAST NAME", "PASSWORD", "ROLE", "BIRTHDAY");
            System.out.println("|-------|---------------------|----------------------|----------------" +
                    "---|--------------|--------------|");
            viewers.stream()
                    .sorted(Comparator.comparing(Viewer::getId))
                    .collect(Collectors.toList())
                    .forEach(viewer -> System.out.printf("%-2s%-8s%-22s%-24s%-20s%-14s%-1s\n%-1s",
                            " ", viewer.getId(), viewer.getFirstName(), viewer.getLastName(), viewer.getPassword(),
                            viewer.getRole().name(), formatter.format(viewer.getBirthday()),
                            "|-------|---------------------|----------------------|----------------" +
                                    "---|--------------|--------------|\n"));
        } else {
            System.out.println("List viewers is empty.");
        }
    }

    private void printViewerForUser(List<Viewer> viewerList) {
        if (!viewerList.isEmpty()) {
            System.out.println("\n|----------------------------------------------------------------------------------|");
            System.out.printf("%-35s%-1s\n", " ", "LIST OF VIEWERS");
            System.out.println("|----------------------------------------------------------------------------------|");
            System.out.printf("%-3s%-12s%-23s%-21s%-13s%-1s\n",
                    " ", "ID", "FIRST NAME", "LAST NAME", "ROLE", "BIRTHDAY");
            System.out.println("|-------|---------------------|----------------------|--------------|--------------|");
            viewerList.stream()
                    .filter(viewer -> viewer.getId() != 1)
                    .sorted(Comparator.comparing(Viewer::getId))
                    .collect(Collectors.toList())
                    .forEach(System.out::println);
        } else {
            System.out.println("List viewers is empty.");
        }
    }

    public List<Viewer> searchViewerMovieCountByGenre(long genreId) {
        List<Viewer> viewers = viewerDao.searchViewerMovieCountByGenre(genreId);
        printViewerByRequest(viewers);
        return viewers;
    }

    public List<Viewer> searchViewersVisitingMovieInIntervalDaysFromBirthday() {
        List<Viewer> viewers = viewerDao.searchViewersVisitingMovieInIntervalDaysFromBirthday();
        printViewerByRequest(viewers);
        return viewers;
    }

    public List<Viewer> searchViewerByComplexQuery(long genreId, double amount, List<LocalDate> dates) {
        List<Viewer> viewers = viewerDao.searchViewerByComplexQuery(genreId, amount, dates);
        printViewerByRequest(viewers);
        return viewers;
    }

    public void searchDateWithSeveralViewersBirthday() {
        List<LocalDate> birthdays = new ArrayList<>();
        List<Viewer> viewers = viewerDao.getAll();
        List resultDate = viewerDao.searchDateWithSeveralViewersBirthday();
        List<Viewer> viewersBirthday = (List<Viewer>) resultDate;
        if (viewersBirthday.size() != 0) {
            for (Viewer viewer : viewersBirthday) {
                birthdays.add(viewer.getBirthday());
            }
            printViewersWithOneDayBirthday(birthdays, viewers);
        }
    }

    private void printViewersWithOneDayBirthday(List<LocalDate> birthday, List<Viewer> viewers) {
        if (birthday != null && !viewers.isEmpty()) {
            System.out.println("\n|--------------------------------------------------------------------|");
            System.out.printf("%-30s%-1s\n", " ", "REQUEST RESULT");
            System.out.println("|--------------------------------------------------------------------|");
            for (LocalDate date : birthday) {
                System.out.printf("%-12s%-39s%-1s\n%-1s\n", " ",
                        "LIST OF VIEWERS WHO HAVE A BIRTHDAY ON\t", formatterDate.format(date).toUpperCase(),
                        "|--------------------------------------------------------------------|");
                System.out.printf("%-3s%-10s%-21s%-19s%-10s%-1s\n%-1s\n", " ",
                        "ID", "FIRST NAME", "LAST NAME", "ROLE", "YEARS",
                        "|------|-------------------|--------------------|------------|-------|");
                for (Viewer viewer : viewers) {
                    if (!viewer.getFirstName().equalsIgnoreCase("ticket_Active") &&
                            date.format(DateTimeFormatter.ofPattern("dd-MM")).
                                    equals(viewer.getBirthday().format(DateTimeFormatter.ofPattern("dd-MM")))) {
                        System.out.printf("%-3s%-6s%-20s%-21s%-15s%-1s\n%-1s\n",
                                " ", viewer.getId(), viewer.getFirstName(), viewer.getLastName(),
                                viewer.getRole().name(), LocalDate.now().getYear() - viewer.getBirthday().getYear(),
                                "|------|-------------------|--------------------|------------|-------|");
                    }
                }
            }
        } else {
            System.out.println("At your request viewers not found\n");
        }
    }

    private void printViewerByRequest(List<Viewer> viewers) {
        if (!viewers.isEmpty()) {
            System.out.println("|------------------------------------------------------------|");
            System.out.printf("%-27s%-1s\n", " ", "REQUEST RESULT");
            System.out.println("|------------------------------------------------------------|");
            System.out.printf("%-3s%-10s%-21s%-17s%-1s\n%-1s\n", " ", "ID", "FIRST NAME", "LAST NAME", "BIRTHDAY",
                    "|------|-------------------|--------------------|------------|");
            viewers.forEach(viewer -> System.out.printf("%-3s%-6s%-20s%-21s%-1s\n%-1s\n",
                    " ", viewer.getId(), viewer.getFirstName(), viewer.getLastName(),
                    formatter.format(viewer.getBirthday()),
                    "|------|-------------------|--------------------|------------|"));
        } else {
            System.out.println("At your request viewers not found\n");
        }
    }

    public List<Role> printAllRoles() {
        List<Role> roles = Arrays.asList(Role.values());
        if (roles.size() != 0) {
            System.out.println("\n|--------------------|");
            System.out.printf("%-3s%-1s\n", " ", "LIST ROLES");
            System.out.println("|--------------------|");
            System.out.printf("%-3s%-9s%-1s\n%-1s\n", " ", "ID", "ROLE",
                    "|------|-------------|");
            roles.forEach(role -> System.out.printf("%-3s%-6s%-1s\n%-1s\n",
                    " ", role.getId(), role.name(),
                    "|------|-------------|"));
        } else {
            System.out.println("List seats is empty.");
        }
        return roles;
    }

    private void printMapWithListBirthday(Multimap<String, Viewer> dateListMap) {
        if (!dateListMap.isEmpty()) {
            System.out.println("|------------------------------------------------------------|");
            System.out.printf("%-24s%-1s\n", " ", "REQUEST RESULT");
            System.out.println("|------------------------------------------------------------|");
            for (Map.Entry<String, Collection<Viewer>> mapDate : dateListMap.asMap().entrySet()) {
                System.out.printf("%-2s%-39s%-1s\n%-1s\n", " ",
                        "LIST OF VIEWERS WHO HAVE A BIRTHDAY ON ", mapDate.getKey(),
                        "|------------------------------------------------------------|");
                for (Viewer viewer : mapDate.getValue()) {
                    System.out.printf("%-3s%-10s%-21s%-17s%-1s\n%-1s\n", " ",
                            "ID", "FIRST NAME", "LAST NAME", "BIRTHDAY",
                            "|------|-------------------|--------------------|------------|");
                    System.out.printf("%-3s%-6s%-20s%-21s%-1s\n%-1s\n",
                            " ", viewer.getId(), viewer.getFirstName(), viewer.getLastName(),
                            formatter.format(viewer.getBirthday()),
                            "|------|-------------------|--------------------|------------|");
                }
            }
        } else {
            System.out.println("At your request viewers not found\n");
        }
    }

    public Role getRoleById(long roleId) {
        List<Role> roles = Arrays.asList(Role.values());
        return roles.stream().
                filter(role -> role.getId() == roleId).
                findFirst().
                orElse(null);
    }
}
