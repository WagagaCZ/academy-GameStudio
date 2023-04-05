package sk.tuke.gamestudio.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.service.CommentException;
import sk.tuke.gamestudio.server.repository.CommentRepository;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CommentServiceJPATest {

    @Autowired
    private CommentServiceJPA commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void addComment() throws CommentException {
        Comment newComment = new Comment("test", "test", "test", new Timestamp(System.currentTimeMillis()));
        commentService.addComment(newComment);

        Comment result = commentRepository.findById(newComment.getId()).get();

        assertEquals(newComment, result);
    }

    @Test
    void getComments() throws CommentException {
        commentRepository.save(new Comment("player", "game", "comment", new Timestamp(System.currentTimeMillis())));

        Comment result = commentRepository.findFirst10ByGameOrderByIdDesc("game").get(0);

        assertEquals("game", result.getGame());
        assertEquals("player", result.getPlayer());
        assertEquals("comment", result.getComment());
    }
}