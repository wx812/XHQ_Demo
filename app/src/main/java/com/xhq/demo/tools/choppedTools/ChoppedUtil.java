package com.xhq.demo.tools.choppedTools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.xhq.demo.R;
import com.xhq.demo.tools.appTools.AppUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/11.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class ChoppedUtil{

    /**
     * Name of the meta-data item that should be set in the AndroidManifest.xml
     * to specify the icon that should be displayed for the preference.
     */
    private static final String META_DATA_PREFERENCE_ICON = "com.android.settings.icon";

    /**
     * Name of the meta-data item that should be set in the AndroidManifest.xml
     * to specify the title that should be displayed for the preference.
     */
    private static final String META_DATA_PREFERENCE_TITLE = "com.android.settings.title";

    /**
     * Name of the meta-data item that should be set in the AndroidManifest.xml
     * to specify the summary text that should be displayed for the preference.
     */
    private static final String META_DATA_PREFERENCE_SUMMARY = "com.android.settings.summary";


    /**
     * Set the preference's title to the matching activity's label.
     */
    public static final int UPDATE_PREFERENCE_FLAG_SET_TITLE_TO_MATCHING_ACTIVITY = 1;

    /**
     * Finds a matching activity for a preference's intent. If a matching
     * activity is not found, it will remove the preference.
     *
     * @param context
     *            The context.
     * @param parentPreferenceGroup
     *            The preference group that contains the preference whose intent
     *            is being resolved.
     * @param preferenceKey
     *            The key of the preference whose intent is being resolved.
     * @param flags
     *            0 or one or more of
     *            {@link #UPDATE_PREFERENCE_FLAG_SET_TITLE_TO_MATCHING_ACTIVITY}
     *            .
     * @return Whether an activity was found. If false, the preference was
     *         removed.
     */
    public static boolean updatePreferenceToSpecificActivityOrRemove(Context context,
                                                                     PreferenceGroup parentPreferenceGroup, String preferenceKey, int flags) {

        Preference preference = parentPreferenceGroup.findPreference(preferenceKey);
        if (preference == null) {
            return false;
        }

        Intent intent = preference.getIntent();
        if (intent != null) {
            // Find the activity that is in the system image
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                ResolveInfo resolveInfo = list.get(i);
                if ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

                    // Replace the intent with this specific activity
                    preference.setIntent(new Intent().setClassName(
                            resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));

                    if ((flags & UPDATE_PREFERENCE_FLAG_SET_TITLE_TO_MATCHING_ACTIVITY) != 0) {
                        // Set the preference title to the activity's label
                        preference.setTitle(resolveInfo.loadLabel(pm));
                    }

                    return true;
                }
            }
        }

        // Did not find a matching activity, so remove the preference
        parentPreferenceGroup.removePreference(preference);

        return false;
    }


    public static boolean updateHeaderToSpecificActivityFromMetaDataOrRemove(Context context,
                                                                             List<PreferenceActivity.Header> target, PreferenceActivity.Header header) {

        Intent intent = header.intent;
        if (intent != null) {
            // Find the activity that is in the system image
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                ResolveInfo resolveInfo = list.get(i);
                if ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    Drawable icon = null;
                    String title = null;
                    String summary = null;

                    // Get the activity's meta-data
                    try {
                        Resources res = pm
                                .getResourcesForApplication(resolveInfo.activityInfo.packageName);
                        Bundle metaData = resolveInfo.activityInfo.metaData;

                        if (res != null && metaData != null) {
                            icon = res.getDrawable(metaData.getInt(META_DATA_PREFERENCE_ICON));
                            title = res.getString(metaData.getInt(META_DATA_PREFERENCE_TITLE));
                            summary = res.getString(metaData.getInt(META_DATA_PREFERENCE_SUMMARY));
                        }
                    } catch (PackageManager.NameNotFoundException | Resources.NotFoundException e) {
                        // Ignore
                    }

                    // Set the preference title to the activity's label if no
                    // meta-data is found
                    if (TextUtils.isEmpty(title)) {
                        title = resolveInfo.loadLabel(pm).toString();
                    }

                    // Set icon, title and summary for the preference
                    // TODO:
                    // header.icon = icon;
                    header.title = title;
                    header.summary = summary;
                    // Replace the intent with this specific activity
                    header.intent = new Intent().setClassName(resolveInfo.activityInfo.packageName,
                                                              resolveInfo.activityInfo.name);

                    return true;
                }
            }
        }

        // Did not find a matching activity, so remove the preference
        target.remove(header);

        return false;
    }


    /**
     * Returns true if Monkey is running.
     */
    public static boolean isMonkeyRunning() {
        return ActivityManager.isUserAMonkey();
    }


    //查询权限
    public static List<String> findDeniedPermissions(Activity activity, String... permission){
        List<String> denyPermissions = new ArrayList<>();
        for(String value : permission){
            if(ContextCompat.checkSelfPermission(activity, value) != PackageManager.PERMISSION_GRANTED){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }


    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if(null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 打电话
     *
     * @param context
     * @param phone
     */
    public static void makePhone(Context context, String phone){
        try{
//            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //			if(TelephonyManager.SIM_STATE_READY == mgr .getSimState()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
            //			}else {
            //				Utils.showBlackToast(context, "当前手机sim卡不可用！");
            //			}
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 验证手机号是否存在
     *
     * @param mobiles
     */
    public static boolean isMobileNum(String mobiles){
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    虚拟运营商：17x
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][3578]\\d{9}";
        //"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobiles.matches(telRegex);
    }


    //隐藏手机号中间4位
    public static String hideMobile(@NonNull String phone){
        return phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
    }


    //隐藏中间部分
    public static String hideName(@NonNull String name){
        return hideString(name, 2, 3);
    }

    //隐藏银行卡号中间部分
    public static String hideBankCardNum(@NonNull String cardNum){
        return hideString(cardNum, 4, 5);
    }


    /**
     * according to the offset hidden string
     *
     * @param sourceStr source string
     * @param startOffsetPos start offset
     * @param endOffsetPos end offset
     * @return hided string
     */
    public static String hideString(@NonNull String sourceStr, int startOffsetPos, int endOffsetPos){
        int length = sourceStr.length();
        startOffsetPos = startOffsetPos < 0 ? 0 : startOffsetPos;
        endOffsetPos = endOffsetPos > length ? length : endOffsetPos;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sourceStr.length(); i++){
            char c = sourceStr.charAt(i);
            if(i >= startOffsetPos && i <= length - endOffsetPos){
                sb.append('*');
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String formatBankCardNum(String str){
        final String WHITE_SPACE = " ";
        String num = str.replaceAll(WHITE_SPACE, "");
        int len = num.length();

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < len; i++){
            builder.append(num.charAt(i));
            if(i == 3 || i == 7 || i == 11 || i == 15){
                if(i != len - 1)
                    builder.append(WHITE_SPACE);
            }
        }

        return builder.toString();
    }


    /**
     * 返回截取字段颜色
     *
     * @return
     */
    public static SpannableString setTextOtherColor(String str, int startNum, int endNum){
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#e74c3c"));
        spannableString.setSpan(colorSpan, startNum, endNum, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    /**
     * 更新进程的Widget
     *
     * @param context
     */
    public static void updateProcessWidget(Context context) {
//        AppWidgetManager awm = AppWidgetManager.getInstance(context);
//        ComponentName provider = new ComponentName(context, ProcessAppWidgetProvider.class);
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.process_widget);
//         更新Widget上面控件的内容
//        views.setTextViewText(R.id.process_count, "正在运行软件:" + AppUtils.getSystemRunningProcessSize());
//        views.setTextViewText(R.id.process_memory, "可用内存:" + Utils.getFreeMem(context));
//         给Widget安装的按钮设置事件(点击按钮的时候就会发出一个广播)
//        Intent intent = new Intent();
//        intent.setAction("action.killbackgroundprocess");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, 0);
//        views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
//        awm.updateAppWidget(provider, views);
    }


    public static boolean isBatteryPresent(Intent batteryChangedIntent) {
        return batteryChangedIntent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
    }

    public static String getBatteryPercentage(Intent batteryChangedIntent) {
        int level = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return String.valueOf(level * 100 / scale) + "%";
    }

    public static String getBatteryStatus(Resources res, Intent batteryChangedIntent) {
        final Intent intent = batteryChangedIntent;

        int plugType = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                                        BatteryManager.BATTERY_STATUS_UNKNOWN);
        String statusString;
        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            statusString = (String)AppUtils.getAppContext().getText(R.string.battery_info_status_charging);
//            statusString = res.getString(R.string.battery_info_status_charging);
            if (plugType > 0) {
                int resStr;
                if (plugType == BatteryManager.BATTERY_PLUGGED_AC) {
                    resStr = R.string.battery_info_status_charging_ac;
                } else if (plugType == BatteryManager.BATTERY_PLUGGED_USB) {
                    resStr = R.string.battery_info_status_charging_usb;
                } else {
                    resStr = R.string.battery_info_status_charging_wireless;
                }
                statusString = statusString + " " + res.getString(resStr);
            }
        } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            statusString = res.getString(R.string.battery_info_status_discharging);
        } else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
            statusString = res.getString(R.string.battery_info_status_not_charging);
        } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
            statusString = res.getString(R.string.battery_info_status_full);
        } else {
            statusString = res.getString(R.string.battery_info_status_unknown);
        }
        return statusString;
    }



    private static String getLocalProfileGivenName(Context context) {
        final ContentResolver cr = context.getContentResolver();

        // Find the raw contact ID for the local ME profile raw contact.
        final long localRowProfileId;
        final Cursor localRawProfile = cr.query(ContactsContract.Profile.CONTENT_RAW_CONTACTS_URI,
                                                new String[] { ContactsContract.RawContacts._ID },
                                                ContactsContract.RawContacts.ACCOUNT_TYPE + " IS NULL AND "
                                                        + ContactsContract.RawContacts.ACCOUNT_NAME + " IS NULL",
                                                null, null);
        if (localRawProfile == null)
            return null;

        try {
            if (!localRawProfile.moveToFirst()) {
                return null;
            }
            localRowProfileId = localRawProfile.getLong(0);
        } finally {
            localRawProfile.close();
        }

        // Find the structured name for the raw contact.
        final Cursor structuredName = cr
                .query(ContactsContract.Profile.CONTENT_URI.buildUpon().
                        appendPath(ContactsContract.Contacts.Data.CONTENT_DIRECTORY).build(),
                       new String[] { ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME },
                       ContactsContract.Data.RAW_CONTACT_ID + "=" + localRowProfileId,
                       null, null);
        if (structuredName == null)
            return null;

        try {
            if (!structuredName.moveToFirst()) {
                return null;
            }
            String partialName = structuredName.getString(0);
            if (TextUtils.isEmpty(partialName)) {
                partialName = structuredName.getString(1);
            }
            return partialName;
        } finally {
            structuredName.close();
        }
    }


    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }


    public static void bindString(SQLiteStatement stat, int position, String values) {
        if (values == null) {
            stat.bindNull(position);
        } else {
            stat.bindString(position, values);
        }
    }

}
