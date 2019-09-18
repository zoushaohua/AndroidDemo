package com.hik.dialyinterview.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class DetailBean {
    @Id
    @Unique
    private String id;
    private String url;
    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Generated(hash = 1998031264)
    public DetailBean(String id, String url, String name, String content) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.content = content;
    }

    @Generated(hash = 610650804)
    public DetailBean() {
    }
}
