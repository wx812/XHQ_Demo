package com.xhq.im.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.xhq.common.constant.apiconfig.ApiKey;
import com.xhq.common.interfaces.view.IBaseView;
import com.xhq.common.tool.ToastUtils;
import com.xhq.im.R;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/11/2.
 *     Desc  : There are pages of the toolbar arrow.
 *     Updt  : 2017/12/4.
 * </pre>
 */
public class BaseArrowActivity extends BaseActivity implements View.OnClickListener, IBaseView{

//    private ProgressBar pb;
//    private ProgressDialog mProgressDialog;

    public Toolbar mToolbar;
    public LinearLayout ll_progress;
    public TextView tv_pb;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.tab_background));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow));
        String title = getIntent().getStringExtra(ApiKey.HomeKey.home_name);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);

        ll_progress = (LinearLayout)findViewById(R.id.ll_progress);
        tv_pb = (TextView)findViewById(R.id.tv_pb);
//        ll_progress.setVisibility(View.GONE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            OkGo.getInstance().cancelTag(this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v){

    }


    @Override
    public void showToast(String s){
        ToastUtils.showToast(s, true);
    }


    @Override
    public void showProgress(String msg){

//        if(mProgressDialog == null){
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setCanceledOnTouchOutside(false);
//        }
//        mProgressDialog.setMessage(msg);
//        mProgressDialog.show();

        if(!ll_progress.isShown()){
            ll_progress.setVisibility(View.VISIBLE);
        }
        tv_pb.setText(msg);

    }


    @Override
    public void hideProgress(){
        if(ll_progress.isShown()){
            ll_progress.setVisibility(View.GONE);
            OkGo.getInstance().cancelTag(this);
        }
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        OkGo.getInstance().cancelTag(this);
    }
}
