package com.example.demo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="subject")
public class Subject{
  @Id
  private Integer subject_id;
  private Integer user_id;
  private String subject_name;
  private String subject_isbn;
  private String subject_code;
  private String subject_state;
  private String subject_note;
  private String subject_price;
  private String subject_author;
  private String subject_registerDay;
  private String subject_flag;
  private String subject_image;
public Integer getSubject_id() {
	return subject_id;
}
public void setSubject_id(Integer subject_id) {
	this.subject_id = subject_id;
}
public Integer getUser_id() {
	return user_id;
}
public void setUser_id(Integer user_id) {
	this.user_id = user_id;
}
public String getSubject_name() {
	return subject_name;
}
public void setSubject_name(String subject_name) {
	this.subject_name = subject_name;
}
public String getSubject_isbn() {
	return subject_isbn;
}
public void setSubject_isbn(String subject_isbn) {
	this.subject_isbn = subject_isbn;
}
public String getSubject_code() {
	return subject_code;
}
public void setSubject_code(String subject_code) {
	this.subject_code = subject_code;
}
public String getSubject_state() {
	return subject_state;
}
public void setSubject_state(String subject_state) {
	this.subject_state = subject_state;
}
public String getSubject_note() {
	return subject_note;
}
public void setSubject_note(String subject_note) {
	this.subject_note = subject_note;
}
public String getSubject_price() {
	return subject_price;
}
public void setSubject_price(String subject_price) {
	this.subject_price = subject_price;
}
public String getSubject_author() {
	return subject_author;
}
public void setSubject_author(String subject_author) {
	this.subject_author = subject_author;
}
public String getSubject_registerDay() {
	return subject_registerDay;
}
public void setSubject_registerDay(String subject_registerDay) {
	this.subject_registerDay = subject_registerDay;
}
public String getSubject_flag() {
	return subject_flag;
}
public void setSubject_flag(String subject_flag) {
	this.subject_flag = subject_flag;
}
public String getSubject_image() {
	return subject_image;
}
public void setSubject_image(String subject_image) {
	this.subject_image = subject_image;
}
  
  
}

/*在庫ID	subject_id	int
登録ユーザーID	user_id	int
教科書名	subject_name	varchar(20)
ISBN	subject_isbn	varchar(15)
分類	subject_code	varchar(2)
状態	subject_state	char(1)
備考	subject_note	varchar(255)
販売価格	subject_price	varchar(10)
著者	subject_author	varchar(20)
登録日	subject_registerDay	date
在庫フラグ	subject_flag	char(1)
画像	subject_image	varchar(16000)*/