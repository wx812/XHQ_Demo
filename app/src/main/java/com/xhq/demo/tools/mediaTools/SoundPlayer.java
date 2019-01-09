package com.xhq.demo.tools.mediaTools;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.xhq.demo.R;
import com.xhq.demo.tools.appTools.AppUtils;


/**
 * @author ssy
 * @date: 2017/8/16
 */
public class SoundPlayer implements SoundPool.OnLoadCompleteListener{

    private SoundPool sp;


    private SoundPlayer(){
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1){
    }


    enum PlayType{
        ORDER_NOT_EXISTS,           // 播放 order_not_exists
        ORDER_DETAIL_ERROR,         // 播放 order_detail_error
        ORDER_DETAIL_NULL,          // 播放 order_detail_null
        STOCK_ERROR,                // 播放 stock_error
        PICK_FINISH,                // 播放 pick_finish
        ELECTRIC_ERROR,             // 播放 electric_error
        FOUND_ORDER,                // 播放 found_order
        BACK_PICK,                  // 返回领料页面
        LOCK_OPENED                 // 返回领料页面
    }

    private static class InstanceHolder{
        private static SoundPlayer sInstance = new SoundPlayer();
    }


    public static SoundPlayer getInstance(){
        return InstanceHolder.sInstance;
    }


    private void createSoundBuilder(){
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
    }


    /**
     * 工单编号不存在，请确认工单状态。
     */
    public void playOrderNotExists(){
        play(PlayType.ORDER_NOT_EXISTS);
    }


    /**
     * 查询工单明细发生异常，请确认工单状态。
     */
    public void playOrderDetailError(){
        play(PlayType.ORDER_DETAIL_ERROR);
    }


    /**
     * 领料单产品为空，请确认工单状态。
     */
    public void playOrderDetailNull(){
        play(PlayType.ORDER_DETAIL_NULL);
    }


    /**
     * 库存数量不足，不能领料，请重新下单！
     */
    public void playStockError(){
        play(PlayType.STOCK_ERROR);
    }


    /**
     * 领料完成，请将标签拆下并投入标签盒
     */
    public void playPickFinish(){
        play(PlayType.PICK_FINISH);
    }


    /**
     * 返回领料页面
     */
    public void playBackPickActivity(){
        play(PlayType.BACK_PICK);
    }


    /**
     * 供电异常，请稍后再来领料
     */
    public void playElectricError(){
        play(PlayType.ELECTRIC_ERROR);
    }


    /**
     * 找到订单，门已打开
     */
    public void playFoundOrder(){
        play(PlayType.FOUND_ORDER);
    }


    /**
     * 门锁已打开
     */
    public void playLockOpened(){
        play(PlayType.LOCK_OPENED);
    }


    private void play(PlayType type){
        if(sp == null){
            createSoundBuilder();
        }
        int id = -1;

        Context applicationContext = AppUtils.getAppContext();

        switch(type){

            case ORDER_NOT_EXISTS:
                id = sp.load(applicationContext, R.raw.order_not_exists, 1);
                break;

            case ORDER_DETAIL_ERROR:
                id = sp.load(applicationContext, R.raw.order_detail_error, 1);
                break;

            case ORDER_DETAIL_NULL:
                id = sp.load(applicationContext, R.raw.order_detail_null, 1);
                break;

            case STOCK_ERROR:
                id = sp.load(applicationContext, R.raw.stock_error, 1);
                break;

            case PICK_FINISH:
                id = sp.load(applicationContext, R.raw.pick_finish, 1);
                break;

            case ELECTRIC_ERROR:
                id = sp.load(applicationContext, R.raw.electric_error, 1);
                break;

            case FOUND_ORDER:
                id = sp.load(applicationContext, R.raw.found_order, 1);
                break;

            case BACK_PICK:
                id = sp.load(applicationContext, R.raw.scan_suc_return_pick, 1);
                break;

            case LOCK_OPENED:
                id = sp.load(applicationContext, R.raw.lock_opened, 1);
                break;

            default:
                break;
        }
        try{
            //预留100ms准备时间
            Thread.sleep(100);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        sp.play(id, 1, 1, 0, 0, 1);
    }
}
