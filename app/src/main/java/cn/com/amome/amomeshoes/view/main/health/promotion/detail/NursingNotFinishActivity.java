package cn.com.amome.amomeshoes.view.main.health.promotion.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.com.amome.amomeshoes.R;

public class NursingNotFinishActivity extends Activity {
    private Context mContext;
    private String TAG = "NursingNotFinishActivity";
    private String day;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_not_finish);
        mContext = this;
        //day = getIntent().getStringExtra("day");
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
