package com.iuriich.KrgadosPlayLauncher.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableListViewAppItem extends LinearLayout implements Checkable {

    private Checkable radioButton;

    public CheckableListViewAppItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View v = getChildAt(i);
            if (v != null && Checkable.class.isAssignableFrom(v.getClass())) {
                radioButton = (Checkable) v;
            }
        }
    }

    @Override
    public boolean isChecked() {
        return radioButton != null && radioButton.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        if (radioButton != null) {
            radioButton.setChecked(checked);
        }
    }

    @Override
    public void toggle() {
        if (radioButton != null) {
            radioButton.toggle();
        }
    }
}