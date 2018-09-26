package com.netease.nim.uikit.business.recent;

import java.io.Serializable;
import java.util.List;

/**
 * Administrator
 * 2017/10/14
 * 15:39
 */
public class TeamTypeEntity implements Serializable {

    private List<TeamTypesBean> teamTypes;

    public List<TeamTypesBean> getTeamTypes() {
        return teamTypes;
    }

    public void setTeamTypes(List<TeamTypesBean> teamTypes) {
        this.teamTypes = teamTypes;
    }

    public static class TeamTypesBean implements Serializable {
        private int orgId;
        private String detailtid;
        private int detailtType;//圈子类型(1-粉团队；2-粉团队小组；3-玩家圈；4-店面讨论组；5-活动临时组；6-云店玩家圈;7-看脸圈
        private String detailId;

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getDetailId() {
            return detailId;
        }

        public void setDetailId(String detailId) {
            this.detailId = detailId;
        }

        public String getDetailtid() {
            return detailtid;
        }

        public void setDetailtid(String detailtid) {
            this.detailtid = detailtid;
        }

        public int getDetailtType() {
            return detailtType;
        }

        public void setDetailtType(int detailtType) {
            this.detailtType = detailtType;
        }
    }

}
