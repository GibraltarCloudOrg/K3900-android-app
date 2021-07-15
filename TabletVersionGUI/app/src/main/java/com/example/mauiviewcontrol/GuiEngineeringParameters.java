package com.example.mauiviewcontrol;

public class GuiEngineeringParameters {
    private static GuiEngineeringParameters mGuiEngineeringParameters=null;
    private boolean mPanUnixStyle=false;

    public static GuiEngineeringParameters getSingletonInstance(){
        if(mGuiEngineeringParameters==null){
            mGuiEngineeringParameters=new GuiEngineeringParameters();
        }
        return mGuiEngineeringParameters;
    }

    public void setPanUnixStyle(boolean panUnixStyle){
        mPanUnixStyle=panUnixStyle;
    }

    public boolean getPanUnixStyle(){
        return mPanUnixStyle;
    }
}
