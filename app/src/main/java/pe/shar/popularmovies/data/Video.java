package pe.shar.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by steve on 04/04/2017.
 */

public class Video {
    private static final String TAG = Video.class.getSimpleName();

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private long size;

    // Trailer, Teaser, Clip, Featurette
    @SerializedName("type")
    private String type;

    @Override
    public String toString() {
        return id + " " + name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static final class Response {
        public int id;

        @SerializedName("results")
        public List<Video> videos;
    }
}
