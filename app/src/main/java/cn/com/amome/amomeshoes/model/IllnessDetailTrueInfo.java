package cn.com.amome.amomeshoes.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ccf on 17-10-19.
 */

public class IllnessDetailTrueInfo {

    /**
     * type : 扁平足
     * icon : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/foot/2/bianpingzu2.jpg
     * userdaily : [{"training":[{"num":"6","name":"脚跟踩地三连动,足趾屈伸,足趾展收,左脚捡物,右脚捡物,双足夹物","num_done":"2","newest_id":"0","done_times":"2"}],"accessory":[{"num":"4","name":"配件一,配件二,配件三,配件四","num_done":"0"}],"nursing":[{"num":"2","name":"按摩,异地行走","num_done":"1"}]}]
     */

    private String type;
    private String icon;
    private List<UserdailyBean> userdaily;

    public static IllnessDetailTrueInfo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), IllnessDetailTrueInfo.class);
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

    public List<UserdailyBean> getUserdaily() {
        return userdaily;
    }

    public void setUserdaily(List<UserdailyBean> userdaily) {
        this.userdaily = userdaily;
    }

    public static class UserdailyBean {
        private List<TrainingBean> training;
        private List<AccessoryBean> accessory;
        private List<NursingBean> nursing;

        public static UserdailyBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), UserdailyBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public List<TrainingBean> getTraining() {
            return training;
        }

        public void setTraining(List<TrainingBean> training) {
            this.training = training;
        }

        public List<AccessoryBean> getAccessory() {
            return accessory;
        }

        public void setAccessory(List<AccessoryBean> accessory) {
            this.accessory = accessory;
        }

        public List<NursingBean> getNursing() {
            return nursing;
        }

        public void setNursing(List<NursingBean> nursing) {
            this.nursing = nursing;
        }

        public static class TrainingBean {
            /**
             * num : 6
             * name : 脚跟踩地三连动,足趾屈伸,足趾展收,左脚捡物,右脚捡物,双足夹物
             * num_done : 2
             * newest_id : 0
             * done_times : 2
             */

            private String num;
            private String name;
            private String num_done;
            private String newest_id;
            private String done_times;

            public static TrainingBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), TrainingBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNum_done() {
                return num_done;
            }

            public void setNum_done(String num_done) {
                this.num_done = num_done;
            }

            public String getNewest_id() {
                return newest_id;
            }

            public void setNewest_id(String newest_id) {
                this.newest_id = newest_id;
            }

            public String getDone_times() {
                return done_times;
            }

            public void setDone_times(String done_times) {
                this.done_times = done_times;
            }
        }

        public static class AccessoryBean {
            /**
             * num : 4
             * name : 配件一,配件二,配件三,配件四
             * num_done : 0
             */

            private String num;
            private String name;
            private String num_done;

            public static AccessoryBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), AccessoryBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNum_done() {
                return num_done;
            }

            public void setNum_done(String num_done) {
                this.num_done = num_done;
            }
        }

        public static class NursingBean {
            /**
             * num : 2
             * name : 按摩,异地行走
             * num_done : 1
             */

            private String num;
            private String name;
            private String num_done;

            public static NursingBean objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), NursingBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNum_done() {
                return num_done;
            }

            public void setNum_done(String num_done) {
                this.num_done = num_done;
            }
        }
    }
}
