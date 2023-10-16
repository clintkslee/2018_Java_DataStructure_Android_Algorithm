package org.androidtown.test1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SingleProfessorView extends LinearLayout {
    TextView name;
    TextView major;
    TextView lab;
    TextView tel;
    TextView mail;

    public SingleProfessorView (Context context) {
        super(context);
        init(context);
    }

    public SingleProfessorView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.professor_list, this, true);
        name = (TextView) findViewById(R.id.professorName);
        major = (TextView) findViewById(R.id.professorMajor);
        lab = (TextView) findViewById(R.id.professorLab);
        tel = (TextView) findViewById(R.id.professorTel);
        mail = (TextView) findViewById(R.id.professorMail);
    }

    public void setName(String name) {
        this.name.setText(name);
    }
    public void setMajor(String major) {
        this.major.setText(major);
    }
    public void setLab(String lab) {
        this.lab.setText(lab);
    }
    public void setTel(String tel) {
        this.tel.setText(tel);
    }
    public void setMail(String mail) {
        this.mail.setText(mail);
    }
}