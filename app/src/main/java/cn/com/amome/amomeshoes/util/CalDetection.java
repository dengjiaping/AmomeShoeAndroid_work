package cn.com.amome.amomeshoes.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.model.CopInfo;
import cn.com.amome.amomeshoes.model.FrontAndBackTimeInfo;
import cn.com.amome.amomeshoes.model.PressData;
import cn.com.amome.amomeshoes.model.PressCharacterModel;
import cn.com.amome.amomeshoes.model.PressStepModel;
import cn.com.amome.amomeshoes.model.ShakeInfo;
import cn.com.amome.amomeshoes.model.StandDetectionModel;
import cn.com.amome.amomeshoes.model.StepModel;
import cn.com.amome.amomeshoes.model.WalkInfo;
import cn.com.amome.amomeshoes.model.WalkTimeNumInfo;
import cn.com.amome.amomeshoes.widget.CopStandardView;
import android.util.Log;

public class CalDetection {
	private static String TAG = "CalDetection";
	private static final short minValue = 20;// 51
	private static short[] totalPress, minPress, maxPress;

	/**
	 * 蹲一蹲-右脚足弓判断 根据区域点数计算
	 * 
	 * @param 64点数据
	 * @return 1:高弓;2:正常;3:扁平
	 */
	public static int calRightInstep(short[] mPress) {
		int return_code = 0;
		int num = 0; // 计算空白区域周围多少个点被压住
		if (mPress[21] > minValue) {
			num++;
		}
		if (mPress[22] > minValue) {
			num++;
		}
		if (mPress[23] > minValue) {
			num++;
		}
		if (mPress[24] > minValue) {
			num++;
		}
		if (mPress[25] > minValue) {
			num++;
		}
		if (mPress[26] > minValue) {
			num++;
		}
		if (mPress[27] > minValue) {
			num++;
		}
		if (mPress[28] > minValue) {
			num++;
		}
		if (mPress[29] > minValue) {
			num++;
		}
		if (mPress[30] > minValue) {
			num++;
		}
		if (mPress[31] > minValue) {
			num++;
		}
		if (mPress[32] > minValue) {
			num++;
		}
		if (mPress[33] > minValue) {
			num++;
		}
		if (mPress[34] > minValue) {
			num++;
		}
		if (mPress[35] > minValue) {
			num++;
		}
		if (mPress[36] > minValue) {
			num++;
		}
		if (mPress[37] > minValue) {
			num++;
		}
		if (mPress[38] > minValue) {
			num++;
		}
		if (mPress[39] > minValue) {
			num++;
		}
		if (mPress[40] > minValue) {
			num++;
		}
		if (mPress[41] > minValue) {
			num++;
		}
		if (mPress[42] > minValue) {
			num++;
		}
		if (mPress[43] > minValue) {
			num++;
		}
		if (mPress[44] > minValue) {
			num++;
		}
		if (mPress[45] > minValue) {
			num++;
		}
		Log.i(TAG, "num" + num);
		// 大于21为扁平，小于13为高弓，介于之间为正常
		if (num > 21) {
			return_code = 3;
		} else if (num < 13) {
			return_code = 1;
		} else if (num >= 13 && num <= 21) {
			return_code = 2;
		} else {
			return_code = 4;
		}
		Log.i(TAG, "return_code" + return_code);
		return return_code;
	}

	/**
	 * 蹲一蹲-左脚足弓判断 根据区域点数计算
	 * 
	 * @param 64点数据
	 * @return 1:高弓;2:正常;3:扁平
	 */
	public static int calLeftInstep(short[] mPress) {
		int return_code = 0;
		int num = 0; // 计算空白区域周围多少个点被压住
		if (mPress[21] > minValue) {
			num++;
		}
		if (mPress[22] > minValue) {
			num++;
		}
		if (mPress[23] > minValue) {
			num++;
		}
		if (mPress[24] > minValue) {
			num++;
		}
		if (mPress[25] > minValue) {
			num++;
		}
		if (mPress[26] > minValue) {
			num++;
		}
		if (mPress[27] > minValue) {
			num++;
		}
		if (mPress[28] > minValue) {
			num++;
		}
		if (mPress[29] > minValue) {
			num++;
		}
		if (mPress[30] > minValue) {
			num++;
		}
		if (mPress[31] > minValue) {
			num++;
		}
		if (mPress[32] > minValue) {
			num++;
		}
		if (mPress[33] > minValue) {
			num++;
		}
		if (mPress[34] > minValue) {
			num++;
		}
		if (mPress[35] > minValue) {
			num++;
		}
		if (mPress[36] > minValue) {
			num++;
		}
		if (mPress[37] > minValue) {
			num++;
		}
		if (mPress[38] > minValue) {
			num++;
		}
		if (mPress[39] > minValue) {
			num++;
		}
		if (mPress[40] > minValue) {
			num++;
		}
		if (mPress[41] > minValue) {
			num++;
		}
		if (mPress[42] > minValue) {
			num++;
		}
		if (mPress[43] > minValue) {
			num++;
		}
		if (mPress[44] > minValue) {
			num++;
		}
		if (mPress[49] > minValue) {
			num++;
		}
		Log.i(TAG, "num" + num);
		// 大于21为扁平，小于13为高弓，介于之间为正常
		if (num > 21) {
			return_code = 3;
		} else if (num < 13) {
			return_code = 1;
		} else {
			return_code = 2;
		}
		Log.i(TAG, "return_code" + return_code);
		return return_code;
	}

	/**
	 * 蹲一蹲-双足弓判断
	 * 
	 * @param left_code
	 *            1:高弓;2:正常;3:扁平
	 * @param right_code
	 *            1:高弓;2:正常;3:扁平
	 * @return 足弓判断结果
	 */
	public static String calDoubleInstep(int left_code, int right_code) {
		String str = "";
		if (left_code == 1 && right_code == 1) {
			str = "高弓足";
		} else if (left_code == 2 && right_code == 2) {
			str = "正常";
		} else if (left_code == 3 && right_code == 3) {
			str = "扁平足";
		} else if ((left_code == 1 && right_code == 2)
				|| (left_code == 2 && right_code == 1)) {
			str = "高弓足";
		} else if ((left_code == 3 && right_code == 2)
				|| (left_code == 2 && right_code == 3)) {
			str = "扁平足";
		} else if (left_code == 4 && right_code == 1) {
			str = "高弓足";
		} else if (left_code == 4 && right_code == 2) {
			str = "正常";
		} else if (left_code == 4 && right_code == 3) {
			str = "扁平足";
		} else if (left_code == 4 && right_code == 4) {
			str = "";
		} else if (left_code == 1 && right_code == 4) {
			str = "高弓足";
		} else if (left_code == 2 && right_code == 4) {
			str = "正常";
		} else if (left_code == 3 && right_code == 4) {
			str = "扁平足";
		} else if ((left_code == 3 && right_code == 1)
				|| (left_code == 1 && right_code == 3)) {
			str = "正常";
		} else {
			str = "";
		}
		Log.i(TAG, "双脚足弓判断结果为" + str);
		return str;
	}

	/**
	 * 蹲一蹲-左脚前后旋判断
	 * 
	 * @param 64点数据
	 * @return 左脚前后旋判断结果
	 */
	public static String calLeftSpin(short[] mPress) {
		String spin = "";
		int outAreaCount = 0, inAreaCount = 0;// 统计外侧区域和内侧区域压上的点
		if (mPress[0] > minValue) {
			outAreaCount++;
		}
		if (mPress[1] > minValue) {
			outAreaCount++;
		}
		if (mPress[4] > minValue) {
			outAreaCount++;
		}
		if (mPress[5] > minValue) {
			outAreaCount++;
		}
		if (mPress[9] > minValue) {
			outAreaCount++;
		}
		if (mPress[10] > minValue) {
			outAreaCount++;
		}
		if (mPress[11] > minValue) {
			outAreaCount++;
		}
		if (mPress[15] > minValue) {
			outAreaCount++;
		}
		if (mPress[16] > minValue) {
			outAreaCount++;
		}
		if (mPress[17] > minValue) {
			outAreaCount++;
		}
		if (mPress[21] > minValue) {
			outAreaCount++;
		}
		if (mPress[22] > minValue) {
			outAreaCount++;
		}
		if (mPress[23] > minValue) {
			outAreaCount++;
		}

		if (mPress[2] > minValue) {
			inAreaCount++;
		}
		if (mPress[3] > minValue) {
			inAreaCount++;
		}
		if (mPress[7] > minValue) {
			inAreaCount++;
		}
		if (mPress[8] > minValue) {
			inAreaCount++;
		}
		if (mPress[12] > minValue) {
			inAreaCount++;
		}
		if (mPress[13] > minValue) {
			inAreaCount++;
		}
		if (mPress[14] > minValue) {
			inAreaCount++;
		}
		if (mPress[18] > minValue) {
			inAreaCount++;
		}
		if (mPress[19] > minValue) {
			inAreaCount++;
		}
		if (mPress[20] > minValue) {
			inAreaCount++;
		}
		if (mPress[24] > minValue) {
			inAreaCount++;
		}
		if (mPress[25] > minValue) {
			inAreaCount++;
		}
		if (mPress[26] > minValue) {
			inAreaCount++;
		}

		if (outAreaCount < inAreaCount) {
			Log.i(TAG, "左脚旋后");
			if (outAreaCount >= 13) {
				spin = "左脚正常";
			} else if (outAreaCount <= 9) {
				spin = "左脚重度旋后";
			} else {
				spin = "左脚轻度旋后";
			}
		} else if (outAreaCount > inAreaCount) {
			Log.i(TAG, "左脚旋前");
			if (inAreaCount >= 13) {
				spin = "左脚正常";
			} else if (inAreaCount <= 9) {
				spin = "左脚重度旋前";
			} else {
				spin = "左脚轻度旋前";
			}
		} else {
			Log.i(TAG, "左脚正常");
			spin = "左脚正常";
		}
		return spin;
	}

