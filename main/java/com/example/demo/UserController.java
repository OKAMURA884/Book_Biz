package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController{
	
	@Autowired
	  private JdbcTemplate jdbcTemplate;

	
 // このURLにアクセスした際の動作
 @RequestMapping("/add") 
 public String index(Model model){
	 
   return "entry-input";
 }
 
 //会員情報登録確認
 @RequestMapping(value = "/insert-confirm", method = RequestMethod.POST)
 public String confirm(Locale locale, Model model, @RequestParam("user_name") String user_name,@RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday, @RequestParam("user_post") String user_post, @RequestParam("user_address") String user_address) {
	  model.addAttribute("user_name", user_name);
	  model.addAttribute("user_pass", user_pass);
	  model.addAttribute("user_mailAddress", user_mailAddress);
	  model.addAttribute("user_phoneNumber", user_phoneNumber);
	  model.addAttribute("user_birthday", user_birthday);
	  model.addAttribute("user_post", user_post);
	  model.addAttribute("user_address", user_address);
			
	// 情報を取得
	  user_mailAddress = "'" + user_mailAddress + "'";
  	List<Map<String, Object>> Mail = jdbcTemplate.queryForList("select user_pass from user where user_mailAddress = " +  user_mailAddress);
  	 if(Mail.size() != 0) {
			   model.addAttribute("errorMsg", "このメールアドレスはすでに使用されています");
			   return "entry-input";
		   }else {
			   return "entry-confirm";
		   }
    
 }
 
 //会員情報登録確定
 @RequestMapping(value = "/insert-decision", method = RequestMethod.POST)
 public String decision(Locale locale, Model model, @RequestParam("user_name") String user_name, @RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday, @RequestParam("user_post") String user_post, @RequestParam("user_address") String user_address) {
	//現在日時の取得
     Date now = new Date();
     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
     String time = sdf1.format(now);
	 String user_admin = "0";
	 String user_retireFlag = "0";
	 String user_registerDay = time;
	 String user_retireDay = null;
	  String[] data = {user_name,user_pass,user_mailAddress,user_phoneNumber,user_birthday,user_post,user_address,user_admin,user_retireFlag,user_registerDay,user_retireDay};

			   jdbcTemplate.update("insert into user (user_name, user_pass, user_mailAddress, user_phoneNumber, user_birthday, user_post, user_address, user_admin, user_retireFlag, user_registerDay, user_retireDay) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10]);
				
				  user_mailAddress = "'" + user_mailAddress + "'";
				  
			        // 情報を取得
			         	List<Map<String, Object>> list = jdbcTemplate.queryForList("select user_id, user_name from user where user_mailAddress =  " + user_mailAddress);
			            model.addAttribute("userlist", list); 
			            return "entry-done";
	 
	  
     
 }
 
//岡村作成　マイページの会員登録情報ボタン押下時、会員情報変更画面変更へ
	@RequestMapping(value = "/user-update-input", method = RequestMethod.POST)
	public ModelAndView input(@RequestParam("user_id") int user_id,@RequestParam("user_name") String user_name,@RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday,@RequestParam("user_post") String user_post,
			@RequestParam("user_address") String user_address,ModelAndView m) {
    	
		m.addObject("user_id", user_id);
		m.addObject("user_name",user_name);
		m.addObject("user_pass", user_pass);
		m.addObject("user_mailAddress", user_mailAddress);
		m.addObject("user_phoneNumber", user_phoneNumber);
		m.addObject("user_birthday", user_birthday);
		m.addObject("user_post",user_post);
		m.addObject("user_address",user_address);
		m.setViewName("user-update-input");
		return m;

	}
	
	//岡村作成　会員情報変更画面変更から会員情報確認画面へ
	@RequestMapping(value = "/user-update-confirm", method = RequestMethod.POST)
	public ModelAndView confirm(@RequestParam("user_id") int user_id,@RequestParam("user_name") String user_name,@RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday,@RequestParam("user_post") String user_post,
			@RequestParam("user_address") String user_address,ModelAndView m) {
		m.addObject("user_id", user_id);
		m.addObject("user_name",user_name);
		m.addObject("user_pass", user_pass);
		m.addObject("user_mailAddress", user_mailAddress);
		m.addObject("user_phoneNumber", user_phoneNumber);
		m.addObject("user_birthday", user_birthday);
		m.addObject("user_post",user_post);
		m.addObject("user_address",user_address);
		m.setViewName("user-update-confirm");
		return m;

	}
	
	//岡村作成　会員情報確認画面からDBに更新手続き
		@RequestMapping(value = "/update", method = RequestMethod.POST)
		public String update(@RequestParam("user_id") int user_id,@RequestParam("user_name") String user_name,@RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday,@RequestParam("user_post") String user_post,
				@RequestParam("user_address") String user_address,Model model) {
						  
			 jdbcTemplate.update("update user set user_name = ?, user_pass = ?, user_mailAddress = ?, user_phoneNumber = ?, user_birthday = ?, user_post = ? , user_address = ? where user_id = " + user_id,user_name , user_pass , user_mailAddress , user_phoneNumber , user_birthday , user_post , user_address);
			 user_mailAddress = "'" + user_mailAddress + "'";
		    	List<Map<String, Object>> user_info = jdbcTemplate.queryForList("select * from user where user_mailAddress = " +  user_mailAddress);
			  	  
		            	  model.addAttribute("user_info", user_info);
		            	  model.addAttribute("user_id", user_id);
		            	  return "user-mypage";
		}
	/*	//岡村作成 会員情報の退会手続き
		@RequestMapping(value = "/delete", method = RequestMethod.POST)
		public String delete(@RequestParam("user_id") int user_id, Model model) {
			
			jdbcTemplate.update("delete from user where user_id = " + user_id);
			
			return "login";
			
		}　 */
		//田口作成 会員情報の退会手続き（退会フラグを１に変更する ログイン画面ではフラグが１はログイン失敗にする
				@RequestMapping(value = "/delete", method = RequestMethod.POST)
				public String delete(@RequestParam("user_id") int user_id, Model model) {
					
					//現在日時の取得
				     Date now = new Date();
				     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				     String time = sdf1.format(now);
					 String user_retireDay = time;
					 
					 jdbcTemplate.update("update user set user_retireFlag = '1' , user_retireDay = '" + user_retireDay + "' where user_id = " + user_id);
						//退会時、ポイント0になる処理
						jdbcTemplate.update("update user set user_point = 0 where user_id = " + user_id);
					
						
					return "login";
					
				}
				//岡村作成　マイページ画面からログアウト処理
				@RequestMapping(value = "/user-logout", method = RequestMethod.GET)
				   public String logout(@RequestParam("user_id") String user_id,Model model) {
					jdbcTemplate.update("update cart set cart_flag = 0 where user_id = " + user_id);
					   return "login";
				   }
				
}