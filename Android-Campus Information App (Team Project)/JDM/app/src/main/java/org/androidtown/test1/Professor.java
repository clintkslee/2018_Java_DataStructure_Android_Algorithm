package org.androidtown.test1;

public class Professor {
    private String name;
    private String college;
    private String major;
    private String lab;
    private String tel;
    private String mail;

    public Professor() {}
    public Professor(String name, String college, String major, String lab, String tel, String mail) {
        this.college=college;
        this.lab=lab;
        this.mail=mail;
        this.major=major;
        this.name=name;
        this.tel=tel;
    }

    public String getCollege() {
        return college;
    }
    public String getLab() {
        return lab;
    }
    public String getMail() {
        return mail;
    }
    public String getMajor() {
        return major;
    }
    public String getName() {
        return name;
    }
    public String getTel() {
        return tel;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public void setLab(String lab) {
        this.lab = lab;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public void setName(String name) {
        this.name = name;
    }
}