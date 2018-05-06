package io.github.zeleven.mua.Model;

import java.io.Serializable;

/**
 * Created by tc on 7/17/16. 课表模型
 */
public class CourseModel implements Serializable {

    private int uid;         //学生 id
    private int cid;         //课程 id
    private String cname;       //课程名
    private String schoolYear;  //学年
    private String term;        //学期
    private Double credit;       //学分
    private int startSection;   //开始节次
    private int endSection;     //结束节次
    private int startWeek;      //开始周次
    private int endWeek;        //结束周次
    private int dayOfWeek;      //周几
    private String classroom;   //教室
    private String teacher;     //教师

    public CourseModel() {
    }

    public CourseModel(int uid, int cid, String cname, String schoolYear, String term, Double credit, int startSection, int endSection, int startWeek, int endWeek, int dayOfWeek, String classroom, String teacher) {
        this.uid = uid;
        this.cid = cid;
        this.cname = cname;
        this.schoolYear = schoolYear;
        this.term = term;
        this.credit = credit;
        this.startSection = startSection;
        this.endSection = endSection;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.dayOfWeek = dayOfWeek;
        this.classroom = classroom;
        this.teacher = teacher;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "课程名字："+cname+"  "+"上课地点:"+classroom+"\n"+
                "任课教师："+teacher+"  "+"课程学分："+credit+"\n"+
                "课程学年："+schoolYear+"  "+"课程学期："+term+"\n"+
                "开始周次:"+startWeek+"  "+"结束周次："+endWeek;

    }
}
