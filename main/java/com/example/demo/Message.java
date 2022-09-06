package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message")
public class Message {
	  @Id
	  private Integer message_id;
	  private Integer subject_id;
	  private String message_content;
	  private Integer message_senderId;
	  private Integer message_receiverId;
	  private String message_date;
	  private String message_readFlag;

	  public Integer getMessage_id() {
		  return message_id;
	  }
	  public void setMessage_id(Integer message_id) {
		  this.message_id = message_id;
	  }
	  public Integer getSubject_id() {
		  return subject_id;
	  }
	  public void setSubject_id(Integer subject_id) {
		  this.subject_id = subject_id;
	  }
	  public String getMessage_content() {
		  return message_content;
	  }
	  public void setMessage_content(String message_content) {
		  this.message_content = message_content;
	  }
	  public Integer getMessage_senderId() {
		  return message_senderId;
	  }
	  public void setMessage_senderId(Integer message_senderId) {
		  this.message_senderId = message_senderId;
	  }
	  public Integer getMessage_receiverId() {
		  return message_receiverId;
	  }
	  public void setMessage_receiverId(Integer message_receiverId) {
		  this.message_receiverId = message_receiverId;
	  }
	  public String getMessage_date() {
		  return message_date;
	  }
	  public void setMessage_date(String message_date) {
		  this.message_date = message_date;
	  }
	  public String getMessage_readFlag() {
		  return message_readFlag;
	  }
	  public void setMessage_readFlag(String message_readFlag) {
		  this.message_readFlag = message_readFlag;
	  }


}