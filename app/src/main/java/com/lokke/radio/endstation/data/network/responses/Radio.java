package com.lokke.radio.endstation.data.network.responses;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Radio implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("genres")
    @Expose
    private String genres;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("streaming_link")
    @Expose
    private String streamingLink;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStreamingLink() {
        return streamingLink;
    }

    public void setStreamingLink(String streamingLink) {
        this.streamingLink = streamingLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }


    @NonNull
    @Override
    public String toString() {
        return "Radio{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", genres='" + genres + '\'' +
                ", language='" + language + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", streamingLink='" + streamingLink + '\'' +
                ", description='" + description + '\'' +
                ", privacyPolicy='" + privacyPolicy + '\'' +
                '}';
    }
}

