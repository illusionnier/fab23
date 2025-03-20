package com.fabryka.apfabryka;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NNInternet extends AppCompatActivity {
    public String android_id;
    public Integer ccount = 0;
    public TextView textMyASID;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_nninternet);
        this.android_id = Settings.Secure.getString(getContentResolver(), "android_id");
        TextView textView = (TextView) findViewById(R.id.textMyASID);
        this.textMyASID = textView;
        textView.setText("ВАШ ИДЕНТИФИКАТОР = " + this.android_id);
    }

    public void MyReturn(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void MySetup(View view) {
        Integer valueOf = Integer.valueOf(this.ccount.intValue() + 1);
        this.ccount = valueOf;
        if (valueOf.intValue() == 5) {
            this.ccount = 0;
            startActivity(new Intent(this, NNSetting.class));
        }
    }
}
