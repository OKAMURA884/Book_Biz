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

@Controller
public class MessageController {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	// TOP画面で「受信」押下後、message.htmlを表示
	@RequestMapping("/message-room")
	public String message_top(@RequestParam("user_id") String user_id, @RequestParam("user_name") String user_name, Model model) {



		//受信者IDが自分のものの未読フラグを「0」に変える　→既読にする
		jdbcTemplate.update("update message set message_readFlag = 0 where message_receiverId = " + user_id);

		model.addAttribute("user_id", user_id);
		model.addAttribute("user_name", user_name);

		//送信者IDが自分のものを取得
		 List<Map<String, Object>> sendList = jdbcTemplate.queryForList
				 ("select m.message_content, m.message_receiverId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_receiverId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_senderId = " + user_id);
		 model.addAttribute("sendList", sendList);

		 //受信者IDが自分のものを取得
		 List<Map<String, Object>> receiveList = jdbcTemplate.queryForList
				 ("select m.message_content, m.message_senderId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_senderId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_receiverId = " + user_id);
		 model.addAttribute("receiveList", receiveList);

		return "message";

	}



	//教科書詳細画面(user_subject_info)からのメッセージ送信
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	 public String send(ImageForm imageForm, Model model, @RequestParam("subject_id") String subject_id, @RequestParam("message_content") String message_content,
			 					@RequestParam("user_user_id") String user_user_id, @RequestParam("user_id") String user_id,@RequestParam("my") String my)  throws Exception {

		//未読フラグの情報をヘッダーに引き継ぐ
    	String no_read = "1";
    	List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_user_id);
    	//指定した文字列が存在するか確認
        if (msg.toString().contains(no_read))
        {
        	model.addAttribute("unread_msg", "未読メッセージがあります");
        }

		model.addAttribute("user_id", user_id);

		//コメント数制御
		if(message_content.length() == 0 || message_content.length() > 100 ) {
			model.addAttribute("error_Message", "メッセージは1～100字以内でお願いします。");
			List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);
			model.addAttribute("itemlist", items);

			//model.addAttribute("user_id", user_id);
			model.addAttribute("user_id", user_user_id);
			model.addAttribute("subject_id", subject_id);

