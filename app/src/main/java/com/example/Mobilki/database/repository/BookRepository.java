package com.example.Mobilki.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.Mobilki.database.BookDatabase;
import com.example.Mobilki.database.dao.BookDao;
import com.example.Mobilki.database.entity.Book;

public class BookRepository {
    private final BookDao bookDao;
    private final LiveData<List<Book>> books;

    public BookRepository(Application application) {
        BookDatabase database = BookDatabase.getDatabase(application);
        bookDao = database.bookDao();
        books = bookDao.findAll();
    }

    public LiveData<List<Book>> findAllBooks() {
        return books;
    }

    public void insert(Book book) {
        BookDatabase.databaseWriterExecutor.execute(() -> {
            bookDao.insert(book);
        });
    }

    public void update(Book book) {
        BookDatabase.databaseWriterExecutor.execute(() -> {
            bookDao.update(book);
        });
    }

    public void delete(Book book) {
        BookDatabase.databaseWriterExecutor.execute(() -> {
            bookDao.delete(book);
        });
    }
}