	/**
	 * 蹲一蹲-右脚前后旋判断
	 * 
	 * @param 64点数据
	 * @return 右脚前后旋判断结果
	 */
	public static String calRightSpin(short[] mPress) {
		String spin = "";
		int outAreaCount = 0, inAreaCount = 0;// 统计外侧区域和内侧区域压上的点
		if (mPress[0] > minValue) {
			inAreaCount++;
		}
		if (mPress[1] > minValue) {
			inAreaCount++;
		}
		if (mPress[4] > minValue) {
			inAreaCount++;
		}
		if (mPress[5] > minValue) {
			inAreaCount++;
		}
		if (mPress[9] > minValue) {
			inAreaCount++;
		}
		if (mPress[10] > minValue) {
			inAreaCount++;
		}
		if (mPress[11] > minValue) {
			inAreaCount++;
		}
		if (mPress[15] > minValue) {
			inAreaCount++;
		}
		if (mPress[16] > minValue) {
			inAreaCount++;
		}
		if (mPress[17] > minValue) {
			inAreaCount++;
		}
		if (mPress[21] > minValue) {
			inAreaCount++;
		}
		if (mPress[22] > minValue) {
			inAreaCount++;
		}
		if (mPress[23] > minValue) {
			inAreaCount++;
		}

		if (mPress[2] > minValue) {
			outAreaCount++;
		}
		if (mPress[3] > minValue) {
			outAreaCount++;
		}
		if (mPress[7] > minValue) {
			outAreaCount++;
		}
		if (mPress[8] > minValue) {
			outAreaCount++;
		}
		if (mPress[12] > minValue) {
			outAreaCount++;
		}
		if (mPress[13] > minValue) {
			outAreaCount++;
		}
		if (mPress[14] > minValue) {
			outAreaCount++;
		}
		if (mPress[18] > minValue) {
			outAreaCount++;
		}
		if (mPress[19] > minValue) {
			outAreaCount++;
		}
		if (mPress[20] > minValue) {
			outAreaCount++;
		}
		if (mPress[24] > minValue) {
			outAreaCount++;
		}
		if (mPress[25] > minValue) {
			outAreaCount++;
		}
		if (mPress[26] > minValue) {
			outAreaCount++;
		}

		if (outAreaCount < inAreaCount) {
			Log.i(TAG, "右脚旋后");
			if (outAreaCount >= 13) {
				spin = "右脚正常";
			} else if (outAreaCount <= 9) {
				spin = "右脚重度旋后";
			} else {
				spin = "右脚轻度旋后";
			}
		} else if (outAreaCount > inAreaCount) {
			Log.i(TAG, "右脚旋前");
			if (inAreaCount >= 13) {
				spin = "右脚正常";
			} else if (inAreaCount <= 9) {
				spin = "右脚重度旋前";
			} else {
				spin = "右脚脚轻度旋前";
			}
		} else {
			Log.i(TAG, "右脚正常");
			spin = "右脚正常";
		}
		return spin;
	}

	/**
	 * 蹲一蹲-双脚前后旋判断
	 * 
	 * @param leftSpin
	 *            左脚结果
	 * @param rightSpin
	 *            右脚结果
	 * @return 双脚结果
	 */
	public static String calDoubleSpin(String leftSpin, String rightSpin) {
		String spin = "";
		if (leftSpin.contains("旋前") && rightSpin.contains("旋前")) {
			if (leftSpin.contains("重度") || rightSpin.contains("重度")) {
				spin = "重度旋前";
			} else {
				spin = "轻度旋前";
			}
		} else if (leftSpin.contains("旋后") && rightSpin.contains("旋后")) {
			if (leftSpin.contains("重度") || rightSpin.contains("重度")) {
				spin = "重度旋后";
			} else {
				spin = "轻度旋后";
			}
		} else {
			spin = "正常";
		}
		Log.i(TAG, "双脚前后旋判断结果为" + spin);
		return spin;
	}

	/**
	 * 站一站-计算站立平均压力数据（排除每个点的最大最小值，再求平均值）
	 * 
	 * @param stressList
	 *            蓝牙上报的所有压力数据集合
	 * @return
	 */
	public static short[] CalAvgStress(List<short[]> stressList) {
		totalPress = new short[64];// 总压力
		minPress = new short[64];// 最小压力
		maxPress = new short[64];// 最大压力
		for (int i = 0; i < 64; i++) {
			minPress[i] = 0;
			maxPress[i] = 0;
			totalPress[i] = 0;
			int allPressNum = 0;
			for (int j = 0; j < stressList.size(); j++) {
				if (j == 0) {
					minPress[i] = stressList.get(j)[i];
					maxPress[i] = stressList.get(j)[i];
					totalPress[i] = stressList.get(j)[i];
				} else {
					minPress[i] = minPress[i] < stressList.get(j)[i] ? minPress[i]
							: stressList.get(j)[i];
					maxPress[i] = maxPress[i] > stressList.get(j)[i] ? maxPress[i]
							: stressList.get(j)[i];
					allPressNum += stressList.get(j)[i];// 第i个点的总压力值
				}
			}
			totalPress[i] = (short) ((allPressNum - minPress[i] - maxPress[i]) / (stressList
					.size() - 2)); // 第i个点的总压力值减掉最大值和最小值最后求出第i个点的均值
		}
		return totalPress; // 此时已变为平均压力
	}

	/**
	 * 站一站-肩部，背部疾病判断
	 * 
	 * @param leftPress
	 *            左脚平均压力数据
	 * @param rightPress
	 *            右脚平均压力数据
	 * @return StandDetectionModel实例
	 */
	public static StandDetectionModel CalDisease(short[] leftPress,
			short[] rightPress) {
		float leftAreaTop = 0.0f, leftAreaBottom = 0.0f, rightAreaTop = 0.0f, rightAreaBottom = 0.0f; // 左脚前端总压力，左脚后端总压力，右脚前端总压力，右脚后端总压力
		float leftTopValue = 0.0f, rightTopValue = 0.0f, leftValue = 0.0f, rightValue = 0.0f;// 左脚前端占整只脚的比重，右脚前端占整只脚的比重，左脚压力占双脚压力的比重，右脚压力占双脚压力的比重
		String disease = "", disease2 = ""; // 疾病1和疾病2，疾病1为肩部疾病，疾病二为背部疾病或盆股疾病等
		StandDetectionModel standDetectionModel = new StandDetectionModel(); // 创建StandDetectionModel实例，存放压力比例和疾病信息
		// 计算左右脚前半部分压力总值
		for (int i = 0; i <= 36; i++) {
			leftAreaTop += leftPress[i];
			rightAreaTop += rightPress[i];
		}
		// 计算左右脚后半部分压力总值
		for (int i = 37; i <= 63; i++) {
			leftAreaBottom += leftPress[i];
			rightAreaBottom += rightPress[i];
		}

		// 通过左脚压力所占比重，判断肩部是否有高低肩
		leftValue = (leftAreaTop + leftAreaBottom)
				/ (leftAreaTop + leftAreaBottom + rightAreaTop + rightAreaBottom);// 计算左脚压力占双脚压力的比重
		if (leftValue > 0.55f) {
			disease = "高低肩-右肩高";
		} else if (leftValue < 0.45f) {
			disease = "高低肩-左肩高";
		} else {
			disease = "正常";
		}
		Log.i(TAG, "疾病一" + disease);
		standDetectionModel.setDisease(disease);

		// 通过两只脚前端比重来判断以下疾病
		leftTopValue = leftAreaTop / (leftAreaTop + leftAreaBottom);// 计算左脚前端占整只脚的比重
		rightTopValue = rightAreaTop / (rightAreaTop + rightAreaBottom);// 计算右脚前端占整只脚的比重
		if (leftTopValue > 0.39f && rightTopValue < 0.28f) {
			disease2 = "右侧--骨盆前悬，脊柱右弯";
		} else if ((leftTopValue < 0.28f) && (rightTopValue > 0.39f)) {
			disease2 = "左侧--骨盆后悬，脊柱左弯";
		} else if ((leftTopValue < 0.28f) && (rightTopValue < 0.28f)) {
			disease2 = "重心靠后--腰椎痛";
		} else if ((leftTopValue > 0.39f) && (rightTopValue > 0.39f)) {
			disease2 = "重心靠前--颈椎痛";
		} else {
			disease2 = "正常";
		}
		Log.i(TAG, "疾病二" + disease2);
		standDetectionModel.setDisease2(disease2);

		return standDetectionModel;
	}

