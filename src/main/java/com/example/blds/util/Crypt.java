package com.example.blds.util;

import com.example.blds.entity.HzConsult;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.List;

/**
 * 加密解密
 */
public class Crypt {

    public static Base64 base64=new Base64();

    public static void main(String[] args) throws Exception {
        String s=desEncrypt("1",Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        System.out.println(s);
    }

    /**
     * 加密
     * @param input  明文
     * @param strkey  秘钥
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String input,String strkey) throws Exception
    {
        Base64 base64d = new Base64();
        DESedeKeySpec p8ksp = null;
        p8ksp = new DESedeKeySpec(base64d.decode(strkey.getBytes()));
        Key key = null;
        key = SecretKeyFactory.getInstance("DESede").generateSecret(p8ksp);

        byte[] plainBytes = (byte[])null;
        Cipher cipher = null;
        byte[] cipherText = (byte[])null;
        //“算法/模式/填充”
        plainBytes = input.getBytes("UTF8");
        cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        SecretKeySpec myKey = new SecretKeySpec(key.getEncoded(), "DESede");
        cipher.init(1, myKey);
        cipherText = cipher.doFinal(plainBytes);
        return removeBR(new String(base64.encode(cipherText)));
    }

    /**
     * 解密
     * @param cipherText 密文
     * @param strkey 秘钥
     * @return
     * @throws Exception
     */
    public static String desDecrypt(String cipherText,String strkey) throws Exception
    {
        cipherText = cipherText.replaceAll(" ", "+").replace("%2B", "+");
        Base64 base64d = new Base64();
        DESedeKeySpec p8ksp = null;
        p8ksp = new DESedeKeySpec(base64d.decode(strkey.getBytes()));
        Key key = null;
        key = SecretKeyFactory.getInstance("DESede").generateSecret(p8ksp);

        Cipher cipher = null;
        byte[] inPut = base64d.decode(cipherText.getBytes());
        //“算法/模式/填充”
        cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        SecretKeySpec myKey = new SecretKeySpec(key.getEncoded(), "DESede");
        cipher.init(2, myKey);
        byte[] output = cipher.doFinal(inPut);
        return new String(output, "UTF8");
    }

    public static Integer desDecryptByInteger(String cipherText,String strkey){
        try {
            return Integer.parseInt(desDecrypt(cipherText,strkey));
        } catch (Exception e) {
            throw new RuntimeException("解密出现错误！");
        }
    }

    public static List<HzConsult> desEncryptConsultList(List<HzConsult> consultList, String strkey){
        consultList.forEach((e)->{
            try {
                e.setConsult_id(desEncrypt(e.getId().toString(),strkey));
                e.setId(null);
            } catch (Exception e1) {
                throw new RuntimeException("加密错误");
            }
        });
        return consultList;
    }

    private static String removeBR(String str) {
        StringBuffer sf = new StringBuffer(str);

        for (int i = 0; i < sf.length(); ++i)
        {
            if (sf.charAt(i) == '\n')
            {
                sf = sf.deleteCharAt(i);
            }
        }
        for (int i = 0; i < sf.length(); ++i){
            if (sf.charAt(i) == '\r')
            {
                sf = sf.deleteCharAt(i);
            }
        }

        return sf.toString();
    }

}
