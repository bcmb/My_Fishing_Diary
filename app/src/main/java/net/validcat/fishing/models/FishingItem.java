package net.validcat.fishing.models;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import net.validcat.fishing.data.Constants;
import net.validcat.fishing.data.FishingContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FishingItem {
    long id = -1;
    String place;
    long date;
    //int weather
    String weather;
    String description;
//    String catches;
    String price;
    String thumb;
    private List<String> photoList;
    int weatherIcon;

    public FishingItem() {}

    // data which are set in the formation of a list item
    public long getId() {
        return id;
    }
    public long getDate() {
        return date;
    }
    public String getPlace() {
        return place;
    }
    public String getWeather() {
        return weather;
    }
    public String getDescription() {
        return description;
    }
    public String getPrice() {
        return price;
    }
    public List<String> getPhotoList(){
        return photoList;
    }
    public int getWeatherIcon(){
        return weatherIcon;
    }
    public void addPhoto(String photoPath) {
        if (photoList == null)
            photoList = new ArrayList<>();
        photoList.add(photoPath);
    }

    public String getThumb() {
        return thumb;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item[id:" + id + "]";
    }

    public static FishingItem createFishingItemFromCursor(Context context, Cursor data) {
        FishingItem item = new FishingItem();
        item.id = data.getLong(FishingContract.FishingEntry.INDEX_ID);
        item.place = data.getString(FishingContract.FishingEntry.INDEX_PLACE);
        item.date = data.getLong(FishingContract.FishingEntry.INDEX_DATE);
        item.weather = data.getString(FishingContract.FishingEntry.INDEX_WEATHER);
        item.description = data.getString(FishingContract.FishingEntry.INDEX_DESCRIPTION);
        item.price = data.getString(FishingContract.FishingEntry.INDEX_PRICE);
        String photoStr = data.getString(FishingContract.FishingEntry.INDEX_IMAGE);
        if (!TextUtils.isEmpty(photoStr))
            item.photoList = Arrays.asList(photoStr.split(Constants.SPLIT_IMAGE_PATH_PATTERN));
        item.weatherIcon = data.getInt(FishingContract.FishingEntry.INDEX_WEATHER_ICON);
        item.thumb  = data.getString(FishingContract.FishingEntry.INDEX_THUMB);

        return item;
    }

    public String convertPhotoArrToString() {
        if (photoList == null)
            return "";

        StringBuilder sb = new StringBuilder();
        for (String n : photoList) {
            if (sb.length() > 0) sb.append(',');
            sb.append("'").append(n).append("'");
        }

        return sb.toString();
    }

}
