/*
 * FirstHandleFile.java
 * @author LiJingYi
 * @email jingyiliscut@gmail.com
 */
package com.ljy;
import java.io.File;
import java.util.ArrayList;

import jxl.*;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;

public class FirstHandleFile {
	public String school[] = {"珠海校区","南校区","东校区"};
	public String course[] = {"马克思主义基本原理","毛泽东思想和中国特色社会主义理论体系概论",
						"思想道德修养与法律基础","中国近现代史纲要"};
	
//	public ArrayList<Building> initialEarth(){
//		 ArrayList<Building> earth = new ArrayList<Building>();
//		 Building buildA = new Building("A");
//		 //String Aids[] = {"A101","A102","A103",A104,A105,A201,A202,A203,A204,A207,A301,A302,A306,A401,A402,A403,A404,A405,A501,A502,A503,A504,A505,}
//		 buildA.addRoom(new Room())
//		 return earth;
//	}
//	
	public void formatInputFile(String input){
		//String inputFile = "examlist.xls";
		String inputFile = input;
		String outputFile = "formatexam.xls";

		//顺序按照，珠海，南校，东校
		//每个校区顺序按照  1马克思主义基本原理 2毛泽东 3思想道德修养与法律基础 4中国近现代史纲要
		int sheetcode = -1; //记录output文件的sheet，从0-11，每个sheet是一个考试类别
		int SHEET_ZERO = 0;
		Sheet insheet;
		WritableSheet outsheet;
		Workbook inbook;
		WritableWorkbook outbook;
		Cell cell0,cell1,cell2,cell3,cell4;//依次为教学班号，课程名，老师，人数，校区
		try{
			inbook = Workbook.getWorkbook(new File(inputFile));
			insheet = inbook.getSheet(SHEET_ZERO);
			outbook = Workbook.createWorkbook(new File(outputFile));
			for(int i=0;i<school.length*course.length;i++){
				outbook.createSheet(""+i,i);
			}
			//记录每个sheet目前插入到的行数
			int rowsforeachsheet[] = new int[school.length*course.length];
			int row = 0;
			
			while(row < insheet.getRows()){
				cell0 = insheet.getCell(0, row);//(列，行) 教学班号
				cell1 = insheet.getCell(1, row);//课程名
				cell2 = insheet.getCell(2, row);//老师
				cell3 = insheet.getCell(3, row);//人数
				cell4 = insheet.getCell(4, row);//校区
				
				for(int i=0;i<school.length;i++){
					for(int j=0;j<course.length;j++){
						if(school[i].equals(cell4.getContents())==true &&
								course[j].equals(cell1.getContents())==true){
							sheetcode = i*course.length + j;
							break;
						}
					}					
				}				
				
				outsheet = outbook.getSheet(sheetcode); 
				outsheet.addCell(new Label(0, rowsforeachsheet[sheetcode], cell0.getContents()));
				outsheet.addCell(new Label(1, rowsforeachsheet[sheetcode], cell1.getContents()));
				outsheet.addCell(new Label(2, rowsforeachsheet[sheetcode], cell2.getContents()));
				outsheet.addCell(new Label(3, rowsforeachsheet[sheetcode], cell3.getContents()));
				outsheet.addCell(new Label(4, rowsforeachsheet[sheetcode], cell4.getContents()));
				rowsforeachsheet[sheetcode]++;
				row++;
			}
			
			 //写入数据
            outbook.write(); 
            //关闭文件
            outbook.close(); 
        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
