/** 
 * description£º
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç11:52:55
 */ 
package org.wheat.leaflets.entity;

import java.io.Serializable;

/** 
 * description:
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç11:52:55
 */
public class SellerPreference implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String sellerEmail;
	private String sellerName;
	private String sellerLogoPath;
	private String sellerPhoneNumber;
	private double lat;
	private double lng;
	private String sellerAddress;
	
	
	
	
	public String getSellerEmail() {
		return sellerEmail;
	}
	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerLogoPath() {
		return sellerLogoPath;
	}
	public void setSellerLogoPath(String sellerLogoPath) {
		this.sellerLogoPath = sellerLogoPath;
	}

	public String getSellerPhoneNumber() {
		return sellerPhoneNumber;
	}

	public void setSellerPhoneNumber(String sellerPhoneNumber) {
		this.sellerPhoneNumber = sellerPhoneNumber;
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

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
}
