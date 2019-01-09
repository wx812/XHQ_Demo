package com.xhq.demo.socket;

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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Lenovo
 */
public class TCPUtils {

    private static final String TAG = "TCPUtils";

    private Socket socket;

    private ServerSocket serverSocket;

    private Context context;

    private byte[] buffer = new byte[1024];

    public TCPUtils(Context context) {
        this.context = context;
    }

    public void initSocket(String host, int port) {
        try {
            socket = new Socket();
            socket.setReuseAddress(true);
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 5000);
            setSoTimeout(120000);
        }catch (Exception e) {
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

    public void setSoTimeout(final int timeout) throws Exception {
        socket.setSoTimeout(timeout);
    }

    public int getSoTimeout() throws Exception {
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
        long length = file.length();
        int len;
        try {
            OutputStream outputStream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream is = cr.openInputStream(Uri.fromFile(file));
            if(null == is) return false;
            while ((len = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                length -= len;
                if (length <= 0) break;
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

    public JSONObject receiveBody(long dataLength) {
        try {
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
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

    public File receiveFile(String path, String name, long length) {
        int len;

        File f = new File(path + File.separator + name);
        if (f.exists()) {
            f.delete();
        }
        File dirs = new File(f.getParent());
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        try {
            f.createNewFile();
            InputStream inputStream = socket.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(f);
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                length -= len;
                if (length <= 0) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
}