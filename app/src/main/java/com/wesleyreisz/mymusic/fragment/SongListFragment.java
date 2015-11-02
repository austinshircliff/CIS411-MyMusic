package com.wesleyreisz.mymusic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wesleyreisz.mymusic.R;
import com.wesleyreisz.mymusic.model.Song;
import com.wesleyreisz.mymusic.service.MockMusicService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SongListFragment extends Fragment {
    private OnItemChange onItemChange;
    private List<Song> songs;
    private ListView listView;
    private SongAdapter arrayAdapter;

    public SongListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_new_list, container, false);

        listView = (ListView) view.findViewById(R.id.listViewSongs);
        songs = new ArrayList<Song>();

        // Create an ArrayAdapter for the ListView
        arrayAdapter = new SongAdapter(getActivity(),
                R.layout.layout_for_each_song,
                songs);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //let the activity know
                onItemChange.ItemClicked(position, songs.get(position).getSongId());
            }
        });

        refreshSongList();
        return view;
    }

    private void refreshSongList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Song");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> songList, ParseException e) {
                if (e == null) {
                    songs.clear();
                    for (ParseObject s : songList) {
                        Song song = new Song(
                                s.getObjectId(),
                                s.getString("songTitle"),
                                s.getString("artistTitle"),
                                s.getString("album"),
                                s.getDate("date")
                        );
                        songs.add(song);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }else{
                    Log.d("Song", "error: " + e.getMessage());
                }
            }

        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        onItemChange=(OnItemChange) activity;

    }

    public interface OnItemChange{
        public void ItemClicked(int changeToSongPosition, String songName);
    }
}
