package com.web.travel.exception;

import com.web.travel.utils.MessageUtils;
import lombok.Setter;

@Setter
public class NotFoundException extends RuntimeException {
    private String message;
    public NotFoundException(String messageCode, Object... var) {
        this.message = MessageUtils.getMessage(messageCode, var);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
