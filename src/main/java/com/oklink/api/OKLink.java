package com.oklink.api;

public interface OKLink {
	
	
	public String getTicker(String symbol) throws Exception;
	public String getCountryList()throws Exception;
	
	public String setDeliveryStatus(int status)throws Exception;
	
	
	public String createRemit(int nationId,int transferNetwork,int payMode,
			double receiveAmount,double sendAmount,String receiveName,
			String receivePhone,String receiveEmail,String sendName,String sender_birthdate,String sender_certificate,String sender_certinum,String senderPhone,
			String senderEmail,String payBankInfo,int pay_shopid,String pay_walletid,double remitFee,boolean isCreateNow) throws Exception ;
	public String getRemitInfo(String id,int type)throws Exception;
	
	public String getPayInfo(String id)throws Exception;
	public String toPay(String id,String hex) throws Exception;
	
	public String getRemitList(int type,int pageNo,int pagesize,int status)throws Exception;
	public String accept(String id)throws Exception;
	public String getRejectInfo(String id)throws Exception;
	public String reject(String id,String hex,String reason)throws Exception;
	
	public String getReciveInfo(String id)throws Exception;
	public String appealReceive(String id,int pickupCode,String receiveHex)throws Exception;
	public String getUserInfo()throws Exception;
	public String getBankInfo(int country_id)throws Exception;
	
	public String getPayShopInfo(int country_id)throws Exception;
	
	
}
