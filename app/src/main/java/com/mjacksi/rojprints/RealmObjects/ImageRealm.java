package com.mjacksi.rojprints.RealmObjects;

import com.nguyenhoanglam.imagepicker.model.Image;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageRealm extends RealmObject {
    @PrimaryKey
    private long id;

    private String name;
    private String path;

    public ImageRealm(){}

    public ImageRealm(long id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    Image getAsImage(){
        return new Image(id,name,path);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
