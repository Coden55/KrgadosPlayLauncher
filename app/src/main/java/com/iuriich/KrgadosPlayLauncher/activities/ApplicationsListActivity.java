package com.iuriich.KrgadosPlayLauncher.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iuriich.KrgadosPlayLauncher.Preferences;
import com.iuriich.KrgadosPlayLauncher.R;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsListActivity extends Activity {

    private ListView applicationListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications_list);

        PackageManager manager = getPackageManager();
        ArrayList<AppDetail> apps = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetail appDetail = new AppDetail(ri.activityInfo.loadIcon(manager), ri.loadLabel(manager).toString(), ri.activityInfo.packageName);
            apps.add(appDetail);
        }

        applicationListView = (ListView) findViewById(R.id.applicationListView);
        ArrayAdapter<AppDetail> adapter = new AppAdapter(apps);
        applicationListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class AppDetail {
        private String label;
        private String name;
        private Drawable icon;

        private AppDetail(Drawable icon, String label, String name) {
            this.icon = icon;
            this.label = label;
            this.name = name;
        }

        private String getLabel() {
            return label;
        }

        private String getName() {
            return name;
        }

        private Drawable getIcon() {
            return icon;
        }
    }

    private class AppAdapter extends ArrayAdapter<AppDetail> {

        private String appNameInit;

        AppAdapter(ArrayList<AppDetail> apps) {
            super(ApplicationsListActivity.this, R.layout.listview_app_item, apps);
            appNameInit = (String) Preferences.getInstance().getPref(Preferences.Pref.appname);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.listview_app_item, null);
            }

            final AppDetail app = getItem(position);
            assert app != null;

            ImageView appIcon = (ImageView) convertView.findViewById(R.id.app_icon);

            appIcon.setImageDrawable(app.getIcon());

            TextView appLabel = (TextView) convertView.findViewById(R.id.app_label);
            appLabel.setText(app.getLabel());

            RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);

            if(appNameInit != null && app.getName().equals(appNameInit)) {
                applicationListView.setItemChecked(position, true);
            }

            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                       if(b) {
                           appNameInit = app.getName();
                           Preferences.getInstance().setPref(Preferences.Pref.appname, appNameInit);
                       }
                }
            });

            appIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(app.getName());
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }
}
