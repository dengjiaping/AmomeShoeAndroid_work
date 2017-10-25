package cn.com.amome.amomeshoes.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ccf on 17-10-25.
 */

public class PromotionDataInfo {
    public PromotionDataInfo() {
    }

    /**
     * type : 高弓足
     * icon : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/gaogong1@2x.png
     * accessory : 5
     * nursing : 5
     * training : 5
     */

    private String type;
    private String icon;
    private String accessory;
    private String nursing;
    private String training;

    public static PromotionDataInfo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), PromotionDataInfo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getNursing() {
        return nursing;
    }

    public void setNursing(String nursing) {
        this.nursing = nursing;
    }

    public String getTraining() {
        return training;
    }

    public void setTraining(String training) {
        this.training = training;
    }
}
