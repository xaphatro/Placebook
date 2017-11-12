package com.example.paragjain.firebaseauthentication;

/**
 * Created by Pradeep on 12-Nov-17.
 */

public class Item {
    String itemID;
    String itemName;
    String locationName;
    String longitude;
    String latitude;

    public Item(String itemID, String itemName, String locationName, String longitude, String latitude) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Item(String itemID, String itemName) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
