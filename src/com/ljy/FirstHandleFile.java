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
	public String school[] = {"�麣У��","��У��","��У��"};
	public String course[] = {"���˼�������ԭ��","ë��˼����й���ɫ�������������ϵ����",
						"˼����������뷨�ɻ���","�й����ִ�ʷ��Ҫ"};
	
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

		//˳���գ��麣����У����У
		//ÿ��У��˳����  1���˼�������ԭ�� 2ë�� 3˼����������뷨�ɻ��� 4�й����ִ�ʷ��Ҫ
		int sheetcode = -1; //��¼output�ļ���sheet����0-11��ÿ��sheet��һ���������
		int SHEET_ZERO = 0;
		Sheet insheet;
		WritableSheet outsheet;
		Workbook inbook;
		WritableWorkbook outbook;
		Cell cell0,cell1,cell2,cell3,cell4;//����Ϊ��ѧ��ţ��γ�������ʦ��������У��
		try{
			inbook = Workbook.getWorkbook(new File(inputFile));
			insheet = inbook.getSheet(SHEET_ZERO);
			outbook = Workbook.createWorkbook(new File(outputFile));
			for(int i=0;i<school.length*course.length;i++){
				outbook.createSheet(""+i,i);
			}
			//��¼ÿ��sheetĿǰ���뵽������
			int rowsforeachsheet[] = new int[school.length*course.length];
			int row = 0;
			
			while(row < insheet.getRows()){
				cell0 = insheet.getCell(0, row);//(�У���) ��ѧ���
				cell1 = insheet.getCell(1, row);//�γ���
				cell2 = insheet.getCell(2, row);//��ʦ
				cell3 = insheet.getCell(3, row);//����
				cell4 = insheet.getCell(4, row);//У��
				
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
			
			 //д������
            outbook.write(); 
            //�ر��ļ�
            outbook.close(); 
        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
