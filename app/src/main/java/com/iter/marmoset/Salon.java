package com.iter.marmoset;

import java.util.ArrayList;

/**
 * Created by deren on 2/24/2018.
 */


public class Salon {

    public Salon() {
    }

    public Salon(String name, String address, String zipcode, String image_url, ArrayList<String> slideshow) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.image_url = image_url;
        this.slideshow = slideshow;
    }

    String name, address, zipcode, image_url;
    ArrayList<String> slideshow;

    public ArrayList<String> getSlideshow() {
        return slideshow;
    }

    public void setSlideshow(ArrayList<String> slideshow) {
        this.slideshow = slideshow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
