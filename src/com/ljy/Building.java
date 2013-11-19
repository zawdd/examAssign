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
	int allRoomSpace; //��¼�˽��������п�λ
	int leftRoomSpace; //ʣ���δ����Ŀ�λ
	
	public Building (String name){
		this.Name = name;
		this.Rooms = new ArrayList<Room>();
		this.allRoomSpace = 0;
		this.leftRoomSpace = 0;
	}
	
	//�������
	public boolean addRoom(Room room){
		this.allRoomSpace += room.RoomVol;
		this.leftRoomSpace += room.RoomVol;
		return this.Rooms.add(room);
	}
	//�����������
	public int countRoom(){
		return this.Rooms.size();
	}
	//���Ҹ�����λ����������
	public void sort(){
		Collections.sort(Rooms);
	}
	//�ҵ��ʹ���number������������̵�
	public int[] findMinRoomNumber(int number){
		int result[] = new int[2];
		int beg = 0;
		int minlen = 999;
		for(int i=0;i<this.Rooms.size();i++){
			if(this.Rooms.get(i).isFree()==true){
				int tmpnumber=0;//��¼��ǰ�ķ�����λ��
				for(int j=i;j<this.Rooms.size();j++){
					if(this.Rooms.get(j).isFree()!=true){
						break;
					}else{
						tmpnumber += this.Rooms.get(j).RoomVol;
						if(tmpnumber > number){//�������ķ���Ŀ�λ���������λʱ
							if((j-i+1) < minlen){//�������ķ������ȵ�ǰ��С��
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
	//�ҵ���ǰ������ӽ��Ŀ��ý���
	//�Ӵ�С������������������number��ʱ����¼��index
	//������������numberСʱ����ֹ��������һ����number�������index
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
		//���еĽ��Ҷ���ռ����
		if(i==this.Rooms.size() && result == -1){
			return null;
		}
		if(result == -1){//number�����еĿ��Ҷ���ֱ�ӷ��ص�ǰ���Ľ���
			room = this.Rooms.get(i);
		}else{//���ر�number��Ľ�������С��
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
