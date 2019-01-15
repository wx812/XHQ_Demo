package com.xhq.demo.tools.fileTools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.appTools.AppUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 字符流处理单元为2个字节的Unicode字符，分别操作字符、字符数组或字符串，而字节流处理单元为1个字节，操作字节和字节数组。<br>
 * 所以字符流是由Java虚拟机将字节转化为2个字节的Unicode字符为单位的字符而成的，所以它对多国语言支持性比较好！
 * 如果是 音频文件、图片、歌曲，就用字节流好点，如果是关系到中文（文本）的，用字符流好点.<p>
 * <p>
 * 所有文件的储存是都是字节（byte）的储存，在磁盘上保留的并不是文件的字符而是先把字符编码成字节，再储存这些字节到磁盘。
 * 在读取文件（特别是文本文件）时，也是一个字节一个字节地读取以形成字节序列.
 * 1. 字节流可用于任何类型的对象，包括二进制对象，而字符流只能处理字符或者字符串；
 * 2. 字节流提供了处理任何类型的IO操作的功能，但它不能直接处理Unicode字符，而字符流就可以。
 * <ul>
 * make open delete file
 * <li>{@link #makeFile(File)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * <li>{@link #openFile(Context, File)}</li>
 * <li>{@link #deleteFile(File)}</li>
 * <li>{@link #deleteFileOrDir(File)}</li>
 * </ul>
 *
 * <ul>
 * Read or write file
 * <li>{@link #readFile(File, String)} read file</li>
 * <li>{@link #readFile2List(String, String)} read file to string list</li>
 * <li>{@link #write2File(List, File, boolean)} write file from String List</li>
 * <li>{@link #write2File(InputStream, String, boolean)} write file</li>
 * <li>{@link #write2File(InputStream, File, boolean)} write file</li>
 * </ul>
 *
 * <ul>
 * Operate file
 * <li>{@link #getFileByPath(String)}</li>
 * <li>{@link #getFileNameWithExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileMIMEType(File)}</li>
 * <li>{@link #isFileAndExist(File)}</li>
 * <li>{@link #isDirAndExist(File)}</li>
 * </ul>
 *
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/10/9.
 *     Desc  : File Tools
 *     Updt  : 2071/12/22
 * </pre>
 */
public class FileUtils{

    public static final String FILE_DOT_SEPARATOR = ".";


    public static boolean makeFile(String absFilePath){
        return makeFile(getFileByPath(absFilePath));
    }
    /**
     * judgement if the file exists, and delete it before creating it
     *
     * @param file file
     */
    public static boolean makeFile(File file){
        if(isFileAndExist(file)) file.delete();
//        // 如果父目录不存在, 则返回false
        if(!makeDirs(file.getParentFile())) return false;
        try{
            return file.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file file
     */
    public static boolean createFile(@NonNull File file){
        if(file.exists()) return file.isFile();
        try{
            return file.createNewFile();
        }catch(IOException | SecurityException e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 创建单个文件
     *
     * @param absFileName 文件名，包含路径
     */
    public static boolean createFile(String absFileName){
        File file = new File(absFileName);
        if(file.exists()) return false;
        if(absFileName.endsWith(File.separator)) return false;
        // 如果文件所在的目录不存在，则创建目录
        if(!makeDirs(file.getParentFile())) return false;
        try{
            return file.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Creates the directory named by the trailing filename of this file,
     * including the complete directory path required to create this directory. <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeDirs("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param absDirPath folder path
     * @return true if the necessary directories have been created or the target directory already exists,
     * false one of the
     */
    public static boolean makeDirs(@NonNull String absDirPath){
        File f = new File(absDirPath);
        return f.exists() ? f.isDirectory() : f.mkdirs();
    }


    public static boolean makeDirs(@NonNull File fileDir){
        return fileDir.exists() || fileDir.mkdirs();
    }


    /**
     * If a fileDir exists, delete the fileDir first and then create a new directory(fileDir)
     */
    public static boolean creatDirs(File fileDir){
        if(isDirAndExist(fileDir)) deleteDir(fileDir.getPath());
        return makeDirs(fileDir.getParentFile()) && fileDir.mkdirs();
    }


    /**
     * 生成上传文件的文件名，文件名以：日期开头+"_"+文件的原始名称
     *
     * @param fileName 文件的原始名称
     * @return 日期+"_"+文件的原始名称
     */
    public static String createFileName(String fileName){
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        return time + "_" + fileName;
    }


    /**
     * open the file
     *
     * @param ctx context
     * @param file file
     */
    public static void openFile(Context ctx, File file){
        String type = getFileMIMEType(file);
        if(type.equals("1")){
            Toast.makeText(ctx, "暂不支持此类型文件", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), type);
            //有可能会报错。比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
            try{
                ctx.startActivity(intent);
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(ctx, "暂不支持此类型文件", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * delete file
     */
    public static boolean deleteFile(@NonNull File file){
        return !file.exists() || !file.isFile() || file.delete();
    }


    public static boolean deleteFile(@NonNull String filePath){
        return deleteFile(getFileByPath(filePath));
    }


    public static boolean deleteFilesInDir(String dirPath){
        return deleteFilesInDir(getFileByPath(dirPath));
    }


    /**
     * delete all file in the directory
     *
     * 优化, 可添加线程  删除 目录
     */
    public static boolean deleteFilesInDir(@NonNull File file){
        if(!file.exists()) return true;
        if(file.isFile()) return file.delete();
        if(!file.isDirectory()) return false;
        File[] files = file.listFiles();
        if(files != null && files.length != 0){
            for(File f : files){
                if(f.isFile()){
                    f.delete();
                }else if(f.isDirectory()){
                    deleteFilesInDir(f);
                }
            }
        }
        return true;
    }


    // 删除指定目录下所有文件
    public static void deleteAllFile(final String filePath) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                File display = new File(filePath);
                if (!display.exists()) return;
                File[] items = display.listFiles();
                int i = display.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (items[j].isFile()) {
                        items[j].delete();// 删除文件
                    }
                }
            }
        });
        t.start();
    }


    /**
     * delete the folder, including itself
     */
    public static boolean deleteDir(String dirPath){
        return deleteFileOrDir(Objects.requireNonNull(getFileByPath(dirPath)));
    }


    /**
     * delete file or folder
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, recursively deleted. return true</li>
     * <ul>
     *
     * @param file absolute path of file or directory
     */
    public static boolean deleteFileOrDir(@NonNull File file){
        return deleteFilesInDir(file) && file.delete();
    }

    //=====================================================================================================
    //=====================================================================================================


    /**
     * read file, except media file(eg: video or audio)
     *
     * @param file file object
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     */
    public static StringBuilder readFile(@NonNull File file, @Nullable String charsetName){
        if(!file.isFile()) return null;
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader br = null;
        if(TextUtils.isEmpty(charsetName)) charsetName = "UTF-8";
        try{
            isr = new InputStreamReader(new FileInputStream(file), charsetName);
            br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                if(!sb.toString().equals("")) sb.append("\r\n");
                sb.append(line);
            }
            return sb;
        }catch(IOException e){
            throw new RuntimeException("IOException occurred. ", e);
        }finally{
            IOUtil.closeIO(br, isr);
        }
    }


    /**
     * read file to string list, a element of list is a line, except media file
     */
    public static List<String> readFile2List(@NonNull String filePath, @NonNull String charsetName){
        File file = new File(filePath);
        List<String> list = new ArrayList<>();
        if(!file.isFile()) return null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try{
            isr = new InputStreamReader(new FileInputStream(file), charsetName);
            br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                list.add(line);
            }
            return list;
        }catch(IOException e){
            throw new RuntimeException("IOException occurred. ", e);
        }finally{
            IOUtil.closeIO(br, isr);
        }
    }


    /**
     * 指定编码按行读取文件到列表中
     *
     * @param file 文件
     * @param start 需要读取的开始行数
     * @param end 需要读取的结束行数
     * @param charsetName 编码格式
     * @return 包含从start行到end行的list
     */
    public static List<String> readFile2List(File file, int start, int end, @NonNull String charsetName){
        if(file == null) return null;
        if(start > end) return null;
        BufferedReader br = null;
        try{
            String line;
            int curLine = 1;
            List<String> list = new ArrayList<>();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            while((line = br.readLine()) != null){
                if(curLine > end) break;
                if(start <= curLine && curLine <= end) list.add(line);
                ++curLine;
            }
            return list;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            IOUtil.closeIO(br);
        }
    }


    /**
     * 指定编码按行读取文件到字符串中
     */
    public static String readFile2String(@NonNull String filePath, String charsetName){
        File file = new File(filePath);
        if(!file.isFile()) return null;
        StringBuilder sb = readFile(file, charsetName);
        // 去除最后的换行符
        assert sb != null;
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    /**
     * 读取文件到字符数组中
     */
    public static byte[] readFile2Bytes(@NonNull File file){
        try{
            return FileOperateUtils.stream2Bytes(new FileInputStream(file));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 从文件中获取byte数组
     */
    public static byte[] getBytesFromFile(String filePath){
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try{
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(FileSizeUtils.KB);
            byte[] b = new byte[FileSizeUtils.KB];
            int len;
            while((len = fis.read(b)) != -1){
                bos.write(b, 0, len);
            }
            bos.flush();
            buffer = bos.toByteArray();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            IOUtil.closeIO(bos, fis);
        }
        return buffer;
    }



    /**
     * write file, the string will be written to the begin of the file, if false of the append
     *
     * @param srcText content
     * @param destFile destination file
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean write2File(@NonNull String srcText, @NonNull File destFile, boolean append){
//        if(destFile == null || TextUtils.isEmpty(srcText)) return false;
        if(!createFile(destFile)) return false;
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new FileWriter(destFile, append));
            bw.write(srcText);
            bw.flush();
            return true;
        }catch(IOException e){
            throw new RuntimeException("IOException occurred. ", e);
        }finally{
            IOUtil.closeIO(bw);
        }
    }



    /**
     * write file, the string list will be written to the begin of the file, if false of the append
     */
    public static boolean write2File(List<String> contentList, File destFile, boolean append){
        if(StringUtils.isEmpty(contentList)) return false;
        if(!createFile(destFile)) return false;
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new FileWriter(destFile, append));
            for(int i = 0; i < contentList.size(); i++){
                String line = contentList.get(i);
                if(i > 0) bw.write("\r\n");
                bw.write(line);
            }
            bw.flush();
            return true;
        }catch(IOException e){
            throw new RuntimeException("IOException occurred. ", e);
        }finally{
            IOUtil.closeIO(bw);
        }
    }


    /**
     * see {@link #write2File(InputStream, File, boolean)}
     */
    public static boolean write2File(InputStream srcIS, String destPath, boolean append){
        return write2File(srcIS, getFileByPath(destPath), append);
    }


    /**
     * write file, the inputStream content will be written to the begin of the file, if false of the append
     *
     * @param destFile the file to be opened for writing.
     * @param srcIS the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the
     * beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean write2File(InputStream srcIS, File destFile, boolean append){
        if(destFile == null || srcIS == null) return false;
        if(!makeFile(destFile)) return false;
        OutputStream os = null;
        try{
            os = new BufferedOutputStream(new FileOutputStream(destFile, append));
            byte[] data = new byte[FileSizeUtils.KB];
            int len;
            while((len = srcIS.read(data)) != -1){
                os.write(data, 0, len);
            }
            os.flush();
            return true;
        }catch(FileNotFoundException e){
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }catch(IOException e){
            throw new RuntimeException("IOException occurred. ", e);
        }finally{
            IOUtil.closeIO(os);
        }
    }


    //=========================================================================================================
    //=========================================================================================================


    /**
     * 根据文件路径获取文件
     */
    public static File getFileByPath(@NonNull String filePath){
        return new File(filePath);
    }


    /**
     * get folder name from path
     * <p>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     */
    public static String getFolderName(@NonNull String filePath){
        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? "" : filePath.substring(0, filePos);
    }


    /**
     * 获取全路径中目录名
     */
    public static String getDirName(@NonNull String filePath){
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }


    /**
     * get suffix of file from path
     * <p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath file path
     */
    public static String getFileExtension(@NonNull String filePath){
        int filePos = filePath.lastIndexOf(File.separator);
        int dotPos = filePath.lastIndexOf(FILE_DOT_SEPARATOR);
        if(dotPos == -1) return "";
        return (filePos >= dotPos) ? "" : filePath.substring(dotPos + 1);
    }


    /**
     * get file name from path, not include suffix
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("abc")              =   "abc"
     *      getFileName("a.mp3")            =   "a"
     *      getFileName("a.b.rmvb")         =   "a.b"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath file path
     * @return file name from path, not include suffix
     */
    public static String getFileName(@NonNull String filePath){
        int filePos = filePath.lastIndexOf(File.separator);
        int dotPos = filePath.lastIndexOf(FILE_DOT_SEPARATOR);
        if(filePos == -1){
            return (dotPos == -1 ? filePath : filePath.substring(0, dotPos));
        }
        if(dotPos == -1) return filePath.substring(filePos + 1);
        return (filePos < dotPos ? filePath.substring(filePos + 1, dotPos)
                : filePath.substring(filePos + 1));
    }


    /**
     * get file name from path, include suffix
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath file path
     * @return file name from path, include suffix
     */
    public static String getFileNameWithExtension(@NonNull String filePath){
        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? filePath : filePath.substring(filePos + 1);
    }


    /**
     * get the mimeType of file
     *
     * @param file file
     * @return mimeType
     */
    public static String getFileMIMEType(File file){
        String type = "*/*";
        String name = file.getName();
        if(!name.contains(FILE_DOT_SEPARATOR)) return "1";
        String end = name.substring(name.lastIndexOf(FILE_DOT_SEPARATOR))
                         .toLowerCase()
                         .trim();
        for(String[] aMIME_MapTable : MIME_MAP_TABLE){
            if(end.equals(aMIME_MapTable[0])){
                type = aMIME_MapTable[1];
//                Log.i("println", "   所属分类：" + aMIME_MapTable[1]);
            }
        }
        return type;
    }


    public static FileInfo GetFileInfo(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return null;
        return GetFileInfo(file);
    }

    public static FileInfo GetFileInfo(@NonNull File file) {
        if (!file.exists()) return null;
        FileInfo fileInfo = new FileInfo();
        fileInfo.canRead = file.canRead();
        fileInfo.canWrite = file.canWrite();
        fileInfo.isHidden = file.isHidden();
        fileInfo.name = file.getName();
        fileInfo.ModifiedDate = file.lastModified();
        fileInfo.IsDir = file.isDirectory();
        fileInfo.path = file.getAbsolutePath();
        fileInfo.total = file.length();
        return fileInfo;
    }

    private static final String ANDROID_SECURE = "/mnt/sdcard/.android_secure";
    public static boolean isNormalFile(String fullName) {
        return !fullName.equals(ANDROID_SECURE);
    }

    public static FileInfo GetFileInfo(@NonNull File file, FilenameFilter filter, boolean showHidden) {
        FileInfo fileInfo = GetFileInfo(file);
        if(fileInfo == null) return null;
        if (fileInfo.IsDir) {
            int cont = 0;
            File[] files = file.listFiles(filter);
            if (files == null) return null;	// null means we cannot access this dir
            for (File f : files) {
                if ((!f.isHidden() || showHidden) && isNormalFile(f.getAbsolutePath())) cont++;
            }
            fileInfo.count = cont;
        } else {
            fileInfo.total = file.length();
        }
        return fileInfo;
    }




    /**
     * Determine if the file or directory exists
     *
     * @param file "file" or directory
     */
    public static boolean isFileExist(File file){
        return file != null && file.exists();
    }


    public static boolean isFileAndExist(File file){
        return isFileExist(file) && file.isFile();
    }


    public static boolean isDirAndExist(File dir){
        return isFileExist(dir) && dir.isDirectory();
    }


    /**
     * 获取目录下所有文件
     *
     * @param isRecursive 是否递归进子目录
     */
    public static List<File> listFilesInDir(File dir, boolean isRecursive){
        if(!FileUtils.isDirAndExist(dir)) return null;
        if(isRecursive) return listFilesInDir(dir);
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            Collections.addAll(list, files);
        }
        return list;
    }


    /**
     * 获取目录下所有文件包括子目录
     */
    public static List<File> listFilesInDir(File dir){
        if(!FileUtils.isDirAndExist(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                list.add(file);
                if(file.isDirectory()){
                    list.addAll(Objects.requireNonNull(listFilesInDir(file)));
                }
            }
        }
        return list;
    }



    /**
     * rename file or directory
     */
    public static boolean rename(String filePath, String newName){
        return rename(getFileByPath(filePath), newName);
    }


    /**
     * rename file or directory
     *
     * @param file file that need to be renamed
     * @param newName new name
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean rename(File file, @NonNull String newName){
        if(file == null || !file.exists()) return false;
        if(newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + newName);
        return !newFile.exists() && file.renameTo(newFile);
    }

    //===============================================================================================================
    //===============================================================================================================

    /**
     * save obj list to file
     *
     * @param objList object list
     * @param fileName To save the file name of the different object
     * @param <T> T
     */
    private static <T> void SaveObj2File(List<T> objList, final String fileName){
        File zoningFile = new File(StorageUtil.getAppCacheDir(""), "zoningData.out");
        if(null == objList || objList.isEmpty()) return;
        if(!zoningFile.exists()){
            Observable.fromArray(objList)
                      .map(list -> {
                          for(int i = 0; i < list.size(); i++){
                              T bean = list.get(i);
                              FileOutputStream fos = null;
                              try{
                                  Context ctx = AppUtils.getAppContext();
                                  fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
                                  ObjectOutputStream oos = new ObjectOutputStream(fos);
                                  oos.writeObject(bean);
                              }catch(IOException e){
                                  e.printStackTrace();
                              }finally{
                                  IOUtil.closeIO(fos);
                              }
                          }
                          return "1";
                      })
                      .subscribeOn(Schedulers.io())
                      .subscribe();
        }
    }


    /**
     * get object from the file(file path)
     * @param filePath file path
     * @param objList object list
     * @param <T> T
     */
    @SuppressLint("CheckResult")
    public static <T> void getObjFromFile(String filePath, final List<T> objList){
        File f = new File(filePath);
        if(f.exists() && !f.isDirectory()){
            Observable.just(f)
                      .map(file -> {
                          FileInputStream fis = null;
                          ObjectInputStream ois = null;
                          try{
                              fis = new FileInputStream(file);
                              ois = new ObjectInputStream(fis);
                              T obj = (T)ois.readObject();
                              objList.clear();
                              objList.add(obj);
                          }catch(IOException | ClassNotFoundException e){
                              e.printStackTrace();
                          }finally{
                              IOUtil.closeIO(ois, fis);
                          }
                          return objList;
                      })
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(obj -> {
//                              pGetMsg.passZoningInquireMsgForResult(obj);
                      });
        }
    }

    //===============================================================================================================
    //===============================================================================================================


    public void fileSave(Object oAuth_1){
        Context ctx = AppUtils.getAppContext();
        //保存在本地
        try{
            // 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠）
            FileOutputStream fos = ctx.openFileOutput("oauth_1.out", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(oAuth_1);// 写入
            fos.close(); // 关闭输出流
            Toast.makeText(ctx, "保存oAuth_1成功",Toast.LENGTH_LONG).show();
        }catch(IOException e){
            e.printStackTrace();
            Toast.makeText(ctx, "出现异常2",Toast.LENGTH_LONG).show();
        }

        //保存在sd卡
        if(StorageUtil.isEnableSDCard()){
            File sdCardDir = StorageUtil.getExStorageDir();
            File sdFile = new File(sdCardDir, "oauth_1.out");
            try{
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(oAuth_1);// 写入
                fos.close(); // 关闭输出流
            }catch(IOException e){
                e.printStackTrace();
            }
            Toast.makeText(ctx, "成功保存到sd卡", Toast.LENGTH_LONG).show();
        }
    }

    //2、从本地或SD卡中取出对象
    //（从本地取得保存的对象）
//    public Object readOAuth1(){
//        OAuthV1 oAuth_1=null;
//        //String filename = "oauth_file.cfg";
//        try{
//            FileInputStream fis=this.openFileInput("oauth_1.out"); //获得输入流
//            ObjectInputStream ois=new ObjectInputStream(fis);
//            oAuth_1=(OAuthV1)ois.readObject();
//
//            //tv.setText(per.toString()); //设置文本控件显示内容
//            // Toast.makeText(ShareTencentActivity.this,"读取成功",Toast.LENGTH_LONG).show();
//        }catch(ClassNotFoundException|IOException e){
//
//            e.printStackTrace();
//        }
//        return oAuth_1;
//    }

//    //（2）从SD卡中取得保存的对象 对SD卡操作别忘了加权限
//    @RequiresPermission(allOf = {Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission
// .WRITE_EXTERNAL_STORAGE })
//    public OAuthV1 readOAuth2(){
//        OAuthV1 oAuth_1 = null;
//        File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
//        File sdFile = new File(sdCardDir, "oauth_1.out");
//
//        try{
//            FileInputStream fis = new FileInputStream(sdFile); //获得输入流
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            oAuth_1 = (OAuthV1)ois.readObject();
//            ois.close();
//        }catch(IOException | ClassNotFoundException e){
//            e.printStackTrace();
//        }
//    }

    //文件的后缀名
    public static final String[][] MIME_MAP_TABLE = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-JavaScript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };


}
