package com.ziniuyimeixiang.imanager;

/**
 * Created by j_mei on 2018-03-21.
 */

public class ClothesModel extends Model {

    private String upperCloth;
    private String lowerCloth;

    /* female */
    private Boolean dress, rompers, legging, skirts;

    /* both */
    private Boolean hoodies, jeans, pants, shorts, tShirt, sweater, winterCoat, springCoat, windAndWaterProofJacket;

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

    public Boolean getDress() {
        return dress;
    }

    public void setDress(Boolean dress) {
        this.dress = dress;
    }

    public Boolean getRompers() {
        return rompers;
    }

    public void setRompers(Boolean rompers) {
        this.rompers = rompers;
    }

    public Boolean getLegging() {
        return legging;
    }

    public void setLegging(Boolean legging) {
        this.legging = legging;
    }

    public Boolean getSkirts() {
        return skirts;
    }

    public void setSkirts(Boolean skirts) {
        this.skirts = skirts;
    }

    public Boolean getHoodies() {
        return hoodies;
    }

    public void setHoodies(Boolean hoodies) {
        this.hoodies = hoodies;
    }

    public Boolean getJeans() {
        return jeans;
    }

    public void setJeans(Boolean jeans) {
        this.jeans = jeans;
    }

    public Boolean getPants() {
        return pants;
    }

    public void setPants(Boolean pants) {
        this.pants = pants;
    }

    public Boolean getShorts() {
        return shorts;
    }

    public void setShorts(Boolean shorts) {
        this.shorts = shorts;
    }

    public Boolean gettShirt() {
        return tShirt;
    }

    public void settShirt(Boolean tShirt) {
        this.tShirt = tShirt;
    }

    public Boolean getWinterCoat() {
        return winterCoat;
    }

    public void setWinterCoat(Boolean winterCoat) {
        this.winterCoat = winterCoat;
    }

    public Boolean getSpringCoat() {
        return springCoat;
    }

    public void setSpringCoat(Boolean springCoat) {
        this.springCoat = springCoat;
    }

    public Boolean getWindAndWaterProofJacket() {
        return windAndWaterProofJacket;
    }

    public void setWindAndWaterProofJacket(Boolean windAndWaterProofJacket) {
        this.windAndWaterProofJacket = windAndWaterProofJacket;
    }


    public Boolean getSweater() {
        return sweater;
    }

    public void setSweater(Boolean sweater) {
        this.sweater = sweater;
    }

    /**
     * choose clothes
     */

    public void changeFemaleClothesDependonTemp(int currentTemp, int windSpeed, int weatherCode){
        setAllFalse();
        if (currentTemp < -20){
            winterCoat = true;
            sweater = true;
            legging = true;
        }
        else if (currentTemp >= -20 && currentTemp <= 0){
            winterCoat = true;
            legging = true;
            tShirt = true;
        }
        else if (currentTemp >= 0 && currentTemp <= 10){
            if (windSpeed >= 24){
                windAndWaterProofJacket = true;
            }
            else {
                springCoat = true;
            }
            hoodies = true;
            legging = true;
        }
        else if (currentTemp >= 10 && currentTemp <= 20){
            if (windSpeed >= 24){
                windAndWaterProofJacket = true;
            }
            tShirt = true;
            legging = true;
            rompers = true;
        }
        else if (currentTemp >= 20){
            dress = true;
            tShirt = true;
            skirts = true;
        }

        setChanged();
        notifyObservers();
    }

    public void changeMaleClothesDependonTemp(int currentTemp, int windSpeed, int weatherCode){
        setAllFalse();
        if (currentTemp < -20){
            winterCoat = true;
            sweater = true;
            jeans = true;
        }
        else if (currentTemp >= -20 && currentTemp <= 0){
            winterCoat = true;
            jeans = true;
            tShirt = true;
        }
        else if (currentTemp >= 0 && currentTemp <= 10){
            if (windSpeed >= 24){
                windAndWaterProofJacket = true;
            }
            else {
                springCoat = true;
            }
            hoodies = true;
            sweater = true;
            jeans = true;
        }
        else if (currentTemp >= 10 && currentTemp <= 20){
            if (windSpeed >= 24){
                windAndWaterProofJacket = true;
            }
            tShirt = true;
            jeans = true;
        }
        else if (currentTemp >= 20){
            tShirt = true;
            shorts = true;
        }
        setChanged();
        notifyObservers();
    }


    private void setAllFalse() {
        dress = false;
        rompers = false;
        legging = false;
        skirts = false;
        hoodies = false;
        jeans = false;
        pants = false;
        shorts = false;
        tShirt = false;
        sweater = false;
        winterCoat = false;
        springCoat = false;
        windAndWaterProofJacket = false;
    }
}
