package com.fabryka.apfabryka;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NNSetting extends AppCompatActivity {
    public String MyTextUrl;
    public String android_id;
    public EditText txtMyUrl;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.android_id = Settings.Secure.getString(getContentResolver(), "android_id");
        setContentView((int) R.layout.activity_nnsetting);
        this.txtMyUrl = (EditText) findViewById(R.id.editMyUrl);
    }

    public void ListMyUrl(View view) {
        this.MyTextUrl = "https://ms.fabryka.ru/";
        PersistantStorage.init(this);
        String property = PersistantStorage.getProperty("MyTextUrl");
        this.MyTextUrl = property;
        if (!property.contains("http") || this.MyTextUrl.isEmpty()) {
            this.MyTextUrl = "https://ms.fabryka.ru/";
        }
        this.txtMyUrl.setText(this.MyTextUrl);
        Toast.makeText(this, "Прочитал: " + this.MyTextUrl, 0).show();
    }

    public void SaveMyUrl(View view) {
        if (this.txtMyUrl.getText().toString().isEmpty()) {
            Toast.makeText(this, "Не указан адрес!!", 0).show();
            return;
        }
        this.MyTextUrl = this.txtMyUrl.getText().toString();
        PersistantStorage.init(this);
        PersistantStorage.addProperty("MyTextUrl", this.MyTextUrl);
        Toast.makeText(this, "Сохранил: " + this.MyTextUrl, 0).show();
    }
}
