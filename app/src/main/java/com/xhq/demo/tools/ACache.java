/*
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xhq.demo.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.WorkerThread;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * 1、替换SharePreference当做配置文件
 * 2、可以缓存网络请求数据，比如oschina的android客户端可以缓存http请求的新闻内容，缓存时间假设为1个小时，超时后自动失效，
 *      让客户端重新请求新的数据，减少客户端流量，同时减少服务器并发量。
 *
 * @author Michael Yang（www.yangfuhai.com） update at 2013.08.07
 *
 *
 * original address https://github.com/yangfuhai/ASimpleCache
 * the following is the modified code
 */
public class ACache{
    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
    private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
    //private static Map<String, ACache> mInstanceMap = Collections.synchronizedMap(new HashMap<String, ACache>());
    private static ACache instance = null;
    private ACacheManager mCache;


    public static ACache get(Context ctx){
        return get(ctx, "ACache");
    }

    public static ACache get(Context ctx, String cacheName) {
        File f = new File(ctx.getFilesDir(), cacheName);
        return get(f, MAX_SIZE, MAX_COUNT);
    }


    public static ACache get(File cacheDir){
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }


    public static ACache get(Context ctx, long max_size, int max_count, String cacheDir){
        File f = new File(ctx.getFilesDir(), cacheDir);
        return get(f, max_size, max_count);
    }

    public  static ACache get(File cacheDir, long max_size, int max_count) {
        if (instance == null) {
            synchronized (ACache.class) {
                //ACache manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
                if (instance == null) {
                    instance = new ACache(cacheDir, max_size, max_count);
                    //mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
                }
            }
        }
        return instance;
    }


    private static String myPid(){
        return "_" + android.os.Process.myPid();
    }


