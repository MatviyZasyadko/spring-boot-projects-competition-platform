package com.SpringBoot.ProjectsCompetitionPlatform.comments;

import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.users.User;
import com.SpringBoot.ProjectsCompetitionPlatform.votes.Vote;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRepository {

    final List<Comment> comments = new ArrayList<>(List.of(
            new Comment("", new User(), new Project(), null, new Date(), null),
            new Comment("", new User(), new Project(), null, new Date(), null),
            new Comment("", new User(), new Project(), null, new Date(), null),
            new Comment("", new User(), new Project(), null, new Date(), null)
    ));

    public void create(Comment comment) {
        comments.add(comment);
    }
    public List<Comment> getAll() {
        return comments;
    }

    public Comment getOneByUsername(String name) {
        return comments.stream()
                .filter(comment -> comment.getAuthor().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean delete(Comment comment) {
        return comments.remove(comment);
    }


}
