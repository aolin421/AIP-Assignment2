package au.edu.uts.aip.ams.member;

import java.security.*;

/**
 * Encryptor for member password
 * @author admin
 */
public class Sha {
  public static String hash256(String data) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(data.getBytes());
    return bytesToHex(md.digest());
  }
  public static String bytesToHex(byte[] bytes) {
    StringBuffer result = new StringBuffer();
    for (byte byt : bytes) {
      result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
    }
    return result.toString();
  }
}
