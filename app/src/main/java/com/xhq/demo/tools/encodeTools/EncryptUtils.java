package com.xhq.demo.tools.encodeTools;

import com.xhq.demo.tools.CloseUtils;
import com.xhq.demo.tools.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 密码学的三大作用：加密（ Encryption）、认证（Authentication），鉴定（Identification）<br>
 * 加密：防止坏人获取你的数据。<br>
 * 认证：防止坏人假冒你的身份。<br>
 * 鉴定：防止坏人修改了你的数据而你却并没有发现。
 *
 * hash家族算法 --> 不可逆 的加密
 *
 * Hmac家族算法 --> 收发方 需要约定
 *
 * AES 算法 --> 对称算法
 *
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/6/10.
 *     Desc  : encrypt and decrypt tolls.
 *     Updt  : Description.
 * </pre>
 */
public class EncryptUtils {

    //==========================================MD5 encrypt file==========================================

    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @param salt 盐<br>
     *              所谓加Salt，就是加点“佐料”。其基本想法是这样的——当用户首次提供密码时（通常是注册时），
     *              由系统自动往这个密码里撒一些“佐料”，然后再散列。而当用户登录时，
     *              系统为用户提供的代码撒上同样的“佐料”，然后散列，再比较散列值，已确定密码是否正确。
     *              这个值是由系统随机生成的，并且只有系统知道。这样，即便两个用户使用了同一个密码，
     *              由于系统为它们生成的salt值不同，他们的散列值也是不同的。
     * @return 16进制加盐密文
     */
    public static String encryptMD5AddSalt(String data, String salt) {
        return toHexString(encryptHash((data + salt).getBytes(), "MD5"));
    }


    public static String encryptMD5AddSalt(byte[] data, byte[] salt) {
        if (data == null || salt == null) return null;
        byte[] dataSalt = new byte[data.length + salt.length];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return toHexString(encryptHash(dataSalt, "MD5"));
    }


    /**
     * MD5加密文件
     *
     * @param filePath 文件路径
     * @return 文件的16进制密文
     */
    public static String encryptMD5File2String(String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return encryptMD5File2String(file);
    }


    /**
     * MD5加密文件
     *
     * @param file 文件
     * @return 文件的16进制密文
     */
    public static String encryptMD5File2String(File file) {
        return toHexString(encryptMD5File(file));
    }

    /**
     * MD5加密文件
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static byte[] encryptMD5File(File file) {
        if (file == null) return null;
        FileInputStream fis = null; // 实现了AutoCloseable接口, 可以不用手动关闭
        DigestInputStream digestInputStream;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fis, md);
            byte[] buffer = new byte[8 * 1024];
            while (true) {
                if (!(digestInputStream.read(buffer,0,buffer.length) > 0)) break;
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(fis);
        }
    }


    //====================Hash算法 MD2, MD5, SHA1, SHA224, SHA256(推荐), SHA384, SH1512=====================


    /**
     * see {@link #hashTemplate(byte[], String)}
     * default charset --> utf-8
     *
     * @return 16进制密文
     */
    public static String encryptHash2String(String data, String algorithm) {
//        return encryptHash2String(data.getBytes("GBK"), algorithm);
        return encryptHash2String(data.getBytes(), algorithm);
    }

    /**
     * see {@link #hashTemplate(byte[], String)}
     *
     * @return 16进制密文
     */
    public static String encryptHash2String(byte[] data, String algorithm) {
        return toHexString(encryptHash(data, algorithm));
    }

    /**
     * see {@link #hashTemplate(byte[], String)}
     */
    public static byte[] encryptHash(byte[] data, String algorithm) {
        return hashTemplate(data, algorithm);
    }

    /**
     * 建议使用SHA-256、SHA-3算法。 <P>
     *     Hash算法是指任意长度的字符串输入，此算法能给出固定n比特的字符串输出，输出的字符串一般称为Hash值。
     *     </p>
     *
     * Hash算法可以用来检验数据的完整性, 不可逆的特性使Hash算法成为一种单向密码体制，
     * 只能加密不能解密，可以用来加密用户的登录密码等凭证。
     *<p>
     * hash encryption template
     *
     * @param data      needed encrypted data
     * @param algorithm encryption algorithm : <br>
     *                  MD2, MD5, SHA1, SHA224, SHA256, SHA384, SH1512<br>
     *                  eg: "SHA256"
     * @return Cipher text byte array
     */
    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    //========================HmacMD5,SHA1,SHA224,SHA256,SHA348,SHA512==============================