			return "user-subject-info";

		}else {

		//現在日時の取得 = 送信日時
	     Date now = new Date();
	     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	     String time = sdf1.format(now);
	     String message_date = time;

	     //未読フラグ
	     String message_readFlag = "1";

	     //DB登録
	     String[] data = {subject_id, message_content, user_user_id, user_id, message_date, message_readFlag};
	     jdbcTemplate.update("insert into message (subject_id, message_content, message_senderId, message_receiverId, message_date, message_readFlag) VALUES (?, ?, ?, ?, ?, ?)", data[0], data[1], data[2], data[3], data[4], data[5]);

	     //送信者IDが自分のものを取得
	     List<Map<String, Object>> sendList = jdbcTemplate.queryForList
				 ("select m.message_content, m.message_receiverId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_receiverId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_senderId = " + user_user_id);
	     model.addAttribute("sendList", sendList);

		//受信者IDが自分のものを取得
		 List<Map<String, Object>> receiveList = jdbcTemplate.queryForList
				 ("select m.message_content, m.message_senderId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_senderId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_receiverId = " + user_user_id);
		 model.addAttribute("receiveList", receiveList);

		 //model.addAttribute("user_id", user_id);
		 //model.addAttribute("user_user_id", user_user_id);
		 model.addAttribute("user_id", user_user_id);
		 //model.addAttribute("subject_id", subject_id);

	     return "message";

		}


	}


		//メッセージ画面から送信（＝問合せに対する返信になるはず）
		 @RequestMapping(value = "/reply", method = RequestMethod.POST)
		 public String reply(@RequestParam(name = "address", defaultValue = "") String address, @RequestParam("contents") String contents,
				 					@RequestParam("user_id") String user_id,	Model model) {

			//未読フラグの情報をヘッダーに引き継ぐ
		    	String no_read = "1";
		    	List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
		    	//指定した文字列が存在するか確認
		        if (msg.toString().contains(no_read))
		        {
		        	model.addAttribute("unread_msg", "未読メッセージがあります");
		        }

			 address = "'" + address + "'";

			 if(address.equals("''")){
				model.addAttribute("error_Message", "返信できるメッセージはありません");
				//送信者IDが自分のものを取得
				List<Map<String, Object>> sendList = jdbcTemplate.queryForList
							("select m.message_content, m.message_receiverId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_receiverId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_senderId = " + user_id);
				model.addAttribute("sendList", sendList);
				//受信者IDが自分のものを取得
				List<Map<String, Object>> receiveList = jdbcTemplate.queryForList
							 ("select m.message_content, m.message_senderId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_senderId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_receiverId = " + user_id);
				model.addAttribute("receiveList", receiveList);

				model.addAttribute("user_id", user_id);

				return "message";


			//コメント数制御
			 }else if(contents.length() == 0 || contents.length() > 100 ) {
				model.addAttribute("error_Message", "メッセージは1～100字以内でお願いします");
				//送信者IDが自分のものを取得
				List<Map<String, Object>> sendList = jdbcTemplate.queryForList
						 ("select m.message_content, m.message_receiverId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_receiverId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_senderId = " + user_id);
				model.addAttribute("sendList", sendList);
				//受信者IDが自分のものを取得
				List<Map<String, Object>> receiveList = jdbcTemplate.queryForList
						 ("select m.message_content, m.message_senderId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_senderId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_receiverId = " + user_id);
				model.addAttribute("receiveList", receiveList);

				model.addAttribute("user_id", user_id);

				return "message";

			}else {


			 //返信先で選んだコメントからsubject_idを取得
			 List<Map<String, Object>> subject_id = jdbcTemplate.queryForList("select subject_id from message where message_content = " + address);
			 String Subject_id = subject_id.toString();
			 Subject_id = Subject_id.substring(13, Subject_id.length()-2);

			 //返信先で選んだコメントから返信先ユーザーのID(message_senderId)を取得
			 List<Map<String, Object>> message_senderId = jdbcTemplate.queryForList("select message_senderId from message where message_content = " + address);
			 String reply_userId = message_senderId.toString();
			 reply_userId = reply_userId.substring(19, reply_userId.length()-2);

			 //現在日時の取得 = 送信日時
		     Date now = new Date();
		     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		     String time = sdf1.format(now);
		     String message_date = time;

		     //未読フラグ
		     String message_readFlag = "1";

			 String[] data = {Subject_id, contents, user_id, reply_userId, message_date, message_readFlag};

			 //DB登録
			 jdbcTemplate.update("insert into message (subject_id, message_content, message_senderId, message_receiverId, message_date, message_readFlag) VALUES (?, ?, ?, ?, ?, ?)", data[0], data[1], data[2], data[3], data[4], data[5]);


		     // messageテーブル情報を取得（=送信履歴表示）
			 List<Map<String, Object>> sendList = jdbcTemplate.queryForList
					 ("select m.message_content, m.message_receiverId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_receiverId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_senderId = " + user_id);
			 model.addAttribute("sendList", sendList);

		     //messageテーブル情報を取得（=受信履歴表示）
			 List<Map<String, Object>> receiveList = jdbcTemplate.queryForList
					 ("select m.message_content, m.message_senderId, m.message_date, u.user_name, s.subject_name from message as m inner join user as u on m.message_senderId = u.user_id inner join subject as s on m.subject_id = s.subject_id where m.message_receiverId = " + user_id);
			 model.addAttribute("receiveList", receiveList);

			 model.addAttribute("user_id", user_id);

		     return "message";

			}


		 }



		 //商品詳細にメッセージ履歴の表示
		 @RequestMapping(value = "/message_check", method = RequestMethod.POST)
		 public String message_check(ImageForm imageForm, Model model, @RequestParam("subject_id") String subject_id,
				 								@RequestParam("user_id") String user_id,@RequestParam("user_name") String user_name,
				 								@RequestParam("subject_user_id") String subject_user_id) throws Exception  {


			//未読フラグの情報をヘッダーに引き継ぐ
		    String no_read = "1";
		    List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
		    //指定した文字列が存在するか確認
		     if (msg.toString().contains(no_read)){
		        model.addAttribute("unread_msg", "未読メッセージがあります");
		      }



			  //messageテーブル情報を取得
		     List<Map<String, Object>> msgList = jdbcTemplate.queryForList("select * from message where subject_id = " + subject_id);
		     	//model.addAttribute("msgList", msgList);

		 	String list_count = msgList.toString();
		 	//System.out.println(list_count);

		 	if(list_count.equals("[]")){
		 		model.addAttribute("errorMessage", "この商品に関するメッセージのやりとりはありません。");
		 		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);
				model.addAttribute("itemlist", items);
				model.addAttribute("user_id", user_id);
				return "user-subject-info";

		 	 }else {

		 		model.addAttribute("msgList", msgList);

		 		//メッセージ情報と送信者名取得
		 		List<Map<String, Object>> msg_items = jdbcTemplate.queryForList("select m.message_content, m.message_senderId, m.message_date, u.user_name from message as m inner join user as u on m.message_senderId = u.user_id where m.subject_id = " + subject_id);
		 		model.addAttribute("msg_items", msg_items);

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


				model.addAttribute("user_id", user_id);
				model.addAttribute("user_name", user_name);

		 		return "user-subject-info";

		 	 }


		 }





}

/*DB登録に必要なもの
メッセージID　　message_id　→自動採番
在庫ID 　subject_id
コメント内容　　message_content
送信者ID　　message_senderId　→ログインしてるユーザー
受信者ID　　message_receiverId　→出品してるユーザー
送信日時　　message_date
未読フラグ　　message_readFlag  →既読は0　未読は1
*/