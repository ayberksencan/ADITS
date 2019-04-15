package com.via.adits.Adapters;

public class WifiAddress {
    private String SSID;
    private String BSSID;
    private String DBM;
    private String SIGNAL;

    public WifiAddress(String mSSID, String mBSSID, String mDBM, String mSIGNAL){
        SSID = mSSID;
        BSSID = mBSSID;
        DBM = mDBM;
        SIGNAL = mSIGNAL;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

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

