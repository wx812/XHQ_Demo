package com.xhq.demo.media_loader.callback;


import com.xhq.demo.bean.fileBean.BaseFile;
import com.xhq.demo.bean.fileBean.Directory;

import java.util.List;

/**
 * Created by Vincent Woo
 * Date: 2016/10/11
 * Time: 11:39
 */

public interface FilterResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}
