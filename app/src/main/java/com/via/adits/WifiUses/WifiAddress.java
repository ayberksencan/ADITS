package com.via.adits.WifiUses;

public class WifiAddress {
    private String SSID;
    private String BSSID;
    private String DBM;

    public WifiAddress(String mSSID, String mBSSID, String mDBM){
        SSID = mSSID;
        BSSID = mBSSID;
        DBM = mDBM;
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
}
