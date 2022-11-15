package com.ideas2it.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ideas2it.controller.RecruiterController;
import com.ideas2it.dao.impl.RecruiterDaoImpl;
import com.ideas2it.dao.RecruiterDao;
import com.ideas2it.dto.DataTransformation;
import com.ideas2it.exception.HireWorldException;
import com.ideas2it.model.Recruiter;
import com.ideas2it.model.RecruiterDTO;
import com.ideas2it.service.RecruiterService;
import com.ideas2it.util.constant.Constant;
import com.ideas2it.util.Validation;

public class RecruiterServiceImpl implements RecruiterService { 
    private String nameregex = Constant.NAME_PATTERN;
    private String emailAddressregex = Constant.EMAILADDRESS_PATTERN;
    private String mobileNumberregex = Constant.MOBILE_NUMBER_PATTERN;
    private int id = Constant.ZERO;
        
    private RecruiterDao recruiterDao = new RecruiterDaoImpl();
    private DataTransformation scoutDTO = new DataTransformation();

    /**
     *{@inheritdoc}
     */
    public RecruiterDTO createRecruiter ( String name, String emailAddress,long mobileNumber)throws HireWorldException {
        RecruiterDTO recruiterDTO = new RecruiterDTO(name,emailAddress,mobileNumber);
        Recruiter recruiter = recruiterDao.insertRecruiter(scoutDTO.convertToRecruiter(recruiterDTO)); 
        return scoutDTO.convertToRecruiterDTO(recruiter);
    } 

    /**
     *{@inheritdoc}
     */
    public boolean isValidName(String name) throws HireWorldException {
        return Validation.isValidString(name,nameregex);
    }
    
    /**
     *{@inheritdoc}
     */
    public boolean isValidEmailAddress(String emailAddress)throws HireWorldException {
        return Validation.isValidString(emailAddress, emailAddressregex);
    }
    
    /**
     *{@inheritdoc}
     */
    public boolean isValidMobileNumber(long mobileNumber) throws HireWorldException {
        return Validation.isValidMobileNumber(mobileNumber, mobileNumberregex);
    }
    
     /**
     *{@inheritdoc}
     */
    public RecruiterDTO getRecruiterById(int id) throws HireWorldException {
        Recruiter recruiter = recruiterDao.retrieveRecruiterById(id);

        if (null != recruiter) {
        return scoutDTO.convertToRecruiterDTO(recruiter);
        } else {
            throw new HireWorldException("No Details Found"); 
        }
    }
    
    /**
     *{@inheritdoc}
     */
    public RecruiterDTO updateRecruiterById(String value, int recruiterId, int choice) throws HireWorldException {
    RecruiterDTO updatedRecruiter;

    if (Constant.ZERO == recruiterId) {
            throw new HireWorldException(Constant.RECRUITER_NOT_FOUND);
        } else {
            updatedRecruiter = getRecruiterById (recruiterId);

            switch (choice ) {
                case Constant.SET_RECRUITER_NAME:
                    updatedRecruiter.setName(value);
                    break;
                case Constant.SET_RECRUITER_EMAIL_ADDRESS:
                    updatedRecruiter.setEmailAddress(value); 
                    break;
                case Constant.SET_RECRUITER_MOBILE_NUMBER:                    
                    long mobileNumber =Long.parseLong(value);
                    updatedRecruiter.setMobileNumber(mobileNumber);
                    break; 
            }
        }
        return scoutDTO.convertToRecruiterDTO(recruiterDao.updateRecruiterById(scoutDTO.convertToRecruiter(updatedRecruiter), recruiterId));
    }
    
    /**
     *{@inheritdoc}
     */
    public boolean removeRecruiterById(int recruiterId) throws HireWorldException {
        return recruiterDao.removeRecruiterById(recruiterId);
    }
   
    /**
     *{@inheritdoc}
     */
    public List<RecruiterDTO> displayRecruiters()throws HireWorldException {
        List<RecruiterDTO> recruitersDTO = new ArrayList<>();        
        List<Recruiter> recruiters = recruiterDao.retrieveRecruiters();
  
        if (!recruiters.isEmpty()) {
            for (int i = 0; i < recruiters.size(); i++) {
                recruitersDTO.add(scoutDTO.convertToRecruiterDTO(recruiters.get(i)));
            }
        } else {
            throw new HireWorldException("No Details Found");
        }
        return recruitersDTO;
    }

    /**
     *{@inheritdoc}
     */
    public boolean isIdExist(int recruiterId) throws HireWorldException { 
        if(null != getRecruiterById(recruiterId)) {
            return true;
        }
        return false;
    }

    /**
     *{@inheritdoc}
     */
    public RecruiterDTO assignApplicant(RecruiterDTO recruiterDTO) throws HireWorldException  {
        return scoutDTO.convertToRecruiterDTO(recruiterDao.insertRecruiter(scoutDTO.convertToRecruiter(recruiterDTO)));
    }

    /**
     *{@inheritdoc}
     */
    public List<RecruiterDTO> getRecruitersByApplicantId(int applicantId) throws HireWorldException {
        List<RecruiterDTO> recruitersDTO = new ArrayList<>();        
        List<Recruiter> recruiters = recruiterDao.retriveRecruitersByApplicantId(applicantId);
  
        if (!recruiters.isEmpty()) {
            for (int i = 0; i < recruiters.size(); i++) {
                recruitersDTO.add(scoutDTO.convertToRecruiterDTO(recruiters.get(i)));
            }
        } else {
            throw new HireWorldException("No Details Found");
        }
        return recruitersDTO;
    }   
}

    
    






    



   
 
   
    