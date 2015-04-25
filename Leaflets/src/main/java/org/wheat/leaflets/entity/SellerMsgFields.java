/** 
 * description£º
 * @author wheat
 * date: 2015-4-11  
 * time: ÏÂÎç3:30:07
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-4-11  
 * time: ÏÂÎç3:30:07
 */
public class SellerMsgFields 
{
	@SerializedName("username")
	private String userName;
	
	@SerializedName("phone_number")
	private String phoneNubmer;
	
	@SerializedName("seller_logo_name")
	private String sellerLogoPaht;
	
	@SerializedName("seller_name")
	private String sellerName;
	
	@SerializedName("lat")
	private double lat;
	
	@SerializedName("lng")
	private double lng;
	
	@SerializedName("email")
	private String email;

	@SerializedName("seller_address")
	private String sellerAddress;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNubmer() {
		return phoneNubmer;
	}

	public void setPhoneNubmer(String phoneNubmer) {
		this.phoneNubmer = phoneNubmer;
	}

	public String getSellerLogoPaht() {
		return sellerLogoPaht;
	}

	public void setSellerLogoPaht(String sellerLogoPaht) {
		this.sellerLogoPaht = sellerLogoPaht;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
	
}
