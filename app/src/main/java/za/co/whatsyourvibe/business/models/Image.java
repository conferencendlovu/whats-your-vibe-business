package za.co.whatsyourvibe.business.models;

import java.io.Serializable;

public class Image implements Serializable {

    private String id;

    private String downloadLink;

    public Image() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String url) {
        this.downloadLink = url;
    }
}
