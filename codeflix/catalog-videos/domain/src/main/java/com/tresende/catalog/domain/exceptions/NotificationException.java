package com.tresende.catalog.domain.exceptions;


import com.tresende.catalog.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }

    public static NotificationException with(final String aMessage, final Notification notification) {
        return new NotificationException(aMessage, notification);
    }
}
