package com.iuriich.KrgadosPlayLauncher;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.iuriich.KrgadosPlayLauncher.utils.StatusbarBlockerViewGroup;

import java.util.ArrayList;

public class App extends Application {

    private static App instance;

    private ArrayList<StatusbarBlockerViewGroup> blockersList;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        blockersList = new ArrayList<>();
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