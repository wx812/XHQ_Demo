package com.xhq.demo.tools.fileTools.zip;

import android.text.TextUtils;

import com.xhq.demo.tools.fileTools.zip.apache.zip.ZipEntry;
import com.xhq.demo.tools.fileTools.zip.apache.zip.ZipFile;
import com.xhq.demo.tools.fileTools.zip.apache.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * 压缩或解压zip：
 * 由于直接使用java.util.zip工具包下的类，会出现中文乱码问题，
 * java.util.zip.ZipFile , this(file, mode, StandardCharsets.UTF_8); 写死使用了UTF-8.<br>
 * 所以使用ant.jar中的org.apache.tools.zip下的工具类
 *
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/6/10.
 *     Desc  : zip tools.
 *     Updt  : Description.
 * </pre>
 */
public class ZipUtilByApatche{
    
    /**
     * 压缩文件或路径
     * @param zipPath 压缩的目的地址
     * @param srcFiles 压缩的源文件
     */
    public static void zipFile( List<File> srcFiles,  String zipPath){
        try {
            if( zipPath.endsWith(".zip") || zipPath.endsWith(".ZIP") ){
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(zipPath))) ;
                zos.setEncoding("GBK");
                for( File f : srcFiles ){
                    handlerFile(zipPath , zos , f , "");
                }
                zos.close();
            }else{
                System.out.println("target file[" + zipPath + "] is not .zip type file");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param zip 压缩的目的地址
     * @param zipOut 
     * @param srcFile  被压缩的文件信息
     * @param path  在zip中的相对路径
     * @throws IOException
     */
    private static void handlerFile(String zip , ZipOutputStream zipOut , File srcFile , String path ) throws IOException{
//        System.out.println(" begin to compression file[" + srcFile.getName() + "]");
        if( !TextUtils.isEmpty(path) && ! path.endsWith(File.separator)){
            path += File.separator ;
        }
        if( ! srcFile.getPath().equals(zip) ){
            if( srcFile.isDirectory() ){
                File[] files = srcFile.listFiles() ;
                if( files.length == 0 ){
                    zipOut.putNextEntry(new ZipEntry( path + srcFile.getName() + File.separator));
                    zipOut.closeEntry();
                }else{
                    for( File _f : files ){
                        handlerFile( zip ,zipOut , _f , path + srcFile.getName() );
                    }
                }
            }else{
            	byte[] _byte = new byte[1024] ;
                InputStream _in = new FileInputStream(srcFile) ;
                zipOut.putNextEntry(new ZipEntry(path + srcFile.getName()));
                int len;
                while( (len = _in.read(_byte)) > 0  ){
                    zipOut.write(_byte, 0, len);
                }
                _in.close();
                zipOut.closeEntry();
            }
        }
    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
     * @param zipPath 待解压缩的ZIP文件名
     * @param descDir  目标目录
     */
    public static List<File> upzipFile(String zipPath, String descDir) {
        return upzipFile( new File(zipPath) , descDir ) ;
    }
    
    /**
     * 对.zip文件进行解压缩
     * @param zipFile  解压缩文件
     * @param descDir  压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<File> upzipFile(File zipFile, String descDir) {
        List<File> _list = new ArrayList<>() ;
        try {
            ZipFile _zipFile = new ZipFile(zipFile , "GBK") ;
            for( Enumeration entries = _zipFile.getEntries() ; entries.hasMoreElements() ; ){
                ZipEntry entry = (ZipEntry)entries.nextElement() ;
                File _file = new File(descDir + File.separator + entry.getName()) ;
                if( entry.isDirectory() ){
                    _file.mkdirs() ;
                }else{
                	byte[] _byte = new byte[1024] ;
                    File _parent = _file.getParentFile() ;
                    if( !_parent.exists() ){
                        _parent.mkdirs() ;
                    }
                    InputStream _in = _zipFile.getInputStream(entry);
                    OutputStream _out = new FileOutputStream(_file) ;
                    int len;
                    while( (len = _in.read(_byte)) > 0){
                        _out.write(_byte, 0, len);
                    }
                    _in.close(); 
                    _out.flush();
                    _out.close();
                    _list.add(_file) ;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _list ;
    }
}