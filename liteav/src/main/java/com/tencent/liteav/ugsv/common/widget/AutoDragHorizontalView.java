package com.tencent.liteav.ugsv.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;


/**
 * the parent layout width can't for wrap_content
 * <p>
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/4/1.
 *     Desc  : Auto drag Horizontal view.
 *     Updt  : Description.
 * </pre>
 */
public class AutoDragHorizontalView extends RecyclerView{


    private int mDisplayItemCount = 3;                          // the number of items that can be displayed, and odd number

    private int mDefSelectPosition = 0;                         // the position of the selected by default;

    private int itemScrollX;                                    // item scroll x value
    private WrapperRCVAdapter mWrapAdapter;
    private Adapter dataAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private boolean isInit;
    private boolean isFirstPositionChanged = true;              // first init, whether call the mItemSelectedListener

    private int mLastSelectedPosition = mDefSelectPosition;          // record the position of the selected last time
    private int mCurrentSelectPosition = mDefSelectPosition;    // the currently selected position

    private Scroller mScroller;

    private int mLastMoveX;                                     // record the position of the moved distance when call the moveToPosition()

    private boolean isMoveFinished = true;


    public AutoDragHorizontalView(Context context){
        this(context, null);
    }


    public AutoDragHorizontalView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }


    public AutoDragHorizontalView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mScroller = new Scroller(getContext());
        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(isInit){
                if(mDefSelectPosition >= dataAdapter.getItemCount()){
                    mDefSelectPosition = dataAdapter.getItemCount() - 1;
                }

                if(isFirstPositionChanged){
                    getItemSelectedOnChanged(mDefSelectPosition);
                    getItemClickChanged(mDefSelectPosition);
                }

                // set the item of offset when item scroll to the position specified
                mLinearLayoutManager.scrollToPositionWithOffset(0, -mDefSelectPosition * (mWrapAdapter.getItemWidth()));
                isInit = false;
            }
        });
    }


    @Override
    public void computeScroll(){
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            int x = mScroller.getCurrX() - mLastMoveX;
            mLastMoveX += x;
            scrollBy(x, 0);
        }else if(mScroller.isFinished()){
            if(isMoveFinished){
                return;
            }

            // refresh last selected position and current select position
            mWrapAdapter.notifyItemChanged(mLastSelectedPosition + 1);
            mWrapAdapter.notifyItemChanged(mCurrentSelectPosition + 1);
            mLastSelectedPosition = mCurrentSelectPosition;
            getItemSelectedOnChanged(mCurrentSelectPosition);
            getItemClickChanged(mCurrentSelectPosition);
            isMoveFinished = true;
        }
    }


    @Override
    public void onScrolled(int dx, int dy){
        super.onScrolled(dx, dy);
        itemScrollX += dx;
//        KLog.e("xhq, itemScrollX : value - ", itemScrollX);
        calculateSelectedPosition();
    }


    private void calculateSelectedPosition(){
        int itemWidth = mWrapAdapter.getItemWidth();
        if(itemScrollX > 0){
            mCurrentSelectPosition = (itemScrollX) / itemWidth + mDefSelectPosition;
        }else{
            mCurrentSelectPosition = mDefSelectPosition + (itemScrollX) / itemWidth;
        }
    }


    @Override
    public void onScrollStateChanged(int state){
        super.onScrollStateChanged(state);

        if(state == RecyclerView.SCROLL_STATE_IDLE){
            if(mWrapAdapter == null) return;

            int itemWidth = mWrapAdapter.getItemWidth();
            int headerFooterWidth = mWrapAdapter.getHeadTailWidth();

            if(itemWidth == 0 || headerFooterWidth == 0) return;

            // Beyond the previous item
            int overLastPosOffset = itemScrollX % itemWidth;
            if(overLastPosOffset == 0){
                //Just in an item selected location without sliding offset correction

            }else if(Math.abs(overLastPosOffset) <= itemWidth / 2){

                scrollBy(-overLastPosOffset, 0);
            }else if(overLastPosOffset > 0){

                scrollBy((itemWidth - overLastPosOffset), 0);
            }else{

                scrollBy(-(itemWidth + overLastPosOffset), 0);
            }
            calculateSelectedPosition();

            mWrapAdapter.notifyItemChanged(mLastSelectedPosition + 1);
            mWrapAdapter.notifyItemChanged(mCurrentSelectPosition + 1);
            mLastSelectedPosition = mCurrentSelectPosition;

            getItemSelectedOnChanged(mCurrentSelectPosition);
            getItemClickChanged(mCurrentSelectPosition);
        }
    }


    /**
     * init default position
     * <p>
     * This method should be called before {@link AutoDragHorizontalView#setAdapter(android.support.v7.widget.RecyclerView.Adapter)}
     *
     * @param defSelectPosition init default position, when it more than items, select position of in the end
     */
    public void setDefSelectPosition(int defSelectPosition){
        if(dataAdapter != null){
            throw new RuntimeException("This method should be called before setAdapter()!");
        }
        this.mDefSelectPosition = defSelectPosition;
        mCurrentSelectPosition = defSelectPosition;
        mLastSelectedPosition = defSelectPosition;
    }


    /**
     * get selected position when WrapAdapter data changed
     *
     * @param position position
     */
    private void getItemSelectedOnChanged(int position){
        if(mItemSelectedListener != null){
            mItemSelectedListener.onItemSelectedChanged(position);
        }
    }


    private void getItemClickChanged(int position){
        if(mItemClickListener != null){
            mItemClickListener.onItemClick(mWrapAdapter.getItemView(), position);
        }
    }


    @Override
    public void setLayoutManager(LayoutManager layout){
        if(!(layout instanceof LinearLayoutManager)){
            throw new IllegalStateException("The LayoutManager here must be LinearLayoutManager!");
        }
        this.mLinearLayoutManager = (LinearLayoutManager)layout;
    }


    @Override
    public void setAdapter(final Adapter adapter){
        this.dataAdapter = adapter;
        this.mWrapAdapter = new WrapperRCVAdapter(adapter, getContext(), mDisplayItemCount);
        adapter.registerAdapterDataObserver(new AdapterDataObserver(){

            @Override
            public void onChanged(){
                super.onChanged();
                mWrapAdapter.notifyDataSetChanged();
                getItemSelectedOnChanged(mCurrentSelectPosition);
                getItemClickChanged(mCurrentSelectPosition);
            }
        });

        itemScrollX = 0;
        if(mLinearLayoutManager == null){
            mLinearLayoutManager = new LinearLayoutManager(getContext());
        }
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        super.setLayoutManager(mLinearLayoutManager);
        super.setAdapter(this.mWrapAdapter);
        isInit = true;
    }

