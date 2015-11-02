package com.wesleyreisz.mymusic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.wesleyreisz.mymusic.R;

import java.util.Date;

/**
 * Created by wesleyreisz on 11/1/15.
 */
public class SongAddFragment extends Fragment {
    private OnReloadClick onReloadClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_add_song, container, false);

        Button btnAddSong = (Button) view.findViewById(R.id.btnSongAdd);
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: implement validation
                EditText songTitle = (EditText)view.findViewById(R.id.songTitle);
                EditText artistName = (EditText)view.findViewById(R.id.txtArtist);
                EditText albumName = (EditText)view.findViewById(R.id.songAlbumName);
                EditText albumDate = (EditText)view.findViewById(R.id.songPublishedDate);

                //add song
                ParseObject songObject = new ParseObject("Song");
                songObject.put("songTitle",songTitle.getText().toString());
                songObject.put("artistTitle",artistName.getText().toString());
                songObject.put("album",albumName.getText().toString());

                //todo: implement a datepicker
                songObject.put("date",new Date());

                songObject.saveInBackground();

                onReloadClick.reload();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        onReloadClick = (OnReloadClick) activity;
    }

    public interface OnReloadClick{
        public void reload();
    }
}
