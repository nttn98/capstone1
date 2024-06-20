package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Contact;

public interface ContactServices {
    List<Contact> getAll();

    Contact saveContact(Contact contact);

    Contact getContactById(Long id);

    Contact changeStatusContact(Contact contact);

    void deleteContactById(Long id);

}
