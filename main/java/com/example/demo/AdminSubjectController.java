package com.example.demo;

import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminSubjectController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	//岡村作成 管理者の教科書件検索初期画面の操作
	@RequestMapping("/subject")
	public String index(ImageForm imageForm,Model model) {
		List<Map<String, Object>> subjects = jdbcTemplate.queryForList("select * from subject ");
		model.addAttribute("itemlist", subjects);
		return "admin-subject-management";
	}
	//管理者の教科書画面のキーワード検索subject_code
	@RequestMapping("subject-search")
	public String subjectSearch(
			@RequestParam(name="subject_name") String search_word, 
			@RequestParam(name="subject_code") String subject_code,
			@RequestParam(name="subject_state") String subject_state,
			Model model) {
		String word = "";

		List<Map<String, Object>> author = jdbcTemplate.queryForList("select * from subject where subject_author like ? ",
				new Object[] {"%" + search_word + "%"});
		List<Map<String, Object>> name = jdbcTemplate.queryForList("select * from subject where subject_name like ? ",
				new Object[] {"%" + search_word + "%"});
		List<Map<String, Object>> isbn = jdbcTemplate.queryForList("select * from subject where subject_isbn like ? ",
				new Object[] {"%" + search_word + "%"});
		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject ");

		if(author.size() != 0) {
			word = "subject_author";
		}else if(isbn.size() != 0) {
			word = "subject_isbn";
		}else if(name.size() != 0) {
			word = "subject_name";
		}else {
			word = "subject_name";
		}
		//すべて表示
		if (subject_code.equals("すべて") && subject_state.equals("すべて") && search_word.length() == 0) {
			items = jdbcTemplate.queryForList("select * from subject ");
			model.addAttribute("itemlist",items);
		} else if (subject_code.equals("すべて") && subject_state.equals("すべて")) {
			items = jdbcTemplate.queryForList("select * from subject where " + word + " like ? ",
					new Object[] {"%" + search_word + "%"});
			model.addAttribute("itemlist",items);

		}
		else if (subject_code.equals("すべて") && search_word.length() != 0) {
			items = jdbcTemplate.queryForList("select * from subject where subject_state = ? and " + word + " like ? ",
					new Object[] {subject_state , "%" + search_word + "%"});
			model.addAttribute("itemlist",items);
		}
		else if (subject_code.equals("すべて") && search_word.length() == 0) {
			items = jdbcTemplate.queryForList("select * from subject where subject_state = ? ",
					new Object[] {subject_state });
			model.addAttribute("itemlist",items);
		}
		else if (subject_state.equals("すべて") && search_word.length() != 0) {
			items = jdbcTemplate.queryForList("select * from subject where subject_code = ? and " + word + " like ? ",
					new Object[] {subject_code , "%" + search_word + "%"});
			model.addAttribute("itemlist",items);
		}
		else if (subject_state.equals("すべて") && search_word.length() == 0) {
			items = jdbcTemplate.queryForList("select * from subject where subject_code = ? ",
					new Object[] {subject_code });
			model.addAttribute("itemlist",items);
		}
		else if (search_word.length() != 0) {
			items = jdbcTemplate.queryForList("select * from subject where subject_code = ? and subject_state = ? and " + word + " like ? ",
					new Object[] {subject_code , subject_state , "%" + search_word + "%"});
			model.addAttribute("itemlist",items);
		}
		else if (search_word.length() == 0) {
			items = jdbcTemplate.queryForList("select * from subject where subject_code = ? and subject_state = ? ",
					new Object[] {subject_code , subject_state});
			model.addAttribute("itemlist",items);
		}

		if(items.size() == 0) {
			model.addAttribute("errorMsg", "該当するデータがありません");
		}

		return "admin-subject-management";
	}
	////管理者の教科書画面から会員画面へ
	@RequestMapping(value = "/user-search", method = RequestMethod.GET)
	public String userSearch(Model model) {
		List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from user");
		model.addAttribute("userList", users);
		return "admin-user-management";

	}
	//管理者の教科書画面のログアウト処理
	@RequestMapping(value = "/admin-subject-logout", method = RequestMethod.GET)
	public String logout(Model model) {
		
		return "login";
	}

	//教科書検索画面から教科書変更画面へ
	@RequestMapping(value = "/subject-update", method = RequestMethod.POST)
	public String subjectUpdate(ImageForm imageForm,@RequestParam("subject_id") int subject_id,@RequestParam("user_id") int user_id,@RequestParam("subject_isbn") String subject_isbn,
			@RequestParam("subject_name") String subject_name,@RequestParam("subject_author") String subject_author,@RequestParam("subject_code") String subject_code,
			@RequestParam("subject_state") String subject_state,@RequestParam("subject_price") String subject_price,@RequestParam("subject_note") String subject_note,
			@RequestParam("subject_image") String subject_image,Model model) {

		model.addAttribute("subject_image",subject_image);
		model.addAttribute("user_id", user_id);
		model.addAttribute("subject_id",subject_id);
		model.addAttribute("subject_name",subject_name);
		model.addAttribute("subject_isbn",subject_isbn);
		model.addAttribute("subject_author",subject_author);
		model.addAttribute("subject_code",subject_code);
		model.addAttribute("subject_state",subject_state);
		model.addAttribute("subject_price",subject_price);
		model.addAttribute("subject_note",subject_note);

		return "admin-subject-update-input";
	}
	//管理者の教科書変更申請
	@RequestMapping(value = "/admin-subject-update", method = RequestMethod.POST)
	public String update(ImageForm imageForm,@RequestParam("subject_id") int subject_id,@RequestParam("user_id") int user_id,
			@RequestParam("subject_name") String subject_name,@RequestParam("subject_isbn") String subject_isbn,@RequestParam("subject_author") String subject_author,@RequestParam("subject_code") String subject_code,
			@RequestParam("subject_state") String subject_state,@RequestParam("subject_price") String subject_price,@RequestParam("subject_note") String subject_note, @RequestParam("subject_image") String subject_image,Model model) throws Exception {

		
		  System.out.println(imageForm.getImage().getSize());
	        StringBuffer data = new StringBuffer();
		 String base64 = new String(Base64.encodeBase64(imageForm.getImage().getBytes()),"ASCII");
	        data.append("data:image/jpeg;base64,");
	        data.append(base64);
	        String data2 = "data:image/jpeg;base64,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";    
	        if(subject_image.equals("data:image/jpeg;base64,")) {
	        	subject_image = data2;
	        }else if(data.toString().length() > 50) {
	        	subject_image = data.toString();
	        }

		subject_name = "'" + subject_name + "'";
		subject_isbn = "'" + subject_isbn + "'";
		subject_code = "'" + subject_code + "'";
		subject_state = "'" + subject_state + "'";
		subject_author = "'" + subject_author + "'";
		subject_price = "'" + subject_price + "'";
		subject_note = "'" + subject_note + "'";
		subject_image = "'" + subject_image + "'";

		jdbcTemplate.update("update subject set subject_name = " + subject_name + " ,subject_note = " + subject_note + ", subject_isbn = " + subject_isbn + ", subject_code = " + subject_code + ", subject_state = " + subject_state + ", subject_author = " + subject_author +", subject_price = " + subject_price + " , subject_image = " + subject_image +" where subject_id = " + subject_id);
		//		jdbcTemplate.update("update subject set subject_name = ?,subject_image = ?, subject_isbn = ?, subject_author = ?, subject_code = ?, subject_state = ?, subject_price = ?, subject_note = ? where subject_id = " + subject_id, subject_name, subject_isbn, subject_author, subject_code, subject_state, subject_price, subject_note, subject_image);
		List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where user_id = " + user_id);
		model.addAttribute("itemlist", items);
		model.addAttribute("user_id", user_id);
		return "admin-subject-management";
	}
	//管理者の教科書削除申請
	@PostMapping("/subject-delete")
	public String subject_delete(Model model, @RequestParam("subject_id") int subject_id,  @RequestParam("user_id") int user_id) throws Exception {

		// 情報を取得
		jdbcTemplate.update("delete from message where subject_id = " + subject_id);
		jdbcTemplate.update("delete from purchase where subject_id = " + subject_id);
		jdbcTemplate.update("delete from cart where subject_id = " + subject_id);
		jdbcTemplate.update("delete from subject where subject_id = " + subject_id);

		// 書籍情報を取得
		List<Map<String, Object>> subjects = jdbcTemplate.queryForList("select * from subject where user_id = " + user_id);
		model.addAttribute("itemlist", subjects);

		return "admin-subject-management";
	}
}