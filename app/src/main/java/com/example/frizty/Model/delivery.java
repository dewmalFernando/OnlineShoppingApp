package com.example.frizty.Model;

public class delivery {

    String streetName,City,state,Zipcode;



    public delivery()
    {

    }

    public delivery(String streetName, String city, String state, String zipcode) {

        this.streetName = streetName;
        City = city;
        this.state = state;
        Zipcode = zipcode;
    }





    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return Zipcode;
    }


    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }
}
