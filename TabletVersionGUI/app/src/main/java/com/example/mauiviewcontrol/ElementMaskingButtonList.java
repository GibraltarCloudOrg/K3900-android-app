package com.example.mauiviewcontrol;

import android.widget.Button;

import java.util.ArrayList;

public class ElementMaskingButtonList {
    private ArrayList<ElementMaskingButton> mElementMaskingButtonArrayList = new ArrayList<ElementMaskingButton>();

    public void setEnabled(boolean enabled) {
        for (int index = 0; index < size(); ++index)
            mElementMaskingButtonArrayList.get(index).setEnabled(enabled);
    }

    public int size() {
        return mElementMaskingButtonArrayList.size();
    }

    public void add(ElementMaskingButton elementMaskingButton) {
        mElementMaskingButtonArrayList.add(elementMaskingButton);
    }
}
