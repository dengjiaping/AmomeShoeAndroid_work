package cn.com.amome.amomeshoes.model;

/**
 * Created by ccf on 17-9-30.
 * 用于activity页面，活动模块中的疾病的实体类
 */

public class IllnessInfo {
    /**
     *
     * return_msg : [{"type":"扁平足","icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/bianping1@2x.png","is_user_related":"0"},{"type":"旋前","icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/xuanqian1@2x.png","is_user_related":"0"},{"type":"旋后","icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/xuanhou1@2x.png","is_user_related":"0"},{"type":"高弓足","icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/gaogong1@2x.png","is_user_related":"0"},{"type":"脚气","icon":"http://www.iamome.cn/","is_user_related":"0"},{"type":"拇外翻","icon":"http://www.iamome.cn/AmomeWebApp/res/healthpromotion/muwaifan1@2x.png","is_user_related":"0"}]
     */





    private String type;//病的名称
    private String icon;//图片的url
    private String is_user_related;//是否已添加

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

    public String getIs_user_related() {
        return is_user_related;
    }

    public void setIs_user_related(String is_user_related) {
        this.is_user_related = is_user_related;
    }

}
