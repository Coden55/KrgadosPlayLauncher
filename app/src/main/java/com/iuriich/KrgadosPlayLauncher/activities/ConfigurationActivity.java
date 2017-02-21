package com.iuriich.KrgadosPlayLauncher.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.iuriich.KrgadosPlayLauncher.App;
import com.iuriich.KrgadosPlayLauncher.Preferences;
import com.iuriich.KrgadosPlayLauncher.R;

public class ConfigurationActivity extends Activity {

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        app = App.getInstance();

        Button applications = (Button) findViewById(R.id.applications);
        applications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigurationActivity.this, ApplicationsListActivity.class);
                startActivity(intent);
            }
        });

        CheckBox enable_auto_launch = (CheckBox) findViewById(R.id.enable_auto_launch);
        enable_auto_launch.setChecked((Boolean) Preferences.getInstance().getPref(Preferences.Pref.enable_auto_launch));
        enable_auto_launch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences.getInstance().setPref(Preferences.Pref.enable_auto_launch, b);
            }
        });

    }

}
