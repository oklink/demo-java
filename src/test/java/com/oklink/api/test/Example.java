package com.oklink.api.test;

import com.oklink.api.OKLink;
import com.oklink.api.OKLinkBuilder;

public class Example {
	
	
	private static final String KEY = "";  //apikey
	private static final String SECRET = "";//secretkey

	public static void main(String[] args) throws Exception {
		OKLink oklink = OKLinkBuilder.getInstance().setHost("https://www.oklink.com")
                .build(KEY, SECRET);
		  String result = null;
		 // #common api
		
		 //result = oklink.getTicker("BTC");
		 //result = oklink.getCountryList();
		 
		 
		 //#setting api
		 //result = oklink.setRevenue("1","101", "1", "-1");
		 //result = oklink.setBusinessStatus(1);
		 //result = oklink.setLimitAmount(60000);
		 //result = oklink.setPayMode(3); //1 cash 2 banktransfer 3 all
		 //result = oklink.setTransferNetWork(6.30, 1, 1,0);
		 //result = oklink.setReceiveBtcAddress("mv4Vdhf741JRiYToaYXGkF8SptxtTLdyoo");
		  
		  
		 //#remitance api 
	     //result = oklink.createRemit(89,1,1,0,153,"lisi","86,15001137489","yuliang","86,15001137489","") ;  //269
		 //result = oklink.getRemitInfo(267);
		 //result = oklink.getRemitList(2, 1, 10, 5);
		
		 //#pay
         //result = oklink.getPayInfo(269);
         //JSONObject json = JSON.parseObject(result);
         //JSONObject payInfo = json.getJSONObject("pay_info");
         //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "123!e", payInfo.getString("salt"));
         //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("pay_hex"),SignTool.COIN_TOKEN, privateKeyWIF,"", true);
         //result = oklink.toPay(269, hex);
		
		 //#accept
		 //result = oklink.accept(338);
		
		 //#receive
		  
		 //result = oklink.uploadImg(337, "D:\\image003(06-03-18-39-30).png");
		 //result = oklink.getReciveInfo(269);
		 //JSONObject json = JSON.parseObject(result);
         //JSONObject payInfo = json.getJSONObject("receive_info");
         //String privateKeyWIF = SignTool.decodePrivateKeyWIF(payInfo.getString("aes256key"), "1q!", payInfo.getString("salt"));
         //String redeem_script = payInfo.getString("redeem_script");
         //String hex = SignTool.verifyAndSignTransaction(payInfo.getString("receive_hex"),SignTool.COIN_TOKEN, privateKeyWIF,
         //		redeem_script, true);
         //result = oklink.appealReceive(338, 651287, hex);
		
		 //#user account api
		 //result = oklink.getUserInfo();
		 System.out.println(result);
		
	}
	
}
