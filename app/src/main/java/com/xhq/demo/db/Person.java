package com.xhq.demo.db;

public class Person {
	public String userName ="";
	public String sUserName ="";
	public String nickName ="";
	public String noteName ="";
	public String headName ="";
	public String pinyin="";
	public String place="" ;
	public String signatureString="";
    public int type= 0;
    public Person(){}
	public Person(String userName,String sUserName,String nickName) {
		this.userName = userName;
		this.sUserName = sUserName;
		this.nickName = nickName;
	}

}
