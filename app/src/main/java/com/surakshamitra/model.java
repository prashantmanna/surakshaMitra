package com.surakshamitra;
public class model {
    int img;
    String id;
    String contact, number;

    // Constructor for creating a model with an image, contact, and number
    public model(int img, String contact, String number) {
        this.img = img;
        this.contact = contact;
        this.number = number;
    }

    // Constructor for creating a model with only contact and number (used in your adapter)
    public model(String contact, String number) {
        this.contact = contact;
        this.number = number;
    }

    // Constructor for creating a model with an id, contact, and number
    public model(String id, int img, String contact, String number) {
        this.id = id;
        this.img = img;
        this.contact = contact;
        this.number = number;
    }

    // Getter and Setter methods for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    // Other getter and setter methods for img, contact, and number remain the same
    // ...
}
