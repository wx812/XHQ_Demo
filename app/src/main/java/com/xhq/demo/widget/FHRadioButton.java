package com.xhq.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhq.demo.R;
import com.xhq.demo.tools.uiTools.screen.DensityUtils;

import java.util.ArrayList;

public class FHRadioButton extends LinearLayout implements OnClickListener {

	private static final String TAG = "FHRadioButton";
	private static final int ORDER_IMAGE_FIRST = 0;
	private static final int ORDER_TEXT_FIRST = 1;
	private static final int DEFAULT_SELECTED_INDEX = 0;
	private static final float DEFAULT_MARGIN = 0f;
	private static final int DEFAULT_ORDER = ORDER_IMAGE_FIRST;
	private static final int DEFAULT_ORIENTATION = LinearLayout.HORIZONTAL;
	private static final int DEFAULT_NUM = 2;
	private static final int DEFAULT_TEXT_COLOR = 0xff000000;
	private static final float DEFAULT_TEXT_VIEW_TEXT_SIZE_SP = 16;
	private static final int DEFAULT_TEXTS_RES = 0;
	private static final int DEFAULT_IMAGE_RES = 0;
	private Context mContext;
	private int mDrawableBackgroundRadioSelected;
	private int mDrawableBackgroundRadio;
	private Drawable mDrawableBackgroundText;
	private float mTextMarginLeft = DEFAULT_MARGIN;
	private float mTextMarginRight = DEFAULT_MARGIN;
	private float mTextMarginTop = DEFAULT_MARGIN;
	private float mTextMarginBottom = DEFAULT_MARGIN;
	private float mImageMarginLeft = DEFAULT_MARGIN;
	private float mImageMarginRight = DEFAULT_MARGIN;
	private float mImageMarginTop = DEFAULT_MARGIN;
	private float mImageMarginBottom = DEFAULT_MARGIN;
	private float mUnitMarginLeft = DEFAULT_MARGIN;
	private float mUnitMarginRight = DEFAULT_MARGIN;
	private float mUnitMarginTop = DEFAULT_MARGIN;
	private float mUnitMarginBottom = DEFAULT_MARGIN;
	private int mOrder = DEFAULT_ORDER;
	private int mOrientation = DEFAULT_ORIENTATION;
	private int mNum = DEFAULT_NUM;
	private int mTextColor = DEFAULT_TEXT_COLOR;
	private float mTextSize = DEFAULT_TEXT_VIEW_TEXT_SIZE_SP;
	private int mTextsRes = DEFAULT_TEXTS_RES;
	private String[] mTexts;
	private ArrayList<ImageView> mImageViews = new ArrayList<>();
	private ArrayList<TextView> mTextViews = new ArrayList<>();
	private View mContentView = null;
	private LinearLayout mContainer = null;
	private Object mTagTextView = new Object();
	private Object mTagImageView = new Object();

	private int mSelectedIndex = DEFAULT_SELECTED_INDEX;

	public FHRadioButton(Context context) {
		this(context, null);
	}

