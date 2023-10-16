package org.androidtown.test1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class NewsContent extends AppCompatActivity {

    String dateAndkey;
    String date;
    String title;
    String content;
    String firebasetext;
    String URL;
    Uri Uri;
    File localFile;
    ScrollView sv;

    FirebaseDatabase FDstorage;
    DatabaseReference DRtitle;
    FirebaseStorage storage;
    DatabaseReference DRtext;
    StorageReference mStorageRef,stringRef,picRef;
    StorageReference spacePictureRef,spaceTextRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);


        Intent intent = getIntent();
        dateAndkey = intent.getStringExtra("dateAndkey");//받아온 날짜정보와 키값
        title="";
        date="";
        content="";
        FDstorage = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl("gs://test1firebase-b19fd.appspot.com");
        //////////////////////////////////////////////
        DRtext = FDstorage.getReference(dateAndkey);

        picRef= mStorageRef.child("NewsList/"+dateAndkey+"/pic1.jpg");//사진 저장 위치
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //database 다 읽어오는 for문
                    String keyString = "";
                    String key="";
                    String dateKey="";
//News;date&title;content;
                    keyString=snapshot.getValue().toString();
                    Log.d("lastdebug",dateAndkey+" "+keyString);
                    boolean newscheck,titlecheck=true,datecheck=false,contentcheck=false;
                    firebasetext = "";
                    newscheck=isNews(snapshot.getValue().toString());
                    if(newscheck) {//news인지 확인
                        for(int i=8;i<keyString.length();i++)//date,title 확인
                        {
                            key="";
                            key+=keyString.charAt(i);
                            if(key.equals(";"))
                            {
                                break;
                            }
                            else {
                                dateKey += key;
                            }
                        }
                        if(dateKey.equals(dateAndkey))
                        {//맞는 데이터를 찾은 경우
                            content="";
                            for(int i=0;i<dateAndkey.length();i++)
                            {//date와 key 분리
                                key="";
                                key+=dateAndkey.charAt(i);
                                if(i<16)
                                    date+=key;
                                else
                                    title+=key;
                            }
                            for(int i=9+dateAndkey.length();i<keyString.length();i++)
                            {
                                if(keyString.charAt(i) == '|'){
                                    content+="\n\n";
                                } else if(keyString.charAt(i) == '^'){
                                    content+="\n";
                                }
                                else {
                                    content += keyString.charAt(i);
                                }
                            }
                            try {
                                loadData();
                                sv = (ScrollView)findViewById(R.id.news_content_scroll);
                                sv.invalidate();
                                sv.requestLayout();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        else
                        {
                            continue;
                        }
                    }
                    else{
                        continue;
                    }

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sv = (ScrollView)findViewById(R.id.news_content_scroll);
        sv.invalidate();
        sv.requestLayout();
    }
    public void loadData() throws IOException {
        localFile = File.createTempFile(dateAndkey,"jpg");

        picRef.getFile(localFile).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("herewecome", "78");
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("herewecome","88");
                Bitmap bm = BitmapFactory.decodeFile(localFile.getPath());
                ImageView iv = (ImageView)findViewById(R.id.news_content_image);
                iv.setImageBitmap(bm);
            }
        });
        Log.d("lastdebug",title+date+content);
        TextView et = (TextView)findViewById(R.id.news_content_title);
        et.setText(title);//title 설정
        et = (TextView)findViewById(R.id.news_content_date);
        et.setText(date);//date 설정
        et = (TextView)findViewById(R.id.news_content_content);
        et.setText(content);
        Log.d("debugsaveData",firebasetext+"!");
        //  Glide.with(this).load(spacePictureRef).into(scheduleImage);//사진 로드
    }
    public boolean isNews(String key)
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
}
