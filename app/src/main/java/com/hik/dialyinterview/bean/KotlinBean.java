package com.hik.dialyinterview.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KotlinBean implements Serializable {
    private static final long serialVersionUID = -5755773037306051776L;
    @Id
    @Unique
    private String  id;
    private String title;
    private String url;
    @Generated(hash = 1472252612)
    public KotlinBean(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }
    @Generated(hash = 1781387959)
    public KotlinBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }


}
