package com.xhq.demo.tools;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xhq.demo.R;
import com.xhq.demo.activity.MainActivity;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.appTools.DeviceUtils;
import com.xhq.demo.tools.uiTools.DialogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Utils{

    // location  Permissions
    public static final int REQUEST_LOCATION = 2;
    public static final int REQUEST_PERMISSIONS = 1;

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
    };
    //百度定位需如下权限
    public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    /**
     * traverse object field to map
     *
     * Each attribute must correspond to a get and set method;
     *
     * @param obj object
     * @return object field to map
     */
    public static Map<String, Object> traverseObjField(Object obj){

        Map<String, Object> fieldMap = new ArrayMap<>();

        try{
            Field[] field = obj.getClass().getDeclaredFields();
            for(Field aField : field){
                String keyName = aField.getName();

                String name = keyName.substring(0, 1).toUpperCase() + keyName.substring(1);  // 将属性的首字符大写，方便构造get，set方法

                String type = aField.getGenericType().toString();       // 获取属性的类型

                Method m = obj.getClass().getMethod("get" + name); // 获取field值的对应方法

                switch(type){
//            System.out.println("属性为：" + name);   // 如果type是类类型，则前面包含"class "，后面跟类名
                    case "class java.lang.String":
                        String valueStr = (String)m.invoke(obj);       // 调用getter方法获取属性值
                        fieldMap.put(keyName, valueStr);
                        break;

                    case "class java.lang.Integer":
                        Integer valueInt = (Integer)m.invoke(obj);
                        fieldMap.put(keyName, valueInt);
                        break;

                    case "class java.lang.Short":
                        Short valueShort = (Short)m.invoke(obj);
                        fieldMap.put(keyName, valueShort);
                        break;

                    case "class java.lang.Double":
                        Double valueDouble = (Double)m.invoke(obj);
                        fieldMap.put(keyName, valueDouble);
                        break;

                    case "class java.lang.Boolean":
                        Boolean valueBoolean = (Boolean)m.invoke(obj);
                        fieldMap.put(keyName, valueBoolean);
                        break;

                    case "class java.lang.Date":
                        Date valueDate = (Date)m.invoke(obj);
                        fieldMap.put(keyName, valueDate);
                        break;
                }
            }
        }catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException e){
            e.printStackTrace();
        }

        return fieldMap;
    }


    /**
     * description @see {@link #traverseObjField(Object)}
     *
     * @param obj {@link #traverseObjField(Object)}
     * @return {@link #traverseObjField(Object)}
     */
    public static Map<String, String> traverseObjStrField(Object obj){

//        Map<String, String> fieldMap = new ArrayMap<>();
        Map<String, String> fieldMap = new HashMap<>();

        try{
            Field[] field = obj.getClass().getDeclaredFields();
            for(Field aField : field){
                String keyName = aField.getName();

                String name = keyName.substring(0, 1).toUpperCase() + keyName.substring(1);  // 将属性的首字符大写，方便构造get，set方法

                String type = aField.getGenericType().toString();       // 获取属性的类型

                Method m = obj.getClass().getMethod("get" + name); // 获取field值的对应方法

                switch(type){
//            System.out.println("属性为：" + name);   // 如果type是类类型，则前面包含"class "，后面跟类名
                    case "class java.lang.String":
                        String valueStr = (String)m.invoke(obj);       // 调用getter方法获取属性值
                        fieldMap.put(keyName, valueStr);
                        break;
                }
            }
        }catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            e.printStackTrace();
        }

        return fieldMap;
    }


    //暂停
    private static void startPause(long time){
        while(true) if(System.currentTimeMillis() > time) break;
    }


    public static String avoidGsonError(Map<String, Object> map) {
        //new Gson().toJson(http://www.baidu.com/id=1); 结果 --> “http://www.baidu.com/id\u003d1 ”,
        // 避免"=" 被解析成 "\u003d" 的Unicode 码
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String str = gson.toJson(map);
        return str + "\\n";
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param ctx
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context ctx, String serviceName) {
        boolean isWork = false;
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = am.getRunningServices(40);
        if (myList.size() <= 0) return false;
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            //LogUtil.i("servie list name:"+mName);
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


    /**
     * 6.0 以上版本 检测是否有定位权限  没有提示用户开启权限
     *
     * @param context
     */

    public static void CheckLocationPermission(final Context context) {
        //android 6.0
        if (DeviceUtils.getSDK_VCode() >= Build.VERSION_CODES.M) {
            int permission_fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            int permission_coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permission_coarse != PackageManager.PERMISSION_GRANTED || permission_fine != PackageManager.PERMISSION_GRANTED) {
                //第一次拒绝权限后弹出提示  有不再提醒按钮
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                                                                         Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    DialogUtils.showMessageOKCancel(context, "居家app需要定位功能，请允许",
                                                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context,
                                                                      new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                                                      REQUEST_LOCATION);
                                }
                            });
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("请允许获取位置信息")
                            .setMessage("由于居家无法获取位置信息的权限，不能正常运行，请开启权限后再使用。\n设置路径：系统设置->居家->权限")
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                                    ((Activity) context).startActivityForResult(intent, 1);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity) context).finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        }
    }

    public static void requestPermission(final Context context) {
        ActivityCompat.requestPermissions((Activity) context,
                                          PERMISSIONS_STORAGE,
                                          REQUEST_PERMISSIONS);
    }

    public static void requestLocPermission(final Context context) {
        ActivityCompat.requestPermissions((Activity) context,
                                          PERMISSIONS_LOCATION,
                                          REQUEST_LOCATION);
    }


    //android 6.0 对于dangous 权限需要在使用的时候请求权限
    public static void CheckWriteExternalStoragePermission(Activity activity){
        final String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if(DeviceUtils.getSDK_VCode() >= Build.VERSION_CODES.M){
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity,
                                                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED){
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
            }
        }
    }


    /**
     * android 6.0及以上系统 对于dangerous 权限需要在使用前单独请求权限
     */

    public static void CheckPermission(Activity mActivity) {
        if (DeviceUtils.getSDK_VCode() >= Build.VERSION_CODES.M) {
            // Check if we have write /camera permission
            int permission = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCamera = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
            if (permission != PackageManager.PERMISSION_GRANTED || permissionCamera != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        mActivity,
                        PERMISSIONS_STORAGE,
                        REQUEST_PERMISSIONS
                                                 );
            }
        }
    }

    // 检查是否拥有使用权
    public boolean isGranted(String permission) {
        Context ctx = AppUtils.getAppContext();
        return !isHigh60System() || isGranted(ctx, permission);
    }


    // 是否申请了该使用权限
    // ContextCompat.checkSelfPermission，主要用于检测某个权限是否已经被授予，
    // 方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED。
    // 当返回DENIED就需要进行申请授权了。
    private boolean isGranted(Context ctx, String permission) {
        int checkSelfPermission = ActivityCompat.checkSelfPermission(ctx, permission);
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
    }


    // 判断是否是Android 6.0以上
    private boolean isHigh60System() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }



    // 申请使用权限
    // shouldShowRequestPermissionRationale主要用于给用户一个申请权限的解释，
    // 该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，
    // 你需要给用户一个解释，为什么要授权，则使用该方法。requestCode这个需要在处理的回调的时候 一一对应的。
    private void requestPermission(Activity act, String permission, int requestCode) {
        if (!isGranted(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(act, permission)) {

            } else {
                ActivityCompat.requestPermissions(act, new String[]{permission}, requestCode);
            }
        } else {
            //直接执行相应操作了
        }
    }


