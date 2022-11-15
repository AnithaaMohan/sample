package com.ideas2it.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.ideas2it.dao.ApplicantDao;
import com.ideas2it.databaseconnection.DatabaseConnection;
import com.ideas2it.enumeration.Qualification;
import com.ideas2it.exception.HireWorldException;
import com.ideas2it.logger.HireWorldLogger;
import com.ideas2it.model.Applicant;
import com.ideas2it.model.Recruiter;
import com.ideas2it.util.Validation;
import com.ideas2it.util.HibernateUtil;
import com.ideas2it.util.constant.Constant;

public class ApplicantDaoImpl implements ApplicantDao {  
    private SessionFactory factory = HibernateUtil.getSessionFactory();   
     
    /**
     *{@inheritdoc}
     */
    public Applicant insertApplicant(Applicant applicant) throws HireWorldException {
       Session session = null;
    
        try {
            session = factory.openSession();
	    session.beginTransaction();
	    session.saveOrUpdate(applicant);
	    session.getTransaction().commit();

            if (0 == applicant.getId()) {
                HireWorldLogger.displayInfo(applicant.getName() + " Applicant not Inserted ");
            }
        } catch (HibernateException hibernateException) {
            System.out.println(hibernateException);
            session.getTransaction().rollback();            
            throw new HireWorldException("Error occoured while trying to insert " + applicant.getName());
        } finally {
            try {

                if (null != session) {
                    session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return applicant;
    }

    /**
     *{@inheritdoc}
     */
    public List<Applicant> retrieveApplicants() throws HireWorldException {
        List<Applicant> applicants = null;
        Session session = null;  	

        try {
            session = factory.openSession();
            applicants =(List<Applicant>) session.createQuery("FROM Applicant").list();
            HireWorldLogger.displayInfo("Displaying all the Applicants");
        } catch (HibernateException hibernateException) {
            throw new HireWorldException("Error occoured while trying to fetch all Applicants ");
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return applicants;
    }
    
    /**
     *{@inheritdoc}
     */
    public Applicant retrieveApplicantById (int applicantId) throws HireWorldException {
        Applicant applicant = null;
        Session session = null;
        Transaction transaction = null;     	

        try {
            session = factory.openSession();
            applicant = (Applicant) session.get(Applicant.class, applicantId );
        } catch (HibernateException hibernateException) {
            throw new HireWorldException("Error occoured while trying to fetch " + applicant.getName());
        } finally {
            try {

                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return applicant;
    }

    /**
     *{@inheritdoc}
     */ 
    public Applicant updateApplicantById(Applicant updatedApplicant, int applicantId) throws HireWorldException {
        boolean isUpdated = false;
        Applicant applicant = null;
        Session session = null;
        Transaction transaction = null;     	

        try {
            session = factory.openSession();
            applicant = (Applicant) session.get(Applicant.class, applicantId);
	    session.beginTransaction();
            applicant.setName(updatedApplicant.getName());
            applicant.setEmailAddress(updatedApplicant.getEmailAddress());
            applicant.setQualification(updatedApplicant.getQualification());
            applicant.setMobileNumber(updatedApplicant.getMobileNumber());
            applicant.setDateOfBirth(updatedApplicant.getDateOfBirth());
            applicant.setGender(updatedApplicant.getGender());
	    session.getTransaction().commit();
            isUpdated = true;
            HireWorldLogger.displayInfo(applicant.getName() + " Applicant updated ");
        } catch (HibernateException hibernateException) {

            if ( transaction != null ) {
                session.getTransaction().rollback();	
	    }
            throw new HireWorldException("Error occoured while trying to fetch " + applicant.getName());
        } finally {
            try {
                if (null != session) {
                session.close();
                }
            } catch (HibernateException hibernateException) {
		HireWorldLogger.displayError("Error occoured while trying to close the session");
	    }
	}
	return applicant;
    }

    /**
     *{@inheritdoc}
     */    
    public boolean removeApplicantById (int applicantId) throws HireWorldException {
        Applicant applicant = null;
        boolean isRemoved = false;
        Session session = null;
        Transaction transaction = null;     	

        try {
            session = factory.openSession();
	    session.beginTransaction();
            applicant = (Applicant) session.get(Applicant.class, applicantId );
            applicant.setDeleted(true);
            System.out.println(applicant);
	    session.update(applicant);
	    session.getTransaction().commit();

            if ( transaction != null ) {
	        isRemoved = true;
	    }
        } catch (HibernateException hibernateException) {
            session.getTransaction().rollback();
            throw new HireWorldException("Error occoured while trying to delete " + applicant.getName());
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
    public List<Applicant> retriveApplicantsByRecruiterId(int recruiterId) throws HireWorldException {
        List<Applicant> applicants = null;
        Session session = null;	

        try {
            session = factory.openSession();
            session.beginTransaction();
            StringBuilder hqlQuery = new StringBuilder();
            hqlQuery.append("select applicant from Applicant applicant ")
                     .append("JOIN applicant.recruiters recruiter ")
                     .append("where recruiter.id = :recruiterId ")
                     .append("and applicant.isDeleted = false "); 
   
            Query<Applicant> query = session.createQuery(hqlQuery.toString());
            applicants = query.setParameter("recruiterId" , recruiterId).getResultList();	    
        } catch (HibernateException hibernateException) {
            throw new HireWorldException(" \n Error occoured while trying to fetch assigned applicants" );
        } finally {
            try {

                if (null != session) {
                    session.close();
                }
            } catch (HibernateException hibernateException) {
                HireWorldLogger.displayError("\n Error occoured while trying to close the session");
	    }
        }
	return applicants;
    }

}






         
    

        
    












  
  