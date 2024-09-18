package com.SpringBoot.ProjectsCompetitionPlatform.comments;

import java.util.List;

public interface CommentService {

    void create(Comment comment);
    List<Comment> getAll();
    List<Comment> getAllByUsername(String name);
    boolean delete(Comment comment);

}
