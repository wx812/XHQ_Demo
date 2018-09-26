package com.xhq.im.base;

import com.xhq.common.interfaces.view.IBaseView;

import java.io.File;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/12/21.
 *     Desc  : download file interface.
 *     Updt  : Description.
 * </pre>
 */
public interface IBaseDownloadFileView extends IBaseView{
    /**
     * 从服务器下载图片
     * @param requestType 请求类型
     * @param file 下载的图片文件
     */
    void receiveFileFromServer(String requestType, File file);
}
