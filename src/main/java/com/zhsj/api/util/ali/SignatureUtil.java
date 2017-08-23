package com.zhsj.api.util.ali;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.lang3.StringUtils;

public class SignatureUtil
{
  public static byte[] sign(byte[] text, byte[] privateKeyData, String algorithm)
    throws GeneralSecurityException
  {
    PrivateKey privateKey = getPrivateKey(privateKeyData, algorithm);
    Signature signatureChecker = Signature.getInstance(algorithm);
    signatureChecker.initSign(privateKey);
    signatureChecker.update(text);
    return signatureChecker.sign();
  }

  public static boolean verify(byte[] text, byte[] signedText, byte[] publicKeyData, String algorithm)
    throws GeneralSecurityException
  {
    PublicKey publicKey = getPublicKey(publicKeyData, algorithm);
    Signature signatureChecker = Signature.getInstance(algorithm);
    signatureChecker.initVerify(publicKey);
    signatureChecker.update(text);
    return signatureChecker.verify(signedText);
  }

  public static PublicKey getPublicKey(byte[] keyData, String algorithm)
    throws GeneralSecurityException
  {
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyData);
    KeyFactory keyFactory = KeyFactory.getInstance(
      StringUtils.substringAfter(algorithm, "with"));
    PublicKey publicKey = keyFactory.generatePublic(keySpec);
    return publicKey;
  }

  public static PrivateKey getPrivateKey(byte[] keyData, String algorithm)
    throws GeneralSecurityException
  {
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
    KeyFactory keyFactory = KeyFactory.getInstance(
      StringUtils.substringAfter(algorithm, "with"));
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    return privateKey;
  }
}