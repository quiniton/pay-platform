package com.quiniton.payplatform.common;

import cn.hutool.core.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String ENCODE_ALGORITHM = "SHA-256";
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

    /**
     * 签名
     *
     * @param privateKey 私钥
     * @param plainText  明文
     * @return
     */
    public static byte[] sign(PrivateKey privateKey, String plainText) {
        MessageDigest messageDigest;
        byte[] signed = null;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plainText.getBytes());
            byte[] outputDigest_sign = messageDigest.digest();
            System.out.println("SHA-256加密后-----》" + bytesToHexString(outputDigest_sign));
            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(outputDigest_sign);
            signed = Sign.sign();
            System.out.println("SHA256withRSA签名后-----》" + bytesToHexString(signed));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            LOGGER.info("SHA-256加密失败:{}", e.getMessage());
        }
        return signed;
    }

    /**
     * 签名
     *
     * @param privateKey
     * @param plainText
     * @return
     */
    public static String sign(String privateKey, String plainText) {
        try {
            PrivateKey key = getPrivateKey(privateKey);
            byte[] bytes = sign(key, plainText);
            return new String(bytes);
        } catch (Exception e) {
            LOGGER.info("SHA-256加密获取private key失败:{}", e.getMessage());
        }
        return "";
    }

    /**
     * 验签
     *
     * @param publicKey 公钥
     * @param plainText 明文
     * @param signed    签名
     */
    public static boolean verifySign(PublicKey publicKey, String plainText, byte[] signed) {

        MessageDigest messageDigest;
        boolean SignedSuccess = false;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plainText.getBytes());
            byte[] outputDigest_verify = messageDigest.digest();
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(outputDigest_verify);
            SignedSuccess = verifySign.verify(signed);
            LOGGER.info("验证成功？---{}", SignedSuccess);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            LOGGER.info("验证失败？---{}", e.getMessage());
        }
        return SignedSuccess;
    }

    /**
     * 验签
     *
     * @param publicKey 公钥
     * @param plainText 明文
     * @param signed    签名
     */
    public static boolean verifySign(String publicKey, String plainText, String signed) {

        MessageDigest messageDigest;
        boolean SignedSuccess = false;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plainText.getBytes());
            byte[] outputDigest_verify = messageDigest.digest();
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            PublicKey key = getPublicKey(publicKey);
            verifySign.initVerify(key);
            verifySign.update(outputDigest_verify);
            SignedSuccess = verifySign.verify(signed.getBytes());
            LOGGER.info("验证成功？---{}", SignedSuccess);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            LOGGER.info("验证失败？---{}",e.getMessage());
        }
        return SignedSuccess;
    }

    /**
     * bytes[]换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] keyBytes = Base64.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(ENCODE_ALGORITHM);

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    /**
     * 还原公钥
     *
     * @param publicKey
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(ENCODE_ALGORITHM);
            PublicKey key = factory.generatePublic(x509EncodedKeySpec);
            return key;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.info("获取public key error :{}", e.getMessage());
        }
        return null;
    }

}
