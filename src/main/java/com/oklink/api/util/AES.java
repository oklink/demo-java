package com.oklink.api.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * AES加解密算法工具类。
 */
public class AES {

    /**
     * 默认的加密算法和补码方式。
     */
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    private static final String KEY_ALGORITHM = "AES";

    private static final int ivSize = 16;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 使用“AES/CBC/PKCS7Padding”方式进行128位加密。
     * 
     * @param data： 要加密的字符串
     * @param password： 加密时使用的密码
     * @return
     * @throws Exception
     */
    public static byte[] encrypt128(byte[] data, byte[] password) throws Exception {
        return encrypt(data, password, 128);
    }

    /**
     * 使用“AES/CBC/PKCS7Padding”方式进行进行128位解密。
     * 
     * @param data： Base64格式的字符串
     * @param password： 加密时使用的密码
     * @return
     * @throws Exception
     */
    public static byte[] decrypt128(byte[] data, byte[] password) throws Exception {
        return decrypt(data, password, 128);
    }

    /**
     * 使用“AES/CBC/PKCS7Padding”方式进行256位加密。
     * 
     * @param data： 要加密的字符串
     * @param password： 加密时使用的密码
     * @return
     * @throws Exception
     */
    public static byte[] encrypt256(byte[] data, byte[] password) throws Exception {
        return encrypt(data, password, 256);
    }

    /**
     * 使用“AES/CBC/PKCS7Padding”方式进行进行256位解密。
     * 
     * @param data： Base64格式的字符串
     * @param password： 加密时使用的密码
     * @return
     * @throws Exception
     */
    public static byte[] decrypt256(byte[] data, byte[] password) throws Exception {
        return decrypt(data, password, 256);
    }

    /**
     * 使用AES/ECB/PKCS7Padding方式进行加密。
     * 
     * @param data： Base64格式的字符串。
     * @param password： 加密时使用的密码。
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] password, int size) throws Exception {
        byte[] key = initKey(password, size);
        byte[] iv = randomByte(password, ivSize);
        byte[] result = crypto(data, key, iv, ALGORITHM, Cipher.ENCRYPT_MODE);
        return result;
    }

    /**
     * 使用AES/ECB/PKCS7Padding方式进行解密。
     * 
     * @param data： Base64格式的字符串。
     * @param password： 加密时使用的密码。
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] password, int size) throws Exception {
        byte[] key = initKey(password, size);
        byte[] iv = randomByte(password, ivSize);
        byte[] result = crypto(Base64.decode(data), key, iv, ALGORITHM, Cipher.DECRYPT_MODE);
        return result;
    }

    /**
     * 根据用户自己自定的参数进行加密操作。
     * 
     * @param data： 要加密的数据
     * @param key： 加密时使用的key
     * @param iv： 加密中使用到的向量
     * @param algorithm： 加密使用的具体算法，补码方式等信息
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv, String algorithm) throws Exception {
        return crypto(data, key, iv, algorithm, Cipher.ENCRYPT_MODE);
    }

    /**
     * 根据用户自己自定的参数进行解密操作。
     * 
     * @param data： 要加密的数据
     * @param key： 加密时使用的key
     * @param iv： 加密中使用到的向量
     * @param algorithm： 加密使用的具体算法，补码方式等信息
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv, String algorithm) throws Exception {
        return crypto(data, key, iv, algorithm, Cipher.DECRYPT_MODE);
    }

    /**
     * 基础操作，所有参数可定制。
     * 
     * @param data
     * @param key
     * @param iv
     * @param algorithm
     * @param mode
     * @return
     * @throws Exception
     */
    private static byte[] crypto(byte[] data, byte[] key, byte[] iv, String algorithm, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec keyspec = new SecretKeySpec(key, KEY_ALGORITHM);
        if (iv != null) {
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(mode, keyspec, ivspec);
        } else {
            cipher.init(mode, keyspec);
        }

        return cipher.doFinal(data);
    }

    /**
     * 以传入的password为种子产生密钥。
     * 
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] initKey(byte[] password, int length) throws Exception {
        // 实例化密钥生成器
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(length, new SecureRandom(password));
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 根据seed，随机生成一个指定大小的字节数组。
     * <p>
     * 优先使用SHA1PRNG算法，不存在则使用系统提供的算法。
     * 
     * @param seed 密码种子
     * @param size 需要的字节大小
     * @return
     */
    public static byte[] randomByte(byte[] seed, int size) {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
        } catch (NoSuchAlgorithmException exception) {
            sr = new SecureRandom(seed);
        }
        byte[] iv = new byte[size];
        sr.nextBytes(iv);
        return iv;
    }
}
