package com.netease.nim.uikit.business.recent;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.HashMap;
import java.util.Map;

/**
 * Administrator
 * 2017/10/24
 * 11:20
 */
public class TeamIconHelper {

    public static final String KEY_TYPE_ICON = "icon";

    public static boolean hasTeamIconExtention(RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team) {
            return false;
        }
        Map<String, Object> ext = recentContact.getExtension();
        if (ext == null) {
            return false;
        }
        Integer mid = (Integer) ext.get(KEY_TYPE_ICON);

        return mid != null && mid.intValue() != 0;
    }

    public static void setRecentContactIcon(RecentContact recentContact, int teamTypeId) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team) {
            return;
        }
        Map<String, Object> extention = recentContact.getExtension();
        if (extention == null) {
            extention = new HashMap<>();
        }
        extention.put(KEY_TYPE_ICON, teamTypeId);
        recentContact.setExtension(extention);
        NIMClient.getService(MsgService.class).updateRecent(recentContact);
    }

}
