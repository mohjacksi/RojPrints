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
    long totalPrice;
    long date;
    String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    long getTotalPrice() {
        return images.size() * pricePerPage;
    }

    public Project() {
    }

    public Project(ArrayList<Image> images, int pricePerPage) {

        for (Image image : images) {
            this.images.add(new ImageRealm(image.getId(), image.getName(), image.getPath()));
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
            this.images.add(new ImageRealm(image.getId(), image.getName(), image.getPath()));
        }
    }

    public int getPricePerPage() {
        return pricePerPage;
    }

    public void setPricePerPage(int pricePerPage) {
        this.pricePerPage = pricePerPage;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<Image> getImagesAsImageObject(){
        ArrayList<Image> images = new ArrayList<>();
        for (ImageRealm imageRealm: this.images){
            images.add(new Image(imageRealm.getId(),imageRealm.getName(),imageRealm.getPath()));
        }
        return images;
    }
}
