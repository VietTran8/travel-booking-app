package com.web.travel.service.impl;

import com.web.travel.model.ContactInfo;
import com.web.travel.repository.ContactInfoRepository;
import com.web.travel.service.interfaces.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;

    @Override
    public ContactInfo saveContactInfo(ContactInfo contactInfo){
        return contactInfoRepository.save(contactInfo);
    }
}
