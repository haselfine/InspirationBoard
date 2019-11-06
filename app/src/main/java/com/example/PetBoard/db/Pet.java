package com.example.PetBoard.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class Pet implements Comparable<Pet>, Parcelable {


    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String tags;
    private Double rating;
    private String photoPath;
    private Date date;

    public Pet(){}

    @Ignore
    public Pet(String name, String description, String tags, Double rating, String photoPath){
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.rating = rating;
        this.photoPath = photoPath;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int describeContents(){
        return 0;
    }

    protected Pet(Parcel in){
        name = in.readString();
        description = in.readString();
        tags = in.readString();
        rating = in.readDouble();
        photoPath = in.readString();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(tags);
        dest.writeDouble(rating);
        dest.writeString(photoPath);

    }

    public int compareTo(Pet pet){
        return this.date.compareTo(pet.date);
    }

    @Override
    public String toString(){
        return "Pet{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", tags=" + tags +
                ", rating=" + rating +
                ", photoPath=" + photoPath +
                ", date=" + date +
                '}';
    }


}
