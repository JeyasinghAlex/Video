package model;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class Video {

    private UUID videoId;
    private String description;
    private String title;
    private String type;
    private String url;
    private Date releaseDate;
    private int releaseYear;
    private float avgRating;
    private Set<String> tags;
    private ByteBuffer previewThumbnail;
    private Set<String> genres;
    private UUID userId;

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(UUID videoId) {
        this.videoId = videoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public ByteBuffer getPreviewThumbnail() {
        return previewThumbnail;
    }

    public void setPreviewThumbnail(ByteBuffer previewThumbnail) {
        this.previewThumbnail = previewThumbnail;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
