package cn.com.amome.amomeshoes.model;

import java.util.List;

/**
 * 存放走一走特征数据和步数
 */
public class StepModel {
	private int stepNum;
	private List<PressCharacterModel> characterList;

	public List<PressCharacterModel> getCharacterList() {
		return characterList;
	}

	public void setCharacterList(List<PressCharacterModel> characterList) {
		this.characterList = characterList;
	}

	public int getStepNum() {
		return stepNum;
	}

	public void setStepNum(int stepNum) {
		this.stepNum = stepNum;
	}

}
