package sk.tuke.gamestudio.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.common.entity.Comment;
import sk.tuke.gamestudio.common.service.CommentException;
import sk.tuke.gamestudio.common.service.CommentService;
import sk.tuke.gamestudio.server.repository.CommentRepository;

import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void addComment(Comment comment) {
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CommentException("Error saving comment\n" + e.getMessage());
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try {
            return commentRepository.findFirst10ByGameOrderByIdDesc(game);
        } catch (Exception e) {
            throw new CommentException("Error getting comments\n" + e.getMessage());
        }
    }

    @Override
    public void reset() {
        try {
            commentRepository.deleteAll();
        } catch (Exception e) {
            throw new CommentException("Error resetting comments\n" + e.getMessage());
        }
    }
}
