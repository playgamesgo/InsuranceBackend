package me.bivhak.insurance.main.payload.response;

import lombok.Getter;
import lombok.Setter;
import me.bivhak.insurance.main.models.User;

import java.util.Date;

/**
 * Response payload for user details.
 */
@Getter
@Setter
public final class UserResponse {
    long id;
    String login;
    String email;
    String name;
    Date createdOn;


    public UserResponse(long id, String login, String email, Date createdAt) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.createdOn = createdAt;
    }

    public static UserResponse fromUser(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
