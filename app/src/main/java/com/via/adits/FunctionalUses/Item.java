package com.via.adits.FunctionalUses;
public class Item {

    /*----------------------Defining the global variables of this class------------------------------------------*/
    private String ssid;
    private String rssi;
    private String bssid;
    private String signalP;
    private String connected;


    /*-------------------------This function returns the SSID(Name) of the network-------------------------------------*/
    public String getSsid() {
        return ssid;
    }

    /*-------------------------This function returns the RSSI(dBm) of the network------------------------------------*/
    public String getRssi() {
        return rssi;
    }

    /*-------------------------This function returns the BSSID(Mac Address) of the network---------------------------*/
    public String getBssid() {
        return bssid;
    }

    /*-------------------------This function returns the signal percentage of the network----------------------------*/
    public String getSignalP(){
        return signalP;
    }

    /*-------------------------This function returns the connected networks information------------------------------*/
    public String getConnected(){
        return connected;
    }

    /*-------------------------This function sets the RSSI of the network manually-----------------------------------*/
    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    /*-------------------------This function sets the SSID of the network manually------------------------------------*/
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }



    /*------------------------------------Constructor Method for initializing an object from this class-------------------------------------------------*/
    public Item(String ssid, String bssid, String rssi, String signalP, String connected) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.rssi = rssi;
        this.signalP = signalP;
        this.connected = connected;
    }

    /*------------------------------------This function converts the data to String format------------------------------------*/
    @Override
    public String toString() {
        return ssid + bssid + rssi + signalP;
    }
}
