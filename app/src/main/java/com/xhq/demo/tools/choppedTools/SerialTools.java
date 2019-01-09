//package com.xhq.demo.tools.choppedTools;
//
//import android.os.Build;
//
//import com.mc.vending.data.VendingChnData;
//import com.mc.vending.db.VendingChnDbOper;
//import com.mc.vending.db.VendingDbOper;
//import com.mc.vending.tools.ConvertHelper;
//import com.mc.vending.tools.ProperTies;
//import com.zkh360.evm.commonlib.util.LogUtil;
//import com.zkh360.evm.hardwarelib.serial.config.HardwareConfig;
//import com.zkh360.evm.hardwarelib.serial.config.SerialConfig;
//import com.zkh360.evm.hardwarelib.serial.model.SerialModelFactory;
//import com.zkh360.evm.hardwarelib.serial.protocol.card.CardResponseListener;
//import com.zkh360.evm.hardwarelib.serial.protocol.card.ReadCardCentral;
//import com.zkh360.evm.hardwarelib.serial.protocol.keyboard.KeyboardContral;
//import com.zkh360.evm.hardwarelib.serial.protocol.keyboard.KeyboardResponseListener;
//import com.zkh360.evm.hardwarelib.serial.protocol.spring.SpringContral;
//import com.zkh360.evm.hardwarelib.serial.protocol.spring.SpringResponseListener;
//import com.zkh360.evm.hardwarelib.serial.protocol.store.StoreContral;
//import com.zkh360.evm.hardwarelib.serial.protocol.store.StoreResponseListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SerialTools{
//
//    public static final String FUNCTION_KEY_COMBINATION = "组合";
//    public static final String FUNCTION_KEY_BORROW = "借出";
//    public static final String FUNCTION_KEY_BACK = "还入";
//    public static final String FUNCTION_KEY_SET = "设置";
//    public static final String FUNCTION_KEY_CANCEL = "取消";
//    public static final String FUNCTION_KEY_CONFIRM = "输入";
//
//    //INBOX是老的工控机型号,新工控机型号是SABRES,新工控机又分两批,开始的一批是4.4.2版本后面的是4.2.2,端口号都有区别
//    private final static String[] SerialToolsPortName = Build.MODEL.contains("INBOX") ?
//            new String[]{"/dev/ttyO2", "/dev/ttyO3", "/dev/ttyO6", "/dev/ttyO7", "/dev/ttyO4"} :
//            Build.VERSION.SDK_INT == 17 ? new String[]{"/dev/ttymxc1", "/dev/ttymxc2", "/dev/ttymxc3", "/dev/ttymxc4", "dev/ttymxc0"} :
//                    new String[]{"/dev/ttymxc0", "/dev/ttymxc1", "/dev/ttymxc2", "/dev/ttymxc3", "dev/ttymxc4"};
//
//    private SerialTools() {
//    }
//
//    private static class InstanceHolder {
//        private static final SerialTools sInstance = new SerialTools();
//    }
//
//    public static SerialTools getInstance() {
//        return SerialTools.InstanceHolder.sInstance;
//    }
//
//    private final VendingDbOper vendingDbOper = new VendingDbOper();
//
//    public void initPortName() {
//        SerialModelFactory.setConfig(new SerialConfig() {
//            @Override
//            public HardwareConfig getReadCardConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[2]).baudRate(19200).build();
//            }
//
//            @Override
//            public HardwareConfig getReadIDCardConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[2]).baudRate(9600).build();
//            }
//
//            @Override
//            public HardwareConfig getReadCusCardConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[2]).baudRate(57600).build();
//            }
//
//            @Override
//            public HardwareConfig getKeyBoardConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[3]).baudRate(9600).build();
//            }
//
//            @Override
//            public HardwareConfig getYLSpringConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[0]).baudRate(9600).build();
//            }
//
//            @Override
//            public HardwareConfig getSpringConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[0]).baudRate(38400).build();
//            }
//
//            @Override
//            public HardwareConfig getStoreConfig() {
//                if (ProperTies.getJPCellPosition().size() > 0) {
//                    return new HardwareConfig.Builder().portName(SerialToolsPortName[4]).baudRate(38400).build();
//                } else {
//                    return new HardwareConfig.Builder().portName(SerialToolsPortName[1]).baudRate(38400).build();
//                }
//            }
//
//            @Override
//            public HardwareConfig getKTStoreConfig() {
//                if (ProperTies.getJPCellPosition().size() > 0) {
//                    return new HardwareConfig.Builder().portName(SerialToolsPortName[4]).baudRate(115200).build();
//                } else {
//                    return new HardwareConfig.Builder().portName(SerialToolsPortName[1]).baudRate(115200).build();
//                }
//            }
//
//            @Override
//            public HardwareConfig getJPStoreConfig() {
//                if (ProperTies.getJPCellPosition().size() > 0) {
//                    return new HardwareConfig.Builder().portName(SerialToolsPortName[1]).baudRate(9600).build();
//                } else {
//                    return new HardwareConfig.Builder().portName(SerialToolsPortName[4]).baudRate(9600).build();
//                }
//            }
//
//            @Override
//            public HardwareConfig getLlrConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[3]).baudRate(9600).build();
//            }
//
//            @Override
//            public HardwareConfig getEtagsConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[3]).baudRate(115200).build();
//            }
//
//            @Override
//            public HardwareConfig getRfidConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[1]).baudRate(115200).build();
//            }
//
//            @Override
//            public HardwareConfig getUpsConfig() {
//                return new HardwareConfig.Builder().portName(SerialToolsPortName[0]).baudRate(2400).build();
//            }
//        });
//    }
//
//    public void openKeyBoard(KeyboardResponseListener listener) {
//        KeyboardContral.getInstance().openKeyBoard(listener);
//    }
//
//    public void closeKeyBoard() {
//        KeyboardContral.getInstance().closeKeyBoard();
//    }
//
//    public void openRFID(CardResponseListener cardResponseListener) {
//        if (!ProperTies.isNewCardReader()) {
//            String cardtype = vendingDbOper.getVending().getVd1CardType();
//            if ("1".equals(cardtype)) {
//                ReadCardCentral.getInstance().startRead(cardResponseListener);
//            } else {
//                ReadCardCentral.getInstance().startRead(cardResponseListener, ReadCardCentral.CardReaderType.ID);
//            }
//        } else {
//            ReadCardCentral.getInstance().startRead(cardResponseListener, ReadCardCentral.CardReaderType.CUSIC);
//        }
//    }
//
//    public void closeRFID() {
//        ReadCardCentral.getInstance().stopRead();
//    }
//
//
//    public void openStore(VendingChnData vendingChn, StoreResponseListener listener) {
//        if (ProperTies.getJPCellPosition().contains(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0))) {
//            StoreContral.getInstance().openJPStore(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
//                    ConvertHelper.toInt(vendingChn.getVc1Height(), 0), listener);
//        } else if (ProperTies.getKTCellPosition().contains(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0))) {
//            StoreContral.getInstance().openKTStore(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
//                    ConvertHelper.toInt(vendingChn.getVc1Height(), 0), listener);
//        } else {
//            StoreContral.getInstance().openStore(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
//                    ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
//                    ConvertHelper.toInt(vendingChn.getVc1Height(), 0), listener);
//        }
//    }
//
//
//    public void checkStore(int deviceNo, StoreResponseListener storeResponseListener) {
//        if (ProperTies.getKTCellPosition().contains(deviceNo)) {
//            //坤同格子机可能存在多种不同的门数,所以检查门锁返回值不固定,要把当前柜子的门数传进去
//            int cellCount = new VendingChnDbOper().findCellCount(deviceNo);
//            StoreContral.getInstance().checkKTStore(deviceNo, cellCount, storeResponseListener);
//        } else {
//            StoreContral.getInstance().checkStore(2, deviceNo, 1, storeResponseListener);
//        }
//    }
//
//    public void openSpring(VendingChnData vendingChn, SpringResponseListener listener) {
//        if (!ProperTies.isJiaShanSpring()) {
//            SpringContral.getInstance().openYLVender(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
//                    ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0), listener);
//        } else {
//            SpringContral.getInstance().openVender(1, ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
//                    ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0) + 1, vendingChn.getInputQty(), listener);
//        }
//    }
//
//    public void checkRasterCount(SpringResponseListener springListener) {
//        SpringContral.getInstance().checkRasterCount(springListener);
//    }
//
//    public void clearRasterCount() {
//        SpringContral.getInstance().clearRasterCount();
//    }
//
//    public interface OpenAllStoreListener {
//        void onOpenStart();
//
//        void onOpenFinish(String msg);
//    }
//
//    private class StoreListener implements StoreResponseListener {
//        private int count;
//
//        private final int maxCount;
//
//        private final OpenAllStoreListener listener;
//
//        StoreListener(OpenAllStoreListener listener, int maxCount) {
//            this.listener = listener;
//            this.maxCount = maxCount;
//        }
//
//        @Override
//        public void onOpenSuccess(String result) {
//            onFinish();
//        }
//
//        @Override
//        public void onFailed(String s) {
//            onFinish();
//        }
//
//        private void onFinish() {
//            count++;
//            if (count == maxCount && listener != null) {
//                listener.onOpenFinish("打开全部格子机完成");
//            }
//        }
//
//        @Override
//        public boolean isValid() {
//            return true;
//        }
//    }
//
//    public void openAllStore(OpenAllStoreListener listener) {
//        LogUtil.i("准备开始打开所有格子机");
//        List<VendingChnData> vendingChns = new VendingChnDbOper().findAll();
//        ArrayList<VendingChnData> tempList = new ArrayList();
//        for (VendingChnData data : vendingChns) {
//            if (VendingChnData.VENDINGCHN_TYPE_CELL.equals(data.getVc1Type())) {
//                tempList.add(data);
//            }
//        }
//        if (tempList.size() == 0) {
//            listener.onOpenFinish("没有格子机！");
//            return;
//        }
//        if (listener != null) {
//            listener.onOpenStart();
//        }
//        StoreListener storeListener = new StoreListener(listener, tempList.size());
//        for (VendingChnData vendingChn : tempList) {
//            if (ProperTies.getJPCellPosition().contains(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0))) {
//                StoreContral.getInstance().openJPStore(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
//                        ConvertHelper.toInt(vendingChn.getVc1Height(), 0), storeListener);
//            } else if (ProperTies.getKTCellPosition().contains(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0))) {
//                StoreContral.getInstance().openKTStore(ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
//                        ConvertHelper.toInt(vendingChn.getVc1Height(), 0), storeListener);
//            } else {
//                StoreContral.getInstance().openStoreWithNoCheck(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
//                        ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
//                        ConvertHelper.toInt(vendingChn.getVc1Height(), 0), storeListener);
//            }
//        }
//    }
//}
