package com.zhsj.api.util.ali;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

public class SymmtricCryptoUtil
{
  public static byte[] symmtricCrypto(byte[] text, byte[] keyData, String algorithm, int mode)
    throws GeneralSecurityException
  {
    String fullAlg = algorithm + "/CBC/PKCS5Padding";
    byte[] iv = initIv(fullAlg);
    return doCrypto(text, keyData, iv, fullAlg, "CBC", "PKCS5Padding", mode);
  }

  public static byte[] symmtricCrypto(byte[] text, byte[] keyData, String algorithm, String padding, int mode)
    throws GeneralSecurityException
  {
    String fullAlg = algorithm + "/CBC/" + padding;
    byte[] iv = initIv(fullAlg);
    return doCrypto(text, keyData, iv, fullAlg, "CBC", padding, mode);
  }

  public static byte[] symmtricCrypto(byte[] text, byte[] keyData, String algorithm, String workingMode, String padding, int mode)
    throws GeneralSecurityException
  {
    String fullAlg = algorithm + "/" + workingMode + "/" + padding;
    byte[] iv = null;
    if (StringUtils.equals(workingMode, "CBC")) {
      iv = initIv(fullAlg);
    }
    return doCrypto(text, keyData, iv, fullAlg, workingMode, padding, mode);
  }

  public static InputStream getInputStream(File file, byte[] keyData, String algorithm, int mode)
    throws IOException, GeneralSecurityException
  {
    return getInputStream(file, keyData, algorithm, "CBC", "PKCS5Padding", 
      mode);
  }

  public static InputStream getInputStream(File file, byte[] keyData, String algorithm, String workingMode, String padding, int mode)
    throws IOException, GeneralSecurityException
  {
    String fullAlg = algorithm + "/CBC/PKCS5Padding";

    FileInputStream fileInputStream = new FileInputStream(file);

    byte[] iv = initIv(fullAlg);
    Cipher cipher = getCipher(keyData, iv, fullAlg, workingMode, mode);
    return new CipherInputStream(fileInputStream, cipher);
  }

  public static OutputStream getOutputStream(File file, byte[] keyData, String algorithm, int mode)
    throws IOException, GeneralSecurityException
  {
    return getOutputStream(file, keyData, algorithm, "CBC", "PKCS5Padding", 
      mode);
  }

  public static OutputStream getOutputStream(File file, byte[] keyData, String algorithm, String workingMode, String padding, int mode)
    throws IOException, GeneralSecurityException
  {
    String fullAlg = algorithm + "/CBC/PKCS5Padding";

    FileOutputStream fileOutputStream = new FileOutputStream(file);

    byte[] iv = initIv(fullAlg);
    Cipher cipher = getCipher(keyData, iv, fullAlg, workingMode, mode);
    return new CipherOutputStream(fileOutputStream, cipher);
  }

  public static byte[] doCrypto(byte[] text, byte[] keyData, byte[] iv, String fullAlg, String workingMode, String padding, int mode)
    throws GeneralSecurityException
  {
    if ((!StringUtils.equals(workingMode, "CBC")) && 
      (!StringUtils.equals(workingMode, "ECB"))) {
      throw new GeneralSecurityException("错误的工作模式,目前KMI只支持CBC和ECB两种工作模式");
    }

    if ((!StringUtils.equals(padding, "PKCS5Padding")) && 
      (!StringUtils.equals(padding, "NoPadding"))) {
      throw new GeneralSecurityException(
        "错误的填充模式,目前KMI只支持PKCS5Padding和NoPadding两种工作模式");
    }

    if ((mode != 1) && (mode != 2)) {
      throw new GeneralSecurityException(
        "错误的加解密标识,目前KMI只支持Cipher.ENCRYPT_MODE和Cipher.DECRYPT_MODE");
    }

    Cipher cipher = getCipher(keyData, iv, fullAlg, workingMode, mode);
    return cipher.doFinal(text);
  }

  private static Cipher getCipher(byte[] keyData, byte[] iv, String fullAlg, String workingMode, int mode)
    throws GeneralSecurityException
  {
    Cipher cipher = Cipher.getInstance(fullAlg);
    SecretKey secretKey = new SecretKeySpec(keyData, 
      StringUtils.substringBefore(fullAlg, "/"));

    if (StringUtils.equals(workingMode, "CBC")) {
      IvParameterSpec ivSpec = new IvParameterSpec(iv);
      cipher.init(mode, secretKey, ivSpec);
    } else {
      cipher.init(mode, secretKey);
    }
    return cipher;
  }

  private static byte[] initIv(String fullAlg)
    throws GeneralSecurityException
  {
    Cipher cipher = Cipher.getInstance(fullAlg);
    int blockSize = cipher.getBlockSize();
    byte[] iv = new byte[blockSize];
    for (int i = 0; i < blockSize; i++) {
      iv[i] = 0;
    }
    return iv;
  }
}