package com.mjacksi.rojprints.RealmObjects;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageRealm extends RealmObject {
    @PrimaryKey
    private long id;

    private String name;

    private String originalPath;
    private String editedPath = "";
    private String url = "";
    public ImageRealm(){}

    public ImageRealm(String name, String originalPath) {
        this.id  = generateUniqueId();
        this.name = name;
        this.originalPath = originalPath;
    }

    Image getAsImage(){
        return new Image(id,name, originalPath);
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

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
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
