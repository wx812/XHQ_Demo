package com.xhq.demo.tools.fileTools;

/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.fileTools.zip.apache.zip.ZipEntry;
import com.xhq.demo.tools.fileTools.zip.apache.zip.ZipFile;
import com.xhq.demo.tools.fileTools.zip.apache.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipInputStream;

import static com.xhq.demo.tools.fileTools.FileSizeUtils.KB;
import static com.xhq.demo.tools.fileTools.FileUtils.isFileAndExist;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/10/9.
 *     Desc  : File Tools
 *     Updt  : 2017/12/22
 * </pre>
 */
public class FileOperateUtils{

    public static final int SORT_NAME = 0;
    public static final int SORT_LAST_MODIFIED_TIME = 1;

    //=====================================================================================================
    //=====================================================================================================


    /**
     * copy the files by byte stream buffer(BufferedOutputStream)
     *
     * @param srcFilePath source file path and already exist
     * @param destFilePath destination file path
     * @param coverLay if the target file already exist, is it cover
     * @return {@code true:} copy success<br>{@code false:} copy fail
     */
    public static boolean copyFile(@NonNull String srcFilePath, @NonNull String destFilePath, boolean coverLay){
        File srcFile = new File(srcFilePath);
        if(!isFileAndExist(srcFile)) return false;
        File descFile = new File(destFilePath);
        if(isFileAndExist(descFile)){
            if(coverLay){
                if(!descFile.delete()) return false;
            }else return false;
        }else if(!FileUtils.makeDirs(descFile)) return false;

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try{
            bis = new BufferedInputStream(new FileInputStream(srcFilePath));
            bos = new BufferedOutputStream(new FileOutputStream(destFilePath));
            byte[] buf = new byte[8192]; // 8 * 1024 = 8kb
            int len;
            while((len = bis.read(buf)) != -1){
                bos.write(buf, 0, len);
            }
            bos.flush();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }finally{
            IOUtil.closeIO(bos, bis);
        }
    }


    /**
     * copy < 50M 的文件
     */
    public static void copyFile(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[16384]; // 16 * 1024 = 16Kb

        int read;
        while((read = is.read(buffer)) != -1) {
            os.write(buffer, 0, read);
        }
        os.flush();
    }


    /**
     * copy the entire directory
     *
     * @param srcDirName 源目录名
     * @param destDirName 目标目录名
     * @param coverLay 如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDir(String srcDirName, String destDirName, boolean coverLay){
        File srcDir = new File(srcDirName);
        if(!FileUtils.isDirAndExist(srcDir)) return false;
        String destDirNames = destDirName;
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        if(!destDirNames.endsWith(File.separator)){
            destDirNames = destDirNames + File.separator;
        }
        File destDir = new File(destDirNames);
        if(destDir.exists()){
            if(coverLay){
                if(!FileUtils.deleteDir(destDirNames)) return false;
            }else return false;
        }else if(!destDir.mkdirs()) return false;

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for(File file : files){
            if(file.isFile()){
                String sFile = destDirName + file.getName();
                flag = copyFile(file.getAbsolutePath(), sFile, false);
                if(!flag) break;// 如果拷贝文件失败，则退出循环
            }
            if(file.isDirectory()){
                String sDir = destDirName + file.getName();
                flag = copyDir(file.getAbsolutePath(), sDir, false);
                if(!flag) break;// 如果拷贝目录失败，则退出循环
            }
        }
        return flag;
    }


    /**
     * 复制或移动目录 see {@link #copyOrMoveFile(File, File, boolean)}
     */
    public static boolean copyOrMoveDir(File srcDir, File destDir, boolean isMove){
        if(!FileUtils.isDirAndExist(srcDir) || !FileUtils.isDirAndExist(destDir)) return false;
        // srcPath : F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
        // destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
        // 为防止以上这种情况出现出现误判，须分别在后面加个路径分隔符
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if(destPath.contains(srcPath)) return false;
        // 目标目录不存在返回false
        if(!FileUtils.makeDirs(destDir)) return false;
        File[] files = srcDir.listFiles();
        for(File file : files){
            File oneDestFile = new File(destPath + file.getName());
            if(file.isFile()){
                if(!copyOrMoveFile(file, oneDestFile, isMove)) return false;
            }else if(file.isDirectory()){
                if(!copyOrMoveDir(file, oneDestFile, isMove)) return false;
            }
        }
        return !isMove || FileUtils.deleteFileOrDir(srcDir);
    }


