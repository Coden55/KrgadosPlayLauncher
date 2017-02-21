package com.iuriich.KrgadosPlayLauncher.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.iuriich.KrgadosPlayLauncher.App;
import com.iuriich.KrgadosPlayLauncher.Preferences;
import com.iuriich.KrgadosPlayLauncher.R;
import com.iuriich.KrgadosPlayLauncher.utils.StatusbarBlockerViewGroup;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD;

public class LauncherActivity extends Activity {

    private WindowManager manager;
    private App app;
    private boolean showDialog = false;

    @Override
    protected void onNewIntent(Intent intent) {
        if(getIntent().getStringExtra("action") != null && getIntent().getStringExtra("action").equals("login")) {
            showDialog = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        app = App.getInstance();
        manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        if(getIntent().getStringExtra("action") != null && getIntent().getStringExtra("action").equals("login")) {
            showDialog = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        preventStatusBarExpansion();

        if(showDialog) {
            showAdminPasswordDialog();
            showDialog = false;
        }
        else startApp();

    }

    @Override
    public void onBackPressed() {
    }

    private void preventStatusBarExpansion() {
        if (app.getBlockersList().size() > 0) {
            return;
        }
        StatusbarBlockerViewGroup statusbarBlockerViewGroup = new StatusbarBlockerViewGroup(this);
        app.getBlockersList().add(statusbarBlockerViewGroup);
        //statusbarBlockerViewGroup.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        statusbarBlockerViewGroup.setId(R.id.blocker);
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) result = getResources().getDimensionPixelSize(resId);
        localLayoutParams.height = result;
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        statusbarBlockerViewGroup.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
                intent.putExtra("action", "login");
                app.startActivity(intent);
                return true;
            }
        });
        manager.addView(statusbarBlockerViewGroup, localLayoutParams);
    }

    private void showAdminPasswordDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText passwordEditText = new EditText(this);
        passwordEditText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD);
        alert.setTitle("Password");
        alert.setView(passwordEditText);
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                startApp();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startApp();
            }
        });
        alert.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (passwordEditText.getText().toString().equals(Preferences.getInstance().getPref(Preferences.Pref.password))) {
                    goAdminActivity();
                } else {
                    Toast.makeText(LauncherActivity.this, R.string.wrong_password, Toast.LENGTH_LONG).show();
                    startApp();
                }
            }
        });
        alert.show();
    }

    private void goAdminActivity() {
        app.removeBlockers();
        Intent intent = new Intent(this, ConfigurationActivity.class);
        startActivity(intent);
    }

    private void startApp() {
        String appname = (String) Preferences.getInstance().getPref(Preferences.Pref.appname);
        Boolean launchEnabled = (Boolean) Preferences.getInstance().getPref(Preferences.Pref.enable_auto_launch);
        if(appname.isEmpty() || !launchEnabled) return;
        Intent intent = getPackageManager().getLaunchIntentForPackage(appname);
        startActivity(intent);
    }

}
