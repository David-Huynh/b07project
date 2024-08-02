package com.b07project2024.group1;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private String id;
    private String title;
    private String author;
    private String genre;
    private String description;

    public Item() {}

    public Item(String id, String title, String author, String genre, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Make Item parcelable for use in Intent
    public Item(Parcel in){
        String[] info = new String[5];

        in.readStringArray(info);
        this.id = info[0];
        this.title = info[1];
        this.author = info[2];
        this.genre = info[3];
        this.description = info[4];
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[] {this.id, this.title, this.author, this.genre, this.description});
    }

    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
