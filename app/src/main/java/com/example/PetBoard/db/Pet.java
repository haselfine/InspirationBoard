package com.example.PetBoard.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Pet implements Comparable<Pet>{


    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String tags;
    private Double rating;
    private String photoPath;

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


    public int compareTo(Pet pet){
        return this.name.toLowerCase().compareTo(pet.getName().toLowerCase());
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
                '}';
    }
}
