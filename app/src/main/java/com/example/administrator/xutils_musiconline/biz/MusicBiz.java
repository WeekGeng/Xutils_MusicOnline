package com.example.administrator.xutils_musiconline.biz;
import android.os.AsyncTask;

import com.example.administrator.xutils_musiconline.entity.Music;

import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 */
public class MusicBiz extends AsyncTask<String,String,List<Music>>{
    @Override
    protected List<Music> doInBackground(String... params) {
        List<Music> list=null;
        String url=params[0];
        return null;
    }
    @Override
    protected void onPostExecute(List<Music> musics) {
        super.onPostExecute(musics);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
