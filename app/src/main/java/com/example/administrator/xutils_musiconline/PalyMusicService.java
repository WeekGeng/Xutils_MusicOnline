package com.example.administrator.xutils_musiconline;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.administrator.xutils_musiconline.contans.GloableContans;

import java.io.IOException;

/**
 * Created by Administrator on 2015/7/24.
 */
public class PalyMusicService extends Service {
    private int size;
    private int position;
    private boolean isLoop=true;
    private MyTask task;
    private String url;
    //播放音乐使用的媒体播放器
    private MediaPlayer player=new MediaPlayer();
    private PalyMusicService receiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url=intent.getStringExtra("url");
        //播放音乐
        playMusic(url);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        receiver=new PalyMusicService();
//        IntentFilter filter=new IntentFilter();
//        filter.addAction(GloableContans.ACTION_NEXT_MUSIC);
//        filter.addAction(GloableContans.ACTION_PRE_MUSIC);
//        filter.addAction(GloableContans.ACTION_PLAY_MUSIC);
//        filter.addAction(GloableContans.ACTION_PROGRESS_CHANGERED);
//        this.registerReceiver(receiver, filter);
        task=new MyTask();
        task.execute(url);
    }
    class MyTask extends AsyncTask<String,Integer,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //给player设置监听
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    //播放完毕后  ...自己写吧

                }
            });
        }
        @Override
        protected Void doInBackground(String... strings) {
            while(isLoop){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(player.isPlaying()){
                    //发送广播
                    Intent intent=new Intent(GloableContans.ACTION_UPDATE_PROGRESS);
                    intent.putExtra("total", player.getDuration());
                    intent.putExtra("current", player.getCurrentPosition());
                    sendBroadcast(intent);
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    /**
     * 接收音乐控制的广播
     */
    class MusicControlReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(GloableContans.ACTION_PLAY_MUSIC)){
                playOrPause();
            }else if(action.equals(GloableContans.ACTION_PRE_MUSIC)){
                pre();
            }else if(action.equals(GloableContans.ACTION_NEXT_MUSIC)){
                next();
            }else if(action.equals(GloableContans.ACTION_PROGRESS_CHANGERED)){
                int progress=intent.getIntExtra("progress", 0);
                seekTo(progress);
            }
        }
    }

    private void next() {
        position= position==size-1 ? 0 : position+1;
    }

    private void playMusic(String url) {
        //获取当前需要播放的歌曲
        try {
            player.reset();
            player.setDataSource(GloableContans.BASEURL+url);
            player.prepare();
            player.start();
			/*
			player.prepareAsync();
			player.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					//准备完成后执行
					player.start();
				}
			});
			*/
            //更新activity的 音乐名称与图片
//            Intent intent=new Intent(GloableContans.ACTION_UPDATE_MUSIC_INFO);
//            intent.putExtra("music", m);
//            sendBroadcast(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "音乐加载出错", Toast.LENGTH_SHORT).show();
            next();
        }
    }

    private void pre() {
    }

    private void seekTo(int progress) {
        player.seekTo(progress);
    }

    private void playOrPause() {
        if(player.isPlaying()){
            player.pause();
        }else{
            player.start();
        }
    }
}
