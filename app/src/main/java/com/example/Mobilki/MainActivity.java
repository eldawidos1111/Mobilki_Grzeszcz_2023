package com.example.Mobilki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Mobilki.api.MainActivityApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


import com.example.Mobilki.database.entity.Book;
import com.example.Mobilki.view.BookViewModel;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    public static final int API_ACTIVITY_REQUEST_CODE = 3;

    private BookViewModel bookViewModel;
    private Book editedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        bookViewModel.findAll().observe(this, adapter::setBooks);

        FloatingActionButton addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
            startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
        });

        FloatingActionButton horoscopeButton = findViewById(R.id.switch_button);
        horoscopeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivityApi.class);
            startActivityForResult(intent, API_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Book book = new Book(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),
                    data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            bookViewModel.insert(book);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_added),
                    Snackbar.LENGTH_LONG).show();
        } else if (requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            editedBook.setTitle(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE));
            editedBook.setAuthor(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            bookViewModel.update(editedBook);
            editedBook = null;
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_edited),
                    Snackbar.LENGTH_LONG).show();
        } else {
            if (requestCode != API_ACTIVITY_REQUEST_CODE){
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.empty_not_saved),
                                Snackbar.LENGTH_LONG)
                        .show();
            }

        }
    }

    private class BookHolder extends RecyclerView.ViewHolder {
        private final TextView bookTitleTextView;
        private final TextView bookAuthorTextView;
        private Book book;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            View bookItem = itemView.findViewById(R.id.book_item);
            bookItem.setOnLongClickListener(v -> {
                bookViewModel.delete(book);
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.book_deleted),
                                Snackbar.LENGTH_LONG)
                        .show();
                return true;
            });
            bookItem.setOnClickListener(v -> {
                editedBook = book;
                Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                intent.putExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE, bookTitleTextView.getText());
                intent.putExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR, bookAuthorTextView.getText());
                startActivityForResult(intent, EDIT_BOOK_ACTIVITY_REQUEST_CODE);
            });
        }

        public void bind(Book book) {
            bookTitleTextView.setText(book.getTitle());
            bookAuthorTextView.setText(book.getAuthor());
            this.book = book;
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(BookHolder holder, int position) {
            if (books != null) {
                Book book = books.get(position);
                holder.bind(book);
            } else {
                Log.d("MainActivity", "No books");
            }
        }

        public int getItemCount() {
            if (books != null) {
                return books.size();
            } else {
                return 0;
            }
        }

        void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }
}