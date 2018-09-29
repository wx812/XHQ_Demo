package com.xhq.demo.tools;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class TCPUtils{

    private static final String TAG = "TCPUtils";

    private Socket socket;

    private ServerSocket serverSocket;

    private Context context;

    private byte[] buffer = new byte[1024];

    private int len;

    public TCPUtils(Context context) {
        this.context = context;
    }

    public void initSocket(String host, int port) {
        try {
            socket = new Socket();
            socket.setReuseAddress(true);
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean accept() {
        try {
            socket = serverSocket.accept();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ServerSocket initServerSocket(int port) {
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            serverSocket = null;
            e.printStackTrace();
        }
        return serverSocket;
    }

    public void setSoTimeout(final int timeout) throws Exception{
        socket.setSoTimeout(timeout);
    }

    public int getSoTimeout() throws Exception{
        return socket.getSoTimeout();
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean sendHead(SocketHead head) {
        try {
            if (head != null) {
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(outputStream);
                oo.writeObject(head);
                oo.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    public boolean sendBody(JSONObject object) {
//        try {
//            if (object != null) {
//                OutputStream outputStream = socket.getOutputStream();
//                DataOutputStream ouput = new DataOutputStream(outputStream);
//                ouput.writeUTF(object.toString());
//                ouput.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    public boolean sendBody(JSONObject object) {
        try {
            if (object != null) {
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream ouput = new DataOutputStream(outputStream);
                ouput.write(object.toString().getBytes());
                ouput.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendString(String data) {
        try {
            if (data != null) {
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream ouput = new DataOutputStream(outputStream);
                ouput.writeUTF(data);
                ouput.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendFile(File file) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = cr.openInputStream(Uri.fromFile(file));
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public SocketHead receiveHead() {
        try {
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream oi = new ObjectInputStream(inputStream);
            return (SocketHead) oi.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSONObject receiveBody() {
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream input = new DataInputStream(inputStream);
            return new JSONObject(input.readUTF());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSONObject receiveBody(long dataLength) {
        try {
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                dataLength -= len;
                if (dataLength <= 0) {
                    break;
                }
            }
            return new JSONObject(bos.toString("UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String receiveString() {
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream input = new DataInputStream(inputStream);
            return input.readUTF();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public File receiveFile(String path, String name, long dataLength) {
        File f = new File(path + File.separator + name);
        if (f.exists()) {
            f.delete();
        }
        File dirs = new File(f.getParent());
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        FileOutputStream outputStream;
        try {
            f.createNewFile();
            InputStream inputStream = socket.getInputStream();
            outputStream = new FileOutputStream(f);
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                dataLength -= len;
                if (dataLength <= 0) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public void closeClientSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void shutdownSteam() {
        try {
            if (socket != null) {
                socket.shutdownInput();
                socket.shutdownOutput();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Created by user on 2017/6/8.
     * 包名必须与客户端一直,否则无法反序列化
     */

    public class SocketHead implements Serializable{
        public int flag; // 常量，用于校验 Header
        public int cmd;  // 操作码，表示本次操作的意图，比如获取数据库文件，获取日志文件
        public long dataLen; // 数据长度
        public long timestamp; //时间戳

        public SocketHead(int cmd, long dataLen) {
            this.cmd = cmd;
            this.dataLen = dataLen;
            this.timestamp = new Date().getTime();
        }

        public SocketHead(int cmd, long dataLen,long timestamp) {
            this.cmd = cmd;
            this.dataLen = dataLen;
            this.timestamp = timestamp;
        }
    }
}