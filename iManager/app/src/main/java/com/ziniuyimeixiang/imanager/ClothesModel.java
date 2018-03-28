package com.ziniuyimeixiang.imanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    /* cloth type */
    private final String winterCoatString = "winter_coat";
    private final String sweaterString = "sweater";
    private final String leggingString = "legging";
    private final String tShirtString = "t_shirt";
    private final String windProofJacketString = "wind_proof_jacket";
    private final String springCoatString = "spring_coat";
    private final String hoodieString = "hoodie";
    private final String dressString = "dress";
    private final String skirtString = "skirt";
    private final String jeansString = "jeans";
    private final String shortsString = "shorts";

    private List<byte[]> images;
    private ArrayList<Bitmap> photos;

    Context context;

    /* database */
    private DbHelper dbHelper;
    private Boolean ifGetImages;

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
        images = new ArrayList<>();
        ifGetImages = false;
    }


    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Bitmap> photos) {
        this.photos = photos;
    }

    public Boolean getIfGetImages() {
        return ifGetImages;
    }

    public void setIfGetImages(Boolean ifGetImages) {
        this.ifGetImages = ifGetImages;
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(ArrayList<byte[]> images) {
        this.images = images;
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

    /**
     * Database section
     */

    public int saveImage(String clothType, Bitmap photo){
        byte[] image = converBitmapToByteArray(photo);
        String tableName = checkDbTableName(clothType);
        if (tableName.equals("")){
            return 0;
        }
        dbHelper.addToDb(tableName, image);
        return 1;
    }

    public void getAllImages(String clothType){
        ifGetImages = false;
        images = dbHelper.getAllImage(clothType);
        photos = convertAllImagesToBitmap(images);
//        if (photos.size() != 0){
//            ifGetImages = true;
//        }
        ifGetImages = true;
        setChanged();
        notifyObservers();
    }

    private ArrayList<Bitmap> convertAllImagesToBitmap(List<byte[]> images) {
        ArrayList<Bitmap> tempPhotos = new ArrayList<Bitmap>();
        Iterator<byte[]> iterator = images.iterator();
        while (iterator.hasNext()){
            byte[] tempImage = iterator.next();
            Bitmap photo = convertByteArrayToBitmap(tempImage);
            tempPhotos.add(photo);
        }
        return tempPhotos;
    }


    private byte[] converBitmapToByteArray(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private Bitmap convertByteArrayToBitmap(byte[] barray){
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(barray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    private String checkDbTableName(String clothType){
        String answer = "";
        if (clothType.equals("winter coat")){
            answer = winterCoatString;
        }
        else if (clothType.equals("sweater")){
            answer = sweaterString;
        }
        else if (clothType.equals("legging")){
            answer = leggingString;
        }
        else if (clothType.equals("T-shirt")){
            answer = tShirtString;
        }
        else if (clothType.equals("wind proof jacket")){
            answer = windProofJacketString;
        }
        else if (clothType.equals("spring coat")){
            answer = springCoatString;
        }
        else if (clothType.equals("hoodie")){
            answer = hoodieString;
        }
        else if (clothType.equals("dress")){
            answer = dressString;
        }
        else if (clothType.equals("skirt")){
            answer = skirtString;
        }
        else if (clothType.equals("jeans")){
            answer = jeansString;
        }
        else if (clothType.equals("shorts")){
            answer = shortsString;
        }
        return answer;
    }



}
