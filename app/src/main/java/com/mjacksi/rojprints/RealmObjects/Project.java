package com.mjacksi.rojprints.RealmObjects;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Project extends RealmObject {
    @PrimaryKey
    private String id;
    RealmList<ImageRealm> images = new RealmList<>();
    int pricePerPage;

    long date;
    String size;
    String type;
    int countAtCart = 1;
    boolean isInCart = false;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTotalPrice() {
        return images.size() * pricePerPage * countAtCart;
    }

    public Project() {
    }

    public Project(ArrayList<Image> images, int pricePerPage) {

        for (Image image : images) {
            this.images.add(new ImageRealm(image.getName(), image.getPath()));
        }

        this.pricePerPage = pricePerPage;
        this.date = System.currentTimeMillis() / 1000L;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<ImageRealm> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images.clear();
        for (Image image : images) {
            this.images.add(new ImageRealm(image.getName(), image.getPath()));
        }
    }

    public void setImages(Image image) {
        this.images.clear();
        this.images.add(new ImageRealm(image.getName(), image.getPath()));
    }

    public int getPricePerPage() {
        return pricePerPage;
    }

    public void setPricePerPage(int pricePerPage) {
        this.pricePerPage = pricePerPage;
    }



    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<Image> getImagesAsImageObject() {
        ArrayList<Image> images = new ArrayList<>();
        for (ImageRealm imageRealm : this.images) {
            images.add(new Image(imageRealm.getId(), imageRealm.getName(), imageRealm.getpath()));
        }
        return images;
    }

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCountAtCart() {
        return countAtCart;
    }

    public void setCountAtCart(int countAtCart) {
        this.countAtCart = countAtCart;
    }

    public int increaseCount(){
        countAtCart += 1;
        return countAtCart;
    }

    public int decreaseCount(){
        if(countAtCart > 1){
            countAtCart = countAtCart-1;
        }
        return countAtCart;
    }
}
