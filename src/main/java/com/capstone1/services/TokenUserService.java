package com.capstone1.services;

import com.capstone1.model.TokenUser;

public interface TokenUserService {

    public void cleanExpiredTokens();

    public void save(TokenUser token);

    public void delete(TokenUser token);

    public void deleteByUserId(long user_id);

    public TokenUser findByToken(String token);
}