	/**
	 * 站一站-将压力值转为字符串，用英文逗号间隔，用于推送至服务端
	 * 
	 * @param mPress
	 * @return
	 */
	public static String changePressToStr(short[] mPress) {
		String str = "";
		for (int i = 0; i < mPress.length; i++) {
			str = str + "," + mPress[i];
		}
		str = str.substring(1, str.length()); // 去掉第一个逗号
		Log.i(TAG, str);
		return str;
	}

	/**
	 * 站一站-计算左右脚压力比例
	 * 
	 * @param leftPress
	 * @param rightPress
	 * @return
	 */
	public static StandDetectionModel CalPressScale(short[] leftPress,
			short[] rightPress) {
		float leftAreaTop = 0.0f, leftAreaBottom = 0.0f, rightAreaTop = 0.0f, rightAreaBottom = 0.0f; // 左脚前端总压力，左脚后端总压力，右脚前端总压力，右脚后端总压力

		StandDetectionModel standDetectionModel = new StandDetectionModel();// 创建StandDetectionModel实例，存放压力比例和疾病信息
		// 计算左右脚前半部分压力总值
		for (int i = 0; i <= 36; i++) {
			leftAreaTop += leftPress[i];
			rightAreaTop += rightPress[i];
		}
		// 计算左右脚后半部分压力总值
		for (int i = 37; i <= 63; i++) {
			leftAreaBottom += leftPress[i];
			rightAreaBottom += rightPress[i];
		}

		// 计算左右脚压力比
		if (leftAreaTop + leftAreaBottom + rightAreaTop + rightAreaBottom != 0) {
			float leftScale = (leftAreaTop + leftAreaBottom)
					/ (leftAreaTop + leftAreaBottom + rightAreaTop + rightAreaBottom);
			float rightScale = (rightAreaTop + rightAreaBottom)
					/ (leftAreaTop + leftAreaBottom + rightAreaTop + rightAreaBottom);
			standDetectionModel.setLeftScale(leftScale);
			standDetectionModel.setRightScale(rightScale);
		}

		return standDetectionModel;
	}

	/**
	 * 摇一摇-根据左脚压力数据计算并记录cop点信息
	 * 
	 * @param mPress
	 * @return CopInfo实例
	 */
	public static CopInfo calLeftCopZuobiao(short mPress[]) {
		short sum = 0;
		// 计算64点的压力总值
		for (int i = 0; i < mPress.length; i++) {
			sum = (short) (sum + mPress[i]);
		}
		if (sum != 0) {
			// cop点的x坐标算法是将64个点的每个点的x坐标乘以对应的压力值再求和，再除64点的压力总值（sum）。cop点的y坐标同理。
			int x_zuobiao = (31 * mPress[0] + 44 * mPress[1] + 60 * mPress[2]
					+ 65 * mPress[3] + 25 * mPress[4] + 36 * mPress[5]
					+ 46 * mPress[6] + 58 * mPress[7] + 68 * mPress[8]
					+ 16 * mPress[9] + 26 * mPress[10] + 37 * mPress[11]
					+ 49 * mPress[12] + 61 * mPress[13] + 71 * mPress[14]
					+ 11 * mPress[15] + 22 * mPress[16] + 34 * mPress[17]
					+ 46 * mPress[18] + 59 * mPress[19] + 71 * mPress[20]
					+ 9 * mPress[21] + 20 * mPress[22] + 32 * mPress[23]
					+ 44 * mPress[24] + 54 * mPress[25] + 64 * mPress[26]
					+ 11 * mPress[27] + 20 * mPress[28] + 32 * mPress[29]
					+ 43 * mPress[30] + 54 * mPress[31] + 14 * mPress[32]
					+ 22 * mPress[33] + 31 * mPress[34] + 40 * mPress[35]
					+ 49 * mPress[36] + 15 * mPress[37] + 28 * mPress[38]
					+ 31 * mPress[39] + 16 * mPress[40] + 24 * mPress[41]
					+ 34 * mPress[42] + 42 * mPress[43] + 51 * mPress[44]
					+ 16 * mPress[45] + 25 * mPress[46] + 34 * mPress[47]
					+ 44 * mPress[48] + 53 * mPress[49] + 16 * mPress[50]
					+ 26 * mPress[51] + 35 * mPress[52] + 45 * mPress[53]
					+ 55 * mPress[54] + 17 * mPress[55] + 26 * mPress[56]
					+ 36 * mPress[57] + 45 * mPress[58] + 55 * mPress[59]
					+ 22 * mPress[60] + 33 * mPress[61] + 43 * mPress[62] + 54 * mPress[63])
					/ sum;
			int y_zuobiao = (18 * mPress[0] + 12 * mPress[1] + 12 * mPress[2]
					+ 16 * mPress[3] + 33 * mPress[4] + 31 * mPress[5]
					+ 31 * mPress[6] + 31 * mPress[7] + 33 * mPress[8]
					+ 54 * mPress[9] + 52 * mPress[10] + 51 * mPress[11]
					+ 50 * mPress[12] + 49 * mPress[13] + 48 * mPress[14]
					+ 73 * mPress[15] + 72 * mPress[16] + 72 * mPress[17]
					+ 72 * mPress[18] + 72 * mPress[19] + 66 * mPress[20]
					+ 92 * mPress[21] + 91 * mPress[22] + 91 * mPress[23]
					+ 88 * mPress[24] + 87 * mPress[25] + 87 * mPress[26]
					+ 112 * mPress[27] + 110 * mPress[28] + 109 * mPress[29]
					+ 107 * mPress[30] + 106 * mPress[31] + 130 * mPress[32]
					+ 129 * mPress[33] + 128 * mPress[34] + 125 * mPress[35]
					+ 124 * mPress[36] + 150 * mPress[37] + 149 * mPress[38]
					+ 148 * mPress[39] + 169 * mPress[40] + 169 * mPress[41]
					+ 168 * mPress[42] + 169 * mPress[43] + 169 * mPress[44]
					+ 189 * mPress[45] + 187 * mPress[46] + 187 * mPress[47]
					+ 187 * mPress[48] + 186 * mPress[49] + 208 * mPress[50]
					+ 208 * mPress[51] + 206 * mPress[52] + 205 * mPress[53]
					+ 205 * mPress[54] + 227 * mPress[55] + 226 * mPress[56]
					+ 225 * mPress[57] + 225 * mPress[58] + 224 * mPress[59]
					+ 239 * mPress[60] + 241 * mPress[61] + 241 * mPress[62] + 236 * mPress[63])
					/ sum;
			CopInfo copInfo = new CopInfo();
			copInfo.setX(x_zuobiao);
			copInfo.setY(y_zuobiao);
			return copInfo;
		} else {
			return null;
		}
	}

	/**
	 * 摇一摇-根据右脚压力数据计算并记录cop点信息
	 * 
	 * @param mPress
	 * @return CopInfo实例
	 */
	public static CopInfo calRightCopZuobiao(short mPress[]) {
		short sum = 0; // 64点的压力总值
		for (int i = 0; i < mPress.length; i++) {
			sum = (short) (sum + mPress[i]);
		}
		if (sum != 0) {
			// cop点的x坐标算法是将64个点的每个点的x坐标乘以对应的压力值再求和，再除64点的压力总值（sum）。cop点的y坐标同理。
			int x_zuobiao = (16 * mPress[0] + 20 * mPress[1] + 36 * mPress[2]
					+ 50 * mPress[3] + 12 * mPress[4] + 22 * mPress[5]
					+ 34 * mPress[6] + 44 * mPress[7] + 55 * mPress[8]
					+ 9 * mPress[9] + 19 * mPress[10] + 31 * mPress[11]
					+ 43 * mPress[12] + 54 * mPress[13] + 65 * mPress[14]
					+ 9 * mPress[15] + 21 * mPress[16] + 34 * mPress[17]
					+ 46 * mPress[18] + 58 * mPress[19] + 69 * mPress[20]
					+ 16 * mPress[21] + 26 * mPress[22] + 36 * mPress[23]
					+ 48 * mPress[24] + 60 * mPress[25] + 71 * mPress[26]
					+ 26 * mPress[27] + 37 * mPress[28] + 48 * mPress[29]
					+ 60 * mPress[30] + 69 * mPress[31] + 31 * mPress[32]
					+ 40 * mPress[33] + 49 * mPress[34] + 58 * mPress[35]
					+ 66 * mPress[36] + 49 * mPress[37] + 52 * mPress[38]
					+ 65 * mPress[39] + 29 * mPress[40] + 38 * mPress[41]
					+ 46 * mPress[42] + 56 * mPress[43] + 64 * mPress[44]
					+ 27 * mPress[45] + 36 * mPress[46] + 46 * mPress[47]
					+ 55 * mPress[48] + 64 * mPress[49] + 25 * mPress[50]
					+ 35 * mPress[51] + 45 * mPress[52] + 54 * mPress[53]
					+ 64 * mPress[54] + 25 * mPress[55] + 35 * mPress[56]
					+ 44 * mPress[57] + 54 * mPress[58] + 63 * mPress[59]
					+ 26 * mPress[60] + 37 * mPress[61] + 47 * mPress[62] + 58 * mPress[63])
					/ sum;
			int y_zuobiao = (16 * mPress[0] + 12 * mPress[1] + 12 * mPress[2]
					+ 18 * mPress[3] + 31 * mPress[4] + 31 * mPress[5]
					+ 31 * mPress[6] + 31 * mPress[7] + 33 * mPress[8]
					+ 48 * mPress[9] + 49 * mPress[10] + 50 * mPress[11]
					+ 51 * mPress[12] + 52 * mPress[13] + 54 * mPress[14]
					+ 66 * mPress[15] + 72 * mPress[16] + 72 * mPress[17]
					+ 72 * mPress[18] + 72 * mPress[19] + 73 * mPress[20]
					+ 87 * mPress[21] + 87 * mPress[22] + 88 * mPress[23]
					+ 91 * mPress[24] + 91 * mPress[25] + 92 * mPress[26]
					+ 106 * mPress[27] + 107 * mPress[28] + 109 * mPress[29]
					+ 110 * mPress[30] + 112 * mPress[31] + 124 * mPress[32]
					+ 125 * mPress[33] + 128 * mPress[34] + 129 * mPress[35]
					+ 130 * mPress[36] + 148 * mPress[37] + 149 * mPress[38]
					+ 150 * mPress[39] + 169 * mPress[40] + 169 * mPress[41]
					+ 168 * mPress[42] + 169 * mPress[43] + 169 * mPress[44]
					+ 186 * mPress[45] + 187 * mPress[46] + 187 * mPress[47]
					+ 187 * mPress[48] + 189 * mPress[49] + 205 * mPress[50]
					+ 205 * mPress[51] + 206 * mPress[52] + 208 * mPress[53]
					+ 208 * mPress[54] + 224 * mPress[55] + 225 * mPress[56]
					+ 225 * mPress[57] + 226 * mPress[58] + 227 * mPress[59]
					+ 236 * mPress[60] + 241 * mPress[61] + 241 * mPress[62] + 239 * mPress[63])
					/ sum;
			CopInfo copInfo = new CopInfo();
			copInfo.setX(x_zuobiao);
			copInfo.setY(y_zuobiao);
			return copInfo;
		} else {
			return null;
		}
	}

