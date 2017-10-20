package cn.com.amome.amomeshoes.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ccf on 17-10-20.
 */

public class VideoIconInfo {
    /**
     * total_size : 28825564
     * videos : [{"icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/video/bianpingzu/jiaogencaidisanliandong.mp4"},{"icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/video/bianpingzu/home.mp4"},{"icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/video/bianpingzu/1.mp4"},{"icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/video/bianpingzu/2.mp4"},{"icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/video/bianpingzu/3.mp4"},{"icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/video/bianpingzu/4.mp4"}]
     */

    private int total_size;
    private List<VideosBean> videos;

    public static VideoIconInfo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), VideoIconInfo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getTotal_size() {
        return total_size;
    }

    public void setTotal_size(int total_size) {
        this.total_size = total_size;
    }

    public List<VideosBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosBean> videos) {
        this.videos = videos;
    }

    public static class VideosBean {
        /**
         * icon : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/video/bianpingzu/jiaogencaidisanliandong.mp4
         */

        private String icon;

        public static VideosBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), VideosBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
