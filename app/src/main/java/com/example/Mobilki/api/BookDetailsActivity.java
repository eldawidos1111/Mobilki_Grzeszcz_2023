package com.example.Mobilki.api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.Mobilki.api.MainActivityApi.IMAGE_URL_BASE;

import com.example.Mobilki.R;

public class BookDetailsActivity extends AppCompatActivity {
    public final static String EXTRA_BOOK_OBJ = "EXTRA_BOOK_OBJ";

    private TextView bookTitleTextView;
    private TextView bookAuthorTextView;
    private ImageView bookCover;
    private TextView bookFirstPublishYearTextView;
    private TextView bookLanguagesTextView;
    private TextView bookTimesTextView;
    private TextView bookPersonsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        bookTitleTextView = findViewById(R.id.book_title);
        bookAuthorTextView = findViewById(R.id.book_author);
        bookFirstPublishYearTextView = findViewById(R.id.book_first_publish_year);
        bookLanguagesTextView = findViewById(R.id.book_languages);
        bookTimesTextView = findViewById(R.id.book_times);
        bookPersonsTextView = findViewById(R.id.book_persons);

        bookCover = findViewById(R.id.img_cover);

        Book book = (Book) getIntent().getSerializableExtra(EXTRA_BOOK_OBJ);

        bookTitleTextView.setText(book.getTitle());
        bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
        bookFirstPublishYearTextView.setText(String.valueOf(book.getFirstPublishYear()));
        bookLanguagesTextView.setText(TextUtils.join(", ", book.getLanguages()));
        bookTimesTextView.setText(TextUtils.join(", ", book.getTimes()));
        bookPersonsTextView.setText(TextUtils.join(", ", book.getPersons()));

        if (book.getCover() != null) {
            Picasso.with(getApplicationContext())
                    .load(IMAGE_URL_BASE + book.getCover() + "-L.jpg")
                    .placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
        } else {
            bookCover.setImageResource(R.drawable.ic_baseline_book_24);
        }
    }
}
