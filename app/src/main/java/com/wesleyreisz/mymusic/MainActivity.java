package com.wesleyreisz.mymusic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wesleyreisz.mymusic.fragment.SongAddFragment;
import com.wesleyreisz.mymusic.fragment.SongListFragment;
import com.wesleyreisz.mymusic.fragment.SongFragment;
import com.wesleyreisz.mymusic.model.Song;

public class MainActivity extends Activity implements
        SongListFragment.OnItemChange,
        SongFragment.OnReloadClick,
        SongAddFragment.OnReloadClick{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SongListFragment listFragment = new SongListFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, listFragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_music, menu);
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
        }else if(id == R.id.action_new){
            addSong();
            return true;
        }else if(id == R.id.action_refresh){
            reload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ItemClicked(int changeToSongPosition, String songId) {
        //Song song = new MockMusicService().findOne(songTitle);
        Song song = new Song();//from songid

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Song");
        query.getInBackground(songId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Song song = new Song(
                            object.getObjectId(),
                            object.getString("songTitle"),
                            object.getString("artistTitle"),
                            object.getString("album"),
                            object.getDate("date")
                    );

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    SongFragment songFragment = new SongFragment();
                    songFragment.setSong(song);
                    loadFragmentIntoCorrectPane(fragmentTransaction, songFragment);
                    fragmentTransaction.commit();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Unable to get Song", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }


    @Override
    public void reload() {
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SongListFragment listFragment = new SongListFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, listFragment);
        fragmentTransaction.commit();
    }

    private void addSong() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        loadFragmentIntoCorrectPane(fragmentTransaction,new SongAddFragment());
        fragmentTransaction.commit();
    }

    private void loadFragmentIntoCorrectPane(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if(findViewById(R.id.fragmentContainerRight)!=null){
            fragmentTransaction.replace(R.id.fragmentContainerRight, fragment);
        }else{
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        }
    }

}
