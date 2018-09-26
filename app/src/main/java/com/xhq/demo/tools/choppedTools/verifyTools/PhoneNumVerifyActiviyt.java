//package com.xhq.demo.tools.VerifyTools.VerifyCodeUtil;
//
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.xhq.demo.R;
//import com.xhq.demo.activity.MainActivity;
//
//
//
///**
// * 使用第三方的 SDK  手机验证
// * @author Administrator
// *
// */
//public class PhoneNumVerifyActiviyt extends Activity implements OnClickListener{
//
//    private EditText et_phoneNumber;
//    private EditText et_verifyNumber;
//    private TextView now;
//    private Button bt_verify;
//    private Button bt_start;
//
//    private String iet_phoneNumber;
//    private String iet_verifyNumber;
//    private int time = 60;
//    private boolean flag = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
////        setContentView(R.layout.activity_main);
//        init();
//
////        SMSSDK.initSDK(this, "<您的appkey>", "<您的appsecret>");
////        EventHandler eh=new EventHandler(){
//
//            public void afterEvent(int event, int result, Object data) {
//
//                Message msg = new Message();
//                msg.arg1 = event;
//                msg.arg2 = result;
//                msg.obj = data;
//                handler.sendMessage(msg);
//            }
//
//        };
//        SMSSDK.registerEventHandler(eh);
//
//    }
//
//    private void init() {
//        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
//
//        bt_verify = (Button) findViewById(R.id.bt_verify);
//
//        et_verifyNumber = (EditText) findViewById(R.id.et_verifyNumber);
//
//        bt_start = (Button) findViewById(R.id.bt_start);
//
//        now = (TextView) findViewById(R.id.now);
//
//        bt_verify.setOnClickListener(this);
//        bt_start.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        if (v.getId() == R.id.bt_verify) {
//			if(!TextUtils.isEmpty(et_phoneNumber.getText().toString().trim())){
//                if(et_phoneNumber.getText().toString().trim().length()==11){
//                    iet_phoneNumber = et_phoneNumber.getText().toString().trim();
//                    SMSSDK.getVerificationCode("86",iet_phoneNumber);
//                    et_verifyNumber.requestFocus();
//                    bt_verify.setVisibility(View.GONE);
//                }else{
//                    Toast.makeText(MainActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
//                    et_phoneNumber.requestFocus();
//                }
//            }else{
//                Toast.makeText(MainActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
//                et_phoneNumber.requestFocus();
//            }
//		} else if (v.getId() == R.id.bt_start) {
//			if(!TextUtils.isEmpty(et_verifyNumber.getText().toString().trim())){
//                if(et_verifyNumber.getText().toString().trim().length()==4){
//                    iet_verifyNumber = et_verifyNumber.getText().toString().trim();
//                    SMSSDK.submitVerificationCode("86", iet_phoneNumber, iet_verifyNumber);
//                    flag = false;
//                }else{
//                    Toast.makeText(MainActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
//                    et_verifyNumber.requestFocus();
//                }
//            }else{
//                Toast.makeText(MainActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
//                et_verifyNumber.requestFocus();
//            }
//		} else {
//		}
//    }
//
//    //验证码送成功后提示文字
//    private void reminderText() {
//        now.setVisibility(View.VISIBLE);
//        handlerText.sendEmptyMessageDelayed(1, 1000);
//    }
//
//    Handler handlerText =new Handler(){
//        public void handleMessage(Message msg) {
//            if(msg.what==1){
//                if(time>0){
//                    now.setText("验证码已发送"+time+"秒");
//                    time--;
//                    handlerText.sendEmptyMessageDelayed(1, 1000);
//                }else{
//                    now.setText("提示信息");
//                    time = 60;
//                    now.setVisibility(View.GONE);
//                    bt_verify.setVisibility(View.VISIBLE);
//                }
//            }else{
//                et_verifyNumber.setText("");
//                now.setText("提示信息");
//                time = 60;
//                now.setVisibility(View.GONE);
//                bt_verify.setVisibility(View.VISIBLE);
//            }
//        };
//    };
//
//    Handler handler=new Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//            int event = msg.arg1;
//            int result = msg.arg2;
//            Object data = msg.obj;
//            Log.e("event", "event="+event);
//            if (result == SMSSDK.RESULT_COMPLETE) {
//                //短信注册成功后，返回MainActivity,然后提示新好友
//                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
//                    Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
//                    handlerText.sendEmptyMessage(2);
//                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
//                    reminderText();
//                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
//                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
//                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                if(flag){
//                    bt_verify.setVisibility(View.VISIBLE);
//                    Toast.makeText(MainActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
//                    et_phoneNumber.requestFocus();
//                }else{
//                    ((Throwable) data).printStackTrace();
//                    int resId = getStringRes(MainActivity.this, "smssdk_network_error");
//                    Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//                    et_verifyNumber.selectAll();
//                    if (resId > 0) {
//                        Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//
//        }
//
//		private int getStringRes(MainActivity mainActivity, String string) {
//
//			return 0;
//		}
//
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SMSSDK.unregisterAllEventHandler();
//    }
//
//}