package com.mmnaseri.utils.samples.spring.data.jpa.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:10 PM)
 */
public class EncryptionUtils {

  public static String encrypt(String text) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-1");
      return new String(digest.digest(text.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      return text;
    }
  }
}
