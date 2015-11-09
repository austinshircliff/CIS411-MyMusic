package com.wesleyreisz.mymusic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wesleyreisz.mymusic.MyMusicApplication;
import com.wesleyreisz.mymusic.R;
import com.wesleyreisz.mymusic.model.Song;
import com.wesleyreisz.mymusic.model.SongSource;
import com.wesleyreisz.mymusic.utils.HttpUtil;
import com.wesleyreisz.mymusic.utils.SongUtil;
import com.wesleyreisz.mymusic.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SongListFragment extends Fragment {
    public static final String TAG = "Song";
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
        songs = new ArrayList<Song>();

        listView = (ListView) view.findViewById(R.id.listViewSongs);

        getSongList(SongSource.ITUNES);

        return view;
    }

    private void showSongs(){
        // Create an ArrayAdapter for the ListView
        arrayAdapter = new SongAdapter(getActivity(),
                R.layout.layout_for_each_song,
                songs);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //let the activity know
                if ( songs.get(position).getSongId()!=null){
                    onItemChange.ItemClicked(position, songs.get(position).getSongId());
                }else{
                    Toast toast = Toast.makeText(getActivity(),"Not Enabled Yet", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }

    private void getSongList(SongSource source){
        if (source==SongSource.PARSE) {
            getSongsFromParse();
        }else if (source == SongSource.ITUNES){
            getSongsFromItunes();
        }
    }

    private void getSongsFromParse(){
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
                        }
                    } else {
                        Log.d(TAG, "error: " + e.getMessage());
                    }
                    showSongs();
                }

            });
    }

    private void getSongsFromItunes(){
        String url = StringUtil.buildTopTenListUrl(MyMusicApplication.ITUNES_URL, MyMusicApplication.MAX_TO_GET);
        Log.d(TAG, "url: " + url);
        new GetITunesTopTenAsyncTask().execute(url);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        onItemChange=(OnItemChange) activity;
    }

    public interface OnItemChange{
        public void ItemClicked(int changeToSongPosition, String songName);
    }


    private class GetITunesTopTenAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(params[0]!=null){
                return HttpUtil.getJson(params[0]);
            }else{
                Log.d(TAG, "Invalid No url provided");
                return "Invalid No url provided";
            }
        }

        @Override
        protected void onPostExecute(String strJson) {
            Log.d("Songs", strJson);
            songs = SongUtil.mapSongs(strJson);
            showSongs();
        }
    }
}
