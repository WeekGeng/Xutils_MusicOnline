package com.example.administrator.xutils_musiconline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.xutils_musiconline.contans.GloableContans;
import com.example.administrator.xutils_musiconline.entity.Music;
import com.example.administrator.xutils_musiconline.refresh.SwipeRefreshLayout;
import com.example.administrator.xutils_musiconline.utils.JsonParser;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SwipeRefreshLayout.OnLoadListener,MyMusicAdapter.GetPosition{
    @ViewInject(R.id.main_recycler_view)
    RecyclerView rcv;
    @ViewInject(R.id.refresh)
    SwipeRefreshLayout refresh;
    @ViewInject(R.id.song_name)
    TextView song_name;
    @ViewInject(R.id.singer_name)
    TextView singer_name;
    @ViewInject(R.id.singer_touxiang)
    ImageView singer_touxiang;
    SharedPreferences preference;
    List<Music> musics;
    AlertDialog dialog;
    MyMusicAdapter adapter;
    //音乐的位置
    private String songPosition;
    private LinearLayoutManager mLayoutManager;

    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference=getSharedPreferences("songPositon", Context.MODE_PRIVATE);
        ViewUtils.inject(this);
        songPosition=preference.getString("position", null);
        refresh.setMode(SwipeRefreshLayout.Mode.BOTH);
        refresh.setLoadNoFull(false);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadListener(this);
        refresh.setColor(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
        RequestParams params = new RequestParams();
        final HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000*10);
        http.send(HttpRequest.HttpMethod.GET,
                GloableContans.BASEURL+"loadMusics.jsp",
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        Toast.makeText(MainActivity.this,"准备下载",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i("responseInfo", responseInfo.result.toString());

                        String resJson=responseInfo.result.toString();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(resJson);
                            String res = obj.getString("result");
                            if ("ok".equals(res)) {
                                JSONArray ary = obj.getJSONArray("data");
                                musics = JsonParser.parse(ary);
                                initView();
                                Log.i("musics", "" + musics.size());
                                rcv.setHasFixedSize(true);
                                // use a linear layout manager
                                mLayoutManager = new LinearLayoutManager(MainActivity.this);
                                rcv.setLayoutManager(mLayoutManager);
                                rcv.setItemAnimator(new DefaultItemAnimator());
                                adapter=new MyMusicAdapter(musics,MainActivity.this);
                                rcv.setAdapter(adapter);
                                adapter.setOnclickListener(MainActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(MainActivity.this,"下载失败"+error.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.i("errorHttp",error.getMessage().toString());
                    }
                });
    }
    private void initView() {
        songPosition=preference.getString("position", null);
        if (songPosition!=null){
            Music music=musics.get(Integer.valueOf(songPosition));
            song_name.setText(music.getAlbum());
            singer_name.setText(music.getName());
            BitmapUtils bitmapUtils=new BitmapUtils(MainActivity.this);
            BitmapDisplayConfig config=new BitmapDisplayConfig();
            config.setLoadingDrawable(getResources().getDrawable(R.drawable.loading));
            config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.fail));
            config.setBitmapMaxSize(new BitmapSize(480, 720));
            bitmapUtils.display(singer_touxiang, GloableContans.BASEURL + music.getAlbumpic(), config, new BitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

                }
            });
        }
    }
    @OnChildClick(R.id.start_pause_song)
    public void startAndPauseOnClick(View v){
        songPosition=preference.getString("position", null);
        if (songPosition!=null){

        }
    }
    @OnChildClick(R.id.before_song)
    public void BeforeOnclick(View v){
        songPosition=preference.getString("position", null);
    }
    @OnChildClick(R.id.next_song)
    public void nextOnclick(View v){
        songPosition=preference.getString("position", null);
    }
    @Override
    public void onRefresh() {
        refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000);
    }
    @Override
    public void onLoad() {
        refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setLoading(false);
            }
        }, 1000);
    }
    @Override
    public void getposition(int position) {
        SharedPreferences.Editor editor=preference.edit();
        editor.putString("position", "" + position);
        editor.commit();
        Music music=musics.get(position);
        song_name.setText(music.getAlbum());
        singer_name.setText(music.getName());
        BitmapUtils bitmapUtils=new BitmapUtils(MainActivity.this);
        BitmapDisplayConfig config=new BitmapDisplayConfig();
        config.setLoadingDrawable(getResources().getDrawable(R.drawable.loading));
        config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.fail));
        config.setBitmapMaxSize(new BitmapSize(480, 720));
        bitmapUtils.display(singer_touxiang, GloableContans.BASEURL + music.getAlbumpic(), config, new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                imageView.setImageBitmap(bitmap);
            }
            @Override
            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

            }
        });
    }
}
