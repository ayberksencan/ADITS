package com.via.adits.Adapters;

public class WifiAddress {
    private String SSID;
    private String BSSID;
    private String DBM;
    private String SIGNAL;

    /*------------------------------------Constructor Method for initializing an object from this class-------------------------------------------------*/
    public WifiAddress(String mSSID, String mBSSID, String mDBM, String mSIGNAL){
        SSID = mSSID;
        BSSID = mBSSID;
        DBM = mDBM;
        SIGNAL = mSIGNAL;
    }

    /*------------------------------------Function for returning the SSID(Name) of the network----------------------------------------------------------------*/
    public String getSSID() {
        return SSID;
    }

    /*------------------------------------Function for setting the SSID of the network-------------------------------------------------------------------*/
    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    /*------------------------------------Function for getting the BSSID(Mac Address) of the network------------------------------------------------------*/
    public String getBSSID() {
        return BSSID;
    }

    /*------------------------------------Function for setting the BSSID of the network--------------------------------------------------------------------*/
    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    /*------------------------------------Function for returning the dBm of the network----------------------------------------------------------------------*/
    public String getDBM() {
        return DBM;
    }

    public void setDBM(String DBM) {
        this.DBM = DBM;
    }

    public String getSIGNAL() {
        return SIGNAL;
    }

    public void setSIGNAL(String SIGNAL) {
        this.SIGNAL = SIGNAL;
    }
}

