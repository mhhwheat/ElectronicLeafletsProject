package org.wheat.leaflets.coders;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.wheat.leaflets.basic.Basic_Converter;


public class Coder_Md5 
{
	/**
	 * md5����
	 * 
	 * @param val
	 * @return
	 */
	public static String md5(String val) {

		try {

			String result = null;

			if (val != null && val.length() > 0) {
				try {

					MessageDigest md5 = MessageDigest.getInstance("MD5");
					byte[] buff = val.getBytes();
					md5.update(buff, 0, buff.length);
					result = String.format("%032x",
							new BigInteger(1, md5.digest()));
				} catch (Throwable e) {
				}
			}

			return result;

		} catch (Throwable e) {
			// handle Throwable
		}
		return "";
	}

	/**
	 * md5����
	 * 
	 * @MD5ʵ�ֹ���
	 */
	public static String md5(byte[] source) {
		String s = null;
		char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			MessageDigest md = MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
										// ���ֽڱ�ʾ���� 16 ���ֽ�
			char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
											// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
											// ת���� 16 �����ַ���ת��
				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��,
															// >>>
															// Ϊ�߼����ƣ�������λһ������
				str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ����Ľ��ת��Ϊ�ַ���

		} catch (Throwable e) {

		}
		return s;
	}

	/**
	 * ��ȡ�ļ���Md5ֵ
	 * 
	 * @param file
	 * @return
	 */
	public static String getMD5SUM(File file) {
		try {
			if (file == null) {
				return null;
			}

			if (!file.exists()) {
				return null;
			}

		} catch (Throwable e) {
			// handle exception
		}

		InputStream fis = null;

		byte[] buffer = new byte[1024];

		int numRead = 0;

		MessageDigest md5;

		try {

			fis = new FileInputStream(file);

			md5 = MessageDigest.getInstance("MD5");

			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			return Basic_Converter.bytesToHexString(md5.digest());

		} catch (Throwable e) {
			// handle exception
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Throwable e2) {
				// handle exception
			}
		}

		return null;
	}
	
	
	/**
	 * У���ļ���md5
	 * 
	 * @param file
	 * @param md5Sum
	 * @return
	 */
	public static boolean checkMd5Sum(File file, String md5Sum) {
		try {

			String fileMd5 = getMD5SUM(file);

			if (fileMd5 == null) {
				System.out.println("md5 of file is null");

				return false;
			}

			fileMd5 = fileMd5.toLowerCase();
			
			System.out.println("md5 of file is :" + fileMd5);

			if (md5Sum == null){

				System.out.println("md5 of file from server is null");
				return false;
			}

			md5Sum = md5Sum.toLowerCase();

			System.out.println("md5 of file from server is :" + md5Sum);

			return fileMd5.equals(md5Sum);

		} catch (Throwable e) {
			// handle exception
			e.printStackTrace();
		}

		return false;
	}
}
