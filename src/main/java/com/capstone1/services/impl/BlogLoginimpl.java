package com.capstone1.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.BlogLogin;
import com.capstone1.repository.BlogLoginRepository;
import com.capstone1.services.BlogLoginService;

@Service
public class BlogLoginimpl implements BlogLoginService {
    @Autowired
    private BlogLoginRepository blogLoginRepository;

    @Override
    public List<BlogLogin> getAllBlogLogins() {
        return blogLoginRepository.findAll();
    }

    @Override
    public List<BlogLogin> getBlogLoginsByStaffId(long id) {
        return blogLoginRepository.findById(id);
    }

    @Override
    public BlogLogin saveBlogLogin(BlogLogin blogLogin) {
        blogLoginRepository.alterAutoIncrementValue();
        return blogLoginRepository.save(blogLogin);

    }

}
