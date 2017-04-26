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

     //Constructeur par défaut
     public ArticleModel(String author, String title, String description, String url,String urlToImage, String publishedAt){

          this.author = author;
          this.title = title;
          this.description = description;
          this.url = url;
          this.urlToImage = urlToImage;

          if (publishedAt != null) {
               this.publishedAt = publishedAt;
          }
          else
          {
               this.publishedAt ="";
          }
     }

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

     public String getPublishedAt () {return publishedAt;}

     public String getPublisheDate() {
          if (publishedAt != null){
          String[] parts = publishedAt.split("T");
          String date = parts[0];
          return date;}
          else {
               return "";
          }
     }
     public String getPublisheTime() {
          if (publishedAt != null){
          String[] parts = publishedAt.split("T");
          String[] parttime = parts[1].split("\\+");
          String time = parttime[0];
          return time;}
          else {
               return "";
          }
     }


}
