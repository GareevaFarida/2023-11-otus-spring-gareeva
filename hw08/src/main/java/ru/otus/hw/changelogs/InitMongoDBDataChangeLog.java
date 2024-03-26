package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private AuthorDto authorDtoPuskin;

    private AuthorDto authorDtoTolstoj;

    private GenreDto genreDtoTales;

    private GenreDto genreDtoRoman;

    private BookDto bookDtoGoldFish;

    private BookDto bookDtoWarAndPeace;

    @ChangeSet(order = "000", id = "dropDB", author = "farida", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "farida", runAlways = true)
    public void initAuthors(AuthorService authorService) {
        authorDtoPuskin = authorService.insert("А.С.Пушкин");
        authorDtoTolstoj = authorService.insert("Л.Н.Толстой");
    }

    @ChangeSet(order = "002", id = "initGenres", author = "farida", runAlways = true)
    public void initGenres(GenreService genreService) {
        genreDtoTales = genreService.insert("Сказки");
        genreDtoRoman = genreService.insert("Роман");
    }

    @ChangeSet(order = "003", id = "initBooks", author = "farida", runAlways = true)
    public void initBooks(BookService bookService) {
        bookDtoGoldFish = bookService.insert("Сказка о золотой рыбке",
                authorDtoPuskin.getId(), genreDtoTales.getId());
        bookDtoWarAndPeace = bookService.insert("Война и мир. Том 1",
                authorDtoTolstoj.getId(), genreDtoRoman.getId());
    }

    @ChangeSet(order = "004", id = "initComments", author = "farida", runAlways = true)
    public void initComments(BookService bookService) {
        bookService.addComment(bookDtoGoldFish.getId(), "Автор, пиши еще!");
        bookService.addComment(bookDtoGoldFish.getId(), "Какие-то слова странные, сейчас так уже не говорят.");
        bookService.addComment(bookDtoWarAndPeace.getId(), "Почему так длинно, нас в школе читать заставили!!!");
        bookService.addComment(bookDtoWarAndPeace.getId(), "Ребят, там 4 тома, держитесь!");
        bookService.addComment(bookDtoWarAndPeace.getId(), "Почему так много французского?");
    }

}
