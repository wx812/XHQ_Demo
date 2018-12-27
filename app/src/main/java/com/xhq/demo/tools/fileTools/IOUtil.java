package com.xhq.demo.tools.fileTools;

import java.io.Closeable;
import java.io.IOException;

/**
 * <p>AutoCloseable是在java.lang中的（1.7才有的）
 * Closeable（1.5才有的）接口继承了AutoCloseable接口</p>
 * 当try代码快结束时，资源（在此时流）会被自动关闭, 无须显示调用资源释放, 只用在try的()中声明的资源的close方法才会被调用
 * <li></li>
 *     Desc  : Close utils <br>
 *     updt  : 2018/5/16 by xhq
 */
public class IOUtil{

    private IOUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * close io
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * be quietly close io
     *
     * @param closeables closeable
     */
    public static void closeIOQuietly(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
