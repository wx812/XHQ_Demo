package com.xhq.demo.widget.IPEdiText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xhq.demo.R;

import java.util.StringTokenizer;

public class IPAddrEditText extends LinearLayout implements View.OnFocusChangeListener {

	private static final String DIGITS = "0123456789";
	protected EditText editText1;
	protected EditText editText2;
	protected EditText editText3;
	protected EditText editText4;
	private Context mContext;

	public IPAddrEditText(Context context) {
		this(context, null);
	}

	public IPAddrEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.activity_edittext_show, this, true);
		initView();
	}

	private void initView() {
		this.editText1 = findViewById(R.id.editip_part1_id);
		this.editText2 = findViewById(R.id.editip_part2_id);
		this.editText3 = findViewById(R.id.editip_part3_id);
		this.editText4 = findViewById(R.id.editip_part4_id);
		initEditText(this.editText1);
		initEditText(this.editText2);
		initEditText(this.editText3);
		initEditText(this.editText4);
	}

	@SuppressLint({ "NewApi", "InlinedApi" })
	private void initEditText(EditText et) {
		et.setText("");
		et.setPadding(0, 0, 5, 3);
		et.addTextChangedListener(new IpEditBoxWatcher(et));
		et.setSingleLine();
		et.setOnFocusChangeListener(this);
		et.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
				        && (keyCode == KeyEvent.KEYCODE_DEL))
					IPAddrEditText.this.handleDelKey();
				return false;
			}
		});
		// et.setInputType(InputType.TYPE_CLASS_PHONE);
	}

	private boolean ipStringisNull(String string) {
		return (string == null) || (string.equals(""));
	}

	private boolean validate(String string) {
		if ((string.equals("")) || (string.length() == 0)) {
			return true;
		}

		if (string.length() >= 2) {
			if (string.startsWith("0")) {
//				Toast.makeText(this.mContext, R.string.ip_illegal_characters, Toast.LENGTH_SHORT).show();
				return false;
			}

            return Integer.parseInt(string) <= 255;
		}

		return true;
	}

	public void setEnabled(boolean enable) {
		this.editText1.setEnabled(enable);
		this.editText2.setEnabled(enable);
		this.editText3.setEnabled(enable);
		this.editText4.setEnabled(enable);
	}

	public void setFocusable(boolean focus) {
		this.editText1.setFocusable(focus);
		this.editText2.setFocusable(focus);
		this.editText3.setFocusable(focus);
		this.editText4.setFocusable(focus);
	}

	public void setListenner() {
		this.editText1.setOnFocusChangeListener(this);
		this.editText2.setOnFocusChangeListener(this);
		this.editText3.setOnFocusChangeListener(this);
		this.editText4.setOnFocusChangeListener(this);
	}

	public void addTextChangedListener(TextWatcher watcher) {
		this.editText1.addTextChangedListener(watcher);
		this.editText2.addTextChangedListener(watcher);
		this.editText3.addTextChangedListener(watcher);
		this.editText4.addTextChangedListener(watcher);
	}

	protected boolean check() {
		return !isEmpty();
	}

	public boolean isEmpty() {
		String str1 = this.editText1.getText().toString();
		String str2 = this.editText2.getText().toString();
		String str3 = this.editText3.getText().toString();
		String str4 = this.editText4.getText().toString();
		return (ipStringisNull(str1)) || (ipStringisNull(str2)) || (ipStringisNull(str3)) || (ipStringisNull(str4));
	}

	public void setZero() {
		String str1 = this.editText1.getText().toString();
		String str2 = this.editText2.getText().toString();
		String str3 = this.editText3.getText().toString();
		String str4 = this.editText4.getText().toString();
		if (ipStringisNull(str1)) {
			editText1.setText("0");
		}
		if (ipStringisNull(str2)) {
			editText2.setText("0");
		}
		if (ipStringisNull(str3)) {
			editText3.setText("0");
		}
		if (ipStringisNull(str4)) {
			editText4.setText("0");
		}
	}

	protected boolean checkFirstNum(String ip) {
		if ((ip == null) || ("".equals(ip)))
			return false;

		try {
			int i = Integer.parseInt(ip);
			if ((i != 0) && (i != 127) && (i != 169) && (i <= 223))
				return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean equals(Object obj) {
        return (obj instanceof IPAddrEditText) && (((IPAddrEditText)obj).getText().equals(getText()));
    }

	public boolean isFocusedByIpEditText() {
		return (this.editText1.isFocused()) || (this.editText2.isFocused())
		        || (this.editText3.isFocused()) || (this.editText4.isFocused());
	}

	public String getText() {
        return String.valueOf(this.editText1.getText()) + "." + this.editText2.getText() +
                "." + this.editText3.getText() + "." +
                this.editText4.getText();
	}

	public boolean isFocusedByIpEditTextOne() {
		return this.editText1.isFocused();
	}

	public void setText(String txt) {
		if (txt.equals("")) {
			this.editText1.setText("");
			this.editText2.setText("");
			this.editText3.setText("");
			this.editText4.setText("");
		} else {
			StringTokenizer sTokenizer = new StringTokenizer(txt, ".");
			if (sTokenizer.countTokens() == 4) {
				String[] strs = new String[sTokenizer.countTokens()];
				for (int i = 0; sTokenizer.hasMoreTokens(); i++) {
					strs[i] = sTokenizer.nextToken();
				}

				this.editText1.setText(strs[0]);
				this.editText2.setText(strs[1]);
				this.editText3.setText(strs[2]);
				this.editText4.setText(strs[3]);
			}
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocused) {
		View parent = (View) v.getParent().getParent().getParent();
		// if (hasFocused) {
		// parent.setBackgroundResource(R.drawable.edittext_has_focus_bg);
		// if (v instanceof EditText) {
		// int len = ((EditText)v).getText().toString().length();
		// ((EditText)v).setSelection(len);
		// }
		// } else {
		// parent.setBackgroundResource(R.drawable.edittext_has_no_focus_bg);
		// }
	}

	private void handleDelKey() {
		EditText et = null;
		if (this.editText4.isFocused()) {
			et = this.editText4;
		} else if (this.editText3.isFocused()) {
			et = this.editText3;
		} else if (this.editText2.isFocused()) {
			et = this.editText2;
		}

		if (et != null) {
			View v = focusSearch(et, View.FOCUS_LEFT);
			if (v instanceof EditText) {
				EditText et2 = (EditText) v;
				if (et.getText().length() == 0) {
					et2.requestFocus();
				}
			}
		}
	}

	class IpEditBoxWatcher implements TextWatcher {
		private boolean bError = false;
		private EditText editText;
		private String tmp = "";

		public IpEditBoxWatcher(EditText et) {
			this.editText = et;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String str = s.toString();
			int len = str.length();
			if ((this.bError) || (str.equals(this.tmp)))
				return;

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				if (DIGITS.indexOf(str.charAt(i)) >= 0) {
					sb.append(str.charAt(i));
				}
			}
			this.tmp = sb.toString();
			int tmpLen = this.tmp.length();
			if (tmpLen != 0 && tmpLen <= 3) {
				if (!IPAddrEditText.this.validate(this.tmp)) {
					this.bError = true;
				}
			} else {
				this.bError = false;
			}
			if (this.bError) {
				if (Integer.parseInt(str) > 255) {
					this.editText.setText("255");
				}

				this.editText.selectAll();
			} else {

				if (tmpLen == 3) {
					if (IPAddrEditText.this.validate(this.tmp)) {
						View v = this.editText.focusSearch(View.FOCUS_RIGHT);
						if (v != null && v instanceof EditText) {
							v.requestFocus();
						}
					}
				}
				this.editText.setSelection(tmpLen);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			this.tmp = s.toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
	}
}
