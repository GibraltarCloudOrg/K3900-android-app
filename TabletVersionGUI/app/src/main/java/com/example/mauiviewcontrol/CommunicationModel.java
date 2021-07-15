package com.example.mauiviewcontrol;

public class CommunicationModel {

    /*
    Tablet:
    Static IP: 192.168.10.250
    mask: 255.255.255.10
    DNS: 192.168.10.11
    Gateway: 192.168.10.1
     */
    public boolean getActivateWiredConnectionViaEthernetCable() {
        return mActivateWiredConnectionViaEthernetCable;
    }

    public void setActivateWiredConnectionViaEthernetCable(boolean activateWiredConnectionViaEthernetCable) {
        mActivateWiredConnectionViaEthernetCable = activateWiredConnectionViaEthernetCable;
    }

    public boolean getDisconnectWiFiDirect() {
        return mDisconnectWiFiDirect;
    }

    public void setDisconnectWiFiDirect(boolean disconnectWiFiDirect) {
        mDisconnectWiFiDirect = disconnectWiFiDirect;
    }

    public static CommunicationModel getCommunicationModelSingletonInstance() {
        if (null == sSingletonInstance)
            sSingletonInstance = new CommunicationModel();
        return sSingletonInstance;
    }

    private static CommunicationModel sSingletonInstance = null;
    private boolean mActivateWiredConnectionViaEthernetCable = false;
    private boolean mDisconnectWiFiDirect = false;
}
