package common.util.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import common.util.log.UtilLog;
import common.util.string.UtilString;

/**
 * 加密解密
 * 
 * @author fgq 20120815
 * 
 */
public class UtilCrypt {
	public static final String key = "fileTrans.key";
	private static final UtilCrypt instance = new UtilCrypt();

	public static UtilCrypt getInstance() {
		return instance;
	}

	// 构造
	private UtilCrypt() {
	}

	// 初始化Key码
	private Key initKeyForAES(String key) throws NoSuchAlgorithmException {
		if (null == key || key.length() == 0) {
			throw new NullPointerException("key not is null");
		}
		SecretKeySpec key2 = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(key.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			key2 = new SecretKeySpec(enCodeFormat, "AES");
		} catch (NoSuchAlgorithmException e) {
			UtilLog.logError("初始化Key码错误:", e);
		}
		return key2;

	}

	// ------------------以下为AES加密解密------------------------
	/**
	 * AES加密算法，不受密钥长度限制
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public String encryptAES(String content, String key) {
		try {
			SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return asHex(result); // 加密
		} catch (Exception e) {
			UtilLog.logError("加密错误:", e);
			return null;
		}
	}

	/**
	 * aes解密算法，不受密钥长度限制
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public String decryptAES(String content, String key) {
		try {
			if ("".equalsIgnoreCase(UtilString.isNil(content)))
				return "";
			SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, secretKey);// 初始化
			byte[] result = cipher.doFinal(asBytes(content));
			return new String(result); // 加密
		} catch (Exception e) {
			UtilLog.logError("解密错误:", e);
			return null;
		}
	}

	/**
	 * 将2进制数值转换为16进制字符串
	 * 
	 * @param buf
	 * @return
	 */
	private String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;
		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");
			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}
		return strbuf.toString();
	}

	/**
	 * 将16进制转换
	 * 
	 * @param hexStr
	 * @return
	 */
	private byte[] asBytes(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	// ------------------以下为DES加密解密------------------------
	/**
	 * DES加密
	 * 
	 * @param content
	 *            加密的内容
	 * @param keyString
	 *            加密的key
	 * @return
	 */
	/*public String encryptDES(String content, String keyString) {
		try {
			DESKeySpec dks = new DESKeySpec(createDESKey(keyString));
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(
					dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptData = cipher.doFinal(content.getBytes());
			return (new BASE64Encoder()).encodeBuffer(encryptData);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	/**
	 * DES解密
	 * 
	 * @param encrypt
	 *            密文
	 * @param keyString
	 *            解密的key
	 * @return
	 */
	/*public String decryptDES(String encrypt, String keyString) {
		try {
			if ("".equalsIgnoreCase(UtilString.isNil(encrypt)))
				return "";
			DESKeySpec dks = new DESKeySpec(createDESKey(keyString));
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(
					dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptData = cipher.doFinal((new BASE64Decoder())
					.decodeBuffer(encrypt));
			return new String(encryptData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	/**
	 * 创建DES密钥
	 * 
	 * @param keyString
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private byte[] createDESKey(String keyString)
			throws NoSuchAlgorithmException {
		// 如果key为空，则置默认
		if (keyString == null || keyString.trim().equals("")) {
			keyString = key;
		}
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		kg.init(new SecureRandom(keyString.getBytes()));
		SecretKey key = kg.generateKey();
		return key.getEncoded();
	}

	// public static void main(String[] args) {
	// CryptUtil crypt = CryptUtil.getInstance();
	// String content = "otcotcotcotcotcotcotcotcotcotc";
	// String dcontent = crypt.encryptAES(content, CryptUtil.key);
	// System.out.println(content + "加密后:" + dcontent);
	// System.out.println(dcontent + "解密后:"
	// + crypt.decryptAES(dcontent, CryptUtil.key));
	//	}
}