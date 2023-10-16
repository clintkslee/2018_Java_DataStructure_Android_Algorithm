package org.androidtown.test1;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CourseMainActivity extends AppCompatActivity {
    JSONArray professorArray;
    Professor[] professors;
    JSONArray courseArray;
    Course[] courses;
    Button mainProfessorButton;
    Button mainCourseButton;;
    RelativeLayout tabPage1;
    RelativeLayout tabPage2;
    EditText profEditText;
    ListView listView;
    ListView listView2;
    ProfessorAdapter adapterALL;
    ProfessorAdapter adapterTEMP;
    CourseAdapter adapterALL2;
    CourseAdapter adapterTEMP2;
    Button miniButton;
    String tempString;
    Spinner profCollegeSpinner;
    Spinner profMajorSpinner;
    EditText courseEditText;
    EditText courseClassEditText;
    Button CourseSelectiveButton; //일반선택
    Button CourseTeacherButton; //교직
    Spinner courseCollegeSpinner;
    Spinner courseMajorSpinner;
    Spinner courseRequiredGradeSpinner;
    Spinner courseRequiredSubjectSpinner;
    Spinner courseCulturalGradeSpinner;
    private static final int CAL=0,NEW=1,LEC=2;//추가static변수
    int activity,clicked;//추가변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);
        activity = 2;
        clicked=0;
        ImageView plused_iv = (ImageView)findViewById(R.id.menu2);
        plused_iv.bringToFront();

        profEditText = (EditText)findViewById(R.id.ProfEditText);
        tabPage1 = (RelativeLayout)findViewById(R.id.RelativeLayoutProfInfo);
        tabPage2 = (RelativeLayout)findViewById(R.id.RelativeLayoutCourseInfo);
        tabPage2.setVisibility(View.INVISIBLE);
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final Button resetButton2 = (Button)findViewById(R.id.resetButton2);
        final RelativeLayout RL1 = (RelativeLayout)findViewById(R.id.RL1);
        final  RelativeLayout RL2 = (RelativeLayout)findViewById(R.id.RL2);
        final RelativeLayout RL3 = (RelativeLayout)findViewById(R.id.RL3);
        final RelativeLayout RL4 = (RelativeLayout)findViewById(R.id.RL4);
        final RelativeLayout RL5 = (RelativeLayout)findViewById(R.id.RL5);
        final RelativeLayout RL6 = (RelativeLayout)findViewById(R.id.RL6);
        final Button viewAllButton2 = (Button)findViewById(R.id.viewAllButton2);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        professors = new Professor[62];
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            professorArray = jsonObject.getJSONArray("professor");
            for (int i = 0 ; i < professorArray.length(); i ++) {
                JSONObject object = professorArray.getJSONObject(i);
                professors[i] = new Professor(object.getString("name"), object.getString("college"), object.getString("major"), object.getString("lab"), object.getString("tel"), object.getString("mail"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
        courses = new Course[29];
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            courseArray = jsonObject.getJSONArray("course");
            for (int i = 0 ; i < courseArray.length(); i ++) {
                JSONObject object = courseArray.getJSONObject(i);
                courses[i] = new Course(object.getString("name"), object.getString("kind"), object.getString("code"),
                        object.getString("teacher"), object.getString("forwho"), object.getString("time"), object.getString("when"),
                        object.getString("spin1"), object.getString("spin2"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }

        adapterALL = new ProfessorAdapter();
        for(int i=0; i<professors.length; i++) {
            adapterALL.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
        }
        listView.setAdapter(adapterALL);
        adapterTEMP = adapterALL;

        adapterALL2 = new CourseAdapter();
        for(int i=0; i<courses.length; i++) {
            adapterALL2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
        }
        listView2.setAdapter(adapterALL2);
        adapterTEMP2=adapterALL2;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

        profMajorSpinner = (Spinner)findViewById(R.id.ProfMajorSpinner);
        profCollegeSpinner = (Spinner)findViewById(R.id.ProfCollegeSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.College));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        profCollegeSpinner.setAdapter(spinnerAdapter);
        profCollegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case (1):
                        populateSubSpinners(R.array.Humanities);
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("독어독문학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("일어일문학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (2):
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("수학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("물리학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        populateSubSpinners(R.array.Natural);
                        break;
                    case (3):
                        populateSubSpinners(R.array.Law);
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("법학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("국제법무학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (4):
                        populateSubSpinners(R.array.Social);
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("행정학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("언론홍보학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);;
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (5):
                        populateSubSpinners(R.array.Econimic);
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("글로벌통상학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("국제무역학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (6):
                        populateSubSpinners(R.array.Business);
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("경영학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("금융학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (7):
                        populateSubSpinners(R.array.Engineering);
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("화학공학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("유기신소재파이버공학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (3):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("전기공학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (4):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("기계공학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (5):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("산업정보시스템공학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (6):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("건축학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (8):
                        populateSubSpinners(R.array.IT);
                        profMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                                adapterTEMP = new ProfessorAdapter();
                                switch(iter) {
                                    case (1):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("컴퓨터학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (2):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("전자정보공학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (3):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("글로벌미디어학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (4):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("소프트웨어학부"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (5):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("스마트시스템소프트웨어학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                    case (6):
                                        for (int i = 0; i < professors.length; i++) {
                                            if (professors[i].getMajor().equals("미디어경영학과"))
                                                adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                                        }
                                        listView.setAdapter(adapterTEMP);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ///////////////////////////////////////////////////////////////////이하 교양선택 Spinner
        courseCulturalGradeSpinner = (Spinner)findViewById(R.id.CourseCulturalGradeSpinner);
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.cselect));
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseCulturalGradeSpinner.setAdapter(spinnerAdapter2);
        courseCulturalGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                adapterTEMP2 = new CourseAdapter();
                switch(iter) {
                    case (1):
                        for (int i = 0; i < courses.length; i++) {
                            if (courses[i].getSpin1().equals("기초역량(국제어문-국제어)"))
                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                        }
                        listView2.setAdapter(adapterTEMP2);
                        listView2.setVisibility(View.VISIBLE);
                        RL1.setVisibility(View.INVISIBLE);
                        RL2.setVisibility(View.INVISIBLE);
                        RL3.setVisibility(View.INVISIBLE);
                        RL4.setVisibility(View.INVISIBLE);
                        RL5.setVisibility(View.INVISIBLE);
                        RL6.setVisibility(View.INVISIBLE);
                        resetButton2.setVisibility(View.VISIBLE);
                        break;
                    case (2):
                        for (int i = 0; i < courses.length; i++) {
                            if (courses[i].getSpin1().equals("숭실품성(인성-종교가치인성교육)"))
                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                        }
                        listView2.setAdapter(adapterTEMP2);
                        listView2.setVisibility(View.VISIBLE);
                        RL1.setVisibility(View.INVISIBLE);
                        RL2.setVisibility(View.INVISIBLE);
                        RL3.setVisibility(View.INVISIBLE);
                        RL4.setVisibility(View.INVISIBLE);
                        RL5.setVisibility(View.INVISIBLE);
                        RL6.setVisibility(View.INVISIBLE);
                        resetButton2.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /////////////////////////////////////////////////////////////////////////////////이하 교양필수 Spinner
        courseRequiredGradeSpinner = (Spinner)findViewById(R.id.CourseRequiredGradeSpinner);
        courseRequiredSubjectSpinner = (Spinner)findViewById(R.id.CourseRequiredSubjectSpinner);
        ArrayAdapter<String> spinnerAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.ckyopil1));
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseRequiredGradeSpinner.setAdapter(spinnerAdapter3);
        courseRequiredGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                switch(iter){
                    case (1):
                        populateSubSpinners2(R.array.cchapel);
                        courseRequiredSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i1, long l) {
                                adapterTEMP2 = new CourseAdapter();
                                switch (i1){
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("채플"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (2):
                        populateSubSpinners2(R.array.cgrade1);
                        courseRequiredSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long l) {
                                adapterTEMP2 = new CourseAdapter();
                                switch (i2){
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("영어1"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("컴퓨팅적사고"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (3):
                        populateSubSpinners2(R.array.cgrade2);
                        courseRequiredSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long l) {
                                adapterTEMP2 = new CourseAdapter();
                                switch (i3){
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("숭실인의역량과진로탐색2"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////이하 강의정보-학부전공별 Spinner
        courseCollegeSpinner = (Spinner)findViewById(R.id.CourseCollegeSpinner);
        courseMajorSpinner = (Spinner)findViewById(R.id.CourseMajorSpinner);
        ArrayAdapter<String> spinnerAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.cCollege));
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseCollegeSpinner.setAdapter(spinnerAdapter4);
        courseCollegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int iter, long l) {
                switch(iter) {
                    case (1):
                        populateSubSpinners3(R.array.cHumanities);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("독어독문학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("일어일문학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (2):
                        populateSubSpinners3(R.array.cNatural);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("수학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("물리학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case (3):
                        populateSubSpinners3(R.array.cLaw);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("법학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("국제법무학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case (4):
                        populateSubSpinners3(R.array.Social);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("행정학부"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("언론홍보학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case (5):
                        populateSubSpinners3(R.array.cEconimic);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("글로벌통상학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("국제무역학과"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case (6):
                        populateSubSpinners3(R.array.cBusiness);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("경영학부"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("금융학부"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case (7):
                        populateSubSpinners3(R.array.cEngineering);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):for (int i = 0; i < courses.length; i++) {
                                        if (courses[i].getSpin2().equals("화학공학과"))
                                            adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                    }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("기계공학부"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case (8):
                        populateSubSpinners3(R.array.cIT);
                        courseMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                                adapterTEMP2=new CourseAdapter();
                                switch (ii) {
                                    case (1):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("컴퓨터학부"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                    case (2):
                                        for (int i = 0; i < courses.length; i++) {
                                            if (courses[i].getSpin2().equals("소프트웨어학부"))
                                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                                        }
                                        listView2.setAdapter(adapterTEMP2);
                                        listView2.setVisibility(View.VISIBLE);
                                        RL1.setVisibility(View.INVISIBLE);
                                        RL2.setVisibility(View.INVISIBLE);
                                        RL3.setVisibility(View.INVISIBLE);
                                        RL4.setVisibility(View.INVISIBLE);
                                        RL5.setVisibility(View.INVISIBLE);
                                        RL6.setVisibility(View.INVISIBLE);
                                        resetButton2.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////
        final CharSequence info[] = new CharSequence[] {"전화 연결", "이메일 복사" };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("작업을 선택하세요");

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long ld) {
                final Professor item = (Professor) adapterTEMP.getItem(position);
                builder.setItems(info, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which)
                        {
                            case 0:
                                // 전화 연결
                                String tel1=item.getTel();
                                String tel2="tel:02";
                                tel1=tel1.replace("-","");
                                tel2=tel2.concat(tel1);
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(tel2)));
                                break;
                            case 1:
                                // 이메일 복사
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("MAIL", item.getMail());
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(getApplication(), "이메일이 클립보드에 복사되었습니다.",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////
        final CharSequence info2[] = new CharSequence[] {"강의계획서 미리보기"};
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("작업을 선택하세요");

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long ld) {
                final Course item = (Course) adapterTEMP2.getItem(position);
                builder2.setItems(info2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which)
                        {
                            case 0:
                                // 강의계획서 미리보기
                                Intent ImageIntent = new Intent(getApplicationContext(), ShowCourse.class);
                                ImageIntent.putExtra("imagename", item.getName());
                                startActivity(ImageIntent);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder2.show();
                return false;
            }
        });

        mainProfessorButton = (Button) findViewById(R.id.MainProfessorButton);
        mainProfessorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabPage1.setVisibility(View.VISIBLE);
                tabPage2.setVisibility(View.INVISIBLE);
            }
        });
        mainCourseButton = (Button) findViewById(R.id.MainCourseButton);
        mainCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabPage2.setVisibility(View.VISIBLE);
                tabPage1.setVisibility(View.INVISIBLE);
                resetButton2.performClick();
            }
        });
        miniButton = (Button)findViewById(R.id.minibutton);
        miniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterTEMP = new ProfessorAdapter();
                if (profEditText.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                    adapterTEMP=adapterALL;
                    listView.setAdapter(adapterTEMP);
                } else {
                    tempString=profEditText.getText().toString();
                    for(int i=0; i<professors.length; i++)
                    {
                        if(professors[i].getName().equals(tempString))
                            adapterTEMP.addItem(new Professor(professors[i].getName(), professors[i].getCollege(), professors[i].getMajor(), professors[i].getLab(), professors[i].getTel(), professors[i].getMail()));
                    }
                    if(adapterTEMP.getCount()==0){
                        Toast.makeText(getApplicationContext(), "존재하지 않는 이름입니다.", Toast.LENGTH_SHORT).show();
                        adapterTEMP=adapterALL;
                        listView.setAdapter(adapterTEMP);

                    } else {
                        Toast.makeText(getApplicationContext(), adapterTEMP.getCount()+"명 검색되었습니다.", Toast.LENGTH_SHORT).show();
                        listView.setAdapter(adapterTEMP);
                    }
                }
            }
        });
        profEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    miniButton.performClick();
                    return true;
                }
                return false;
            }
        });
        ////////////////////////////////////////////////////////이하 강의정보 리스너 대충
        viewAllButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RL1.setVisibility(View.INVISIBLE);
                RL2.setVisibility(View.INVISIBLE);
                RL3.setVisibility(View.INVISIBLE);
                RL4.setVisibility(View.INVISIBLE);
                RL5.setVisibility(View.INVISIBLE);
                RL6.setVisibility(View.INVISIBLE);
                adapterTEMP2=adapterALL2;
                listView2.setAdapter(adapterTEMP2);
                listView2.setVisibility(View.VISIBLE);
                resetButton2.setVisibility(View.VISIBLE);
            }
        });
        resetButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RL1.setVisibility(View.VISIBLE);
                RL2.setVisibility(View.VISIBLE);
                RL3.setVisibility(View.VISIBLE);
                RL4.setVisibility(View.VISIBLE);
                RL5.setVisibility(View.VISIBLE);
                RL6.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.INVISIBLE);
                resetButton2.setVisibility(View.INVISIBLE);
            }
        });
        courseEditText = (EditText)findViewById(R.id.CourseEditText);
        courseEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    adapterTEMP2 = new CourseAdapter();
                    if (courseEditText.getText().toString().length() == 0 ) {
                        Toast.makeText(getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                        adapterTEMP2=adapterALL2;
                        listView2.setAdapter(adapterTEMP2);
                    } else {
                        tempString=courseEditText.getText().toString();
                        for(int i=0; i<courses.length; i++)
                        {
                            if(courses[i].getTeacher().equals(tempString))
                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                        }
                        if(adapterTEMP2.getCount()==0){
                            Toast.makeText(getApplicationContext(), "존재하지 않는 이름입니다.", Toast.LENGTH_SHORT).show();
                            adapterTEMP2=adapterALL2;
                            listView2.setAdapter(adapterTEMP2);
                        } else {
                            Toast.makeText(getApplicationContext(), adapterTEMP2.getCount()+"명 검색되었습니다.", Toast.LENGTH_SHORT).show();
                            listView2.setAdapter(adapterTEMP2);
                            listView2.setVisibility(View.VISIBLE);
                            RL1.setVisibility(View.INVISIBLE);
                            RL2.setVisibility(View.INVISIBLE);
                            RL3.setVisibility(View.INVISIBLE);
                            RL4.setVisibility(View.INVISIBLE);
                            RL5.setVisibility(View.INVISIBLE);
                            RL6.setVisibility(View.INVISIBLE);
                            resetButton2.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        courseClassEditText = (EditText)findViewById(R.id.CourseClassEditText);
        courseClassEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    adapterTEMP2 = new CourseAdapter();
                    if (courseClassEditText.getText().toString().length() == 0 ) {
                        Toast.makeText(getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                        adapterTEMP2=adapterALL2;
                        listView2.setAdapter(adapterTEMP2);
                    } else {
                        tempString=courseClassEditText.getText().toString();
                        for(int i=0; i<courses.length; i++)
                        {
                            if(courses[i].getName().equals(tempString))
                                adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                        }
                        if(adapterTEMP2.getCount()==0){
                            Toast.makeText(getApplicationContext(), "존재하지 않는 강의명입니다.", Toast.LENGTH_SHORT).show();
                            adapterTEMP2=adapterALL2;
                            listView2.setAdapter(adapterTEMP2);
                        } else {
                            Toast.makeText(getApplicationContext(), adapterTEMP2.getCount()+"개 강의가 검색되었습니다.", Toast.LENGTH_SHORT).show();
                            listView2.setAdapter(adapterTEMP2);
                            listView2.setVisibility(View.VISIBLE);
                            RL1.setVisibility(View.INVISIBLE);
                            RL2.setVisibility(View.INVISIBLE);
                            RL3.setVisibility(View.INVISIBLE);
                            RL4.setVisibility(View.INVISIBLE);
                            RL5.setVisibility(View.INVISIBLE);
                            RL6.setVisibility(View.INVISIBLE);
                            resetButton2.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        CourseSelectiveButton = (Button)findViewById(R.id.CourseSelectiveButton);
        CourseSelectiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterTEMP2 = new CourseAdapter();
                for(int i=0; i<courses.length; i++)
                {
                    if(courses[i].getKind().equals("일반선택"))
                        adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                }
                Toast.makeText(getApplicationContext(), adapterTEMP2.getCount()+"개 강의가 검색되었습니다.", Toast.LENGTH_SHORT).show();
                listView2.setAdapter(adapterTEMP2);
                listView2.setVisibility(View.VISIBLE);
                RL1.setVisibility(View.INVISIBLE);
                RL2.setVisibility(View.INVISIBLE);
                RL3.setVisibility(View.INVISIBLE);
                RL4.setVisibility(View.INVISIBLE);
                RL5.setVisibility(View.INVISIBLE);
                RL6.setVisibility(View.INVISIBLE);
                resetButton2.setVisibility(View.VISIBLE);
            }
        });

        CourseTeacherButton = (Button)findViewById(R.id.bt1);
        CourseTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterTEMP2 = new CourseAdapter();
                for(int i=0; i<courses.length; i++)
                {
                    if(courses[i].getKind().equals("교직"))
                        adapterTEMP2.addItem(new Course(courses[i].getName(), courses[i].getKind(), courses[i].getCode(), courses[i].getTeacher(), courses[i].getForwho(), courses[i].getTime(), courses[i].getWhen(), courses[i].getSpin1(), courses[i].getSpin2()));
                }
                Toast.makeText(getApplicationContext(), adapterTEMP2.getCount()+"개 강의가 검색되었습니다.", Toast.LENGTH_SHORT).show();
                listView2.setAdapter(adapterTEMP2);
                listView2.setVisibility(View.VISIBLE);
                RL1.setVisibility(View.INVISIBLE);
                RL2.setVisibility(View.INVISIBLE);
                RL3.setVisibility(View.INVISIBLE);
                RL4.setVisibility(View.INVISIBLE);
                RL5.setVisibility(View.INVISIBLE);
                RL6.setVisibility(View.INVISIBLE);
                resetButton2.setVisibility(View.VISIBLE);
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open("mynogada.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private class ProfessorAdapter extends BaseAdapter {
        private ArrayList<Professor> items = new ArrayList();
        @Override
        public int getCount() { return items.size(); }
        @Override
        public Object getItem(int position) { return items.get(position); }
        @Override
        public long getItemId(int position) { return position; }
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            SingleProfessorView view = (SingleProfessorView) convertView;
            if (convertView == null)
                view = new SingleProfessorView(getApplicationContext());
            Log.d("Prof","professor"+position);
            Professor item = items.get(position);
            view.setName(item.getName());
            view.setMajor(item.getMajor());
            view.setLab(item.getLab());
            view.setTel(item.getTel());
            view.setMail(item.getMail());
            return view;
        }
        void addItem(Professor item) { items.add(item); }
    }

    private class CourseAdapter extends BaseAdapter {
        private ArrayList<Course> items = new ArrayList();
        @Override
        public int getCount() { return items.size(); }
        @Override
        public Object getItem(int position) { return items.get(position); }
        @Override
        public long getItemId(int position) { return position; }
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            SingleCourseView view = (SingleCourseView) convertView;
            if (convertView == null)
                view = new SingleCourseView(getApplicationContext());
            Log.d("Course","course"+position);
            Course item = items.get(position);
            view.setCourseName(item.getName());
            view.setCourseKind(item.getKind());
            view.setCourseCode(item.getCode());
            view.setCourseProfName(item.getTeacher());
            view.setCourseForWhom(item.getForwho());
            view.setCourseTime(item.getTime());
            view.setCourseWhen(item.getWhen());
            return view;
        }
        void addItem(Course item) { items.add(item); }
    }

    private void populateSubSpinners(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(this,
                itemNum,
                android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profMajorSpinner.setAdapter(fAdapter);
    }
    private void populateSubSpinners2(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(this,
                itemNum,
                android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseRequiredSubjectSpinner.setAdapter(fAdapter);
    }
    private void populateSubSpinners3(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(this,
                itemNum,
                android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseMajorSpinner.setAdapter(fAdapter);
    }
    //menu 이동 버튼
    public void exchangeActivity2(View v)
    {
        //화면에 이동할 버튼 띄워주기
        Toast.makeText(getApplicationContext(),"button",
                Toast.LENGTH_SHORT).show();
        if(clicked==0)
        {
            ImageView iv = (ImageView)findViewById(R.id.menuCalendar2);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            iv = (ImageView)findViewById(R.id.menuNewsstand2);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            iv = (ImageView)findViewById(R.id.menuLectureinfo2);
            iv.bringToFront();
            iv.setVisibility(View.VISIBLE);
            clicked=1;
            iv = (ImageView)findViewById(R.id.menu2);
            iv.setImageResource(R.mipmap.white_plus_icon);
        }
        else
        {
            ImageView iv = (ImageView)findViewById(R.id.menuCalendar2);
            iv.setVisibility(View.INVISIBLE);
            iv = (ImageView)findViewById(R.id.menuNewsstand2);
            iv.setVisibility(View.INVISIBLE);
            iv = (ImageView)findViewById(R.id.menuLectureinfo2);
            iv.setVisibility(View.INVISIBLE);
            clicked=0;
            iv = (ImageView)findViewById(R.id.menu2);
            iv.setImageResource(R.mipmap.black_plus_icon);
        }
    }
    public void exchangeActivityCalendar2(View v)
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
    public void exchangeActivityNewsstand2(View v)
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
    public void exchangeActivityLectureinfo2(View v)
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

