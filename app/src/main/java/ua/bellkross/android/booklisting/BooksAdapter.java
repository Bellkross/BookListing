package ua.bellkross.android.booklisting;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Book> {

    public BooksAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        Book book = getItem(position);

        TextView title = listItem.findViewById(R.id.title);
        title.setText(book.getTitle());

        TextView authors = listItem.findViewById(R.id.authors);
        authors.setText(book.getAuthors());

        return listItem;
    }
}
