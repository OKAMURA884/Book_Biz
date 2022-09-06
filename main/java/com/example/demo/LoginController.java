//管理者側のログイン処理は78行目～となります　田口

package com.example.demo;



import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	// 変数に代入
	private UserRepository usersRepository;

	@ModelAttribute
	public ImageForm setForm() {
		return new ImageForm();
	}

	@RequestMapping("/login")
	public String first() {
		return "login";
	}


	@RequestMapping(value = "/login_check", method = RequestMethod.POST)
	public String home(Locale locale, Model model, @RequestParam("mail") String mail, @RequestParam("password") String password)  {

		mail = "'" + mail + "'";
		// 情報を取得
		List<Map<String, Object>> lists = jdbcTemplate.queryForList("select user_pass from user where user_mailAddress = " +  mail);
		List<Map<String, Object>> user_info = jdbcTemplate.queryForList("select * from user where user_mailAddress = " +  mail);
		List<Map<String, Object>> userretireFlag = jdbcTemplate.queryForList("select user_retireFlag from user where user_mailAddress = " +  mail);

		//小金 user_adminとuser_retireFlag取得
		List<Map<String, Object>> admin_flag = jdbcTemplate.queryForList("select user_admin from user where user_mailAddress = " +  mail);
		//List<Map<String, Object>> retire_flag= jdbcTemplate.queryForList("select user_retireFlag from user where user_mailAddress = " +  mail);



		

		if(lists.toString().equals("[]")) {
			model.addAttribute("no", "ログイン失敗");
		}else {
			String Pass;
			Pass = lists.toString();
			Pass = Pass.substring(12, Pass.length()-2);
			String Flag = userretireFlag.toString();
			Flag = Flag.substring(18, Flag.length()-2);

			/*	//小金 user_adminとuser_retireDayの加工
			  	    	 String Flag = admin_flag.toString();
			  	    	 Flag = Flag.substring(13,Flag.length()-2);

			  	    	 String Retire = retire_flag.toString();
			  	    	 Retire = Retire.substring(18, Retire.length()-2); */

			//小金 user_adminとuser_retireDayの加工 変数名を一部修正　田口
			String Admin_Flag = admin_flag.toString();
			Admin_Flag = Admin_Flag.substring(13,Admin_Flag.length()-2);

			//String Retire = retire_flag.toString();
			//Retire = Retire.substring(18, Retire.length()-2); 

			//現在の時刻取得
			Date now = new Date();

			Date user_date = jdbcTemplate.queryForObject("select user_pointDay from user where user_mailAddress = " + mail, Date.class);
			if (user_date != null) {
				long diff = now.getTime() - user_date.getTime();
				TimeUnit time = TimeUnit.DAYS; 
				long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
				//付与日から1年過ぎたら無効になる処理
				if(diffrence > 365 ) {
					jdbcTemplate.update("update user set user_point = 0 where user_mailAddress = "  + mail);
					List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user");
					model.addAttribute("userList", users);
				}
			}
			
			//ユーザーのマイページへ
			if( Pass.equals(password) && Flag.equals("0") && Admin_Flag.equals("0")) {
				model.addAttribute("user_info", user_info);
				//名前とIDを取得
				List<Map<String, Object>> user_id = jdbcTemplate.queryForList("select user_id from user where user_mailAddress = " +  mail);
				List<Map<String, Object>> user_name = jdbcTemplate.queryForList("select user_name from user where user_mailAddress = " +  mail);
				
				String User_id = user_id.toString();
				User_id = User_id.substring(10, User_id.length()-2);
				model.addAttribute("user_id", User_id);
			
				String User_name = user_name.toString();
				User_name = User_name.substring(12, User_name.length()-2);

				model.addAttribute("user_name", User_name);
				return "user-mypage";
				//管理者のマイページへ
			}else if(Pass.equals(password) && Flag.equals("0") && Admin_Flag.equals("1")){
				//returnには管理者コントローラーを。
				List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user");
				model.addAttribute("userList", users);
				return "admin-user-management";
			} else {
				model.addAttribute("no", "ログイン失敗");

			}
		}


		return "login";

		/* //小金
	  	    	 if( Pass.equals(password) && Flag.equals("1")) {
	  	    		 return "admin-user-management";
	  	    	 }else if(Pass.equals(password) && Retire.equals("0")) {
	  	    		 model.addAttribute("user_info", user_info);
	  	    		 return "user-mypage";
	  	    	 }else {
	  	    		 model.addAttribute("no", "ログイン失敗");
	  	    	 }
	  	    	 return "login"; */

	}
	//ユーザーのマイページに画面遷移
	@RequestMapping(value = "/user-mypage", method = RequestMethod.POST)
	public String mypage(Locale locale, Model model, @RequestParam("user_id") String user_id) {

		user_id = "'" + user_id + "'";
		List<Map<String, Object>> user_info = jdbcTemplate.queryForList("select * from user where user_id = " +  user_id);
		
		//名前を取得
		List<Map<String, Object>> user_name = jdbcTemplate.queryForList("select user_name from user where user_id = " +  user_id);
		
		String User_name = user_name.toString();
		User_name = User_name.substring(12, User_name.length()-2);

		 model.addAttribute("user_id", user_id);
		model.addAttribute("user_name", User_name);
		model.addAttribute("user_info", user_info);
		return "user-mypage";
	}

	/*動作確認用  //管理者のマイページに画面遷移
	    @RequestMapping(value = "admin-mypage", method = RequestMethod.POST)
	    public String admin(Locale locale, Model model, @RequestParam("user_id") String user_id) {

	    	user_id = "'" + user_id + "'";
	    	List<Map<String, Object>> user_info = jdbcTemplate.queryForList("select * from user where user_id = " +  user_id);

	            	  model.addAttribute("user_info", user_info);
	            	  return "admin-mypage";
	    } */

}