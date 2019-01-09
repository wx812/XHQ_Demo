package com.xhq.demo.widget.IPEdiText;

import android.content.Context;
import android.util.AttributeSet;

public class SubnetMaskEditText extends IPAddrEditText {

	public static final String TAG = SubnetMaskEditText.class.getName();
	public SubnetMaskEditText(Context context) {
		super(context);
	}

	public SubnetMaskEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean check() {
		if (!super.check())
			return false;
		if (!checkFirstNum(this.editText1.getText().toString()))
			return false;
		return isValidMask(getText());
	}

	protected boolean checkFirstNum(String string) {
		if ((string == null) || ("".equals(string)))
			return false;
		try {
			int ip = Integer.parseInt(string);
			if ((ip != 0) && (ip != 127) && (ip != 169))
				return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isValidMask(String ipValue) {
		String[] ips = ipValue.split("\\.");

		StringBuilder binaryVal = new StringBuilder();
		if (ips.length != 4) return false;

        for(String ip : ips){
            StringBuilder binaryStr = new StringBuilder(Integer.toBinaryString(Integer.parseInt(ip)));

            int times = 8 - binaryStr.length();

            for(int j = 0; j < times; j++){
                binaryStr.insert(0, "0");
            }
            binaryVal.append(binaryStr);
        }

//		LogDebug.i(TAG,
//		        "binaryVal: " + binaryVal + " binaryVal.indexOf(0):" + binaryVal.indexOf("0")
//		                + " binaryVal.lastIndexOf(1):" + binaryVal.lastIndexOf("1"));
        return binaryVal.indexOf("0") >= binaryVal.lastIndexOf("1");

    }

}