	/**
	 * 摇一摇计算所有推送服务端的参数
	 * 
	 * @param doubleOpenCopList
	 *            双足睁眼cop集合
	 * @param singleOpenCopList
	 *            单足睁眼cop集合
	 * @param doubleCloseCopList
	 *            双足闭眼cop集合
	 * @param singleCloseCopList
	 *            单足闭眼cop集合
	 * @param calDoubleOpenCopList
	 *            用于计算的双足睁眼cop集合
	 * @param calSingleOpenCopList
	 *            用于计算的单足睁眼cop集合
	 * @param calDoubleCloseCopList
	 *            用于计算的双足闭眼cop集合
	 * @param calSingleCloseCopList
	 *            用于计算的单足睁眼cop集合
	 * @param second
	 * @return
	 */
	public static ShakeInfo calShakeInfo(List<CopInfo> doubleOpenCopList,
			List<CopInfo> singleOpenCopList, List<CopInfo> doubleCloseCopList,
			List<CopInfo> singleCloseCopList,
			List<CopInfo> calDoubleOpenCopList,
			List<CopInfo> calSingleOpenCopList,
			List<CopInfo> calDoubleCloseCopList,
			List<CopInfo> calSingleCloseCopList, int second) {
		ShakeInfo shakeInfo = new ShakeInfo(); // 创建ShakeInfo实例，用于存放上传至服务端的数据
		// 计算轨迹长
		float bo_totaltrail = CalDetection.calCopTrailLong(doubleOpenCopList);// 双脚睁眼轨迹长
		float so_totaltrail = CalDetection.calCopTrailLong(singleOpenCopList);// 单脚睁眼轨迹长
		float bc_totaltrail = CalDetection.calCopTrailLong(doubleCloseCopList);// 双脚闭眼轨迹长
		float sc_totaltrail = CalDetection.calCopTrailLong(singleCloseCopList);// 单脚闭眼轨迹长
		float bo_xoffset, bo_yoffset, so_xoffset, so_yoffset, bc_xoffset, bc_yoffset, sc_xoffset, sc_yoffset, bo_xsum = 0, bo_ysum = 0, so_xsum = 0, so_ysum = 0, bc_xsum = 0, bc_ysum = 0, sc_xsum = 0, sc_ysum = 0;// 双足睁眼x和y偏移、单足睁眼x和y偏移、双足闭眼x和y偏移、单足闭眼x和y偏移、双足睁眼x坐标累计之和，双足睁眼y坐标累计之和，单足睁眼x坐标累计之和，单足睁眼y坐标累计之和，双足闭眼x坐标累计之和，双足闭眼y坐标累计之和，单足闭眼x坐标累计之和，单足闭眼y坐标累计之和
		String bo_copStr = "", so_copStr = "", bc_copStr = "", sc_copStr = "";//定义双脚睁眼、闭眼，单足睁眼、闭眼的cop字符串
		for (int i = 0; i < doubleOpenCopList.size(); i++) {
			bo_xsum += doubleOpenCopList.get(i).getX();
			bo_ysum += doubleOpenCopList.get(i).getY();
			bo_copStr += doubleOpenCopList.get(i).getX() + ","
					+ doubleOpenCopList.get(i).getY() + "-";// 记录双足睁眼cop点，用-间隔
		}
		bo_copStr = bo_copStr.substring(0, bo_copStr.length() - 1);
		// Log.i(TAG, "bo_copStr" + bo_copStr);
		bo_xoffset = bo_xsum / doubleOpenCopList.size() - CopStandardView.cop_x;// 计算双足睁眼x轴重心偏移
		bo_yoffset = bo_ysum / doubleOpenCopList.size() - CopStandardView.cop_y;// 计算双足睁眼y轴重心偏移

		for (int i = 0; i < singleOpenCopList.size(); i++) {
			so_xsum += singleOpenCopList.get(i).getX();
			so_ysum += singleOpenCopList.get(i).getY();
			so_copStr += singleOpenCopList.get(i).getX() + ","
					+ singleOpenCopList.get(i).getY() + "-";// 记录单足睁眼cop点，用-间隔
		}
		so_copStr = so_copStr.substring(0, so_copStr.length() - 1);
		// Log.i(TAG, "so_copStr" + so_copStr);
		so_xoffset = so_xsum / singleOpenCopList.size() - CopStandardView.cop_x;// 计算单足睁眼x轴重心偏移
		so_yoffset = so_ysum / singleOpenCopList.size() - CopStandardView.cop_y;// 计算单足睁眼y轴重心偏移

		for (int i = 0; i < doubleCloseCopList.size(); i++) {
			bc_xsum += doubleCloseCopList.get(i).getX();
			bc_ysum += doubleCloseCopList.get(i).getY();
			bc_copStr += doubleCloseCopList.get(i).getX() + ","
					+ doubleCloseCopList.get(i).getY() + "-";// 记录单足闭眼cop点，用-间隔
		}
		bc_copStr = bc_copStr.substring(0, bc_copStr.length() - 1);
		// Log.i(TAG, "bc_copStr" + bc_copStr);
		bc_xoffset = bc_xsum / doubleCloseCopList.size()
				- CopStandardView.cop_x;// 计算双足闭眼x轴重心偏移
		bc_yoffset = bc_ysum / doubleCloseCopList.size()
				- CopStandardView.cop_y;// 计算双足闭眼y轴重心偏移

		for (int i = 0; i < singleCloseCopList.size(); i++) {
			sc_xsum += singleCloseCopList.get(i).getX();
			sc_ysum += singleCloseCopList.get(i).getY();
			sc_copStr += singleCloseCopList.get(i).getX() + ","
					+ singleCloseCopList.get(i).getY() + "-";// 记录单足闭眼cop点，用-间隔
		}
		sc_copStr = sc_copStr.substring(0, sc_copStr.length() - 1);
		// Log.i(TAG, "sc_copStr" + sc_copStr);
		sc_xoffset = sc_xsum / singleCloseCopList.size()
				- CopStandardView.cop_x;// 计算单足闭眼x轴重心偏移
		sc_yoffset = sc_ysum / singleCloseCopList.size()
				- CopStandardView.cop_y;// 计算单足闭眼y轴重心偏移

		// 计算外围面积
		float bo_outerarea = CalDetection.CalcConvexHull(calDoubleOpenCopList); // 双足睁眼外围面积
		float so_outerarea = CalDetection.CalcConvexHull(calSingleOpenCopList);// 单足睁眼外围面积
		float bc_outerarea = CalDetection.CalcConvexHull(calDoubleCloseCopList);// 双足闭眼外围面积
		float sc_outerarea = CalDetection.CalcConvexHull(calSingleCloseCopList);// 单足闭眼外围面积

		float bo_trailperarea = 0, bc_trailperarea = 0, so_trailperarea = 0, sc_trailperarea = 0, bo_romberg = 0, bc_romberg = 0, so_romberg = 0, sc_romberg = 0;//双足睁眼单位面积轨迹长、单足睁眼单位面积轨迹长、双足闭眼单位面积轨迹长、、单足闭眼单位面积轨迹长、双足睁眼、闭眼romberg率，单足睁眼、闭眼romberg率

		if (bo_outerarea != 0) {
			bo_trailperarea = bo_totaltrail / bo_outerarea;// 计算双足睁眼单位面积轨迹长
			bo_romberg = bc_outerarea / bo_outerarea;// 计算双足romberg率
			bc_romberg = bc_outerarea / bo_outerarea;// 计算双足romberg率
		}
		if (so_outerarea != 0) {
			so_trailperarea = so_totaltrail / so_outerarea;// 计算单足睁眼单位面积轨迹长
			so_romberg = sc_outerarea / so_outerarea;// 计算单足romberg率
			sc_romberg = sc_outerarea / so_outerarea;// 计算单足romberg率
		}
		if (bc_outerarea != 0) {
			bc_trailperarea = bc_totaltrail / bc_outerarea;// 计算双足闭眼单位面积轨迹长
		}
		if (sc_outerarea != 0) {
			sc_trailperarea = sc_totaltrail / sc_outerarea;// 计算单足闭眼单位面积轨迹长
		}

		// 计算单位时间轨迹长
		float bo_trailpersecond, so_trailpersecond, bc_trailpersecond, sc_trailpersecond;
		bo_trailpersecond = bo_totaltrail / second;// 双足睁眼
		so_trailpersecond = bc_totaltrail / second;// 单足睁眼
		bc_trailpersecond = so_totaltrail / second;// 双足闭眼
		sc_trailpersecond = sc_totaltrail / second;// 单足闭眼

		// 计算平衡指数
		String bo_balanceindex, bc_balanceindex, so_balanceindex, sc_balanceindex, compositeindex;//双足睁眼、闭眼，单足睁眼、闭眼的平衡指数
		bo_balanceindex = CalDetection.calBalanceindex(bo_totaltrail,
				bo_outerarea, bo_trailperarea, bo_trailpersecond, bo_xoffset,
				bo_yoffset, bo_romberg);// 双足睁眼
		bc_balanceindex = CalDetection.calBalanceindex(bc_totaltrail,
				bc_outerarea, bc_trailperarea, bc_trailpersecond, bc_xoffset,
				bc_yoffset, bc_romberg);// 单足睁眼
		so_balanceindex = CalDetection.calBalanceindex(so_totaltrail,
				so_outerarea, so_trailperarea, so_trailpersecond, so_xoffset,
				so_yoffset, so_romberg);// 双足闭眼
		sc_balanceindex = CalDetection.calBalanceindex(sc_totaltrail,
				sc_outerarea, sc_trailperarea, sc_trailpersecond, sc_xoffset,
				sc_yoffset, sc_romberg);// 单足闭眼

		compositeindex = CalDetection.calTotalBalanceIndex(bo_balanceindex,
				bc_balanceindex, so_balanceindex, sc_balanceindex);// 综合平衡指数

		shakeInfo.setBoTotalTrail(bo_totaltrail);
		shakeInfo.setSoTotalTrail(so_totaltrail);
		shakeInfo.setBcTotalTrail(bc_totaltrail);
		shakeInfo.setScTotalTrail(sc_totaltrail);
		shakeInfo.setBoOuterArea(bo_outerarea);
		shakeInfo.setSoOuterArea(so_outerarea);
		shakeInfo.setBcOuterArea(bc_outerarea);
		shakeInfo.setScOuterArea(sc_outerarea);
		shakeInfo.setBoXoffset(bo_xoffset);
		shakeInfo.setSoXoffset(so_xoffset);
		shakeInfo.setBcXoffset(bc_xoffset);
		shakeInfo.setScXoffset(sc_xoffset);
		shakeInfo.setBoYoffset(bo_yoffset);
		shakeInfo.setSoYoffset(so_yoffset);
		shakeInfo.setBcYoffset(bc_yoffset);
		shakeInfo.setScYoffset(sc_yoffset);
		shakeInfo.setBoXSum(bo_xsum);
		shakeInfo.setSoXSum(so_xsum);
		shakeInfo.setBcXSum(bc_xsum);
		shakeInfo.setScXSum(sc_xsum);
		shakeInfo.setBoYSum(bo_ysum);
		shakeInfo.setSoYSum(so_ysum);
		shakeInfo.setBcYSum(bc_ysum);
		shakeInfo.setScYSum(sc_ysum);
		shakeInfo.setBoTrailperarea(bo_trailperarea);
		shakeInfo.setSoTrailperarea(so_trailperarea);
		shakeInfo.setBcTrailperarea(bc_trailperarea);
		shakeInfo.setScTrailperarea(sc_trailperarea);
		shakeInfo.setBoRomberg(bo_romberg);
		shakeInfo.setSoRomberg(so_romberg);
		shakeInfo.setBcRomberg(bc_romberg);
		shakeInfo.setScRomberg(sc_romberg);
		shakeInfo.setBoTrailpersecond(bo_trailpersecond);
		shakeInfo.setSoTrailpersecond(so_trailpersecond);
		shakeInfo.setBcTrailpersecond(bc_trailpersecond);
		shakeInfo.setScTrailpersecond(sc_trailpersecond);
		shakeInfo.setBoCopStr(bo_copStr);
		shakeInfo.setSoCopStr(so_copStr);
		shakeInfo.setBcCopStr(bc_copStr);
		shakeInfo.setScCopStr(sc_copStr);
		shakeInfo.setBoBalanceindex(bo_balanceindex);
		shakeInfo.setSoBalanceindex(so_balanceindex);
		shakeInfo.setBcBalanceindex(bc_balanceindex);
		shakeInfo.setScBalanceindex(sc_balanceindex);
		shakeInfo.setCompositeindex(compositeindex);

		return shakeInfo;
	}

