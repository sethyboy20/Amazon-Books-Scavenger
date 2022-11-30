/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COP3530.Project3;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *
 * @author Mr.Waddle
 */

@Component
public class Book {
    private class Review {
        String userID;
        String profileName;
        String helpfulness;
        double score;
        double time;
        String summary;
        String fullText;
    }
    
    private String bookID;
    private String title;
    private String desc;
    private double price;
    private ArrayList<String> authors = new ArrayList<>();
    private String image;
    private String previewLink;
    private String publisher;
    private String publishedDate;
    private String infoLink;
    private ArrayList<String> categories = new ArrayList<>();
    private double ratingsCount;
    private ArrayList<Review> reviews = new ArrayList<>();
    
    public Book() {
        this.bookID = "";
        this.title = "";
        this.desc = "";
        this.price = 0;
        this.image = "";
        this.previewLink = "";
        this.publisher = "";
        this.publishedDate = "";
        this.infoLink = "";
        this.ratingsCount = 0;
    }
    
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }
    
    public String getBookID() {
        return bookID;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void addAuthor(String authorName) {
        authors.add(authorName);
    }
    
    public ArrayList<String> getAuthors() {
        return authors;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setPrevLink(String previewLink) {
        this.previewLink = previewLink;
    }
    
    public String getPrevLink() {
        return previewLink;
    }
    
    public void setPub(String publisher) {
        this.publisher = publisher;
    }
    
    public String getPub() {
        return publisher;
    }
    
    public void setPubDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    public String getPubDate() {
        return publishedDate;
    }
    
    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }
    
    public String getInfoLink() {
        return infoLink;
    }
    
    public void addCategory(String catName) {
        categories.add(catName);
    }
    
    public ArrayList<String> getCategories() {
        return categories;
    }
    
    public void setRatingsCount(double ratingsCount) {
        this.ratingsCount = ratingsCount;
    }
    
    public double getRatingsCount() {
        return ratingsCount;
    }
    
    public void addReview(String userID, String profileName, String helpfulness,
                            double score, double time, String summary, String fullText) {
        Review newRev = new Review();
        newRev.userID = userID;
        newRev.profileName = profileName;
        newRev.helpfulness = helpfulness;
        newRev.score = score;
        newRev.time = time;
        newRev.summary = summary;
        newRev.fullText = fullText;
        
        reviews.add(newRev);
    }
    
    public ArrayList<Review> getReviews() {
        return reviews;
    }
}


