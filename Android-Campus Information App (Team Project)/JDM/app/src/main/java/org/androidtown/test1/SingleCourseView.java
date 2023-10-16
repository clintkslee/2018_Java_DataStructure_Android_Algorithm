package org.androidtown.test1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleCourseView extends LinearLayout {
    TextView courseName;
    TextView courseKind;
    TextView courseCode;
    TextView courseProfName;
    TextView courseForWhom;
    TextView courseTime;
    TextView courseWhen;

    public SingleCourseView(Context context) {
        super(context);
        init(context);
    }

    public SingleCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.course_list, this, true);
        courseName = (TextView) findViewById(R.id.courseName);
        courseKind = (TextView) findViewById(R.id.courseKind);
        courseCode = (TextView) findViewById(R.id.courseCode);
        courseProfName = (TextView) findViewById(R.id.courseProfName);
        courseForWhom = (TextView)findViewById(R.id.courseForWhom);
        courseTime = (TextView)findViewById(R.id.courseTime);
        courseWhen = (TextView)findViewById(R.id.courseWhen);
    }

    public void setCourseName(String courseName) {
        this.courseName.setText(courseName);
    }
    public void setCourseKind(String courseKind) {
        this.courseKind.setText(courseKind);
    }
    public void setCourseCode(String courseCode) {
        this.courseCode.setText(courseCode);
    }
    public void setCourseProfName(String courseProfName) {
        this.courseProfName.setText(courseProfName);
    }
    public void setCourseForWhom(String courseForWhom) {
        this.courseForWhom.setText(courseForWhom);
    }
    public void setCourseTime(String courseTime) {
        this.courseTime.setText(courseTime);
    }
    public void setCourseWhen(String courseWhen) {
        this.courseWhen.setText(courseWhen);
    }
}