package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.ViewerDaoImpl;
import com.abseliamov.cinemaservice.model.GenericModel;
import com.abseliamov.cinemaservice.model.Role;
import com.abseliamov.cinemaservice.model.Viewer;
import com.abseliamov.cinemaservice.utils.CurrentViewer;
import com.google.common.collect.Multimap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewerService {
    private ViewerDaoImpl viewerDao;
    private CurrentViewer currentViewer;
    private static final String ERROR_NAME_OR_PASSWORD =
            "Please enter correct username and password or enter \'0\' to exit:";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ViewerService(ViewerDaoImpl viewerDao, CurrentViewer currentViewer) {
        this.viewerDao = viewerDao;
        this.currentViewer = currentViewer;
    }

    public boolean createViewer(String firstName, String lastName, String password, Role role, LocalDate birthday) {
        List<Viewer> viewers = viewerDao.getAll();
        Viewer viewer = viewers
                .stream()
                .filter(viewerItem -> viewerItem.getName().equalsIgnoreCase(firstName) &&
                        viewerItem.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
        if (viewer == null) {
            viewerDao.add(new Viewer(0, firstName, lastName, password, role, birthday));
            System.out.println("Viewer with name \'" + firstName + "\' successfully added.");
            return true;
        } else {
            System.out.println("Such viewer already exists.");
        }
        return false;
    }

    public boolean authorization(String name, String password) {
        boolean checkUser = false;
        Viewer viewer;
        if (name.isEmpty() || password.isEmpty()) {
            System.out.println(ERROR_NAME_OR_PASSWORD);
            return checkUser;
        } else if ((viewer = viewerDao.checkUserAuthorization(name, password)) != null) {
            currentViewer.setViewer(viewer);
            checkUser = true;
        } else {
            System.out.println("User with name \'" + name + "\' and password \'" + password + "\' does not exist.");
            System.out.println(ERROR_NAME_OR_PASSWORD);
        }
        return checkUser;
    }

    public Viewer getById(long viewerId) {
        return viewerDao.getById(viewerId);
    }

    public List<Viewer> getAll() {
        Role role = currentViewer.getViewer().getRole();
        List<Viewer> viewerList = viewerDao.getAll();
        if (role == Role.ADMIN) {
            printViewerForAdmin(viewerList);
        } else if (role == Role.USER) {
            printViewer(viewerList);
        }
        return viewerList;
    }

    public void update(long viewerId, String firstName, String lastName,
                       String password, LocalDate birthday, Role role) {
        List<Viewer> viewers = viewerDao.getAll();
        Viewer updateViewer = new Viewer(viewerId, firstName, lastName, password, role, birthday);
        Viewer viewer = viewers
                .stream()
                .filter(viewerItem -> viewerItem.equals(updateViewer))
                .findFirst()
                .orElse(null);
        if (viewer == null) {
            if (viewerDao.update(viewerId, updateViewer)) {
                System.out.println("Update successfully.");
            }
        }
    }

    public void delete(long viewerId) {
        if (viewerDao.delete(viewerId)) {
            System.out.println("Viewer with id \'" + viewerId + "\' deleted.");
        }
    }

    private void printViewerForAdmin(List<Viewer> viewerList) {
        if (!viewerList.isEmpty()) {
            System.out.println("\n|-------------------------------------------------------------------" +
                    "-----------------------------------|");
            System.out.printf("%-45s%-1s\n", " ", "LIST OF VIEWERS");
            System.out.println("|---------------------------------------------------------------------" +
                    "---------------------------------|");
            System.out.printf("%-3s%-12s%-23s%-22s%-19s%-13s%-1s\n",
                    " ", "ID", "FIRST NAME", "LAST NAME", "PASSWORD", "ROLE", "BIRTHDAY");
            System.out.println("|-------|---------------------|----------------------|----------------" +
                    "---|--------------|--------------|");
            viewerList.stream()
                    .sorted(Comparator.comparing(GenericModel::getId))
                    .collect(Collectors.toList())
                    .forEach(viewer -> System.out.printf("%-2s%-8s%-22s%-24s%-20s%-14s%-1s\n%-1s",
                            " ", viewer.getId(), viewer.getName(), viewer.getLastName(), viewer.getPassword(),
                            viewer.getRole().name(), formatter.format(viewer.getBirthday()),
                            "|-------|---------------------|----------------------|----------------" +
                                    "---|--------------|--------------|\n"));
        } else {
            System.out.println("List viewers is empty.");
        }
    }

    private void printViewer(List<Viewer> viewerList) {
        if (!viewerList.isEmpty()) {
            System.out.println("\n|----------------------------------------------------------------------------------|");
            System.out.printf("%-35s%-1s\n", " ", "LIST OF VIEWERS");
            System.out.println("|----------------------------------------------------------------------------------|");
            System.out.printf("%-3s%-12s%-23s%-21s%-13s%-1s\n",
                    " ", "ID", "FIRST NAME", "LAST NAME", "ROLE", "BIRTHDAY");
            System.out.println("|-------|---------------------|----------------------|--------------|--------------|");
            viewerList.stream()
                    .sorted(Comparator.comparing(GenericModel::getId))
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

    public Multimap<String, Viewer> searchDateWithSeveralViewersBirthday() {
        Multimap<String, Viewer> localDateListMap = viewerDao.searchDateWithSeveralViewersBirthday();
        printMapWithListBirthday(localDateListMap);
        return localDateListMap;
    }

    private void printViewerByRequest(List<Viewer> viewers) {
        if (!viewers.isEmpty()) {
            System.out.println("|------------------------------------------------------------|");
            System.out.printf("%-27s%-1s\n", " ", "REQUEST RESULT");
            System.out.println("|------------------------------------------------------------|");
            System.out.printf("%-3s%-10s%-21s%-17s%-1s\n%-1s\n", " ", "ID", "FIRST NAME", "LAST NAME", "BIRTHDAY",
                    "|------|-------------------|--------------------|------------|");
            viewers.forEach(viewer -> System.out.printf("%-3s%-6s%-20s%-21s%-1s\n%-1s\n",
                    " ", viewer.getId(), viewer.getName(), viewer.getLastName(),
                    formatter.format(viewer.getBirthday()),
                    "|------|-------------------|--------------------|------------|"));
        } else {
            System.out.println("At your request viewers not found\n");
        }
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
                            " ", viewer.getId(), viewer.getName(), viewer.getLastName(),
                            formatter.format(viewer.getBirthday()),
                            "|------|-------------------|--------------------|------------|");
                }
            }
        } else {
            System.out.println("At your request viewers not found\n");
        }
    }
}
