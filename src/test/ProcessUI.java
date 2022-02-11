package test;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class ProcessUI extends JFrame{
	static Container con;
	static Clock clock = new Clock();
	static JLabel[] queueLabels={new JLabel("就绪队列"),new JLabel("阻塞队列1"),new JLabel("阻塞队列2"),new JLabel("阻塞队列3")};
	static JTextArea[] queueText;  //队列文本框
	
	static JScrollPane messageScrollPane;  //消息文本框滚动条
	static JTextArea messageText;  //消息文本框
	static JLabel messageLabel = new JLabel("事件消息");
	
	static JScrollPane processCreateScrollPane;  //进程创建退出信息文本框滚动条
	static JTextArea processCreateText;  //进程创建退出信息文本框
	static JLabel processCreateLabel = new JLabel("进程完成情况");
	
	static JPanel CPUPane;  //CPU状态区域
	static JLabel CPULabel = new JLabel("CPU状态信息");
	static String[] strs = {"运行时间：","psw状态：","关中断状态：","当前运行进程：","当前运行指令：","指令状态："};
	static JLabel[] labels = new JLabel[6];
	static JTextField[] texts = new JTextField[6];
	
	static JPanel devicePane;  //设备使用区域
	static JLabel deviceLabel = new JLabel("设备使用情况");
	static String[] deviceStrs = {"键盘设备","屏幕设备","磁盘设备"};
	static JLabel[] deviceLabels = new JLabel[3];
	static JTextField[] deviceText = new JTextField[3];
	
	static JPanel buttonPane;   //按钮区域
	static String[] buttonStrs = {"开始","暂停","继续","创建新进程","关闭"};
	static JButton[] buttons = new JButton[5];
	
	ProcessSchedule ps;
	JobsRequest jr;
	static boolean pause = false;  //暂停信号
	
	public ProcessUI() {
		super("进程低级调度");
		queueText = new JTextArea[10];
		initFrame();
	}
	public void initFrame() {
		con = this.getContentPane();
		setFrame();
		setQueueTextArea(800,0);
		setMessageTextArea(0,0);
		setProcessCreateTextArea(0,450);
		setCPUArea(800, 250);
		setDeviceArea(800, 500);
		setButtonArea(0, 780);
	}
	public void setFrame() {
		this.setLayout(null);
		this.setBounds(100, 100, 1600, 900);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	public void setQueueTextArea(int x,int y) {  //设置队列显示位置
		for(int i=0;i<4;i++) {
			queueLabels[i].setBounds(x+ 20+i*150, y+20, 100, 20);
			con.add(queueLabels[i]);
			queueText[i] = new JTextArea();
			queueText[i].setBounds(x+ 20+i*150, y+50, 100, 200);
			queueText[i].setEditable(false);
			queueText[i].setFont(new Font("宋体", 0, 18));
			con.add(queueText[i]);
		}
	}
	public static void addMessage(String s) {  //在消息框中加入消息
		messageText.append(s+"\r\n");
		messageText.setCaretPosition(messageText.getText().length());  //将光标定位到最后一个字符，使界面能显示出最新输出的信息
	}
	public static void addProcessMessage(String s)  {  //在进程完成情况中加入信息
		processCreateText.append(s+"\r\n");
		processCreateText.setCaretPosition(processCreateText.getText().length());
	}
	public static void addQueueMessage(String s,int queueID) {  //在队列中显示信息，queueID为显示信息的队列ID
		queueText[queueID].append(s+"\r\n");
	}
	public static void clearQueueMessage(int queueID) {  //清空队列信息
		queueText[queueID].setText("");
	}
	public static void setRunningProcessMessage()  {  //设置运行进程信息和运行指令信息
		if(ProcessSchedule.getRunningProcess()==null) {
			texts[3].setText("");
			texts[4].setText("");
			texts[5].setText("");
		} else {
			texts[3].setText(String.valueOf(ProcessSchedule.getRunningProcess().getPcb().getPro_ID()));
			texts[4].setText(String.valueOf(ProcessSchedule.getRunningProcess().getPcb().getInstruc().getInstruc_ID()));
			texts[5].setText(String.valueOf(ProcessSchedule.getRunningProcess().getPcb().getInstruc().getInstruc_state()));
		}
	}
	public static void setCPUpswMessage(int psw) {  //0为内核态，1为用户态
		if(psw==0) {
			texts[1].setText("内核态");
		} else {
			texts[1].setText("用户态");
		}
	}
	public static void setCPUbreakMessage(String s) {  //设置关中断信息状态
		texts[2].setText(s);
	}
	public static void setDeviceMessage() {  //设置设备信息
		if(ProcessSchedule.BlockQ1.size()>0) {
			deviceText[0].setText("进程"+ProcessSchedule.BlockQ1.get(0).getPcb().getPro_ID());
		} else {
			deviceText[0].setText("");
		}
		if(ProcessSchedule.BlockQ2.size()>0) {
			deviceText[1].setText("进程"+ProcessSchedule.BlockQ2.get(0).getPcb().getPro_ID());
		} else {
			deviceText[1].setText("");
		}
		if(ProcessSchedule.BlockQ3.size()>0) {
			deviceText[2].setText("进程"+ProcessSchedule.BlockQ3.get(0).getPcb().getPro_ID());
		} else {
			deviceText[2].setText("");
		}
	}
	public void setMessageTextArea(int x,int y) {  //设置消息显示位置
		messageText = new JTextArea();
		messageText.setFont(new Font("宋体", 0, 18));
		messageText.setEditable(false);
		messageScrollPane = new JScrollPane(messageText);
		messageScrollPane.setBounds(x+20,y+50, 700, 450);
		messageLabel.setBounds(x+20,y+20, 100, 20);
		con.add(messageScrollPane);
		con.add(messageLabel);
	}
	public void setProcessCreateTextArea(int x,int y) {  //设置进程创建退出信息显示位置
		processCreateText = new JTextArea();
		processCreateText.setFont(new Font("宋体", 0, 18));
		processCreateText.setEditable(false);
		processCreateScrollPane = new JScrollPane(processCreateText);
		processCreateScrollPane.setBounds(x+20,y+100, 700, 200);
		processCreateLabel.setBounds(x+20,y+70, 100, 20);
		con.add(processCreateScrollPane);
		con.add(processCreateLabel);
	}
	public void setCPUArea(int x,int y) {  //设置CPU状态信息的位置
		CPULabel.setBounds(x+20, y+20, 100, 20);  //设置画布
		con.add(CPULabel);
		CPUPane = new JPanel();
		CPUPane.setBackground(Color.ORANGE);
		CPUPane.setLayout(null);
		CPUPane.setBounds(x+20, y+50, 600, 200);
		
		int position = 0,height = 0;
		for(int i=0;i<texts.length;i++) {
			if(i<=2) {
				position = 20;
				height = i;
			} else {
				position = 300;
				height = i-3;
			}
			labels[i] = new JLabel(strs[i]);
			labels[i].setBounds(position, 20+60*height, 100, 20);
			CPUPane.add(labels[i]);
			texts[i] = new JTextField();
			texts[i].setEditable(false);
			texts[i].setBounds(position+100, 20+60*height, 100, 20);
			
			
			CPUPane.add(texts[i]);
		}
		texts[0].setText(String.valueOf(clock.getTime()));
		texts[2].setText("开中断");
		con.add(CPUPane);
	}
	
	public void setDeviceArea(int x,int y) {  //设置设备使用信息区域
		deviceLabel.setBounds(x+20, y+20, 100, 20);
		con.add(deviceLabel);
		devicePane = new JPanel();
		devicePane.setBackground(Color.PINK);
		devicePane.setLayout(null);
		devicePane.setBounds(x+20, y+50, 600, 200);
		
		for(int i=0;i<deviceStrs.length;i++) {
			deviceLabels[i] = new JLabel(deviceStrs[i]);
			deviceLabels[i].setBounds(70+i*200, 20, 100, 20);
			devicePane.add(deviceLabels[i]);
			deviceText[i] = new JTextField();
			deviceText[i].setEditable(false);
			deviceText[i].setBounds(50+i*200, 60, 100, 20);
			devicePane.add(deviceText[i]);
		}
		con.add(devicePane);
	}
	public void setButtonArea(int x,int y) {  //设置按钮区域位置
		buttonPane = new JPanel();
		buttonPane.setLayout(null);
		buttonPane.setBounds(x, y, 1600, 50);
		for(int i=0;i<buttons.length;i++) {
			buttons[i] = new JButton(buttonStrs[i]);
			buttons[i].setBounds(500+i*150, 0, 100, 40);
			buttonPane.add(buttons[i]);
		}
		con.add(buttonPane);
		
		//为按钮添加监听器
		//开始按钮
		buttons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ps = new ProcessSchedule();
				jr = new JobsRequest();
				ps.start();
				jr.start();
			}
		});
		
		//暂停按钮
		buttons[1].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				ps.suspend();
				jr.suspend();
			}
			
		});
		
		//继续按钮
		buttons[2].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ps.resume();
				jr.resume();
			}
		});
		
		//关闭按钮
		buttons[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pause = true;
				dispose();
			}
		});
		
		//创建进程按钮
		buttons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JobsRequest.addJob(JobsRequest.createANewJob(20));
			}
		});
		
	}
	public static void OutputMessage() {
		if(CPU.getIr()==null) {
			System.out.println(clock.getTime()+":[空闲]");  //输出控制台
			IOFile.writeMessageInData(clock.getTime()+":[空闲]");  //输出字符串数据
			addMessage(clock.getTime()+":[空闲]");  //输出到图形化界面
			setRunningProcessMessage();
			
		} else {
			String s = clock.getTime()+":[运行进程"+ProcessSchedule.getRunningProcess().getPcb().getPro_ID()+"指令"+CPU.getIr().getInstruc_ID()+"类型"+CPU.getIr().getInstruc_state()+"]";
			System.out.println(s);
			IOFile.writeMessageInData(s);
			addMessage(s);
			setRunningProcessMessage();
		}
		StringBuffer ss = new StringBuffer(clock.getTime()+":[就绪队列1");
		clearQueueMessage(0);
		for(int i=0;i<ProcessSchedule.ReadyQ.size();i++) {
			ss = ss.append(" 进程"+ProcessSchedule.ReadyQ.get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.ReadyQ.get(i).getPcb().getPro_ID(), 0);
		}
		ss = ss.append("]");
		System.out.println(ss.toString());
		IOFile.writeMessageInData(ss.toString());
		addMessage(ss.toString());
		
		StringBuffer sq1 = new StringBuffer(clock.getTime()+":[阻塞队列1");
		clearQueueMessage(1);
		for(int i=0;i<ProcessSchedule.BlockQ1.size();i++) {
			sq1.append(" 进程"+ProcessSchedule.BlockQ1.get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.BlockQ1.get(i).getPcb().getPro_ID(), 1);
		}
		sq1.append("]");
		System.out.println(sq1.toString());
		IOFile.writeMessageInData(sq1.toString());
		addMessage(sq1.toString());
		
		StringBuffer sq2 = new StringBuffer(clock.getTime()+":[阻塞队列2");
		clearQueueMessage(2);
		for(int i=0;i<ProcessSchedule.BlockQ2.size();i++) {
			sq2.append(" 进程"+ProcessSchedule.BlockQ2.get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.BlockQ2.get(i).getPcb().getPro_ID(), 2);
		}
		sq2.append("]");
		System.out.println(sq2.toString());
		IOFile.writeMessageInData(sq2.toString());
		addMessage(sq2.toString());
		
		StringBuffer sq3 = new StringBuffer(clock.getTime()+":[阻塞队列3");
		clearQueueMessage(3);
		for(int i=0;i<ProcessSchedule.BlockQ3.size();i++) {
			sq3.append(" 进程"+ProcessSchedule.BlockQ3.get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.BlockQ3.get(i).getPcb().getPro_ID(), 3);
		}
		sq3.append("]");
		System.out.println(sq3.toString());
		IOFile.writeMessageInData(sq3.toString());
		addMessage(sq3.toString());
		setDeviceMessage();
	}
	public static void main(String[] args) {
		ProcessUI pui = new ProcessUI();
		pui.setVisible(true);
		IOFile.ReadJobsList();
		IOFile.initWriteData();
	}
}