    /**
     * see {@link #hmacTemplate(byte[], byte[], String)}
     * default charset --> utf-8
     *
     * @return 16进制密文
     */
    public static String encryptHmac2String(String data, String key, String algorithm) {
        return encryptHmac2String(data.getBytes(), key.getBytes(), algorithm);
    }

    /**
     * see {@link #hmacTemplate(byte[], byte[], String)}
     *
     * @return 16进制密文
     */
    public static String encryptHmac2String(byte[] data, byte[] key, String algorithm) {
        return toHexString(encryptHmac(data, key, algorithm));
    }

    /**
     * see {@link #hmacTemplate(byte[], byte[], String)}
     */
    public static byte[] encryptHmac(byte[] data, byte[] key, String algorithm) {
        return hmacTemplate(data, key, algorithm);
    }

    /**
     * 建议使用HmacSHA256算法<p></p>
     *
     * 消息发送者使用MAC算法计算出消息的MAC值，追加到消息后面一起发送给接收者。
     * 接收者收到消息后，用相同的MAC算法计算接收到消息MAC值，并与接收到的MAC值对比是否一样。
     *<p>
     * MAC（Message Authentication Code，消息认证码算法）是含有密钥的散列函数算法，兼容了MD和SHA算法的特性，
     * 并在此基础上加入了密钥, MAC算法主要集合了MD和SHA两大系列消息摘要算法.
     * <p>经过MAC算法得到的摘要值也可以使用十六进制编码表示，其摘要值长度与参与实现的摘要值长度相同。
     * 例如，HmacSHA1算法得到的摘要长度就是SHA1算法得到的摘要长度，都是160位二进制码，换算成十六进制编码为40位。</p>
     *
     * Hmac encryption template
     * <P> encryption algorithm : HmacMD5, HmacSHA1,HmacSHA224,HmacSHA256,HmacSHA348,HmacSHA512</P>
     *
     * @param data      needed encrypted data
     * @param key       keys
     * @param algorithm encryption algorithm : <br>
     *                  HmacMD5,HmacSHA1,HmacSHA224,HmacSHA256,HmacSHA348,HmacSHA512<br>
     *                  eg: "HmacSHA256"
     * @return Cipher text byte array
     */
    private static byte[] hmacTemplate(byte[] data, byte[] key, String algorithm) {
        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    // ********************** AES DES 3DES , 推荐使用 AES***********************/

    /**
     * AES加密后转为Base64编码
     * default charset --> utf-8
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @return Base64密文
     */
    public static byte[] encryptAES2Base64(byte[] data, byte[] key) {
        return EncodeUtils.base64Encode(encryptAES(data, key));
    }

    /**
     * AES加密后转为16进制
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @return 16进制密文
     */
    public static String encryptAES2HexString(byte[] data, byte[] key) {
        return toHexString(encryptAES(data, key));
    }

    /**
     * AES加密
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @return 密文
     */
    public static byte[] encryptAES(byte[] data, byte[] key) {
        return desTemplate(data, key, "AES", "AES/CBC/PKCS5Padding", true);
    }


    /**
     * AES解密16进制密文
     * default charset --> utf-8
     *
     * @param hexString 16进制密文
     * @param key  16、24、32字节秘钥
     * @return 明文
     */
    public static byte[] decryptHexStringAES(String hexString, byte[] key) {
        return decryptAES(hexString2Bytes(hexString), key);
    }

    /**
     * AES解密Base64编码密文
     *
     * @param base64Array Base64编码密文
     * @param key  16、24、32字节秘钥
     * @return 明文
     */
    public static byte[] decryptBase64AES(byte[] base64Array, byte[] key) {
        return decryptAES(EncodeUtils.base64Decode(base64Array), key);
    }

    /**
     * AES解密
     *
     * @param data 密文
     * @param key  16、24、32字节秘钥
     * @return 明文
     */
    public static byte[] decryptAES(byte[] data, byte[] key) {
        return desTemplate(data, key, "AES", "AES/CBC/PKCS5Padding", false);
    }

    /**
     *
     * 对称加密算法建议使用AES算法, 不要使用ECB模式，不建议使用DES算法。
     * <P>
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding
     *
     * </p>
     *     Android 提供的AES加密算法API默认使用的是ECB模式，
     *     所以要显式指定加密算法为：CBC或CFB模式，可带上PKCS5Padding填充。
     * </P>
     *
     * <p>
     * 在对称加密算法中，数据发信方将明文（原始数据）和加密密钥一起经过特殊加密算法处理后，使其变成复杂的加密密文发送出去。
     * 收信方收到密文后，若想解读原文，则需要使用加密用过的密钥及相同算法的逆算法对密文进行解密，才能使其恢复成可读明文。
     * </p>
     * ES encrypt template
     *
     * @param data           needed encrypted data
     * @param key            keys
     * @param algorithm      encrypt algorithm<br>
     *                       String algorithm = "AES";
     * @param mode 转变<br>
     *                       String transformation = "AES/CBC/PKCS5Padding";
     * @param isEncrypt      {@code true}: encrypt {@code false}: decrypt
     * @return 密文或者明文，适用于AES(推荐)，deprecated(3DES，DES)
     */
    private static byte[] desTemplate(byte[] data, byte[] key, String algorithm, String mode, boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(mode);
            SecureRandom random = new SecureRandom();
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, random);
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 产生HmacSHA256, AES等等算法的 摘要算法的密钥
     */
    public static byte[] genKey(String algorithm) throws NoSuchAlgorithmException {
        // 初始化HmacMD5摘要算法的密钥产生器
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        if("AES".equals(algorithm)){
            generator.init(256); // AES密钥长度最少是128位，推荐使用256位。
        }
        SecretKey secretKey = generator.generateKey();// 产生密钥
        return secretKey.getEncoded(); // 获得密钥
    }


    /**
     * 将字节数组转换为十六进制的字符串显示
     *
     * @param bytes 字节数组
     * @return 十六进制字符串；
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        StringBuffer buf = new StringBuffer(len << 1);
        final char hexChar[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (byte aByte : bytes) {
            buf.append(hexChar[(aByte & 0xf0) >>> 4]);
            buf.append(hexChar[aByte & 0x0f]);
        }
        return buf.toString();
    }


    /**
     * hexString转byteArr
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    private static byte[] hexString2Bytes(String hexString) {
        if (StringUtils.isSpace(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }


    //解密(进制变更)
    public static String decrypt(String txt) {
        StringBuilder src = new StringBuilder(64);
        int l1, n = 1, srclen = 0;
        String s;
        for (int i = 0, l = txt.length(); i < l; ) {
            l1 = Integer.parseInt(txt.substring(i, i + 1), 21) - 10;
            s = txt.substring(++i, i + l1);
            if (i > 1) {
                src.append((char) (Integer.parseInt(s, 36) - n * 10 - srclen));
                n++;
            } else {
                srclen = Integer.parseInt(s, 36) - 10;
            }
            i += l1;
        }
        return src.toString();
    }

    //加密(进制变更)
    public static String encrypt(String txt) {
        if (txt == null || txt.length() == 0) return "";
        StringBuilder src = new StringBuilder(64);
        int i, l = txt.length();
        String s;
        s = Integer.toString(l + 10, 36);
        src.append(Integer.toString(s.length() + 10, 21)).append(s);
        for (i = 0; i < l; i++) {
            s = Integer.toString(txt.charAt(i) + (i + 1) * 10 + l, 36);
            src.append(Integer.toString(s.length() + 10, 21)).append(s);
        }
        return src.toString();
    }



    //===================================================================================================
    //===================================================================================================
    //===================================================================================================



    public static String encryptPerson(String s) {
        try {
//            return EncryptPerson.encrypt(URLEncoder.encode(s, "utf-8").replace("+", "%20").getBytes());
            return EncryptPerson.encrypt(s.getBytes("utf-8"));
        } catch (Exception e) {
            return EncryptPerson.encrypt(s.getBytes());
        }
    }

    public static String decryptPerson(String s) {
        try {
            return new String(EncryptPerson.decrypt(s), "utf-8");
        } catch (Exception e) {
            return new String(EncryptPerson.decrypt(s));
        }
    }


    //得到字符串校验码
    public static String getSecurityCode(String src) {
        String md5_1 = encryptHash2String("DFP-JJKJ-Valid-" + src, "MD5").toLowerCase();
        String md5 = encryptHash2String(md5_1 + "-2nd", "MD5").toLowerCase();
        return md5.substring(23, 28) + md5.substring(1, 6) + md5.substring(11, 16);
    }

    //==================================================================================================



}