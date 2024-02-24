package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(String id) {
        return repository.findById(id)
                .map(val -> modelMapper.map(val, CommentDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllCommentsByBookId(String bookId) {
        return repository.findAllByBookId(bookId).stream()
                .map(val -> modelMapper.map(val, CommentDto.class))
                .toList();
    }

    @Override
    @Transactional
    public CommentDto save(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return modelMapper.map(repository.save(comment), CommentDto.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
