package com.xhq.demo.widget.IPEdiText;

import android.content.Context;
import android.util.AttributeSet;

public class DnsEditText extends IPAddrEditText {

	public DnsEditText(Context paramContext) {
		super(paramContext);
	}

	public DnsEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	private boolean checkDNS(String ipValue) {
        return checkDnsFirstNum(this.editText1.getText().toString());
    }

	private boolean checkDnsFirstNum(String ipValue) {
		if ((ipValue.length() >= 2) && (ipValue.startsWith("0")))
			return false;

		int ip = Integer.parseInt(ipValue);
        return (ip != 127) && (ip != 169) && (ip <= 223);
    }

	public boolean check() {
		if (isAllEmpty())
			return true;
		if (isEmpty())
			return false;
		return checkDNS(getText());
	}

	public boolean isAllEmpty() {
		return getText().split("\\.").length == 0;
	}
}
