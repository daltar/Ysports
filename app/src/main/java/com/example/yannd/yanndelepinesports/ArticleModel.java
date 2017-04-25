package com.example.yannd.yanndelepinesports;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by yannd on 23/04/2017.
 */

public class ArticleModel {
     @SerializedName("author")
     private String author;
     @SerializedName("title")
     private String title;
     @SerializedName("description")
     private String description;
     @SerializedName("url")
     private String url;
     @SerializedName("urlToImage")
     private String urlToImage;
     @SerializedName("publishedAt")
     private String publishedAt;

     public String getAuthor() {
          return author;
     }

     public String getTitle() {
          return title;
     }

     public String getDescription() {
          return description;
     }

     public String getUrl() {
          return url;
     }

     public String getUrlToImage() {
          return urlToImage;
     }

     public String getPublisheDate() {
          String[] parts = publishedAt.split("T");
          String date = parts[0];
          return date;
     }
     public String getPublisheTime() {
          String[] parts = publishedAt.split("T");
          String[] parttime = parts[1].split("\\+");
          String time = parttime[0];
          return time;
     }


}
