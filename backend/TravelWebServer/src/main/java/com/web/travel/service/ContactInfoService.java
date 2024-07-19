package com.web.travel.service;

import com.web.travel.model.ContactInfo;
import com.web.travel.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactInfoService {
    @Autowired
    ContactInfoRepository contactInfoRepository;
    public ContactInfo saveContactInfo(ContactInfo contactInfo){
        return contactInfoRepository.save(contactInfo);
    }
}
