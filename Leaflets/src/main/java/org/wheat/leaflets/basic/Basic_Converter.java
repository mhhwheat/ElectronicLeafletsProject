package org.wheat.leaflets.basic;

public class Basic_Converter 
{
//	static final String UpperCaseHex = "0123456789ABCDEF";
    public static final String LowerCaseHex = "0123456789abcdef";
    //	static final char HEX_DIGITS_UPPERCASE[]=UpperCaseHex.toCharArray();
    public static final char HEX_DIGITS_LOWERCASE[] = LowerCaseHex.toCharArray();

    /**
     * �ַ�����0-9a-zA-Z\_\- a-z: 10-35 A-Z: 36-61 _: 62 -: 63
     */
    final static int MAX64 = 64;

    /**
     * ����֮����ַ���
     */
    private static final char dic64[] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-".toCharArray();

//	/** ��ָ����ʮ�������ַ���ת��Ϊ64�����ַ��� */
//	final static String converHexTo64(String str) {
//		 
//		final int lens = str.length();
//		
//		
//		int  tempLen=3-(lens%3);
//		 
//		tempLen%=3;
//		
//		int countLens=lens+tempLen;
//		
//		StringBuilder temSb=new StringBuilder();
//		for (int i = 0; i < tempLen; i++) {
//			temSb.append('0');			
//		}
//		
//		System.err.println(str.length());
//		str= temSb.append(str).toString();
//		
//		System.err.println(str+"=>"+str.length());
//		
//		StringBuilder buffer = new StringBuilder((lens << 1) / 3);
//		int dec = 0;
//		byte chr = 0;
//		int end = 0;
//
//		for (int i = 0; i < countLens; i += 3) {
//			end = (i + 3) < countLens ? (i + 3) : countLens;
//			String tmp = str.substring(i, end);
//			dec = Integer.parseInt(tmp, 16);
//			chr = (byte) ((dec < MAX64) ? 0 : (dec >>> 6));//���С��64�����λ��0
//			buffer.append(dic64[chr]);
//
//			buffer.append(dic64[dec & 63]);
//		}
//
//		return buffer.toString();
//	}

    /**
     * <p>
     * ʮ�������ַ�ת��Ϊbyte
     * </p>
     * java��byte��-127��128֮��<br/>
     * 0~Fʮ�������ַ���ʾ�ķ�Χ��0��15<br/>
     *
     * @param c 0~F ʮ�������ַ�
     * @return 0~15
     */
    public static byte hexCharToByte(char c) {
        try {
            c = Character.toLowerCase(c);
        } catch (Throwable e) {
            // handle exception
        }
        return (byte) LowerCaseHex.indexOf(c);
    }


    /**
     * ��byteת��Ϊhex String
     *
     * @param b
     * @return
     */
    public static String bytesToHexString(byte[] b) {

        StringBuilder sb = new StringBuilder(b.length * 2);

        for (int i = 0; i < b.length; i++) {

            sb.append(HEX_DIGITS_LOWERCASE[(b[i] & 0xf0) >>> 4]);

            sb.append(HEX_DIGITS_LOWERCASE[b[i] & 0x0f]);

        }

        return sb.toString();

    }
}
