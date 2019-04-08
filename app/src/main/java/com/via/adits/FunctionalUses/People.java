package com.via.adits.FunctionalUses;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019
public class People {


    //GETTERS(Sınıftan verilerin çekilmesini sağlayan fonksiyonlar)-------------------------------------------------------------------------
    private String Name, Age, TCNo, Health, Level;

    public String getName() {
        return Name;
    }

    public String getAge() {
        return Age;
    }

    public String getTCNo() {
        return TCNo;
    }

    public String getHealth() {
        return Health;
    }

    public String getLevel() {
        return Level;
    }


    //SETTERS(Sınıftaki verilerin düzenlenmesini sağlayan fonksiyonlar)---------------------------------------------------------------------------
    public void setName(String name) {
        Name = name;
    }

    public void setAge(String age) {
        Age = age;
    }

    public void setTCNo(String TCNo) {
        this.TCNo = TCNo;
    }

    public void setHealth(String health) {
        Health = health;
    }

    public void setLevel(String level) {
        this.Level = level;
    }

    //FONKSİYONLAR----------------------------------------------------------------------

    //People classına ait bir nesne yaratan fonksiyon.
    public People(String Name, String TCNo, String Age, String Health, String Level) {
        this.Name = Name;
        this.TCNo = TCNo;
        this.Age = Age;
        this.Health = Health;
        this.Level = Level;
    }

    //Alınan verileri String formatına dönüştüren sonra geri ileten fonksiyon.
    @Override
    public String toString() {
        return Name + TCNo + Age + Health + Level;
    }
}
