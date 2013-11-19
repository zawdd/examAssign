/*
 * Teacher.java
 * @author LiJingYi
 * @email jingyiliscut@gmail.com
 */
package com.ljy;
import java.util.*;

import jxl.*;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;

public class Teacher implements Comparable<Teacher>{
	String TeacherName;
	ArrayList<Class> ClassArr;
	ArrayList<Room> RoomArr;
	int allClassNumber;		//����ʦ�ܹ��м�����ѧ��
	int allStudentNumber;	//����ʦ�����н�ѧ�༶������
	int allRoomSpace;		//����ʦ�����п����ռ�
	int allRoomNumber;		//����ʦӵ�еĿ�������
	
	public Teacher(String name){
		this.TeacherName = name;
		this.ClassArr = new ArrayList<Class>();
		this.RoomArr = new ArrayList<Room>();
		this.allStudentNumber = 0;
		this.allRoomSpace = 0;
		this.allClassNumber = 0;
		this.allRoomNumber = 999;
	}
	
	public boolean addClass(Class cla){
		this.allStudentNumber += cla.ClassVol;
		this.allClassNumber ++;
		return this.ClassArr.add(cla);
	}
	
	public boolean addRoom(Room room){
		this.allRoomSpace += room.RoomVol;
		this.allRoomNumber ++;
		return this.RoomArr.add(room);
	}
	
	public boolean removeRoom(Room room){
		this.allRoomSpace -= room.RoomVol;
		this.allRoomNumber --;
		return this.RoomArr.remove(room);
	}
	
	public void Print(){
		int maxsize = this.ClassArr.size()>this.RoomArr.size()?this.ClassArr.size():this.RoomArr.size();
		
		for(int i=0;i<maxsize;i++){
			System.out.print(this.TeacherName+ "\t");
			if(i<this.ClassArr.size()){
				Class c = this.ClassArr.get(i);
				System.out.print(c.ClassID+"\t" + c.ClassName + "\t" + c.ClassVol+ "\t");
			}else{
				System.out.print("" + "\t" + "" + "\t" + ""+ "\t");
			}
			if(i<this.RoomArr.size()){
				Room r = this.RoomArr.get(i);
				System.out.print(r.RoomID+"\t"+r.RoomVol+"\t");
			}else{
				System.out.print("" + "\t" + "" + "\t");
			}
			System.out.println(this.allStudentNumber+"\t"+this.allRoomSpace);
		}		
		
	}

	public int writeOnFile(WritableSheet sheet,int begrow){
		int maxsize = this.ClassArr.size()>this.RoomArr.size()?this.ClassArr.size():this.RoomArr.size();
		//����Ϊ��ʦ�����༶ID���γ�����,�༶����������ID��������������ѧ�������ܿ�λ��
		Cell cell0,cell1,cell2,cell3,cell4,cell5,cell6,cell7;
		WritableSheet outsheet = sheet;
		int row = begrow;
		try{
		
			for(int i=0;i<maxsize;i++){
				//System.out.print(this.TeacherName+ "\t");
				outsheet.addCell(new Label(0,row,this.TeacherName));
				if(i<this.ClassArr.size()){
					Class c = this.ClassArr.get(i);
					//System.out.print(c.ClassID+"\t" + c.ClassName + "\t" + c.ClassVol+ "\t");
					outsheet.addCell(new Label(1,row,c.ClassID));
					outsheet.addCell(new Label(2,row,c.ClassName));
					outsheet.addCell(new Label(3,row,""+c.ClassVol));
				}else{
					//System.out.print("" + "\t" + "" + "\t" + ""+ "\t");
					outsheet.addCell(new Label(1,row,""));
					outsheet.addCell(new Label(2,row,""));
					outsheet.addCell(new Label(3,row,""));
				}
				if(i<this.RoomArr.size()){
					Room r = this.RoomArr.get(i);
					//System.out.print(r.RoomID+"\t"+r.RoomVol+"\t");
					outsheet.addCell(new Label(4,row,r.RoomID));
					outsheet.addCell(new Label(5,row,""+r.RoomVol));
				}else{
					//System.out.print("" + "\t" + "" + "\t");
					outsheet.addCell(new Label(4,row,""));
					outsheet.addCell(new Label(5,row,""));
				}
				//System.out.println(this.allStudentNumber+"\t"+this.allRoomSpace);
				outsheet.addCell(new Label(6,row,""+this.allStudentNumber));
				outsheet.addCell(new Label(7,row,""+this.allRoomSpace));
				
				row++;
			}//for
			outsheet.mergeCells(2, begrow, 2, row-1);//�ϲ���ͬ����ʦ���֣��γ����֣�
			outsheet.mergeCells(0, begrow, 0, row-1);
			outsheet.mergeCells(6, begrow, 6, row-1);//�ϲ������Ϳ�������
			outsheet.mergeCells(7, begrow, 7, row-1);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return row;
	}

	@Override
	public int compareTo(Teacher t) {
		// TODO Auto-generated method stub
		//���ս�ʦӵ�еİ༶�ĸ�����������
		return this.allClassNumber - t.allClassNumber;
		//����ѧ��������������
		//return t.allStudentNumber- this.allStudentNumber;
		//�Ȱ��ս�ʦӵ�еİ༶�������ٵ��࣬�ٰ��������ɶൽ��
//		if (this.allClassNumber == t.allClassNumber){
//			return t.allStudentNumber- this.allStudentNumber;
//		}else{
//			return this.allClassNumber - t.allClassNumber;
//		}
		//return  t.allClassNumber - this.allClassNumber; 
	} 
}
