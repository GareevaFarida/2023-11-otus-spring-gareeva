package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "comment_text", nullable = false, length = 1024)
    private String text;

    @ManyToOne(targetEntity = Book.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Override
    public String toString() {
        return "Comment{id=%d, text='%s'}".formatted(id, text);
    }
}