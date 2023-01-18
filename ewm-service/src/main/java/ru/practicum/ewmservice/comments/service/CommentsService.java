package ru.practicum.ewmservice.comments.service;


import ru.practicum.ewmservice.comments.dto.CommentsDto;

import java.util.List;

public interface CommentsService {

    List<CommentsDto> getComments(Long eventId, int from, int size);

    CommentsDto getCommentById(Long eventId, Long commentId);

    List<CommentsDto> getCommentsByUser(Long userId, int from, int size);

    CommentsDto createdComment(Long eventId, CommentsDto commentsDto);

    CommentsDto patchComment(Long eventId, Long commentId, Long userId, CommentsDto commentsDto);

    void deleteComments(Long eventId);

    void deleteCommentById(Long eventId, Long commentId, Long userId);
}
