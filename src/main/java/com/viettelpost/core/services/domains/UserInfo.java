package com.viettelpost.core.services.domains;

import com.viettelpost.core.utils.Utils;
import lombok.Data;
import net.minidev.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class UserInfo implements Serializable {
    JSONObject infos;

    public UserInfo() {
    }

    public UserInfo(JSONObject infos) {
        this.infos = infos;
    }

    public JSONObject getInfos() {
        return infos;
    }

    public void setInfos(JSONObject infos) {
        this.infos = infos;
    }

    public boolean verify() {
        long exp = infos.containsKey("exp") ? infos.getAsNumber("exp").longValue() : 0;
        return exp == 0 || exp > System.currentTimeMillis();
    }

    public Long getUserId() {

        return (Long) ((HashMap)infos.get("result")).get("userid");
    }

    public String getPost() {
        return (String) ((HashMap)infos.get("result")).get("ma_buucuc");
    }

    public String getOrg() {
        return (String) ((HashMap)infos.get("result")).get("don_vi");
    }

    public String getName() {
        return (String) ((HashMap)infos.get("result")).get("name");
    }

    public String getPhone() {
        return (String) ((HashMap)infos.get("result")).get("phone");
    }

    public String getMaChucDanh() {
        return (String) ((HashMap)infos.get("result")).get("ma_chucdanh") ;
    }

    public String getUsername() {
        return (String) ((HashMap)infos.get("result")).get("username");
    }

    public String getFirstname() {
        return (String) ((HashMap)infos.get("result")).get("firstname");
    }

    public String getLastname() {
        return (String) ((HashMap)infos.get("result")).get("lastname");
    }

    public String getEmail() {
        return (String) ((HashMap)infos.get("result")).get("email");
    }

    public String getManhanvien() {
        return (String) ((HashMap)infos.get("result")).get("manhanvien");
    }

    public String getDn_userid() {
        return (String) ((HashMap)infos.get("result")).get("dn_userid");
    }

    public String getMa_buucuc() {
        return (String) ((HashMap)infos.get("result")).get("ma_buucuc");
    }

    public String getChi_nhanh() {
        return (String) ((HashMap)infos.get("result")).get("don_vi");
    }

    public String getMa_chucdanh() {
        return (String) ((HashMap)infos.get("result")).get("ma_chucdanh");
    }

    public String getEmployeeGroupId() {
        return  (String) ((HashMap)infos.get("result")).get("employee_group_id");
    }

    public String getRoleId() {
        return  (String) ((HashMap)infos.get("result")).get("roleid");
    }

    public Integer getSource() {
        String sourceTmp = infos.getAsString("source");
//        if (Utils.isNullOrEmpty(sourceTmp) || !StringUtils.isNumeric(sourceTmp)) {
        if (Utils.isNullOrEmpty(sourceTmp)) {
            return -2;
        }
        return Integer.valueOf(sourceTmp);
    }

}
