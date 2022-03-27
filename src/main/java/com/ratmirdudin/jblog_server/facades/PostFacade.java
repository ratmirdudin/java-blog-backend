package com.ratmirdudin.jblog_server.facades;

import com.ratmirdudin.jblog_server.models.entities.Post;
import com.ratmirdudin.jblog_server.payloads.responses.CommentRest;
import com.ratmirdudin.jblog_server.payloads.responses.DetailPostRest;
import com.ratmirdudin.jblog_server.payloads.responses.PostRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostFacade {

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    @Autowired
    private CommentFacade commentFacade;

    public DetailPostRest convertToDetailPostRest(Post post) {
        DetailPostRest detailPostRest = new DetailPostRest();
        detailPostRest.setId(post.getId());
        detailPostRest.setTitle(post.getTitle());
        detailPostRest.setContent(post.getContent());

//        Map<String, Object> userMap = new HashMap<>();
//        userMap.put("id", post.getUser().getId());
//        userMap.put("username", post.getUsername());
//        detailPostRest.setUser(userMap);
        detailPostRest.setUsername(post.getUsername());


        List<CommentRest> comments = post.getComments()
                .stream()
                .map(commentFacade::convertToRest)
                .collect(Collectors.toList());
        detailPostRest.setComments(comments);


        Date createdDate = Date.from(post.getCreatedAt());
        Date updatedDate = Date.from(post.getUpdatedAt());
        detailPostRest.setCreatedDate(formatter.format(createdDate));
        detailPostRest.setUpdatedDate(formatter.format(updatedDate));

        return detailPostRest;
    }

    public PostRest convertToPostRest(Post post) {
        PostRest postRest = new PostRest();
        postRest.setId(post.getId());
        postRest.setTitle(post.getTitle());
        postRest.setContent(post.getContent());

//        Map<String, Object> userMap = new HashMap<>();
//        userMap.put("id", post.getUser().getId());
//        userMap.put("username", post.getUsername());
//        postRest.setUser(userMap);
        postRest.setUsername(post.getUsername());

        Date createdDate = Date.from(post.getCreatedAt());
        Date updatedDate = Date.from(post.getUpdatedAt());
        postRest.setCreatedDate(formatter.format(createdDate));
        postRest.setUpdatedDate(formatter.format(updatedDate));

        return postRest;
    }

}
