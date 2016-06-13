package com.oklink.api.util;

import org.bitcoinj.core.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Random;

/**
 * 可以和CryptoJS互相加解密使用的工具类。
 * <p>
 * 使用条件： 需要替换“\jre\lib\security”中的两个jar包。根据jre版本下载相应的修订包。
 * <p>
 * jdk1.6: http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html
 * <p>
 * jdk1.7: http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
 * <p>
 * jdk1.8: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 */
public class AESForCryptoJS {

    private static int keySize = 8; // 8 words = 256-bit
    private static int ivSize = 4; // 4 words = 128-bit

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 将密码和盐拼接后使用MD5加密，获取实际密码。
     * 
     * @param password
     * @param salt
     * @return
     */
    public static String getEncodedPass(String password, String salt) {
        try {
            byte[] input = (password + salt).getBytes();

            // 使用MD5摘要算法进行加密。
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input);
            byte[] md = digest.digest();

            String result = Utils.HEX.encode(md);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用密码对数据进行加密，并将结果转换为Base64编码的字符串。
     * 
     * @param data
     * @param password
     * @return
     * @throws Exception
     */
    public static String encryptToBase64(String data, String password) throws Exception {
        byte[] result = encrypt(data, password);
        return Base64.toBase64String(result);
    }

    /**
     * 使用密码对数据进行加密。
     * 
     * @param data
     * @param password
     * @return 密文的结构为: “Salted__”(8位) + saltBytes(8位) + data
     * @throws Exception
     */
    public static byte[] encrypt(String data, String password) throws Exception {
        byte[] salt = getSalt();
        byte[] javaKey = new byte[keySize * 4];
        byte[] javaIv = new byte[ivSize * 4];
        // 从password中获取key和iv。
        evpKDF(password.getBytes("UTF-8"), keySize, ivSize, salt, javaKey, javaIv);

        byte[] original = AES.encrypt(data.getBytes(), javaKey, javaIv, AES.ALGORITHM);

        // 密文的结构为: “Salted__”(8位) + saltBytes(8位) + data
        byte[] result = new byte[original.length + 16];
        System.arraycopy("Salted__".getBytes(), 0, result, 0, 8);
        System.arraycopy(salt, 0, result, 8, 8);
        System.arraycopy(original, 0, result, 16, original.length);

        return result;
    }

    /**
     * 使用password对data进行解密。
     * 
     * @param data: CryptoJS加密产生的Base64编码的信息。
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String password) throws Exception {
        byte[] data_decoded = Base64.decode(data);

        // 密文解密后的结构为: “Salted__”(8位) + saltBytes(8位) + data
        byte[] salt = Arrays.copyOfRange(data_decoded, 8, 16);
        byte[] ciphertextBytes = Arrays.copyOfRange(data_decoded, 16, data_decoded.length);

        byte[] javaKey = new byte[keySize * 4];
        byte[] javaIv = new byte[ivSize * 4];

        evpKDF(password.getBytes("UTF-8"), keySize, ivSize, salt, javaKey, javaIv);

        byte[] original = AES.decrypt(ciphertextBytes, javaKey, javaIv, AES.ALGORITHM);
        return new String(original);
    }

    /**
     * 获取一个8位的字节数组做盐。
     * 
     * @return
     */
    private static byte[] getSalt() {
        byte[] salt = new byte[8];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("找不到指定的算法：SHA1PRNG");
            Random rand = new Random();
            rand.nextBytes(salt);
        }
        return salt;
    }

    /**
     * evpKDF通过salt将密码分离为key和iv的算法模仿。
     * <p>
     * 来源：
     * http://stackoverflow.com/questions/29151211/how-to-decrypt-an-encrypted-aes-256-string-from
     * -cryptojs-using-java
     * 
     * @param password
     * @param keySize
     * @param ivSize
     * @param salt
     * @param resultKey
     * @param resultIv
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static byte[] evpKDF(byte[] password, int keySize, int ivSize, byte[] salt,
            byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return evpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }

    private static byte[] evpKDF(byte[] password, int keySize, int ivSize, byte[] salt,
            int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv)
            throws NoSuchAlgorithmException {
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);
            block = hasher.digest(salt);
            hasher.reset();

            // Iterations
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }

            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                    Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));

            numberOfDerivedWords += block.length / 4;
        }

        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);

        return derivedBytes; // key + iv
    }
}
