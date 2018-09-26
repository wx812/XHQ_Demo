package com.xhq.demo.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 银行卡号格式输入  created by xyy @20170626
 */
public class BankCardEditText extends AppCompatEditText{
	public BankCardEditText(Context context) {
		super(context);
	}

	public BankCardEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBankCardTypeOn();
	}

	public BankCardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private boolean isRun = false;
	private String d = "";

	private int getEditTextCursorIndex(EditText editText) {
		return editText.getText().toString().length();
	}

	public void setBankCardTypeOn() {
		BankCardEditText.this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (isRun) {
					isRun = false;
					return;
				}
				isRun = true;
				d = "";
				String newStr = s.toString();
				newStr = newStr.replace(" ", "");

				int index = 0;
				while ((index + 4) < newStr.length()) {
					d += (newStr.substring(index, index + 4) + " ");
					index += 4;
				}
				d += (newStr.substring(index, newStr.length()));
				int i = getEditTextCursorIndex(BankCardEditText.this);

				BankCardEditText.this.setText(d);
				try {

					if (i % 5 == 0 && before == 0) {
						if (i + 1 <= d.length()) {// 判断位数再设置，否则在第四位的时候按空格程序会崩掉
							BankCardEditText.this.setSelection(i + 1);
						} else {
							BankCardEditText.this.setSelection(d.length());
						}
					} else if (before == 1 && i < d.length()) {
						BankCardEditText.this.setSelection(i);
					} else if (before == 0 && i < d.length()) {
						BankCardEditText.this.setSelection(i);
					} else
						BankCardEditText.this.setSelection(d.length());
				} catch (Exception e) {
					// VposUtility.showException(e);
				}

			}

			private void insertText(EditText editText, String mText) {
				editText.getText().insert(getEditTextCursorIndex(editText),
						mText);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
