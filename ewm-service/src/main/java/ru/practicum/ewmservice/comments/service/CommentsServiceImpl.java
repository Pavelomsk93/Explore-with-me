package ru.practicum.ewmservice.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.comments.dto.CommentsDto;
import ru.practicum.ewmservice.comments.mapper.CommentsMapper;
import ru.practicum.ewmservice.comments.model.Comments;
import ru.practicum.ewmservice.comments.repository.CommentsRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.EventState;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.EntityNotFoundException;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;
import ru.practicum.ewmservice.util.PageRequestOverride;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentsDto> getComments(Long eventId, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        checkExistingEvent(eventId);

        return commentsRepository.findCommentOrderByEventId(eventId, pageRequest)
                .stream()
                .map(CommentsMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentsDto getCommentById(Long eventId, Long commentId) {
        checkExistingEvent(eventId);
        Comments comments = commentsRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Комментарий %s не существует.", commentId)));
        return CommentsMapper.toCommentDto(comments);
    }

    @Override
    public List<CommentsDto> getCommentsByUser(Long userId, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        checkExistingUser(userId);
        return commentsRepository.findCommentOrderByUserId(userId, pageRequest)
                .stream()
                .map(CommentsMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentsDto createdComment(Long eventId, CommentsDto commentsDto) {
        LocalDateTime created = LocalDateTime.now();
        validationBodyComment(commentsDto);
        Event event = checkExistingEvent(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            User user = checkExistingUser(commentsDto.getUser());
            Comments comments = CommentsMapper.toComment(commentsDto);
            comments.setUser(user);
            comments.setEvent(event);
            comments.setPublishedOn(created);
            commentsRepository.save(comments);
            return CommentsMapper.toCommentDto(commentsRepository.save(comments));
        } else {
            throw new EntityNotFoundException(
                    String.format("Событие %s не опубликовано. Оставить комментарий невозможно.", eventId));
        }
    }

    @Override
    @Transactional
    public CommentsDto patchComment(Long eventId, Long commentId, Long userId, CommentsDto commentsDto) {
        if (commentsDto.getUser().equals(userId)) {
            checkExistingEvent(eventId);
            validationBodyComment(commentsDto);
            Comments comments = checkExistingComments(commentId);
            comments.setText(commentsDto.getText());
            Comments comments1 = commentsRepository.save(comments);
            return CommentsMapper.toCommentDto(comments1);
        } else {
            throw new EntityNotFoundException(
                    String.format("Пользователь %s не может обновить чужой комментарий.", userId));
        }
    }

    @Override
    @Transactional
    public void deleteComments(Long eventId) {
        commentsRepository.deleteCommentOrderByEventId(eventId);
    }

    @Override
    @Transactional
    public void deleteCommentById(Long eventId, Long commentId, Long userId) {
        Comments comments = checkExistingComments(commentId);
        if (comments.getUser().getId().equals(userId)) {
            checkExistingEvent(eventId);
            commentsRepository.deleteById(commentId);
        } else {
            throw new EntityNotFoundException(
                    String.format("Пользователь %s не может удалить чужой комментарий.", userId));
        }
    }

    private Event checkExistingEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Событие %s не существует.", eventId)));
    }

    private Comments checkExistingComments(Long commentId) {
        return commentsRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Комментарий %s не существует.", commentId)));
    }

    private User checkExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь %s не существует.", userId)));
    }

    private static void validationBodyComment(CommentsDto commentsDto) {
        if (commentsDto.getText().isEmpty()) {
            throw new EntityNotFoundException("Текст комментария не может быть пустым");
        }
    }
}
