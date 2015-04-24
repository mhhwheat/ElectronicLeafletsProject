/** 
 * description��
 * @author wheat
 * date: 2015-4-9  
 * time: ����9:40:22
 */ 
package org.wheat.leaflets.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import android.util.Base64;

/** 
 * description:
 * @author wheat
 * date: 2015-4-9  
 * time: ����9:40:22
 */
public class SerializableUtil 
{
	public static <E> String list2String(List<E> list) throws IOException{
        //ʵ����һ��ByteArrayOutputStream��������װ��ѹ������ֽ��ļ�
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Ȼ�󽫵õ����ַ�����װ�ص�ObjectOutputStream
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        //writeObject ��������д���ض���Ķ����״̬���Ա���Ӧ��readObject���Ի�ԭ��
        oos.writeObject(list);
        //�����Base64.encode���ֽ��ļ�ת����Base64���룬����String��ʽ����
        String listString = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));
        //�ر�oos
        oos.close();
        return listString;
        }
     
    public static  String obj2Str(Object obj)throws IOException
    {
        if(obj == null) {
            return "";
        }
              //ʵ����һ��ByteArrayOutputStream��������װ��ѹ������ֽ��ļ�
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //Ȼ�󽫵õ����ַ�����װ�ص�ObjectOutputStream
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                //writeObject ��������д���ض���Ķ����״̬���Ա���Ӧ��readObject���Ի�ԭ��
                oos.writeObject(obj);
                //�����Base64.encode���ֽ��ļ�ת����Base64���룬����String��ʽ����
                String listString = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));
                //�ر�oos
                oos.close();
                return listString;
    }
     
    //�����л������ݻ�ԭ��Object
    public static Object str2Obj(String str) throws StreamCorruptedException,IOException{
        byte[] mByte = Base64.decode(str.getBytes(),Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(mByte);
        ObjectInputStream ois = new ObjectInputStream(bais);
         
        try {
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
     
    }
     
    public static <E> List<E> string2List(String str) throws StreamCorruptedException,IOException{
        byte[] mByte = Base64.decode(str.getBytes(),Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(mByte);
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<E> stringList = null;
        try {
            stringList = (List<E>) ois.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringList;
        }
}
