package com.xhq.demo.tools.fileTools;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/5/28.
 *     Desc  : file information.
 *     Updt  : Description.
 * </pre>
 */
public class FileInfo{

    public String name;
    public String path;

    public long total;              // total size
    public long free;               // free size
    public long ModifiedDate;
    public int count;               // numbers of the files in the directory

    public boolean IsDir;
    public boolean canRead;
    public boolean canWrite;
    public boolean isHidden;

}
