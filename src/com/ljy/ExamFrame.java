/*
 * ExamFrame.java
 * @author LiJingYi
 * @email jingyiliscut@gmail.com
 */
package com.ljy;
import java.awt.*; 
import java.awt.event.*; 

public class ExamFrame  extends Frame implements ActionListener { 
	FileDialog open = new FileDialog(this, "���ļ�", FileDialog.LOAD); 
	String fileName; 
	Button b_dakai = new Button("ѡ���ļ�"); 
	TextArea text = new TextArea(); 
	
	public ExamFrame() { 
		super("���԰��ų��� By LiJingYi"); 
		setBounds(200, 100, 500, 150); 
		b_dakai.addActionListener(this); 
		add(text); 
		add(b_dakai,"North"); 
		setVisible(true); 
	} 

	public void actionPerformed(ActionEvent e) { 
		if (e.getSource() == b_dakai) { 
		open.show(); 
		text.setText("��ѡ�������ļ���");
		String dir = open.getDirectory();
		fileName = open.getFile(); 
			if (fileName != null && dir != null) { 
				//System.out.println(dir+fileName);
				MainDrive test = new MainDrive();
				test.handleEast(fileName);
				test.handleSouth(fileName);
				test.handleZhuhai(fileName);
				text.setText("���");
			} else{
				text.setText("��ѡ���ļ���");
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
