/*
 * Building.java
 * @author LiJingYi
 * @email jingyiliscut@gmail.com
 */

package com.ljy;
import java.util.*;

public class Building {
	String Name;
	ArrayList<Room> Rooms;
	int allRoomSpace; //记录此建筑的所有考位
	int leftRoomSpace; //剩余的未分配的空位
	
	public Building (String name){
		this.Name = name;
		this.Rooms = new ArrayList<Room>();
		this.allRoomSpace = 0;
		this.leftRoomSpace = 0;
	}
	
	//增添教室
	public boolean addRoom(Room room){
		this.allRoomSpace += room.RoomVol;
		this.leftRoomSpace += room.RoomVol;
		return this.Rooms.add(room);
	}
	//计算教室总数
	public int countRoom(){
		return this.Rooms.size();
	}
	//教室根据座位数降序排列
	public void sort(){
		Collections.sort(Rooms);
	}
	//找到和大于number的子序列中最短的
	public int[] findMinRoomNumber(int number){
		int result[] = new int[2];
		int beg = 0;
		int minlen = 999;
		for(int i=0;i<this.Rooms.size();i++){
			if(this.Rooms.get(i).isFree()==true){
				int tmpnumber=0;//记录当前的房间座位数
				for(int j=i;j<this.Rooms.size();j++){
					if(this.Rooms.get(j).isFree()!=true){
						break;
					}else{
						tmpnumber += this.Rooms.get(j).RoomVol;
						if(tmpnumber > number){//当连续的房间的空位大于所需空位时
							if((j-i+1) < minlen){//如果所需的房间数比当前的小，
								minlen = j-i+1;
								beg = i;
								break;
							}				
						}
					}
				}
			}
		}
		result[0] = beg;
		result[1] = minlen;
		return result;
	}
	//找到当前人数最接近的可用教室
	//从大到小遍历，当课室人数比number大时，记录此index
	//当课室人数比number小时，终止，返回上一个比number大的数的index
	public Room findProperRoom(int number){
		Room room = null;
		this.sort();
		int result = -1;
		int i;
		for(i=0;i<this.Rooms.size();i++){
			if(this.Rooms.get(i).isFree == true){
				if(this.Rooms.get(i).RoomVol < number){
					break;
				}else{
					result = i;
				}
			}
		}
		//所有的教室都被占用了
		if(i==this.Rooms.size() && result == -1){
			return null;
		}
		if(result == -1){//number比所有的课室都大，直接返回当前最大的教室
			room = this.Rooms.get(i);
		}else{//返回比number大的教室中最小的
			room = this.Rooms.get(result);
		}
		return room;
	}
	
	public void Print(){
		//System.out.println(this.Name+"\t"+this.allRoomSpace);
		for(Room r : this.Rooms){
			System.out.println(r.RoomID+"\t"+r.RoomVol);
		}
	}
}
