package com.example.mauiviewcontrol;

public class AutomatedTestingModel {
    private static AutomatedTestingModel singletonInstance = null;
    public static AutomatedTestingModel getAutomatedTestingModelSingletonInstance() {
        if (null == singletonInstance)
            singletonInstance = new AutomatedTestingModel();
        return singletonInstance;
    }

    private boolean mInProcess = false;
    public boolean inProcess() { return mInProcess; }
    public void setInProcess(boolean inProcess) { mInProcess = inProcess; }

    private class TestCase {
        boolean mainWindowOn = true;
        boolean logInOn = true;
        boolean imagingOn = true;
        boolean statusOn = true;
        boolean probeOn = true;
        boolean patientOn = true;
        boolean measurementOn = true;
        boolean presetOn = true;
        boolean saveOn = true;
        boolean loadOn = true;
        boolean modifyOn = true;
        boolean versionOn = true;
        boolean cleanScreenOn = true;
        boolean logOutOn = true;
    }
    private TestCase testCase = new TestCase();

    public boolean getMainWindowOn() { return testCase.mainWindowOn; }
    public boolean getLogInOn() { return testCase.logInOn; }
    public boolean getImagingOn() { return testCase.imagingOn; }
    public boolean getStatusOn() { return testCase.statusOn; }
    public boolean getProbeOn() { return testCase.probeOn; }
    public boolean getPatientOn() { return testCase.patientOn; }
    public boolean getMeasurementOn() { return testCase.measurementOn; }
    public boolean getPresetOn() { return testCase.presetOn; }
    public boolean getSaveOn() { return testCase.saveOn; }
    public boolean getLoadOn() { return testCase.loadOn; }
    public boolean getModifyOn() { return testCase.modifyOn; }
    public boolean getVersionOn() { return testCase.versionOn; }
    public boolean getCleanScreenOn() { return testCase.cleanScreenOn; }
    public boolean getLogOutOn() { return testCase.logOutOn; }

    public void setMainWindowOn(boolean on) { testCase.mainWindowOn = on; }
    public void setLogInOn(boolean on) { testCase.logInOn = on; }
    public void setImagingOn(boolean on) { testCase.imagingOn = on; }
    public void setStatusOn(boolean on) { testCase.statusOn = on; }
    public void setProbeOn(boolean on) { testCase.probeOn = on; }
    public void setPatientOn(boolean on) { testCase.patientOn = on; }
    public void setMeasurementOn(boolean on) { testCase.measurementOn = on; }
    public void setPresetOn(boolean on) { testCase.presetOn = on; }
    public void setSaveOn(boolean on) { testCase.saveOn = on; }
    public void setLoadOn(boolean on) { testCase.loadOn = on; }
    public void setModifyOn(boolean on) { testCase.modifyOn = on; }
    public void setVersionOn(boolean on) { testCase.versionOn = on; }
    public void setCleanScreenOn(boolean on) { testCase.cleanScreenOn = on; }
    public void setLogOutOn(boolean on) { testCase.logOutOn = on; }

    public boolean getAllCasesOn() {
        return getMainWindowOn() &
                getLogInOn() &
                getImagingOn() &
                getStatusOn() &
                getProbeOn() &
                getPatientOn() &
                getMeasurementOn() &
                getPresetOn() &
                getSaveOn() &
                getLoadOn() &
                getModifyOn() &
                getVersionOn() &
                getCleanScreenOn() &
                getLogOutOn();
    }

    public void setAllAutomatedTestCasesOn(boolean on) {
        setMainWindowOn(on);
        setLogInOn(on);
        setImagingOn(on);
        setStatusOn(on);
        setProbeOn(on);
        setPatientOn(on);
        setMeasurementOn(on);
        setPresetOn(on);
        setSaveOn(on);
        setLoadOn(on);
        setModifyOn(on);
        setVersionOn(on);
        setCleanScreenOn(on);
        setLogOutOn(on);
    }
}
