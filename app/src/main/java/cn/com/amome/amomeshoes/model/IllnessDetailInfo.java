package cn.com.amome.amomeshoes.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ccf on 17-10-11.
 */

public class IllnessDetailInfo {

    /**
     * type : 扁平足
     * definition :  扁平足是指足纵弓塌陷或消失。可根据其形成特点分为可塑性扁平足和僵硬性扁平足。可塑性扁平足是指在站立时,体重的负荷使足弓塌陷或消失, 而当没有体重负荷时足弓正常。僵硬性扁平足则不管是否负重, 足弓都存在塌陷或消失的现象。
     * reason :  形成扁平足的原因很多, 除先天性因素外, 多数少儿的扁平足是由脚底软组织劳损或双脚缺乏锻炼, 以致肌肉和肌腱力量发育不足造成足跖骨、舟骨、楔骨及它们之间的相互位置畸形而形成扁平足的。
     * influence :  扁平足者长期站立、行走、跑跳易出现腿、膝、腰、背、颈部、头部疼痛、疲劳和脚底筋膜炎、跟腱炎、膝盖骨腱炎、髋骨腱炎各种炎症。
     * icon : http://www.iamome.cn/AmomeWebApp/res/healthpromotion/foot/2/bianpingzu2.jpg
     * healthscheme : [{"training":[{"num":"6","name":"脚跟踩地三连动,足趾屈伸,足趾展收,左脚捡物,右脚捡物,双足夹物","detail":"坐姿状态下，双脚足跟踩地，按顺序练习双足内翻、外翻、足背屈（即勾脚尖）。一组10次。"}],"accessory":[{"num":"4","name":"配件一,配件二,配件三,配件四","detail":"这里是配件一的详情"}],"nursing":[{"num":"2","name":"按摩,异地行走","detail":"用揉法、揉捏法、重推的手法分别按摩胫骨前肌、胫骨后肌、趾长屈肌、和足底小肌群及韧带，以增加肌肉和韧带力量及弹性。每日按摩1次，每次15-30分钟。"}]}]
     */

    private String type;
    private String definition;
    private String reason;
    private String influence;
    private String icon;
    private List<HealthschemeBean> healthscheme;

    public static IllnessDetailInfo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), IllnessDetailInfo.class);
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<HealthschemeBean> getHealthscheme() {
        return healthscheme;
    }

    public void setHealthscheme(List<HealthschemeBean> healthscheme) {
        this.healthscheme = healthscheme;
    }

    public static class HealthschemeBean {
        private List<TrainingBean> training;
        private List<AccessoryBean> accessory;
        private List<NursingBean> nursing;

        public static HealthschemeBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), HealthschemeBean.class);
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
             * detail : 坐姿状态下，双脚足跟踩地，按顺序练习双足内翻、外翻、足背屈（即勾脚尖）。一组10次。
             */

            private String num;
            private String name;
            private String detail;

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

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }
        }

        public static class AccessoryBean {
            /**
             * num : 4
             * name : 配件一,配件二,配件三,配件四
             * detail : 这里是配件一的详情
             */

            private String num;
            private String name;
            private String detail;

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

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }
        }

        public static class NursingBean {
            /**
             * num : 2
             * name : 按摩,异地行走
             * detail : 用揉法、揉捏法、重推的手法分别按摩胫骨前肌、胫骨后肌、趾长屈肌、和足底小肌群及韧带，以增加肌肉和韧带力量及弹性。每日按摩1次，每次15-30分钟。
             */

            private String num;
            private String name;
            private String detail;

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

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }
        }
    }
}
