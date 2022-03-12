package ui;

import hardware.CPU;
import hardware.Clock;
import hardware.RAM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import other.IOFile;
import other.OSManage;

import kernel.*;
import kernel.Process;



@SuppressWarnings("serial")
public class ProcessUI extends JFrame implements Runnable {
	
	OSManage om;
	
	static Container con;
	static Clock clock = new Clock();
	static JLabel[] queueLabels={new JLabel("就绪队列"),new JLabel("阻塞队列1"),new JLabel("阻塞队列2"),new JLabel("阻塞队列3"),new JLabel("阻塞队列4"),new JLabel("阻塞队列5"),new JLabel("作业后备队列"),new JLabel("完成队列") };
	static JTextArea[] queueText;  //队列文本框
	
	static JScrollPane messageScrollPane;  //消息文本框滚动条
	static JTextArea messageText;  //消息文本框
	static JLabel messageLabel = new JLabel("事件消息");
	
	static JPanel pageItemPane;
	static JLabel pageItemLabel = new JLabel("页表内容");
	static JTable pageTable;
    static DefaultTableModel pageTableInfo;
	static JScrollPane pageItemScrollPane;  //页表信息文本框滚动条
	
	static JPanel TLBPane;
	static JLabel TLBLabel = new JLabel("快表内容");
	static JTable TLBTable;
    static DefaultTableModel TLBTableInfo;
	static JScrollPane TLBScrollPane;  //快表信息文本框滚动条
	
	static JPanel CPUPane;  //CPU状态区域
	static JLabel CPULabel = new JLabel("CPU状态信息");
	static String[] CPUstrs = {"运行时间：","CPU状态："};
	static JLabel[] CPUlabels = new JLabel[2];
	static JTextField[] CPUtexts = new JTextField[2];
	
	static JPanel runningPane;  //设备使用区域
	static JLabel runningLabel = new JLabel("当前运行进程情况");
	static String[] runningStrs = {"进程ID","进程优先级","IR","进程指令数","指令类型","PC"};
	static JLabel[] runningLabels = new JLabel[6];
	static JTextField[] runningText = new JTextField[6];
	
	static JPanel buttonPane;   //按钮区域
	static String[] buttonStrs = {"开始","暂停","继续","创建新进程","关闭"};
	static JButton[] buttons = new JButton[5];
	
	static JPanel memPane;   //内存区域
	static JLabel memLabel = new JLabel("内存块使用情况0-31");
	static JLabel[] memBlock;  //内存块表示，"■" 表示已占用，"□"表示未占用
	
	
	static boolean pause = false;  //暂停信号
	
	public ProcessUI() {
		super("进程低级调度");
		
		om = new OSManage();
		
		queueText = new JTextArea[10];
		initFrame();
	}
	public void initFrame() {
		con = this.getContentPane();
		setFrame();
		setQueueTextArea(700,0);
		setMessageTextArea(0,0);
		setPageItemTable(0,450);
		setTLBTable(350,450);
		setCPUArea(700, 250);
		setRunningArea(700, 500);
		setMemArea(1300,250);
		
		setButtonArea(0, 780);
	}
	public void setFrame() {
		this.setLayout(null);
		this.setBounds(100, 100, 1850, 900);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	public void setQueueTextArea(int x,int y) {  //设置队列显示位置

		for(int i=0;i<8;i++) {
			queueLabels[i].setBounds(x+ 20+i*100, y+20, 80, 20);
			con.add(queueLabels[i]);
			queueText[i] = new JTextArea();
			queueText[i].setBounds(x+ 20+i*100, y+50, 80, 200);
			queueText[i].setEditable(false);
			queueText[i].setFont(new Font("宋体", 0, 18));
			con.add(queueText[i]);
		}
	}
	
	public static boolean isPause() {
		return pause;
	}
	
	
	public static void addMessage(String s) {  //在消息框中加入消息
		messageText.append(s+"\r\n");
		messageText.setCaretPosition(messageText.getText().length());  //将光标定位到最后一个字符，使界面能显示出最新输出的信息
	}
//	public static void addProcessMessage(String s)  {  //在进程完成情况中加入信息
//		pageItemText.append(s+"\r\n");
//		pageItemText.setCaretPosition(processCreateText.getText().length());
//	}
	
	
	
	public static void setCPUpswMessage() {  //0为内核态，1为用户态
		if(CPU.getPsw()==0) {
			CPUtexts[1].setText("内核态");
		} else {
			CPUtexts[1].setText("用户态");
		}
	}
	
	public void setMessageTextArea(int x,int y) {  //设置消息显示位置
		messageText = new JTextArea();
		messageText.setFont(new Font("宋体", 0, 18));
		messageText.setEditable(false);
		messageScrollPane = new JScrollPane(messageText);
		messageScrollPane.setBounds(x+20,y+50, 650, 450);
		messageLabel.setBounds(x+20,y+20, 100, 20);
		con.add(messageScrollPane);
		con.add(messageLabel);
	}
	public void setPageItemTable(int x,int y) {  //设置页表信息
		// 页表
		pageItemPane = new JPanel();
		pageItemPane.setBounds(x+20, y+100, 300, 200);
		pageItemPane.setLayout(new BorderLayout());
		
        String[] pageTableHeader = new String[]{"逻辑页号", "内存框号", "外存块号","调入次数"};
        pageTableInfo = new DefaultTableModel(new String[][]{}, pageTableHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 关闭单元格编辑
                return false;
            }
        };
        pageTable = new JTable(pageTableInfo);
        pageTable.getTableHeader().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        pageTable.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        
        
        pageItemPane.add(pageTable.getTableHeader(), BorderLayout.NORTH);
        
        
        
        pageItemScrollPane = new JScrollPane(pageTable);
        pageItemScrollPane.setBounds(0, 0, 300, 200);
        
        pageItemPane.add(pageItemScrollPane);
        pageItemLabel.setBounds(x+20, y+70, 100, 20);
        con.add(pageItemLabel);
        con.add(pageItemPane);
	}
	public void setTLBTable(int x,int y) {  //设置页表信息
		// 页表
		TLBPane = new JPanel();
		TLBPane.setBounds(x+20, y+100, 300, 200);
		TLBPane.setLayout(new BorderLayout());
		
        String[] TLBHeader = new String[]{"进程归属","逻辑页号", "内存框号","调入次数"};
        TLBTableInfo = new DefaultTableModel(new String[][]{}, TLBHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 关闭单元格编辑
                return false;
            }
        };
        TLBTable = new JTable(TLBTableInfo);
        TLBTable.getTableHeader().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        TLBTable.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        
        
