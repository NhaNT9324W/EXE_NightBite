package com.nightbite.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    /** Tiện ích: "User không tìm thấy với id: 42" */
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " không tìm thấy với id: " + id);
    }
}
