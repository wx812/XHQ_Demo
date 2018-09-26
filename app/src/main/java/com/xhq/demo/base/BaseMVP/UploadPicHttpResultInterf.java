package com.xhq.demo.base.BaseMVP;

import java.io.File;
import java.util.Map;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/11/6.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public interface UploadPicHttpResultInterf{

    //    void uploadPicHttpRequest(Context context, String picType, String imageUrl);

    void uploadPicHttpResult(String type, File mFile_small);

    void uploadPicHttpResult(String type, File mFile_small, Map<String, String> map);
}
