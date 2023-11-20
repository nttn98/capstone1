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
    TokenUserRepository tokenRepository;

    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteByExpirationTimeBefore(now);
    }

    @Override
    public void save(TokenUser token) {
        tokenRepository.save(token);
    }

    @Override
    public TokenUser findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void delete(TokenUser token) {
        tokenRepository.delete(token);
    }

    @Override
    public void deleteByUserId(long user_id) {
        tokenRepository.deleteByUserId(user_id);
    }

}