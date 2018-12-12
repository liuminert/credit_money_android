package com.tesu.creditgold.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tesu.creditgold.R;

public class DisableShopActivity extends Activity {
    private TextView tv_telephone;
    private TextView tv_top_back;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_shop);

        tv_telephone = (TextView) findViewById(R.id.tv_telephone);
        tv_top_back = (TextView) findViewById(R.id.tv_top_back);

        tv_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = tv_telephone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
        tv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
