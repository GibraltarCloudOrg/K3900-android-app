package com.example.mauiviewcontrol;

import android.view.View;
import android.widget.AdapterView;

public class MauiListView {
    static void changeListViewSelectedItemColor(AdapterView<?> parent, View view, int position, int lastClickedIndex) {
        /*if ((lastClickId != -1) && (lastClickId != position)) {
            parent.getChildAt(lastClickId).setBackgroundResource(R.color.black);
            view.setBackgroundResource(android.R.color.holo_green_dark);
        }
        if (lastClickId == -1)
            view.setBackgroundResource(android.R.color.holo_green_dark);*/
        if (0 <= position && parent.getCount() > position && null != view)
            view.setBackgroundResource(android.R.color.holo_green_dark);
        if (lastClickedIndex != position && 0 <= lastClickedIndex && parent.getCount() > lastClickedIndex)
            parent.getChildAt(lastClickedIndex).setBackgroundResource(R.color.black);
    }

    static void clearAllItemsBackgroundColor(AdapterView<?> parent) {
        if (null != parent) {
            for (int index = 0; index < parent.getCount(); ++index) {
                View itemView = parent.getChildAt(index);
                if (null != itemView)
                    itemView.setBackgroundResource(R.color.black);
            }
        }
    }
}
