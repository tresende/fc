package com.tresende.catalog.admin.infrastructure.services.local;

import com.tresende.catalog.admin.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(final Object event) {
        LOG.info("Event was sent: {}", event);
    }
}
