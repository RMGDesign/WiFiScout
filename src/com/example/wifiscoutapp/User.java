package com.example.wifiscoutapp;
import java.util.*;

public class User {
	public String macAdd; 
	public String ipAdd;
	public String name;
	public String notes;
	public String type;
	public Calendar current;
	public User(String mac,String ip){
		this.macAdd = mac;
		this.ipAdd = ip;
		this.name =" ";
		this.notes =" ";
		this.type = "User";
	}
	public User(String namer,String mac,String ip,String not,String ty){
		this.name = namer;
		this.notes = not;
		this.macAdd = mac;
		this.ipAdd = ip;
		this.type = ty;
		
	}
	public String getName(){
		return name;
	}
	public String getMac(){
		return macAdd;
	}
	public String getIp(){
		return ipAdd;
	}
	public String getType(){
		return type;
	}
	public String getNotes(){
		return notes;
	}
	public String toString(){
		return getType()+"~"+ getMac()+"~"+ getIp()+"~"+ getName() + "~" +getNotes();
	}
}