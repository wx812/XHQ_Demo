//package com.xhq.demo.socket;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Looper;
//
//import com.zkh360.operationassistant.util.DateUtil;
//import com.zkh360.operationassistant.util.LogUtils;
//import com.zkh360.operationassistant.util.PrefsUtil;
//import com.zkh360.operationassistant.util.SyncFileUtils;
//
//import org.json.JSONObject;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// *
// * @author user
// * @date 2017/6/8
// */
//
//public class SendThread extends Thread {
//    private static final String TAG = "SendThread";
//    public static final int GET_DATABASE = 0;
//    public static final int GET_LOGLIST = 1;
//    public static final int GET_LOGFILE = 2;
//    public static final int GET_UPLOADDATA = 3;
//    public static final int SEND_DOWNLOADDATA = 4;
//    public static final int GET_VENDING_CODE = 5;
//    public static final int UPLOAD_DATA_RECEIPT = 6;
//
//    private final int port = 12450;
//    private Context context;
//    private String address;
//    private TCPUtils tcpUtils;
//    private SocketHead head;
//    private JSONObject body;
//    private SocketListener listener;
//    private Handler handler;
//    private ProgressDialog dialog;
//    private String vendingCode;
//    private long timestamp;
//
//    public void setVendingCode(String vendingCode) {
//        this.vendingCode = vendingCode;
//    }
//
//    public SendThread(Context context, String address, int cmd, JSONObject body) {
//        this.context = context;
//        this.address = address;
//        this.head = new SocketHead(cmd, body == null ? 0 : body.toString().getBytes().length);
//        this.body = body;
//        handler = new Handler(Looper.getMainLooper());
//    }
//
//    public void setSocketListener(SocketListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    public void run() {
//        showDialog();
//        tcpUtils = new TCPUtils(context);
//        tcpUtils.initSocket(address, port);
//        String fileName;
//        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
//        switch (head.cmd) {
//            case GET_DATABASE:
//                fileName = format.format(new Date()) + "vending_machine.sqlite";
//                requestFile(Environment.getExternalStorageDirectory() + "/" + context.getPackageName(), fileName);
//                break;
//            case GET_LOGFILE:
//                fileName = format.format(new Date()) + body.optJSONObject("data").optString("name");
//                requestFile(Environment.getExternalStorageDirectory() + "/" + context.getPackageName(), fileName);
//                break;
//            case GET_UPLOADDATA:
//                fileName = String.format("%s_%s.sqlite", vendingCode, DateUtil.getNow());
//                requestFile(SyncFileUtils.getVendingRootDir(vendingCode), fileName);
//                break;
//            case SEND_DOWNLOADDATA:
//                String serverDb = SyncFileUtils.getNewestServerDatabase(vendingCode);
//                if (serverDb == null) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listener.onSuccess(null);
//                        }
//                    });
//                    return;
//                }
//                File file = new File(serverDb);
//                head.dataLen = file.length();
//                sendFile(file);
//                break;
//            default:
//                requestData();
//                break;
//        }
//        tcpUtils.close();
//        dismissDialog();
//    }
//
//    private void requestData() {
//        if (tcpUtils.sendHead(head) && tcpUtils.sendBody(body)) {
//            SocketHead head = tcpUtils.receiveHead();
//            if (head == null) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onFail();
//                    }
//                });
//                return;
//            }
//            final JSONObject object = tcpUtils.receiveBody(head.dataLen);
//            if (object != null && head.dataLen == object.toString().getBytes().length) {
//                int result = object.optInt("result", 1);
//                if (result == 0) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listener.onSuccess(object);
//                        }
//                    });
//                } else {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listener.onFail();
//                        }
//                    });
//                }
//            } else {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onFail();
//                    }
//                });
//            }
//        } else {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    listener.onFail();
//                }
//            });
//        }
//    }
//
//    private void requestFile(String path, String name) {
//        if (tcpUtils.sendHead(head) && tcpUtils.sendBody(body)) {
//            final SocketHead head = tcpUtils.receiveHead();
//            if (head == null) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onFail();
//                    }
//                });
//                return;
//            }
//
//            if (head.dataLen == 0) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onSuccess(null);
//                    }
//                });
//                return;
//            }
//
//            final File file = tcpUtils.receiveFile(path, name, head.dataLen);
//            if (file != null && head.dataLen == file.length()) {
//                SyncFileUtils.removeOthers(SyncFileUtils.getVendingRootDir(vendingCode), file.getAbsolutePath());
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (SendThread.this.head.cmd == GET_UPLOADDATA) {
//                            PrefsUtil.putLong(context, vendingCode + PrefsUtil.SUFFIX_FILE, head.timestamp);
//                            PrefsUtil.putBoolean(context, vendingCode + PrefsUtil.SUFFIX_UPLOADED, false);
//                        }
//                        listener.onSuccess(String.valueOf(head.timestamp));
//                    }
//                });
//            } else {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onFail();
//                    }
//                });
//            }
//        } else {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    listener.onFail();
//                }
//            });
//        }
//    }
//
//    private void sendFile(File file) {
//        if (tcpUtils.sendHead(head) && tcpUtils.sendFile(file)) {
//            SocketHead head = tcpUtils.receiveHead();
//            if (head == null) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onFail();
//                    }
//                });
//                return;
//            }
//
//            final JSONObject object = tcpUtils.receiveBody(head.dataLen);
//            if (object != null && head.dataLen == object.toString().getBytes().length) {
//                int result = object.optInt("result", 1);
//                if (result == 0) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listener.onSuccess(object);
//                        }
//                    });
//                } else {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listener.onFail();
//                        }
//                    });
//                }
//            } else {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onFail();
//                    }
//                });
//            }
//        } else {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    listener.onFail();
//                }
//            });
//        }
//    }
//
//
//    private void showDialog() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (dialog != null && dialog.isShowing()) {
//                    return;
//                } else {
//                    dialog = new ProgressDialog(context);
//                    dialog.setMessage("通讯中,请稍后...");
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setCancelable(true);
//                    dialog.show();
//                }
//            }
//        });
//    }
//
//    private void dismissDialog() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//        if (head.cmd == UPLOAD_DATA_RECEIPT) {
//            head.timestamp = timestamp;
//        }
//    }
//
//    public interface SocketListener {
//        void onSuccess(Object object);
//
//        void onFail();
//    }
//}
