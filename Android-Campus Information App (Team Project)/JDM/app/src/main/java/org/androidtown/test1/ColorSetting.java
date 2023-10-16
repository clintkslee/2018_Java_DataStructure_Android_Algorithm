package org.androidtown.test1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ColorSetting extends AppCompatActivity {
    int computer,it_universe,universe,computer_office,etc;
    String curChoice;
    LinearLayout color_choice_view;
    ListView color_choice_listview;
    ListviewAdapter color_choice_adapter;
    ImageView curImage;
    DatabaseReference DRcomputer,DRit_universe,DRuniverse,DRcomputer_office,DRetc,DB;
    ArrayList<String> DRArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_setting);

        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        DRcomputer = firebase.getReference("computer");
        DRit_universe = firebase.getReference("it_universe");
        DRuniverse = firebase.getReference("universe");
        DRcomputer_office = firebase.getReference("computer_office");
        DRetc = firebase.getReference("etc");

        curChoice="";
        color_choice_view = (LinearLayout)findViewById(R.id.color_setting);
        color_choice_listview = (ListView) findViewById(R.id.color_setting_listview);
        ColorItemSetting();
        ColorItemSetting();
        DRArray = new ArrayList<>();
        color_choice_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listviewitem lvi = (Listviewitem)color_choice_adapter.getItem(position);
                curImage.setImageResource(lvi.getIcon());
                color_choice_view.setVisibility(View.INVISIBLE);
                if(curChoice.equals("컴퓨터학부 학생회")) {
                    computer = lvi.getIcon();
                }
                else if(curChoice.equals("it 학생회"))
                    it_universe = lvi.getIcon();
                else if(curChoice.equals("총학생회"))
                    universe = lvi.getIcon();
                else if(curChoice.equals("컴퓨터학부 학사"))
                    computer_office = lvi.getIcon();
                else if(curChoice.equals("그 외의 일정"))
                    etc = lvi.getIcon();
            }
        });
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                char key=0;

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    key = (snapshot.getValue().toString()).charAt(0);
                    String keyString = ""+key;
                    String image = "";
                    if(!isColor(snapshot.getValue().toString()))
                        continue;
                    for(int i=1;i<(snapshot.getValue().toString()).length();i++)
                        image += (snapshot.getValue().toString()).charAt(i);
                    Log.d(keyString,"dddeee2");
                    if(keyString.equals("1")) {
                        Log.d("com","dddeee2");
                        computer = Integer.parseInt(image);
                    }
                    else if(keyString.equals("2")) {
                        it_universe = Integer.parseInt(image);
                    }
                    else if(keyString.equals("3"))
                        universe = Integer.parseInt(image);
                    else if(keyString.equals("4"))
                        computer_office= Integer.parseInt(image);
                    else if(keyString.equals("5"))
                        etc = Integer.parseInt(image);
                    Log.d(String.valueOf(snapshot.getValue()),"dddeee");

                }
                loadColors();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    public boolean isColor(String color)
    {

        for(int i=0;i<color.length();i++)
            if(color.charAt(i)==';')
                return false;
        return true;
    }
    public void loadColors()
    {
        Log.d("load",String.valueOf(computer));
        Log.d("load",String.valueOf(it_universe));
        Log.d("load",String.valueOf(universe));
        Log.d("load",String.valueOf(computer_office));
        Log.d("load",String.valueOf(etc));
        ImageView curlin = (ImageView)((LinearLayout)findViewById(R.id.computer)).getChildAt(0);
        curlin.setImageResource(computer);
        curlin = (ImageView)((LinearLayout)findViewById(R.id.it_universe)).getChildAt(0);
        curlin.setImageResource(it_universe);
        curlin = (ImageView)((LinearLayout)findViewById(R.id.universe)).getChildAt(0);
        curlin.setImageResource(universe);
        curlin = (ImageView)((LinearLayout)findViewById(R.id.computer_office)).getChildAt(0);
        curlin.setImageResource(computer_office);
        curlin = (ImageView)((LinearLayout)findViewById(R.id.etc)).getChildAt(0);
        curlin.setImageResource(etc);
    }
    public void saveColors()
    {
        Log.d("savedColors computer",String.valueOf(computer));
        Log.d("savedColors it_universe",String.valueOf(it_universe));
        Log.d("savedColors universe",String.valueOf(universe));
        Log.d("savedColors computer_office",String.valueOf(computer_office));
        Log.d("savedColors etc",String.valueOf(etc));
        DRcomputer.setValue("1"+String.valueOf(computer));
        DRit_universe.setValue("2"+String.valueOf(it_universe));
        DRuniverse.setValue("3"+String.valueOf(universe));
        DRcomputer_office.setValue("4"+String.valueOf(computer_office));
        DRetc.setValue("5"+String.valueOf(etc));
    }
    public void onClickSettingCancel(View v)
    {
        color_choice_view.setVisibility(View.INVISIBLE);
    }
    public void onClickBack(View v)
    {
          saveColors();
        color_choice_view.setVisibility(View.INVISIBLE);
        finish();
    }
    public void color_choice(View v)
    {

        color_choice_view.setVisibility(View.VISIBLE);
        color_choice_view.bringToFront();
        curImage = (ImageView)((LinearLayout)v).getChildAt(0);
        curChoice = ((TextView)(((LinearLayout)v).getChildAt(1))).getText().toString();

    }
    public void ColorItemSetting()
    {

        ArrayList<Listviewitem> color_list = new ArrayList<Listviewitem>();
        //ArrayList items에 데이터 추가
        color_list.add(new Listviewitem(R.mipmap.black_spot,"Black",false));
        color_list.add(new Listviewitem(R.mipmap.red_spot,"Red",false));
        color_list.add(new Listviewitem(R.mipmap.green_spot, "Green", false));
        color_list.add(new Listviewitem(R.mipmap.gray_spot,"Gray",false));
        color_list.add(new Listviewitem(R.mipmap.blue_spot,"Blue",false));
        color_list.add(new Listviewitem(R.mipmap.deepred_spot,"DeepRed",false));
        color_list.add(new Listviewitem(R.mipmap.purple_spot,"Purple",false));
        color_list.add(new Listviewitem(R.mipmap.skyblue_spot,"SkyBlue",false));
        color_list.add(new Listviewitem(R.mipmap.yellow_spot,"Yellow",false));
        color_choice_adapter = new ListviewAdapter(this,R.layout.itemview,color_list);

        color_choice_listview.setAdapter(color_choice_adapter);

    }
}
