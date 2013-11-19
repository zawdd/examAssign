/*
 * ExamFrame.java
 * @author LiJingYi
 * @email jingyiliscut@gmail.com
 */
package com.ljy;
import java.awt.*; 
import java.awt.event.*; 

public class ExamFrame  extends Frame implements ActionListener { 
	FileDialog open = new FileDialog(this, "打开文件", FileDialog.LOAD); 
	String fileName; 
	Button b_dakai = new Button("选择文件"); 
	TextArea text = new TextArea(); 
	
	public ExamFrame() { 
		super("考试安排程序 By LiJingYi"); 
		setBounds(200, 100, 500, 150); 
		b_dakai.addActionListener(this); 
		add(text); 
		add(b_dakai,"North"); 
		setVisible(true); 
	} 

	public void actionPerformed(ActionEvent e) { 
		if (e.getSource() == b_dakai) { 
		open.show(); 
		text.setText("请选择输入文件！");
		String dir = open.getDirectory();
		fileName = open.getFile(); 
			if (fileName != null && dir != null) { 
				//System.out.println(dir+fileName);
				MainDrive test = new MainDrive();
				test.handleEast(fileName);
				test.handleSouth(fileName);
				test.handleZhuhai(fileName);
				text.setText("完成");
			} else{
				text.setText("请选择文件！");
			}
		} 
	} 
	public static void main(String args[]) { 
	new ExamFrame().addWindowListener(new WindowAdapter() { 
		public void windowClosing(WindowEvent e) { 
			System.exit(0); 
		}; 
	}); 
	 
	} 
}
