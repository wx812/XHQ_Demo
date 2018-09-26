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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * author ：shiyang
 * date : 2017/11、10
 * description : 添加好友提示窗
 */

public class AddFriendDialogFragment extends DialogFragment implements View.OnClickListener {
    private String userCode;
    private ItemClickCallBack listener;
    private EditText et_add_friend;
    private String titleText;
    private String hintText;
    private TextView titleView;
    private List<String> selected;

    public void setListener(ItemClickCallBack listener) {
        this.listener = listener;
    }

    public static AddFriendDialogFragment newInstance(String userCode) {
        Bundle args = new Bundle();
        args.putString("userCode", userCode);
        AddFriendDialogFragment fragment = new AddFriendDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddFriendDialogFragment newInstance(ArrayList<String> selected, String titleText, String hintText) {
        Bundle args = new Bundle();
        args.putSerializable("selected", (Serializable) selected);
        args.putString("titleText", titleText);
        args.putString("hintText", hintText);
        AddFriendDialogFragment fragment = new AddFriendDialogFragment();
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
            userCode = getArguments().getString("userCode");
            selected = (List<String>) getArguments().getSerializable("selected");
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
                if (null == selected) {
                    listener.onItemClickSure(userCode, msg);
                } else {
                    listener.onItemClickSure(selected, msg);
                }
                hideKeyboard();
                dismiss();
            }
        }
    }

    public interface ItemClickCallBack {
        void onItemClickSure(String userCode, String msg);

        void onItemClickSure(List<String> selected, String msg);
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext(). getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_add_friend.getWindowToken(), 0) ;
    }
}
