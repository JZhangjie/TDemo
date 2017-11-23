package cn.jdz.glib.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Created by alex on 2017/9/1.
 */

public class FileHelper {

    //region 文件路径操作

    /**
     * 依据relativePath的结尾是否为分隔符判读是文件还是文件夹
     * @param parent
     * @param relativePath
     * @return
     */
    public static File getPath(File parent,String relativePath){

        String path=File.separator;
        if(parent != null)
            path= parent.getPath();
        String filename = null;
        if (relativePath.startsWith(File.separator)) {
            relativePath = relativePath.substring(1);
        }
        List<String> plist = new ArrayList<String>(Arrays.asList(relativePath.split(File.separator)));
        int num = plist.size();
        if (!relativePath.endsWith(File.separator)) {
            filename = plist.get(plist.size() - 1);
            plist.remove(filename);
        }
        for (int i = 0; i < plist.size(); i++) {
            String p = plist.get(i);
            if (p == null || p.trim() == "") {
                continue;
            }
            path += File.separator + p;
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        if (filename != null)
            path = path + "/" + filename;
        return new File(path);
    }

    //endregion

    //region 图片压缩
    public static void compressImage(File file) {
        compressImage(file.getAbsolutePath());
    }

    public static void compressImage(String filepath) {
        compressImage(filepath, filepath);
    }

    public static void compressImage(String filepath, String despath) {
        File file = new File(filepath);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap_bound = BitmapFactory.decodeFile(filepath, options);
            options.inJustDecodeBounds = false;
            float w = options.outWidth;
            float h = options.outHeight;
            float ww = 1024f;
            float hh = 1024f;
            float scale = 1.0f;
            if (w > h && w > ww) {
                scale = w / ww;
            } else if (h > w && h > hh) {
                scale = h / hh;
            }
            if (scale < 0) {
                scale = 1.0f;
            }
            options.inSampleSize = (int) scale;
            Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
            int desw = (int) (w / scale);
            int desh = (int) (h / scale);
            Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap, desw, desh, true);
            File temp = new File(despath);
            if (temp.exists()) {
                temp.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(despath);
                if (newbitmap != null) {
                    newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fos != null)
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public static void compressImage(File file, int size) {
        compressImage(file.getAbsolutePath(), size);
    }

    public static void compressImage(String filepath, int size) {
        compressImage(filepath, filepath, size);
    }

    public static void compressImage(String filepath, String despath, int size) {
        File file = new File(filepath);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
            File temp = new File(despath);
            if (temp.exists()) {
                temp.delete();
            }
            ByteArrayOutputStream fos = null;
            fos = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                int scale = 90;
                while (fos.toByteArray().length / 1024 > size && scale>1) {
                    fos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, scale, fos);
                    if(scale>10)
                        scale -= 10;
                    else
                        scale-=1;
                }
            }
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(temp);
                fos.writeTo(os);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //endregion

    //region 文本文件读取
    public static StringBuilder readFile(String filePath) {
        return readFile(filePath, "UTF-8");
    }

    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    //endregion

    //region 文件(夹)复制

    /**
     * 复制Assert中的文件或文件夹
     * @param oldPath assert中需要复制的路径
     * @param newPath  目标路径
     * @param context  访问assert的上下文
     * @return
     * @throws IOException
     */
    public static boolean copyFiles(String oldPath, String newPath, Context context) throws IOException {
        boolean isCopy = true;
        AssetManager mAssetManger = context.getAssets();
        String[] fileNames=mAssetManger.list(oldPath);// 获取assets目录下的所有文件及有文件的目录名

        if (fileNames.length > 0) {//如果是目录,如果是具体文件则长度为0
            File file = new File(newPath);
            file.mkdirs();//如果文件夹不存在，则递归
            for (String fileName : fileNames) {
                if(oldPath=="")   //assets中的oldPath是相对路径，不能够以“/”开头
                    copyFiles(fileName,newPath+"/"+fileName,context);
                else
                    copyFiles(oldPath+"/"+fileName,newPath+"/"+fileName,context);
            }
        }else {//如果是文件
            InputStream is = mAssetManger.open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        }
        return isCopy;
    }
    //endregion

    //region 文件打包
    public static void zip(ZipOutputStream outputStream, File file, String name){
        try {
            if(file.isFile()){
                outputStream.putNextEntry(new ZipEntry(name));
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                while (fileInputStream.read(buffer)!=-1){
                    outputStream.write(buffer,0,buffer.length);
                }
                fileInputStream.close();
            }
            else if(file.isDirectory()){
                for(File f :file.listFiles()){
                    zip(outputStream,f,name+File.separator+f.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
