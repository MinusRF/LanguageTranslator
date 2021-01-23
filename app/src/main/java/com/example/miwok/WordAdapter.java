package com.example.miwok;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import static android.view.View.GONE;

public class WordAdapter extends ArrayAdapter<Word> {


    private int colorId;
    public WordAdapter(Activity context, int simple_list_item_1, ArrayList<Word> nums, int colorid) {
        super(context, 0, nums);
        colorId = colorid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Word currentWord =  getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView miwokTextView = listItemView.findViewById(R.id.miwok_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        miwokTextView.setText(currentWord.getMiwokTranslation());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView defaultTextView = listItemView.findViewById(R.id.english_view);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        defaultTextView.setText(currentWord.getDefaultTranslation());

        ImageView image = (ImageView) listItemView.findViewById(R.id.img);
        if(currentWord.hasImage()){
            image.setImageResource(currentWord.getImgAddress());
            image.setVisibility(View.VISIBLE);
        }

        else
            image.setVisibility(GONE);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), colorId);
        textContainer.setBackgroundColor(color);
        return listItemView;
    }

}

