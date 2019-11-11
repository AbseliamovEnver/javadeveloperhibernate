package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Viewer;
import com.abseliamov.cinemaservice.utils.EntityManagerUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ViewerDaoImpl extends AbstractDao<Viewer> {

    public ViewerDaoImpl(String entityName, SessionFactory sessionFactory, Class<Viewer> clazz) {
        super(entityName, sessionFactory, clazz);
    }

    public List searchDateWithSeveralViewersBirthday() {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        String sql = "SELECT *, DATE_FORMAT(birthday, '%m-%d') AS date_month FROM viewers " +
                " GROUP BY date_month HAVING COUNT(date_month) >= 5";
        Query query = entityManager.createNativeQuery(sql, Viewer.class);
        return query.getResultList();
    }
}
