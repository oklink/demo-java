package com.oklink.api;

public interface OKLink {
	
	
	public String getTicker(String symbol) throws Exception;
	public String getCountryList()throws Exception;
	public String setRevenue(int type,int min,int max,int percent_fee) throws Exception;
	public String setLimitAmount(int amount)throws Exception;
	public String setPayMode(int pay_mode)throws Exception;
	public String setTransferNetWork(double outbound_rate,int targetCurrent,int plat,int isOpne)throws Exception;
	public String setReceiveBtcAddress(String address)throws Exception;
	public String setBusinessStatus(int status)throws Exception;
	
	
	public String createRemit(int nationId,int transferNetwork,int payMode,double receiveAmount,double sendAmount,String receiveName,String receivePhone,String sendName,String senderPhone,String payBankInfo,boolean isCreateNow) throws Exception ;
	public String getRemitInfo(long remitId)throws Exception;
	
	public String getPayInfo(long remitId)throws Exception;
	public String toPay(long remitId,String hex) throws Exception;
	
	public String getRemitList(int type,int pageNo,int pagesize,int status)throws Exception;
	public String accept(long remitId)throws Exception;
	
	public String getReciveInfo(long remitId)throws Exception;
	public String appealReceive(long remitId,int pickupCode,String receiveHex)throws Exception;
	public String getUserInfo()throws Exception;
	public String uploadImg(long remitId,String imgPath)throws Exception;
	
	
}
