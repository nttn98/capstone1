package com.capstone1.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.capstone1.model.Token;
import com.capstone1.repository.TokenRepository;
import com.capstone1.services.TokenService;

import jakarta.transaction.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenRepository tokenRepository;

    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteByExpirationTimeBefore(now);
    }

    @Override
    public void save(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void delete(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    @Transactional
    public void deleteByUserId(long user_id) {
        tokenRepository.deleteByUserId(user_id);
    }

}