	public FHRadioButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FHRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mContentView = inflate(context, R.layout.view_fh_radio_button, this);
		mContainer = mContentView.findViewById(R.id.container);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FHRadioButton);

		final int count = a.getIndexCount();
		for (int i = 0; i < count; ++i) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.FHRadioButton_backgroundRadioSelected:
				mDrawableBackgroundRadioSelected = a.getResourceId(attr, DEFAULT_IMAGE_RES);
				break;
			case R.styleable.FHRadioButton_backgroundRadio:
				mDrawableBackgroundRadio = a.getResourceId(attr, DEFAULT_IMAGE_RES);
				break;
			case R.styleable.FHRadioButton_backgroundText:
				mDrawableBackgroundText = a.getDrawable(attr);
				break;
			case R.styleable.FHRadioButton_textMarginLeft:
				mTextMarginLeft = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_textMarginRight:
				mTextMarginRight = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_textMarginTop:
				mTextMarginTop = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_textMarginBottom:
				mTextMarginBottom = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_imageMarginLeft:
				mImageMarginLeft = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_imageMarginRight:
				mImageMarginRight = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_imageMarginTop:
				mImageMarginTop = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_imageMarginBottom:
				mImageMarginBottom = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_unitMarginLeft:
				mUnitMarginLeft = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_unitMarginRight:
				mUnitMarginRight = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_unitMarginTop:
				mUnitMarginTop = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_unitMarginBottom:
				mUnitMarginBottom = a.getDimension(attr, DEFAULT_MARGIN);
				break;
			case R.styleable.FHRadioButton_order:
				mOrder = a.getInt(attr, DEFAULT_ORDER);
				break;
			case R.styleable.FHRadioButton_radioButtonNum:
				mNum = a.getInt(attr, DEFAULT_NUM);
				break;
			case R.styleable.FHRadioButton_contentTextColor:
				mTextColor = a.getColor(attr, DEFAULT_TEXT_COLOR);
				break;
			case R.styleable.FHRadioButton_contentTextSize:
				mTextSize = DensityUtils.px2sp(
				        a.getDimensionPixelSize(attr, DensityUtils.sp2px(DEFAULT_TEXT_VIEW_TEXT_SIZE_SP)));
				break;
			case R.styleable.FHRadioButton_optionsOrientation:
				mOrientation = a.getInt(attr, DEFAULT_ORIENTATION);
				break;
			case R.styleable.FHRadioButton_texts:
				mTextsRes = a.getResourceId(attr, DEFAULT_TEXTS_RES);
				mTexts = mContext.getResources().getStringArray(mTextsRes);
				break;
			case R.styleable.FHRadioButton_selectedIndex:
				mSelectedIndex = a.getInt(attr, DEFAULT_SELECTED_INDEX);
				break;

			}
		}
		a.recycle();

		mContainer.setOrientation(mOrientation);
		LayoutParams paramsUnit = null;
		if (mOrientation == LinearLayout.HORIZONTAL) {
			paramsUnit = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		} else {
			paramsUnit = new LayoutParams(LayoutParams.WRAP_CONTENT, 0);
		}

		paramsUnit.weight = 1;
		paramsUnit.leftMargin = (int) mUnitMarginLeft;
		paramsUnit.rightMargin = (int) mUnitMarginRight;
		paramsUnit.topMargin = (int) mUnitMarginTop;
		paramsUnit.bottomMargin = (int) mUnitMarginBottom;

		LayoutParams paramsImageView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsImageView.leftMargin = (int) mImageMarginLeft;
		paramsImageView.rightMargin = (int) mImageMarginRight;
		paramsImageView.topMargin = (int) mImageMarginTop;
		paramsImageView.bottomMargin = (int) mImageMarginBottom;

		LayoutParams paramsTextView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTextView.leftMargin = (int) mTextMarginLeft;
		paramsTextView.rightMargin = (int) mTextMarginRight;
		paramsTextView.topMargin = (int) mTextMarginTop;
		paramsTextView.bottomMargin = (int) mTextMarginBottom;

		for (int n = 0; n < mNum; n++) {

			LinearLayout ll = new LinearLayout(mContext);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);

			ImageView image = new ImageView(mContext);
			image.setBackgroundResource(mDrawableBackgroundRadio);
			image.setLayoutParams(paramsImageView);
			image.setTag(mTagImageView);

			TextView text = new TextView(mContext);
			text.setGravity(Gravity.CENTER);

			if (n < mTexts.length) text.setText(mTexts[n]);
			text.setLayoutParams(paramsTextView);
			text.setTag(mTagTextView);
			text.setTextSize(mTextSize);
			text.setTextColor(mTextColor);

			if (mOrder == ORDER_IMAGE_FIRST) {
				ll.addView(image);
				ll.addView(text);
			} else {
				ll.addView(text);
				ll.addView(image);
			}
			ll.setTag(n);
			ll.setOnClickListener(this);

			mImageViews.add(image);
			mTextViews.add(text);
			mContainer.addView(ll, paramsUnit);
		}
		mContainer.setWeightSum(mNum);
		setSelectedIndex(mSelectedIndex);
	}

	public void setRadioButtonNum(int num) {
		mNum = num;
	}

	public void setTextsRes(int textsRes) {
		mTextsRes = textsRes;
		mTexts = mContext.getResources().getStringArray(mTextsRes);
	}

	public void setTexts(String[] texts) {
		mTexts = texts;
	}

	public void setSelectedIndex(int selectedIndex) {
		if (selectedIndex >= 0 && selectedIndex < mNum) {
			refreshView(selectedIndex);
		} else {

		}
	}

	public int getSelectedIndex() {
		return mSelectedIndex;
	}

	@Override
	public void onClick(View v) {
		Integer index = (Integer) v.getTag();
		if (index != null) {
			refreshView(index);
		} else {
			throw new IllegalArgumentException("need to set a tag to LinearLayout element");
		}
	}

	private void refreshView(int selectedIndex) {
		mSelectedIndex = selectedIndex;
		LinearLayout clickedLL;
		ImageView image;
		for (int i = 0; i < mNum; i++) {
			clickedLL = this.findViewWithTag(i);
			image = clickedLL.findViewWithTag(mTagImageView);
			if (i == selectedIndex) {
				image.setBackgroundResource(mDrawableBackgroundRadioSelected);
			} else {
				image.setBackgroundResource(mDrawableBackgroundRadio);
			}
		}
	}
}