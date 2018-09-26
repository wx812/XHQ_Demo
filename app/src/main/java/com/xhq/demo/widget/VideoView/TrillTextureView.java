package com.xhq.demo.widget.VideoView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JZResizeTextureView;

/**
 * Author: YuJunKui
 * Time:2018/1/31 9:41
 * Tips:
 */

public class TrillTextureView extends JZResizeTextureView{

    public TrillTextureView(Context context) {
        super(context);
    }

    public TrillTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int[] size = measure4VideoSize(this, currentVideoWidth, currentVideoHeight,
                                       widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(size[0], size[1]);
    }


    public static int[] measure4VideoSize(View view, int videoWidth, int videoHeight, int widthMeasureSpec, int heightMeasureSpec) {
        //蛮横的写成宽度拉到屏幕宽度
        int viewRotation = (int) view.getRotation();

        // 如果判断成立，则说明显示的TextureView和本身的位置是有90度的旋转的，所以需要交换宽高参数。
        if (viewRotation == 90 || viewRotation == 270) {
            int tempMeasureSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempMeasureSpec;
        }

        int width, height, tempHeight;

        //取view实际高度
        int viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        if (videoWidth > 0 && videoHeight > 0) {

            //假定宽度拉到控件宽度  高度按比例计算
            tempHeight = (int) (viewWidth / (videoWidth * 1f / videoHeight));

            //计算后倘若 控件高度>视频高度&&控件高度-视频高度<y160
            int dimen = 0;
//            dimen = ScreenUtils.getDimenPx(R.dimen.y160);
            if (viewHeight > tempHeight && viewHeight - tempHeight < dimen) {
                //则高度拉到控件高度  宽度按比例计算
                height = viewHeight;
                width = (int) (viewHeight / (videoHeight * 1f / videoWidth));

            } else {
                //使用宽度拉到控件高度
                width = viewWidth;
                height = tempHeight;
            }

        } else {
            //无视频宽度下
            width = viewWidth;
            height = viewHeight;
        }

        return new int[]{width, height};
    }


}