        TLBPane.add(TLBTable.getTableHeader(), BorderLayout.NORTH);
        
        
        
        TLBScrollPane = new JScrollPane(TLBTable);
        TLBScrollPane.setBounds(0, 0, 300, 200);
        
        TLBPane.add(TLBScrollPane);
        TLBLabel.setBounds(x+20, y+70, 100, 20);
        con.add(TLBLabel);
        con.add(TLBPane);
	}
	public void setCPUArea(int x,int y) {  //设置CPU状态信息的位置
		CPULabel.setBounds(x+20, y+20, 100, 20);  //设置画布
		con.add(CPULabel);
		CPUPane = new JPanel();
		CPUPane.setBackground(Color.ORANGE);
		CPUPane.setLayout(null);
		CPUPane.setBounds(x+20, y+50, 550, 200);
		
		int position = 0,height = 0;
		for(int i=0;i<CPUtexts.length;i++) {
			if(i<=2) {
				position = 20;
				height = i;
			} else {
				position = 300;
				height = i-3;
			}
			CPUlabels[i] = new JLabel(CPUstrs[i]);
			CPUlabels[i].setBounds(position, 20+60*height, 100, 20);
			CPUPane.add(CPUlabels[i]);
			CPUtexts[i] = new JTextField();
			CPUtexts[i].setEditable(false);
			CPUtexts[i].setBounds(position+100, 20+60*height, 100, 20);
			
			
			CPUPane.add(CPUtexts[i]);
		}
		CPUtexts[0].setText(String.valueOf(clock.getTime()));
		con.add(CPUPane);
	}
	
	public void setRunningArea(int x,int y) {  //设置正在运行进程区域
		runningLabel.setBounds(x+20, y+20, 120, 20);
		con.add(runningLabel);
		runningPane = new JPanel();
		runningPane.setBackground(Color.PINK);
		runningPane.setLayout(null);
		runningPane.setBounds(x+20, y+50, 550, 200);
		
		for(int i=0;i<runningStrs.length;i++) {
			runningLabels[i] = new JLabel(runningStrs[i]);
			runningLabels[i].setBounds(50+i%3*150, 20+(i/3)*100, 100, 20);
			runningPane.add(runningLabels[i]);
			runningText[i] = new JTextField();
			runningText[i].setEditable(false);
			runningText[i].setBounds(50+i%3*150, 40+(i/3)*100, 100, 20);
			runningPane.add(runningText[i]);
		}
		con.add(runningPane);
	}
	
	public void setMemArea(int x,int y) {  //设置内存块使用情况区域
		memLabel.setBounds(x+20, y+20, 200, 20);
		con.add(memLabel);
		memPane = new JPanel();
		memPane.setBounds(x+20, y+50, 500, 200);
		memPane.setBackground(Color.PINK);
		memPane.setLayout(null);
		con.add(memPane);
		
		//内存块初始化
		memBlock = new JLabel[RAM.PAGE_NUM];
		for(int i=0;i<RAM.PAGE_NUM;i++) {
			memBlock[i] = new JLabel();
			memBlock[i].setBounds(i%8*60+20,(i/8)*50,30,30);
			memBlock[i].setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
			memPane.add(memBlock[i]);
		}
		refreshMemArea();

		
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
				om.runSystem();
			}
		});
		
		//暂停按钮
		buttons[1].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				//ps.suspend();
				//jr.suspend();
			}
			
		});
		
		//继续按钮
		buttons[2].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//ps.resume();
				//jr.resume();
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
				//JobsRequest.addJob(JobsRequest.createANewJob(20));
			}
		});
		
	}
	public static void OutputMessage() {
		if(CPU.getIr()==null) {
			System.out.println(clock.getTime()+":[空闲]");  //输出控制台
			IOFile.writeMessageInData(clock.getTime()+":[空闲]");  //输出字符串数据
			addMessage(clock.getTime()+":[空闲]");  //输出到图形化界面
			//setRunningProcessMessage();
			
		} else {
			String s = clock.getTime()+":[运行进程"+ProcessSchedule.getRunningProcess().getPcb().getPro_ID()+"指令"+CPU.getIr().getInstruc_ID()+"类型"+CPU.getIr().getInstruc_state()+"]";
			System.out.println(s);
			IOFile.writeMessageInData(s);
			addMessage(s);
			//setRunningProcessMessage();
		}
		StringBuffer ss = new StringBuffer(clock.getTime()+":[就绪队列1");
		clearQueueMessage(0);
		for(int i=0;i<ProcessSchedule.getReadyQ().size();i++) {
			ss = ss.append(" 进程"+ProcessSchedule.getReadyQ().get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.getReadyQ().get(i).getPcb().getPro_ID(), 0);
		}
		ss = ss.append("]");
		System.out.println(ss.toString());
		IOFile.writeMessageInData(ss.toString());
		addMessage(ss.toString());
		
		StringBuffer sq1 = new StringBuffer(clock.getTime()+":[阻塞队列1");
		clearQueueMessage(1);
		for(int i=0;i<ProcessSchedule.getBlockQ1().size();i++) {
			sq1.append(" 进程"+ProcessSchedule.getBlockQ1().get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.getBlockQ1().get(i).getPcb().getPro_ID(), 1);
		}
		sq1.append("]");
		System.out.println(sq1.toString());
		IOFile.writeMessageInData(sq1.toString());
		addMessage(sq1.toString());
		
		StringBuffer sq2 = new StringBuffer(clock.getTime()+":[阻塞队列2");
		clearQueueMessage(2);
		for(int i=0;i<ProcessSchedule.getBlockQ2().size();i++) {
			sq2.append(" 进程"+ProcessSchedule.getBlockQ2().get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.getBlockQ2().get(i).getPcb().getPro_ID(), 2);
		}
		sq2.append("]");
		System.out.println(sq2.toString());
		IOFile.writeMessageInData(sq2.toString());
		addMessage(sq2.toString());
		
		StringBuffer sq3 = new StringBuffer(clock.getTime()+":[阻塞队列3");
		clearQueueMessage(3);
		for(int i=0;i<ProcessSchedule.getBlockQ3().size();i++) {
			sq3.append(" 进程"+ProcessSchedule.getBlockQ3().get(i).getPcb().getPro_ID());
			addQueueMessage("进程"+ProcessSchedule.getBlockQ3().get(i).getPcb().getPro_ID(), 3);
		}
		sq3.append("]");
		System.out.println(sq3.toString());
		IOFile.writeMessageInData(sq3.toString());
		addMessage(sq3.toString());
		//setDeviceMessage();
	}
	
	//刷新整个页面的状态信息
	public void refresh() {
		//刷新所有队列
		refreshQueue(); 
		
		//刷新当前运行进程
		refreshRunning();
		
		//刷新CPU
		refreshCPU();
		
		//刷新内存
		refreshMemArea();
		
		//刷新页表
		refreshPageTable();
		
		//刷新TLB
		refreshTLBTable();
	}
	
	//刷新内存区域
	public synchronized void refreshMemArea() {
		for(int i=0;i<RAM.PAGE_NUM;i++) {
			if(RAM.getAllBlocks()[i] == true) {
				memBlock[i].setText("■");
			} else {
				memBlock[i].setText("□");
			}
		}
		
	}
	
	//队列信息的基本操作
	public synchronized static void addQueueMessage(String s,int queueID) {  //在队列中显示信息，queueID为显示信息的队列ID
		queueText[queueID].append(s+"\r\n");
	}
	public synchronized static void clearQueueMessage(int queueID) {  //清空队列信息
		queueText[queueID].setText("");
	}
	//刷新各种队列
	public synchronized void refreshQueue() {
		for(int i=0;i<8;i++) {
			clearQueueMessage(i);
		}
		for(int k=0;k<ProcessSchedule.getReadyQ().size();k++) {
			addQueueMessage("进程"+ProcessSchedule.getReadyQ().get(k).getPcb().getPro_ID(),0);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ1().size();k++) {
			addQueueMessage("进程"+ProcessSchedule.getBlockQ1().get(k).getPcb().getPro_ID(),1);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ2().size();k++) {
			addQueueMessage("进程"+ProcessSchedule.getBlockQ2().get(k).getPcb().getPro_ID(),2);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ3().size();k++) {
			addQueueMessage("进程"+ProcessSchedule.getBlockQ3().get(k).getPcb().getPro_ID(),3);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ4().size();k++) {
			addQueueMessage("进程"+ProcessSchedule.getBlockQ4().get(k).getPcb().getPro_ID(),4);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ5().size();k++) {
			addQueueMessage("进程"+ProcessSchedule.getBlockQ5().get(k).getPcb().getPro_ID(),5);
		}
		for(int k=0;k<ProcessSchedule.getReverseQ().size();k++) {
			addQueueMessage("作业"+ProcessSchedule.getReverseQ().get(k).getJobsID(),6);
		}
		for(int k=0;k<ProcessSchedule.getFinishedQ().size();k++) {
			addQueueMessage("进程"+ProcessSchedule.getFinishedQ().get(k).getPcb().getPro_ID(),7);
		}
	}
	//刷新CPU状态
	public synchronized void refreshCPU() {
		//设置当前时钟信息
		CPUtexts[0].setText(ProcessSchedule.getClock().getTime()+"");
		//设置CPU运行状态
		setCPUpswMessage();
	}
	
	
	//刷新当前运行进程状态
	public void refreshRunning() {
		Process p = ProcessSchedule.getRunningProcess();
		if(p==null) {
			return;
		}
		synchronized(p) {
			if(p!=null) {
				runningText[0].setText(p.getPcb().getPro_ID()+"");
				runningText[1].setText(p.getPcb().getPriority()+"");
				runningText[2].setText(p.getPcb().getInstruc().getInstruc_ID()+"");
				runningText[3].setText(p.getPcb().getInstrucNum()+"");
				runningText[4].setText(p.getPcb().getInstruc().getInstruc_state()+"");
				runningText[5].setText(p.getPcb().getPc()+"");
			} else {
				for(int i=0;i<runningStrs.length;i++) {
					runningText[i].setText("无");
				}
			}
		}
	}
	
	//刷新页表
	public void refreshPageTable() {
		pageTableInfo.setRowCount(0);
		for(int i=0;i<ProcessSchedule.getPcbList().size();i++) {
			for(int j=0;j<ProcessSchedule.getPcbList().get(i).getPage_items_num();j++) {
				Vector<String> v = new Vector<String>();
				v.add("进程"+ProcessSchedule.getPcbList().get(i).getPro_ID()+":"+ProcessSchedule.getPcbList().get(i).getPage_items()[j].getLogicPageNo());
				v.add(ProcessSchedule.getPcbList().get(i).getPage_items()[j].getPageFrameNo()+"");
				v.add(ProcessSchedule.getPcbList().get(i).getPage_items()[j].getDiskBlockNo()+"");
				v.add(ProcessSchedule.getPcbList().get(i).getPage_items()[j].getInMemTimes()+"");
				pageTableInfo.addRow(v);
			}
			
		}
	}
	
	public synchronized void refreshTLBTable() {
		TLBTableInfo.setRowCount(0);
		for(int i=0;i<ProcessSchedule.getPcbList().size();i++) {
			for(int j=0;j<ProcessSchedule.getPcbList().get(i).getPage_frame_nums().length;j++) {
				Vector<Integer> v = new Vector<Integer>();
				v.add(ProcessSchedule.getPcbList().get(i).getPro_ID());
				v.add(ProcessSchedule.getPcbList().get(i).getPage_frame_nums()[j].getLogic_no());
				v.add(ProcessSchedule.getPcbList().get(i).getPage_frame_nums()[j].getPage_frame_no());
				v.add(ProcessSchedule.getPcbList().get(i).getPage_frame_nums()[j].getInMemTimes());
				TLBTableInfo.addRow(v);
			}
			
		}
	}
	
	
	@Override
	public void run() {
		
		while(true) {
			refresh();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String[] args) {
		
		ProcessUI pui = new ProcessUI();
		
		Thread t = new Thread(pui);
		
		pui.setVisible(true);
		
		t.start();
	}
}
