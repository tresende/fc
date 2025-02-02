package com.tresende.catalog.admin.domain.event;

import java.io.Serializable;
import java.time.Instant;

public interface DomainEvent extends Serializable {

    Instant occurredOn();
}
