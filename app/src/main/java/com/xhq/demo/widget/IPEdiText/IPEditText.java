package com.xhq.demo.widget.IPEdiText;

import android.content.Context;
import android.util.AttributeSet;

public class IPEditText extends IPAddrEditText {

	public static final String TAG = "FH_IPEditText";

	public IPEditText(Context context) {
		super(context);
	}

	public IPEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean check() {
		if (!super.check())
			return false;
		if ((!checkFirstNum(this.editText1.getText().toString())) || (isContainsZero()))
			return false;
		return true;
	}

	protected boolean isContainsZero() {
		String ip = getText().toString().trim();
//		LogDebug.i(TAG, "address = " + ip);
		String et1 = this.editText1.getText().toString().trim();
		String et2 = this.editText2.getText().toString().trim();
		String et3 = this.editText3.getText().toString().trim();
		String et4 = this.editText4.getText().toString().trim();
		if (("0".equals(et1)) || ("0".equals(et4)))
			return true;
		if ((et1.startsWith("0")) || ((et2.startsWith("0")) && (et2.length() > 1))
		        || ((et3.startsWith("0")) && (et3.length() > 1)) || (et4.startsWith("0")))
			return true;

		return false;
	}
}
