package com.micky.www.videomodule;

/**
 * Created by Micky on 2019/1/8.
 */

public class VideoBean {
    private String videoName;
    private String videoCover;
    private String videoUrl;
    private String videoTime;

    public VideoBean(String videoName, String videoCover, String videoUrl, String videoTime) {
        this.videoName = videoName;
        this.videoCover = videoCover;
        this.videoUrl = videoUrl;
        this.videoTime = videoTime;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }
}
