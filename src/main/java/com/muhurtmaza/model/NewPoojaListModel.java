package com.muhurtmaza.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 05-02-2016.
 */
public class NewPoojaListModel {

    @SerializedName("pooja_id")
    String poojaId;

    @SerializedName("pooja_name")
    String poojaName;

    @SerializedName("image")
    String image;

    @SerializedName("pooja_duration")
    String duration;

    @SerializedName("no_of_guruji")
    String noOfGuruji;

    @SerializedName("views")
    String views;

    @SerializedName("booked")
    String booked;

    @SerializedName("rating")
    String rating;

    @SerializedName("dakshina_with_samagree")
    String dakshinaWithSamagree;

    @SerializedName("dakshina_without_samagree")
    String dakshinaWithoutSamagree;

    public String getPoojaId() {
        return poojaId;
    }

    public void setPoojaId(String poojaId) {
        this.poojaId = poojaId;
    }

    public String getPoojaName() {
        return poojaName;
    }

    public void setPoojaName(String poojaName) {
        this.poojaName = poojaName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNoOfGuruji() {
        return noOfGuruji;
    }

    public void setNoOfGuruji(String noOfGuruji) {
        this.noOfGuruji = noOfGuruji;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDakshinaWithSamagree() {
        return dakshinaWithSamagree;
    }

    public void setDakshinaWithSamagree(String dakshinaWithSamagree) {
        this.dakshinaWithSamagree = dakshinaWithSamagree;
    }

    public String getDakshinaWithoutSamagree() {
        return dakshinaWithoutSamagree;
    }

    public void setDakshinaWithoutSamagree(String dakshinaWithoutSamagree) {
        this.dakshinaWithoutSamagree = dakshinaWithoutSamagree;
    }

    @Override
    public String toString() {
        return "NewPoojaListModel{" +
                "poojaId='" + poojaId + '\'' +
                ", poojaName='" + poojaName + '\'' +
                ", image='" + image + '\'' +
                ", duration='" + duration + '\'' +
                ", noOfGuruji='" + noOfGuruji + '\'' +
                ", views='" + views + '\'' +
                ", booked='" + booked + '\'' +
                ", rating='" + rating + '\'' +
                ", dakshinaWithSamagree='" + dakshinaWithSamagree + '\'' +
                ", dakshinaWithoutSamagree='" + dakshinaWithoutSamagree + '\'' +
                '}';
    }
}


