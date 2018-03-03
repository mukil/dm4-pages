package de.mikromedia.webpages.model;

import de.deepamehta.core.Topic;
import de.deepamehta.core.service.CoreService;
import static de.mikromedia.webpages.WebpageService.ASSOCIATION;
import static de.mikromedia.webpages.WebpageService.DEEPAMEHTA_FILE;
import static de.mikromedia.webpages.WebpageService.FILE_PATH;
import static de.mikromedia.webpages.WebpageService.ROLE_DEFAULT;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import static de.mikromedia.webpages.WebpageService.TILE_HEADLINE;
import static de.mikromedia.webpages.WebpageService.BACKGROUND_COLOR;
import static de.mikromedia.webpages.WebpageService.FONT_COLOR;
import static de.mikromedia.webpages.WebpageService.IMAGE_LARGE;
import static de.mikromedia.webpages.WebpageService.IMAGE_SMALL;
import static de.mikromedia.webpages.WebpageService.LINK;
import static de.mikromedia.webpages.WebpageService.TILE;
import static de.mikromedia.webpages.WebpageService.TILE_HTML;

/**
 *
 * @author malt
 */
public class Tile {
    
    private Topic content;
    private Topic relatedTopic;

    public Tile(Topic sectionContent) {
        this.content = sectionContent;
        if (!isSectionContentTopic()) {
            throw new IllegalArgumentException("Given topic is not of type Section Content");
        }
        this.content.loadChildTopics();
    }

    public Tile(long topicId, CoreService dms) {
        this.content = dms.getTopic(topicId);
        if (!isSectionContentTopic()) {
            throw new IllegalArgumentException("Given topic is not of type Section Content");
        }
        this.content.loadChildTopics();
    }

    public long getId() {
        return this.content.getId();
    }

    public Topic getTopic() {
        return this.content;
    }

    // --- Custom Section Data Accessors
    
    public String getTitle() {
        return this.content.getChildTopics().getStringOrNull(TILE_HEADLINE);
    }

    public String getHtml() {
        return this.content.getChildTopics().getStringOrNull(TILE_HTML);
    }

    public String getLink() {
        return this.content.getChildTopics().getStringOrNull(LINK);
    }

    /**
     * If some DM standard topic is directly associated with a "Tile" topic
     * that topic should be given priority in the templates and become the actual section content.
     * @return  Topic   A standard topic replacing the (title, html) section content.
     **/
    public Topic getRelatedTopic() {
        if (this.relatedTopic != null) {
            this.relatedTopic = this.content.getRelatedTopic(ASSOCIATION, ROLE_DEFAULT,
                ROLE_DEFAULT, null);
        }
        return this.relatedTopic;
    }

    public String getSmallImage() {
        Topic imageFile = this.content.getRelatedTopic(IMAGE_SMALL, ROLE_DEFAULT,
                ROLE_DEFAULT, DEEPAMEHTA_FILE);
        return (imageFile == null) ? "" : imageFile.getChildTopics().getStringOrNull(FILE_PATH);
    }

    public String getLargeImage() {
        Topic imageFile = this.content.getRelatedTopic(IMAGE_LARGE, ROLE_DEFAULT,
                ROLE_DEFAULT, DEEPAMEHTA_FILE);
        return (imageFile == null) ? "" : imageFile.getChildTopics().getStringOrNull(FILE_PATH);
    }

    public String getBackgroundColor() {
        return this.content.getChildTopics().getStringOrNull(BACKGROUND_COLOR);
    }

    public String getFontColor() {
        return this.content.getChildTopics().getStringOrNull(FONT_COLOR);
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject()
                .put("title", getTitle())
                .put("html", getHtml())
                .put("related_topic", getRelatedTopic())
                .put("font_color", getFontColor())
                .put("bg_color", getBackgroundColor());
        } catch (JSONException ex) {
            Logger.getLogger(Webpage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean isSectionContentTopic() {
        if (this.content == null) return false;
        return (this.content.getTypeUri().equals(TILE));
    }
    
}
