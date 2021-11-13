package dev.renegade.bookmarkit;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;


/**
 * IDs are not returned by RestRepository by default. I like them exposed so that the client can easily find
 * the ID of created and listed resources.
 * */
@Component
public class RestRepositoryConfigurator implements RepositoryRestConfigurer {

  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
      config.exposeIdsFor(Bookmark.class);
    }

}