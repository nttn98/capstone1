package com.capstone1.controller;

import java.security.MessageDigest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class Encoding {

    public Encoding() {

    }

    public String toSHA1(String str) {

        String rs = null;
        try {
            byte[] dataBytes = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            rs = Base64.encodeBase64String(md.digest(dataBytes));
        } catch (Exception e) {
            System.out.println(e);

        }
        return rs;
    }

}
