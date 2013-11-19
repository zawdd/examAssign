package com.ljy;
import java.util.*;
import java.io.File;
import java.io.IOException;

import jxl.*;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;
import jxl.write.WriteException;

public class MainDrive {
	String buildingNames[] = {"�麣У��","��У��1","��У��","��һ��ѧ¥","�麣У��","��У��2","��У��3"};
	String courseNames[] = {"���˼","ë��","˼��","ʷ��"};
	int east[] = {0,23,42,66,83,104};
	int zhuhai[] = {0,61};
	int south[] = {0,28}; 
	int southall[] = {0,28,63}; 
	int southyijiao[] = {0,35};
	
	public Building initialFloor(int schoolid,int beg, int end){//����beg������end
		Building building = new Building(buildingNames[schoolid]);
		Sheet sheet;
		Workbook book;
		Cell cell0,cell1;
		try{
			book = Workbook.getWorkbook(new File("rooms.xls"));
			sheet = book.getSheet(schoolid);
			
			int row = beg;
			while(row < end){
				cell0 = sheet.getCell(0, row);//(�У���)
				cell1 = sheet.getCell(1, row);
				
				Room newRoom = new Room(cell0.getContents(), 
										Integer.parseInt(cell1.getContents()));
				
				building.addRoom(newRoom);
				row++;
			}
			
			book.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return building;
	}
	
	public ArrayList<Building> initialBuilding(int schoolid,int floornumber[]){
		ArrayList<Building> builds = new ArrayList<Building>();
		for(int i=0;i<floornumber.length-1;i++){
			builds.add(this.initialFloor(schoolid, floornumber[i], floornumber[i+1]));
		}		
		return builds;
	}

	//���䣬�����Ľ��ң�������С
	public ArrayList<Teacher> assign3(ArrayList<Teacher> ts, ArrayList<Building> build){
		ArrayList<Teacher> teachers = ts;
		ArrayList<Building> builds=build;
		Collections.sort(teachers);
		//��ʼ�������
		int allNeedSpace = 0;
		int allFreeSpace = 0;
		for(Teacher t : teachers){
			allNeedSpace += t.allStudentNumber;
		}
		for(Building b : builds){
			allFreeSpace += b.allRoomSpace;
		}
		if(allNeedSpace > allFreeSpace){
			System.out.println("There is no enougth space!");
			return null;
		}
		
		//��ÿ����ʦ�ֵ�build��һ�����ʵ�¥����
	
		for(Teacher teacher : teachers){
			int buildid=0,buildbeg=0,buildlen=999;//��¼�ҳ�������ʵĽ����Ŀ�ʼ�������С����
			for(int id = 0; id<builds.size(); id++){
				int tmpres[] = builds.get(id).findMinRoomNumber(teacher.allStudentNumber);
				if(tmpres[1] < buildlen){
					buildlen = tmpres[1];
					buildbeg = tmpres[0];
					buildid = id;
				}
			}
			if(buildlen == 999){
				//�Ҳ���				
				//System.out.println("zhaobudao!");
				break;
			}else{
				for(int i=buildbeg;i<buildbeg+buildlen;i++){
					teacher.addRoom(builds.get(buildid).Rooms.get(i));
					builds.get(buildid).Rooms.get(i).setUsage();
				}
			}

		}
		return teachers;
	}
	
/*	public ArrayList<Teacher> assign(ArrayList<Teacher> ts, Building build){
 		//�����еĽ���������̰���㷨�ⱳ������Ĳ������
 		//���õĿ�������٣�����ÿ����ʦ�Ŀ���������
		ArrayList<Teacher> teachers = ts;
		Building building = build;
		Collections.sort(teachers);
		building.sort();
		//��ʼ�������
		int allNeedSpace = 0;
		for(Teacher t : teachers){
			allNeedSpace += t.allStudentNumber;
		}
		if(allNeedSpace > building.allRoomSpace){
			System.out.println("There is no enougth space!");
			return null;
		}
		//ѭ���������
		int epxino = 10;//����Ŀ�λ
		for(Teacher teacher : teachers){
			while(teacher.allRoomSpace < teacher.allStudentNumber+epxino){
				int needspace = teacher.allStudentNumber + epxino - teacher.allRoomSpace;
				Room tmpRoom = building.findProperRoom(needspace);
				if(tmpRoom == null){
					System.out.println("error,there is no enougth room!");
					return null;
				}
				teacher.addRoom(tmpRoom);
				tmpRoom.setUsage();
			}

		}
		return teachers;
	}*/

    //��У���Գɹ�
	public int handleEast(String inputfile){	
			
		//��ʽ��ѡ����Ϣ
		FirstHandleFile formatfile = new FirstHandleFile();
		formatfile.formatInputFile(inputfile);
		//���δ���ÿ��У����ÿ�ſ�
		String formatFile = "formatexam.xls";
		int sheetcode = 0;//��ʼ��sheet
		int examNo = 8;//��¼���Ե�sheet��8��ʼ
		int buildNo = 2;//ָ��ʹ���ĸ�¥
		
		String resultFile = "eastRESULT.xls";
		WritableWorkbook outbook;
		WritableSheet outsheet;
		try {
			outbook = Workbook.createWorkbook(new File(resultFile));
			ArrayList<Building> east = new ArrayList<Building>();
			for(int i=0;i<4;i++){//���δ���ÿһ�ſ�
				east = this.initialBuilding(buildNo, this.east);
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, east);
				int row = 0;
				outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
				outsheet.addCell(new Label(0,row,"��ʦ����"));
				outsheet.addCell(new Label(1,row,"��ѧ��ID"));
				outsheet.addCell(new Label(2,row,"�γ�����"));
				outsheet.addCell(new Label(3,row,"��ѧ������"));
				outsheet.addCell(new Label(4,row,"����ID"));
				outsheet.addCell(new Label(5,row,"������λ��"));
				outsheet.addCell(new Label(6,row,"�ܿ�������"));
				outsheet.addCell(new Label(7,row,"�ܿ�����λ��"));
				row++;
				for(Teacher t : teachers){
					row = t.writeOnFile(outsheet, row);
				}
				sheetcode++;
			}
			
			outbook.write();    
			outbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sheetcode;
		
	}
	//�麣У������
	public int handleZhuhai(String inputfile){			
		//��ʽ��ѡ����Ϣ
		FirstHandleFile formatfile = new FirstHandleFile();
		formatfile.formatInputFile(inputfile);
		//���δ���ÿ��У����ÿ�ſ�
		String formatFile = "formatexam.xls";
		int sheetcode = 0;//��ʼ��sheet
		int examNo = 0;//��¼���Ե�sheet�Ӵ�No��ʼ
		int buildNo = 4;//ָ��ʹ���ĸ�¥
		
		String resultFile = "zhuhaiRESULT.xls";
		WritableWorkbook outbook;
		WritableSheet outsheet;
		try {
			outbook = Workbook.createWorkbook(new File(resultFile));
			ArrayList<Building> zhuhai = new ArrayList<Building>();
			for(int i=0;i<4;i++){//���δ���ÿһ�ſ�
				zhuhai = this.initialBuilding(buildNo, this.zhuhai);
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, zhuhai);
				int row = 0;
				outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
				outsheet.addCell(new Label(0,row,"��ʦ����"));
				outsheet.addCell(new Label(1,row,"��ѧ��ID"));
				outsheet.addCell(new Label(2,row,"�γ�����"));
				outsheet.addCell(new Label(3,row,"��ѧ������"));
				outsheet.addCell(new Label(4,row,"����ID"));
				outsheet.addCell(new Label(5,row,"������λ��"));
				outsheet.addCell(new Label(6,row,"�ܿ�������"));
				outsheet.addCell(new Label(7,row,"�ܿ�����λ��"));
				row++;
				for(Teacher t : teachers){
					row = t.writeOnFile(outsheet, row);
				}
				sheetcode++;
			}
			
			outbook.write();    
			outbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sheetcode;
	}
	//��У������
	public int handleSouth(String inputfile){			
		//��ʽ��ѡ����Ϣ
		FirstHandleFile formatfile = new FirstHandleFile();
		formatfile.formatInputFile(inputfile);
		//���δ���ÿ��У����ÿ�ſ�
		String formatFile = "formatexam.xls";
		int sheetcode = 0;//��ʼ��sheet
		int examNo = 4;//��¼���Ե�sheet�Ӵ�No��ʼ
		//��ʼ��Ӧ��������Ϣ
		//MainDrive test = new MainDrive();
		String resultFile = "southRESULT.xls";
		WritableWorkbook outbook;
		WritableSheet outsheet;
		try {
			outbook = Workbook.createWorkbook(new File(resultFile));
			
			int buildNo = 1;//ָ��ʹ���ĸ�¥
			//ֻʹ���ݷ�¥
			ArrayList<Building> south = new ArrayList<Building>();//this.initialBuilding(buildNo, this.south);	

			for(int i=0;i<4;i++){//���δ���ÿһ�ſ�
				south = this.initialBuilding(buildNo, this.south);
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, south);
				//����Ϣд���ļ�				
				try{	
					int row = 0;
					outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
					outsheet.addCell(new Label(0,row,"��ʦ����"));
					outsheet.addCell(new Label(1,row,"��ѧ��ID"));
					outsheet.addCell(new Label(2,row,"�γ�����"));
					outsheet.addCell(new Label(3,row,"��ѧ������"));
					outsheet.addCell(new Label(4,row,"����ID"));
					outsheet.addCell(new Label(5,row,"������λ��"));
					outsheet.addCell(new Label(6,row,"�ܿ�������"));
					outsheet.addCell(new Label(7,row,"�ܿ�����λ��"));
					row++;
					for(Teacher t : teachers){
						row = t.writeOnFile(outsheet, row);
					}
		            sheetcode ++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}//for
			
			//ֻʹ���ݷ�¥��֮����
			buildNo = 5;
			for(int i=0;i<4;i++){//���δ���ÿһ�ſ�
				south = this.initialBuilding(buildNo, this.south);//ÿ��ѭ����Ҫ��ʼ����Ϊ����ձ��	
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, south);
				//����Ϣд���ļ�
				try{	
					int row = 0;
					outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
					outsheet.addCell(new Label(0,row,"��ʦ����"));
					outsheet.addCell(new Label(1,row,"��ѧ��ID"));
					outsheet.addCell(new Label(2,row,"�γ�����"));
					outsheet.addCell(new Label(3,row,"��ѧ������"));
					outsheet.addCell(new Label(4,row,"����ID"));
					outsheet.addCell(new Label(5,row,"������λ��"));
					outsheet.addCell(new Label(6,row,"�ܿ�������"));
					outsheet.addCell(new Label(7,row,"�ܿ�����λ��"));
					row++;
					for(Teacher t : teachers){
						row = t.writeOnFile(outsheet, row);
					}
		            sheetcode ++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}//for
			
			//ʹ����У��ȫ����¥
			buildNo = 6;		

			for(int i=0;i<4;i++){//���δ���ÿһ�ſ�
				south = this.initialBuilding(buildNo, this.southall);	
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, south);
				//����Ϣд���ļ�

				try{				
					int row = 0;
					outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
					outsheet.addCell(new Label(0,row,"��ʦ����"));
					outsheet.addCell(new Label(1,row,"��ѧ��ID"));
					outsheet.addCell(new Label(2,row,"�γ�����"));
					outsheet.addCell(new Label(3,row,"��ѧ������"));
					outsheet.addCell(new Label(4,row,"����ID"));
					outsheet.addCell(new Label(5,row,"������λ��"));
					outsheet.addCell(new Label(6,row,"�ܿ�������"));
					outsheet.addCell(new Label(7,row,"�ܿ�����λ��"));
					row++;
					for(Teacher t : teachers){
						row = t.writeOnFile(outsheet, row);
					}

		            sheetcode ++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}//for
			
			 //����ٹر��ļ�                    
            try {
            	outbook.write();    
				outbook.close();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		return sheetcode;		
	}
	
	
	public static void main(String args[]){
		MainDrive test = new MainDrive();
		String inputFile = "examlist.xls";
		//int sheetcode = 0;
		test.handleSouth(inputFile);
		test.handleZhuhai(inputFile);
		test.handleEast(inputFile);
	}
	
	public ArrayList<Teacher> readClassInfo(String file, int sheeid){
		//��ȡһ�ſεĿ�����Ϣ
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		int examsheetid = sheeid;
		//String examfile = "exam.xls";
		String examfile = file;
		Sheet sheet;
		Workbook book;
		Cell cell0,cell1,cell2,cell3;
		try{
			book = Workbook.getWorkbook(new File(examfile));
			sheet = book.getSheet(examsheetid);
			int row=0;
			while(row < sheet.getRows()){
				cell0 = sheet.getCell(0, row);//(�У���)
				cell1 = sheet.getCell(1, row);
				cell2 = sheet.getCell(2, row);
				cell3 = sheet.getCell(3, row);

				Class newClass = new Class(cell0.getContents(),
											cell1.getContents(),
											Integer.parseInt(cell3.getContents()));
				
				Teacher oldteacher = null;
				for(Teacher tmpteacher : teachers){
					if(tmpteacher.TeacherName.equals(cell2.getContents())==true){
						//tmpteacher.addClass(newClass);
						oldteacher = tmpteacher;
						continue;
					}
				}
				if(oldteacher!=null){
					oldteacher.addClass(newClass);
				}else{
					Teacher newTeacher = new Teacher(cell2.getContents());
					newTeacher.addClass(newClass);
					teachers.add(newTeacher);
				}
				row++;
			}
			
			book.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return teachers;
	}
}
