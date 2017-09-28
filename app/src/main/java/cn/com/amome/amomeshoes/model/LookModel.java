package cn.com.amome.amomeshoes.model;

/**
 * 看一看数据模型
 */
public class LookModel {
    private String tag = "";
    private boolean isSelect = false;
    private int bgDarkResource = 0;
    private int bgLightResource = 0;
    
    /**
     * @return isSelect
     */
    public boolean isSelect() {
        return isSelect;
    }
    /**
     * @param isSelect 要设置的 isSelect
     */
    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
    /**
     * @return bgDarkResource
     */
    public int getBgDarkResource() {
        return bgDarkResource;
    }
    /**
     * @param bgDarkResource 要设置的 bgDarkResource
     */
    public void setBgDarkResource(int bgDarkResource) {
        this.bgDarkResource = bgDarkResource;
    }
    /**
     * @return bgLightResource
     */
    public int getBgLightResource() {
        return bgLightResource;
    }
    /**
     * @param bgLightResource 要设置的 bgLightResource
     */
    public void setBgLightResource(int bgLightResource) {
        this.bgLightResource = bgLightResource;
    }
    /**
     * @return tag
     */
    public String getTag() {
        return tag;
    }
    /**
     * @param tag 要设置的 tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
    
}