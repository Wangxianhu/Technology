import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import Util;//参见本文件下的Util
/**
* @Author 王先虎
* 图片上传类
*/
@Controller
public class PicUploadController {
	/**
	 * 上传图片接口 基于base64字符串
	 * @param map 上传json转为的map
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/picUploadBaseStr", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> picUploadBaseStr(@RequestBody Map<String, String> map)throws IOException {
		Map<String, Object> encOption = null;
		try {
			Util.desOption(map);//解密方法，加解密base64图片效率较低，当前类没有用，
			String fileName=map.get("fileName");//图片名
			String fileDataStr=map.get("fileDataStr");//图片base64后的字符串
			String system=map.get("system");//前台给的路径
			System.out.println("fileName:"+fileName+" system:"+system);
			System.out.println("fileDataStr:"+fileDataStr.length());
			System.out.println("fileDataStr:"+fileDataStr);
			Map<String, Object> reMap = new HashMap<String, Object>();
			if (StringUtils.isNotEmpty(fileDataStr)&& StringUtils.isNotEmpty(fileName)) {//判空
				byte[] decode = Base64.getDecoder().decode(fileDataStr);//fileDataStr base64解一下
				Calendar a = Calendar.getInstance();//日历类
				int yy = a.get(Calendar.YEAR);// 年
				int mm = a.get(Calendar.MONTH) + 1;// 由于月份是从0开始的所以加1
				int dd = a.get(Calendar.DATE);// 日
				String datePath = File.separator + "path" + File.separator + "path1"+File.separator+system+ File.separator +yy+ File.separator +mm+ File.separator +dd;//服务器绝对路径名
				// String datePath = File.separator + "path" + File.separator + "path1"+File.separator+system+ File.separator +yy+ File.separator +mm+ File.separator +dd;
				UUID uuidName = UUID.randomUUID();// uuid文件名
				String[] split = fileName.split("\\.");//文件名以点截取
				String suffix = split[split.length - 1];// 文件后缀
				// linux路径类似File.separator+"path"+File.separator+"path1"
				// windows路径类似"E:"+File.separator+"path"
				File folder=new File(datePath);//创建文件夹对象
				if(!folder.exists()) {//判断是否有文件夹实体，如果没有则创建
					folder.mkdirs();//创建文件加实体
					System.out.println("folder"+folder.toString());
				}
				for(int i=0;i<decode.length;++i)  {  
	                if(decode[i]<0){//base64解码后可能有异常数据，调整异常数据  
	                	decode[i]+=256;  
	                }  
	            } 
				OutputStream out = new FileOutputStream(datePath+File.separator + uuidName + "."+ suffix); //用文件路径创建文件输出流     
	            System.out.println("out:");
				out.write(decode);//向服务器写出文件，也可用循环写出提高效率
	            out.flush();//刷新
	            out.close();//关闭
				reMap.put("code", "1000");
				reMap.put("msg", "上传成功");
				Map<String, String> data=new HashMap<>();
				data.put("imgUrl", system+ File.separator +yy+ File.separator +mm+ File.separator +dd+File.separator + uuidName + "."+ suffix);//返回相对路径
				reMap.put("data", data);
			} else {
				reMap.put("code", "2000");
				reMap.put("msg", "参数非法，文件未上传");
			}
			encOption = Util.encOption(reMap);//加密返回的数据
		} catch (Exception e) {
			e.getStackTrace();
		}
		System.out.println("encOption:"+encOption.toString());
		return encOption;
	}
	/**
	*
	*/
	@RequestMapping(value = "/GenerateImage", method = RequestMethod.POST)
	public String GenerateImage(@RequestBody Map<String, String> map){//对字节数组字符串进行Base64解码并生成图片  
	    String imgStr=map.get("imgStr");    
		if (imgStr == null) //图像数据为空  
	    return "false";  
	        try   
	        {  
	            //Base64解码  
	            byte[] b = Base64.getDecoder().decode(imgStr);  
	            for(int i=0;i<b.length;++i)  
	            {  
	                if(b[i]<0)  
	                {//调整异常数据  
	                    b[i]+=256;  
	                }  
	            }  
	            File folder=new File("D:\\tupian");//文件夹
				if(!folder.exists()) {//判断是否有文件夹，如果没有则创建
					folder.mkdirs();
				}
	            //生成jpeg图片  
	            String imgFilePath = "D:\\tupian\\new.jpg";//新生成的图片  
	            OutputStream out = new FileOutputStream(imgFilePath);      
	            out.write(b);  
	            out.flush();  
	            out.close();  
	            return "true";  
	        }   
	        catch (Exception e)   
	        {  
	            return "false";  
	        }  
	}  
	/**
	 * 上传图片接口 基于流
	 * @param file 文件对象
	 * @param request 请求对象
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/picUploadBaseStream",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> picUploadBaseStream(@RequestParam(required=false) CommonsMultipartFile file,HttpServletResponse response) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");//设置允许跨域请求
		Map<String, Object> map;
		if(file==null||file.isEmpty()) {//判空
			map=new HashMap<>();
			map.put("code", "2000");
			map.put("msg", "文件未上传");
		}else {
			InputStream inputStream=file.getInputStream();//获取图片上传输入流
			//String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());//天的文件夹
			Calendar a = Calendar.getInstance();
			int yy = a.get(Calendar.YEAR);// 年
			int mm = a.get(Calendar.MONTH) + 1;// 由于月份是从0开始的所以加1
			int dd = a.get(Calendar.DATE);// 日
			String datePath = yy+ File.separator +mm+ File.separator +dd;
			
			UUID uuidName = UUID.randomUUID();//uuid文件名
			String[] split = file.getOriginalFilename().split("\\.");//文件名以点截取
			String suffix=split[split.length-1];//文件后缀
			File folder=new File(File.separator+"mnt"+File.separator+"data",datePath);//文件夹对象
			if(!folder.exists()) {//判断是否有文件夹实体，如果没有则创建
				folder.mkdirs();
			}
			//linux路径是File.separator+"path"+File.separator+"path1"
			//windows路径是"E:"+File.separator+"path"
			File newFile=new File(File.separator+"path"+File.separator+"path1",datePath+File.separator+ uuidName+"."+suffix);//创建图片文件对象
			OutputStream  outputStream = new FileOutputStream(newFile);//创建图片文件输出流
			int bytesWritten = 0;//读出bytes的偏移量
			int byteCount = 0;//读出字节的数量
			byte[] bytes = new byte[1024];//缓冲大小
			while ((byteCount = inputStream.read(bytes)) != -1){//循环缓冲输出到服务器
				outputStream.write(bytes, bytesWritten, byteCount);
			}
			inputStream.close();//关闭输入流
			outputStream.close();//关闭输出流
			map=new HashMap<>();
			map.put("code", "1000");
			map.put("msg", "上传成功");
			Map<String, Object> map1=new HashMap<>();
			map1.put("imgUrl", datePath+File.separator+ uuidName+"."+suffix);//返回路径
			map.put("data", map1);
		}
		Map<String, Object> encOption = Util.encOption(map);//加密返回值
		return encOption;
	}
}
