package com.zjut.tushuliulang.tushuliulang.net.question.question;

/**
 * Created by Administrator on 2015/9/3 0003.
 */
public class RespondAndStudentID {

    private String respond;
    private String studentID;
    private String questionID;
    private String date;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRespond() {
        return respond;
    }

    public void setRespond(String respond) {
        this.respond = respond;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    @Override
    public String toString() {
        return "RespondAndStudentID{" +
                "respond='" + respond + '\'' +
                ", studentID='" + studentID + '\'' +
                ", questionID='" + questionID + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