//========================================================================================================================


    /**
     * wrap the Adapter, set  head item, and tail item, and set the width of th item
     */
    class WrapperRCVAdapter extends RecyclerView.Adapter implements OnClickListener{

        private static final int HEAD_TAIL_TYPE = -1;
        private Context context;
        private View itemView;
        private RecyclerView.Adapter dataAdapter;
        private int itemCount;
        private int headTailWidth;  // head and tail width set, the item
        private int itemWidth;
        private ValueAnimator valueAnimator;
        private float mAnimatorValue;
        private OnItemClickListener mOnItemClickListener;


        public WrapperRCVAdapter(Adapter dataAdapter, Context context, int itemCount){
            this.dataAdapter = dataAdapter;
            this.context = context;
            this.itemCount = itemCount;
            this.mOnItemClickListener = getOnItemClickListener();

            if(dataAdapter instanceof IAutoLocateHorizontalView){
                itemView = ((IAutoLocateHorizontalView)dataAdapter).getItemView();
            }else{
                throw new RuntimeException(
                        dataAdapter.getClass().getSimpleName() + " should implements AutoLocateHorizontalView.IAutoLocateHorizontalView !");
            }
        }


        @Override
        public int getItemViewType(int position){
            if(position == 0 || position == getItemCount() - 1){
                return HEAD_TAIL_TYPE;
            }
            return dataAdapter.getItemViewType(position - 1);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            // item width
            int width = parent.getWidth() / itemCount - dpToPx(30);

            // head and tail view set
            if(viewType == HEAD_TAIL_TYPE){
                View view = new View(context);
//                view.setOnClickListener(this);

                int parentHalfWidth = parent.getWidth() / 2;
                headTailWidth = parentHalfWidth - width / 2;
                RecyclerView.LayoutParams params = new LayoutParams(headTailWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                return new HeadTailViewHolder(view);
            }

            //  other view set
            ViewHolder holder = dataAdapter.onCreateViewHolder(parent, viewType);
            itemView = ((IAutoLocateHorizontalView)dataAdapter).getItemView();
            itemView.setOnClickListener(this);
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if(params != null){
                params.width = width;
                itemWidth = width;
                itemView.setLayoutParams(params);
            }
            return holder;
        }


        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            holder.itemView.setTag(position);

            if(!isHeadOrTail(position)){
                dataAdapter.onBindViewHolder(holder, position - 1);
                if(mCurrentSelectPosition == position - 1){
                    ((IAutoLocateHorizontalView)dataAdapter).onItemSelected(true, position - 1, holder, itemWidth);
                }else{
                    ((IAutoLocateHorizontalView)dataAdapter).onItemSelected(false, position - 1, holder, itemWidth);
                }
            }

//            valueAnimator = ValueAnimator.ofFloat(mWrapAdapter.getItemWidth() *mLastSelectedPosition, mWrapAdapter.getItemWidth() *mCurrentSelectPosition);
//            valueAnimator.setDuration(1000);
//            valueAnimator.setRepeatCount(1);
        }


        @Override
        public int getItemCount(){
            return dataAdapter.getItemCount() + 2;
        }


        private boolean isHeadOrTail(int position){
            return position == 0 || position == getItemCount() - 1;
        }


        public int getHeadTailWidth(){
            return headTailWidth;
        }


        public int getItemWidth(){
            return itemWidth;
        }


        public View getItemView(){
            return itemView;
        }


        @Override
        public void onClick(View view){
            if(mOnItemClickListener != null){

                Integer position = (Integer)view.getTag();
                if(position == mCurrentSelectPosition){
                    // click myself
                }else{
                    if(position > mCurrentSelectPosition){
                        scrollBy(mWrapAdapter.getItemWidth() * (position - mCurrentSelectPosition), 0);

//                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation){
//
//                                mAnimatorValue = (float)valueAnimator.getAnimatedValue();
//                                scrollBy((int)mAnimatorValue, 0);
//                            }
//                        });
//                        valueAnimator.start();

                    }else{
                        scrollBy(-(mWrapAdapter.getItemWidth() * (-position + mCurrentSelectPosition)), 0);
//                        scrollBy(-(int)mAnimatorValue, 0);

//                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation){
//
//                                mAnimatorValue = (float)valueAnimator.getAnimatedValue();
//                                scrollBy(-(int)mAnimatorValue, 0);
//                            }
//                        });
//                        valueAnimator.start();
                    }

                    calculateSelectedPosition();

                    mWrapAdapter.notifyItemChanged(mLastSelectedPosition + 1);
                    mWrapAdapter.notifyItemChanged(mCurrentSelectPosition + 1);

                }
                mCurrentSelectPosition = position;
                mLastSelectedPosition = mCurrentSelectPosition;

                mOnItemClickListener.onItemClick(view, position);
            }
        }


        class HeadTailViewHolder extends RecyclerView.ViewHolder{
            HeadTailViewHolder(View itemView){
                super(itemView);
            }
        }
    }

//    ===================================Interface==============================================


    /**
     * recall interface when item selected
     */
    public interface IAutoLocateHorizontalView{

        View getItemView();

        void onItemSelected(boolean isSelected, int position, ViewHolder holder, int itemWidth);
    }


    private OnItemSelectedChangedListener mItemSelectedListener;

    public interface OnItemSelectedChangedListener{
        void onItemSelectedChanged(int position);
    }


    public void setOnSelectedPositionChangedListener(OnItemSelectedChangedListener listener){
        this.mItemSelectedListener = listener;
    }


    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


    public OnItemClickListener getOnItemClickListener(){
        return mItemClickListener;
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }


    private int dpToPx(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
