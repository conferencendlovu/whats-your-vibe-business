package za.co.whatsyourvibe.business.models;

import java.io.Serializable;

public class Image implements Serializable {

    private String id;

    private String url;

    public Image() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
