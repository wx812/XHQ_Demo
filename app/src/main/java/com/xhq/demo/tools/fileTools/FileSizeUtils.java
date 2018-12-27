package com.xhq.demo.tools.fileTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/10/9.
 *     Desc  : File Tools
 *     Updt  : 2071/12/20
 * </pre>
 */
public class FileSizeUtils{
    public static final int SIZE_B = 1;
    public static final int SIZE_KB = 2;
    public static final int SIZE_MB = 3;
    public static final int SIZE_GB = 4;
    public static final int KB = 1024;    // KB与Byte的倍数
    public static final int MB = 1048576;    // MB与Byte的倍数
    public static final int GB = 1073741824;    // GB与Byte的倍数


    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     */
    public static String bytes2KB(long bytes){
        BigDecimal fileSize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(KB * KB);
        float returnValue = fileSize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if(returnValue > 1) return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(KB);
        returnValue = fileSize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }


    /**
     * see {@link #getDirLength(File)}
     */
    public static long getDirLength(String dirPath){
        return getDirLength(FileUtils.getFileByPath(dirPath));
    }


    /**
     * @return 文件大小
     */
    public static long getDirLength(File dir){
        if(!FileUtils.isDirAndExist(dir)) return -1;
        long len = 0;
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                if(file.isDirectory()){
                    len += getDirLength(file);
                }else{
                    len += file.length();
                }
            }
        }
        return len;
    }


    /**
     * 获取目录大小
     *
     * @param dirPath 目录路径
     * @return 文件大小
     */
    public static String getDirSize(String dirPath){
        return getDirSize(FileUtils.getFileByPath(dirPath));
    }


    /**
     * 获取目录大小
     */
    public static String getDirSize(File dir){
        long len = getDirLength(dir);
        return len == -1 ? "" : formatFileSize(len);
    }


    /**
     * @param path file or directory
     * @return file or directory size
     */
    private static long getSize(String path){
        File file = new File(path);
        long blockSize = 0;
        try{
            if(file.isDirectory()){
                blockSize = getDirLength(file);
            }else{
//                blockSize = getFileSize(file);
                blockSize = file.length();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return blockSize;
    }

    /**
     * see {@link #getSize(String)} and {@link #formatFileSize(long)}
     */
    public static String getFileOrDirSize(String filePath){
        return formatFileSize(getSize(filePath));
    }


    /**
     * see {@link #getSize(String)} and {@link #formatFileSize(long, int)}
     */
    public static double getFileOrDirSize(String filePath, int sizeType){
        return formatFileSize(getSize(filePath), sizeType);
    }



    /**
     * @param file file
     * @return file size
     */
    public static long getFileLength(File file){
        if(!FileUtils.isFileAndExist(file)) return -1;
        return file.length();
    }


    public static long getFileLength(String filePath){
        return getFileLength(FileUtils.getFileByPath(filePath));
    }


    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return the length of this file in bytes.
     */
    public static String getFileSize(String filePath){
        return getFileSize(FileUtils.getFileByPath(filePath));
    }


    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return the length of this file in bytes.
     */
    public static String getFileSize(File file){
        long len = getFileLength(file);
        return len == -1 ? "" : formatFileSize(len);
    }



    /**
     * @return long Return type
     */
    private static long getFileSizeByStream(File file){
        long size = 0;
        if(file.exists()){
            FileInputStream fis = null;
            try{
                fis = new FileInputStream(file);
                size = fis.available();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                IOUtil.closeIO(fis);
            }
        }
        return size;
    }


    /**
     * the size of the unit conversion file
     *
     * @param byteNum file size
     * @return file size of string
     */
    private static String formatFileSize(long byteNum){
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSize;
        String wrongSize = "0B";
        if(byteNum == 0) return wrongSize;
        if(byteNum < KB){
            fileSize = df.format((double)byteNum) + "B";
        }else if(byteNum < MB){
            fileSize = df.format((double)byteNum / KB) + "KB";
        }else if(byteNum < GB){
            fileSize = df.format((double)byteNum / MB) + "MB";
        }else{
            fileSize = df.format((double)byteNum / GB) + "GB";
        }
        return fileSize;
    }


    // storage, G M K B
    public static String formatSize(long byteNum) {
        if (byteNum >= GB) {
            float gb = (float) byteNum / GB;
            return String.format(Locale.getDefault(), "%.1f GB", gb);
        } else if (byteNum >= MB) {
            float mb = (float) byteNum / MB;
            return String.format(mb > 100 ? "%.0f MB" : "%.1f MB", mb);
        } else if (byteNum >= KB) {
            float kb = (float) byteNum / KB;
            return String.format(kb > 100 ? "%.0f KB" : "%.1f KB", kb);
        } else return String.format(Locale.getDefault(),"%d B", byteNum);
    }


    /**
     * @return 字节数
     */
    public static long formatFileSize2Byte(long memorySize, final int unit){
        if(memorySize < 0) return -1;
        switch(unit){
            default:
            case SIZE_B:
                return memorySize;
            case SIZE_KB:
                return memorySize * KB;
            case SIZE_MB:
                return memorySize * MB;
            case SIZE_GB:
                return memorySize * GB;
        }
    }


    /**
     * @return the formatted size according to the formatted storage unit(B, KB, MB, GB)
     */
    private static double formatFileSize(final long byteNum, final int sizeType){
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSize = 0;
        switch(sizeType){
            case SIZE_B:
                fileSize = Double.valueOf(df.format((double)byteNum));
                break;
            case SIZE_KB:
                fileSize = Double.valueOf(df.format((double)byteNum / KB));
                break;
            case SIZE_MB:
                fileSize = Double.valueOf(df.format((double)byteNum / MB));
                break;
            case SIZE_GB:
                fileSize = Double.valueOf(df.format((double)byteNum / GB));
                break;
            default:
                break;
        }
        return fileSize;
    }

}
