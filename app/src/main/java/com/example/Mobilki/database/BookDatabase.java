package com.example.Mobilki.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.Mobilki.database.dao.BookDao;
import com.example.Mobilki.database.entity.Book;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {

    public abstract BookDao bookDao();

    private static volatile BookDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(() -> {
                BookDao dao = INSTANCE.bookDao();
                dao.deleteAll();

                Book book = new Book("C# 10 in a Nutshell The Definitive Reference", "Joseph Albahari");
                dao.insert(book);
            });
        }
    };

    public static BookDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BookDatabase.class, "book_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
