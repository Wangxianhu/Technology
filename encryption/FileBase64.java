import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import sun.misc.BASE64Encoder;
/**
 * �����ļ���base64�ִ�֮���ת��
 * @author wangxianhu
 */
public class FileBase64 {
	/*
	 * ע��
	 * 1���ӽ���Ҳ���������淽�������÷�����ͬС�죨���淽�����л��У�
	 * 	new BASE64Encoder().encode(data);//����
	 * 	new BASE64Decoder().decodeBuffer(imgStr);//����
	 * 2�������ϵ�base64���ܺ���ж�����Ϣ��data:image/png;base64,������֮ǰҪȥ��
	 */
	/**
	 * �ַ���תͼƬ
	 * @param imgStr ͼƬ�ַ���
	 * @param path ȫ·��
	 * @return
	 */
	public static boolean generateImage(String imgStr, String path) {
		if (imgStr == null)
			return false;
		try {
			byte[] b = Base64.getDecoder().decode(imgStr);//����	
			for (int i = 0; i < b.length; ++i) {// ��������
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);//�½�
			out.write(b);//д��
			out.flush();//ˢ��
			out.close();//�ر�
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * ͼƬת�ַ��� 
	 * @param imgFile ȫ·��
	 * @return
	 */
	public static String getImageStr(String imgFile) {
	    InputStream inputStream = null;
	    byte[] data = null;
	    try {
	        inputStream = new FileInputStream(imgFile);//�½�
	        data = new byte[inputStream.available()];
	        inputStream.read(data);//����
	        inputStream.close();//�ر�
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    byte[] encode = Base64.getEncoder().encode(data);//����   
	    return new String(encode);// ����
	}
	/**
	 * ��������
	 * @param args
	 */
	public static void main(String[] args) {
	    String strImg = getImageStr("C:/Users/wangxianhu/Desktop/�½��ļ���/red.png");
	    System.out.println(strImg);
	    generateImage(strImg, "C:/Users/wangxianhu/Desktop/red1123.png");
	}
}
