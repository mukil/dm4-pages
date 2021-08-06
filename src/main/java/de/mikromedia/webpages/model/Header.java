package de.mikromedia.webpages.model;

import static de.mikromedia.webpages.WebpageService.BUTTON;
import static de.mikromedia.webpages.WebpageService.HEADER_TITLE;
import static de.mikromedia.webpages.WebpageService.FILE_PATH;
import static de.mikromedia.webpages.WebpageService.HEADER;
import static de.mikromedia.webpages.WebpageService.HEADER_CONTENT;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import static de.mikromedia.webpages.WebpageService.BUTTON_TITLE;
import static de.mikromedia.webpages.WebpageService.DEFAULT_ATTACHMENT;
import static de.mikromedia.webpages.WebpageService.DEFAULT_SIZE;
import static de.mikromedia.webpages.WebpageService.IMAGE_ATTACHMENT_STYLE;
import static de.mikromedia.webpages.WebpageService.IMAGE_LARGE;
import static de.mikromedia.webpages.WebpageService.IMAGE_SIZE_STYLE;
import static de.mikromedia.webpages.WebpageService.IMAGE_SMALL;
import systems.dmx.core.Assoc;
import systems.dmx.core.RelatedTopic;
import systems.dmx.core.Topic;
import systems.dmx.core.service.CoreService;
import static de.mikromedia.webpages.WebpageService.DMX_FILE;
import static de.mikromedia.webpages.WebpageService.FONT_COLOR_ASSOC;
import static de.mikromedia.webpages.WebpageService.BACKGROUND_COLOR_ASSOC;
import static de.mikromedia.webpages.WebpageService.HEADER_JS;
import static de.mikromedia.webpages.WebpageService.WEBCLIENT_COLOR;
import static systems.dmx.core.Constants.DEFAULT;

public class Header {

    private Topic pageHeader;
    private RelatedTopic imageLarge;
    private RelatedTopic imageSmall;

    private Logger log = Logger.getLogger(getClass().getName());

    public Header(Topic pageHeader) {
        this.pageHeader = pageHeader;
        if (!isHeaderTopic()) {
            throw new IllegalArgumentException("Given topic is not of type Header");
        }
        this.pageHeader.loadChildTopics();
    }

    public Header(long topicId, CoreService dms) {
        this.pageHeader = dms.getTopic(topicId);
        if (!isHeaderTopic()) {
            throw new IllegalArgumentException("Given topic is not of type Header");
        }
        this.pageHeader.loadChildTopics();
    }

    public long getId() {
        return this.pageHeader.getId();
    }

    public Topic getTopic() {
        return this.pageHeader;
    }

    // --- Custom Header Data Accessors
    
    public String getTitle() {
        return this.pageHeader.getChildTopics().getString(HEADER_TITLE, null);
    }

    public String getContent() {
        return this.pageHeader.getChildTopics().getString(HEADER_CONTENT, null);
    }

    public String getHeaderJS() {
        return this.pageHeader.getChildTopics().getString(HEADER_JS, null);
    }

    public List<Button> getButtons() {
        List<Button> headerButtons = new ArrayList();
        List<RelatedTopic> buttons = this.pageHeader.getChildTopics().getTopicsOrNull(BUTTON);
        if (buttons != null) {
            for (RelatedTopic topic : buttons) {
                Topic buttonTitle = topic.getChildTopics().getTopicOrNull(BUTTON_TITLE);
                if (buttonTitle != null && !buttonTitle.getSimpleValue().toString().isEmpty()) {
                    Button button = new Button(topic);
                    headerButtons.add(button);
                }
            }
        }
        return headerButtons;
    }

    public String getSmallImage() {
        if (this.imageSmall != null)
            return this.imageSmall.getChildTopics().getString(FILE_PATH, null);
        this.imageSmall = this.pageHeader.getRelatedTopic(IMAGE_SMALL, DEFAULT,
                DEFAULT, DMX_FILE);
        return (this.imageSmall == null) ? "" : this.imageSmall.getChildTopics().getString(FILE_PATH, null);
    }

    public String getSmallImageSize() {
        String val = null;
        if (imageSmall == null) getSmallImage();
        if (imageSmall != null) {
            Assoc imageConfig = imageSmall.getRelatingAssoc();
            val = imageConfig.getChildTopics().getString(IMAGE_SIZE_STYLE, null);
        }
        return (val == null) ? DEFAULT_SIZE : val.toLowerCase();
    }

    public String getSmallImageAttachment() {
        String val = null;
        if (imageSmall != null) {
            Assoc imageConfig = imageSmall.getRelatingAssoc();
            val = imageConfig.getChildTopics().getString(IMAGE_ATTACHMENT_STYLE, null);
        }
        return (val == null) ? DEFAULT_ATTACHMENT : val.toLowerCase();
    }

    public String getLargeImage() {
        if (this.imageLarge != null)
            return this.imageLarge.getChildTopics().getString(FILE_PATH, null);
        this.imageLarge = this.pageHeader.getRelatedTopic(IMAGE_LARGE, DEFAULT,
                DEFAULT, DMX_FILE);
        return (this.imageLarge == null) ? "" : this.imageLarge.getChildTopics().getString(FILE_PATH, null);
    }

    public String getLargeImageSize() {
        String val = null;
        if (imageLarge == null) getLargeImage();
        if (imageLarge != null) {
            Assoc imageConfig = imageLarge.getRelatingAssoc();
            val = imageConfig.getChildTopics().getString(IMAGE_SIZE_STYLE, null);
        }
        return (val == null) ? DEFAULT_SIZE : val.toLowerCase();
    }

    public String getLargeImageAttachment() {
        String val = null;
        if (imageLarge != null) {
            Assoc imageConfig = imageLarge.getRelatingAssoc();
            val = imageConfig.getChildTopics().getString(IMAGE_ATTACHMENT_STYLE, null);
        }
        return (val == null) ? DEFAULT_ATTACHMENT : val.toLowerCase();
    }

    public String getBackgroundColor() {
        return this.pageHeader.getChildTopics().getString(WEBCLIENT_COLOR + "#" + BACKGROUND_COLOR_ASSOC, null);
    }

    public String getFontColor() {
        return this.pageHeader.getChildTopics().getString(WEBCLIENT_COLOR + "#" + FONT_COLOR_ASSOC, null);
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject()
                .put("title", getTitle())
                .put("buttons", getButtons())
                .put("font_color", getFontColor())
                .put("bg_color", getBackgroundColor());
        } catch (JSONException ex) {
            Logger.getLogger(Webpage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean isHeaderTopic() {
        if (this.pageHeader == null) return false;
        return (this.pageHeader.getTypeUri().equals(HEADER));
    }
    
}
