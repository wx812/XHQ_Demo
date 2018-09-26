package com.xhq.demo.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xhq.demo.R;

import java.util.Objects;


public class LoadDialog extends ProgressDialog {

	private TextView tips_text;
	private String content;


	public LoadDialog(Context ctx) {
		this(ctx, 0);
	}

	public LoadDialog(Context ctx, String msg){
		this(ctx, 0);
		WindowManager.LayoutParams wmlp= Objects.requireNonNull(this.getWindow()).getAttributes();
		wmlp.alpha=0.5f;
        wmlp.gravity = Gravity.CENTER;
		getWindow().setAttributes(wmlp);
		this.content = msg;
	}


	public LoadDialog(Context ctx, String msg, @StyleRes int styleResId) {
		this(ctx, styleResId);	// R.style.Transparent_Dialog
		this.content = msg;
	}

	public LoadDialog(Context ctx, int theme) {
		super(ctx, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		View view = getLayoutInflater().inflate(R.layout.progress_load, null);
		setCancelable(false);
		setCanceledOnTouchOutside(true);
		TextView tips_text = view.findViewById(R.id.textView1);
		if(!TextUtils.isEmpty(content)){
			tips_text.setText(content);
		}
//		view.setGravity(Gravity.CENTER);
		setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

}
