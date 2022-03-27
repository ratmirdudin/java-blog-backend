package com.ratmirdudin.jblog_server.facades;

import com.ratmirdudin.jblog_server.models.entities.Comment;
import com.ratmirdudin.jblog_server.payloads.responses.CommentRest;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommentFacade {

    public CommentRest convertToRest(Comment comment) {
        CommentRest commentRest = new CommentRest();

        commentRest.setId(comment.getId());
        commentRest.setMessage(comment.getMessage());

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", comment.getUserId());
        userMap.put("username", comment.getUsername());
        commentRest.setUser(userMap);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date createdDate = Date.from(comment.getCreatedAt());
        Date updatedDate = Date.from(comment.getUpdatedAt());
        commentRest.setCreatedDate(formatter.format(createdDate));
        commentRest.setUpdatedDate(formatter.format(updatedDate));

        return commentRest;
    }
}
