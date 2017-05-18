package com.oklink.api.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oklink.api.OKLink;
import com.oklink.api.OKLinkBuilder;
import com.oklink.api.util.SignTool;

public class Example {
	
	
	private static final String KEY = "";  //apikey	
	private static final String SECRET = "";//secretkey

	public static void main(String[] args) throws Exception {
		OKLink oklink = OKLinkBuilder.getInstance().setHost("https://www.oklink.com")
                .build(KEY, SECRET);
		  String result = null;
		 //#common api
		 //result = oklink.getTicker("EXRATE");
		 //result = oklink.getCountryList("delivery");  //
		 //result = oklink.getRemittanceLimit("40","2");
	      
		 //#remitance api 
		 //Step 1 get detail format 
		 result = oklink.getDetailInfo("78","2");
		  
		 //Step 2 build detail info
		 Map<String,Object> detail_info = new HashMap<String,Object>();
		 detail_info.put("bank_id", 3325);
		 detail_info.put("version", 0);
		detail_info.put("bank_acc_number","3100");
		detail_info.put("branch_bank_name", "fdasfsd");
		
		Map<String,Object> beneficiary = new HashMap<String,Object>();
		                 beneficiary.put("full_name", "jimmy");
		                beneficiary.put("mobile_number", "86,186000001");
		                beneficiary.put("email", "b@test.com");
		              
	     detail_info.put("beneficiary",beneficiary);
	     Map<String,Object> remitter = new HashMap<String,Object>();
	                    remitter.put("full_name", "John");
	                    remitter.put("mobile_number", "85,15001137489");
	                    remitter.put("email", "a@test.com");
	                     
	      detail_info.put("remitter", remitter);
		  
		 //Step3
	 //  result = oklink.createRemit(129,1,2,100,0,1,JSON.toJSONString(detail_info),true) ;  //269
		 //result = oklink.getRemitInfo("DLBpc4y3LgnEhH",1);
		 //result = oklink.getRemitList(2, 1, 10, 5);
		
		 //#pay
         //result = oklink.getPayInfo("D3uU3cPA5d7Cws");
         //JSONObject json = JSON.parseObject(result);
		 //JSONObject payInfo = json.getJSONObject("pay_info");
		 //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "123!e", payInfo.getString("salt"));
		 //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("pay_hex"),SignTool.COIN_TOKEN, privateKeyWIF,"", true);
		 //result = oklink.toPay("D3uU3cPA5d7Cws", hex);
		
		 //#accept
		 //result = oklink.accept("D3uU3cPA5d7Cws");
		  
         //result = oklink.getRejectInfo("FNEF81zo6xym7s");
		 //JSONObject json = JSON.parseObject(result);
		 //JSONObject payInfo = json.getJSONObject("reject_info");
		 //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "1qazxsw@", payInfo.getString("salt"));
		 //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("reject_hex"),SignTool.COIN_TOKEN, privateKeyWIF,payInfo.getString("redeem_script"), true);
		 //result = oklink.reject(1081, hex,"this is no good");
		 
		 //#refund 
		 //result = oklink.getReFundInfo("D3uU3cPA5d7Cws");
		 //JSONObject json = JSON.parseObject(result);
		 //JSONObject payInfo = json.getJSONObject("refund_info");
		 //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "123!e", payInfo.getString("salt"));
		 //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("refund_hex"),SignTool.COIN_TOKEN, privateKeyWIF,payInfo.getString("redeem_script"), true);
		 //result = oklink.refund("D3uU3cPA5d7Cws", hex);
		  
		 //#receive
         //result = oklink.getReciveInfo(845);
         //JSONObject json = JSON.parseObject(result);
         //JSONObject payInfo = json.getJSONObject("receive_info");
         //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "a1!", payInfo.getString("salt"));
         //String redeem_script = payInfo.getString("redeem_script");
         //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("receive_hex"),SignTool.COIN_TOKEN, privateKeyWIF,
         //  redeem_script, true);
         //result = oklink.appealReceive(845, 651287, hex);
		
		 //#user account api
		 //result = oklink.getUserInfo();
		 System.out.println(result);
		
	}
	
}
