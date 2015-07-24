package com.example.administrator.xutils_musiconline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.xutils_musiconline.contans.GloableContans;
import com.example.administrator.xutils_musiconline.entity.Music;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.core.BitmapSize;

import java.util.List;

/**
 * Created by Administrator on 2015/7/24.
 */
public class MyMusicAdapter extends RecyclerView.Adapter<MyMusicAdapter.MyViewHolder>{
    List<Music> musics;
    private Context context;
    GetPosition position;
    View v;
    public MyMusicAdapter( List<Music> musics,Context context) {
        this.musics=musics;
        this.context=context;
        Log.i("adapter_music",musics.size()+"");
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;
        v = LayoutInflater.from(context)
                .inflate(R.layout.music_item,null);
        holder= new MyViewHolder(v);
        return holder;
    }
    public void setOnclickListener(GetPosition position){
        this.position=position;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.i("tv_song_name",musics.get(position).getAlbumpic());
//

        BitmapUtils bitmapUtils=new BitmapUtils(context);
        BitmapDisplayConfig config=new BitmapDisplayConfig();
        config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.loading));
        config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.fail));
        config.setBitmapMaxSize(new BitmapSize(480, 720));
        bitmapUtils.display(holder.iv, GloableContans.BASEURL+musics.get(position).getAlbumpic(),config,new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    imageView.setImageBitmap(bitmap);
            }
            @Override
            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

            }
        });
//        Uri uri=Uri.parse(GloableContans.BASEURL+musics.get(position).getAlbumpic());
//        holder.iv.setImageURI(uri);

        holder.tv_song_name.setText(musics.get(position).getAlbum());
        holder.tv_singer_name.setText(musics.get(position).getName());
        holder.tv_song_time.setText(musics.get(position).getDurationtime());
        holder.tv_song_artist.setText(musics.get(position).getAuthor());
    }
    @Override
    public int getItemCount() {
        return musics.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_song_name;
        TextView tv_singer_name;
        TextView tv_song_time;
        TextView tv_song_artist;
        ImageView iv;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv=(ImageView)itemView.findViewById(R.id.iv_touxiang);
            tv_singer_name=(TextView)itemView.findViewById(R.id.item_singer_name);
            tv_song_name= (TextView) itemView.findViewById(R.id.item_song_name);
            tv_song_time=(TextView)itemView.findViewById(R.id.item_song_time);
            tv_song_artist=(TextView)itemView.findViewById(R.id.item_artist_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position.getposition(getLayoutPosition());
                }
            });
        }
    }
    public interface GetPosition{
        void getposition(int position);
    }
}
