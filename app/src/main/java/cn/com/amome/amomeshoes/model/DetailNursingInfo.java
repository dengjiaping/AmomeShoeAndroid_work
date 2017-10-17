package cn.com.amome.amomeshoes.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ccf on 17-10-16.
 */

public class DetailNursingInfo {
    /**
     * name : 按摩
     * detail : 用揉法、揉捏法、重推的手法分别按摩胫骨前肌、胫骨后肌、趾长屈肌、和足底小肌群及韧带，以增加肌肉和韧带力量及弹性。每日按摩1次，每次15-30分钟。
     * icon : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/anmo1@2x.png
     */

    private String name;
    private String detail;
    private String icon;

    public static DetailNursingInfo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DetailNursingInfo.class);
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
