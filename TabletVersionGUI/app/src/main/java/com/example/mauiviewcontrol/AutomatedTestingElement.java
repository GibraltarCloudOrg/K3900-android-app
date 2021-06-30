package com.example.mauiviewcontrol;

public interface AutomatedTestingElement {
    public enum TestCase { None, MainWindow, LogIn, Imaging, Status, Probe, Patient, Measurement, Preset, Save, Load, Modify, Version, CleanScreen, LogOut }

    void executeAutomatedTesting();
}
