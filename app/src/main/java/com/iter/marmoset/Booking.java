package com.iter.marmoset;

/**
 * Created by trickedoutdavid on 2/25/18.
 */

public class Booking {
    public Booking(String client_id, String client_name, String date, String salon_name, String service, String session, String stylist, String salon_id) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.date = date;
        this.salon_name = salon_name;
        this.service = service;
        this.salon_id = salon_id;
        this.session = session;
        this.stylist = stylist;
    }

    public Booking(){

    }

    String client_id, client_name, date, salon_name, service, session, stylist, salon_id;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSalon_name() {
        return salon_name;
    }

    public void setSalon_name(String salon_name) {
        this.salon_name = salon_name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getStylist() {
        return stylist;
    }

    public void setStylist(String stylist) {
        this.stylist = stylist;
    }

    public String getSalon_id() {
        return salon_id;
    }

    public void setSalon_id(String salon_id) {
        this.salon_id = salon_id;
    }
}