	/**
	 * 摇一摇-计算轨迹长
	 * 
	 * @param copList
	 *            cop点集合，包含多组点信息
	 * @return
	 */
	public static float calCopTrailLong(List<CopInfo> copList) {
		float copTrailLong = 0f;
		for (int i = 0; i < copList.size() - 1; i++) {
			// 计算相邻两个点的长度，利用勾股定理计算
			float mTrail = (float) Math.sqrt(Math.pow(copList.get(i + 1).getX()
					- copList.get(i).getX(), 2)
					+ Math.pow(copList.get(i + 1).getY()
							- copList.get(i).getY(), 2));
			copTrailLong += mTrail;
		}
		return copTrailLong;
	}

	/**
	 * 摇一摇-计算包络面积
	 * 
	 * @param copList
	 *            cop点集合
	 * @return 包络面积
	 */
	public static float CalcConvexHull(List<CopInfo> copList) {
		// 点集中至少应有3个点，才能构成多边形
		if (copList.size() < 3) {
			return 0f;
		}
		// 将第1个点预设为基点（最小点）
		CopInfo miniInfo = new CopInfo();
		miniInfo.setX(copList.get(0).getX());
		miniInfo.setY(copList.get(0).getY());
		// 查找基点
		for (int i = 1; i < copList.size(); i++) {
			if (copList.get(i).getY() < miniInfo.getY()
					|| (copList.get(i).getY() == miniInfo.getY() && copList
							.get(i).getX() < miniInfo.getX())) {
				miniInfo.setX(copList.get(i).getX());
				miniInfo.setY(copList.get(i).getY());
			}
		}
		// 计算出各点与基点构成的向量
		for (int i = 0; i != copList.size();) {
			if (copList.get(i).getX() == miniInfo.getX()
					&& copList.get(i).getY() == miniInfo.getY()) {
				copList.remove(i);
			} else {
				copList.get(i).setX(copList.get(i).getX() - miniInfo.getX());
				copList.get(i).setY(copList.get(i).getY() - miniInfo.getY());
				++i;
			}
		}
		// 按各向量与横坐标之间的夹角排序
		comPare(copList);
		// 删除相同的向量
		for (int i = 0; i < copList.size() - 1; i++) {
			if (copList.get(i).getX() == copList.get(i + 1).getX()
					&& copList.get(i).getY() == copList.get(i + 1).getY()) {
				copList.remove(i + 1);
			}
		}
		// 计算得到首尾依次相联的向量
		for (int i = copList.size() - 1; i != 0; i--) {
			int j = i - 1;
			copList.get(i).setX(copList.get(i).getX() - copList.get(j).getX());
			copList.get(i).setY(copList.get(i).getY() - copList.get(j).getY());
		}
		// 依次删除不在凸包上的向量
		for (int i = 1; i != copList.size(); i++) {
			for (int j = i - 1; j != 0;) {
				float v1 = copList.get(i).getX() * copList.get(j).getY(), v2 = copList
						.get(i).getY() * copList.get(j).getX(); // 计算叉积
				// 如果叉积小于0，则无没有逆向旋转
				// 如果叉积等于0，还需判断方向是否相逆
				if (v1 < v2
						|| (v1 == v2
								&& copList.get(i).getX()
										* copList.get(j).getX() > 0 && copList
								.get(i).getY() * copList.get(j).getY() > 0))
					break;
				// 删除前一个向量后，需更新当前向量，与前面的向量首尾相连
				// 向量三角形计算公式
				copList.get(i).setX(
						copList.get(i).getX() + copList.get(j).getX());
				copList.get(i).setY(
						copList.get(i).getY() + copList.get(j).getY());
				copList.remove(j);
				i = j;
				j = j - 1;
			}
		}
		// 将所有首尾相连的向量依次累加，换算成坐标
		copList.get(0).setX(copList.get(0).getX() + miniInfo.getX());
		copList.get(0).setY(copList.get(0).getY() + miniInfo.getY());
		for (int i = 1; i != copList.size(); i++) {
			copList.get(i).setX(
					copList.get(i).getX() + copList.get(i - 1).getX());
			copList.get(i).setY(
					copList.get(i).getY() + copList.get(i - 1).getY());
		}
		copList.add(miniInfo);
		float area = calcCHArea(copList);
		return area;
	}

