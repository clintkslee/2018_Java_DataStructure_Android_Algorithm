package org.androidtown.test1;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCourse extends AppCompatActivity {
    ImageView img;
    Button btt1;
    Button btt2;
    TextView pt;
    BitmapDrawable bitmap;
    int page;
    int id;
    String useName;
    String nameChange;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_course);

        img = (ImageView)findViewById(R.id.showImageView);
        btt1 = (Button)findViewById(R.id.showButton1);
        btt2 = (Button)findViewById(R.id.showButton2);
        pt = (TextView)findViewById(R.id.pageText);
        page = 1;

        Intent i = getIntent();
        res = getResources();

        String imaName = i.getStringExtra("imagename");
        nameChange=null;
        switch(imaName){
            case("알고리즘"):nameChange="algo";break;
            case("암호학"):nameChange="amho";break;
            case("회계원리"):nameChange="huoe";break;

            case("영어1"):nameChange="eng1";break;
            case("일본어문어문법"):nameChange="japs";break;
            case("채플"):nameChange="chaps";break;

            case("중국어기초"):nameChange="chin";break;
            case("양자역학2"):nameChange="yang";break;
            case("숭실과기독교"):nameChange="soki";break;

            case("윈도우프로그래밍및실습"):nameChange="winp";break;
            case("유체역학(가)"):nameChange="youc";break;
            case("광고기획론"):nameChange="brod";break;

            case("교육학개론"):nameChange="teac";break;
            case("섬김의리더십"):nameChange="lead";break;
            case("사용자인터페이스및실습"):nameChange="usei";break;

            case("(외국인을위한)디지털시대의글쓰기"):nameChange="writ";break;
            case("(외국인을위한)한국사상의이해"):nameChange="kort";break;
            case("헌법1"):nameChange="hlaw";break;

            case("금융시장론"):nameChange="mark";break;
            case("조직행동론"):nameChange="team";break;
            case("정책학원론"):nameChange="acad";break;

            case("무역관습론"):nameChange="cust";break;
            case("집중독일어2"):nameChange="germ";break;
            case("컴퓨팅적사고"):nameChange="coth";break;

            case("분쟁해결과법"):nameChange="blaw";break;
            case("환경에너지공학"):nameChange="hnat";break;
            case("숭실인의역량과진로탐색2"):nameChange="syok";break;

            case("데이터통신과네트워크"):nameChange="data";break;
            case("생활지도및상담"):nameChange="live";break;
        }
        useName = nameChange+page;

        id = res.getIdentifier(useName, "drawable", ShowCourse.this.getPackageName());
        img.setImageResource(id);

        btt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page==1);
                else{
                    page--;
                    useName = nameChange+page;
                    id = res.getIdentifier(useName, "drawable", ShowCourse.this.getPackageName());
                    pt.setText(page+" / 4");
                    img.setImageResource(id);
                }
            }
        });
        btt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page==4);
                else{
                    page++;
                    useName = nameChange+page;
                    id = res.getIdentifier(useName, "drawable", ShowCourse.this.getPackageName());
                    pt.setText(page+" / 4");
                    img.setImageResource(id);
                }
            }
        });
    }
}
