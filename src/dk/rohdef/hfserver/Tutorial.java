package dk.rohdef.hfserver;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by rohdef on 7/12/15.
 */
@XmlRootElement
public class Tutorial implements Serializable {
    private String title, content;
    private String videoId;
    private String[] tags;

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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
