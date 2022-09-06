//管理者側のログイン処理は78行目～となります　田口

package com.example.demo;

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
public class PurchaseController{
	
	 @Autowired
	  private JdbcTemplate jdbcTemplate;
	 
	  @ModelAttribute
	    public ImageForm setForm() {
	        return new ImageForm();
	    }
	  
	//購入履歴
	    @PostMapping("/BuyShow")
	    public String BuyShow(ImageForm imageForm, Model model, @RequestParam("user_id") String user_id) throws Exception {
	    	
	    	// マイページに戻るための情報を取得
         	List<Map<String, Object>> list = jdbcTemplate.queryForList("select user_id, user_name from user where user_id =  " + user_id);
            model.addAttribute("userlist", list);
            
	    	 // 書籍情報を取得
	    	List<Map<String, Object>> buy_items = jdbcTemplate.queryForList("select * from purchase where user_id = " + user_id);
	    	//List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where user_id = " + user_id);
	    	List<Map<String, Object>> buy_id = jdbcTemplate.queryForList("select subject_id from purchase where user_id = " + user_id);
	    	model.addAttribute("buy_items", buy_items);
	    	
	    	 //購入済みの商品はあるのかどうか
	    	String buy_count = buy_id.toString();
	    	 
	    	 if(buy_count.equals("[]")) {
	    		 model.addAttribute("error_message", "該当するデータはありません。");
	            }else {
	            	String Buy_Id = buy_id.toString(); 
	   	    	 Buy_Id = Buy_Id.replace("subject_id", ""); 
	   	    	 Buy_Id = Buy_Id.replace("[{", ""); 
	   	    	 Buy_Id = Buy_Id.replace("}]", ""); 
	   	    	 Buy_Id = Buy_Id.replace("}", ""); 
	   	    	 Buy_Id = Buy_Id.replace("{", "");
	   	    	 Buy_Id = Buy_Id.replace("=", "");
	   	         model.addAttribute("buy_id", Buy_Id);
	   	         
	   	         List<Map<String, Object>> items = jdbcTemplate.queryForList("select * from subject where subject_id in(" + Buy_Id + ")");
	   	         model.addAttribute("itemlist", items);
	   	         
	   	    	 //合計金額を算出する
	   	    	 List<Map<String, Object>> sum_price = jdbcTemplate.queryForList("select sum(purchase_totalPrice) from purchase where user_id = " + user_id);
	   	    	 String sum;
	   	    	 sum = sum_price.toString();
	   	    	 sum = sum.substring(27, sum.length()-2);
	   	    	 model.addAttribute("sum", sum);
	   	    	 model.addAttribute("user_id", user_id);
	            }
	    	 
	    	 
	    	 return "buy-info";
	    }//田口
	    

}    