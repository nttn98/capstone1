package com.capstone1.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.capstone1.model.TokenAdmin;
import com.capstone1.repository.TokenAdminRepository;
import com.capstone1.services.TokenAdminService;

import jakarta.transaction.Transactional;

@Service
public class TokenAdminServiceImpl implements TokenAdminService {

    @Autowired
    TokenAdminRepository tokenAdminRepository;

    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenAdminRepository.deleteByExpirationTimeBefore(now);
    }

    @Override
    public void save(TokenAdmin token) {
        tokenAdminRepository.save(token);
    }

    @Override
    public TokenAdmin findByToken(String token) {
        return tokenAdminRepository.findByToken(token);
    }

    @Override
    public void delete(TokenAdmin token) {
        tokenAdminRepository.delete(token);
    }

    @Override
    @Transactional
    public void deleteByStaffId(long staff_id) {
        tokenAdminRepository.deleteByStaffId(staff_id);
    }

}