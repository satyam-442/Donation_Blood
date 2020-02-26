package com.example.blooddonor;

public class Donor
{
    public String City, DOB, Email, Gender, Group, Name, Address, Phone, image;

    public Donor() {
    }

    public Donor(String City, String DOB, String Email, String Gender, String Group, String Name, String Address, String Phone, String image) {
        this.City = City;
        this.DOB = DOB;
        this.Email = Email;
        this.Gender = Gender;
        this.Group = Group;
        this.Name = Name;
        this.Address = Address;
        this.Phone = Phone;
        this.image = image;
    }

    public String getCityy() {
        return City;
    }

    public void setCityy(String city) {
        City = city;
    }

    public String getDobb() {
        return DOB;
    }

    public void setDobb(String DOB) {
        this.DOB = DOB;
    }

    public String getEmaill() {
        return Email;
    }

    public void setEmaill(String email) {
        Email = email;
    }

    public String getGenderr() {
        return Gender;
    }

    public void setGenderr(String gender) {
        Gender = gender;
    }

    public String getGroupp() {
        return Group;
    }

    public void setGroupp(String group) {
        Group = group;
    }

    public String getNamee() {
        return Name;
    }

    public void setNamee(String name) {
        Name = name;
    }

    public String getAddres() {
        return Address;
    }

    public void setAddres(String address) {
        Address = address;
    }

    public String getPhonee() {
        return Phone;
    }

    public void setPhonee(String phone) {
        Phone = phone;
    }

    public String getImagee() {
        return image;
    }

    public void setImagee(String image) {
        this.image = image;
    }
}