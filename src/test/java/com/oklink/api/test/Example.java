package com.oklink.api.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oklink.api.OKLink;
import com.oklink.api.OKLinkBuilder;
import com.oklink.api.util.SignTool;

public class Example {
	
	
	private static final String KEY = "";  //apikey	
	private static final String SECRET = "";//secretke

	public static void main(String[] args) throws Exception {
		OKLink oklink = OKLinkBuilder.getInstance().setHost("https://www.oklink.com")
                .build(KEY, SECRET);
		  String result = null;
		 //#common api
		
		 //result = oklink.getTicker("EXRATE");
		 //result = oklink.getCountryList();
		 
		 //#setting api
		 //result = oklink.setDeliveryStatus(1);
		  
		 //#remitance api 
		 //result = oklink.getBankInfo(143);
	     //result = oklink.createRemit(143,1,2,0,3,"zhangsan1","86,15001137489","","lisi1","86,15001137489","","53@11122222333",true) ;  //269
		 //result = oklink.getRemitInfo(845,1);
		 //result = oklink.getRemitList(2, 1, 10, 5);
		
		 //#pay
         //result = oklink.getPayInfo(475);
         //result = oklink.getPayShopInfo(143);
         //JSONObject json = JSON.parseObject(result);
         //JSONObject payInfo = json.getJSONObject("pay_info");
         //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "aaa111!!!", payInfo.getString("salt"));
         //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("pay_hex"),SignTool.COIN_TOKEN, privateKeyWIF,"", true);
         //result = oklink.toPay(475, hex);
		
		 //#accept
		 //result = oklink.accept(845);
		  
         //result = oklink.getRejectInfo(1081);
		 //JSONObject json = JSON.parseObject(result);
		 //JSONObject payInfo = json.getJSONObject("reject_info");
		 //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "1qazxsw@", payInfo.getString("salt"));
		 //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("reject_hex"),SignTool.COIN_TOKEN, privateKeyWIF,payInfo.getString("redeem_script"), true);
		 //result = oklink.reject(1081, hex,"this is no good");
		 
		 
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
