package org.wheat.leaflets.coders;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes
{
	/**
     * ��Կ�㷨
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * ģʽ/���
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    
    
    /*
     * ����
     */
    public static byte[] encrypt(byte[] data,byte[] keyData,byte[] ivData) throws Throwable
    {
    	SecretKeySpec key=new SecretKeySpec(keyData, KEY_ALGORITHM);
    	Cipher cipher=Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
    	IvParameterSpec iv=new IvParameterSpec(ivData);// ʹ��CBCģʽ����Ҫһ������iv�������Ӽ����㷨��ǿ��
    	cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }
    
    /*
     * ����
     */
    public static byte[] decrypt(byte[] data, byte[] keyData, byte[] ivData)
            throws Throwable {
        SecretKeySpec key = new SecretKeySpec(keyData, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(ivData);// ʹ��CBCģʽ����Ҫһ������iv�������Ӽ����㷨��ǿ��
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }
}
