package com.example.mauiviewcontrol;

import java.util.ArrayList;

public class ElementButtonList {
    private ArrayList<ElementButton> elementButtonsArrayList;
    private int groupNumber;

    public ElementButtonList(int n){
        groupNumber=n;
        elementButtonsArrayList=new ArrayList<ElementButton>();
    }

    public int size(){
        return elementButtonsArrayList.size();
    }

    public void add(ElementButton elementButton){
        elementButtonsArrayList.add(elementButton);
    }

    public void setEnabled(boolean enabled){
        for(int i=0;i<size();i++){
            elementButtonsArrayList.get(i).setEnabled(enabled);
        }
    }

    public ElementButton get(int i){
        return elementButtonsArrayList.get(i);
    }
    public int groupNumber(){
        return groupNumber;
    }
}