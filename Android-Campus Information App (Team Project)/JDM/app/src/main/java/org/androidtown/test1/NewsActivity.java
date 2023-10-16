package org.androidtown.test1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    int activity,clicked;
    final static int CAL=0,NEW=1,LEC=2;
    ArrayList<NewsInfo> ssunewsArr;
    ArrayList<NewsInfo> ssizennetArr;
    ArrayList<NewsInfo> comtimesArr;

    NewsAdapter ssunewsAdapter;
    NewsAdapter ssizennetAdapter;
    NewsAdapter comtimesAdapter;
    ListView listView;

    int press=1;

    private StorageReference stringRef;
    private StorageReference picRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        clicked=0;
        activity=1;
        ImageView iv = (ImageView)findViewById(R.id.menu3);
        iv.bringToFront();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        listView=(ListView)findViewById(R.id.newslistView);

        ssunewsArr=new ArrayList<>();
        ssizennetArr=new ArrayList<>();
        comtimesArr=new ArrayList<>();

        ssunewsAdapter=new NewsAdapter(this, R.layout.news_info, ssunewsArr);
        ssizennetAdapter=new NewsAdapter(this, R.layout.news_info, ssizennetArr);
        comtimesAdapter=new NewsAdapter(this, R.layout.news_info, comtimesArr);
        listView.setAdapter(ssunewsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsInfo ni = (NewsInfo)(adapterView.getAdapter().getItem(i));
                Intent intent=new Intent(getApplicationContext(), NewsContent.class);
                String key = "";
                key += ni.getDate();
                key +=ni.getHeadline();
                //key = "2018-12-15 00:59headlinetest";
                intent.putExtra("dateAndkey",key);
                startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ssunewsArr.clear();
                ssizennetArr.clear();
                comtimesArr.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //database 다 읽어오는 for문
                    String keyString = "";
                    String key="";
                    String title="";
                    String date="";
//News;date&title;content;
                    keyString=snapshot.getValue().toString();
                    boolean newscheck;
                    newscheck=isNews(snapshot.getValue().toString());
                    if(newscheck) {//news인지 확인
                        String kindNews="";
                        for(int i=0;i<7;i++)
                        {
                            key = "";
                            key += keyString.charAt(i);
                            kindNews+=key;
                        }
                        for(int i=8;i<keyString.length();i++)//date,title 확인
                        {
                            key="";
                            key+=keyString.charAt(i);
                            if(key.equals(";"))
                            {
                                break;
                            }
                            else if(i<=23){
                                date += key;
                            }
                            else
                                title += key;
                        }
                        //array에 넣어주는 부분
                        if(kindNews.equals("SsuNews"))
                        {
                            ssunewsArr.add(new NewsInfo(title, date));
                        }
                        else if(kindNews.equals("ComNews"))
                        {
                            comtimesArr.add(new NewsInfo(title, date));
                        }
                        else if(kindNews.equals("SsiNews"))
                        {
                            ssizennetArr.add(new NewsInfo(title, date));
                        }
                    }
                    else{
                        continue;
                    }

                }
                setnewslist();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setnewslist()
    {
        ssunewsAdapter=new NewsAdapter(this, R.layout.news_info, ssunewsArr);
        ssizennetAdapter=new NewsAdapter(this, R.layout.news_info, ssizennetArr);
        comtimesAdapter=new NewsAdapter(this, R.layout.news_info, comtimesArr);
        listView.setAdapter(ssunewsAdapter);
    }
    boolean isNews(String key)
    {
        String news="";
        for(int i=0;i<7;i++)
        {
            news+=key.charAt(i);
        }
        if(news.equals("SsuNews") || news.equals("ComNews") || news.equals("SsiNews"))
            return true;
        return false;
    }
    public void onSsunewsButtonClicked(View v){
        if(press != 1) {
            listView.setAdapter(ssunewsAdapter);
            ssunewsAdapter.notifyDataSetChanged();

            press=1;
        }
    }

    public void onSsizennetButtonClicked(View v){
        if(press != 2) {
            listView.setAdapter(ssizennetAdapter);
            ssizennetAdapter.notifyDataSetChanged();

            press=2;
        }
    }

    public void onComtimesButtonClicked(View v){
        if(press != 3) {
            listView.setAdapter(comtimesAdapter);
            comtimesAdapter.notifyDataSetChanged();

            press=3;
        }
    }
    public void exchangeActivity3(View v)
    {
        //화면에 이동할 버튼 띄워주기
        Toast.makeText(getApplicationContext(),"button",
                Toast.LENGTH_SHORT).show();
        if(clicked==0)
        {
            ImageView iv = (ImageView)findViewById(R.id.menuCalendar3);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            iv = (ImageView)findViewById(R.id.menuNewsstand3);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            iv = (ImageView)findViewById(R.id.menuLectureinfo3);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            clicked=1;
            iv = (ImageView)findViewById(R.id.menu3);
            iv.setImageResource(R.mipmap.white_plus_icon);
        }
        else
        {
            ImageView iv = (ImageView)findViewById(R.id.menuCalendar3);
            iv.setVisibility(View.INVISIBLE);
            iv = (ImageView)findViewById(R.id.menuNewsstand3);
            iv.setVisibility(View.INVISIBLE);
            iv = (ImageView)findViewById(R.id.menuLectureinfo3);
            iv.setVisibility(View.INVISIBLE);
            clicked=0;
            iv = (ImageView)findViewById(R.id.menu3);
            iv.setImageResource(R.mipmap.black_plus_icon);
        }
    }
    public void exchangeActivityCalendar3(View v)
    {
        //화면에 이동할 버튼 띄워주기
        Toast.makeText(getApplicationContext(),"Calendar",
                Toast.LENGTH_SHORT).show();
        if(activity==CAL)
            Toast.makeText(getApplicationContext(),"현재 실행중인 화면입니다.",
                    Toast.LENGTH_SHORT).show();
        else
        {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(myIntent);
        }

    }
    public void exchangeActivityNewsstand3(View v)
    {
        //화면에 이동할 버튼 띄워주기
        Toast.makeText(getApplicationContext(),"NewsStand",
                Toast.LENGTH_SHORT).show();
        if(activity==NEW)
            Toast.makeText(getApplicationContext(),"현재 실행중인 화면입니다.",
                    Toast.LENGTH_SHORT).show();
        else
        {
            Intent myIntent = new Intent(getApplicationContext(), NewsActivity.class);
            startActivity(myIntent);
        }
    }
    public void exchangeActivityLectureinfo3(View v)
    {
        //화면에 이동할 버튼 띄워주기
        Toast.makeText(getApplicationContext(),"LectureInfo",
                Toast.LENGTH_SHORT).show();
        if(activity==LEC)
            Toast.makeText(getApplicationContext(),"현재 실행중인 화면입니다.",
                    Toast.LENGTH_SHORT).show();
        else
        {
            Intent myIntent = new Intent(getApplicationContext(), CourseMainActivity.class);
            startActivity(myIntent);
        }
    }
}