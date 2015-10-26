package com.wesleyreisz.mymusic.fragment;

import com.wesleyreisz.mymusic.R;

/**
 * Created by wesleyreisz on 10/25/15.
 */
public class SongFactory {
   public static int findSong(String song){
       if("Dark Horse".equalsIgnoreCase(song)){
           return R.raw.dark_horse;
       }else if("Watch me".equalsIgnoreCase(song)){
           return R.raw.watch_me;
       }else if("Kryptonite".equalsIgnoreCase(song)){
           return R.raw.kryptonite;
       }else{
           return R.raw.watch_me;
       }
   }
}
