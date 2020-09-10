package lej.happy.retube.data.models.VideoStats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("viewCount")
    @Expose
    private String viewCount;
    @SerializedName("likeCount")
    @Expose
    private String likeCount;
    @SerializedName("dislikeCount")
    @Expose
    private String dislikeCount;
    @SerializedName("commentCount")
    @Expose
    private String commentCount;

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }


    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

}
