package org.androidtown.test1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.util.Collections.rotate;

public class MakeSchedule extends AppCompatActivity {
    String dateAndkey;
    String date;
    String key;
    String imagePath;
    String changable;
    String color;
    String URL;
    int i_image;
    String i_text,firebasetext;
    FirebaseStorage storage;
    FirebaseDatabase FDstorage;
    DatabaseReference DRtext;
    StorageReference storageRef;
    StorageReference spacePictureRef,spaceTextRef;
    ImageView scheduleImage;
    Uri newUri,oldUri;
    File localFile;
//dateAndkey = 2018-12-15 00:59title
    final static int GALLERY_CODE =12345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_schedule);

        Intent intent = getIntent();
        dateAndkey = intent.getStringExtra("dateAndkey");//받아온 날짜정보
        changable = intent.getStringExtra("changable");//새로 만드는 일정인지 확인
        color = intent.getStringExtra("color");
        newUri = null;
        Log.d("debug64",changable);
        if(changable.equals("false"))
        {
            EditText et = (EditText)findViewById(R.id.make_schedule_text);
            et.setEnabled(false);
            et = (EditText)findViewById(R.id.make_schedule_title);
            et.setEnabled(false);
            et = (EditText)findViewById(R.id.make_schedule_URL);
            et.setEnabled(false);
            Button bt = (Button)findViewById(R.id.make_schedule_imageChange);
            bt.setVisibility(View.GONE);
            bt = (Button)findViewById(R.id.make_schedule_save);
            bt.setVisibility(View.GONE);
            bt = (Button)findViewById(R.id.make_schedule_cancel);
            bt.setText("Back");
        }
        else
        {
            EditText et = (EditText)findViewById(R.id.make_schedule_text);
            et.setEnabled(true);
            et = (EditText)findViewById(R.id.make_schedule_title);
            et.setEnabled(true);
            et = (EditText)findViewById(R.id.make_schedule_URL);
            et.setEnabled(true);
            Button bt = (Button)findViewById(R.id.make_schedule_imageChange);
            bt.setVisibility(View.VISIBLE);
            bt = (Button)findViewById(R.id.make_schedule_save);
            bt.setVisibility(View.VISIBLE);
            bt = (Button)findViewById(R.id.make_schedule_cancel);
            bt.setText("Cancel");
        }
        firebasetext ="";
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://test1firebase-b19fd.appspot.com");
        FDstorage = FirebaseDatabase.getInstance();
        DRtext = FDstorage.getReference(dateAndkey);
        Log.d("noimagewhat",dateAndkey);
        spacePictureRef = storageRef.child(dateAndkey+"/picture.jpg");
        //spaceTextRef = storageRef.child(dateAndkey+"/text.txt");
        TextView tv = (TextView)findViewById(R.id.make_schedule_date);
        scheduleImage = (ImageView)findViewById(R.id.make_schedule_image);
        tv.setText(dateAndkey);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keyString = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("snapshotdebug",snapshot.getValue().toString());
                    keyString="";
                    String key="";

                    boolean whatColor=false,keyplus=true,urlplus=false;
                    firebasetext = "";
                    URL = "";
                    for(int i=0;i<snapshot.getValue().toString().length();i++) {
                        key = "";
                        key += (snapshot.getValue().toString()).charAt(i);
                        if (keyplus && !key.equals(";"))
                            keyString += key;//";"나올때 까지 keyString 저장
                        else if (keyplus && key.equals(";")) {
                            keyplus = false;//";"가 나오면 키를 저장하지 않고 색깔저장으로 변경
                            whatColor = true;
                        } else if (whatColor && !key.equals(";")) {
                            //얻어오는 색깔정보 건너 띄기
                        } else if (whatColor && key.equals(";")) {//url정보로 변경
                            whatColor = false;
                            urlplus = true;
                        } else if(urlplus && !key.equals(";")) {
                            URL+=key;
                        }  else if(urlplus && key.equals(";")){
                            urlplus = false;
                        }
                        else
                            firebasetext+=key;

                    }
                    Log.d("debugsaveData",keyString+" "+firebasetext);
                    if(keyString.equals(dateAndkey))
                        break;
                    Log.d("debugsaveData",keyString);

                }
                try {
                    if(!isNews(keyString))
                        loadData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public boolean isNews(String key)
    {
        String news="";
        Log.d("isnews173",key);
        for(int i=0;i<7;i++)
        {
            news+=key.charAt(i);
        }
        if(news.equals("SsuNews") || news.equals("ComNews") || news.equals("SsiNews"))
            return true;
        return false;
    }
    public void loadData() throws IOException {
            localFile = File.createTempFile(dateAndkey,"jpg");

        spacePictureRef.getFile(localFile).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("hereicome", "78");
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("hereicome","88");
                Bitmap bm = BitmapFactory.decodeFile(localFile.getPath());
                scheduleImage.setImageBitmap(bm);
            }
        });

        Log.d("imageerror",localFile.toString());

        /*
        Glide.with(this).load("gs://test1firebase-b19fd.appspot.com/"+dateAndkey+"/pictuer").into(scheduleImage);
        Log.d("debugsaveData","start");*/
        //editText 내용 설정
        EditText et = (EditText)findViewById(R.id.make_schedule_text);
        et.setText(firebasetext);//내용설정
        String title = "";
        for(int i=8;i<dateAndkey.length();i++)
            title += dateAndkey.charAt(i);
        et = (EditText)findViewById(R.id.make_schedule_title);
        et.setText(title);//타이틀 설정
        et = (EditText)findViewById(R.id.make_schedule_URL);
        et.setText(URL);
        Log.d("debugsaveData",firebasetext+"!");
      //  Glide.with(this).load(spacePictureRef).into(scheduleImage);//사진 로드
    }
    public void saveData()
    {

        EditText et = (EditText)findViewById(R.id.make_schedule_text);
        String saveText = et.getText().toString();
        Log.d("saveDataForText",dateAndkey+";"+saveText+";"+URL);
        //바뀐 내용 저장하기 전에 내용 삭제
        String date="",key="";
        DRtext.setValue(null);// 기존의 내용 삭제
        for(int i=0;i<dateAndkey.length();i++) {
            if(i<8)
                date += dateAndkey.charAt(i);
            else
                key += dateAndkey.charAt(i);
        }
        EditText ettitle = (EditText)findViewById(R.id.make_schedule_title);
        if(key.equals(ettitle.getText().toString()))//같은 제목의 일정
        {
            //
        }
        else//제목이 바뀜
        {
            //기존의 제목 변경
            //firebase db의 내용 변경, storage의 폴더 이름 변경
           // DRtext.removeValue();//기존의 데이터베이스 삭제
            key = ettitle.getText().toString();
            DRtext = FDstorage.getReference(date+key);//새로운 데이터베이스 경로 생성
            spacePictureRef.delete();
            spacePictureRef = storageRef.child(date+key+"/picture.jpg");
            //storage 기존의 spacePictureRef주소의 내용 삭제 후 새로운 이름의 폴더 생성
        }
        ettitle = (EditText)findViewById(R.id.make_schedule_URL);
        URL = ettitle.getText().toString();
        DRtext.setValue(date +key+";"+color+";"+URL+";"+saveText);//새로운 데이터 저장
   //     spacePictureRef = storageRef.child(dateAndkey+"/pictuer.jpg");
     /*   Uri file = Uri.fromFile(new File(imagePath));
        UploadTask uploadTask = spaceRef.putFile(file);*/
        Log.d("whatthefuck","263");
        if(newUri != null) {//이미지가 바뀐 경우 바뀐 이미지 저장
            UploadTask uploadTask = spacePictureRef.putFile(newUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("debugsaveData", "save fail");
                }
            });
        }

        else//이미지가 바뀌지 않으면 기존의 이미지 저장
        {
            UploadTask uploadTask = spacePictureRef.putFile(Uri.parse(localFile.getPath()));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("debugsaveData", "save fail");
                }
            });
        }
    }
    public void Choice_image(View v)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);

    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {//intent로 사진을 넘겨 받음 getData()로 Uri를 넘겨받음
        if(resultCode == RESULT_OK)
        {
            if(requestCode == GALLERY_CODE)
                sendPicture(data.getData());
        }
    }
    private void sendPicture(Uri imgUri)//image를 Bitmap으로 imageview에 띄워주기
    {
        newUri = imgUri;//uri에 받아온 사진 uri저장
        imagePath = getRealPathFromURI(imgUri);
        Log.d("debug89",imagePath);


        try{
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            scheduleImage.setImageBitmap(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri contentUri)//사진이 저장된 절대 경로 반환
    {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }
    public void onClickmakescheduleSave(View v)
    {
        //바뀐 정보를 저장하고 종료
        saveData();
        finish();
    }
    public void onClickmakescheduleCancel(View v)
    {
        //기존의 정보를 저장해 놨다가 바꾼 정보를 저장하지 않고 다시 원래 정보를 저장하고 종료
        finish();
    }
    public void onClickURL(View v)
    {
        boolean cango=false;
        if(URL.length()>4)
            if(URL.charAt(0)=='h' && URL.charAt(1)=='t' && URL.charAt(2)=='t' && URL.charAt(3)=='p')
                cango=true;
        if(cango) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
            startActivity(myIntent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"URL이 양식에 맞지 않습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
