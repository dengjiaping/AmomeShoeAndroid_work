package cn.com.amome.amomeshoes.model;

/**
 * Created by ccf on 17-9-30.
 * 用于activity页面，活动模块中的疾病的实体类
 */

public class IllnessInfo {
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
