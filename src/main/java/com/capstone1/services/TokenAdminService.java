package com.capstone1.services;

import com.capstone1.model.TokenAdmin;

public interface TokenAdminService {

    public void cleanExpiredTokens();

    public void save(TokenAdmin token);

    public void delete(TokenAdmin token);

    public void deleteByStaffId(long staffId);

    public TokenAdmin findByToken(String token);
}
