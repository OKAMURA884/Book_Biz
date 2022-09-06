package com.example.demo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User{
  @Id
  private Integer user_id;
  private String user_name;
  private String user_pass;
  private String user_mailAddress;
  private String user_phoneNumber;
  private String user_birthday;
  private String user_post;
  private String user_address;
  private String user_admin;
  private String user_retireFlag;
  private String user_registerDay;
  private Integer user_point;
  private String user_pointDay;
public Integer getUser_id() {
	return user_id;
}
public void setUser_id(Integer user_id) {
	this.user_id = user_id;
}
public String getUser_name() {
	return user_name;
}
public void setUser_name(String user_name) {
	this.user_name = user_name;
}
public String getUser_pass() {
	return user_pass;
}
public void setUser_pass(String user_pass) {
	this.user_pass = user_pass;
}
public String getUser_mailAddress() {
	return user_mailAddress;
}
public void setUser_mailAddress(String user_mailAddress) {
	this.user_mailAddress = user_mailAddress;
}
public String getUser_phoneNumber() {
	return user_phoneNumber;
}
public void setUser_phoneNumber(String user_phoneNumber) {
	this.user_phoneNumber = user_phoneNumber;
}
public String getUser_birthday() {
	return user_birthday;
}
public void setUser_birthday(String user_birthday) {
	this.user_birthday = user_birthday;
}
public String getUser_post() {
	return user_post;
}
public void setUser_post(String user_post) {
	this.user_post = user_post;
}
public String getUser_address() {
	return user_address;
}
public void setUser_address(String user_address) {
	this.user_address = user_address;
}
public String getUser_admin() {
	return user_admin;
}
public void setUser_admin(String user_admin) {
	this.user_admin = user_admin;
}
public String getUser_retireFlag() {
	return user_retireFlag;
}
public void setUser_retireFlag(String user_retireFlag) {
	this.user_retireFlag = user_retireFlag;
}
public String getUser_registerDay() {
	return user_registerDay;
}
public void setUser_registerDay(String user_registerDay) {
	this.user_registerDay = user_registerDay;
}
public Integer getUser_point() {
	return user_point;
}
public void setUser_point(Integer user_point) {
	this.user_point = user_point;
}
public String getUser_pointDay() {
	return user_pointDay;
}
public void setUser_pointDay(String user_pointDay) {
	this.user_pointDay = user_pointDay;
}

  
}

/*ユーザーID	user_id	int	yes
氏名	user_name	varchar(20)	yes
パスワード	user_pass	varchar(20)	yes
メールアドレス	user_mailAddress	varchar(50)	yes
電話番号	user_phoneNumber	varchar(20)	yes
生年月日	user_birthday	char(10)	yes
郵便番号	user_post	char(7)	yes
住所	user_address	varchar(50)	yes
権限	user_admin	char(1)	yes
退会フラグ	user_retireFlag	char(1)	yes
登録日	user_registerDay	date	yes*/