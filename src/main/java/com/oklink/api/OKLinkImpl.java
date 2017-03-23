package com.oklink.api;

import java.util.HashMap;
import java.util.Map;

public class OKLinkImpl implements OKLink {
	
	private HttpUtil httpUtil;
	
	public OKLinkImpl(OKLinkBuilder builder) {
		this.httpUtil = new HttpUtil(builder.apiKey, builder.apiSecret, builder.host);
	}
	
	public String getTicker(String symbol) throws Exception {
		// TODO Auto-generated method stub
		String url ="/api/v2/ticker.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("symbol",symbol);  
		return httpUtil.doPOST(url,params) ;
	}
	
	@Override
	public String getCountryList() throws Exception {
		// TODO Auto-generated method stub
		String nationsUrl = "/api/v2/country.do";
		return httpUtil.doPOST(nationsUrl, null);
	}
	
	@Override
	public String getDetailInfo(String countryId,String payMode) throws Exception {
		// TODO Auto-generated method stub
		String url ="/api/v2/detail_info.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("country_id",countryId);  
		params.put("pay_mode",payMode);  
		return httpUtil.doPOST(url,params) ;
	}
	
	
	
	@Override
	public String createRemit(int countryId, int transferNetwork, int payMode, double receiveAmount, double sendAmount,
			double remitFee,String detailInfo,boolean isCreateNow)
			throws Exception {
		// TODO Auto-generated method stub
		String Url = "/api/v2/create_remittance.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("country_id",countryId+"");  
		params.put("transfer_network", transferNetwork+"");//1:OKD 2:BTC
		params.put("pay_mode",payMode+""); //1:Cash 2:Bank transfer
		if(sendAmount!=0d){
		   params.put("send_amount",sendAmount+"");
		}else if(receiveAmount!=0d){
		  params.put("receive_amount",receiveAmount+"");
		}
		params.put("detail_info", detailInfo);
		params.put("remit_fee", remitFee+"");
		if(isCreateNow){
			params.put("is_create","1");
		}else{
			params.put("is_create","0");
		}
		return httpUtil.doPOST(Url, params);		
	}

	
	
	
	private static boolean isEmpty(String str) {
		if(str == null) 
			return true; 
		String tempStr = str.trim(); 
		if(tempStr.length() == 0)
			return true; 
		if(tempStr.equals("null"))
			return true;
		return false; 
	}


	@Override
	public String getPayInfo(String id) throws Exception {
		// TODO Auto-generated method stub
		String Url = "/api/v2/pay_info.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		return httpUtil.doPOST(Url, params);
		
	}


	public String toPay(String id, String hex) throws Exception {
		// TODO Auto-generated method stub
		String Url = "/api/v2/pay.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id); 
		params.put("pay_hex", hex);
		return httpUtil.doPOST(Url, params);
	}


	@Override
	public String getRemitInfo(String id,int type) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/remittance_info.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		params.put("type",type+"");
		return httpUtil.doPOST(url, params);
	}

	@Override
	public String getRemitList(int type,int pageNo,int pageSize,int status) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/remittance_list.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("status",status+"");
		params.put("type", type+"");
		params.put("current_page", pageNo+"");
		params.put("page_size",pageSize+"");
		return httpUtil.doPOST(url, params);
	}

	public String accept(String id) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/accept.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		return httpUtil.doPOST(url, params);
	}

	@Override
	public String getReciveInfo(String id) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/receive_info.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		return httpUtil.doPOST(url, params);
	}


	public String appealReceive(String id, int pickupCode, String receiveHex) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/receive.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);
		params.put("pickup_code", pickupCode+"");
		params.put("receive_hex",receiveHex );
		return httpUtil.doPOST(url, params);
	}


	public String getUserInfo() throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/user_info.do";
		return httpUtil.doPOST(url, null);
	}




	@Override
	public String reject(String id,String hex,String reason) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/reject.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		params.put("reject_hex",hex);
		params.put("reason", reason);
		return httpUtil.doPOST(url, params);
	}

	@Override
	public String getRejectInfo(String id) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/reject_info.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		return httpUtil.doPOST(url, params);
	}


	@Override
	public String getReFundInfo(String id) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/refund_info.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		return httpUtil.doPOST(url, params);
	}

	@Override
	public String refund(String id, String hex) throws Exception {
		// TODO Auto-generated method stub
		String url = "/api/v2/refund.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id",id);  
		params.put("refund_hex",hex);
		return httpUtil.doPOST(url, params);
	}

		
	

}
