/** 
 * description£º
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç11:21:45
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç11:21:45
 */
public class RegisterSellerMsg 
{
	@SerializedName("username")
	private String strEmail;
	
	@SerializedName("password")
	private String strPassword;
	
	@SerializedName("seller_name")
	private String strSellerName;

	@SerializedName("seller_lat")
	private double sellerLat;

	@SerializedName("seller_lng")
	private double sellerLng;

	@SerializedName("seller_address")
	private String sellerAddress;

	@SerializedName("seller_logo")
	private String sellerLogo;

	
	public void setEmail(String email)
	{
		this.strEmail=email;
	}
	
	public String getEmail()
	{
		return this.strEmail;
	}
	
	public void setPassword(String password)
	{
		this.strPassword=password;
	}
	
	public String getPassword()
	{
		return this.strPassword;
	}
	
	public void setSellerName(String sellerName)
	{
		this.strSellerName=sellerName;
	}
	
	public String getSellerName()
	{
		return this.strSellerName;
	}

	public double getSellerLng() {
		return sellerLng;
	}

	public void setSellerLng(double sellerLng) {
		this.sellerLng = sellerLng;
	}

	public double getSellerLat() {
		return sellerLat;
	}

	public void setSellerLat(double sellerLat) {
		this.sellerLat = sellerLat;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	public String getSellerLogo() {
		return sellerLogo;
	}

	public void setSellerLogo(String sellerLogo) {
		this.sellerLogo = sellerLogo;
	}
}
