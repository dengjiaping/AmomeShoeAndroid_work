package cn.com.amome.amomeshoes.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ccf on 17-10-13.
 */

public class DetailTrainingInfo {

    /**
     * name : 脚跟踩地三连动
     * detail : 坐姿状态下，双脚足跟踩地，按顺序练习双足内翻、外翻、足背屈（即勾脚尖）。一组10次。
     * icon : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/home.mp4
     */

    private String name;
    private String detail;
    private String icon;

    public static DetailTrainingInfo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DetailTrainingInfo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
