package com.ljy;

public class Room implements Comparable<Room>{
	String RoomID ;
	int RoomVol;
	boolean isFree;
	
	public Room(String id, int vol){
		this.RoomID = id;
		this.RoomVol = vol;
		this.isFree = true;
	}
	
	void setUsage(){
		this.isFree = false;
	}
	
	void setFree(){
		this.isFree = true;
	}
	
	boolean isFree(){
		return this.isFree;
	}

	@Override
	public int compareTo(Room r) {
		// TODO Auto-generated method stub
		return r.RoomVol - this.RoomVol;
	}
}