    /**
     * 复制或移动文件
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @param isMove 是否移动
     * @return {@code true}: 复制或移动成功<br>{@code false}: 复制或移动失败
     */
    private static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove){
        // 源, 目标文件不存在或者不是文件则返回false
        if(!isFileAndExist(srcFile) || !isFileAndExist(destFile)) return false;
        if(!FileUtils.makeDirs(destFile.getParentFile())) return false;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(srcFile);
            boolean b = (isMove && !FileUtils.deleteFileOrDir(srcFile));
            return FileUtils.write2File(fis, destFile, false) && !b;
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }finally{
            IOUtil.closeIO(fis);
        }
    }

    //=====================================================================================================
    //=====================================================================================================


    /**
     * 获取文件编码格式
     */
    public static String getFileCharset(File file){
        int p = 0;
        InputStream is = null;
        try{
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            IOUtil.closeIO(is);
        }
        switch(p){
            case 0xefbb:
                return "UTF-8";
            case 0xfffe:
                return "Unicode";
            case 0xfeff:
                return "UTF-16BE";
            default:
                return "GBK";
        }
    }


    /**
     * 获取文件行数
     */
    public static int getFileLines(File file){
        int fileLines = 1;
        InputStream is = null;
        try{
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[FileSizeUtils.KB];
            int len;
            while((len = is.read(buffer)) != -1){
                for(int i = 0; i < len; ++i){
                    if(buffer[i] == '\n') ++fileLines;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            IOUtil.closeIO(is);
        }
        return fileLines;
    }


    /**
     * 获取文件的MD5校验码
     */
    public static String getFileMD5ToString(File file){
        return StringUtils.bytes2HexString(getFileMD5(file));
    }


    public static byte[] getFileMD5(File file){
        if(file == null) return null;
        DigestInputStream dis = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[FileSizeUtils.KB * 32];
            while(dis.read(buffer) > 0) ;
            md = dis.getMessageDigest();
            return md.digest();
        }catch(NoSuchAlgorithmException | IOException e){
            e.printStackTrace();
        }finally{
            IOUtil.closeIO(dis);
        }
        return null;
    }


    public static String splicePath(String path1, String path2){
        if(path1.endsWith(File.separator)) return path1 + path2;
        return path1 + File.separator + path2;
    }

    //=====================================================================================================
    //=====================================================================================================


    /**
     * 获取目录下所有后缀名为suffix的文件
     * <p>大小写忽略</p>
     *
     * @param dir 目录
     * @param suffix 后缀名
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    public static List<File> searchFileInDir(File dir, String suffix, boolean isRecursive){
        if(isRecursive) return searchFileBySuffix(dir, suffix);
        if(dir == null || !FileUtils.isDirAndExist(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                if(file.getName().toUpperCase().endsWith(suffix.toUpperCase())){
                    list.add(file);
                }
            }
        }
        return list;
    }


    public static List<File> searchFileBySuffix(File dir, String suffix){
        if(dir == null || !FileUtils.isDirAndExist(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                if(file.getName().toUpperCase().endsWith(suffix.toUpperCase())){
                    list.add(file);
                }
                if(file.isDirectory()){
                    list.addAll(searchFileBySuffix(file, suffix));
                }
            }
        }
        return list;
    }


    /**
     * 获取目录下所有符合filter的文件
     *
     * @param dir 目录
     * @param filter 过滤器
     * @param isRecursive 是否递归进子目录
     * @return 文件列表
     */
    public static List<File> searchFileInDir(File dir, FilenameFilter filter, boolean isRecursive){
        if(isRecursive) return searchFileInDir(dir, filter);
        if(dir == null || !FileUtils.isDirAndExist(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                if(filter.accept(file.getParentFile(), file.getName())){
                    list.add(file);
                }
            }
        }
        return list;
    }


    public static List<File> searchFileInDir(File dir, FilenameFilter filter){
        if(dir == null || !FileUtils.isDirAndExist(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                if(filter.accept(file.getParentFile(), file.getName())){
                    list.add(file);
                }
                if(file.isDirectory()){
                    list.addAll(searchFileInDir(file, filter));
                }
            }
        }
        return list;
    }


    /**
     * 获取目录下指定文件名的文件包括子目录
     * <p>大小写忽略</p>
     *
     * @param dir 目录
     * @param fileName 文件名
     * @return 文件链表
     */
    public static List<File> searchFileInDir(File dir, String fileName){
        if(dir == null || !FileUtils.isDirAndExist(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                if(file.getName().toUpperCase().equals(fileName.toUpperCase())){
                    list.add(file);
                }
                if(file.isDirectory()){
                    list.addAll(searchFileInDir(file, fileName));
                }
            }
        }
        return list;
    }

    //=====================================================================================================
    //=====================================================================================================


    public static void sort(List<FileInfo> files, final int type){
        switch(type){
            case SORT_NAME:
                sortByName(files);
                break;
            case SORT_LAST_MODIFIED_TIME:
                sortByLastModifiedTime(files);
                break;
        }
        // Collections.reverse(files);
    }


    private static void sortByLastModifiedTime(List<FileInfo> files){
        Collections.sort(files, (f1, f2) -> {
            if((!f1.IsDir || !f2.IsDir) && (f1.IsDir || f2.IsDir)){
                return f1.IsDir ? -1 : 1;
            }else{
                if(f1.ModifiedDate > f2.ModifiedDate) return 1;
                if(f1.ModifiedDate < f2.ModifiedDate) return -1;
            }
            return 0;
        });
    }


    private static void sortByName(List<FileInfo> files){
        Collections.sort(files, (f1, f2) -> (f1.IsDir && f2.IsDir) || (!f1.IsDir && !f2.IsDir)
                ? f1.name.compareToIgnoreCase(f2.name) : f1.IsDir ? -1 : 1);
    }

    //=====================================================================================================
    //=====================================================================================================


    /**
     * 压缩文件或目录
     *
     * @param srcDir 压缩的根目录file
     * @param fileName 根目录下的待压缩的文件名或文件夹名，其中*或""表示跟目录下的全部文件
     * @param destFile 目标zip文件
     */
    public static void zipFiles(@NonNull File srcDir, String fileName, @NonNull File destFile){
        if(!FileUtils.isDirAndExist(srcDir)) return;
        String dirPath = srcDir.getAbsolutePath();
        ZipOutputStream zipOS = null;
        try{
            zipOS = new ZipOutputStream(new FileOutputStream(destFile));
            if("*".equals(fileName) || "".equals(fileName)){
                zipDirectoryToZipFile(dirPath, srcDir, zipOS);
            }else{
                File file = new File(srcDir, fileName);
                if(file.isFile()){
                    zipFilesToZipFile(dirPath, file, zipOS);
                }else{
                    zipDirectoryToZipFile(dirPath, file, zipOS);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            IOUtil.closeIO(zipOS);
        }
    }


    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到destDirName目录下
     *
     * @param zipFileName 需要解压的ZIP文件
     * @param destDirName 目标目录
     */
    public static boolean unZipFiles(String zipFileName, String destDirName){
        String destDirNames = destDirName;
        if(!destDirNames.endsWith(File.separator)){
            destDirNames = destDirNames + File.separator;
        }
        ZipFile zipFile = null;
        OutputStream os = null;
        InputStream is = null;
        try{
            // 根据ZIP文件创建ZipFile对象
            zipFile = new ZipFile(zipFileName);
            byte[] buf = new byte[4096];    // 4 *1024 = 4kb
            int readByte;
            @SuppressWarnings("rawtypes")   // 获取ZIP文件里所有的entry
                    Enumeration enums = zipFile.getEntries();
            while(enums.hasMoreElements()){
                ZipEntry entry = (ZipEntry)enums.nextElement();
                String entryName = entry.getName();
                String descDir = destDirNames + entryName;
                if(entry.isDirectory()){
                    new File(descDir).mkdirs();
                    continue;
                }else{
                    new File(descDir).getParentFile().mkdirs();
                }
                File file = new File(descDir);
                os = new FileOutputStream(file);
                is = zipFile.getInputStream(entry);
                while((readByte = is.read(buf)) != -1){
                    os.write(buf, 0, readByte);
                }
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            IOUtil.closeIO(is, os, zipFile);
        }
    }

    public static synchronized String unZip(@NonNull String zipFile, String targetDir) {
        File file = new File(zipFile);
        if(!file.exists()){
            return null;
        }else{
            File targetFolder = new File(targetDir);
            if(!targetFolder.exists()) targetFolder.mkdirs();

            String dataDir = null;
            short BUFFER = 4096;
            FileInputStream fis = null;
            ZipInputStream zis = null;
            FileOutputStream fos = null;
            BufferedOutputStream dest = null;

            try{
                fis = new FileInputStream(file);
                zis = new ZipInputStream(new BufferedInputStream(fis));

                while(true){
                    while(true){
                        String strEntry;
                        java.util.zip.ZipEntry entry;
                        do{
                            if((entry = zis.getNextEntry()) == null) return dataDir;
                            strEntry = entry.getName();
                        }while(strEntry.contains("../"));

                        if(entry.isDirectory()){
                            String count1 = targetDir + File.separator + strEntry;
                            File data1 = new File(count1);
                            if(!data1.exists()) data1.mkdirs();
                            if(TextUtils.isEmpty(dataDir)) dataDir = data1.getPath();
                        }else{
                            byte[] data = new byte[BUFFER];
                            String targetFileDir = targetDir + File.separator + strEntry;
                            File targetFile = new File(targetFileDir);

                            try{
                                fos = new FileOutputStream(targetFile);
                                dest = new BufferedOutputStream(fos, BUFFER);
                                int count;
                                while((count = zis.read(data)) != -1){
                                    dest.write(data, 0, count);
                                }
                                dest.flush();
                            }catch(IOException var41){
                            }finally{
                                IOUtil.closeIO(dest, fos);
                            }
                        }
                    }
                }
            }catch(IOException var43){
            }finally{
                IOUtil.closeIO(zis, fis);
            }
            return dataDir;
        }
    }


    /**
     * 获取待压缩文件在ZIP文件中entry的名字，即相对于跟目录的相对路径名
     *
     * @param dirPath 目录名
     * @param file entry文件名
     */
    private static String getEntryName(String dirPath, File file){
        String dirPaths = dirPath;
        if(!dirPaths.endsWith(File.separator)){
            dirPaths = dirPaths + File.separator;
        }
        String filePath = file.getAbsolutePath();
        // 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
        if(file.isDirectory()) filePath += "/";
        int index = filePath.indexOf(dirPaths);
        return filePath.substring(index + dirPaths.length());
    }


    /**
     * 将目录压缩到ZIP输出流
     */
    public static void zipDirectoryToZipFile(String dirPath, File destDir, ZipOutputStream zipOS){
        if(destDir.isDirectory()){
            File[] files = destDir.listFiles();
            if(files.length == 0){    // 空的文件夹
                ZipEntry entry = new ZipEntry(getEntryName(dirPath, destDir)); // 压缩实体
                try{
                    zipOS.putNextEntry(entry);
//                    zipOS.closeEntry();
                }catch(Exception e){
                    e.printStackTrace();
                }
                return;
            }

            for(File file : files){
                if(file.isFile()){
                    zipFilesToZipFile(dirPath, file, zipOS);
                }else{
                    zipDirectoryToZipFile(dirPath, file, zipOS);
                }
            }
        }
    }


    /**
     * 将文件压缩到ZIP输出流
     *
     * @param srcPath 被压缩源
     * @param file 生成的压缩文件对象
     * @param zipOS 输出流
     */
    public static void zipFilesToZipFile(String srcPath, File file, ZipOutputStream zipOS){
        FileInputStream fis = null;
        byte[] buf = new byte[4096];
        int readByte;
        if(file.isFile()){
            try{
                fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(getEntryName(srcPath, file));
                zipOS.putNextEntry(zipEntry);  // 存储信息到压缩文件
                while((readByte = fis.read(buf)) != -1){
                    zipOS.write(buf, 0, readByte);
                }
//                zipOS.closeEntry();
//				System.out.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                IOUtil.closeIO(fis);
            }
        }
    }

    //=====================================================================================================
    //=====================================================================================================


    /**
     * inputStream to byteArr
     */
    public static byte[] stream2Bytes(InputStream is){
        if(is == null) return null;
        return FileOperateUtils.stream2OutputStream(is).toByteArray();
    }


    /**
     * outputStream to byteArr
     */
    public static byte[] stream2Bytes(OutputStream out){
        if(out == null) return null;
        return ((ByteArrayOutputStream)out).toByteArray();
    }


    /**
     * Stream to String
     */
    public static String stream2String(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try{
            while((line = br.readLine()) != null){
                sb.append(line).append("/n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            IOUtil.closeIO(br);
        }
        return sb.toString();
    }


    /**
     * inputStream to string按编码
     */
    public static String stream2String(InputStream is, @NonNull String charsetName){
        try{
            return new String(FileOperateUtils.stream2Bytes(is), charsetName);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * outputStream to string按编码
     */
    public static String stream2String(OutputStream out, @NonNull String charsetName){
        try{
            return new String(FileOperateUtils.stream2Bytes(out), charsetName);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * string to inputStream按编码
     */
    public static InputStream string2InputStream(String string, @NonNull String charsetName){
        try{
            return new ByteArrayInputStream(string.getBytes(charsetName));
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * string to outputStream按编码
     */
    public static OutputStream string2OutputStream(String string, @NonNull String charsetName){
        try{
            return FileOperateUtils.bytes2OutputStream(string.getBytes(charsetName));
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * outputStream to inputStream
     */
    public ByteArrayInputStream stream2InputStream(OutputStream os){
        if(os == null) return null;
        return new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
    }


    /**
     * inputStream to outputStream
     */
    public static ByteArrayOutputStream stream2OutputStream(InputStream is){
        if(is == null) return null;
        ByteArrayOutputStream bos = null;
        try{
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[KB];
            int len;
            while((len = is.read(b)) != -1){
                bos.write(b, 0, len);
            }
            bos.flush();
            return bos;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            IOUtil.closeIO(bos);
        }
    }


    /**
     * byteArr to inputStream
     */
    public static InputStream bytes2InputStream(byte[] bytes){
        if(StringUtils.isEmpty(bytes)) return null;
        return new ByteArrayInputStream(bytes);
    }


    /**
     * outputStream to byteArr
     */
    public static OutputStream bytes2OutputStream(byte[] bytes){
        if(StringUtils.isEmpty(bytes)) return null;
        OutputStream os = null;
        try{
            os = new ByteArrayOutputStream();
            os.write(bytes);
            os.flush();
            return os;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            IOUtil.closeIO(os);
        }
    }

    //=====================================================================================================
    //=====================================================================================================


    /**
     * get the properties of the special props file name
     */
    public static Properties getProperties(String propsFileName){
        Properties props = new Properties();
        try{
            InputStream is = AppUtils.getAppContext().openFileInput(propsFileName);
            props.load(is);
        }catch(Exception e1){
            e1.printStackTrace();
            return null;
        }
        return props;
    }


    /**
     * open properties file
     *
     * @param propsFileName properties file name
     * @return outputStream
     */
    public static OutputStream openPropsFile(String propsFileName){
        OutputStream os;
        try{
            os = AppUtils.getAppContext().openFileOutput(propsFileName, Context.MODE_PRIVATE);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
        return os;
    }

    //=====================================================================================================
    //=====================================================================================================

//	/**
//	 * 修复路径，将 \\ 或 / 等替换为 File.separator
//	 */
//	public static String path(String path){
//		String p = org.apache.commons.lang.StringUtils.replace(path, "\\", "/");
//		p = org.apache.commons.lang.StringUtils.join(org.apache.commons.lang.StringUtils.split(p, "/"), "/");
//		if(!org.apache.commons.lang.StringUtils.startsWithAny(p, "/") && org.apache.commons.lang.StringUtils
// .startsWithAny(path, "\\", "/")){
//			p += "/";
//		}
//		if(!org.apache.commons.lang.StringUtils.endsWithAny(p, "/") && org.apache.commons.lang.StringUtils
// .endsWithAny(path, "\\", "/")){
//			p = p + "/";
//		}
//		return p;
//	}
}