	/**
	 * 摇一摇-比较向量中哪个与x轴向量(1, 0)的夹角更大
	 * 
	 * @param copList
	 *            cop点集合
	 */
	private static void comPare(List<CopInfo> copList) {
		CopInfo tmpCopInfo; // 记录临时中间值
		int size = copList.size(); // 数组大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				float m1 = (float) Math.sqrt(copList.get(i).getX()
						* copList.get(i).getX() + copList.get(i).getY()
						* copList.get(i).getY());
				float m2 = (float) Math.sqrt(copList.get(j).getX()
						* copList.get(j).getX() + copList.get(j).getY()
						* copList.get(j).getY());
				float v1 = copList.get(i).getX() / m1;
				float v2 = copList.get(j).getX() / m2;
				if (v1 < v2 || (v1 == v2 && m1 > m2)) { // 交换两数的位置
					tmpCopInfo = copList.get(i);
					copList.get(i).setX(copList.get(j).getX());
					copList.get(i).setY(copList.get(j).getY());
					copList.get(j).setX(tmpCopInfo.getX());
					copList.get(j).setY(tmpCopInfo.getY());
				}
			}
		}
	}

	/**
	 * 摇一摇-计算凸包面积
	 * 
	 * @param copList
	 *            cop点集合
	 */
	private static float calcCHArea(List<CopInfo> copList) {
		float area = 0.0f;
		for (int i = 2; i < copList.size(); i++) {
			float a, b, c, T;
			// 计算边长a，b，c
			a = (float) Math.sqrt((copList.get(i).getX() - copList.get(0)
					.getX())
					* (copList.get(i).getX() - copList.get(0).getX())
					+ (copList.get(i).getY() - copList.get(0).getY())
					* (copList.get(i).getY() - copList.get(0).getY()));

			b = (float) Math.sqrt((copList.get(i - 1).getX() - copList.get(0)
					.getX())
					* (copList.get(i - 1).getX() - copList.get(0).getX())
					+ (copList.get(i - 1).getY() - copList.get(0).getY())
					* (copList.get(i - 1).getY() - copList.get(0).getY()));

			c = (float) Math.sqrt((copList.get(i).getX() - copList.get(i - 1)
					.getX())
					* (copList.get(i).getX() - copList.get(i - 1).getX())
					+ (copList.get(i).getY() - copList.get(i - 1).getY())
					* (copList.get(i).getY() - copList.get(i - 1).getY()));
			// 计算半周长T
			T = (a + b + c) / 2;

			area += Math.sqrt(T * (T - a) * (T - b) * (T - c)); // 通过三边和半周长求面积(海伦公式)
		}
		return area;
	}

	/**
	 * 摇一摇-计算四种情况指数
	 * 
	 * @param totaltrail
	 *            总轨迹长
	 * @param outerarea
	 *            外围面积
	 * @param trailperarea
	 *            单位面积轨迹
	 * @param trailpersecond
	 *            单位时间轨迹
	 * @param xoffset
	 *            x偏移
	 * @param yoffset
	 *            y偏移
	 * @param romberg
	 *            romberg率
	 * @return 指数
	 */
	public static String calBalanceindex(float totaltrail, float outerarea,
			float trailperarea, float trailpersecond, float xoffset,
			float yoffset, float romberg) {
		int good = 0, normal = 0, bad = 0;//定义好，中，坏，用于统计哪个多
		if (totaltrail <= 25) {
			good++;
		} else if (totaltrail > 25 && totaltrail <= 120) {
			normal++;
		} else {
			bad++;
		}
		if (outerarea <= 0.5) {
			good++;
		} else if (outerarea > 0.5 && outerarea <= 8) {
			normal++;
		} else {
			bad++;
		}
		if (trailperarea <= 1) {
			good++;
		} else if (trailperarea > 1 && trailperarea <= 80) {
			normal++;
		} else {
			bad++;
		}
		if (trailpersecond <= 0.15) {
			good++;
		} else if (trailpersecond > 0.15 && trailpersecond <= 1.5) {
			normal++;
		} else {
			bad++;
		}
		if (xoffset < 0) {
			xoffset = -xoffset;
		}
		if (xoffset <= 1.3) {
			good++;
		} else if (xoffset > 1.3 && xoffset <= 6) {
			normal++;
		} else {
			bad++;
		}
		if (yoffset < 0) {
			yoffset = -yoffset;
		}
		if (yoffset <= 0.9) {
			good++;
		} else if (yoffset > 0.9 && yoffset <= 2.6) {
			normal++;
		} else {
			bad++;
		}
		if (romberg <= 50) {
			good++;
		} else if (romberg > 50 && romberg <= 205) {
			normal++;
		} else {
			bad++;
		}
		//哪个最大，结果就为哪个
		if (good >= normal && good >= bad) {
			return "优秀";
		} else if (normal >= bad) {
			return "正常";
		} else {
			return "弱";
		}

	}

	/**
	 * 摇一摇-计算综合指数
	 * 
	 * @param bo_balanceindex
	 *            双脚睁眼平衡指数
	 * @param bc_balanceindex
	 *            双脚闭眼平衡指数
	 * @param so_balanceindex
	 *            单脚睁眼平衡指数
	 * @param sc_balanceindex
	 *            单脚闭眼平衡指数
	 * @return 综合平衡指数
	 */
	public static String calTotalBalanceIndex(String bo_balanceindex,
			String bc_balanceindex, String so_balanceindex,
			String sc_balanceindex) {

		int good = 0, normal = 0, bad = 0;
		if (bo_balanceindex.equals("优秀")) {
			good++;
		} else if (bo_balanceindex.equals("正常")) {
			normal++;
		} else if (bo_balanceindex.equals("弱")) {
			bad++;
		}
		if (bc_balanceindex.equals("优秀")) {
			good++;
		} else if (bc_balanceindex.equals("正常")) {
			normal++;
		} else if (bc_balanceindex.equals("弱")) {
			bad++;
		}
		if (so_balanceindex.equals("优秀")) {
			good++;
		} else if (so_balanceindex.equals("正常")) {
			normal++;
		} else if (so_balanceindex.equals("弱")) {
			bad++;
		}
		if (sc_balanceindex.equals("优秀")) {
			good++;
		} else if (sc_balanceindex.equals("正常")) {
			normal++;
		} else if (sc_balanceindex.equals("弱")) {
			bad++;
		}
		if (good >= normal && good >= bad) {
			return "优秀";
		} else if (normal >= bad) {
			return "正常";
		} else {
			return "弱";
		}

	}

	/**
	 * 摇一摇-报告雷达图
	 * 
	 * @param trailpersecond
	 *            单位时间轨迹长
	 * @param xoffset
	 *            x偏移
	 * @param yoffset
	 *            y偏移
	 * @param romberg
	 *            romberg率
	 * @param outerarea
	 *            外围面积
	 * @param trailperarea
	 *            单位面积轨迹长
	 * @param high
	 *            雷达图中心到顶点长
	 * @param med
	 *            雷达图中心到中点长
	 * @param low
	 *            雷达图中心到最低点长
	 * @return
	 */
	public static float[] calShakeRadar(float trailpersecond, float xoffset,
			float yoffset, float romberg, float outerarea, float trailperarea,
			float high, float med, float low) {
		float[] radarData = new float[6];
		//计算单位时间轨迹长的雷达图的长度
		// 0-0.15 0.15-1.5 1.5+
		if (trailpersecond >= 0 && trailpersecond <= 0.15) {
			radarData[0] = (float) ((high - med) * (trailpersecond - 0.15)
					/ (0 - 0.15) + med);
		} else if (trailpersecond > 0.15 && trailpersecond <= 1.5) {
			radarData[0] = (float) ((med - low) * (trailpersecond - 1.5)
					/ (0.15 - 1.5) + low);
		} else if (trailpersecond > 1.5) {
			radarData[0] = (float) ((low - 0) * (trailpersecond - 500)
					/ (1.5 - 500) + 0);
		}
		xoffset = Math.abs(xoffset);
		//计算x偏移雷达图的长度
		// 0-1.3 1.3-6 6+
		if (xoffset >= 0 && xoffset <= 1.3) {
			radarData[1] = (float) ((high - med) * (xoffset - 1.3) / (0 - 1.3) + med);
		} else if (xoffset > 1.3 && xoffset <= 6) {
			radarData[1] = (float) ((med - low) * (xoffset - 6) / (1.3 - 6) + low);
		} else if (xoffset > 6) {
			radarData[1] = (float) ((low - 0) * (xoffset - 10) / (6 - 10) + 0);
		}
		//计算y偏移雷达图的长度
		yoffset = Math.abs(yoffset);
		// 0-0.9 0.9-2.6 2.6+
		if (yoffset >= 0 && yoffset <= 0.9) {
			radarData[2] = (float) ((high - med) * (yoffset - 0.9) / (0 - 0.9) + med);
		} else if (yoffset > 0.9 && yoffset <= 2.6) {
			radarData[2] = (float) ((med - low) * (yoffset - 2.6) / (0.9 - 2.6) + low);
		} else if (yoffset > 2.6) {
			radarData[2] = (float) ((low - 0) * (yoffset - 100) / (2.6 - 100) + 0);
		}
		//计算romberg率的雷达图的长度
		// 0-50 50-205 205+
		if (romberg >= 0 && romberg <= 50) {
			radarData[3] = (float) ((high - med) * (romberg - 50) / (0 - 50) + med);
		} else if (romberg > 50 && romberg <= 205) {
			radarData[3] = (float) ((med - low) * (romberg - 205) / (50 - 205) + low);
		} else if (romberg > 205) {
			radarData[3] = (float) ((low - 0) * (romberg - 5000) / (205 - 5000) + 0);
		}
		//计算外围面积的雷达图的长度
		// 0-0.5 0.5-8 8+
		if (outerarea >= 0 && outerarea <= 0.5) {
			radarData[4] = (float) ((high - med) * (outerarea - 0.5)
					/ (0 - 0.5) + med);
		} else if (outerarea > 0.5 && outerarea <= 8) {
			radarData[4] = (float) ((med - low) * (outerarea - 8) / (0.5 - 8) + low);
		} else if (outerarea > 8) {
			radarData[4] = (float) ((low - 0) * (outerarea - 2000) / (8 - 2000) + 0);
		}
		//计算单位面积轨迹长的雷达图的长度
		// 0-1 1-80 80+
		if (trailperarea >= 0 && trailperarea <= 1) {
			radarData[5] = (float) ((high - med) * (trailperarea - 1) / (0 - 1) + med);
		} else if (trailperarea > 1 && trailperarea <= 80) {
			radarData[5] = (float) ((med - low) * (trailperarea - 80)
					/ (1 - 80) + low);
		} else if (trailperarea > 80) {
			radarData[5] = (float) ((low - 0) * (trailperarea - 5000)
					/ (80 - 5000) + 0);
		}
		return radarData;
	}

	/**
	 * 走一走-提取特征数据
	 *
	 * @param originList
	 *            测试期间蓝牙上报的数据
	 * @param name
	 *            "left" or "right"
	 * @return
	 */
	public static StepModel calCharacterValue(List<PressData> originList,
			String name) {
		List<PressCharacterModel> characterList = new ArrayList<PressCharacterModel>();
		int stepNum = 0;
		// 数值非常大的坏点置0
		for (int i = 0; i < 64; i++) {
			boolean failDot = false;
			for (int j = 0; j < originList.size(); j++) {
				if (originList.get(j).getPressData()[i] > 250) {
					failDot = true;
				} else {
					failDot = false;
					break;
				}
			}
			if (failDot) {
				Log.i(TAG, "第" + i + "个点是坏的，置0");
				for (int j = 0; j < originList.size(); j++) {
					originList.get(j).getPressData()[i] = 0;
				}
			}
		}
		boolean backSuc = false;
		// 通过压上的点的数值计算是前脚尖还是后脚跟
		for (int i = 0; i < originList.size() - 1; i++) {
			int frontCount = 0, backCount = 0, nextFrontCount = 0, nextBackCount = 0;
			if (originList.get(i).getPressData()[2] > 150) {
				frontCount++;
			}
			if (originList.get(i).getPressData()[3] > 150) {
				frontCount++;
			}
			if (originList.get(i).getPressData()[6] > 150) {
				frontCount++;
			}
			if (originList.get(i).getPressData()[7] > 150) {
				frontCount++;
			}
			if (originList.get(i).getPressData()[8] > 150) {
				frontCount++;
			}
			if (originList.get(i).getPressData()[56] > 150) {
				backCount++;
			}
			if (originList.get(i).getPressData()[57] > 150) {
				backCount++;
			}
			if (originList.get(i).getPressData()[58] > 150) {
				backCount++;
			}
			if (originList.get(i).getPressData()[61] > 150) {
				backCount++;
			}
			if (originList.get(i).getPressData()[62] > 150) {
				backCount++;
			}

			if (originList.get(i + 1).getPressData()[2] > 150) {
				nextFrontCount++;
			}
			if (originList.get(i + 1).getPressData()[3] > 150) {
				nextFrontCount++;
			}
			if (originList.get(i + 1).getPressData()[6] > 150) {
				nextFrontCount++;
			}
			if (originList.get(i + 1).getPressData()[7] > 150) {
				nextFrontCount++;
			}
			if (originList.get(i + 1).getPressData()[8] > 150) {
				nextFrontCount++;
			}
			if (originList.get(i + 1).getPressData()[56] > 150) {
				nextBackCount++;
			}
			if (originList.get(i + 1).getPressData()[57] > 150) {
				nextBackCount++;
			}
			if (originList.get(i + 1).getPressData()[58] > 150) {
				nextBackCount++;
			}
			if (originList.get(i + 1).getPressData()[61] > 150) {
				nextBackCount++;
			}
			if (originList.get(i + 1).getPressData()[62] > 150) {
				nextBackCount++;
			}
			if (!backSuc) {
				// 后脚跟判断方法
				if (frontCount < 2 && backCount >= 2) {
					PressCharacterModel pressModel = new PressCharacterModel();
					pressModel.setTime(originList.get(i).getTime());
					pressModel.setName("后脚跟");
					backSuc = true;
					characterList.add(pressModel);
					stepNum++;
				}
			} else {
				if (nextFrontCount >= 2 && nextBackCount < 2) {

				} else {
					// 前脚尖判断方法
					if (frontCount >= 2 && backCount < 2) {
						PressCharacterModel pressModel = new PressCharacterModel();
						pressModel.setTime(originList.get(i).getTime());
						pressModel.setName("前脚尖");
						backSuc = false;
						characterList.add(pressModel);
					}
				}
			}
		}
		Log.i(TAG, name + "characterListl.size=" + characterList.size());
		// 保证第一个值是后脚跟，最后一个是前脚尖
		if (characterList.size() > 0
				&& characterList.get(0).getName().equals("前脚尖")) {
			characterList.remove(0);
		}
		if (characterList.size() > 0
				&& characterList.get(characterList.size() - 1).getName()
						.equals("后脚跟")) {
			characterList.remove(characterList.size() - 1);
		}
		Log.i(TAG,
				"调整后" + name + "characterList.size()=" + characterList.size());
		// for (int i = 0; i < characterList.size(); i++) {
		// Log.i(TAG, characterList.get(i).getName());
		// Log.i(TAG, "front:" + characterList.get(i).getFrontValue());
		// Log.i(TAG, "med:" + characterList.get(i).getMedValue());
		// Log.i(TAG, "back:" + characterList.get(i).getBackValue());
		// Log.i(TAG, "time:" + characterList.get(i).getTime());
		// Log.i(TAG, "");
		// }
		StepModel stepModel = new StepModel();
		stepModel.setCharacterList(characterList);
		stepModel.setStepNum(stepNum);
		return stepModel;
	}

	/**
	 * 走一走-计算步伐、步幅时间
	 * 
	 * @param leftCharacterList
	 *            左脚特征数据集合
	 * @param rightCharacterList
	 *            右脚特征数据集合
	 * @return WalkInfo实例，包含走一走报告需要的除步频外的所有数据
	 */
	public static WalkInfo calStepTime(
			List<PressCharacterModel> leftCharacterList,
			List<PressCharacterModel> rightCharacterList) {
		float leftPaceTime = 0, leftStrideTime = 0, leftSinglePhaseTime = 0, leftDoublePhaseTime = 0, leftStandPhaseTime = 0, leftSwingPhaseTime = 0, rightPaceTime = 0, rightStrideTime = 0, rightSinglePhaseTime = 0, rightDoublePhaseTime = 0, rightStandPhaseTime = 0, rightSwingPhaseTime = 0;
		WalkInfo walkInfo = new WalkInfo();// 存放最终数据

		long[] leftIntervalTime = new long[3]; // 存放最稳定的步幅组成的起止时间 起始时间，终止时间，次数
		long[] rightIntervalTime = new long[3];// 存放最稳定的步幅组成的起止时间 起始时间，终止时间，次数
		leftIntervalTime = compareIntervalTime(leftCharacterList, "left");
		rightIntervalTime = compareIntervalTime(rightCharacterList, "right");

		Log.i(TAG, "左脚：时间间隔出现最多的次数为" + leftIntervalTime[2] + "起始时间："
				+ leftIntervalTime[0] + "终止时间：" + leftIntervalTime[1]);
		Log.i(TAG, "右脚：时间间隔出现最多的次数为" + rightIntervalTime[2] + "起始时间："
				+ rightIntervalTime[0] + "终止时间：" + rightIntervalTime[1]);

		int leftCount = 0, rightCount = 0, leftSingleCount = 0, rightSingleCount = 0, leftStandCount = 0, rightStandCount = 0, paceCount = 0;
		long leftSingleTime = 0, rightSingleTime = 0, leftStandTime = 0, rightStandTime = 0, leftPaceSumTime = 0, rightPaceSumTime = 0, leftDoubleTime = 0, rightDoubleTime = 0;

		// 计算步幅、单支撑、摆动相、站立相
		// 计算左脚后脚跟个数
		for (int i = 0; i < leftCharacterList.size(); i++) {
			if (leftCharacterList.get(i).getTime() >= leftIntervalTime[0]
					&& leftCharacterList.get(i).getTime() <= leftIntervalTime[1]
					&& leftCharacterList.get(i).getName().equals("后脚跟")) {
				leftCount++;
			}
		}
		for (int i = 0; i < leftCharacterList.size(); i++) {
			if (leftCharacterList.get(i).getTime() >= leftIntervalTime[0]
					&& leftCharacterList.get(i).getTime() <= leftIntervalTime[1]
					&& leftCharacterList.get(i).getName().equals("前脚尖")) {
				// 计算右脚单支撑时间之和
				if (i + 1 < leftCharacterList.size()) {
					rightSingleCount++;
					rightSingleTime += leftCharacterList.get(i + 1).getTime()
							- leftCharacterList.get(i).getTime();
				}
				// 计算左脚站立时间之和
				if (i >= 1) {
					leftStandCount++;
					leftStandTime += leftCharacterList.get(i).getTime()
							- leftCharacterList.get(i - 1).getTime();
				}
			}
		}
		// 计算右脚后脚跟个数
		for (int i = 0; i < rightCharacterList.size(); i++) {
			if (rightCharacterList.get(i).getTime() >= rightIntervalTime[0]
					&& rightCharacterList.get(i).getTime() <= rightIntervalTime[1]
					&& rightCharacterList.get(i).getName().equals("后脚跟")) {
				rightCount++;
			}

		}
		for (int i = 0; i < rightCharacterList.size(); i++) {
			if (rightCharacterList.get(i).getTime() >= rightIntervalTime[0]
					&& rightCharacterList.get(i).getTime() <= rightIntervalTime[1]
					&& rightCharacterList.get(i).getName().equals("前脚尖")) {
				// 计算左脚单支撑时间之和
				if (i + 1 < rightCharacterList.size()) {
					leftSingleCount++;
					leftSingleTime += rightCharacterList.get(i + 1).getTime()
							- rightCharacterList.get(i).getTime();
				}
				// 计算左脚站立时间之和
				if (i >= 1) {
					rightStandCount++;
					rightStandTime += rightCharacterList.get(i).getTime()
							- rightCharacterList.get(i - 1).getTime();
				}
			}
		}
		// 计算步幅时间
		leftStrideTime = (leftIntervalTime[1] - leftIntervalTime[0])
				/ leftCount;
		rightStrideTime = (rightIntervalTime[1] - rightIntervalTime[0])
				/ rightCount;

		// 计算站立相时间
		leftStandPhaseTime = leftStandTime / leftStandCount;
		rightStandPhaseTime = rightStandTime / rightStandCount;

		// 计算单支撑时间
		leftSinglePhaseTime = leftStrideTime - leftStandPhaseTime;
		rightSinglePhaseTime = rightStrideTime - rightStandPhaseTime;

		// 计算摆动相时间
		leftSwingPhaseTime = leftSinglePhaseTime;
		rightSwingPhaseTime = rightSinglePhaseTime;

		// 计算双支撑时间
		leftDoublePhaseTime = (leftStandPhaseTime - leftSwingPhaseTime
				+ rightStandPhaseTime - rightSwingPhaseTime) / 4;
		rightDoublePhaseTime = leftDoublePhaseTime;

		// 计算步伐时间
		leftPaceTime = leftStandPhaseTime - leftDoublePhaseTime;
		rightPaceTime = rightStandPhaseTime - rightDoublePhaseTime;

		walkInfo.setLeftPaceTime(leftPaceTime);
		walkInfo.setLeftStrideTime(leftStrideTime);
		walkInfo.setLeftSinglePhaseTime(leftSinglePhaseTime);
		walkInfo.setLeftDoublePhaseTime(leftDoublePhaseTime);
		walkInfo.setLeftStandPhaseTime(leftStandPhaseTime);
		walkInfo.setLeftSwingPhaseTime(leftSwingPhaseTime);

		walkInfo.setRightPaceTime(rightPaceTime);
		walkInfo.setRightStrideTime(rightStrideTime);
		walkInfo.setRightSinglePhaseTime(rightSinglePhaseTime);
		walkInfo.setRightDoublePhaseTime(rightDoublePhaseTime);
		walkInfo.setRightStandPhaseTime(rightStandPhaseTime);
		walkInfo.setRightSwingPhaseTime(rightSwingPhaseTime);

		return walkInfo;
	}

	/**
	 * 计算最稳定的步幅时间段（双脚都使用该方法）
	 * 
	 * @param leftCharacterList
	 *            特征集合
	 * @param name
	 *            “left”或“right”
	 * @return
	 */
	private static long[] compareIntervalTime(
			List<PressCharacterModel> leftCharacterList, String name) {
		long[] leftIntervalTime = new long[3];

		// 算出间隔时间,也就是两组脚后跟的间隔时间
		List<PressStepModel> leftPressStepList = new ArrayList<PressStepModel>();
		Log.i(TAG, name + "的第一个数据:" + leftCharacterList.get(0).getName()
				+ "    时间:" + leftCharacterList.get(0).getTime());
		if (leftCharacterList.get(0).getName().equals("后脚跟")) {
			for (int j = 2; j < leftCharacterList.size(); j = j + 2) {
				long time = (leftCharacterList.get(j).getTime() - leftCharacterList
						.get(j - 2).getTime());
				long startTime = leftCharacterList.get(j - 2).getTime();
				PressStepModel pressStepModel = new PressStepModel();
				pressStepModel.setStrideTimeL(time);
				pressStepModel.setStartTime(startTime);
				leftPressStepList.add(pressStepModel);
			}
			for (int i = 0; i < leftPressStepList.size(); i++) {
				Log.i(TAG, leftPressStepList.get(i).getStartTime() + "-----"
						+ leftPressStepList.get(i).getStrideTimeL());
			}
		}

		// 计算出时间间隔及对应的次数，目的：后面比较哪组时间间隔的数量最多（所占比例最高）
		List<WalkTimeNumInfo> leftWalkTimeNumList = new ArrayList<WalkTimeNumInfo>(); // 存放时间间隔和对应出现的次数。目的：后面需要用它比较哪个时间间隔的数量最多
		long startTime = leftPressStepList.get(0).getStartTime();// 设定第一组起始时间为起始时间
		long finishTime = leftPressStepList.get(0).getStartTime()
				+ leftPressStepList.get(0).getStrideTimeL();// 设定第一组终止时间为结束时间
		int count = 1;// 用来统计这个间隔连续出现的次数
		int num = 0;// 用来记录这是leftWalkTimeNumList的第几个元素
		WalkTimeNumInfo walkTimeNumInfo = new WalkTimeNumInfo();
		walkTimeNumInfo.setStartTime(startTime);
		walkTimeNumInfo.setFinishTime(finishTime);
		walkTimeNumInfo.setStepCount(count);
		leftWalkTimeNumList.add(walkTimeNumInfo);
		for (int i = 1; i < leftPressStepList.size(); i++) {
			// 如果相邻两组间隔时间之差小于某值，认为属于正常误差，数量累加1。否则，开始统计新的时间间隔次数
			if (Math.abs(leftPressStepList.get(i).getStrideTimeL()
					- leftPressStepList.get(i - 1).getStrideTimeL()) <= 250) {
				Log.i(TAG,
						"符合小于250,结束时间为： 起始时间"
								+ leftPressStepList.get(i).getStartTime()
								+ "    步幅时间"
								+ leftPressStepList.get(i).getStrideTimeL()
								+ "="
								+ (leftPressStepList.get(i).getStartTime() + leftPressStepList
										.get(i).getStrideTimeL()) + "。");
				count++;
				leftWalkTimeNumList.get(num).setStepCount(count);
				leftWalkTimeNumList.get(num).setFinishTime(
						leftPressStepList.get(i).getStartTime()
								+ leftPressStepList.get(i).getStrideTimeL());
			} else {
				if (i != leftPressStepList.size() - 1) {
					i++;
					num++;
					count = 1;
					WalkTimeNumInfo walkTimeNumInfo1 = new WalkTimeNumInfo();
					Log.i(TAG, "重新计算时间，起始时间"
							+ leftPressStepList.get(i).getStartTime());
					walkTimeNumInfo1.setStartTime(leftPressStepList.get(i)
							.getStartTime());
					walkTimeNumInfo1.setFinishTime(leftPressStepList.get(i)
							.getStartTime()
							+ leftPressStepList.get(i).getStrideTimeL());
					walkTimeNumInfo1.setStepCount(count);
					leftWalkTimeNumList.add(walkTimeNumInfo1);
				}
			}
		}

		// 比较哪组时间间隔的数量最多（所占比例最高）
		int maxIndex = 0; // 存放集合序号
		for (int i = 1; i < leftWalkTimeNumList.size(); i++) {
			if (leftWalkTimeNumList.get(i).getStepCount() > leftWalkTimeNumList
					.get(maxIndex).getStepCount()) {
				maxIndex = i;
			}
		}

		// 得出数量最多的步伐时间组成的起止时间和步幅次数
		leftIntervalTime[0] = leftWalkTimeNumList.get(maxIndex).getStartTime();
		leftIntervalTime[1] = leftWalkTimeNumList.get(maxIndex).getFinishTime();
		leftIntervalTime[2] = leftWalkTimeNumList.get(maxIndex).getStepCount();

		return leftIntervalTime;
	}
}
