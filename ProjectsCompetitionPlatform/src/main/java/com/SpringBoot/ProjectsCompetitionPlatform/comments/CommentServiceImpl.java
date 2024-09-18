package com.SpringBoot.ProjectsCompetitionPlatform.comments;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public void create(Comment comment) {
        commentRepository.create(comment);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Override
    public List<Comment> getAllByUsername(String username) {
        return commentRepository
                .getAll()
                .stream()
                .filter(checkUsername(username))
                .toList();
    }

    private Predicate<Comment> checkUsername(String username) {
        return comment -> {
            if (username == null) {
                return true;
            } else {
                return comment.getAuthor().getName().equals(username);
            }
        };
    }

    @Override
    public boolean delete(Comment comment) {
        return commentRepository.delete(comment);
    }
}
