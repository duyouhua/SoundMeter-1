package com.maple.audiometry.activity;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.maple.audiometry.R;

/**
 * 测试结果
 * 
 * @author shaoshuai
 * 
 */
public class EndActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.bt_end)
	private Button bt_end;// 退出
	@ViewInject(R.id.tv_check_result)
	private TextView tv_check_result;// 检测结果
	@ViewInject(R.id.tv_propose)
	private TextView tv_propose;// 分析与建议

	/** 听力等级 */
	private String[] hearingRank;
	/** 分析建议的说明文字 */
	private String[] dbExplain;
	int fbl[];
	int fbr[];
	double left, right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		ViewUtils.inject(this);

		Intent intent = getIntent();
		fbl = intent.getIntArrayExtra("left");
		fbr = intent.getIntArrayExtra("right");

		bt_end.setOnClickListener(this);

		initView();

	}

	private void initView() {
		hearingRank = getResources().getStringArray(R.array.hearing_rank_arr);
		dbExplain = getResources().getStringArray(R.array.propose_arr);

		// 给出平均听力=（1000Hz测试的结果+2000Hz测试得到结果+500Hz测试得到结果）/3
		left = (fbl[0] + fbl[1] + fbl[4]) / 3.0;
		right = (fbr[0] + fbr[1] + fbr[4]) / 3.0;
		DecimalFormat df = new DecimalFormat("##.00");

		int leftRank = computeRank(left);
		int rightRank = computeRank(right);
		// 结果
		String leftStr = "左耳：" + df.format(left) + "(" + hearingRank[leftRank]
				+ ")";
		String rightStr = "右耳：" + df.format(right) + "("
				+ hearingRank[rightRank] + ")";
		tv_check_result.setText(leftStr + "\n" + rightStr);
		// 分析和建议
		if (leftRank == rightRank) {
			tv_propose.setText("双耳：\n" + dbExplain[leftRank]);
		} else {
			tv_propose.setText("左耳：\n" + dbExplain[leftRank] + "\n右耳：\n"
					+ dbExplain[rightRank]);
		}
	}

	/**
	 * 根据平均听力计算听力等级
	 * 
	 * @param dous
	 * @return
	 */
	private int computeRank(double dous) {
		int rank = 0;
		if (dous <= 25) {
			rank = 0;
		} else if (dous > 25 && dous <= 40) {// 轻度
			rank = 1;
		} else if (dous > 40 && dous <= 55) {// 中度
			rank = 2;
		} else if (dous > 55 && dous <= 70) {// 中重度
			rank = 3;
		} else if (dous > 70 && dous <= 90) {// 重度
			rank = 4;
		} else if (dous > 90) {// 极重度
			rank = 5;
		}
		return rank;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bt_end:// 退出
			toMainPager();
			break;
		default:
			break;
		}

	}

	/**
	 * 返回主界面
	 */
	private void toMainPager() {
		finish();
		Intent intent = new Intent(EndActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}