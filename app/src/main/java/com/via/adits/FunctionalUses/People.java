package com.via.adits.FunctionalUses;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

/*--------------------------------------This class has been created for defining the Personal Information variables-------------------------------------*/
public class People {

    /*-----------------------------------Defining the global variables for using them in the processes of this class------------------------------------*/
    private String Name, Age, TCNo, Health, Level;

    /*----------------------------------This function returns the "Name" variable-----------------------------------------------------------------------*/
    public String getName() {
        return Name;
    }

    /*----------------------------------This function returns the "Age" variable-----------------------------------------------------------------------*/
    public String getAge() {
        return Age;
    }

    /*----------------------------------This function returns the "Tc No" variable-----------------------------------------------------------------------*/
    public String getTCNo() {
        return TCNo;
    }

    /*----------------------------------This function returns the "Health" variable-----------------------------------------------------------------------*/
    public String getHealth() {
        return Health;
    }

    /*----------------------------------This function returns the "Level" variable-----------------------------------------------------------------------*/
    public String getLevel() {
        return Level;
    }


    /*----------------------------------This function sets the "Name" variable-----------------------------------------------------------------------*/
    public void setName(String name) {
        Name = name;
    }

    /*----------------------------------This function sets the "Age" variable-----------------------------------------------------------------------*/
    public void setAge(String age) {
        Age = age;
    }

    /*----------------------------------This function sets the "Tc No" variable-----------------------------------------------------------------------*/
    public void setTCNo(String TCNo) {
        this.TCNo = TCNo;
    }

    /*----------------------------------This function sets the "Health" variable-----------------------------------------------------------------------*/
    public void setHealth(String health) {
        Health = health;
    }

    /*----------------------------------This function sets the "Level" variable-----------------------------------------------------------------------*/
    public void setLevel(String level) {
        this.Level = level;
    }


    /*------------------------------------Constructor Method for initializing an object from this class-------------------------------------------------*/
    public People(String Name, String TCNo, String Age, String Health, String Level) {
        this.Name = Name;
        this.TCNo = TCNo;
        this.Age = Age;
        this.Health = Health;
        this.Level = Level;
    }

    /*------------------------------------This function converts the data to String format and returns it-------------------------------------------------*/
    @Override
    public String toString() {
        return Name + TCNo + Age + Health + Level;
    }
}
