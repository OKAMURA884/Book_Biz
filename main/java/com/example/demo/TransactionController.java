package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransactionController{
	
	 @Autowired
	  private JdbcTemplate jdbcTemplate;
 @Autowired
 // 変数に代入
 private SubjectRepository usersRepository;
 
	  @ModelAttribute
	    public ImageForm setForm() {
	        return new ImageForm();
	    }
	    
	    //ログイン済み 商品詳細の通り道
	    @PostMapping("/user-subject-info")
	    public String itemInfoMove(ImageForm imageForm, Model model,@RequestParam("subject_id") String subject_id) throws Exception {
	    		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);
		        model.addAttribute("itemlist", items);
		        
	    	 return "user-subjsect-info";
	    }//田口
	    
	  //ログインしていない 商品詳細の通り道
	    @PostMapping("/no-login-user-subject-info")
	    public String NoitemInfoMove(ImageForm imageForm, Model model,@RequestParam("subject_id") String subject_id) throws Exception {
	    		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);
		        model.addAttribute("itemlist", items);
		        
		        //出品者の名前
		        List<Map<String, Object>> seller = jdbcTemplate.queryForList("select user_id from subject where subject_id = " + subject_id);
		        String Seller = seller.toString();
		        Seller = Seller.substring(10, Seller.length()-2);
		        model.addAttribute("seller", Seller);
		        List<Map<String, Object>> seller_name = jdbcTemplate.queryForList("select user_name from user where user_id = " + Seller);
		        String Seller_Name = seller_name.toString();
		        Seller_Name = Seller_Name.substring(12, Seller_Name.length()-2);
		        model.addAttribute("seller", Seller_Name);
	    	 return "no-login-user-subject-info";
	    }//田口
	    
	    //商品詳細
	    @PostMapping("/user-subject-info-login-finish")
	    public String loginfinish(ImageForm imageForm, Model model,@RequestParam("user_id") String user_id,@RequestParam("user_name") String user_name,@RequestParam("subject_id") String subject_id,@RequestParam("subject_user_id") String subject_user_id) throws Exception {

	    	//未読フラグの情報をヘッダーに引き継ぐ
	    	String no_read = "1";
	    	List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
	    	//指定した文字列が存在するか確認
	        if (msg.toString().contains(no_read))
	        {
	        	model.addAttribute("unread_msg", "未読メッセージがあります");
	        }
	        
    		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);

		        model.addAttribute("itemlist", items);
		        List<Map<String, Object>> user_point = jdbcTemplate.queryForList("select user_point from user where user_id = " + user_id);
		        model.addAttribute("user", user_point);
		        if(subject_user_id == user_id || subject_user_id.equals(user_id)) {
		        	 model.addAttribute("my", "あなたが出品した商品です");
		        	 
		        }
		        model.addAttribute("user_id", user_id);
		        model.addAttribute("user_name", user_name);
		        
		        //出品者の名前
		        List<Map<String, Object>> seller = jdbcTemplate.queryForList("select user_id from subject where subject_id = " + subject_id);
		        String Seller = seller.toString();
		        Seller = Seller.substring(10, Seller.length()-2);
		        model.addAttribute("seller", Seller);
		        List<Map<String, Object>> seller_name = jdbcTemplate.queryForList("select user_name from user where user_id = " + Seller);
		        String Seller_Name = seller_name.toString();
		        Seller_Name = Seller_Name.substring(12, Seller_Name.length()-2);
		        model.addAttribute("seller", Seller_Name);
	    	 return "user-subject-info";
	    }//田口
	    
	  //購入確認
	    @PostMapping("/BuyConfirm")
	    public String BuyConfirm(ImageForm imageForm, Model model,@RequestParam("subject_id") String subject_id,@RequestParam("user_id") String user_id,@RequestParam("user_name") String user_name,@RequestParam("my") String my,@RequestParam("user_user_id") String user_user_id,@RequestParam("point-use") String point) throws Exception {
	    	int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_user_id,Integer.class);
	    	if(point != "0" && point !="") {
	    		if(Integer.parseInt(point) > user_point) {
	    			model.addAttribute("errorMsg", "上限ポイントを超えています");
	    			return "user-subject-info";
	    		}
	    		else if(Integer.parseInt(point)%100 != 0) {
	    			model.addAttribute("errorMsg", "100ポイント単位で入力してください");
	    			return "user-subject-info";
	    		} 
	    	}
	    	//myは自分が出品したかどうかのエラーメッセージ。エラーメッセージの値がなければ自分自身が出品した商品ではないので購入処理に移る
	    	if(my == null || my.length() == 0) {
	    		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);
	    		model.addAttribute("point",point);
		        model.addAttribute("itemlist", items);
		        model.addAttribute("user_user_id", user_user_id);
		        model.addAttribute("user_name", user_name);
	    		return "buy-confirm";
	    	}else if(my != null || my.length() != 0){
	    		model.addAttribute("NotBuy", "この商品を買うことはできません");
	    		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_flag = '1'");
		        model.addAttribute("itemlist", items);
	    		 model.addAttribute("user_id", user_user_id);
			        model.addAttribute("user_name", user_name);
	    		return "user-top";
	    	}
			return user_user_id;
	    		
		
	    }//田口

	    
	    //購入
	    @PostMapping("/BuyDone")
	    public String Buy(ImageForm imageForm, Model model,@RequestParam("subject_id") String subject_id,@RequestParam("subject_price") String subject_price,@RequestParam("user_id") String user_id,@RequestParam("my") String my,@RequestParam("user_user_id") String user_user_id,@RequestParam("point") String pointed) throws Exception {

	    	//未読フラグの情報をヘッダーに引き継ぐ
	    	String no_read = "1";
	    	List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
	    	//指定した文字列が存在するか確認
	        if (msg.toString().contains(no_read))
	        {
	        	model.addAttribute("unread_msg", "未読メッセージがあります");
	        }
	        
	    	//現在日時の取得
	        Date now = new Date();
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	        String time = sdf1.format(now);
	        
	        
		        //在庫をゼロにする
		    	String subject_flag = "0";
		        subject_flag = "'" + subject_flag + "'";
		    	jdbcTemplate.update("update subject set subject_flag = " + subject_flag + ", subuject_saleDate = '" + time + "' where subject_id = " + subject_id);
			  
		    	//購入テーブルに登録する
		    	
		    	 String purchase_totalPrice = subject_price;
		    	 String[] data = {user_user_id,subject_id,time,purchase_totalPrice};

		    	jdbcTemplate.update("insert into purchase (user_id, subject_id, purchase_day, purchase_totalPrice) VALUES (?, ?, ?, ?)", data[0], data[1], data[2], data[3]);
		    	//岡村　作成　購入時にポイントを利用した場合のポイントを引いて、残りのポイントの処理
		    	if(pointed != null) {
		    		int user_pointed = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_user_id,Integer.class);
		    		int less_point = user_pointed - Integer.parseInt(pointed);
		    		jdbcTemplate.update("update user set user_point = ? where user_id = " + user_user_id, less_point);
		    	}
		    	//岡村　作成　購入時のポイント付与と日の処理
		    	//ポイントで引いた場合」
		    	if(pointed != null) {
		    		int totalPoint= Integer.parseInt(purchase_totalPrice) - Integer.parseInt(pointed);
		    	int point =  (int)(totalPoint* 0.05);
		    	if(point >= 1) {

		    		int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_user_id,Integer.class);
		    		int total_point = point + user_point;

		    		jdbcTemplate.update("update user set user_point = ?, user_pointDay = ? where user_id = " + user_user_id, total_point,time);

		    		List<Map<String, Object>> user_info = jdbcTemplate.queryForList("select * from user where user_id = " +  user_user_id);

		            model.addAttribute("userlist", user_info);
		            model.addAttribute("user_user_id", user_user_id);

		    	 return "buy-done";
		    		}
		    	    //ポイントで引かなかった場合
		    	}

		    		int point =  (int)(Integer.parseInt(purchase_totalPrice) * 0.05);
			    	if(point >= 1) {

			    		int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_user_id,Integer.class);
			    		int total_point = point + user_point;

			    		jdbcTemplate.update("update user set user_point = ?, user_pointDay = ? where user_id = " + user_user_id, total_point,time);
			    		List<Map<String, Object>> user_info = jdbcTemplate.queryForList("select * from user where user_id = " +  user_user_id);

			            model.addAttribute("userlist", user_info);
			            model.addAttribute("user_user_id", user_user_id);

			    	 return "buy-done";

			    	 }

	    		List<Map<String, Object>> user_info = jdbcTemplate.queryForList("select * from user where user_id = " +  user_user_id);

	            model.addAttribute("userlist", user_info);
	            model.addAttribute("user_user_id", user_user_id);

	    	 return "buy-done";
		    	}
	    
	    }