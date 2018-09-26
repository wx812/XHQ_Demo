package com.xhq.demo.widget.CircleLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


/**
 * 所有的Activity继承的基类Activity,包含了ActionBar菜单
 *
 * @author duguang
 * 博客地址:http://blog.csdn.net/duguang77
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setView();
        initView();
        setListener();
        initData();
    }


    @Override
    protected void onResume(){
        super.onResume();
//        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause(){
        super.onPause();
//        MobclickAgent.onPause(this);
    }


    /**
     * 设置布局文件
     */
    public abstract void setView();

    /**
     * 初始化布局文件中的控件
     */
    public abstract void initView();

    /**
     * 设置控件的监听
     */
    public abstract void setListener();


    private void initData(){
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

//        switch(item.getItemId()){
//            case R.id.menu_about:
//                Intent intent = new Intent(BaseActivity.this, AboutActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.small_2_big, R.anim.fade_out);
//                return true;
//            case R.id.menu_feedback:
//                FeedbackAgent agent = new FeedbackAgent(this);
//                agent.startFeedbackActivity();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                return true;
//            case R.id.menu_share:
//                final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
//                                                                                        RequestType.SOCIAL);
//                mController.openShare(BaseActivity.this, false);
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
    }


}
