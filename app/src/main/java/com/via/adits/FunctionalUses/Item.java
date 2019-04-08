package com.via.adits.FunctionalUses;
public class Item {

    //Item Class'ına ait değişkenlerin tanımlandığı blok.
    private String ssid;
    private String rssi;
    private String bssid;
    private String signalP;


    //Sınıftan bilgileri dışa aktaran fonksiyonların bulunduğu blok.
    public String getSsid() {
        return ssid;
    }

    public String getRssi() {
        return rssi;
    }

    public String getBssid() {
        return bssid;
    }

    public String getSignalP(){
        return signalP;
    }

    //Sınıftaki bilgilerin düzenlenmesini sağlayan fonksiyonların bulunduğu blok.
    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }



    //Item Sınıfından bir nesne oluşturulmasını sağlayan fonksiyon.
    public Item(String ssid, String bssid, String rssi, String signalP) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.rssi = rssi;
        this.signalP = signalP;
    }

    //Değerlerin String formatına dönüştürülmesini sağlayan fonksiyon.
    @Override
    public String toString() {
        return ssid + bssid + rssi + signalP;
    }
}
