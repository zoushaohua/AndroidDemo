package com.hik.dialyinterview.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

@Entity
public class DialyBean implements Serializable {
    private static final long serialVersionUID = -3265667806095974887L;
    @Id
    @Unique
    private String id;
    @Unique
    private String title;
    private String content;
    private String url;

    @Generated(hash = 64444498)
    public DialyBean(String id, String title, String content, String url) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.url = url;
    }

    @Generated(hash = 1003646340)
    public DialyBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


}
