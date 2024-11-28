package com.ukma.competition.platform.comments;

import com.ukma.competition.platform.comments.dto.CommentDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl extends GenericServiceImpl<CommentEntity, String, CommentRepository> implements CommentService {
    public CommentServiceImpl(CommentRepository repository) {
        super(repository);
    }

    public CommentDto convertToDto(CommentEntity comment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());

        String createdAt = formatter.format(comment.getCreatedAt());

        return new CommentDto(
                comment.getAuthor(),
                comment.getText(),
                createdAt);
    }
}
