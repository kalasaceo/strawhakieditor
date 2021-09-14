package com.daasuu.camerarecorder.mode;

import java.io.Serializable;

/**
 * Created by HoangAnhTuan on 1/21/2018.
 */

public class Feed implements Serializable {

    private Info info;
    private Model model;
    private String thiscomment;
    private String pdate;
    private String plike;
    public Feed() {
    }

    public Feed(Info info, Model model,String thiscomment,String pdate,String plike) {
        this.info = info;
        this.model = model;
        this.thiscomment=thiscomment;
        this.pdate=pdate;
        this.plike=plike;
    }

    public Info getInfo() {
        return info;
    }
    public String getmycomment() {
        return thiscomment;
    }
    public String getpdate() {
        return pdate;
    }
    public String getlike() {
        return plike;
    }
    public void setInfo(Info info) {
        this.info = info;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public enum Model {
        M1, M2
    }
}
