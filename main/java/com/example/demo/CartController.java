package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class CartController {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	// 変数に代入
	private SubjectRepository usersRepository;

	@ModelAttribute
	public ImageForm setForm() {
		return new ImageForm();
	}

	//カートに入れるボタン押下->購入(カート)画面へ
	@PostMapping("/cart-in")
	public String a(ImageForm imageForm, Model model,@RequestParam("user_id") String user_id,@RequestParam("user_name") String user_name,@RequestParam("subject_id") String subject_id,@RequestParam("subject_user_id") String subject_user_id,@RequestParam("my") String my) throws Exception {

		//未読フラグの情報をヘッダーに引き継ぐ
		String no_read = "1";
		List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
		//指定した文字列が存在するか確認
		if (msg.toString().contains(no_read))
		{
			model.addAttribute("unread_msg", "未読メッセージがあります");
		}

		//sunject_id=0のものなら
		//カートの中身がない場合
				/*List<Map<String, Object>> subject_id_zero = jdbcTemplate.queryForList("select subject_id from subject where user_id  = " + user_id +" and subject_flag = 1");
				if(subject_id_zero.size() == 0 ) {
					int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
					model.addAttribute("user_point", user_point);
					model.addAttribute("empty_cart_message", "カートは空です。");
					model.addAttribute("double_cart_message", "ただいま追加した商品はすでに購入されていました。");
					model.addAttribute("notbuy_cart_message", "すでに購入された商品はカートに入れることが出来ません。");
					return "subject-buy";

				}*/
		model.addAttribute("subject_user_id", subject_user_id); //出品者のuser_id subject.user_idと同じ
		model.addAttribute("user_id", user_id); //購入者user_id cart.user_idと同じ
		model.addAttribute("user_name", user_name);

		int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
		model.addAttribute("user_point", user_point);
		//自分の出品した商品でなければ
		if(my == null || my.length() == 0) {
			List<Map<String, Object>> sub_id = jdbcTemplate.queryForList("select cart_id from cart where user_id  = " + user_id +" and cart_flag = '1' and subject_id = " + subject_id);
			//すでにカートに追加した商品でなければ
			if(sub_id.size() <= 0 ) {

				//cartテーブルの中のcart_flag="1"でuser_id=user_user_id(購入者id)のときのcart_id
				List<Map<String, Object>> cart = jdbcTemplate.queryForList("select cart_id from cart where user_id  = " + user_id +" and cart_flag = '1' ");
				//userが5冊以上カートに入れていなければ
				if(cart.size() < 5) {
					//cartテーブルに登録
					jdbcTemplate.update("insert into cart (user_id,subject_id,cart_flag) VALUES (?, ?, ?)", user_id, subject_id, "1");
				}else {
					String error_message = "カートの中には5冊までしか入れられません。";
					model.addAttribute("error_message", error_message);
					String error_message2 = "先ほどの商品はカートに追加されていません。";
					model.addAttribute("error_message2", error_message2);
				}
			}else {
				model.addAttribute("NotBuy", "すでにカートにある商品です");
				List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);
				model.addAttribute("itemlist", items);

				if(subject_user_id == user_id || user_id.equals(subject_user_id)) {
					model.addAttribute("my", "あなたが出品した商品です");

				}
				model.addAttribute("user_id", user_id);
				model.addAttribute("user_name", user_name);
				model.addAttribute("subject_user_id", subject_user_id);

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
			}

		}else {
			model.addAttribute("NotBuy", "あなたが出品した商品をカートに入れることはできません");
			List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id = " + subject_id);
			model.addAttribute("itemlist", items);

			if(subject_user_id == user_id || user_id.equals(subject_user_id)) {
				model.addAttribute("my", "あなたが出品した商品です");

			}
			model.addAttribute("user_id", user_id);
			model.addAttribute("user_name", user_name);
			model.addAttribute("subject_user_id", subject_user_id);

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
		}

		//cartとsubjectのsubject_idが同じ+在庫有subject_flag="1"+cart_falg="1"のとき表示
		String cart_flag="1";
		//subjectテーブルとcartテーブルのsubject_idを結合
		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag+" and subject.subject_flag = '1' ");
		model.addAttribute("itemlist", items);

		//合計金額計算
		List<Map<String,Object>> subject_price_all = jdbcTemplate.queryForList("select sum(subject_price) from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag+" and subject.subject_flag = '1' ");
		//listから取り出し  price_all={sum(subject_price)=2000.0}
		Map<String, Object> price_all = subject_price_all.get(0);
		//商品値段を数値に price="2000.0"
		Object price = price_all.get("sum(subject_price)");
		//object->float->int
		if(price != null) {
		float fValue = Float.valueOf(price.toString());
		int value = (int)fValue;
		model.addAttribute("price", value);
		}else {
			model.addAttribute("price", 0);
		}
		return "subject-buy";
	}


	//削除ボタン押下->購入(カート)画面へ cartテーブルのcart_flagを0に cart_idをhiddenから受け取りそのcart_idのcart_flagを0に更新
	@PostMapping("/cart_delete")
	public String c( Model model,@RequestParam("cart_id") String cart_id,@RequestParam("subject_user_id") String subject_user_id,@RequestParam("user_id") String user_id,@RequestParam("user_name") String user_name,@RequestParam("subject_id") String subject_id) throws Exception {

		//未読フラグの情報をヘッダーに引き継ぐ
		String no_read = "1";
		List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
		//指定した文字列が存在するか確認
		if (msg.toString().contains(no_read))
		{
			model.addAttribute("unread_msg", "未読メッセージがあります");
		}

		int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
		model.addAttribute("user_point", user_point);
		//cart_flag="0"に変更
		jdbcTemplate.update("update cart set cart_flag = " + 0 +" where cart_id = " + cart_id);

		//カート画面に戻る
		model.addAttribute("user_id", user_id); //出品者のuser_id subject.user_idと同じ
		model.addAttribute("user_user_id", subject_user_id); //購入者user_id cart.user_idと同じ
		model.addAttribute("user_name", user_name);
		//カートの中身がある場合
		String cart_flag="1";
		List<Map<String, Object>> cart_id2 = jdbcTemplate.queryForList("select cart_id from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
		if(cart_id2.size() == 0 ) {
			model.addAttribute("empty_cart_message", "カートは空です。");
			model.addAttribute("price", 0);
			return "subject-buy";
		}

		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag);
		model.addAttribute("itemlist", items);


		//合計金額計算
		List<Map<String,Object>> subject_price_all = jdbcTemplate.queryForList("select sum(subject_price) from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag + " and subject.subject_flag = 1");
		//listから取り出し
		Map<String, Object> price_all = subject_price_all.get(0);
		Object price = price_all.get("sum(subject_price)");
		//カートが空でpriceがnullではないとき
		if(price != null) {
			float fValue = Float.valueOf(price.toString());
			int value = (int)fValue;

			model.addAttribute("price", value);
		}else {
			model.addAttribute("price", 0);
		}
		return "subject-buy";
	}


	//岡村さん ポイント処理の記述
	//購入ボタン押下->購入確認画面へ ポイントの処理も
	//購入金額からポイント引く＋在庫ない商品があればカート画面に戻る＋
	@PostMapping("/cart-buy-confirm")
	public String d( Model model,@RequestParam("user_id") String user_id,@RequestParam("user_name") String user_name,@RequestParam("price") String price,@RequestParam("point") String point) throws Exception {

		//未読フラグの情報をヘッダーに引き継ぐ
		String no_read = "1";
		List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
		//指定した文字列が存在するか確認
		if (msg.toString().contains(no_read))
		{
			model.addAttribute("unread_msg", "未読メッセージがあります");
		}


		String cart_flag="1";

		//カートにすでに購入された商品が含まれている場合
		List<Map<String, Object>> a = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag+" and subject.subject_flag = 0");
		if(a.size() != 0 ) {
			//すでに購入された商品のcartflag0に
			jdbcTemplate.update("update cart join subject on cart.subject_id = subject.subject_id set cart_flag = 0 where cart.user_id  = " + user_id +" and subject.subject_flag = 0");

			model.addAttribute("double_cart_message", "カートの中の商品にすでに購入された商品が含まれていました。");
			model.addAttribute("notbuy_cart_message", "すでに購入された商品はカートから削除されています。");
			model.addAttribute("user_id", user_id);
			int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
			model.addAttribute("user_point",user_point);
			List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag + " and subject.subject_flag = 1");
			model.addAttribute("itemlist", items);
			model.addAttribute("user_name", user_name);
			model.addAttribute("point",point);
			//合計金額計算
			List<Map<String,Object>> subject_price_all = jdbcTemplate.queryForList("select sum(subject_price) from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
			//listから取り出し
			Map<String, Object> price_all = subject_price_all.get(0);
			Object price1 = price_all.get("sum(subject_price)");
			if(price1 != null) {
				float fValue = Float.valueOf(price1.toString());
				int value = (int)fValue;
				model.addAttribute("price", value);
			}else {
				model.addAttribute("price", 0);
			}
			return "subject-buy";
		}


		//カートの中身がない場合
		List<Map<String, Object>> cart_id = jdbcTemplate.queryForList("select cart_id from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
		if(cart_id.size() == 0 ) {
			model.addAttribute("empty_cart_message", "カートが空なので購入できません。");
			model.addAttribute("price", 0);
			//			List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag);
			//			model.addAttribute("itemlist", items);
			model.addAttribute("user_id", user_id);
			int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
			model.addAttribute("user_point",user_point);
			return "subject-buy";
		}

		//-----------ポイント処理------------

		if(point != "0" && point !="") {
			int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
			if(Integer.parseInt(point) > user_point) {
				model.addAttribute("errorMsg", "上限ポイントを超えています");
				List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag + " and subject.subject_flag = 1");
				model.addAttribute("itemlist", items);
				model.addAttribute("price", price);
				model.addAttribute("user_id", user_id);
				model.addAttribute("user_point",user_point);
				return "subject-buy";
			}
			else if(Integer.parseInt(point)%100 != 0) {
				model.addAttribute("errorMsg", "100ポイント単位で入力してください");
				List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag + " and subject.subject_flag = 1");
				model.addAttribute("itemlist", items);
				model.addAttribute("price", price);
				model.addAttribute("user_id", user_id);
				model.addAttribute("user_point",user_point);
				return "subject-buy";
			}
		}

		if(point != "0" && point !="") {
			List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag + " and subject.subject_flag = 1");
			model.addAttribute("itemlist", items);
			model.addAttribute("user_id", user_id);
			model.addAttribute("user_name", user_name);
			int price1 = Integer.parseInt(price);
			int price_point =  price1 - Integer.parseInt(point) ;
			model.addAttribute("price_all", price_point);
			model.addAttribute("point",point);

			return "buy-confirm";

		} else {
			List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag + " and subject.subject_flag = 1");
			model.addAttribute("itemlist", items);
			model.addAttribute("user_id", user_id);
			model.addAttribute("user_name", user_name);
			int price1 = Integer.parseInt(price);
			model.addAttribute("price_all", price1);
			model.addAttribute("point",point);

			return "buy-confirm";
		}
	}




	//確定ボタン押下->DBに登録更新->top画面へ
	@PostMapping("/cart-buy")
	public String cart_buy( Model model,@RequestParam("user_id") String user_id,@RequestParam("user_name") String user_name,@RequestParam("price_all") String price_all,@RequestParam("point") String pointed) throws Exception {

		//未読フラグの情報をヘッダーに引き継ぐ
		String no_read = "1";
		List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
		//指定した文字列が存在するか確認
		if (msg.toString().contains(no_read))
		{
			model.addAttribute("unread_msg", "未読メッセージがあります");
		}

		//在庫ない商品があればカート画面に戻る
		//カートテーブルのsubject_id(user_id=購入者、cart_flag=1)取得
		//そのsubject_idをリストにいれて、それを利用してsubjectテーブルと照合し、subject_flag=1のsubject_flagの数を調べる
		//subject_flag=1のsubject_flagがリストの数よりも少なければ購入処理は中止

		//カートテーブルのsubject_id(user_id=購入者、cart_flag=1)取得
		List<Map<String, Object>> s_id = jdbcTemplate.queryForList("select cart.subject_id from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = 1");
		//カートテーブルのsubject_id(user_id=購入者、cart_flag=1、subject_flag=1)取得
		List<Map<String, Object>> s_id2 = jdbcTemplate.queryForList("select cart.subject_id from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = 1 and subject.subject_flag = 1");

		if(s_id.size() == s_id2.size()) {

			//現在日時の取得
			Date now = new Date();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			String time = sdf1.format(now);

			//cartテーブルとsubjectテーブルを結合させてuser_idからcartに登録されたsubject_idを取ってくる
			String cart_flag="1";

			List<Map<String, Object>> sub_id = jdbcTemplate.queryForList("select cart.subject_id from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag);

			//その1～5つの教科書の在庫フラグを0にする処理
			String subject_flag = "0";
			subject_flag = "'" + subject_flag + "'";

			List<Integer> subject_id_list = new ArrayList<>();
			//subject_idをintにしてループでlistに追加
			for(int i = 0;i<sub_id.size();i++) {
				Map<String, Object> subj_id = sub_id.get(i);
				Object subject_id = subj_id.get("subject_id");
				if(subject_id != null) {
					float fValue = Float.valueOf(subject_id.toString());
					int value = (int)fValue;
					//listにsubject_idが入る
					subject_id_list.add(value);
				}
			}

			for(int i=0; i < subject_id_list.size(); i++) {
				//subject在庫フラグ0に
				jdbcTemplate.update("update subject set subject_flag = " + subject_flag + ", subuject_saleDate = '" + time + "' where subject_id = " + subject_id_list.get(i));
				//purcahseテーブル登録
				jdbcTemplate.update("insert into purchase (user_id, subject_id, purchase_day, purchase_totalPrice) VALUES (?, ?, ?, ?)", user_id,subject_id_list.get(i), time, price_all);
			}
			//cartflag0に
			jdbcTemplate.update("update cart set cart_flag = "+ 0 +" where  user_id = "+ user_id);

			//ポイント処理 岡村さん
			//ポイント付与＋使用分マイナス
			if(pointed != null) {
				int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
				int less_point = user_point - Integer.parseInt(pointed);
				jdbcTemplate.update("update user set user_point = ? where user_id = " + user_id, less_point);
			}

			int point =  (int)(Integer.parseInt(price_all) * 0.05);
			if(point >= 1) {
				int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
				int total_point = point + user_point;
				jdbcTemplate.update("update user set user_point = ?, user_pointDay = ? where user_id = " + user_id, total_point,time);
				//top画面へ戻る
				List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_flag = '1'");
				model.addAttribute("itemlist", items);
				model.addAttribute("user_id", user_id);
				model.addAttribute("user_name", user_name);
			} else {

				List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_flag = '1'");
				model.addAttribute("itemlist", items);
				model.addAttribute("user_id", user_id);
				model.addAttribute("user_name", user_name);

			}
			return "user-top";
			//カートの商品が他の人に購入されていた場合
		}else {

			//すでに購入された商品のcartflag0に
			jdbcTemplate.update("update cart join subject on cart.subject_id = subject.subject_id set cart_flag = 0 where cart.user_id  = " + user_id +" and subject.subject_flag = 0");

			int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);
			model.addAttribute("user_point", user_point);
			model.addAttribute("user_id", user_id); //購入者user_id cart.user_idと同じ
			model.addAttribute("user_name", user_name);
			String cart_flag="1";
			//カートの中身がある場合
			List<Map<String, Object>> cart_id = jdbcTemplate.queryForList("select cart_id from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
			if(cart_id.size() > 0 ) {

				//cartとsubjectのsubject_idが同じ+在庫有subject_flag="1"+cart_falg="1"のとき表示
				//subjectテーブルとcartテーブルのsubject_idを結合
				List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
				model.addAttribute("itemlist", items);

				//合計金額計算
				List<Map<String,Object>> subject_price_all2 = jdbcTemplate.queryForList("select sum(subject_price) from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag + " and subject.subject_flag = 1");
				//listから取り出し
				Map<String, Object> price_all2 = subject_price_all2.get(0);
				Object price = price_all2.get("sum(subject_price)");
				float fValue = Float.valueOf(price.toString());
				int value = (int)fValue;
				model.addAttribute("price", value);
				model.addAttribute("double_cart_message", "カートの中の商品にすでに購入された商品が含まれていました。");
				model.addAttribute("notbuy_cart_message", "すでに購入された商品はカートから削除されています。");
			}
			else {
				model.addAttribute("empty_cart_message", "カートは空です。");
				model.addAttribute("double_cart_message", "カートの中の商品にすでに購入された商品が含まれていました。");
				model.addAttribute("notbuy_cart_message", "すでに購入された商品はカートから削除されています。");
				return "subject-buy";
			}
			return "subject-buy";
		}
	}


	//カートの中を見るボタン押下
	@PostMapping("/cart-confirm")
	public String e(ImageForm imageForm, Model model,@RequestParam("user_name") String user_name,@RequestParam("user_id") String user_id) throws Exception {
		int user_point = jdbcTemplate.queryForObject("select user_point from user where user_id = " + user_id,Integer.class);

		//未読フラグの情報をヘッダーに引き継ぐ
		String no_read = "1";
		List<Map<String, Object>> msg = jdbcTemplate.queryForList("select message_readFlag from message where message_receiverId = " + user_id);
		//指定した文字列が存在するか確認
		if (msg.toString().contains(no_read))
		{
			model.addAttribute("unread_msg", "未読メッセージがあります");
		}

		model.addAttribute("user_point", user_point);
		model.addAttribute("user_id", user_id); //購入者user_id cart.user_idと同じ
		model.addAttribute("user_name", user_name);
		String cart_flag="1";
		//カートの中身がある場合
		List<Map<String, Object>> cart_id = jdbcTemplate.queryForList("select cart_id from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
		if(cart_id.size() > 0 ) {

			//cartとsubjectのsubject_idが同じ+在庫有subject_flag="1"+cart_falg="1"のとき表示
			//subjectテーブルとcartテーブルのsubject_idを結合
			List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
			model.addAttribute("itemlist", items);


			//合計金額計算
			List<Map<String,Object>> subject_price_all = jdbcTemplate.queryForList("select sum(subject_price) from subject join cart on subject.subject_id = cart.subject_id  where cart.user_id  = " + user_id +" and cart.cart_flag = "+ cart_flag+" and subject.subject_flag = 1");
			//listから取り出し
			Map<String, Object> price_all = subject_price_all.get(0);
			Object price = price_all.get("sum(subject_price)");
			float fValue = Float.valueOf(price.toString());
			int value = (int)fValue;
			model.addAttribute("price", value);
		}
		//カートの中身がない場合
		else {
			model.addAttribute("empty_cart_message", "カートは空です。");
			model.addAttribute("price", 0);
			return "subject-buy";
		}
		return "subject-buy";
	}
	//topへボタン押下
	@PostMapping("/f")
	public String f(ImageForm imageForm, Model model,@RequestParam("user_name") String user_name,@RequestParam("user_id") String user_id) throws Exception {

		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_flag = '1'");
		model.addAttribute("itemlist", items);
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_name", user_name);
		return "user-top";
	}

}