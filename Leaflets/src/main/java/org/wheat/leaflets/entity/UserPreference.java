/** 
 * description£º
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç9:53:58
 */ 
package org.wheat.leaflets.entity;

import java.io.Serializable;

/** 
 * description:
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç9:53:58
 */
public class UserPreference implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String userEmail;
	private String nickName;
	private String userAvatar;
	private  String personalIntroduction;
	private double lat;
	private double lng;
	private String phoneNumber;
	
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getPersonalIntroduction() {
		return personalIntroduction;
	}

	public void setPersonalIntroduction(String personalIntroduction) {
		this.personalIntroduction = personalIntroduction;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
