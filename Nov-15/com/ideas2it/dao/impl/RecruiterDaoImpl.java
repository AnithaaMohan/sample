package com.ideas2it.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.Transaction;

import com.ideas2it.dao.RecruiterDao;
import com.ideas2it.databaseconnection.DatabaseConnection;
import com.ideas2it.exception.HireWorldException;
import com.ideas2it.logger.HireWorldLogger;
import com.ideas2it.model.Applicant;
import com.ideas2it.model.Recruiter;
import com.ideas2it.util.constant.Constant;
import com.ideas2it.util.HibernateUtil;
import com.ideas2it.util.Validation;

public class RecruiterDaoImpl implements RecruiterDao {
    private SessionFactory factory = HibernateUtil.getSessionFactory(); 
    
    /**
     *{@inheritdoc}
     */
    public Recruiter insertRecruiter(Recruiter recruiter) throws HireWorldException {
        Session session = null;

         try {
            session = factory.openSession();
	    session.beginTransaction();
	    session.saveOrUpdate(recruiter);
	    session.getTransaction().commit();
            if (0 != recruiter.getId()) {
                throw new HireWorldException(recruiter.getName() + " Recruiter Inserted ");
            }
        } catch (HibernateException hibernateException) {
            session.getTransaction().rollback();
            throw new HireWorldException("Error occoured while trying to insert " + recruiter.getName());
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return recruiter;
    }

    /**
     *{@inheritdoc}
     */
    public List<Recruiter> retrieveRecruiters() throws HireWorldException {
        List<Recruiter> recruiters = null;
        Session session = null;  	

        try {
            session = factory.openSession();
            recruiters =(List<Recruiter>) session.createQuery("FROM Recruiter").list();
            HireWorldLogger.displayInfo("Displaying all the Recruiters");
        } catch (HibernateException hibernateException) {
            throw new HireWorldException("Error occoured while trying to fetch all Recruiters ");
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return recruiters;
    }
    
    /**
     *{@inheritdoc}
     */
     public Recruiter retrieveRecruiterById(int recruiterId) throws HireWorldException {    
        Recruiter recruiter = null;
        Session session = null;
        Transaction transaction = null;     	

        try {
            session = factory.openSession();
            recruiter = (Recruiter) session.get(Recruiter.class, recruiterId );
        } catch (HibernateException hibernateException) {
            session.getTransaction().rollback();
            throw new HireWorldException("Error occoured while trying to fetch " + recruiter.getName());
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return recruiter;
    }  
    /**
     *{@inheritdoc}
     */
    public Recruiter updateRecruiterById(Recruiter updatedRecruiter, int recruiterId) throws HireWorldException {
        boolean isUpdated = false;
        Recruiter recruiter = null;
        Session session = null;
        Transaction transaction = null;     	

        try {
            session = factory.openSession();
            recruiter = (Recruiter) session.get(Recruiter.class, recruiterId);
	    session.beginTransaction();
            recruiter.setName(updatedRecruiter.getName());
            recruiter.setEmailAddress(updatedRecruiter.getEmailAddress());
            recruiter.setMobileNumber(updatedRecruiter.getMobileNumber());
	    session.getTransaction().commit();
            isUpdated = true;
            HireWorldLogger.displayInfo(recruiter.getName() + " Recruiter updated ");
        } catch (HibernateException hibernateException) {

            if ( transaction != null ) {
                session.getTransaction().rollback();	
	    }
            throw new HireWorldException("Error occoured while trying to fetch " + recruiter.getName());
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return recruiter;
    }
    
    /**
     *{@inheritdoc}
     */
    public boolean removeRecruiterById (int recruiterId) throws HireWorldException {
        Session session = null;
        Transaction transaction = null;
     	boolean isRemoved = false;
        Recruiter recruiter = null;

        try {
            session = factory.openSession();
	    session.beginTransaction();
            recruiter = (Recruiter) session.get(Recruiter.class, recruiterId );
	    session.delete(recruiter);
	    session.getTransaction().commit();

            if ( transaction != null ) {
		isRemoved = true;
	    }
        } catch (HibernateException hibernateException) {
            session.getTransaction().rollback();
            throw new HireWorldException("Error occoured while trying to delete " + recruiter.getName());
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return isRemoved;
    }

    /**
     *{@inheritdoc}
     */
    public List<Recruiter> retriveRecruitersByApplicantId(int applicantId) throws HireWorldException {
        List<Recruiter> recruiters = new ArrayList<>();   
        Session session = null;

        try {
            session = factory.openSession();
            session.beginTransaction();
            StringBuilder hqlQuery = new StringBuilder();
            hqlQuery.append("select recruiter from Recruiter recruiter ")
                     .append("JOIN recruiter.applicants applicant ")
                     .append("where applicant.id = :applicantId ")
                     .append("and recruiter.isDeleted = false "); 
            Query<Recruiter> query = session.createQuery(hqlQuery.toString());
            recruiters = query.setParameter("applicantId" , applicantId).getResultList();	    
        } catch (HibernateException hibernateException) {
            throw new HireWorldException("Error occoured while trying to fetch recruiters assigned" );
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
                HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
        }
	return recruiters;
    }
}
    



         
    

        
    












  
  