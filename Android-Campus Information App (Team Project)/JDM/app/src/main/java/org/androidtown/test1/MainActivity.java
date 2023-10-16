package org.androidtown.test1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.Image;
import android.provider.ContactsContract;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.icu.util.ChineseCalendar;
import java.util.Date;
import android.util.Log;

import android.graphics.Color;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Date date;
    float startX,endX;
    SimpleDateFormat sdf;
    String s_year,s_month,s_day;
    int i_year,i_month,i_day,computer_color,computer_office_color,etc_color,it_universe_color,universe_color;
    private static final String[] solarArr = new String[]{"0101", "0301", "0505", "0606", "0815", "1003","1009","1225"};
    private static final String[] lunarArr = new String[]{"0101", "0102", "0408", "0814", "0815", "0816"};
    private static final int isDRAG = 200;
    private static final int CAL=0,NEW=1,LEC=2;
    TextView focus;
    TextView DateChangeTextView;
    ImageView focusLabel;
    LinearLayout focusBack;
    GridLayout main_calendar;
    View curView;
    ArrayList<Listviewitem> items;
    ArrayList<Listviewitem> detail_items;
    ArrayList<MySchedule> ScheduleList;
    LinearLayout calendarBackGround,detailSchedule,detailSchedule2,detailSchedule3;
    ListView listview,detail_listview;
    ListviewAdapter adapter,adapter_detail;
    FirebaseDatabase FDstorage;
    DatabaseReference DRtitle;
    int activity,clicked;

    boolean isDetailCalendarOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = 0; clicked=0;
        ImageView menuButton = (ImageView)findViewById(R.id.menu);
        menuButton.bringToFront();
        main_calendar = (GridLayout) findViewById(R.id.main_calendar);
        listview = (ListView)findViewById(R.id.listview);
        detail_listview = (ListView)findViewById(R.id.detail_listview);
        calendarBackGround = (LinearLayout)findViewById(R.id.calendarBackGround);
        DateChangeTextView = (TextView)findViewById(R.id.Date_button);
        detailSchedule = (LinearLayout)findViewById(R.id.detail_schedule);
        detailSchedule2 = (LinearLayout)findViewById(R.id.detail_schedule2);
        detailSchedule3 = (LinearLayout)findViewById(R.id.detail_schedule3);

        ScheduleList = new ArrayList<MySchedule>();
        FDstorage = FirebaseDatabase.getInstance();
        date = new Date();
        sdf = new SimpleDateFormat("yyyy-MM-dd hh");
        s_year = sdf.format(date);
        s_day = s_year.substring(8,10);
        s_month = s_year.substring(5,7);
        s_year = s_year.substring(0,4);
        i_year = Integer.parseInt(s_year);
        i_month = Integer.parseInt(s_month);
        i_day = Integer.parseInt(s_day);
        items = new ArrayList<Listviewitem>();
        detail_items = new ArrayList<Listviewitem>();
        adapter = new ListviewAdapter(this,R.layout.itemview,items);
        adapter_detail = new ListviewAdapter(this,R.layout.itemview,detail_items);
        listview.setAdapter(adapter);
        itemSetting(i_year,i_month,i_day);
        detail_listview.setAdapter(adapter_detail);
        setDate(s_year,s_month,s_day);
        setDateClickEvent();

        detailItemSetting(i_year,i_month,i_day);
        Log.d("error113","3");
        FDstorage.getReference().getRoot().addValueEventListener(new ValueEventListener() {
            @Override//firebase변경에 반응함
            public void onDataChange(DataSnapshot dataSnapshot) {
            ScheduleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Log.d("snapshotdebug",snapshot.getValue().toString());
                String snapshotValue=snapshot.getValue().toString();//읽어온 값
                //첫번째 키값을 제외한 나머지 코드가 색깔코드라면 key에 해당하는 매체에 색배정

                boolean isColor = isColor(snapshotValue);
                String key="";
                String firebaseDate="";
                String firebaseColor="";
                String firebaseTitle="";
                boolean isRight=false,keyplus=true;
                if(isColor)
                {
                    Log.d("beforecolor",snapshotValue);
                    key +=snapshotValue.charAt(0);
                    String colorcode = "";
                    for(int i=1;i<snapshotValue.length();i++)
                        colorcode+=snapshotValue.charAt(i);
                    if(key.equals("1"))
                        computer_color=Integer.parseInt(colorcode);
                    else if(key.equals("2"))
                        it_universe_color = Integer.parseInt(colorcode);
                    else if(key.equals("3"))
                        universe_color = Integer.parseInt(colorcode);
                    else if(key.equals("4"))
                        computer_office_color = Integer.parseInt(colorcode);
                    else if(key.equals("5"))
                        etc_color = Integer.parseInt(colorcode);
                    Log.d("aftercolor",key+" "+colorcode);
                }
                else
                {


                    boolean colorOn=false,dateAndkeyOn=false;
                    String firebaseDateAndKey="";
                    for(int i=0;i<snapshotValue.length();i++) {
                        key +=snapshotValue.charAt(i);
                        if(i<8)
                            firebaseDate+=key;//날짜 읽기
                        else if(i==8) {
                            dateAndkeyOn = true;
                            firebaseTitle+=key;
                        }
                        else if(dateAndkeyOn && !key.equals(";"))
                            firebaseTitle +=key;//타이틀 읽기
                        else if(dateAndkeyOn && key.equals(";")) {
                            dateAndkeyOn = false;
                              colorOn=true;
                        }
                        else if(colorOn && !key.equals(";"))
                        {
                            firebaseColor+=key;//색읽기
                        }
                        else if(colorOn && key.equals(";"))
                        {
                            break;
                        }

                        key = "";
                    }
                    //얻어진 날짜와 해당 년,월을 비교해서 저장할 데이터 배열을 만들고 달력에 반영해주기
                    ScheduleList.add(new MySchedule(firebaseDate,firebaseTitle,firebaseColor));
                    Log.d("after",firebaseDate+"?"+firebaseColor+"!"+firebaseTitle);
                }


            }
                Page_Initialization();
                setMonthPage(s_year,s_month);

        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override//firebase text가져오기
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ScheduleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("snapshotdebug",snapshot.getValue().toString());
                    String snapshotValue=snapshot.getValue().toString();//읽어온 값
                    //첫번째 키값을 제외한 나머지 코드가 색깔코드라면 key에 해당하는 매체에 색배정

                    boolean isColor = isColor(snapshotValue);
                    String key="";
                    String firebaseDate="";
                    String firebaseColor="";
                    String firebaseTitle="";
                    boolean isRight=false,keyplus=true;

                    if(isColor)
                    {
                        Log.d("beforecolor",snapshotValue);
                        key +=snapshot.getValue().toString().charAt(0);
                        String colorcode = "";
                        for(int i=1;i<snapshotValue.length();i++)
                            colorcode+=snapshotValue.charAt(i);
                        if(key.equals("1"))
                            computer_color=Integer.parseInt(colorcode);
                        else if(key.equals("2"))
                            it_universe_color = Integer.parseInt(colorcode);
                        else if(key.equals("3"))
                            universe_color = Integer.parseInt(colorcode);
                        else if(key.equals("4"))
                            computer_office_color = Integer.parseInt(colorcode);
                        else if(key.equals("5"))
                            etc_color = Integer.parseInt(colorcode);

                    }
                    else
                    {
                        Log.d("mainfirebase","130");

                        boolean colorOn=false,dateAndkeyOn=false;
                        String firebaseDateAndKey="";
                        for(int i=0;i<snapshotValue.length();i++) {
                            key +=snapshotValue.charAt(i);
                            if(i<8)
                                firebaseDate+=key;//날짜 읽기
                            else if(i==8) {
                                dateAndkeyOn = true;
                                firebaseTitle+=key;
                            }
                            else if(dateAndkeyOn && !key.equals(";"))
                                    firebaseTitle +=key;//타이틀 읽기
                                else if(dateAndkeyOn && key.equals(";")) {
                                    dateAndkeyOn = false;
                                    colorOn=true;
                                }
                                else if(colorOn && !key.equals(";"))
                                {
                                    firebaseColor+=key;//색읽기
                                }
                                else if(colorOn && key.equals(";"))
                                {
                                    break;
                                }

                                key = "";
                        }
                        //얻어진 날짜와 해당 년,월을 비교해서 저장할 데이터 배열을 만들고 달력에 반영해주기
                        if(!isNews(snapshotValue))
                            ScheduleList.add(new MySchedule(firebaseDate,firebaseTitle,firebaseColor));
                        Log.d("after",firebaseDate+"?"+firebaseColor+"!"+firebaseTitle);
                    }


                }
                Page_Initialization();
                setMonthPage(s_year,s_month);
                itemSetting(i_year,i_month,i_day);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DateChangeTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MyDatePickerDialog dialog = new MyDatePickerDialog();
                dialog.setListener(DatePickerListener);
                dialog.show(getSupportFragmentManager(),"YearmonthPicker");
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){//detail schedule창 올리기(하단의 아이템창 클릭시)

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                detailItemSetting(i_year,i_month,i_day);
                detailSchedule.bringToFront();
                TextView text = (TextView)findViewById(R.id.detail_schedule_text);
                text.setText(s_year+"/"+s_month+"/"+s_day);
                detailSchedule.setVisibility(View.VISIBLE);
                calendarBackGround.setBackgroundColor(Color.GRAY);
        //        main_calendar.bringToFront();
                listview.setBackgroundColor(Color.GRAY);
                isDetailCalendarOn=true;
            }
        });
        detail_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//detail listview 클릭 이벤트
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//detail schedule창에서 아이템 클릭시 상세정보창 띄우기
                Listviewitem lvi = (Listviewitem)(parent.getAdapter().getItem(position));
                String key = lvi.getName();
                if(lvi.getName().equals("+ 일정 추가하기"))
                {
                    ClickMakeSchedule("true","");
                }
                else if(lvi.getChangable())
                {
                    Log.d("listclick","0");
                     //새로운 액티비티 띄우기
                    ClickMakeSchedule("true",key);//새로운 정보 만드는창 띄우기
                }
                else
                {
                    Log.d("listclick","1");
                    ClickMakeSchedule("false",key);
                }
            }
        });
    }
    private DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener()  //datepickerdialog를 통해 날짜를 설정하면 일어나는 일
    {
        @Override
        public void onDateSet(DatePicker view, int set_year, int set_month, int dayOfMonth) {
            Toast.makeText(getApplicationContext(),set_year+"년"+set_month+"월",
                    Toast.LENGTH_SHORT).show();
            i_month = set_month;
            s_month = String.valueOf(i_month);
            if(i_month<10)
                s_month = "0"+s_month;
            i_year = set_year;
            s_year = String.valueOf(i_year);
            Page_Initialization();
            setDate(s_year,s_month,s_day);
        }
    };
    public void setDate(String year, String month, String day)
    {
        Page_Initialization();
        TextView curTextView = (TextView)findViewById(R.id.Date_button);
        curTextView.setText(year+"년"+month+"월");
        setMonthPage(year,month);
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
    public void Page_Initialization()
    {
        for(int i=0;i<6;i++)
            for(int j=0;j<7;j++)
            {
                TextView curTextView = (TextView)findViewById(find_day(i,j));
                LinearLayout curLinear = (LinearLayout)findViewById(find_dayBack(i,j));
                curLinear.setBackgroundColor(Color.WHITE);
                curTextView.setText("");//text 초기화
                // curTextView.setBackgroundColor(Color.WHITE);
                if(j==6)
                    curTextView.setTextColor(Color.BLUE);
                else if(j==0)
                    curTextView.setTextColor(Color.RED);
                else
                    curTextView.setTextColor(Color.BLACK);//text 색초기화
                //imageView 초기화 추가 예정
                for(int k=1;k<=5;k++)
                {
                    TextView tv = (TextView)findViewById(find_dayLabel(i,j,k));
                    tv.setTextColor(Color.WHITE);
                }
            }
    }
    public void setMonthPage(String s_year, String s_month)//월 페이지 설정 함수
    {
        //달력 알고리즘
        int i;//2017 7월 1일 기준(토)
        int year=Integer.parseInt(s_year),month=Integer.parseInt(s_month);
        boolean leap_year = checkYear(year);
        int day;
        int x=6, y=0, gap = 0;
        ///////////////////////////////////////////////////////////////////////////////년에대한계산
        if (year < 2017)
        {
            for (i = year; i < 2017; ++i)
            {
                if (checkYear(i+1))
                    gap += 2;
                else
                    gap += 1;
            }
            x = 6 - (gap%7);
        }
        else
        {
            for (i = 2017; i < year; ++i)
            {
                if (checkYear(i))
                    gap += 2;
                else
                    gap += 1;
            }
            x =(6 + (gap%7))%7;
        }
        /////////////////////////////////////////////////////////////////////////////월에 대한 계산
        gap = 0;
        if (month > 7)
        {
            for (i = 7; i < month; ++i)
            {
                if (i == 7)
                    gap += 3;
                else if (i % 2 == 1)
                {
                    gap += 2;
                }
                else
                {
                    gap += 3;
                }

            }
            x = (x + (gap % 7)) % 7;
        }
        else
        {
            for (i = month; i < 7; ++i)
            {
                if (i % 2 == 1)
                {
                    gap += 3;
                }
                else if (i == 2)
                {
                    if (checkYear(year))
                        gap += 1;
                    else;
                }
                else
                {
                    gap += 2;
                }
            }
            x -= (gap % 7);
            if (x < 0)
            {
                x = 7 + x;
            }
        }
        /////////////////////////////////////////////////////////////////////////31,30,29,28 일자 확인
        if (month < 8)
        {
            if (month % 2 == 1)
                day = 31;
            else if (month == 2)
            {
                if (checkYear(year))
                    day = 29;
                else
                    day = 28;
            }
            else
                day = 30;
        }
        else
        {
            if (month % 2 == 1)
                day = 30;
            else
                day = 31;
        }
        //////////////////////////////////////////////////////////////////////출력하는 부분
        for (i = 1; i <= day; ++i)
        {
            if (x>6)
            {
                x = 0;
                ++y;
            }
            //날짜 설정해주면서 점도 찍어주기
            TextView viewday = (TextView)findViewById(find_day(y,x));
            if(i==i_day) {
                focus = viewday;
                focusBack = (LinearLayout)findViewById(find_dayBack(y,x));
                //focusLabel = (ImageView)findViewById(find_dayLabel(y,x));
                focusBack.setBackgroundColor(Color.GRAY);
            }
            if(i<10) {
                if (isRedDay(s_year, s_month, "0"+String.valueOf(i))) {
                    viewday.setTextColor(Color.RED);
                }
                makeDot(s_year,s_month,"0"+String.valueOf(i),x,y);
            }
            else {
                if (isRedDay(s_year, s_month, String.valueOf(i))) {
                    viewday.setTextColor(Color.RED);

                }
                makeDot(s_year,s_month,String.valueOf(i),x,y);
            }
            viewday.setText(String.valueOf(i));
            ++x;
        }
        Log.d("error501","501");
    }
    public boolean checkYear(int year)//윤년 확인 함수
    {
        if (year % 4 != 0)
            return false;
        else if (year % 100 != 0)
            return true;
        else if (year % 400 != 0)
            return false;
        else
            return true;
    }
    public boolean isRedDay(String year, String month, String day)
    {
        boolean solar=false,luna=false;
        for(int i=0;i<solarArr.length;i++)
        {
            if(solarArr[i].equals(month+day))
                solar = true;
        }
        luna = isHolidayLunar(year+month+day);
        return (luna || solar);
    }
    private static boolean isHolidayLunar(String date) {
        try {
            Calendar cal = Calendar.getInstance();
            ChineseCalendar chinaCal = new ChineseCalendar();

            cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
            cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));


            chinaCal.setTimeInMillis(cal.getTimeInMillis());

            // 음력으로 변환된 월과 일자
            int mm = chinaCal.get(ChineseCalendar.MONTH)+1;
            int dd = chinaCal.get(ChineseCalendar.DAY_OF_MONTH);

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%02d", mm));
            sb.append(String.format("%02d", dd));
            // 음력 12월의 마지막날 (설날 첫번째 휴일)인지 확인
            if (mm == 12) {
                int lastDate = chinaCal.getActualMaximum(ChineseCalendar.DAY_OF_MONTH);
                if (dd == lastDate) {
                    return true;
                }
            }

            // 음력 휴일에 포함되는지 여부 리턴
            return Arrays.asList(lunarArr).contains(sb.toString());
        } catch(Exception ex) {
            System.out.println(ex.getStackTrace());
            return false;
        }
    }
    public void makeDot(String s_year,String s_month,String s_day,int x,int y)
    {

        String Date = s_year+s_month+s_day;
        Log.d("makeDot","Date = "+Date);
        TextView iv1 = (TextView)findViewById(find_dayLabel(y,x,1));
        TextView iv2 = (TextView)findViewById(find_dayLabel(y,x,2));
        TextView iv3 = (TextView)findViewById(find_dayLabel(y,x,3));
        TextView iv4 = (TextView)findViewById(find_dayLabel(y,x,4));
        TextView iv5 = (TextView)findViewById(find_dayLabel(y,x,5));
        iv1.setTextColor(colorList(computer_color));
        iv2.setTextColor(colorList(it_universe_color));
        iv3.setTextColor(colorList(universe_color));
        iv4.setTextColor(colorList(computer_office_color));
        iv5.setTextColor(colorList(etc_color));
        Log.d("makeDot",universe_color+"");
        int a=0,b=0,c=0,d=0,e=0;
        for(int i=0;i<ScheduleList.size();i++)
        {
            Log.d("makeDot","2");
            if(ScheduleList.get(i).getDate().equals(Date))//날짜와 일치하는 데이터를 찾은 경우
            {
                String scheduleColor = ScheduleList.get(i).getColor();
                if (scheduleColor.equals("computer")) {
                    a++;
                    Log.d("makeDot", "computer"+String.valueOf(computer_color));

                } else if (scheduleColor.equals("it_universe"))
                {
                    b++;

                }
                else if (scheduleColor.equals("universe"))
                {
                    c++;

                }
                else if (scheduleColor.equals("computer_office"))
                {
                    d++;

                }
                else if (scheduleColor.equals("etc"))
                {
                    Log.d("makeDot","etc");
                    e++;

                }
            }

        }
        Log.d("makeDot","end");
        iv1.setText(String.valueOf(a));
        iv2.setText(String.valueOf(b));
        iv3.setText(String.valueOf(c));
        iv4.setText(String.valueOf(d));
        iv5.setText(String.valueOf(e));
    }
    public int colorList(int color)
    {
        if(color == R.mipmap.black_spot)
            return Color.BLACK;
        else if(color == R.mipmap.red_spot)
            return Color.RED;
        else if(color == R.mipmap.green_spot)
            return Color.GREEN;
        else if(color == R.mipmap.gray_spot)
            return Color.GRAY;
        else if(color == R.mipmap.blue_spot)
            return Color.BLUE;
        else if(color == R.mipmap.deepred_spot)
            return Color.rgb(165,42,42);
        else if(color == R.mipmap.purple_spot)
            return Color.rgb(99,32,204);
        else if(color == R.mipmap.skyblue_spot)
            return Color.rgb(0,191,255);
        else if(color == R.mipmap.yellow_spot)
            return Color.YELLOW;
        else
            return Color.WHITE;
    }
    public void detailItemSetting(int i_year,int i_month, int i_day)//detail schedule 창 listview 아이템 넣기
    {
        Log.d("error643","ee");
        detail_items.clear();
        //ArrayList items에 for (int i = 0; i < focus.getText().length(); i++)
        //                            str += focus.getText().charAt(i);
        //                        if(str.equals(""))
        //                            return true;
        //                        i_day = Integer.parseInt(str);
        //                        if(i_day<10)
        //                            s_day = "0"+str;
        //                        else
        //                            s_day = str;데이터 추가
        String Date = String.valueOf(i_year);
        if(i_month<10)
            Date+="0";
        Date += String.valueOf(i_month);
        if(i_day<10)
            Date+="0";
        Date += String.valueOf(i_day);
        Log.d("error661","!"+ScheduleList.size());
        for(int i=0;i<ScheduleList.size();i++)
        {
            if(ScheduleList.get(i).getDate().equals(Date))//날짜와 일치하는 데이터를 찾은 경우
            {
                MySchedule schedule = ScheduleList.get(i);
                Log.d("scheduleabc",schedule.getColor()+" "+etc_color);
                if(schedule.getColor().equals("etc"))
                    detail_items.add(new Listviewitem(whatColor(schedule.getColor()),schedule.getTitle(),true));
                else
                     detail_items.add(new Listviewitem(whatColor(schedule.getColor()),schedule.getTitle(),false));
            }
        }
        detail_items.add(new Listviewitem(R.mipmap.gray_spot,"+ 일정 추가하기",true));

        adapter_detail.upDateItemList(detail_items);
        adapter_detail.notifyDataSetChanged();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        adapter.upDateItemList(items);
    }

    public void itemSetting(int i_year,int i_month,int i_day)//schedule창 listview 아이템 넣기
    {   //listview에 해당 정보 기반 데이터 추가하기
        //items = new ArrayList<Listviewitem>();
        items.clear();
        //ArrayList items에 데이터 추가
        String Date = String.valueOf(i_year);
        if(i_month<10)
            Date+="0";
        Date += String.valueOf(i_month);
        if(i_day<10)
            Date+="0";
        Date += String.valueOf(i_day);
        for(int i=0;i<ScheduleList.size();i++)
        {
            if(ScheduleList.get(i).getDate().equals(Date))//날짜와 일치하는 데이터를 찾은 경우
            {
                MySchedule schedule = ScheduleList.get(i);
                items.add(new Listviewitem(whatColor(schedule.getColor()),schedule.getTitle(),false));
                Log.d("error706",String.valueOf(whatColor(schedule.getColor())));
            }
        }
        /*
        items.add(new Listviewitem(R.mipmap.black_spot,"Black",false));
        items.add(new Listviewitem(R.mipmap.red_spot,"Red",false));
        items.add(new Listviewitem(R.mipmap.green_spot,"Green",false));
        items.add(new Listviewitem(R.mipmap.gray_spot,"Gray",false));
        items.add(new Listviewitem(R.mipmap.blue_spot,"Blue",false));
        */
        //adapter = new ListviewAdapter(this,R.layout.itemview,items);'
        Log.d("error711","11");
        adapter.upDateItemList(items);
        /*
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.upDateItemList(items);
                adapter.notifyDataSetChanged();
            }
        });*/
        listview.setAdapter(adapter);
        ImageView menuButton = (ImageView)findViewById(R.id.menu);
        menuButton.bringToFront();
       // adapter.notifyDataSetChanged();
    }
    public void setDateClickEvent()
    {
        LinearLayout curLinear;
        for(int i=0;i<6;i++)
            for(int j=0;j<7;j++)
            {
                curLinear = (LinearLayout)findViewById(find_dayBack(i,j));
                curLinear.setOnTouchListener(new MainActivity.MyDateTouchListener());
            }
    }

    private class MyDateTouchListener implements View.OnTouchListener{//isDrag를 넘지않으면서 드래그하는경우를 인식하지 못함

        int[] location;
        @Override
        public boolean onTouch(View v,MotionEvent event){
            // v.getLocationOnScreen(location);
            Log.d("Action","0");
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                Log.d("Action_down","1");
                startX = event.getX();
                curView = v;
                return true;
            }
            else if(event.getAction()==MotionEvent.ACTION_MOVE) {
                Log.d("Action_move","2");
                return false;
            }
            else if(event.getAction()==MotionEvent.ACTION_UP) {
                endX = event.getX();
                Log.d("Action_up",String.valueOf(startX-endX));
                Log.d("erroR","0");
                if((startX-endX)<(-isDRAG))//오른쪽으로 드래그 한 경우
                {
                    Log.d("erroR","1");
                    i_month--;
                    if(i_month<1) {
                        if(i_year==1900)
                            return false;
                        i_year--;
                        i_month = 12;
                    }
                    s_month = String.valueOf(i_month);
                    if(i_month<10)
                        s_month = "0"+s_month;
                    s_year = String.valueOf(i_year);
                    Page_Initialization();
                    setDate(s_year,s_month,s_day);
                }
                else if((startX-endX)>isDRAG)//왼쪽으로 드래그 한 경우
                {
                    Log.d("erroR","2");
                    i_month++;
                    if(i_month>12) {
                        if(i_year==2500)
                            return false;
                        i_year++;
                        i_month = 1;
                    }
                    s_month = String.valueOf(i_month);
                    if(i_month<10)
                        s_month = "0"+s_month;
                    s_year = String.valueOf(i_year);
                    Page_Initialization();
                    setDate(s_year,s_month,s_day);
                }
                else if(curView.equals(v))//클릭한경우
                {
                    focus = (TextView)((LinearLayout)v).getChildAt(0);
                    String str="";
                    for (int i = 0; i < focus.getText().length(); i++)
                        str += focus.getText().charAt(i);
                    if(str.equals(""))
                        return true;
                    i_day = Integer.parseInt(str);
                    if(i_day<10)
                        s_day = "0"+str;
                    else
                        s_day = str;
                    itemSetting(i_year,i_month,i_day);
                    adapter.notifyDataSetChanged();
                    adapter_detail.notifyDataSetChanged();
                    Log.d("integerday",String.valueOf(i_day));
                    if(focusBack == v) // focus된 날짜를 다시클릭한경우 오류
                    {
                        if(((TextView)(focusBack.getChildAt(0))).getText().equals(""))
                            return true;
                        detailSchedule.bringToFront();
                        TextView text = (TextView)findViewById(R.id.detail_schedule_text);
                        text.setText(s_year+"/"+s_month+"/"+s_day);
                        detailItemSetting(i_year,i_month,i_day);
                        detailSchedule.setVisibility(View.VISIBLE);
                        calendarBackGround.setBackgroundColor(Color.GRAY);
                 //       main_calendar.bringToFront();
                        listview.setBackgroundColor(Color.GRAY);

                        isDetailCalendarOn=true;
                    }
                    else //focus가 아닌 날짜를 클릭한 경우
                    {
                        adapter.notifyDataSetChanged();
                        focusBack.setBackgroundColor(Color.WHITE);
                        focusBack = (LinearLayout) v;

                        focus = (TextView) focusBack.getChildAt(0);
                   //     focusLabel = (ImageView) focusBack.getChildAt(1);

/*
                        String str = new String("");
                        for (int i = 0; i < focus.getText().length(); i++)
                            str += focus.getText().charAt(i);
                        if(str.equals(""))
                            return true;
                        i_day = Integer.parseInt(str);
                        if(i_day<10)
                            s_day = "0"+str;
                        else
                            s_day = str;
*/
                        v.setBackgroundColor(Color.GRAY);
                        curView = v;

                    }
                }
                else
                {
                    Log.d("erroR","4");
                    return false;
                }
                return true;
            }
            else
                return false;
        }
    }
   public void onClickSetting(View v)
   {
       Intent myIntent = new Intent(getApplicationContext(), ColorSetting.class);
       Log.d("845","startColorSetting");
       startActivity(myIntent);
   }
    public void ClickMakeSchedule(String changable,String key)
    {
        Log.d("ClickMakeSchedule",key);
        Intent myIntent = new Intent(getApplicationContext(), MakeSchedule.class);
        //key정보도 같이 넘길 것
        myIntent.putExtra("dateAndkey",s_year+s_month+s_day+key);
        myIntent.putExtra("changable",changable);
        myIntent.putExtra("color","etc");
        Log.d("839","startMakeScheduleSetting");
        startActivity(myIntent);
        itemSetting(i_year,i_month,i_day);
        detailItemSetting(i_year,i_month,i_day);
    }

    public void onClickCancel(View v)
   {
       detailSchedule.setVisibility(View.INVISIBLE);
       calendarBackGround.setBackgroundColor(Color.WHITE);
      // main_calendar.bringToFront();
       listview.setBackgroundColor(Color.WHITE);
       isDetailCalendarOn=false;
   }
   public void onClickCancel2(View v)
   {
       detailSchedule2.setVisibility(View.INVISIBLE);
   }
   public void onClickCancel3(View v)
   {
       detailSchedule3.setVisibility(View.INVISIBLE);
   }
    public int find_dayBack(int week, int day)
    {
        //1주
        if(week==0 && day==0)
            return R.id.day1_1Back;
        else if(week==0 && day==1)
            return R.id.day1_2Back;
        else if(week==0 && day==2)
            return R.id.day1_3Back;
        else if(week==0 && day==3)
            return R.id.day1_4Back;
        else if(week==0 && day==4)
            return R.id.day1_5Back;
        else if(week==0 && day==5)
            return R.id.day1_6Back;
        else if(week==0 && day==6)
            return R.id.day1_7Back;
            //2주
        else if(week==1 && day==0)
            return R.id.day2_1Back;
        else if(week==1 && day==1)
            return R.id.day2_2Back;
        else if(week==1 && day==2)
            return R.id.day2_3Back;
        else if(week==1 && day==3)
            return R.id.day2_4Back;
        else if(week==1 && day==4)
            return R.id.day2_5Back;
        else if(week==1 && day==5)
            return R.id.day2_6Back;
        else if(week==1 && day==6)
            return R.id.day2_7Back;
            //3주
        else if(week==2 && day==0)
            return R.id.day3_1Back;
        else if(week==2 && day==1)
            return R.id.day3_2Back;
        else if(week==2 && day==2)
            return R.id.day3_3Back;
        else if(week==2 && day==3)
            return R.id.day3_4Back;
        else if(week==2 && day==4)
            return R.id.day3_5Back;
        else if(week==2 && day==5)
            return R.id.day3_6Back;
        else if(week==2 && day==6)
            return R.id.day3_7Back;
            //4주
        else if(week==3 && day==0)
            return R.id.day4_1Back;
        else if(week==3 && day==1)
            return R.id.day4_2Back;
        else if(week==3 && day==2)
            return R.id.day4_3Back;
        else if(week==3 && day==3)
            return R.id.day4_4Back;
        else if(week==3 && day==4)
            return R.id.day4_5Back;
        else if(week==3 && day==5)
            return R.id.day4_6Back;
        else if(week==3 && day==6)
            return R.id.day4_7Back;
            //5주
        else if(week==4 && day==0)
            return R.id.day5_1Back;
        else if(week==4 && day==1)
            return R.id.day5_2Back;
        else if(week==4 && day==2)
            return R.id.day5_3Back;
        else if(week==4 && day==3)
            return R.id.day5_4Back;
        else if(week==4 && day==4)
            return R.id.day5_5Back;
        else if(week==4 && day==5)
            return R.id.day5_6Back;
        else if(week==4 && day==6)
            return R.id.day5_7Back;
            //6주
        else if(week==5 && day==0)
            return R.id.day6_1Back;
        else if(week==5 && day==1)
            return R.id.day6_2Back;
        else if(week==5 && day==2)
            return R.id.day6_3Back;
        else if(week==5 && day==3)
            return R.id.day6_4Back;
        else if(week==5 && day==4)
            return R.id.day6_5Back;
        else if(week==5 && day==5)
            return R.id.day6_6Back;
        else if(week==5 && day==6)
            return R.id.day6_7Back;

        else // 전달 오류 시
            return 0;
    }
    public int find_dayLabel(int week, int day,int label)
    {
        //1주
        if(week==0 && day==0)
        {
            if(label==1)
                return R.id.day1_1Label1;
            else if(label==2)
                return R.id.day1_1Label2;
            else if(label==3)
                return R.id.day1_1Label3;
            else if(label==4)
                return R.id.day1_1Label4;
            else if(label==5)
                return R.id.day1_1Label5;
            else
                return 0;
        }
        else if(week==0 && day==1)
        {
            if(label==1)
                return R.id.day1_2Label1;
            else if(label==2)
                return R.id.day1_2Label2;
            else if(label==3)
                return R.id.day1_2Label3;
            else if(label==4)
                return R.id.day1_2Label4;
            else if(label==5)
                return R.id.day1_2Label5;
            else
                return 0;
        }
        else if(week==0 && day==2)
        {
            if(label==1)
                return R.id.day1_3Label1;
            else if(label==2)
                return R.id.day1_3Label2;
            else if(label==3)
                return R.id.day1_3Label3;
            else if(label==4)
                return R.id.day1_3Label4;
            else if(label==5)
                return R.id.day1_3Label5;
            else
                return 0;
        }
        else if(week==0 && day==3)
        {
            if(label==1)
                return R.id.day1_4Label1;
            else if(label==2)
                return R.id.day1_4Label2;
            else if(label==3)
                return R.id.day1_4Label3;
            else if(label==4)
                return R.id.day1_4Label4;
            else if(label==5)
                return R.id.day1_4Label5;
            else
                return 0;
        }
        else if(week==0 && day==4)
        {
            if(label==1)
                return R.id.day1_5Label1;
            else if(label==2)
                return R.id.day1_5Label2;
            else if(label==3)
                return R.id.day1_5Label3;
            else if(label==4)
                return R.id.day1_5Label4;
            else if(label==5)
                return R.id.day1_5Label5;
            else
                return 0;
        }
        else if(week==0 && day==5)
        {
            if(label==1)
                return R.id.day1_6Label1;
            else if(label==2)
                return R.id.day1_6Label2;
            else if(label==3)
                return R.id.day1_6Label3;
            else if(label==4)
                return R.id.day1_6Label4;
            else if(label==5)
                return R.id.day1_6Label5;
            else
                return 0;
        }
        else if(week==0 && day==6)
        {
            if(label==1)
                return R.id.day1_7Label1;
            else if(label==2)
                return R.id.day1_7Label2;
            else if(label==3)
                return R.id.day1_7Label3;
            else if(label==4)
                return R.id.day1_7Label4;
            else if(label==5)
                return R.id.day1_7Label5;
            else
                return 0;
        }
            //2주
        else if(week==1 && day==0)
        {
            if(label==1)
                return R.id.day2_1Label1;
            else if(label==2)
                return R.id.day2_1Label2;
            else if(label==3)
                return R.id.day2_1Label3;
            else if(label==4)
                return R.id.day2_1Label4;
            else if(label==5)
                return R.id.day2_1Label5;
            else
                return 0;
        }
        else if(week==1 && day==1)
        {
            if(label==1)
                return R.id.day2_2Label1;
            else if(label==2)
                return R.id.day2_2Label2;
            else if(label==3)
                return R.id.day2_2Label3;
            else if(label==4)
                return R.id.day2_2Label4;
            else if(label==5)
                return R.id.day2_2Label5;
            else
                return 0;
        }
        else if(week==1 && day==2)
        {
            if(label==1)
                return R.id.day2_3Label1;
            else if(label==2)
                return R.id.day2_3Label2;
            else if(label==3)
                return R.id.day2_3Label3;
            else if(label==4)
                return R.id.day2_3Label4;
            else if(label==5)
                return R.id.day2_3Label5;
            else
                return 0;
        }
        else if(week==1 && day==3)
        {
            if(label==1)
                return R.id.day2_4Label1;
            else if(label==2)
                return R.id.day2_4Label2;
            else if(label==3)
                return R.id.day2_4Label3;
            else if(label==4)
                return R.id.day2_4Label4;
            else if(label==5)
                return R.id.day2_4Label5;
            else
                return 0;
        }
        else if(week==1 && day==4)
        {
            if(label==1)
                return R.id.day2_5Label1;
            else if(label==2)
                return R.id.day2_5Label2;
            else if(label==3)
                return R.id.day2_5Label3;
            else if(label==4)
                return R.id.day2_5Label4;
            else if(label==5)
                return R.id.day2_5Label5;
            else
                return 0;
        }
        else if(week==1 && day==5)
        {
            if(label==1)
                return R.id.day2_6Label1;
            else if(label==2)
                return R.id.day2_6Label2;
            else if(label==3)
                return R.id.day2_6Label3;
            else if(label==4)
                return R.id.day2_6Label4;
            else if(label==5)
                return R.id.day2_6Label5;
            else
                return 0;
        }
        else if(week==1 && day==6)
        {
            if(label==1)
                return R.id.day2_7Label1;
            else if(label==2)
                return R.id.day2_7Label2;
            else if(label==3)
                return R.id.day2_7Label3;
            else if(label==4)
                return R.id.day2_7Label4;
            else if(label==5)
                return R.id.day2_7Label5;
            else
                return 0;
        }
            //3주
        else if(week==2 && day==0)
        {
            if(label==1)
                return R.id.day3_1Label1;
            else if(label==2)
                return R.id.day3_1Label2;
            else if(label==3)
                return R.id.day3_1Label3;
            else if(label==4)
                return R.id.day3_1Label4;
            else if(label==5)
                return R.id.day3_1Label5;
            else
                return 0;
        }
        else if(week==2 && day==1)
        {
            if(label==1)
                return R.id.day3_2Label1;
            else if(label==2)
                return R.id.day3_2Label2;
            else if(label==3)
                return R.id.day3_2Label3;
            else if(label==4)
                return R.id.day3_2Label4;
            else if(label==5)
                return R.id.day3_2Label5;
            else
                return 0;
        }
        else if(week==2 && day==2)
        {
            if(label==1)
                return R.id.day3_3Label1;
            else if(label==2)
                return R.id.day3_3Label2;
            else if(label==3)
                return R.id.day3_3Label3;
            else if(label==4)
                return R.id.day3_3Label4;
            else if(label==5)
                return R.id.day3_3Label5;
            else
                return 0;
        }
        else if(week==2 && day==3)
        {
            if(label==1)
                return R.id.day3_4Label1;
            else if(label==2)
                return R.id.day3_4Label2;
            else if(label==3)
                return R.id.day3_4Label3;
            else if(label==4)
                return R.id.day3_4Label4;
            else if(label==5)
                return R.id.day3_4Label5;
            else
                return 0;
        }
        else if(week==2 && day==4)
        {
            if(label==1)
                return R.id.day3_5Label1;
            else if(label==2)
                return R.id.day3_5Label2;
            else if(label==3)
                return R.id.day3_5Label3;
            else if(label==4)
                return R.id.day3_5Label4;
            else if(label==5)
                return R.id.day3_5Label5;
            else
                return 0;
        }
        else if(week==2 && day==5)
        {
            if(label==1)
                return R.id.day3_6Label1;
            else if(label==2)
                return R.id.day3_6Label2;
            else if(label==3)
                return R.id.day3_6Label3;
            else if(label==4)
                return R.id.day3_6Label4;
            else if(label==5)
                return R.id.day3_6Label5;
            else
                return 0;
        }
        else if(week==2 && day==6)
        {
            if(label==1)
                return R.id.day3_7Label1;
            else if(label==2)
                return R.id.day3_7Label2;
            else if(label==3)
                return R.id.day3_7Label3;
            else if(label==4)
                return R.id.day3_7Label4;
            else if(label==5)
                return R.id.day3_7Label5;
            else
                return 0;
        }
            //4주
        else if(week==3 && day==0)
        {
            if(label==1)
                return R.id.day4_1Label1;
            else if(label==2)
                return R.id.day4_1Label2;
            else if(label==3)
                return R.id.day4_1Label3;
            else if(label==4)
                return R.id.day4_1Label4;
            else if(label==5)
                return R.id.day4_1Label5;
            else
                return 0;
        }
        else if(week==3 && day==1)
        {
            if(label==1)
                return R.id.day4_2Label1;
            else if(label==2)
                return R.id.day4_2Label2;
            else if(label==3)
                return R.id.day4_2Label3;
            else if(label==4)
                return R.id.day4_2Label4;
            else if(label==5)
                return R.id.day4_2Label5;
            else
                return 0;
        }
        else if(week==3 && day==2)
        {
            if(label==1)
                return R.id.day4_3Label1;
            else if(label==2)
                return R.id.day4_3Label2;
            else if(label==3)
                return R.id.day4_3Label3;
            else if(label==4)
                return R.id.day4_3Label4;
            else if(label==5)
                return R.id.day4_3Label5;
            else
                return 0;
        }
        else if(week==3 && day==3)
        {
            if(label==1)
                return R.id.day4_4Label1;
            else if(label==2)
                return R.id.day4_4Label2;
            else if(label==3)
                return R.id.day4_4Label3;
            else if(label==4)
                return R.id.day4_4Label4;
            else if(label==5)
                return R.id.day4_4Label5;
            else
                return 0;
        }
        else if(week==3 && day==4)
        {
            if(label==1)
                return R.id.day4_5Label1;
            else if(label==2)
                return R.id.day4_5Label2;
            else if(label==3)
                return R.id.day4_5Label3;
            else if(label==4)
                return R.id.day4_5Label4;
            else if(label==5)
                return R.id.day4_5Label5;
            else
                return 0;
        }
        else if(week==3 && day==5)
        {
            if(label==1)
                return R.id.day4_6Label1;
            else if(label==2)
                return R.id.day4_6Label2;
            else if(label==3)
                return R.id.day4_6Label3;
            else if(label==4)
                return R.id.day4_6Label4;
            else if(label==5)
                return R.id.day4_6Label5;
            else
                return 0;
        }
        else if(week==3 && day==6)
        {
            if(label==1)
                return R.id.day4_7Label1;
            else if(label==2)
                return R.id.day4_7Label2;
            else if(label==3)
                return R.id.day4_7Label3;
            else if(label==4)
                return R.id.day4_7Label4;
            else if(label==5)
                return R.id.day4_7Label5;
            else
                return 0;
        }
            //5주
        else if(week==4 && day==0)
        {
            if(label==1)
                return R.id.day5_1Label1;
            else if(label==2)
                return R.id.day5_1Label2;
            else if(label==3)
                return R.id.day5_1Label3;
            else if(label==4)
                return R.id.day5_1Label4;
            else if(label==5)
                return R.id.day5_1Label5;
            else
                return 0;
        }
        else if(week==4 && day==1)
        {
            if(label==1)
                return R.id.day5_2Label1;
            else if(label==2)
                return R.id.day5_2Label2;
            else if(label==3)
                return R.id.day5_2Label3;
            else if(label==4)
                return R.id.day5_2Label4;
            else if(label==5)
                return R.id.day5_2Label5;
            else
                return 0;
        }
        else if(week==4 && day==2)
        {
            if(label==1)
                return R.id.day5_3Label1;
            else if(label==2)
                return R.id.day5_3Label2;
            else if(label==3)
                return R.id.day5_3Label3;
            else if(label==4)
                return R.id.day5_3Label4;
            else if(label==5)
                return R.id.day5_3Label5;
            else
                return 0;
        }
        else if(week==4 && day==3)
        {
            if(label==1)
                return R.id.day5_4Label1;
            else if(label==2)
                return R.id.day5_4Label2;
            else if(label==3)
                return R.id.day5_4Label3;
            else if(label==4)
                return R.id.day5_4Label4;
            else if(label==5)
                return R.id.day5_4Label5;
            else
                return 0;
        }
        else if(week==4 && day==4)
        {
            if(label==1)
                return R.id.day5_5Label1;
            else if(label==2)
                return R.id.day5_5Label2;
            else if(label==3)
                return R.id.day5_5Label3;
            else if(label==4)
                return R.id.day5_5Label4;
            else if(label==5)
                return R.id.day5_5Label5;
            else
                return 0;
        }
        else if(week==4 && day==5)
        {
            if(label==1)
                return R.id.day5_6Label1;
            else if(label==2)
                return R.id.day5_6Label2;
            else if(label==3)
                return R.id.day5_6Label3;
            else if(label==4)
                return R.id.day5_6Label4;
            else if(label==5)
                return R.id.day5_6Label5;
            else
                return 0;
        }
        else if(week==4 && day==6)
        {
            if(label==1)
                return R.id.day5_7Label1;
            else if(label==2)
                return R.id.day5_7Label2;
            else if(label==3)
                return R.id.day5_7Label3;
            else if(label==4)
                return R.id.day5_7Label4;
            else if(label==5)
                return R.id.day5_7Label5;
            else
                return 0;
        }
            //6주
        else if(week==5 && day==0)
        {
            if(label==1)
                return R.id.day6_1Label1;
            else if(label==2)
                return R.id.day6_1Label2;
            else if(label==3)
                return R.id.day6_1Label3;
            else if(label==4)
                return R.id.day6_1Label4;
            else if(label==5)
                return R.id.day6_1Label5;
            else
                return 0;
        }
        else if(week==5 && day==1)
        {
            if(label==1)
                return R.id.day6_2Label1;
            else if(label==2)
                return R.id.day6_2Label2;
            else if(label==3)
                return R.id.day6_2Label3;
            else if(label==4)
                return R.id.day6_2Label4;
            else if(label==5)
                return R.id.day6_2Label5;
            else
                return 0;
        }
        else if(week==5 && day==2)
        {
            if(label==1)
                return R.id.day6_3Label1;
            else if(label==2)
                return R.id.day6_3Label2;
            else if(label==3)
                return R.id.day6_3Label3;
            else if(label==4)
                return R.id.day6_3Label4;
            else if(label==5)
                return R.id.day6_3Label5;
            else
                return 0;
        }
        else if(week==5 && day==3)
        {
            if(label==1)
                return R.id.day6_4Label1;
            else if(label==2)
                return R.id.day6_4Label2;
            else if(label==3)
                return R.id.day6_4Label3;
            else if(label==4)
                return R.id.day6_4Label4;
            else if(label==5)
                return R.id.day6_4Label5;
            else
                return 0;
        }
        else if(week==5 && day==4)
        {
            if(label==1)
                return R.id.day6_5Label1;
            else if(label==2)
                return R.id.day6_5Label2;
            else if(label==3)
                return R.id.day6_5Label3;
            else if(label==4)
                return R.id.day6_5Label4;
            else if(label==5)
                return R.id.day6_5Label5;
            else
                return 0;
        }
        else if(week==5 && day==5)
        {
            if(label==1)
                return R.id.day6_6Label1;
            else if(label==2)
                return R.id.day6_6Label2;
            else if(label==3)
                return R.id.day6_6Label3;
            else if(label==4)
                return R.id.day6_6Label4;
            else if(label==5)
                return R.id.day6_6Label5;
            else
                return 0;
        }
        else if(week==5 && day==6)
        {
            if(label==1)
                return R.id.day6_7Label1;
            else if(label==2)
                return R.id.day6_7Label2;
            else if(label==3)
                return R.id.day6_7Label3;
            else if(label==4)
                return R.id.day6_7Label4;
            else if(label==5)
                return R.id.day6_7Label5;
            else
                return 0;
        }

        else // 전달 오류 시
            return 0;
    }
    public int find_day(int week,int day)//해당 일의 id
    {
        //1주
        if(week==0 && day==0)
            return R.id.day1_1;
        else if(week==0 && day==1)
            return R.id.day1_2;
        else if(week==0 && day==2)
            return R.id.day1_3;
        else if(week==0 && day==3)
            return R.id.day1_4;
        else if(week==0 && day==4)
            return R.id.day1_5;
        else if(week==0 && day==5)
            return R.id.day1_6;
        else if(week==0 && day==6)
            return R.id.day1_7;
            //2주
        else if(week==1 && day==0)
            return R.id.day2_1;
        else if(week==1 && day==1)
            return R.id.day2_2;
        else if(week==1 && day==2)
            return R.id.day2_3;
        else if(week==1 && day==3)
            return R.id.day2_4;
        else if(week==1 && day==4)
            return R.id.day2_5;
        else if(week==1 && day==5)
            return R.id.day2_6;
        else if(week==1 && day==6)
            return R.id.day2_7;
            //3주
        else if(week==2 && day==0)
            return R.id.day3_1;
        else if(week==2 && day==1)
            return R.id.day3_2;
        else if(week==2 && day==2)
            return R.id.day3_3;
        else if(week==2 && day==3)
            return R.id.day3_4;
        else if(week==2 && day==4)
            return R.id.day3_5;
        else if(week==2 && day==5)
            return R.id.day3_6;
        else if(week==2 && day==6)
            return R.id.day3_7;
            //4주
        else if(week==3 && day==0)
            return R.id.day4_1;
        else if(week==3 && day==1)
            return R.id.day4_2;
        else if(week==3 && day==2)
            return R.id.day4_3;
        else if(week==3 && day==3)
            return R.id.day4_4;
        else if(week==3 && day==4)
            return R.id.day4_5;
        else if(week==3 && day==5)
            return R.id.day4_6;
        else if(week==3 && day==6)
            return R.id.day4_7;
            //5주
        else if(week==4 && day==0)
            return R.id.day5_1;
        else if(week==4 && day==1)
            return R.id.day5_2;
        else if(week==4 && day==2)
            return R.id.day5_3;
        else if(week==4 && day==3)
            return R.id.day5_4;
        else if(week==4 && day==4)
            return R.id.day5_5;
        else if(week==4 && day==5)
            return R.id.day5_6;
        else if(week==4 && day==6)
            return R.id.day5_7;
            //6주
        else if(week==5 && day==0)
            return R.id.day6_1;
        else if(week==5 && day==1)
            return R.id.day6_2;
        else if(week==5 && day==2)
            return R.id.day6_3;
        else if(week==5 && day==3)
            return R.id.day6_4;
        else if(week==5 && day==4)
            return R.id.day6_5;
        else if(week==5 && day==5)
            return R.id.day6_6;
        else if(week==5 && day==6)
            return R.id.day6_7;

        else // 전달 오류 시
            return 0;
    }
    public boolean isColor(String color)
    {
        Log.d("isColor",color);
        for(int i=0;i<color.length();i++)
            if(color.charAt(i)==';')
                return false;
        return true;

    }
    public int whatKind(String kind)
    {
        String key="";
        for(int i=0;i<3;i++)
            key+= kind.charAt(i);
        if(key.equals("CAL"))
            return CAL;
        else if(key.equals("NEW"))
            return NEW;
        else if(key.equals("LEC"))
            return LEC;
        else
            return -1;
    }
    public int whatColor(String color)
    {
        if(color.equals("computer"))
            return computer_color;
        else if(color.equals("computer_office"))
            return computer_office_color;
        else if(color.equals("it_universe"))
            return it_universe_color;
        else if(color.equals("universe"))
            return universe_color;
        else if(color.equals("etc")) {
            return etc_color;
        }
        else
            return 0;
    }
    public void exchangeActivity(View v)
    {
        //화면에 이동할 버튼 띄워주기
        Toast.makeText(getApplicationContext(),"button",
                Toast.LENGTH_SHORT).show();
        if(clicked==0)
        {
            ImageView iv = (ImageView)findViewById(R.id.menuCalendar);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            iv = (ImageView)findViewById(R.id.menuNewsstand);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            iv = (ImageView)findViewById(R.id.menuLectureinfo);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            clicked=1;
            iv = (ImageView)findViewById(R.id.menu);
            iv.setImageResource(R.mipmap.white_plus_icon);
        }
        else
        {
            ImageView iv = (ImageView)findViewById(R.id.menuCalendar);
            iv.setVisibility(View.INVISIBLE);
            iv = (ImageView)findViewById(R.id.menuNewsstand);
            iv.setVisibility(View.INVISIBLE);
            iv = (ImageView)findViewById(R.id.menuLectureinfo);
            iv.setVisibility(View.INVISIBLE);
            clicked=0;
            iv = (ImageView)findViewById(R.id.menu);
            iv.setImageResource(R.mipmap.black_plus_icon);
        }
    }
    public void exchangeActivityCalendar(View v)
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
    public void exchangeActivityNewsstand(View v)
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
    public void exchangeActivityLectureinfo(View v)
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
