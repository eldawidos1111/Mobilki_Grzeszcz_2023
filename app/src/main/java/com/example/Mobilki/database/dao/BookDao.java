package com.example.Mobilki.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.Mobilki.database.entity.Book;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book book);

    @Update
    public void update(Book book);

    @Delete
    public void delete(Book book);

    @Query("DELETE FROM book")
    public void deleteAll();

    @Query("SELECT * FROM book ORDER BY title")
    public LiveData<List<Book>> findAll();

    @Query("SELECT * FROM book WHERE title LIKE :title")
    public LiveData<List<Book>> findByTitle(String title);

}
