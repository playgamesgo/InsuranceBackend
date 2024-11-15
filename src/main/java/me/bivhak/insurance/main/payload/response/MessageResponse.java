package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Response payload for a message.
 */
@Getter
@Setter
public final class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
