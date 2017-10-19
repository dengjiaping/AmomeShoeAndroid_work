package cn.com.amome.amomeshoes.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ccf on 17-10-19.
 */

public class NursingTrueInfo {

    /**
     * name : 按摩
     * icon : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/anmo2@2x.png
     * breakdays : 0
     * user_breakdays : 0
     * icon_done : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/anmo2_done@2x.png
     * weardays : 10
     * is_done : 1
     */

    private String name;
    private String icon;
    private String breakdays;
    private String user_breakdays;
    private String icon_done;
    private String weardays;
    private String is_done;
    private boolean isChecked = false;

    public NursingTrueInfo() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static NursingTrueInfo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), NursingTrueInfo.class);
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBreakdays() {
        return breakdays;
    }

    public void setBreakdays(String breakdays) {
        this.breakdays = breakdays;
    }

    public String getUser_breakdays() {
        return user_breakdays;
    }

    public void setUser_breakdays(String user_breakdays) {
        this.user_breakdays = user_breakdays;
    }

    public String getIcon_done() {
        return icon_done;
    }

    public void setIcon_done(String icon_done) {
        this.icon_done = icon_done;
    }

    public String getWeardays() {
        return weardays;
    }

    public void setWeardays(String weardays) {
        this.weardays = weardays;
    }

    public String getIs_done() {
        return is_done;
    }

    public void setIs_done(String is_done) {
        this.is_done = is_done;
    }
}
