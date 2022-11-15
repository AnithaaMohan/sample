package com.ideas2it.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ideas2it.controller.ApplicantController;
import com.ideas2it.dao.ApplicantDao;
import com.ideas2it.dao.impl.ApplicantDaoImpl;
import com.ideas2it.dto.DataTransformation;
import com.ideas2it.enumeration.Qualification;
import com.ideas2it.exception.HireWorldException;
import com.ideas2it.model.Applicant;
import com.ideas2it.model.ApplicantDTO;
import com.ideas2it.service.ApplicantService;
import com.ideas2it.util.constant.Constant;
import com.ideas2it.util.DateUtil;
import com.ideas2it.util.Validation;

public class ApplicantServiceImpl implements ApplicantService { 
    private String nameRegex = Constant.NAME_PATTERN;
    private String emailAddressRegex = Constant.EMAILADDRESS_PATTERN;		
    private String mobileNumberRegex = Constant.MOBILE_NUMBER_PATTERN;
    private String dateOfBirthRegex = Constant.DATE_OF_BIRTH_PATTERN;
    private String genderRegex = Constant.GENDER_PATTERN;
    private int id = Constant.ZERO;
    
    private ApplicantDao applicantDao = new ApplicantDaoImpl();
    private DataTransformation aspirantDTO = new DataTransformation ();   
    
    /**
     *{@inheritdoc}
     */
    public ApplicantDTO createApplicant (String name,String emailAddress,String qualification,
            long mobileNumber,Date dateOfBirth, char gender) throws HireWorldException {     

        ApplicantDTO applicantDTO = new ApplicantDTO(name,emailAddress,qualification,
                dateOfBirth,mobileNumber, gender);
        Applicant applicant = applicantDao.insertApplicant(aspirantDTO.convertToApplicant(applicantDTO)); 
        
        return aspirantDTO.convertToApplicantDTO(applicant);
    } 

    /**
     *{@inheritdoc}
     */
    public boolean isValidName( String name) throws HireWorldException {
        return Validation.isValidString(name, nameRegex);
    }

    /**
     *{@inheritdoc}
     */
    public boolean isValidEmailAddress(String emailAddress) throws HireWorldException{
        return Validation.isValidString (emailAddress, emailAddressRegex);
    }

    /**
     *{@inheritdoc}
     */
    public boolean isValidMobileNumber(long mobileNumber) throws HireWorldException {
        return Validation.isValidMobileNumber(mobileNumber, mobileNumberRegex);
    }

    /**
     *{@inheritdoc}
     */
    public boolean isValidDateOfBirth(String dateOfBirth)throws HireWorldException {
        return Validation.isValidString(dateOfBirth, dateOfBirthRegex);
    }

    /**
     *{@inheritdoc}
     */
    public boolean isValidGender(char gender) throws HireWorldException {
        return Validation.isValidGender(gender, genderRegex);
    }

    /**
     *{@inheritdoc}
     */
    public ApplicantDTO getApplicantById(int id)throws HireWorldException{
        Applicant applicant = applicantDao.retrieveApplicantById(id);

        if(null != applicant) {
            return aspirantDTO.convertToApplicantDTO(applicant);
        } else {
          throw new HireWorldException("No Details Found");    
        }
    }

    /**
     *{@inheritdoc}
     */
    public ApplicantDTO updateApplicantById(String value, int applicantId , int choice)throws HireWorldException {
        ApplicantDTO updatedApplicant;

        if (Constant.ZERO == applicantId) {
            throw new HireWorldException(Constant.APPLICANT_NOT_FOUND);
        } else {
            updatedApplicant = getApplicantById (applicantId);

            switch (choice ) {
                case Constant.SET_APPLICANT_NAME :
                    updatedApplicant.setName(value);
                    break;
                case Constant.SET_APPLICANT_EMAIL_ADDRESS:
                    updatedApplicant.setEmailAddress(value); 
                    break;
                case Constant.SET_APPLICANT_QUALIFICATION:
                    updatedApplicant.setQualification(value);
                    break;
                case Constant.SET_APPLICANT_MOBILE_NUMBER:                    
                    long mobileNumber =Long.parseLong(value);
                    updatedApplicant.setMobileNumber(mobileNumber);
                    break;
                case Constant.SET_APPLICANT_DATE:
                    Date date = DateUtil.getParsedDateOfBirth(value);
                    updatedApplicant.setDateOfBirth(date);
                    break;
                case Constant.SET_APPLICANT_GENDER:
                    char gender = value.charAt(Constant.ZERO);
                    updatedApplicant.setGender(gender);                    
                    break;
            }
        }
        return aspirantDTO.convertToApplicantDTO(applicantDao.updateApplicantById(aspirantDTO.convertToApplicant(updatedApplicant), applicantId));
    }

    /**
     *{@inheritdoc}
     */
    public boolean removeApplicantById(int applicantId) throws HireWorldException {
        return applicantDao.removeApplicantById(applicantId);
    }

    /**
     *{@inheritdoc}
     */
    public List<ApplicantDTO> displayApplicants() throws HireWorldException {
        List<ApplicantDTO> applicantsDTO = new ArrayList<>();        
        List<Applicant> applicants = applicantDao.retrieveApplicants();
  
        if (!applicants.isEmpty()) {
            for (int i = 0; i < applicants.size(); i++) {
                applicantsDTO.add(aspirantDTO.convertToApplicantDTO(applicants.get(i)));
            }
        } else {
            throw new HireWorldException("No Details Found");
        }
        return applicantsDTO;
    }

    /**
     *{@inheritdoc}
     */
    public boolean isIdExist(int applicantId) throws HireWorldException { 
        if(null != getApplicantById(applicantId)) {
            return true;
        }
        return false;
    }

    /**
     *{@inheritdoc}
     */
    public ApplicantDTO assignRecruiter(ApplicantDTO applicantDTO) throws HireWorldException {
        return aspirantDTO.convertToApplicantDTO(applicantDao.insertApplicant(aspirantDTO.convertToApplicant(applicantDTO)));
    }

    /**
     *{@inheritdoc}
     */
    public List<ApplicantDTO> getApplicantsByRecruiterId(int recruiterId) throws HireWorldException {
        List<ApplicantDTO> applicantsDTO = new ArrayList<>();        
        List<Applicant> applicants = applicantDao.retriveApplicantsByRecruiterId(recruiterId);
  
        if (!applicants.isEmpty()) {
            for (int i = 0; i < applicants.size(); i++) {
                applicantsDTO.add(aspirantDTO.convertToApplicantDTO(applicants.get(i)));
            }
        } else {
            throw new HireWorldException("No Details Found");
        }
        return applicantsDTO;
    }    
}    






    