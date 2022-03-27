package com.ratmirdudin.jblog_server.facades;

import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.payloads.responses.UserRest;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserRest convertToRest(User user) {
        UserRest userRest = new UserRest();

        userRest.setId(user.getId());
        userRest.setUsername(user.getUsername());
        userRest.setEmail(user.getEmail());
        userRest.setFirstname(user.getFirstname());
        userRest.setLastname(user.getLastname());
        userRest.setEnabled(user.isEnabled());

        return userRest;
    }

}
