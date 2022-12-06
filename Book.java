import java.util.*;

public class Book {
    private String title;
    private String desc;
    private ArrayList<String> authors = new ArrayList<>();
    private String image;
    private String previewLink;
    private String publisher;
    private String publishedDate;
    private String infoLink;
    private ArrayList<String> categories = new ArrayList<>();
    private double ratingsCount;

    public Book() {
        this.title = "";
        this.desc = "";
        this.image = "";
        this.previewLink = "";
        this.publisher = "";
        this.publishedDate = "";
        this.infoLink = "";
        this.ratingsCount = 0;
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

    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                image + '\'' +
                "description:" + desc + '\'' +
                ", authors='" + authors + '\'' +
                ", previewLink='" + previewLink + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", infoLink='" + infoLink + '\'' +
                ", categories='" + categories + '\'' +
                ", ratingCount='" + ratingsCount + '\'' +
                '}';
    }
}