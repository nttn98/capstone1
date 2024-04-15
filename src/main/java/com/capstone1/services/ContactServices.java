package com.capstone1.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.Contact;

public interface ContactServices {
    List<Contact> getAll();

    Page<Contact> getAllContacts(Pageable p);

    Contact saveContact(Contact contact);

    Contact getContactById(Long id);

    Contact changeStatusContact(Contact contact);
    
    void deleteContactById(Long id);

}
