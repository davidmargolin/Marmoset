package com.iter.marmoset;

import java.util.List;

/**
 * Created by deren on 2/24/2018.
 */

public class SalonList {

    public SalonList(){
    }

    public SalonList(List<Salon> salons) {
        this.salons = salons;
    }

    List<Salon> salons;

    public List<Salon> getSalons() {
        return salons;
    }

    public void setSalons(List<Salon> salons) {
        this.salons = salons;
    }
}
