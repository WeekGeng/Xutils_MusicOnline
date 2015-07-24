package com.example.administrator.xutils_musiconline.utils;

import com.example.administrator.xutils_musiconline.entity.Music;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/24.
 */
public class JsonParser {
    /**
     *
     * @param ary
     * @return
     * @throws JSONException
     */
    public static List<Music> parse(JSONArray ary)throws JSONException {
        List<Music> musics=new ArrayList<Music>();
        for(int i=0; i<ary.length(); i++){
            JSONObject obj=ary.getJSONObject(i);
            Music music=new Music();
            music.setId(obj.getInt("id"));
            music.setAlbum(obj.getString("album"));
            music.setAlbumpic(obj.getString("albumpic"));
            music.setAuthor(obj.getString("author"));
            music.setComposer(obj.getString("composer"));
            music.setDowncount(obj.getString("downcount"));
            music.setDurationtime(obj.getString("durationtime"));
            music.setFavcount(obj.getString("favcount"));
            music.setMusicpath(obj.getString("musicpath"));
            music.setName(obj.getString("name"));
            music.setSinger(obj.getString("singer"));
            musics.add(music);
        }
        return musics;
    }
}
