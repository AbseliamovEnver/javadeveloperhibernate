package com.abseliamov.cinemaservice.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtil {

    private static EntityManager entityManager;

    private EntityManagerUtil() {
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory =
                    Persistence.createEntityManagerFactory("jpa-configuration");
            return entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }
}
