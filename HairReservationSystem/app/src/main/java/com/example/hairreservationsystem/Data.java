package com.example.hairreservationsystem;

import java.util.Date;

/**
 * 데이터를 가지고 있는 싱글톤 클래스
 * 늦은 초기화를 통해 내가 필요할 때만 받아오게 할 수 있다.
 * */
public class Data{

     //순서대로 회원 아이디, 비밀번호, 이름, 전화번호
    private String CID;
    private String CPassword;
    private String CName;
    private String CPhoneNum;

    //등급, 등급정보(손님, 미용사, 사업자)
    private int Grade;
    private String disCription;

    //미용사 자격증번호, 사업장 번호
    private String CLicense;
    private String CBusinessNum;

    //미용실 이름, 전화번호, 주소, 회원 수
    private String HRName;
    private String HRCallNum;
    private String HRAddress;
    private int dresserNum;

    private Date date;

    private Data() { }
    private static Data instance;

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }


    public String getCID() {
        return CID;
    }
    public void setCID(String CID){
        this.CID = CID;
    }

    public String getCPassword(){
        return CPassword;
    }
    public void setCPassword(String CPassword){
        this.CPassword=CPassword;
    }

    public String getCName() {
        return CName;
    }
    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getCPhoneNum() {
        return CPhoneNum;
    }
    public void setCPhoneNum(String CPhoneNum) {
        this.CPhoneNum = CPhoneNum;
    }

    public int getGrade() {
        return Grade;
    }
    public void setGrade(int grade) {
        Grade = grade;
    }

    public String getDisCription() {
        return disCription;
    }
    public void setDisCription(String disCription) {
        this.disCription = disCription;
    }

    public String getCLicense() { return CLicense; }
    public void setCLicense(String CLicense) { this.CLicense = CLicense; }

    public String getCBusinessNum() { return CBusinessNum; }
    public void setCBusinessNum(String CBusinessNum) { this.CBusinessNum = CBusinessNum; }

    public String getHRName() { return HRName; }
    public void setHRName(String HRName) { this.HRName = HRName; }

    public String getHRCallNum() { return HRCallNum; }
    public void setHRCallNum(String HRCallNum) { this.HRCallNum = HRCallNum; }

    public String getHRAddress() { return HRAddress; }
    public void setHRAddress(String HRAddress) { this.HRAddress = HRAddress; }

    public int getDresserNum() { return dresserNum; }
    public void setDresserNum(int dresserNum) { this.dresserNum = dresserNum; }
}
