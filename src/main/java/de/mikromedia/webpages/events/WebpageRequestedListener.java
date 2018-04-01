package de.mikromedia.webpages.events;

import de.deepamehta.core.service.EventListener;
import org.thymeleaf.context.AbstractContext;

/**
 *
 * @author malte
 */
public interface WebpageRequestedListener extends EventListener {
    
    void webpageRequested(AbstractContext context, String webAlias, String sitePrefix);

}
