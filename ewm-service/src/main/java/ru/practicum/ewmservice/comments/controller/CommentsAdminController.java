package ru.practicum.ewmservice.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.comments.service.CommentsService;

import static ru.practicum.ewmservice.comments.controller.CommentsAdminController.URL_ADMIN_COMMENTS;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = URL_ADMIN_COMMENTS)
@Slf4j
public class CommentsAdminController {

    private final CommentsService commentsService;
    public static final String URL_ADMIN_COMMENTS = "/events/{eventId}/comments";

    @DeleteMapping
    public void deleteComments(@PathVariable Long eventId) {
        log.info("URL: " + URL_ADMIN_COMMENTS + ". " +
                "DeleteMapping/Удаление всех комментариев у события " + eventId + "/deleteComments");
        commentsService.deleteComments(eventId);
    }

    @DeleteMapping(path = "/{commentId}/users/{userId}")
    public void deleteCommentById(
            @PathVariable Long eventId,
            @PathVariable Long commentId,
            @PathVariable Long userId) {
        log.info("URL: " + URL_ADMIN_COMMENTS + "/{commentId}. " +
                "DeleteMapping/Удаления комментария " + commentId + "/deleteCommentById");
        commentsService.deleteCommentById(eventId, commentId, userId);
    }
}
