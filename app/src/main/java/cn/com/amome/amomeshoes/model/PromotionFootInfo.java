package cn.com.amome.amomeshoes.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 暂时未用
 */
public class PromotionFootInfo {
	public static final String TAG = "PromotionFootInfo";

	@SerializedName("accessory")
	public List<Content> accessory;

	@SerializedName("nursing")
	public List<Content> nursing;

	@SerializedName("training")
	public List<Content> training;

	public class Content {
		@SerializedName("name")
		public String name;

		@SerializedName("value")
		public ArrayList<String> value;
	}
}
