package com.gongyong;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


/**
 * 案件信息
 * @author 思宁
 *
 */
public class Case implements Serializable {
	private int id;
	private int pId;
	private String name;
	private String des;
	private String img;
	private String location;
	private int isHandled;
	private Date date;
	public Date getDate() {
		return date;
	}
	public String getDes() {
		return des;
	}
	public int getId() {
		return id;
	}
	public String getImg() {
		return img;
	}
	public String getLocation() {
		return location;
	}
	public String getName() {
		return name;
	}
	public int getIsHandled() {
		return isHandled;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public void setIsHandled(int isHandled) {
		this.isHandled = isHandled;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setDate(Date d) {
		this.date = d;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Case(){	
}
public Case(int id,String name,String des,String location){
		this.id = id;
		this.name = name;
		this.des = des;
		this.location = location;
	}
	public Case(int id,String name,String des,String location,Date date,String img){
		this.id = id;
		this.name = name;
		this.des = des;
		this.location = location;
		this.img = img;
		this.date = date;
	}
}
