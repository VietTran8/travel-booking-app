package com.web.travel.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MessageUtils {
    private static final ResourceBundle messageBundle = ResourceBundle.getBundle("message.messages", Locale.getDefault());

    public static String getMessage(String messageCode, Object... var) {
        String message = "";

        try {
            message = messageBundle.getString(messageCode);
        }catch (MissingResourceException e) {
            message = messageCode;
        }

        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, var);

        return formattingTuple.getMessage();
    }
}
