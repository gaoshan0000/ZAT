package com.gongyong;

import java.util.Date;
import java.util.Set;


/**
 * 案件信息
 * @author 思宁
 *
 */
public class Case {
	private int id;
	private String name;
	private String des;
	private String img;
	private String location;
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
