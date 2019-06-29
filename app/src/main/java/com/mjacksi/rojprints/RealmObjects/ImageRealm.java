package com.mjacksi.rojprints.RealmObjects;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageRealm extends RealmObject {
    @PrimaryKey
    private long id;

    private String name;

    private String path;
    private String editedPath = "";
    private String url = "";
    public ImageRealm(){}

    public ImageRealm(String name, String path) {
        this.id  = generateUniqueId();
        this.name = name;
        this.path = path;
    }

    Image getAsImage(){
        return new Image(id,name, path);
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

    public String getpath() {
        return path;
    }

    public void setpath(String path) {
        this.path = path;
    }

    private Long generateUniqueId() {
        long val = -1;
        do {
            val = UUID.randomUUID().getMostSignificantBits();
        } while (val < 0);
        return val;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getEditedPath() {
        return editedPath;
    }

    public void setEditedPath(String editedPath) {
        this.editedPath = editedPath;
    }

    public boolean hasEditedImage(){
        return !editedPath.equals("");
    }

}
