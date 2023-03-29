package sk.tuke.gamestudio.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.gamestudio.common.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByGame(String game);

    List<Comment> findFirst10ByGameOrderByIdDesc(String game);
}
