package com.mcet.pandemichelper;

public class VideoDetailsModel {

    public String videoId,url,desc,title;

    public VideoDetailsModel() {
    }

    public VideoDetailsModel(String videoId, String url, String desc, String title) {
        this.videoId = videoId;
        this.url = url;
        this.desc = desc;
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
