package com.viettelpost.core.services.daos;

import com.viettelpost.core.base.BaseDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppDAO extends BaseDAO {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Qualifier("coreFactory")
    SessionFactory sessionFactory;

    public AppDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @Transactional
    public int getNewAppID() throws Exception {
        return (int) getSession(sessionFactory).createSQLQuery("select ID as APP_ID from ERP_SSO.VP_APP where ROWNUM <= 1 order by ID desc").addScalar("APP_ID", new IntegerType()).uniqueResult() + 1;
    }

    @Transactional
    public void insertApp(String name, String desription, String Code) throws Exception {
        Query query = getSession(sessionFactory).createSQLQuery("INSERT INTO ERP_SSO.VP_APP(ID, NAME, DESRIPTION, STATUS, CREATEDATE, CODE) VALUES (?, ?, ?, 1, SYSDATE,?)");
        query.setParameter(1, getNewAppID());
        query.setParameter(2, name);
        query.setParameter(3, desription);
        query.setParameter(4, Code);
        query.executeUpdate();
    }

    @Transactional
    public void deleteApp(Long id) {
        Query query = getSession(sessionFactory).createSQLQuery("DELETE FROM ERP_SSO.VP_APP where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }


    @Transactional
    public void updateApp(String name, String description, String Code, Long id) {
        Query query = getSession(sessionFactory).createSQLQuery("UPDATE ERP_SSO.VP_APP SET NAME = ?, DESRIPTION = ?, CODE = ? WHERE ID = ?");
        query.setParameter(1, name);
        query.setParameter(2, description);
        query.setParameter(3, Code);
        query.setParameter(4, id);
        query.executeUpdate();
    }



}
