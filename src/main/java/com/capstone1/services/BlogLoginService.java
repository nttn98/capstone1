package com.capstone1.services;

import java.util.List;

import com.capstone1.model.BlogLogin;

public interface BlogLoginService {
    List<BlogLogin> getAllBlogLogins();

    List<BlogLogin> getBlogLoginsByStaffId(long id);

    BlogLogin saveBlogLogin(BlogLogin blogLogin);
}
