package com.endava.mss.entities;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ETagUtil {

    public static String calculateETag(byte[] data) {
        try {
        	MessageDigest md = MessageDigest.getInstance("SHA-256");
            
       
            byte[] hashBytes = md.digest(data);
            
            String base64Hash = Base64.getEncoder().encodeToString(hashBytes);
  
            return "\"" + base64Hash + "\"";
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
}