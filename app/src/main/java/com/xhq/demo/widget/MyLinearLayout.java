package com.xhq.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	private GestureDetector gestureDetector;
	private static boolean POPFLAG2;
	private static boolean POPFLAG3;


	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		Context context1 = context;
		gestureDetector = new GestureDetector(context,new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent event) {
				View rootView = getRootView();
				float eventX = event.getX();
				int width=rootView.getWidth()/getChildCount();
				if (eventX>width && eventX<2*width){
					POPFLAG2=true;
					POPFLAG3=true;
				}
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		View rootView = getRootView();
		int width=rootView.getWidth()/getChildCount();
		int height = rootView.getHeight();
		int count=getChildCount();
		
		gestureDetector.onTouchEvent(event);
		
		float eventX = event.getX();
		if (eventX<width){
			event.setLocation(width/2, event.getY());
			getChildAt(0).dispatchTouchEvent(event);
			return true;
		}else if (eventX>width && eventX<2*width){
			float eventY = event.getY();
			if (eventY<height/2){
				event.setLocation(width/2, event.getY());
				for (int i=0;i<count;i++){
					View child = getChildAt(i);
					child.dispatchTouchEvent(event);
				}
				return true;
			}else if (eventY>height/2){
				event.setLocation(width/2, event.getY());
				getChildAt(1).dispatchTouchEvent(event);
				return true;
			}
		}else if (eventX>2*width){
			event.setLocation(width/2, event.getY());
			getChildAt(2).dispatchTouchEvent(event);
			return true;
		}
		
		return true;
	}
	
	
	
}
