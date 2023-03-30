package sk.tuke.gamestudio.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.service.CommentException;
import sk.tuke.gamestudio.common.service.CommentService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}")
    public ResponseEntity<?> getTopCommentsByGame(@PathVariable String game) {
        try {
            List<Comment> comments = commentService.getComments(game);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(Map.of("message","No comments found for game " + game), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(comments);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postComment(@RequestBody Comment comment) {
        try {
            comment.setCommentedOn(new Timestamp(System.currentTimeMillis()));
            commentService.addComment(comment);
            return new ResponseEntity<>(Map.of("message","Comment added"), HttpStatus.CREATED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }
}

