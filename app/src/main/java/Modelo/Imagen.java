package Modelo;

import java.io.Serializable;

public class Imagen implements Serializable {
    public String imagePath;

    public Imagen(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return imagePath ;
    }
    }

