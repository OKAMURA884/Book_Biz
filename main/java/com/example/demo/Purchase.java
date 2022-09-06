package com.example.demo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="purchase")
public class Purchase{
  @Id
  private Integer purchase_id;
  private Integer user_id;
  private Integer subject_id;
  private String purchase_day;
  private Integer purchase_totalPrice;
  
public Integer getPurchase_id() {
	return purchase_id;
}
public void setPurchase_id(Integer purchase_id) {
	this.purchase_id = purchase_id;
}
public Integer getUser_id() {
	return user_id;
}
public void setUser_id(Integer user_id) {
	this.user_id = user_id;
}
public Integer getSubject_id() {
	return subject_id;
}
public void setSubject_id(Integer subject_id) {
	this.subject_id = subject_id;
}
public String getPurchase_day() {
	return purchase_day;
}
public void setPurchase_day(String purchase_day) {
	this.purchase_day = purchase_day;
}
public Integer getPurchase_totalPrice() {
	return purchase_totalPrice;
}
public void setPurchase_totalPrice(Integer purchase_totalPrice) {
	this.purchase_totalPrice = purchase_totalPrice;
}
  
  
}