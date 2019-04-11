import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import sun.misc.BASE64Encoder;
/**
 * 用于文件与base64字串之间的转换
 * @author wangxianhu
 */
public class FileBase64 {
	/*
	 * 注：
	 * 1、加解密也可以用下面方法与现用方法大同小异（下面方法会有换行）
	 * 	new BASE64Encoder().encode(data);//加密
	 * 	new BASE64Decoder().decodeBuffer(imgStr);//解密
	 * 2、网络上的base64加密后会有多余信息（data:image/png;base64,）解密之前要去掉
	 */
	/**
	 * 字符串转图片
	 * @param imgStr 图片字符串
	 * @param path 全路径
	 * @return
	 */
	public static boolean generateImage(String imgStr, String path) {
		if (imgStr == null)
			return false;
		try {
			byte[] b = Base64.getDecoder().decode(imgStr);//解密	
			for (int i = 0; i < b.length; ++i) {// 处理数据
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);//新建
			out.write(b);//写入
			out.flush();//刷新
			out.close();//关闭
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 图片转字符串 
	 * @param imgFile 全路径
	 * @return
	 */
	public static String getImageStr(String imgFile) {
	    InputStream inputStream = null;
	    byte[] data = null;
	    try {
	        inputStream = new FileInputStream(imgFile);//新建
	        data = new byte[inputStream.available()];
	        inputStream.read(data);//读入
	        inputStream.close();//关闭
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    byte[] encode = Base64.getEncoder().encode(data);//加密   
	    return new String(encode);// 加密
	}
	/**
	 * 测试用例
	 * @param args
	 */
	public static void main(String[] args) {
	    String strImg = getImageStr("C:/Users/wangxianhu/Desktop/新建文件夹/red.png");
	    System.out.println(strImg);
	    generateImage(strImg, "C:/Users/wangxianhu/Desktop/red1123.png");
	}
}
