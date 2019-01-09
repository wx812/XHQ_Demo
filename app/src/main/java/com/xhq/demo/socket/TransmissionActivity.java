//package com.xhq.demo.socket;
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.NetworkInfo;
//import android.net.wifi.WifiManager;
//import android.net.wifi.WpsInfo;
//import android.net.wifi.p2p.WifiP2pConfig;
//import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pDeviceList;
//import android.net.wifi.p2p.WifiP2pInfo;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.zkh360.evmlib.OnResponseListener;
//import com.zkh360.operationassistant.R;
//import com.zkh360.operationassistant.base.BaseActivity;
//import com.zkh360.operationassistant.data.AppDataBase;
//import com.zkh360.operationassistant.data.EvmClient;
//import com.zkh360.operationassistant.data.MyRoom;
//import com.zkh360.operationassistant.data.entity.VendingInfo;
//import com.zkh360.operationassistant.transmission.utils.ViewHolder;
//import com.zkh360.operationassistant.util.CusCountDownTimer;
//import com.zkh360.operationassistant.util.LogUtils;
//import com.zkh360.operationassistant.util.PrefsUtil;
//import com.zkh360.operationassistant.util.SyncFileUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Locale;
//
///**
// * 与收获机交互数据
// * @author Lenovo
// */
//public class TransmissionActivity extends BaseActivity {
//    public static final String TAG = "TransmissionActivity";
//
//    private WifiP2pManager mManager;
//    private WifiP2pManager.Channel mChannel;
//    private BroadcastReceiver mReceiver;
//    private ArrayList<WifiP2pDevice> mWifiP2pDevices = new ArrayList<>();
//    private BaseAdapter adapter;
//    private Button mSendDataToVendingButton;
//    private LinearLayout mSendLayout;
//    private ProgressDialog progressDialog = null;
//    private WifiP2pInfo mWifiInfo;
//    private String mVendingCode;
//    private TextView mTimelinessTextView;
//    private CusCountDownTimer mCountDownTimer;
//    private Handler handler = new Handler();
//    private TextView mDownloadTipTextView;
//    private boolean mIsConnected = false;
//    private TextView mVendingCodeTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transmission);
//
//        initToolBar();
//
//        mTimelinessTextView = findViewById(R.id.timeliness);
//        mVendingCodeTextView = findViewById(R.id.keyword);
//
//        Button dbButton = findViewById(R.id.sendDBBtn);
//        dbButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LogUtils.i(TAG, "GET_UPLOADDATA begin");
//                // 如果有一分钟内的文件，那提示操作太快，等一会再操作
//                String updateDB = SyncFileUtils.getNewestVendingDatabase(mVendingCode);
//                if (updateDB != null) {
//                    File updateDBFile = new File(updateDB);
//                    if (updateDBFile.exists()) {
//                        if (System.currentTimeMillis() - updateDBFile.lastModified() < 60*1000) {
//                            showToast("一分钟内拉取多次没什么意义呢");
//                            LogUtils.i(TAG, "GET_UPLOADDATA multi times in short time");
//                            return;
//                        }
//                    }
//                }
//
//                SendThread thread = new SendThread(TransmissionActivity.this,
//                        mWifiInfo.groupOwnerAddress.getHostAddress(), SendThread.GET_UPLOADDATA, null);
//                thread.setVendingCode(mVendingCode);
//                thread.setSocketListener(new SendThread.SocketListener() {
//                    @Override
//                    public void onSuccess(final Object object) { // object 为更新文件时间戳
//                        if (object == null) {
//                            showToast("没有文件需要传输");
//                            LogUtils.i(TAG, "GET_UPLOADDATA no file to transfer");
//                        } else {
//                            showToast("接受文件成功");
//                            LogUtils.i(TAG, "GET_UPLOADDATA receive file succeed");
//                            initUploadTip();
//                            EvmClient.getInstance().putVendingUpdate(mVendingCode, new OnResponseListener<String>() {
//                                @Override
//                                public void onSuccess(String data) {
//                                    showToast("上传更新文件成功");
//                                    LogUtils.i(TAG, "GET_UPLOADDATA upload to server succeed");
//                                    initUploadTip();
//                                    PrefsUtil.putBoolean(TransmissionActivity.this, mVendingCode + PrefsUtil.SUFFIX_UPLOADED, true);
//                                    uploadReceiptToVending();
//                                }
//
//                                @Override
//                                public void onFail(String msg) {
//                                    showToast("上传更新文件失败");
//                                    LogUtils.i(TAG, "GET_UPLOADDATA upload to server failed");
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onFail() {
//                        showToast("接受文件失败");
//                        LogUtils.i(TAG, "GET_UPLOADDATA receive file failed");
//                    }
//                });
//                thread.start();
//            }
//        });
//
//        mSendDataToVendingButton = findViewById(R.id.sendDataToVending);
//        mSendDataToVendingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtils.i(TAG, "SEND_DOWNLOADDATA begin");
//                SendThread thread = new SendThread(TransmissionActivity.this,
//                        mWifiInfo.groupOwnerAddress.getHostAddress(), SendThread.SEND_DOWNLOADDATA, null);
//                thread.setVendingCode(mVendingCode);
//                thread.setSocketListener(new SendThread.SocketListener() {
//                    @Override
//                    public void onSuccess(Object object) {
//                        if (object == null) {
//                            toast("没有文件需要发送");
//                            LogUtils.i(TAG, "SEND_DOWNLOADDATA no file to transfer");
//                            return;
//                        }
//
//                        showToast("发送文件成功");
//                        LogUtils.i(TAG, "SEND_DOWNLOADDATA send file succeed");
//                        String serverDb = SyncFileUtils.getNewestServerDatabase(mVendingCode);
//                        if (!TextUtils.isEmpty(serverDb)) {
//                            new File(serverDb).delete();
//
//                            if (mCountDownTimer != null) {
//                                mCountDownTimer.cancel();
//                            }
//                            mSendDataToVendingButton.setEnabled(false);
//                            mTimelinessTextView.setText(R.string.no_data_to_send);
//
//                            final String simpleFileName = SyncFileUtils.getSimpleName(serverDb);
//                            EvmClient.getInstance().downCfgToVendorDone(simpleFileName, new OnResponseListener<String>() {
//                                @Override
//                                public void onSuccess(String data) {
//                                    SyncFileUtils.removeDoneUpdate(simpleFileName);
//                                    LogUtils.i(TAG, "SEND_DOWNLOADDATA receipt to server succeed");
//                                }
//
//                                @Override
//                                public void onFail(String msg) {
//                                    LogUtils.i(TAG, "SEND_DOWNLOADDATA receipt to server failed");
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onFail() {
//                        showToast("发送文件失败");
//                        LogUtils.i(TAG, "SEND_DOWNLOADDATA send file failed");
//                    }
//                });
//                thread.start();
//            }
//        });
//
//        Button logBtn = findViewById(R.id.sendLogBtn);
//        logBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LogUtils.i(TAG, "GET_LOGLIST begin");
//                SendThread thread = new SendThread(TransmissionActivity.this,
//                        mWifiInfo.groupOwnerAddress.getHostAddress(), SendThread.GET_LOGLIST, null);
//                thread.setVendingCode(mVendingCode);
//                thread.setSocketListener(new SendThread.SocketListener() {
//                    @Override
//                    public void onSuccess(Object object) {
//                        LogUtils.i(TAG, "GET_LOGLIST get file list succeed");
//                        JSONObject json = (JSONObject) object;
//                        final JSONArray array = json.optJSONArray("data");
//                        final Dialog dialog = new Dialog(TransmissionActivity.this);
//                        View v = getLayoutInflater().inflate(R.layout.dialog_loglist, null);
//                        ListView listView = v.findViewById(R.id.listview);
//                        listView.setAdapter(new BaseAdapter() {
//                            @Override
//                            public int getCount() {
//                                return array == null ? 0 : array.length();
//                            }
//
//                            @Override
//                            public Object getItem(int i) {
//                                return null;
//                            }
//
//                            @Override
//                            public long getItemId(int i) {
//                                return 0;
//                            }
//
//                            @Override
//                            public View getView(int i, View view, ViewGroup viewGroup) {
//                                if (view == null) {
//                                    view = getLayoutInflater().inflate(R.layout.loglist_item, null);
//                                }
//                                TextView nameText = ViewHolder.get(view, R.id.name);
//                                TextView sizeText = ViewHolder.get(view, R.id.size);
//                                JSONObject object = array.optJSONObject(i);
//                                nameText.setText(object.optString("name"));
//                                sizeText.setText(object.optLong("size") * 100 / 1024 / 1024 / 100f + "MB");
//                                return view;
//                            }
//                        });
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                LogUtils.i(TAG, "GET_LOGFILE begin");
//                                dialog.dismiss();
//                                JSONObject object = new JSONObject();
//                                JSONObject temp = new JSONObject();
//                                try {
//                                    temp.put("name", array.optJSONObject(i).optString("name"));
//                                    object.put("data", temp);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                SendThread thread = new SendThread(TransmissionActivity.this,
//                                        mWifiInfo.groupOwnerAddress.getHostAddress(), SendThread.GET_LOGFILE, object);
//                                thread.setSocketListener(new SendThread.SocketListener() {
//                                    @Override
//                                    public void onSuccess(Object object) {
//                                        showToast("接受日志文件成功");
//                                        LogUtils.i(TAG, "GET_LOGFILE get log file succeed");
//                                    }
//
//                                    @Override
//                                    public void onFail() {
//                                        showToast("接受日志文件失败");
//                                        LogUtils.i(TAG, "GET_LOGFILE get log file failed");
//                                    }
//                                });
//                                thread.start();
//                            }
//                        });
//                        dialog.setContentView(v);
//                        dialog.show();
//                    }
//
//                    @Override
//                    public void onFail() {
//                        showToast("接受日志文件列表失败");
//                        LogUtils.i(TAG, "GET_LOGLIST get file list failed");
//                    }
//                });
//                thread.start();
//            }
//        });
//        mSendLayout = findViewById(R.id.sendBtnLayout);
//        mSendLayout.setVisibility(View.GONE);
//        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(this, getMainLooper(), null);
//        mReceiver = new WiFiDirectBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//        registerReceiver(mReceiver, intentFilter);
//        ListView listView = findViewById(R.id.listview);
//        adapter = new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return mWifiP2pDevices.size();
//            }
//
//            @Override
//            public Object getItem(int i) {
//                return null;
//            }
//
//            @Override
//            public long getItemId(int i) {
//                return 0;
//            }
//
//            @Override
//            public View getView(int i, View view, ViewGroup viewGroup) {
//                view = getLayoutInflater().inflate(R.layout.list_item_devices, null);
//                TextView nameText = view.findViewById(R.id.nameText);
//                TextView statusText = view.findViewById(R.id.statusText);
//                nameText.setText(mWifiP2pDevices.get(i).deviceName);
//                statusText.setText(getDeviceStatus(mWifiP2pDevices.get(i).status));
//                return view;
//            }
//        };
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
//                LogUtils.i(TAG, "connect to vending begin");
//                final WifiP2pDevice device = mWifiP2pDevices.get(i);
//                if (device.status != WifiP2pDevice.CONNECTED && device.status != WifiP2pDevice.INVITED) {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            final WifiP2pConfig config = new WifiP2pConfig();
//                            config.deviceAddress = device.deviceAddress;
//                            config.wps.setup = WpsInfo.PBC;
//                            config.groupOwnerIntent = 0;
//                            dismissProgressDialog();
//                            progressDialog = ProgressDialog.show(TransmissionActivity.this,
//                                    "连接中", "正在连接至 :"
//                                            + device.deviceAddress, true, true,
//                                    new DialogInterface.OnCancelListener() {
//                                        @Override
//                                        public void onCancel(DialogInterface dialog) {
//                                            cancelConnect();
//                                        }
//                                    }
//                            );
//                            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//                                @Override
//                                public void onSuccess() {
//                                    // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
//                                }
//
//                                @Override
//                                public void onFailure(int reason) {
//                                    showToast("连接失败");
//                                    LogUtils.i(TAG, "connect to vending failed");
//                                    removeGroup();
//                                    dismissProgressDialog();
//                                }
//                            });
//                        }
//                    }, 1000);
//                } else {
//                    removeGroup();
//                }
//            }
//        });
//
//        mDownloadTipTextView = findViewById(R.id.download_tip);
//
//        //隔一段时间搜索一次，保证自身的可见性
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                discoverPeers();
//                handler.postDelayed(this, 60000);
//            }
//        });
//
//        initUploadTip();
//
//        openWiFi();
//    }
//
//    private void initUploadTip() {
//        String filename = SyncFileUtils.getNewestVendingDatabase(mVendingCode);
//        if (TextUtils.isEmpty(filename)) {
//            mDownloadTipTextView.setText(R.string.no_available_data_local);
//            return;
//        }
//
//        File file = new File(filename);
//        if (!file.exists() || file.isDirectory()) {
//            mDownloadTipTextView.setText(R.string.no_available_data_local);
//            return;
//        }
//
//        mDownloadTipTextView.setText(R.string.data_available_to_upload);
//    }
//
//    private void initToolBar() {
//        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//        myToolbar.setNavigationIcon(R.drawable.left);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//    }
//
//    private void uploadReceiptToVending() {
//        LogUtils.i(TAG, "UPLOAD_DATA_RECEIPT begin");
//
//        final long undoTimestamp = PrefsUtil.getLong(TransmissionActivity.this, mVendingCode + PrefsUtil.SUFFIX_FILE, 0L);
//        if (undoTimestamp == 0L) {
//            LogUtils.i(TAG, "UPLOAD_DATA_RECEIPT upload receipt to vending, no timestamp");
//            return;
//        }
//        boolean uploaded = PrefsUtil.getBoolean(TransmissionActivity.this, mVendingCode + PrefsUtil.SUFFIX_UPLOADED, false);
//        if (!uploaded) {
//            LogUtils.i(TAG, "UPLOAD_DATA_RECEIPT upload receipt to vending, undone");
//            return;
//        }
//
//        SendThread thread = new SendThread(TransmissionActivity.this,
//                mWifiInfo.groupOwnerAddress.getHostAddress(), SendThread.UPLOAD_DATA_RECEIPT, null);
//        thread.setVendingCode(mVendingCode);
//        thread.setTimestamp(undoTimestamp);
//        thread.setSocketListener(new SendThread.SocketListener() {
//            @Override
//            public void onSuccess(Object object) {
//                showToast("给售货机发送回执成功");
//                LogUtils.i(TAG, String.format(Locale.US, "send receipt to vending succeed, timestamp is %d", undoTimestamp));
//                PrefsUtil.remove(TransmissionActivity.this, mVendingCode + PrefsUtil.SUFFIX_FILE);
//                PrefsUtil.remove(TransmissionActivity.this, mVendingCode + PrefsUtil.SUFFIX_UPLOADED);
//            }
//
//            @Override
//            public void onFail() {
//                showToast("给售货机发送回执失败");
//                LogUtils.i(TAG, "send receipt to vending failed");
//            }
//        });
//        thread.start();
//    }
//
//    private void initTimeliness() {
//        String filename = SyncFileUtils.getNewestServerDatabase(mVendingCode);
//        if (TextUtils.isEmpty(filename)) {
//            return;
//        }
//
//        File file = new File(filename);
//        if (!file.exists() || file.isDirectory()) {
//            return;
//        }
//
//        long modifyTime = file.lastModified();
//        long remain = (System.currentTimeMillis() - modifyTime);
//        long expire = 30 * 60 * 1000;
//        if (remain > expire) {
//            file.delete();
//            mSendDataToVendingButton.setEnabled(false);
//            mTimelinessTextView.setText(R.string.no_data_to_send);
//        } else {
//            mSendDataToVendingButton.setEnabled(true);
//            mCountDownTimer = new CusCountDownTimer(mTimelinessTextView, R.string.data_timeliness, R.string.no_data_to_send, expire - remain, 1000);
//            mCountDownTimer.start();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mReceiver);
//        handler.removeCallbacksAndMessages(null);
//        removeGroup();
//    }
//
//    private void showToast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//
//    private void dismissProgressDialog() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }
//
//    public void cancelConnect() {
//        if (mManager != null) {
//            mManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
//
//                @Override
//                public void onSuccess() {
//                    showToast("中断连接成功");
//                }
//
//                @Override
//                public void onFailure(int reasonCode) {
//                    showToast("中断连接失败");
//                    removeGroup();
//                }
//            });
//        }
//    }
//
//    private void discoverPeers() {
//        Log.d(TAG, "discoverPeers");
//        mManager.discoverPeers(mChannel, null);
//    }
//
//    public void removeGroup() {
//        mManager.removeGroup(mChannel, null);
//    }
//
//    public void updateThisDevice(WifiP2pDevice device) {
//        TextView view = findViewById(R.id.my_name);
//        view.setText(device.deviceName);
//        view = findViewById(R.id.my_status);
//        view.setText(getDeviceStatus(device.status));
//    }
//
//    private static String getDeviceStatus(int deviceStatus) {
//        switch (deviceStatus) {
//            case WifiP2pDevice.AVAILABLE:
//                return "待连接";
//            case WifiP2pDevice.INVITED:
//                return "连接中";
//            case WifiP2pDevice.CONNECTED:
//                return "已连接";
//            case WifiP2pDevice.FAILED:
//                return "失败";
//            case WifiP2pDevice.UNAVAILABLE:
//                return "不可用";
//            default:
//                return "未知";
//
//        }
//    }
//
//    private void openWiFi() {
//        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (wm != null && !wm.isWifiEnabled()) {
//            wm.setWifiEnabled(true);
//        }
//    }
//
//    class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
//
//        WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
//            @Override
//            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
//                mWifiP2pDevices.clear();
//                mWifiP2pDevices.addAll(wifiP2pDeviceList.getDeviceList());
//                adapter.notifyDataSetChanged();
//            }
//        };
//
//        WifiP2pManager.ConnectionInfoListener connectionListener = new WifiP2pManager.ConnectionInfoListener() {
//            @Override
//            public void onConnectionInfoAvailable(WifiP2pInfo info) {
//                if (info.groupFormed && info.isGroupOwner) {
////                    showToast("server");
//                } else if (info.groupFormed) {
////                    showToast("client");
//                    mWifiInfo = info;
//                    dismissProgressDialog();
//
//                    LogUtils.i(TAG, "onConnectionInfoAvailable called");
//
//                    if (!mIsConnected) {
//                        LogUtils.i(TAG, "GET_VENDING_CODE begin");
//                        SendThread thread = new SendThread(TransmissionActivity.this,
//                                mWifiInfo.groupOwnerAddress.getHostAddress(), SendThread.GET_VENDING_CODE, null);
//                        thread.setSocketListener(new SendThread.SocketListener() {
//                            @Override
//                            public void onSuccess(Object object) {
//                                JSONObject jsonObject = (JSONObject) object;
//                                String vendingCode = jsonObject.optString("data");
//                                if (TextUtils.isEmpty(vendingCode)) {
//                                    showToast("获取售货机编号失败，不能下发数据，需要重进本页面");
//                                    LogUtils.i(TAG, "GET_VENDING_CODE failed, data is null");
//                                } else {
//                                    LogUtils.i(TAG, "GET_VENDING_CODE data is " + vendingCode);
//                                    String[] arr = vendingCode.split("-");
//                                    if (arr.length >= 3) {
//                                        mVendingCode = arr[0];
//                                        try {
//                                            final VendingInfo vendingInfo = new VendingInfo(arr[0].toUpperCase(), Integer.valueOf(arr[1]), arr[2].toUpperCase());
//                                            MyRoom.getInstance().executeAsync(new MyRoom.Transaction() {
//                                                @Override
//                                                public void execute(AppDataBase db) {
//                                                    db.vendingInfoDao().insert(vendingInfo);
//                                                }
//                                            });
//                                        } catch (NumberFormatException e) {
//                                            LogUtils.e(e);
//                                        }
//                                    } else {
//                                        mVendingCode = vendingCode;
//                                    }
//                                    mIsConnected = true;
//                                    initTimeliness();
//                                    mVendingCodeTextView.setText(getApplicationContext().getString(R.string.vending_code_format, mVendingCode));
//                                    mSendLayout.setVisibility(View.VISIBLE);
//                                    uploadReceiptToVending();
//                                    LogUtils.i(TAG, "GET_VENDING_CODE succeed");
//                                }
//                            }
//
//                            @Override
//                            public void onFail() {
//                                showToast("获取售货机编号失败，不能下发数据，需要重进本页面");
//                                LogUtils.i(TAG, "GET_VENDING_CODE failed");
//                            }
//                        });
//                        thread.start();
//                    }
//                }
//            }
//        };
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
//                Log.d(TAG, "P2P_STATE_CHANGED");
//                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
//                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
//                    showToast("wifi已打开");
//                } else {
//                    showToast("wifi处于关闭状态,正在打开wifi...");
//
//                    openWiFi();
//
//                    mWifiP2pDevices.clear();
//
//                    adapter.notifyDataSetChanged();
//                }
//            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
//                Log.d(TAG, "P2P_PEERS_CHANGED");
//                if (mManager != null) {
//                    mManager.requestPeers(mChannel, peerListListener);
//                }
//            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
//                if (mManager != null) {
//                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
//                    Log.d(TAG, "P2P_CONNECTION_CHANGED " + (networkInfo.isConnected() ? "connected" : "disconnected"));
//                    if (networkInfo.isConnected()) {
//                        //我们连上了其他的设备，请求连接信息，以找到群主的IP。
//                        mManager.requestConnectionInfo(mChannel, connectionListener);
//                    } else {
//                        dismissProgressDialog();
//                        discoverPeers();
//                        mSendLayout.setVisibility(View.GONE);
//                        mIsConnected = false;
//                    }
//                }
//            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//                Log.d(TAG, "P2P_THIS_DEVICE_CHANGED");
//                updateThisDevice(intent
//                        .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
//            }
//        }
//    }
//}