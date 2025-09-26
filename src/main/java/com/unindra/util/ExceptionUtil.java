package com.unindra.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Component
public class ExceptionUtil {

    private static MessageSource source;

    public ExceptionUtil(MessageSource source) {
        ExceptionUtil.source = source;
    }

    public static ResponseStatusException badRequest(String key, Locale locale) {
        String message = source.getMessage(key, null, locale);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    public static ResponseStatusException notFound(String key, Locale locale) {
        String message = source.getMessage(key, null, locale);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
