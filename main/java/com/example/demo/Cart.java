package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cart")
public class Cart {
	@Id
	  private Integer purchase_id;
	  private Integer cart_id;
	  private Integer subject_id;
	  private String cart_flag;

	  public Integer getPurchase_id() {
			return purchase_id;
		}
		public void setPurchase_id(Integer purchase_id) {
			this.purchase_id = purchase_id;
		}

		public Integer getCart_id() {
			return cart_id;
		}
		public void setCart_id(Integer cart_id) {
			this.cart_id = cart_id;
		}

		public Integer getSubject_id() {
			return subject_id;
		}
		public void setSubject_id(Integer subject_id) {
			this.subject_id = subject_id;
		}

		public String getCart_flag() {
			return cart_flag;
		}
		public void setPurchase_day(String cart_flag) {
			this.cart_flag = cart_flag;
		}
}