//    // 授权回调
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == CAMERA) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                String jpgPath = getCacheDir() + "test.jpg";
//                takePhotoByPath(jpgPath, 2);
//            } else {
//                // Permission Denied
//                Toast.makeText(MainActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }







    /**
     * 百度定位需要多权限
     * 1、获取手机状态：
     * Manifest.permission.READ_PHONE_STATE
     * <p>
     * 2、获取位置信息：
     * Manifest.permission.ACCESS_COARSE_LOCATION
     * Manifest.permission.ACCESS_FINE_LOCATION
     * <p>
     * 3、读写SD卡：
     * Manifest.permission.READ_EXTERNAL_STORAGE
     * Manifest.permission.WRITE_EXTERNAL_STORAGE
     *
     * @param context
     * @return
     */
    public static boolean hasLocPermission(Context context) {
        if (DeviceUtils.getSDK_VCode() >= Build.VERSION_CODES.M) {
            int permission_fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            int permission_coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
/*		int permission_phone_state = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
		int permission_write = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int permission_read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);*/
            return (permission_coarse == PackageManager.PERMISSION_GRANTED && permission_fine == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }
    }

    public static boolean hasPermission(Context context) {
        if (DeviceUtils.getSDK_VCode() >= Build.VERSION_CODES.M) {
            int permission_write = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permission_camera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            int permission_state = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

            return (permission_write == PackageManager.PERMISSION_GRANTED
                    && permission_camera == PackageManager.PERMISSION_GRANTED
                    && permission_state == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }
    }


    //查询权限
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (ContextCompat.checkSelfPermission(activity, value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }




//	public static void showNetworkTips(final Context context) {
//		if(!netTips){
//			netTips = true;
//			new ObdDialog(context, false, true, ObdDialog.TWO_BUTTON)
//					.setMessage("网络不给力，请检查网络设置")
//					.setIcon(R.mipmap.icon_warn)
//					.setMessageColor(R.color.text_color_black)
//					.setLeftBtnColor(R.color.text_color_black)
//					.setPositiveButton(context.getString(R.string.general_set_network), new ObdDialog.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							netTips = false;
//							try {
//								Intent intent;
//								//判断手机系统的版本  即API大于10 就是3.0或以上版本
//
//								if (Build.MANUFACTURER.equals("Meizu")) {
//									intent = new Intent();
//									ComponentName component = new ComponentName("com.android.settings", "com.android.settings.Settings");
//									intent.setComponent(component);
//									intent.setAction("android.intent.action.VIEW");
//								} else if (Build.VERSION.SDK_INT >= 16) {
//									intent = new Intent(Settings.ACTION_SETTINGS);
//								} else {
//									intent = new Intent();
//									ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
//									intent.setComponent(component);
//									intent.setAction("android.intent.action.VIEW");
//								}
//								context.startActivity(intent);
//							} catch (Exception e) {
//								e.printStackTrace();
//								ToastUtils.showToast(context, R.string.tips_net_manual_set);
//							}
//						}
//					})
//					.setNegativeButton(context.getString(R.string.general_cancel), new ObdDialog.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							netTips = false;
//						}
//					}).show();
//		}
//
//	}

//	public static void showRealVerifyTips(final Context context, @NonNull String msg) {
//		if (!realTips) {
//
//			realTips = true;
//			ObdDialog realDialog = new ObdDialog(context, false, false, ObdDialog.TWO_BUTTON)
////					.setTitle(msg)
//					.setMessage(msg)
////				.setIcon(R.mipmap.icon_warn)
//					.setMessageColor(R.color.text_color_black)
//					.setLeftBtnColor(R.color.text_color_black)
//					.setPositiveButton(context.getString(R.string.go_real_verify), new ObdDialog.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							realTips = false;
//							Intent intent = new Intent(context, VerifyPhoneActivity.class);
//							context.startActivity(intent);
//						}
//					})
//					.setNegativeButton(context.getString(R.string.general_no_need), new ObdDialog.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							realTips = false;
//						}
//					});
//
//			realDialog.setCancelable(false);
//			if (!((Activity) context).isFinishing()) {
//				realDialog.show();
//			}
//		}
//
//	}


    /**
     * 创建快捷方式
     * @param shortCutActName the page that needs to create a shortcut
     * @param shortCutName shortcut name
     * @param drawableResId shortcut icon resource id
     */
    @NonNull
	public static void createShortcuts(String shortCutActName, String shortCutName, @DrawableRes int drawableResId) {
        Context ctx = AppUtils.getAppContext();
		Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutIntent.putExtra("duplicate", false);    //是否允许重复创建

		shortcutIntent.putExtra("android.intent.extra.shortcut.NAME", shortCutName);
        Intent.ShortcutIconResource shortcutIcon = Intent.ShortcutIconResource.fromContext(ctx, drawableResId);
        shortcutIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",shortcutIcon);

        //设置关联程序 启动MainActivity
        //设置action.MAIN和CATEGORY_LAUNCHER可以在卸载应用的时候同时删除桌面快捷方式
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName(ctx, shortCutActName); // eg: MainActivity.class.getName
        intent.addCategory("android.intent.category.LAUNCHER");

        shortcutIntent.putExtra("android.intent.extra.shortcut.INTENT", intent);
		ctx.sendBroadcast(shortcutIntent);
	}


    public static void deleteShortcut(Context ctx, String shortCutName) {
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName);
        // 设置关联程序
        Intent launcherIntent = new Intent(ctx, MainActivity.class).setAction(Intent.ACTION_MAIN);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        ctx.sendBroadcast(intent);
    }



    /**
     * 判断是否有快捷方式
     *
     * @param context
     * @return
     */
    public static boolean hasShortCut(Context context) {
        boolean exist = false;
        String url;
        int sdk_int = DeviceUtils.getSDK_VCode();
        if (sdk_int < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else if (sdk_int < 19) {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
                                       new String[]{context.getString(R.string.app_name)}, null);

        if (cursor != null && cursor.moveToFirst()) {
            exist = true;
        }

        if (cursor != null) {
            cursor.close();
        }

        return exist;
    }



//
//	/**
//	 * 将map转成生成防伪码sc需要的字符串  created by xyy @20170407
//	 */
//	public static String mapToString(Map<String,Object>map){
//		List<String> keys = new ArrayList<>();
//		for(String key:map.keySet()){
//			keys.add(key);
//		}
//
//		if(keys.isEmpty()) return "";
//
//		Collections.sort(keys);
//		StringBuffer sb = new StringBuffer();
//		for(String key:keys){
//			sb.append("_").append(map.get(key));
//		}
//
//		return sb.substring(1);
//
//	}

//





//	/**
//	 * 遇到问题对话框
//	 */
//	public static void ariseProblemDialog(final Context context) {
//		LayoutInflater layoutInflater = LayoutInflater.from(context);
//		final View view = layoutInflater.inflate(R.layout.dialog_arise_problem, null);
//
//		final Dialog dialog = new Dialog(context, R.style.dialog);
//		dialog.setContentView(view);
//		dialog.show();
//
//		dialog.setCancelable(true);
//
//		final EditText edit_remark = (EditText) view.findViewById(R.id.edit_remark);
//		final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
//		final RadioButton radio_card_info_error = (RadioButton) view.findViewById(R.id.radio_card_info_error);
////		final RadioButton radio_card_damage = (RadioButton) view.findViewById(R.id.radio_card_damage);
////		final RadioButton radio_scan_error = (RadioButton) view.findViewById(R.id.radio_scan_error);
////		final RadioButton radio_not_find_card = (RadioButton) view.findViewById(R.id.radio_not_find_card);
////		final RadioButton radio_photo_error = (RadioButton) view.findViewById(R.id.radio_photo_error);
////		final RadioButton radio_other_error = (RadioButton) view.findViewById(R.id.radio_other_error);
//
//		edit_remark.setText(radio_card_info_error.getText());
//		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//
//				RadioButton rb = (RadioButton)view.findViewById(checkedId);
//				if(rb != null){
//					LogUtil.e("rb.getText() ="+rb.getText().toString());
//					if(checkedId == R.id.radio_other_error){
//						edit_remark.setText("");
//					}else {
//						edit_remark.setText(rb.getText().toString());
//					}
//				}
//			}
//		});

//		//取消按钮
//		TextView text_cancel = (TextView) view.findViewById(R.id.text_cancel);
//		text_cancel.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//
//		//确定按钮
//		TextView text_confirm = (TextView) view.findViewById(R.id.text_confirm);
//		text_confirm.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				String error_code= null,error_code_desc;
//				LogUtil.e("radioGroup.getCheckedRadioButtonId() ="+radioGroup.getCheckedRadioButtonId());
//				switch (radioGroup.getCheckedRadioButtonId()){
//					case R.id.radio_card_info_error:
//						error_code = ImEnum.EC_PICK.EC_PICK_01.value;
//						break;
//					case R.id.radio_card_damage:
//						error_code = ImEnum.EC_PICK.EC_PICK_02.value;
//						break;
//					case R.id.radio_scan_error:
//						error_code = ImEnum.EC_PICK.EC_PICK_04.value;
//						break;
//					case R.id.radio_not_find_card:
//						error_code = ImEnum.EC_PICK.EC_PICK_05.value;
//						break;
//					case R.id.radio_photo_error:
//						error_code = ImEnum.EC_PICK.EC_PICK_03.value;
//						break;
//					case R.id.radio_other_error:
//						error_code = ImEnum.EC_PICK.EC_PICK_99.value;
//						break;
//				}
//				error_code_desc = edit_remark.getText().toString();
//				if(TextUtils.isEmpty(error_code_desc)){
//					ToastUtils.showToast(context,"备注不能为空");
//					return;
//				}
//				LogUtil.e("error_code ="+error_code);
//				if(StringUtils.isNotNullOrEmpty(error_code)){
//					NetRequest.ariseProblem(context,error_code,error_code_desc);
//				}
//			}
//		});
//	}





}
