package pe.shar.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by steve on 04/04/2017.
 */

public class Review {
    private static final String TAG = Review.class.getSimpleName();

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    @Override
    public String toString() {
        return id + " " + author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static final class Response {
        public int id;
        public int page;
        public int total_results;
        public int total_pages;

        @SerializedName("results")
        public List<Review> reviews;
    }
}
