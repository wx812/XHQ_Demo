package com.netease.nim.uikit.dialogfragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.msg.model.SystemMessage;


/**
 * author ：shiyang
 * date : 2017/11、10
 * description : 拒绝加群
 */

public class RejectTeamApplyDialogFragment extends DialogFragment implements View.OnClickListener {
    private ItemClickCallBack listener;
    private EditText et_add_friend;
    private String titleText;
    private String hintText;
    private TextView titleView;
    private SystemMessage systemMessage;

    public void setListener(ItemClickCallBack listener) {
        this.listener = listener;
    }

    public static RejectTeamApplyDialogFragment newInstance(SystemMessage systemMessage, String titleText, String hintText) {
        Bundle args = new Bundle();
        args.putSerializable("systemMessage",systemMessage);
        args.putString("titleText", titleText);
        args.putString("hintText", hintText);
        RejectTeamApplyDialogFragment fragment = new RejectTeamApplyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //点击外部不消失
        getDialog().setCanceledOnTouchOutside(false);
        if (getArguments() != null) {
            systemMessage = (SystemMessage) getArguments().getSerializable("systemMessage");
            titleText = getArguments().getString("titleText", "");
            hintText = getArguments().getString("hintText", "");
        }

        View view = inflater.inflate(R.layout.dialog_fragment_add_friend, container, false);
        et_add_friend = (EditText) view.findViewById(R.id.et_add_friend);
        titleView = (TextView) view.findViewById(R.id.tv_c_noticeInfo);

        if (!TextUtils.isEmpty(titleText)) {
            titleView.setText(titleText);
        }

        if (!TextUtils.isEmpty(hintText)) {
            et_add_friend.setHint(hintText);
        }

        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            hideKeyboard();
            dismiss();
        } else if (v.getId() == R.id.tv_confirm) {
            if (listener != null) {
                String msg = et_add_friend.getText().toString();
                listener.onClickSure(msg,systemMessage);
                hideKeyboard();
                dismiss();
            }
        }
    }

    public interface ItemClickCallBack {
        void onClickSure(String msg,SystemMessage message);
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_add_friend.getWindowToken(), 0);
    }
}
