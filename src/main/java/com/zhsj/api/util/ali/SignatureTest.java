package com.zhsj.api.util.ali;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SignatureTest
{
  private static String PRI_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK8InM7etajbugXg12yzmQeUfTXry7qE2Pq8BbrFPl1Gi4Y1vSVEGLa3Ikvcq56hWxrUhUILBoxBQZTYsFXeFohbN2U2bqs1ztSFXbEWSYcHPRGAhvPJD66sY4/gQp/n2G9oaBOgRELvAyuv4/BPdHxhEUkWsB33hOM+IwtRrdkPAgMBAAECgYAPZZ2NoQ+v0+xQOhAZMs9CWHCPPrfEJ42CuOl0HYe34s3BN5b04UGfSQrOS5IZOS5vdMRoI9ommlLvigQw+YFXxg13a7Cv/2S6YaXz5C4UU6Q7vcJfJyc2xPSuSqw03yJ6Pr6Rsh+dM9Pnl3yqvAlGaViJKmwcLw32WeAzq9ZhCQJBAN4bPE6waBXIA3nF42ileLFar4+RXTyYnVchdGW8U8Les2FnGQ5VVfy/6b3NZOasF/bi0CMA1ZS3iwLIbWWJ+ZMCQQDJvnE8SYhwadoLADjeRD/+YTu1RdVcFz5Dp0whvC6Zb8r7d28mMNqWSxlL/58Mxfl1MlAlDUNcW1YyRUcbbiAVAkEAzQkvfyTysUjXjI7WBEDdTjx9XXfALrRka48CuPhGCtszlXNregPlQVKZIRJ/V0L/THbN1cJraMKXnZYRB2976QJBAMk6DMZebhn6awNYCGEi4FXB9IVeD2/Yu0FN6LEpe62OBYAH5DTKu+z3IVdds+5Qtf7A3ZHnhc3kSLjBwyhIEvUCQQCSYCKHTeNPYpRlWm1OxO9zP0Ss1+caBIXeATqMxNFe8q0DH/Dy0PM7qm2mp8r/Xsb1o9bjmien035/OT7WqJaZ";

  private static String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvCJzO3rWo27oF4Ndss5kHlH0168u6hNj6vAW6xT5dRouGNb0lRBi2tyJL3KueoVsa1IVCCwaMQUGU2LBV3haIWzdlNm6rNc7UhV2xFkmHBz0RgIbzyQ+urGOP4EKf59hvaGgToERC7wMrr+PwT3R8YRFJFrAd94TjPiMLUa3ZDwIDAQAB";
  private static final String TEST_TEXT = "request_xml=xzxkAeMfPkE2Q+90pWZIQm65Dvq447z8I840OrrTYpt4e06KA4z1AjvSjM4w8UOOsyxsyxJmQZ54SVlEP13c+wgZ7LDJYnFtOTlbnDs7RIjZP7YtfyRpqJXoHUjH9z4DOYPKZy5q6TywIY1helvJDCWrbDIQbPiZGOgs+wnSgSYn5ebdbhKjrMQtY4Xv3BXEOkyi0NdY5AjxzlrQUfpRC+oV4aRLw5doj5ncv+xf8HwiVV06X7XlRs7yzF/D7lPgQvXgQsnL6IWDH6DCUo7Aphrk+qR2jCGmIWhHhs5PKrB1bjYU7Hpycwafo4K96Nr2Z/mVGWADQnkNaY3sfVl5QqdwhMMKb5E8+5ydzZevX0P//o7xZnI46TDkdUzOzLpYRLOo+Lqsc43JKp4ViHpXx7T58ljmHqR/ywiPaygqGIg=&service_type=recharge_apply";
  private static final String ALGORITHM = "SHA1withRSA";
  private static final String SIGN_TEXT = "request_xml=xzxkAeMfPkE2Q+90pWZIQm65Dvq447z8I840OrrTYpt4e06KA4z1AjvSjM4w8UOOsyxsyxJmQZ54SVlEP13c+wgZ7LDJYnFtOTlbnDs7RIjZP7YtfyRpqJXoHUjH9z4DOYPKZy5q6TywIY1helvJDCWrbDIQbPiZGOgs+wnSgSYn5ebdbhKjrMQtY4Xv3BXEOkyi0NdY5AjxzlrQUfpRC+oV4aRLw5doj5ncv+xf8HwiVV06X7XlRs7yzF/D7lPgQvXgQsnL6IWDH6DCUo7Aphrk+qR2jCGm+XjBbKDcHIIX5rUJJDHihI/iZZEkW7lc8x98Nd484I/lYwYnKdV2hf5fqfNz1LEEufrtsUYaHDKoG4nZ4GM77NYiEHDBk7Wn8VtiExT9YJexG1ro0r3B7N8vbbPzR/yvx4VxsqRs5ro=&service_type=recharge_apply";
  private static final String SIGN = "Yd+JHM+MzWrEGCqDKtah/HOJ8jHb0nx06xw8+vrtTyCGY536F2enXCxHvkCfJMPfABhCj9IMNxtxkgDShBKvwN6MdoEs49/2CJJh2G8Dm3WhZaCVsOhDXJjKZJxVxvAWviQCV3V25CO7m83sg9q8dM8F3QZIyBirLsqI1Ta92xw=";

  public static void testRSA()
  {
    SignUtils signUtils = new SignUtils();

    System.out.println(signUtils.verify("request_xml=xzxkAeMfPkE2Q+90pWZIQm65Dvq447z8I840OrrTYpt4e06KA4z1AjvSjM4w8UOOsyxsyxJmQZ54SVlEP13c+wgZ7LDJYnFtOTlbnDs7RIjZP7YtfyRpqJXoHUjH9z4DOYPKZy5q6TywIY1helvJDCWrbDIQbPiZGOgs+wnSgSYn5ebdbhKjrMQtY4Xv3BXEOkyi0NdY5AjxzlrQUfpRC+oV4aRLw5doj5ncv+xf8HwiVV06X7XlRs7yzF/D7lPgQvXgQsnL6IWDH6DCUo7Aphrk+qR2jCGmIWhHhs5PKrB1bjYU7Hpycwafo4K96Nr2Z/mVGWADQnkNaY3sfVl5QqdwhMMKb5E8+5ydzZevX0P//o7xZnI46TDkdUzOzLpYRLOo+Lqsc43JKp4ViHpXx7T58ljmHqR/ywiPaygqGIg=&service_type=recharge_apply", "Yd+JHM+MzWrEGCqDKtah/HOJ8jHb0nx06xw8+vrtTyCGY536F2enXCxHvkCfJMPfABhCj9IMNxtxkgDShBKvwN6MdoEs49/2CJJh2G8Dm3WhZaCVsOhDXJjKZJxVxvAWviQCV3V25CO7m83sg9q8dM8F3QZIyBirLsqI1Ta92xw=", PUB_KEY, "SHA1withRSA"));
  }

  private static String decode(String value) {
    try {
      return URLDecoder.decode(value, "UTF-8"); } catch (UnsupportedEncodingException e) {
    }
    return value;
  }

  public static void main(String[] args)
  {
    testRSA();
  }
}