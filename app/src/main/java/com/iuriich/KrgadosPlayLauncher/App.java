package com.iuriich.KrgadosPlayLauncher;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.iuriich.KrgadosPlayLauncher.activities.LauncherActivity;
import com.iuriich.KrgadosPlayLauncher.utils.StatusbarBlockerViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class App extends Application {

    private static App instance;

    private ArrayList<StatusbarBlockerViewGroup> blockersList;

    private Timer timer;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        blockersList = new ArrayList<>();

        if(timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                synchronized public void run() {
                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    if (!blockersList.isEmpty() && !componentInfo.getPackageName().equals(Preferences.getInstance().getPref(Preferences.Pref.appname)) &&
                            !componentInfo.getPackageName().equals("com.iuriich.KrgadosPlayLauncher")) {
                        Intent intent = new Intent(App.this, LauncherActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }, 0, 3000);
        }
    }

    public void removeBlockers() {
        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        for (StatusbarBlockerViewGroup statusbarBlockerViewGroup : blockersList) {
            manager.removeView(statusbarBlockerViewGroup);
            blockersList.remove(statusbarBlockerViewGroup);
        }
    }

    public ArrayList<StatusbarBlockerViewGroup> getBlockersList() {
        return blockersList;
    }

}