package com.tresende.catalog.admin.domain;

import com.tresende.catalog.admin.domain.event.DomainEvent;
import com.tresende.catalog.admin.domain.event.DomainEventPublisher;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {

    protected final ID id;
    private final List<DomainEvent> domainEvents;

    public Entity(ID id, final List<DomainEvent> domainEvents) {
        Objects.requireNonNull(id, "Id should not be null");
        this.id = id;
        this.domainEvents = domainEvents == null ? Collections.emptyList() : domainEvents;
    }

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final var entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public abstract void validate(ValidationHandler handler);

    public void publishDomainEvents(final DomainEventPublisher<synDomainEvent> publisher) {
        if (publisher == null) return;
        getDomainEvents()
                .forEach(publisher::publishEvent);

        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if (event != null) {
            domainEvents.add(event);
        }
    }
}
