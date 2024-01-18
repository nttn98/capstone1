package com.capstone1.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.capstone1.model.TokenUser;
import com.capstone1.repository.TokenUserRepository;
import com.capstone1.services.TokenUserService;

import jakarta.transaction.Transactional;

@Service
public class TokenUserServiceImpl implements TokenUserService {

    @Autowired
    TokenUserRepository tokenUserRepository;

    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenUserRepository.deleteByExpirationTimeBefore(now);
    }

    @Override
    public void save(TokenUser token) {
        // tokenUserRepository.alterAutoIncrementValue();
        tokenUserRepository.save(token);
    }

    @Override
    public TokenUser findByToken(String token) {
        return tokenUserRepository.findByToken(token);
    }

    @Override
    public void delete(TokenUser token) {
        tokenUserRepository.delete(token);
    }

    @Override
    public void deleteByUserId(long user_id) {
        tokenUserRepository.deleteByUserId(user_id);
    }

}