    private ACache(File cacheDir, long max_size, int max_count){
        if(!cacheDir.exists() && !cacheDir.mkdirs()){
            throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
        }
        mCache = new ACacheManager(cacheDir, max_size, max_count);
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================


    /**
     * close io
     */
    public void closeIO(Closeable... closeables) {
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
     * 保存 String数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value){
        File file = mCache.newFile(key);
        BufferedWriter out = null;
        try{
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(out != null){
                try{
                    out.flush();
                    out.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }


    /**
     * 保存 String数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的String数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, String value, int saveTime){
        put(key, Utils.newStringWithDateInfo(saveTime, value));
    }


    /**
     * @param key 要 读取 String数据对应的key
     * @return String 数据
     */
    public String getAsString(String key){
        File file = mCache.get(key);
        if(!file.exists()) return null;
        boolean removeFile = false;
        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(file));
            StringBuilder readString = new StringBuilder();
            String currentLine;
            while((currentLine = in.readLine()) != null){
                readString.append(currentLine);
            }
            if(!Utils.isDue(readString.toString())){
                return Utils.clearDateInfo(readString.toString());
            }else{
                removeFile = true;
                return null;
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            closeIO(in);
            if(removeFile) remove(key);
        }
    }

    /**
     * 读取 String数据
     *
     * @param key
     * @return String 数据
     */
    public String getAsStringWithNewLine(String key) {
        File file = mCache.get(key);
        if (!file.exists())
            return null;
        boolean removeFile = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine + "\n";
            }
            if (!Utils.isDue(readString)) {
                return Utils.clearDateInfo(readString).trim();
            } else {
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
    }

    // =======================================
    // ============= JSONObject 数据 读写 ==============
    // =======================================


    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSON数据
     */
    public void put(String key, JSONObject value){
        put(key, value.toString());
    }


    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSONObject数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONObject value, int saveTime){
        put(key, value.toString(), saveTime);
    }


    /**
     * @param key 要 读取JSONObject数据对应的key
     * @return JSONObject数据
     */
    public JSONObject getAsJSONObject(String key){
        String JSONString = getAsString(key);
        try{
            return new JSONObject(JSONString);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================


    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSONArray数据
     */
    public void put(String key, JSONArray value){
        put(key, value.toString());
    }


    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSONArray数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONArray value, int saveTime){
        put(key, value.toString(), saveTime);
    }


    /**
     * @param key 要 读取JSONArray数据 对应的key
     * @return JSONArray数据
     */
    public JSONArray getAsJSONArray(String key){
        String JSONString = getAsString(key);
        try{
            return new JSONArray(JSONString);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================


    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value){
        File file = mCache.newFile(key);
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(file);
            out.write(value);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(out != null){
                try{
                    out.flush();
                    out.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }


    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, byte[] value, int saveTime){
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }


    /**
     * @param key 要获取 byte 数据 对应的key
     * @return byte 数据
     */
    public byte[] getAsBinary(String key){
        RandomAccessFile raFile = null;
        boolean removeFile = false;
        try{
            File file = mCache.get(key);
            if(!file.exists()) return null;
            raFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int)raFile.length()];
            raFile.read(byteArray);
            if(!Utils.isDue(byteArray)){
                return Utils.clearDateInfo(byteArray);
            }else{
                removeFile = true;
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            closeIO(raFile);
            if(removeFile) remove(key);
        }
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================


    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的value
     */
    public void put(String key, Serializable value){
        put(key, value, -1);
    }


    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的value
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Serializable value, int saveTime){
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if(saveTime != -1){
                put(key, data, saveTime);
            }else{
                put(key, data);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeIO(oos,baos);
        }
    }


    /**
     * @param key 要 读取 Serializable数据的key
     * @return Serializable 数据
     */
    public Object getAsObject(String key){
        byte[] data = getAsBinary(key);
        if(data != null){
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try{
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                return ois.readObject();
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }finally{
                closeIO(ois, bais);
            }
        }
        return null;

    }


    /**
     * 保存 list数据 到 缓存中
     *
     * @param <T> object
     * @param key 保存的key
     * @param list 保存的list
     */
    public <T> void putList(String key, List<T> list){
        putList(key, list, -1);
    }


    /**
     * 保存 list数据到 缓存中
     *
     * @param key 保存的key
     * @param list 保存的list
     * @param saveTime 保存的时间，单位：秒
     */
    public <T> void putList(String key, List<T> list, int saveTime){
        SerialList<T> serialList = new SerialList<>();
        serialList.list = list;
        put(key, serialList, saveTime);
    }


    /**
     * 读取 list数据
     *
     * @return list 数据
     */
    public <T> List<T> getAsList(String key){
        Object obj = getAsObject(key);
        if(obj != null && obj instanceof SerialList){
            return ((SerialList)obj).list;
        }else
            return null;

    }


    /**
     * Intent是Activity与Activity之间，Activity与Service之间传递参数的介质
     * Intent.putExtras(key, value)几乎可以包括各种类型的值，但是却没有类似List<Object>之类的传递参数
     * 可以把list强转成Serializable类型，然后通过putExtras(key, (Serializable)list)方法传递过去，接受的时候用
     * (List<YourObject>) getIntent().getSerializable(key)就可以接受到List<YourObject>数据了
     *
     * 但是最重要的一点是：保证你自己定义的类实现了Serializable接口，
     * 然后传递list的时候强转成Serializable类型，接受的时候再转换回来就可以了！
     * @param <T>
     */
    private class SerialList<T> implements Serializable{
        public List<T> list;
    }

    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================


    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的bitmap数据
     */
    public void put(String key, Bitmap value){
        put(key, Utils.Bitmap2Bytes(value));
    }


    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的 bitmap 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Bitmap value, int saveTime){
        put(key, Utils.Bitmap2Bytes(value), saveTime);
    }


    /**
     * @param key 要 读取 bitmap 数据 的key
     * @return bitmap 数据
     */
    public Bitmap getAsBitmap(String key){
        if(getAsBinary(key) == null) return null;
        return Utils.Bytes2Bitmap(getAsBinary(key));
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================


    /**
     * 保存 drawable 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的drawable数据
     */
    public void put(String key, Drawable value){
        put(key, Utils.drawable2Bitmap(value));
    }


    /**
     * 保存 drawable 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的 drawable 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Drawable value, int saveTime){
        put(key, Utils.drawable2Bitmap(value), saveTime);
    }


    /**
     * @param key 要 读取 Drawable 数据的key
     * @return Drawable 数据
     */
    public Drawable getAsDrawable(String key){
        if(getAsBinary(key) == null) return null;
        return Utils.bitmap2Drawable(Utils.Bytes2Bitmap(getAsBinary(key)));
    }


    /**
     * @param key   获取key对应的缓存文件
     * @return value 缓存的文件
     */
    public File file(String key){
        File f = mCache.newFile(key);
        return f.exists() ? f : null;
    }


    /**
     * @param key   移除某个key
     * @return 是否移除成功
     */
    public boolean remove(String key){
        return mCache.remove(key);
    }


    /**
     * 清除所有数据
     */
    public void clear(){
        mCache.clear();
    }


    /**
     * 缓存管理器
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     */
    public class ACacheManager{
        private final AtomicLong cacheSize;
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final int countLimit;
        private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
        protected File cacheDir;


        private ACacheManager(File cacheDir, long sizeLimit, int countLimit){
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new AtomicLong();
            cacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }


        /**
         * 计算 cacheSize和cacheCount
         */
        private void calculateCacheSizeAndCacheCount(){
            new Thread(new Runnable(){
                @Override
                public void run(){
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = cacheDir.listFiles();
                    if(cachedFiles != null){
                        for(File cachedFile : cachedFiles){
                            size += calculateSize(cachedFile);
                            count++;
                            lastUsageDates.put(cachedFile, cachedFile.lastModified());
                        }
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }
                }
            }).start();
        }


        private void put(File file){
            int curCacheCount = cacheCount.get();
            while(curCacheCount + 1 > countLimit){
                long freedSize = removeNext();
                cacheSize.addAndGet(-freedSize);

                curCacheCount = cacheCount.addAndGet(-1);
            }
            cacheCount.addAndGet(1);

            long valueSize = calculateSize(file);
            long curCacheSize = cacheSize.get();
            while(curCacheSize + valueSize > sizeLimit){
                long freedSize = removeNext();
                curCacheSize = cacheSize.addAndGet(-freedSize);
                if(freedSize <0) break;
            }
            cacheSize.addAndGet(valueSize);

            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);
        }


        private File get(String key){
            File file = newFile(key);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);

            return file;
        }


        private File newFile(String key){
            //return new File(cacheDir, key.hashCode() + "");
			return new File(cacheDir, Base64.encodeToString(key.getBytes(), Base64.NO_WRAP) + "");
        }


        private boolean remove(String key){
            File image = get(key);
            return image.delete();
        }

		@WorkerThread
        private void clear(){
            lastUsageDates.clear();
            cacheSize.set(0);
            File[] files = cacheDir.listFiles();
            if(files != null){
                for(File f : files){
                    f.delete();
                }
            }
        }


        /**
         * @return 移除旧的文件 的大小
         */
        private long removeNext(){
            if(lastUsageDates.isEmpty()) return 0;

            Long oldestUsage = null;
            File mostLongUsedFile = null;
            Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
            synchronized(lastUsageDates){
                for(Entry<File, Long> entry : entries){
                    if(mostLongUsedFile == null){
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                    }else{
                        Long lastValueUsage = entry.getValue();
                        if(lastValueUsage < oldestUsage){
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = entry.getKey();
                        }
                    }
                }
            }

            long fileSize = calculateSize(mostLongUsedFile);
            if(mostLongUsedFile.delete()) lastUsageDates.remove(mostLongUsedFile);
            return fileSize;
        }


        private long calculateSize(File file){
            return file.length();
        }
    }

    /**
     * 时间计算工具类
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     */
    private static class Utils{

        /**
         * 判断缓存的String数据是否到期
         *
         * @param str data
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(String str){
            return isDue(str.getBytes());
        }


        /**
         * 判断缓存的byte数据是否到期
         *
         * @param data data
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(byte[] data){
            String[] strArray = getDateInfoFromDate(data);
            if(strArray != null && strArray.length == 2){
                String saveTimeStr = strArray[0];
                while(saveTimeStr.startsWith("0")){
                    saveTimeStr = saveTimeStr
                            .substring(1, saveTimeStr.length());
                }
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strArray[1]);
                return System.currentTimeMillis() > saveTime + deleteAfter * 1000;
            }
            return false;
        }


        private static String newStringWithDateInfo(int second, String strInfo){
            return createDateInfo(second) + strInfo;
        }


        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2){
            byte[] data1 = createDateInfo(second).getBytes();
            byte[] retData = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retData, 0, data1.length);
            System.arraycopy(data2, 0, retData, data1.length, data2.length);
            return retData;
        }


        private static String clearDateInfo(String strInfo){
            if(strInfo != null && hasDateInfo(strInfo.getBytes())){
                strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1, strInfo.length());
            }
            return strInfo;
        }


        private static byte[] clearDateInfo(byte[] data){
            if(hasDateInfo(data)){
                return copyOfRange(data, indexOf(data, mSeparator) + 1, data.length);
            }
            return data;
        }


        private static boolean hasDateInfo(byte[] data){
            return data != null && data.length > 15 && data[13] == '-' && indexOf(data, mSeparator) > 14;
        }


        private static String[] getDateInfoFromDate(byte[] data){
            if(hasDateInfo(data)){
                String saveDate = new String(copyOfRange(data, 0, 13));
                String deleteAfter = new String(copyOfRange(data, 14, indexOf(data, mSeparator)));
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }


        private static int indexOf(byte[] data, char c){
            for(int i = 0; i < data.length; i++){
                if(data[i] == c) return i;
            }
            return -1;
        }


        private static byte[] copyOfRange(byte[] original, int from, int to){
            int newLength = to - from;
            if(newLength < 0) throw new IllegalArgumentException(from + " > " + to);
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }


        private static final char mSeparator = ' ';


        private static String createDateInfo(int second){
            StringBuilder currentTime = new StringBuilder(System.currentTimeMillis() + "");
            while(currentTime.length() < 13){
                currentTime.insert(0, "0");
            }
            return currentTime + "-" + second + mSeparator;
        }


        /*
         * Bitmap → byte[]
         */
        private static byte[] Bitmap2Bytes(Bitmap bm){
            if(bm == null) return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }


        /*
         * byte[] → Bitmap
         */
        private static Bitmap Bytes2Bitmap(byte[] b){
            if(b.length == 0) return null;
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }


        /*
         * Drawable → Bitmap
         */
        private static Bitmap drawable2Bitmap(Drawable drawable){
            if(drawable == null) return null;
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);  // 建立对应 bitmap
            Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);   // 把 drawable 内容画到画布中
            return bitmap;
        }


        /*
         * Bitmap → Drawable
         */
        @SuppressWarnings("deprecation")
        private static Drawable bitmap2Drawable(Bitmap bm){
            return bm == null ? null : new BitmapDrawable(bm);
        }
    }

}