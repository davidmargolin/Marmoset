package com.iter.marmoset;

import com.arlib.floatingsearchview.FloatingSearchView;

import java.util.ArrayList;

/**
 * Created by deren on 2/24/2018.
 */


public class Salon {

    public Salon() {
    }

    public Salon(String name, String address, String zipcode, String image_url, ArrayList<String> slideshow, int likes, int id, String description) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.image_url = image_url;
        this.slideshow = slideshow;
        this.likes = likes;
        this.id = id;
        this.description = description;
    }

    String name, address, zipcode, image_url, description;
    int likes, id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
