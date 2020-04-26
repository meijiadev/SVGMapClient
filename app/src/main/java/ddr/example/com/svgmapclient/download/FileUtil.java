package ddr.example.com.svgmapclient.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ddr.example.com.svgmapclient.common.GlobalParameter;
import ddr.example.com.svgmapclient.other.Logger;


/**
 *作用：文件操作，将文件保存到本地
 *
 */
public class FileUtil {
    private String sdPath;
    public  int MaxLength=1024*2;

    public FileUtil(String path){
        //sdPath=Environment.getExternalStorageDirectory().getPath()+"/"+"机器人"+"/"+path+"/";
        sdPath=GlobalParameter.ROBOT_FOLDER +path+"/";
        createSdDir(sdPath);
    }

    /**
     * 在Sd卡创建目录
     * @return
     */
    public File createSdDir(String sdPath){
        File dir=new File(sdPath);
        if (dir.exists()){
          //  Logger.e("文件夹已存在，无须创建");
        }else {
            Logger.e("创建文件");
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 在Sd卡创建文件
     * @return
     */
    public File createSdFile(String fileName) throws IOException {
        String finalFileName=sdPath+fileName;
        //Logger.e("最终的文件名和路径："+finalFileName);
        File file=new File(finalFileName);
        file.createNewFile();
        return file;
    }

    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public void copy(File source, File  target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);   //字节读取流
            fileOutputStream = new FileOutputStream(target);  //字节输入流
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToSdFromInput(String urlstr,String fileName) throws IOException {
        URL url=new URL(urlstr);
        byte[] downloadBytes;
        File file=createSdFile(fileName);
        OutputStream outputStream=new FileOutputStream(file);
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();  //打开一个HttpURLConnection 的连接
        httpURLConnection.setUseCaches(true);
        InputStream inputStream=httpURLConnection.getInputStream();
        int len=httpURLConnection.getContentLength();
        int count=len/MaxLength;        //取商
        int remainder=len%MaxLength;    //取余数
       // Logger.e("文件长度："+len+"取商："+count+"  取余："+remainder);
        /*if (len>MaxLength){
            for (int i=0;i<count;i++){
                inputStream.read(downloadBytes,0,MaxLength);
                outputStream.write(downloadBytes);
            }
            if (remainder!=0){
                Logger.e("---------->>>>>"+"余数");
                downloadBytes=new byte[remainder];
                inputStream.read(downloadBytes);
                outputStream.write(downloadBytes);
                outputStream.flush();
            }
        }else {*/
        if (len>MaxLength){
            downloadBytes=new byte[MaxLength];
            while (inputStream.read(downloadBytes)!=-1){
                outputStream.write(downloadBytes);
            }
            outputStream.close();
        }else {
            downloadBytes=new byte[len];
            inputStream.read(downloadBytes);
            outputStream.write(downloadBytes);
            outputStream.flush();
        }

    }

    /**
     * 删除指定文件夹下所有文件
     * @param file
     */
    public static void deleteFile(File file){
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files!=null){
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteFile(f);
                }
                file.delete();//如要保留文件夹，只删除文件，请注释这行
            }
        } else if (file.exists()) {
            file.delete();
        }
    }





}
