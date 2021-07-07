package com.example.mauiviewcontrol;

interface BackEndSliderElement {
    String getRuntimeSubText();
    float getValue();
    void setValue(float value);
    boolean accept(BackEndSliderElementVisitor visitor);
}

interface BackEndSliderElementVisitor {
    boolean visit(Tgc element);
    boolean visit(Dlc element);
    boolean visit(SpeedOfSound element);
    boolean visit(MasterGain element);
    boolean visit(AcousticPower element);
    boolean visit(Gaussian element);
    boolean visit(Edge element);
    boolean visit(Contrast element);
    boolean visit(Brightness element);
    boolean visit(Gamma element);
    boolean visit(FineAdjust element);
    boolean visit(Zoom element);
}

class Tgc implements BackEndSliderElement {
    /*private final String name;

    public StartMeasurement(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }*/
    Tgc(int index) { mIndex = index; }
    int mIndex;
    public void setIndex(int index) { mIndex = index; }
    public int getIndex() { return mIndex; }

    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class Dlc implements BackEndSliderElement {
    /*private final String name;

    public StartMeasurement(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }*/

    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class SpeedOfSound implements BackEndSliderElement {

    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class MasterGain implements BackEndSliderElement {
    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class AcousticPower implements BackEndSliderElement {
    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class Gaussian implements BackEndSliderElement {
    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class Edge implements BackEndSliderElement {
    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class Contrast implements BackEndSliderElement {
    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class Brightness implements BackEndSliderElement {

    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class Gamma implements BackEndSliderElement {

    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class FineAdjust implements BackEndSliderElement {

    @Override
    public boolean accept(BackEndSliderElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private float mValue;

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float value) {
        mValue = value;
    }
}

class Zoom implements BackEndSliderElement{
    @Override
    public boolean accept(BackEndSliderElementVisitor visitor){
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText(){return "";}

    private float mValue;

    @Override
    public float getValue(){return mValue;}

    @Override
    public void setValue(float value){mValue=value;}
}

class BackEndSliderElementSendingMessageVisitor implements BackEndSliderElementVisitor {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();

    @Override
    public boolean visit(Tgc element) {
        return mBackend.onTgcChanged(element.getIndex(), element.getValue());
    }

    @Override
    public boolean visit(Dlc element) {
        return mBackend.onDlcChanged(element.getValue());
    }

    @Override
    public boolean visit(SpeedOfSound element) {
        return mBackend.onSpeedOfSoundChanged(element.getValue());
    }

    @Override
    public boolean visit(MasterGain element) {
        return mBackend.onMasterGainChanged(element.getValue());
    }

    @Override
    public boolean visit(AcousticPower element) {
        return mBackend.onAcousticPowerChanged(element.getValue());
    }

    @Override
    public boolean visit(Gaussian element) {
        return mBackend.onGaussianChanged(element.getValue());
    }

    @Override
    public boolean visit(Edge element) {
        return mBackend.onEdgeChanged(element.getValue());
    }

    @Override
    public boolean visit(Contrast element) {
        return mBackend.onContrastChanged(element.getValue());
    }

    @Override
    public boolean visit(Brightness element) {
        return mBackend.onBrightnessChanged(element.getValue());
    }

    @Override
    public boolean visit(Gamma element) {
        return mBackend.onGammaChanged(element.getValue());
    }

    @Override
    public boolean visit(FineAdjust element) {
        return mBackend.onFrequencyChanged(element.getValue());
    }

    @Override
    public boolean visit(Zoom element){
        return mBackend.onZoom(element.getValue(), true);
    }
}
