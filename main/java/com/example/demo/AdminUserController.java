package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class AdminUserController{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	// 変数に代入
	private UserRepository usersRepository;

	//一旦仮のもの   LoginControllerに正しい処理記述後消す 管理者ログイン後 会員一覧表示
	@RequestMapping(value = "/admin")
	public  String admin(Model model) {
		List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user");
		model.addAttribute("userList", users);
		return "admin-user-management";
	}


	//変更ボタン押下 -> 会員情報変更画面へ   /末岡
	@RequestMapping(value = "/admin_update_input" ,method = RequestMethod.POST)
	public ModelAndView admin_update_insert(@RequestParam("user_id") int user_id,@RequestParam("user_name") String user_name,@RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday,@RequestParam("user_post") String user_post,
			@RequestParam("user_address") String user_address,@RequestParam("user_retireFlag") String user_retireFlag,@RequestParam("user_retireDay") String user_retireDay,@RequestParam("user_admin") String user_admin,ModelAndView m) {

		m.addObject("user_id", user_id);
		m.addObject("user_retireDay", user_retireDay);
		m.addObject("user_name",user_name);
		m.addObject("user_pass", user_pass);
		m.addObject("user_mailAddress", user_mailAddress);
		m.addObject("user_phoneNumber", user_phoneNumber);
		m.addObject("user_birthday", user_birthday);
		m.addObject("user_post",user_post);
		m.addObject("user_address",user_address);
		m.addObject("user_retireFlag",user_retireFlag);
		m.addObject("user_admin",user_admin);
		m.setViewName("admin-user-update-input");
		return m;

	}


	//入力し、確認ボタン押下 -> 確認画面へ   /末岡
	@RequestMapping(value = "/admin_update_confirm" ,method = RequestMethod.POST)
	public ModelAndView admin_update_confirm(@RequestParam("user_id") int user_id,@RequestParam("user_name") String user_name,@RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday,@RequestParam("user_post") String user_post,
			@RequestParam("user_address") String user_address,@RequestParam("user_retireFlag") String user_retireFlag,@RequestParam("user_retireDay") String user_retireDay,@RequestParam("user_admin") String user_admin,ModelAndView m) {

		m.addObject("user_id", user_id);
		m.addObject("user_retireDay", user_retireDay);
		m.addObject("user_name",user_name);
		m.addObject("user_pass", user_pass);
		m.addObject("user_mailAddress", user_mailAddress);
		m.addObject("user_phoneNumber", user_phoneNumber);
		m.addObject("user_birthday", user_birthday);
		m.addObject("user_post",user_post);
		m.addObject("user_address",user_address);
		m.addObject("user_retireFlag",user_retireFlag);
		m.addObject("user_admin",user_admin);
		m.setViewName("admin-user-update-confirm");
		return m;

	}

	//変更ボタン押下 -> DBに更新手続き   /末岡
	@RequestMapping(value = "/admin_update", method = RequestMethod.POST)
	public String admin_update(@RequestParam("user_id") int user_id,@RequestParam("user_name") String user_name,@RequestParam("user_pass") String user_pass,@RequestParam("user_mailAddress") String user_mailAddress,@RequestParam("user_phoneNumber") String user_phoneNumber, @RequestParam("user_birthday") String user_birthday,@RequestParam("user_post") String user_post,
			@RequestParam("user_address") String user_address,@RequestParam("user_retireFlag") String user_retireFlag,@RequestParam("user_retireDay") String user_retireDay,@RequestParam("user_admin") String user_admin,Model model) {

		 //現在日時の取得
	     Date now = new Date();
	     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	     String time = sdf1.format(now);


	    //退会なら退会日を現在日に設定、利用中に変更なら退会日nullに
	     if(user_retireFlag.equals("1")) {
			user_retireDay = "'" + time + "'";
		}else{
			user_retireDay=null;
		}

		user_name = "'" + user_name + "'";
		user_pass = "'" + user_pass + "'";
		user_mailAddress = "'" + user_mailAddress + "'";
		user_phoneNumber = "'" + user_phoneNumber + "'";
		user_birthday = "'" + user_birthday + "'";
		user_post = "'" + user_post + "'";
		user_address = "'" + user_address + "'";
		user_retireFlag = "'" + user_retireFlag + "'";
		user_admin = "'" + user_admin + "'";
		//user_retireDay = "'" + user_retireDay + "'";


		//DB更新
		jdbcTemplate.update("update user set user_name = " + user_name + " , user_pass = " + user_pass + ", user_mailAddress = " + user_mailAddress + ", user_phoneNumber = " + user_phoneNumber + ", user_birthday = " + user_birthday +", user_post = " + user_post + " , user_address = " + user_address + " , user_retireFlag = " + user_retireFlag + " , user_retireDay = " + user_retireDay + " , user_admin = " + user_admin + " where user_id = " + user_id);

		//会員一覧画面へ戻る
		List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user");
		model.addAttribute("userList", users);
		model.addAttribute("comMsg","変更完了しました");
		return "admin-user-management";
	}

	//ログアウト   /末岡
	@RequestMapping("/logout")
	public String logout() {
		return "login";
	}


	//会員名入力検索   /末岡
	@RequestMapping("/admin_user_search")
	public String subjectSearch(
			@RequestParam(name="user_name") String user_name, Model model) {

		List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user where user_name like ?",
				new Object[] {"%" + user_name + "%"});

		model.addAttribute("userList", users);
		return "admin-user-management";
	}

	//会員全件検索   /末岡
	@RequestMapping("/admin_user_all_search")
	public String all(Model model) {
		List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user");
		model.addAttribute("userList", users);
		return "admin-user-management";
	}
}
	//管理者の会員削除申請　岡村作成
//		@PostMapping("/admin_user_delete")
//		public String subject_delete(Model model, @RequestParam("user_id") int user_id) throws Exception {
//
//     		jdbcTemplate.update("delete from message where message_senderId = " + user_id);
//     		jdbcTemplate.update("delete from message where message_receiverId = " + user_id);
//     		jdbcTemplate.update("delete from purchase where user_id = " + user_id);
//     		jdbcTemplate.update("delete from cart where user_id = " + user_id);
//			jdbcTemplate.update("delete from subject where user_id = " + user_id);
//			jdbcTemplate.update("delete from user where user_id = " + user_id);
//			
//		
//			// 書籍情報を取得
//			List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user");
//			model.addAttribute("userList", users);
//
//			return "admin-user-management";
//		}
