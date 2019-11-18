package com.example.hairreservationsystem;

/**
 * 데이터를 가지고 있는 싱글톤 클래스
 * 늦은 초기화를 통해 내가 필요할 때만 받아오게 할 수 있다.
 * */
public class Data{
    /**
     * 순서대로
     * 회원ID, 회원PW, 회원이름, 회원전화번호, 회원등급, 회원등급정보
     */
    private String CID;
    private String CPassword;
    private String CName;
    private String CPhoneNum;
    private int Grade;
    private String disCription;

    private Data() { }
    private static Data instance;

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    /**
     * 순서대로 CID, CPassword, CName, CPhoneNum, Grade getter,setter 정의
     * */
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

}
