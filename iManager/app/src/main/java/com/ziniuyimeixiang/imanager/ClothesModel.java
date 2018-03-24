package com.ziniuyimeixiang.imanager;

/**
 * Created by j_mei on 2018-03-21.
 */

public class ClothesModel extends Model {

    String upperCloth;
    String lowerCloth;

    /**
     * instance
     */
    private static final ClothesModel ourInstance = new ClothesModel();
    static ClothesModel getInstance()
    {
        return ourInstance;
    }

    /**
     * Constructor, getter and setter
     */

    public ClothesModel() {
    }

    public String getUpperCloth() {
        return upperCloth;
    }

    public void setUpperCloth(String upperCloth) {
        this.upperCloth = upperCloth;
    }

    public String getLowerCloth() {
        return lowerCloth;
    }

    public void setLowerCloth(String lowerCloth) {
        this.lowerCloth = lowerCloth;
    }

    /**
     * choose clothes
     */

    public void changeClothesDependonTemp(int lowerTemp, int higherTemp){
        if (lowerTemp < -20){
            upperCloth = "Down jacket";
            lowerCloth = "Jeans and Thick pants";
        }
        else if (lowerTemp >= -20 && lowerTemp <= 0){
            upperCloth = "Warm Coat or winter vest";
            lowerCloth = "Jeans or Thick legging";
        }
        else if (lowerTemp >= 0 && lowerTemp <= 10){
            upperCloth = "Coat or Hoodies";
            lowerCloth = "Jeans or legging";
        }
        else if (lowerTemp >= 10 && lowerTemp <= 20){
            upperCloth = "T-shirt";
            lowerCloth = "legging";
        }
        else if (lowerTemp >= 20){
            upperCloth = "T-shirt or Tank top";
            lowerCloth = "shorts or skirts";
        }
        setChanged();
        notifyObservers();
    }
}
