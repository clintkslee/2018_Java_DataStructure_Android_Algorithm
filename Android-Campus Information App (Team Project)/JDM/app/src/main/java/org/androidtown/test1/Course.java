package org.androidtown.test1;

public class Course {
    private String name;
    private String kind;
    private String code;
    private String teacher;
    private String forwho;
    private String time;
    private String when;
    private String spin1;
    private String spin2;

    public Course() {}
    public Course(String name, String kind, String code, String teacher, String forwho, String time, String when, String spin1, String spin2) {
        this.name=name;
        this.kind=kind;
        this.code=code;
        this.teacher=teacher;
        this.forwho=forwho;
        this.time=time;
        this.when=when;
        this.spin1=spin1;
        this.spin2=spin2;
    }

    public String getName() {
        return name;
    }
    public String getKind() {
        return kind;
    }
    public String getCode() {
        return code;
    }
    public String getTeacher() {
        return teacher;
    }
    public String getForwho() {
        return forwho;
    }
    public String getTime() {
        return time;
    }
    public String getWhen() {
        return when;
    }
    public String getSpin1() {
        return spin1;
    }
    public String getSpin2() {
        return spin2;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public void setForwho(String forwho) {
        this.forwho = forwho;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setWhen(String when) {
        this.when = when;
    }
    public void setSpin1(String spin1) {
        this.spin1 = spin1;
    }
    public void setSpin2(String spin2) {
        this.spin2 = spin2;
    }
}

