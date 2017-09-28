package cn.com.amome.amomeshoes.events;

import java.util.List;

import cn.com.amome.amomeshoes.model.ExerciseDataInfo;

/** 向运动历史数据页面传递相关数据 */
public class ExerciseHistEvent {
	private String msg;//消息
	private List<ExerciseDataInfo> modHistDataList;//历史数据（包含坐站走时间卡路里步数步频信息）

	public ExerciseHistEvent(String msg, List<ExerciseDataInfo> modHistDataList) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
		this.modHistDataList = modHistDataList;
	}

	public String getMsg() {
		return msg;
	}

	public List<ExerciseDataInfo> getHistDataList() {
		return modHistDataList;
	}
}
