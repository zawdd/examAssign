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
	String buildingNames[] = {"珠海校区","南校区1","东校区","第一教学楼","珠海校区","南校区2","南校区3"};
	String courseNames[] = {"马克思","毛泽东","思修","史纲"};
	int east[] = {0,23,42,66,83,104};
	int zhuhai[] = {0,61};
	int south[] = {0,28}; 
	int southall[] = {0,28,63}; 
	int southyijiao[] = {0,35};
	
	public Building initialFloor(int schoolid,int beg, int end){//包括beg不包括end
		Building building = new Building(buildingNames[schoolid]);
		Sheet sheet;
		Workbook book;
		Cell cell0,cell1;
		try{
			book = Workbook.getWorkbook(new File("rooms.xls"));
			sheet = book.getSheet(schoolid);
			
			int row = beg;
			while(row < end){
				cell0 = sheet.getCell(0, row);//(列，行)
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

	//分配，连续的教室，数量最小
	public ArrayList<Teacher> assign3(ArrayList<Teacher> ts, ArrayList<Building> build){
		ArrayList<Teacher> teachers = ts;
		ArrayList<Building> builds=build;
		Collections.sort(teachers);
		//初始条件检查
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
		
		//把每个教师分到build的一个合适的楼层中
	
		for(Teacher teacher : teachers){
			int buildid=0,buildbeg=0,buildlen=999;//记录找出的最合适的建筑的开始房间和最小长度
			for(int id = 0; id<builds.size(); id++){
				int tmpres[] = builds.get(id).findMinRoomNumber(teacher.allStudentNumber);
				if(tmpres[1] < buildlen){
					buildlen = tmpres[1];
					buildbeg = tmpres[0];
					buildid = id;
				}
			}
			if(buildlen == 999){
				//找不到				
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
 		//把所有的教室排序，以贪婪算法解背包问题的策略求解
 		//所用的考场会减少，但是每个老师的考场不连续
		ArrayList<Teacher> teachers = ts;
		Building building = build;
		Collections.sort(teachers);
		building.sort();
		//初始条件检查
		int allNeedSpace = 0;
		for(Teacher t : teachers){
			allNeedSpace += t.allStudentNumber;
		}
		if(allNeedSpace > building.allRoomSpace){
			System.out.println("There is no enougth space!");
			return null;
		}
		//循环分配教室
		int epxino = 10;//冗余的空位
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

    //东校测试成功
	public int handleEast(String inputfile){	
			
		//格式化选课信息
		FirstHandleFile formatfile = new FirstHandleFile();
		formatfile.formatInputFile(inputfile);
		//依次处理每个校区的每门课
		String formatFile = "formatexam.xls";
		int sheetcode = 0;//开始的sheet
		int examNo = 8;//记录考试的sheet从8开始
		int buildNo = 2;//指明使用哪个楼
		
		String resultFile = "eastRESULT.xls";
		WritableWorkbook outbook;
		WritableSheet outsheet;
		try {
			outbook = Workbook.createWorkbook(new File(resultFile));
			ArrayList<Building> east = new ArrayList<Building>();
			for(int i=0;i<4;i++){//依次处理每一门课
				east = this.initialBuilding(buildNo, this.east);
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, east);
				int row = 0;
				outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
				outsheet.addCell(new Label(0,row,"教师姓名"));
				outsheet.addCell(new Label(1,row,"教学班ID"));
				outsheet.addCell(new Label(2,row,"课程名称"));
				outsheet.addCell(new Label(3,row,"教学班人数"));
				outsheet.addCell(new Label(4,row,"考场ID"));
				outsheet.addCell(new Label(5,row,"考场座位数"));
				outsheet.addCell(new Label(6,row,"总考生人数"));
				outsheet.addCell(new Label(7,row,"总考场座位数"));
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
	//珠海校区测试
	public int handleZhuhai(String inputfile){			
		//格式化选课信息
		FirstHandleFile formatfile = new FirstHandleFile();
		formatfile.formatInputFile(inputfile);
		//依次处理每个校区的每门课
		String formatFile = "formatexam.xls";
		int sheetcode = 0;//开始的sheet
		int examNo = 0;//记录考试的sheet从此No开始
		int buildNo = 4;//指明使用哪个楼
		
		String resultFile = "zhuhaiRESULT.xls";
		WritableWorkbook outbook;
		WritableSheet outsheet;
		try {
			outbook = Workbook.createWorkbook(new File(resultFile));
			ArrayList<Building> zhuhai = new ArrayList<Building>();
			for(int i=0;i<4;i++){//依次处理每一门课
				zhuhai = this.initialBuilding(buildNo, this.zhuhai);
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, zhuhai);
				int row = 0;
				outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
				outsheet.addCell(new Label(0,row,"教师姓名"));
				outsheet.addCell(new Label(1,row,"教学班ID"));
				outsheet.addCell(new Label(2,row,"课程名称"));
				outsheet.addCell(new Label(3,row,"教学班人数"));
				outsheet.addCell(new Label(4,row,"考场ID"));
				outsheet.addCell(new Label(5,row,"考场座位数"));
				outsheet.addCell(new Label(6,row,"总考生人数"));
				outsheet.addCell(new Label(7,row,"总考场座位数"));
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
	//南校区测试
	public int handleSouth(String inputfile){			
		//格式化选课信息
		FirstHandleFile formatfile = new FirstHandleFile();
		formatfile.formatInputFile(inputfile);
		//依次处理每个校区的每门课
		String formatFile = "formatexam.xls";
		int sheetcode = 0;//开始的sheet
		int examNo = 4;//记录考试的sheet从此No开始
		//初始对应化课室信息
		//MainDrive test = new MainDrive();
		String resultFile = "southRESULT.xls";
		WritableWorkbook outbook;
		WritableSheet outsheet;
		try {
			outbook = Workbook.createWorkbook(new File(resultFile));
			
			int buildNo = 1;//指明使用哪个楼
			//只使用逸夫楼
			ArrayList<Building> south = new ArrayList<Building>();//this.initialBuilding(buildNo, this.south);	

			for(int i=0;i<4;i++){//依次处理每一门课
				south = this.initialBuilding(buildNo, this.south);
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, south);
				//把信息写入文件				
				try{	
					int row = 0;
					outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
					outsheet.addCell(new Label(0,row,"教师姓名"));
					outsheet.addCell(new Label(1,row,"教学班ID"));
					outsheet.addCell(new Label(2,row,"课程名称"));
					outsheet.addCell(new Label(3,row,"教学班人数"));
					outsheet.addCell(new Label(4,row,"考场ID"));
					outsheet.addCell(new Label(5,row,"考场座位数"));
					outsheet.addCell(new Label(6,row,"总考生人数"));
					outsheet.addCell(new Label(7,row,"总考场座位数"));
					row++;
					for(Teacher t : teachers){
						row = t.writeOnFile(outsheet, row);
					}
		            sheetcode ++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}//for
			
			//只使用逸夫楼，之字形
			buildNo = 5;
			for(int i=0;i<4;i++){//依次处理每一门课
				south = this.initialBuilding(buildNo, this.south);//每次循环都要初始化，为了清空标记	
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, south);
				//把信息写入文件
				try{	
					int row = 0;
					outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
					outsheet.addCell(new Label(0,row,"教师姓名"));
					outsheet.addCell(new Label(1,row,"教学班ID"));
					outsheet.addCell(new Label(2,row,"课程名称"));
					outsheet.addCell(new Label(3,row,"教学班人数"));
					outsheet.addCell(new Label(4,row,"考场ID"));
					outsheet.addCell(new Label(5,row,"考场座位数"));
					outsheet.addCell(new Label(6,row,"总考生人数"));
					outsheet.addCell(new Label(7,row,"总考场座位数"));
					row++;
					for(Teacher t : teachers){
						row = t.writeOnFile(outsheet, row);
					}
		            sheetcode ++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}//for
			
			//使用南校区全部的楼
			buildNo = 6;		

			for(int i=0;i<4;i++){//依次处理每一门课
				south = this.initialBuilding(buildNo, this.southall);	
				ArrayList<Teacher> teachers = this.readClassInfo(formatFile,examNo+i);
				teachers = this.assign3(teachers, south);
				//把信息写入文件

				try{				
					int row = 0;
					outsheet = outbook.createSheet(""+this.buildingNames[buildNo]+this.courseNames[i], sheetcode);
					outsheet.addCell(new Label(0,row,"教师姓名"));
					outsheet.addCell(new Label(1,row,"教学班ID"));
					outsheet.addCell(new Label(2,row,"课程名称"));
					outsheet.addCell(new Label(3,row,"教学班人数"));
					outsheet.addCell(new Label(4,row,"考场ID"));
					outsheet.addCell(new Label(5,row,"考场座位数"));
					outsheet.addCell(new Label(6,row,"总考生人数"));
					outsheet.addCell(new Label(7,row,"总考场座位数"));
					row++;
					for(Teacher t : teachers){
						row = t.writeOnFile(outsheet, row);
					}

		            sheetcode ++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}//for
			
			 //最后再关闭文件                    
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
		//读取一门课的考试信息
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
				cell0 = sheet.getCell(0, row);//(列，行)
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
