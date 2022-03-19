package ui;

import hardware.CPU;
import hardware.Clock;
import hardware.RAM;
import hardware.ROM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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
	static JLabel[] queueLabels={new JLabel("��������"),new JLabel("��������1"),new JLabel("��������2"),new JLabel("��������3"),new JLabel("��������4"),new JLabel("��������5"),new JLabel("��ҵ�󱸶���"),new JLabel("��ɶ���") };
	static JTextArea[] queueText;  //�����ı���
	static JScrollPane[] queueScrollPane;  //���й�����
	
	static JScrollPane messageScrollPane;  //��Ϣ�ı��������
	static JTextArea messageText;  //��Ϣ�ı���
	static JLabel messageLabel = new JLabel("�¼���Ϣ");
	
	static JPanel pageItemPane;
	static JLabel pageItemLabel = new JLabel("ҳ������");
	static JTable pageTable;
    static DefaultTableModel pageTableInfo;
	static JScrollPane pageItemScrollPane;  //ҳ����Ϣ�ı��������
	
	static JPanel TLBPane;
	static JLabel TLBLabel = new JLabel("�������");
	static JTable TLBTable;
    static DefaultTableModel TLBTableInfo;
	static JScrollPane TLBScrollPane;  //�����Ϣ�ı��������
	
	static JPanel CPUPane;  //CPU״̬����
	static JLabel CPULabel = new JLabel("CPU״̬��Ϣ");
	static String[] CPUstrs = {"����ʱ�䣺","CPU״̬��"};
	static JLabel[] CPUlabels = new JLabel[2];
	static JTextField[] CPUtexts = new JTextField[2];
	
	static JPanel runningPane;  //�豸ʹ������
	static JLabel runningLabel = new JLabel("��ǰ���н������");
	static String[] runningStrs = {"����ID","�������ȼ�","IR","����ָ����","ָ������","PC"};
	static JLabel[] runningLabels = new JLabel[6];
	static JTextField[] runningText = new JTextField[6];
	
	static JPanel buttonPane;   //��ť����
	static String[] buttonStrs = {"��ʼ","��ͣ","����","�����½���","�ر�"};
	static JButton[] buttons = new JButton[5];
	
	static JPanel memPane;   //�ڴ�����
	static JLabel memLabel = new JLabel("�ڴ��ʹ�����0-31��0-23���û�����24-31�ǻ�����");
	static JLabel[] memBlock;  //�ڴ���ʾ��"��" ��ʾ��ռ�ã�"��"��ʾδռ��
	static JLabel[] memNo;    //�ڴ�����
	
	static JPanel romPane;   //�������
	static JLabel romLabel = new JLabel("��潻����ʹ�����");
	static JScrollPane romScrollPane;  //��������
	static JPanel labelPane;   //�������ڲ��ķ���label��panel
	static JLabel[] romNumLabel;   //�����������
	static JLabel[] romUsedLabel;  //��������ʹ�����
	
	
	public ProcessUI() {
		super("Operating System");
		
		om = new OSManage();
		
		initFrame();
	}
	
	public static Clock getClock() {
		return clock;
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
		setRomArea(1300,500);
		
		
		setButtonArea(100, 780);
		
		this.setVisible(true);
	}
	public void setFrame() {
		this.setLayout(null);
		this.setBounds(50, 50, 1850, 900);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	public void setQueueTextArea(int x,int y) {  //���ö�����ʾλ��

		queueText = new JTextArea[8];
		queueScrollPane = new JScrollPane[8];
		for(int i=0;i<8;i++) {
			queueLabels[i].setBounds(x+ 20+i*140, y+20, 80, 20);
			con.add(queueLabels[i]);
			
			queueText[i] = new JTextArea();
			queueText[i].setEditable(false);
			queueText[i].setFont(new Font("����", 0, 18));
			
			queueScrollPane[i] = new JScrollPane(queueText[i]);
			queueScrollPane[i].setBounds(x+ 20+i*140, y+50, 120, 200);
			con.add(queueScrollPane[i]);
		}
		
		
	}
	
	
	
	public static void addMessage(String s) {  //����Ϣ���м�����Ϣ
		messageText.append(s+"\r\n");
		messageText.setCaretPosition(messageText.getText().length());  //����궨λ�����һ���ַ���ʹ��������ʾ�������������Ϣ
	}
//	public static void addProcessMessage(String s)  {  //�ڽ����������м�����Ϣ
//		pageItemText.append(s+"\r\n");
//		pageItemText.setCaretPosition(processCreateText.getText().length());
//	}
	
	
	
	public static void setCPUpswMessage() {  //0Ϊ�ں�̬��1Ϊ�û�̬
		if(CPU.getPsw()==0) {
			CPUtexts[1].setText("�ں�̬");
		} else {
			CPUtexts[1].setText("�û�̬");
		}
	}
	
	public void setMessageTextArea(int x,int y) {  //������Ϣ��ʾλ��
		messageText = new JTextArea();
		messageText.setFont(new Font("����", 0, 18));
		messageText.setEditable(false);
		messageScrollPane = new JScrollPane(messageText);
		messageScrollPane.setBounds(x+20,y+50, 650, 450);
		messageLabel.setBounds(x+20,y+20, 100, 20);
		con.add(messageScrollPane);
		con.add(messageLabel);
	}
	public void setPageItemTable(int x,int y) {  //����ҳ����Ϣ
		// ҳ��
		pageItemPane = new JPanel();
		pageItemPane.setBounds(x+20, y+100, 300, 200);
		pageItemPane.setLayout(new BorderLayout());
		
        String[] pageTableHeader = new String[]{"�߼�ҳ��", "�ڴ���", "�����","�������","����ʱ��"};
        pageTableInfo = new DefaultTableModel(new String[][]{}, pageTableHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // �رյ�Ԫ��༭
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
	public void setTLBTable(int x,int y) {  //����ҳ����Ϣ
		// ҳ��
		TLBPane = new JPanel();
		TLBPane.setBounds(x+20, y+100, 300, 200);
		TLBPane.setLayout(new BorderLayout());
		
        String[] TLBHeader = new String[]{"���̹���","�߼�ҳ��", "�ڴ���","�������","����ʱ��"};
        TLBTableInfo = new DefaultTableModel(new String[][]{}, TLBHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // �رյ�Ԫ��༭
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
	public void setCPUArea(int x,int y) {  //����CPU״̬��Ϣ��λ��
		CPULabel.setBounds(x+20, y+20, 100, 20);  //���û���
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
	
	public void setRunningArea(int x,int y) {  //�����������н�������
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
	
	public void setMemArea(int x,int y) {  //�����ڴ��ʹ���������
		memLabel.setBounds(x+20, y+20, 500, 20);
		con.add(memLabel);
		memPane = new JPanel();
		memPane.setBounds(x+20, y+50, 500, 200);
		memPane.setBackground(Color.PINK);
		memPane.setLayout(null);
		con.add(memPane);
		
		//�ڴ���ʼ��
		memBlock = new JLabel[RAM.PAGE_NUM];
		memNo = new JLabel[RAM.PAGE_NUM];
		for(int i=0;i<RAM.PAGE_NUM;i++) {
			memBlock[i] = new JLabel();
			memNo[i] = new JLabel();
			
			memBlock[i].setBounds(i%8*60+20,(i/8)*50,30,30);
			memNo[i].setBounds(i%8*60+25,(i/8)*50+20,30,30);
			memNo[i].setText(i+"");
			
			memBlock[i].setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
			memPane.add(memBlock[i]);
			memPane.add(memNo[i]);
		}
		refreshMemArea();

	}
	
	//������������ʾ����
	public void setRomArea(int x,int y) {
		romLabel.setBounds(x+20, y+20, 200, 20);
		con.add(romLabel);
		
		romPane = new JPanel();
		romPane.setBounds(x+20, y+50, 500, 200);
		romPane.setLayout(null);
		
		labelPane = new JPanel();
		labelPane.setLayout(null);
		labelPane.setBounds(0, 0, 500, 1000);
		labelPane.setBackground(Color.cyan);
		labelPane.setPreferredSize(new Dimension(500,ROM.getChange_used().length*5));
		
		
		romNumLabel = new JLabel[ROM.getChange_used().length];
		for(int i=0;i<ROM.getChange_used().length;i+=8) {
			romNumLabel[i] = new JLabel();
			romNumLabel[i].setText(i+"-"+(i+7));
			romNumLabel[i].setBounds(10,i*5+10,100,20);
			labelPane.add(romNumLabel[i]);
		}
		romUsedLabel = new JLabel[ROM.getChange_used().length];
		
		for(int j=0;j<ROM.getChange_used().length;j++) {
			romUsedLabel[j] = new JLabel();
			romUsedLabel[j].setBounds(j%8*40+100,(j/8)*40,30,30);
			romUsedLabel[j].setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
			labelPane.add(romUsedLabel[j]);
		}
		refreshRomArea();
		
		romScrollPane = new JScrollPane(labelPane,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		romScrollPane.setBounds(0, 0, 500, 200);
		
		
		romPane.add(romScrollPane);
		con.add(romPane);
	}
	
	
	public void setButtonArea(int x,int y) {  //���ð�ť����λ��
		buttonPane = new JPanel();
		buttonPane.setLayout(null);
		buttonPane.setBounds(x, y, 1600, 50);
		for(int i=0;i<buttons.length;i++) {
			buttons[i] = new JButton(buttonStrs[i]);
			buttons[i].setBounds(500+i*150, 0, 100, 40);
			buttonPane.add(buttons[i]);
		}
		con.add(buttonPane);
		
		//Ϊ��ť��Ӽ�����
		//��ʼ��ť
		buttons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				om.runSystem();
				om.proceedSystem();
			}
		});
		
		//��ͣ��ť
		buttons[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				om.pauseSystem();
			}
			
		});
		
		//������ť
		buttons[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				om.proceedSystem();
			}
		});
		
		//�رհ�ť
		buttons[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				om.shutdownSystem();
				dispose();
			}
		});
		
		//�������̰�ť
		buttons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				om.getJobsRequest().createANewJob();
			}
		});
		
	}
	public static void OutputMessage() {
		if(CPU.getIr()==null && ProcessSchedule.getRunningProcess()==null) {
			String s = clock.getTime()+":[CPU����]";
			OSManage.messageOutputSystem(s);
		} else {
			synchronized (ProcessSchedule.getRunningProcess()) {
				String s = clock.getTime()+":[���н��̣����̱�ţ�"+ProcessSchedule.getRunningProcess().getPcb().getPro_ID()+
				   						   "��ָ��α�ţ�"+CPU.getIr().getInstruc_ID()+
				   						   "��ָ�����ͱ�ţ�"+CPU.getIr().getInstruc_state()+
				   						   "��ָ������˵����"+Instruction.instrucText[CPU.getIr().getInstruc_state()]+
				   						   "���߼���ַ��"+CPU.getIr().getL_Address()+
				   						   "�������ַ��"+ProcessSchedule.getRunningProcess().getPcb().getPage_items()[CPU.getIr().getL_Address()].getPageFrameNo()+
				   						   "������ʱ����"+(clock.getTime()-ProcessSchedule.getRunningProcess().getPcb().getInTimes())+"]";
				OSManage.messageOutputSystem(s);
			}
		}
		synchronized (ProcessSchedule.getReadyQ()) {
			StringBuffer ss = new StringBuffer("FFFF"+":[��������1��");
			for(int i=0;i<ProcessSchedule.getReadyQ().size();i++) {
				ss = ss.append(ProcessSchedule.getReadyQ().get(i).getPcb().getLastRqTime()+"=����"+ProcessSchedule.getReadyQ().get(i).getPcb().getPro_ID());
				if(i!=ProcessSchedule.getReadyQ().size()-1) {
					ss.append("��");
				}
			}
			ss = ss.append("]");
			OSManage.messageOutputSystem(ss.toString());
		}
		synchronized (ProcessSchedule.getBlockQ1()) {
			StringBuffer sq1 = new StringBuffer("FFFF"+":[��������1��");
			for(int i=0;i<ProcessSchedule.getBlockQ1().size();i++) {
				sq1.append(ProcessSchedule.getBlockQ1().get(i).getPcb().getLastBq1Time()+"/"+
						(i==0?InterruptOperator.getKeyboard_time()+2:"--")+"=����"+
						ProcessSchedule.getBlockQ1().get(i).getPcb().getPro_ID());
				if(i!=ProcessSchedule.getBlockQ1().size()-1) {
					sq1.append("��");
				}
			}
			sq1.append("]");
			OSManage.messageOutputSystem(sq1.toString());
		}
		
		synchronized (ProcessSchedule.getBlockQ2()) {
			StringBuffer sq2 = new StringBuffer("FFFF"+":[��������2��");
			for(int i=0;i<ProcessSchedule.getBlockQ2().size();i++) {
				sq2.append(ProcessSchedule.getBlockQ2().get(i).getPcb().getLastBq2Time()+"/"+
						(i==0?InterruptOperator.getScreen_time()+3:"--")+"=����"+
						ProcessSchedule.getBlockQ2().get(i).getPcb().getPro_ID());
				if(i!=ProcessSchedule.getBlockQ2().size()-1) {
					sq2.append("��");
				}
			}
			sq2.append("]");
			OSManage.messageOutputSystem(sq2.toString());
		}
		
		synchronized (ProcessSchedule.getBlockQ3()) {
			StringBuffer sq3 = new StringBuffer("FFFF"+":[��������3��");
			for(int i=0;i<ProcessSchedule.getBlockQ3().size();i++) {
				sq3.append(ProcessSchedule.getBlockQ3().get(i).getPcb().getLastBq3Time()+"/"+
						(i==0?InterruptOperator.getReadDisk_time()+3:"--")+"=����"+
						ProcessSchedule.getBlockQ3().get(i).getPcb().getPro_ID());
				if(i!=ProcessSchedule.getBlockQ3().size()-1) {
					sq3.append("��");
				}
			}
			sq3.append("]");
			OSManage.messageOutputSystem(sq3.toString());
		}
		
		synchronized (ProcessSchedule.getBlockQ4()) {
			StringBuffer sq4 = new StringBuffer("FFFF"+":[��������4��");
			for(int i=0;i<ProcessSchedule.getBlockQ4().size();i++) {
				sq4.append(ProcessSchedule.getBlockQ4().get(i).getPcb().getLastBq4Time()+"/"+
						(i==0?InterruptOperator.getWriteDisk_time()+4:"--")+"=����"+
						ProcessSchedule.getBlockQ4().get(i).getPcb().getPro_ID());
				if(i!=ProcessSchedule.getBlockQ4().size()-1) {
					sq4.append("��");
				}
			}
			sq4.append("]");
			OSManage.messageOutputSystem(sq4.toString());
		}
		
		synchronized (ProcessSchedule.getBlockQ5()) {
			StringBuffer sq5 = new StringBuffer("FFFF"+":[��������5��");
			for(int i=0;i<ProcessSchedule.getBlockQ5().size();i++) {
				sq5.append(ProcessSchedule.getBlockQ5().get(i).getPcb().getLastBq5Time()+"/"+
						(i==0?InterruptOperator.getPrint_time()+4:"--")+"=����"+
						ProcessSchedule.getBlockQ5().get(i).getPcb().getPro_ID());
				if(i!=ProcessSchedule.getBlockQ5().size()-1) {
					sq5.append("��");
				}
			}
			sq5.append("]");
			OSManage.messageOutputSystem(sq5.toString());
		}
		
		
		synchronized(ProcessSchedule.getBlockQ3()) { 
			synchronized(ProcessSchedule.getBlockQ4()) {
				if(ProcessSchedule.getBlockQ3().size()==0 && ProcessSchedule.getBlockQ4().size()==0) {
					String mes = ProcessUI.getClock().getTime()+":[�������޽���]";
					OSManage.messageOutputSystem(mes);
				} else {
					for(int i=0;i<RAM.BUFFER_AREA_NUM;i++) {
						if(RAM.getAllBlocks()[RAM.BUFFER_AREA_START_NO+i]) {
							for(int k=0;k<ProcessSchedule.getBlockQ3().size();k++) {
								StringBuffer mes = new StringBuffer();
								if(ProcessSchedule.getBlockQ3().get(k).getBufferNo()==RAM.BUFFER_AREA_START_NO+i) {
									mes.append("MMMM:[������"+i+"��"+ProcessSchedule.getBlockQ3().get(k).getLastInbuffer()+"="+ProcessSchedule.getBlockQ3().get(k).getPcb().getPro_ID()+"��]");
									OSManage.messageOutputSystem(mes.toString());
								}
							}
							for(int m=0;m<ProcessSchedule.getBlockQ4().size();m++) {
								StringBuffer mes2 = new StringBuffer();
								if(ProcessSchedule.getBlockQ4().get(m).getBufferNo()==RAM.BUFFER_AREA_START_NO+i) {
									mes2.append("MMMM:[������"+i+"��"+ProcessSchedule.getBlockQ4().get(m).getLastInbuffer()+"="+ProcessSchedule.getBlockQ4().get(m).getPcb().getPro_ID()+"��]");
									OSManage.messageOutputSystem(mes2.toString());
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	//ˢ������ҳ���״̬��Ϣ
	public void refresh() {
		//ˢ�����ж���
		refreshQueue(); 
		
		//ˢ�µ�ǰ���н���
		refreshRunning();
		
		//ˢ��CPU
		refreshCPU();
		
		//ˢ���ڴ�
		refreshMemArea();
		
		//ˢ��ҳ��
		refreshPageTable();
		
		//ˢ��TLB
		refreshTLBTable();
		
		//ˢ�����
		refreshRomArea();
	}
	
	//ˢ���ڴ�����
	public synchronized void refreshMemArea() {
		for(int i=0;i<RAM.PAGE_NUM;i++) {
			if(RAM.getAllBlocks()[i] == true) {
				memBlock[i].setText("��");
			} else {
				memBlock[i].setText("��");
			}
		}
		
	}
	
	//������Ϣ�Ļ�������
	public synchronized static void addQueueMessage(String s,int queueID) {  //�ڶ�������ʾ��Ϣ��queueIDΪ��ʾ��Ϣ�Ķ���ID
		queueText[queueID].append(s+"\r\n");
	}
	public synchronized static void clearQueueMessage(int queueID) {  //��ն�����Ϣ
		queueText[queueID].setText("");
	}
	//ˢ�¸��ֶ���
	public synchronized void refreshQueue() {
		for(int i=0;i<8;i++) {
			clearQueueMessage(i);
		}
		synchronized (ProcessSchedule.getReadyQ()) {
			for(int k=0;k<ProcessSchedule.getReadyQ().size();k++) {
				addQueueMessage("����"+ProcessSchedule.getReadyQ().get(k).getPcb().getPro_ID()+
								",���ȼ���"+ProcessSchedule.getReadyQ().get(k).getPcb().getPriority()+
								",ָ������"+ProcessSchedule.getReadyQ().get(k).getPcb().getInstrucNum()+
								",PC:"+ProcessSchedule.getReadyQ().get(k).getPcb().getPc(),0);
			}
		}
		synchronized(ProcessSchedule.getBlockQ1()) {
			for(int k=0;k<ProcessSchedule.getBlockQ1().size();k++) {
				addQueueMessage("����"+ProcessSchedule.getBlockQ1().get(k).getPcb().getPro_ID(),1);
			}
		}
		synchronized(ProcessSchedule.getBlockQ2()) {
			for(int k=0;k<ProcessSchedule.getBlockQ2().size();k++) {
				addQueueMessage("����"+ProcessSchedule.getBlockQ2().get(k).getPcb().getPro_ID(),2);
			}
		}
		synchronized(ProcessSchedule.getBlockQ3()) {
			for(int k=0;k<ProcessSchedule.getBlockQ3().size();k++) {
				addQueueMessage("����"+ProcessSchedule.getBlockQ3().get(k).getPcb().getPro_ID(),3);
			}
		}
		synchronized(ProcessSchedule.getBlockQ4()) {
			for(int k=0;k<ProcessSchedule.getBlockQ4().size();k++) {
				addQueueMessage("����"+ProcessSchedule.getBlockQ4().get(k).getPcb().getPro_ID(),4);
			}
		}
		synchronized(ProcessSchedule.getBlockQ5()) {
			for(int k=0;k<ProcessSchedule.getBlockQ5().size();k++) {
				addQueueMessage("����"+ProcessSchedule.getBlockQ5().get(k).getPcb().getPro_ID(),5);
			}
		}
		synchronized (ProcessSchedule.getReverseQ()) {
			for(int k=0;k<ProcessSchedule.getReverseQ().size();k++) {
				addQueueMessage("��ҵ"+ProcessSchedule.getReverseQ().get(k).getJobsID()+
								",���ȼ���"+ProcessSchedule.getReverseQ().get(k).getPriority()+
								",����ʱ�䣺"+ProcessSchedule.getReverseQ().get(k).getInTimes()+
								",ָ��������"+ProcessSchedule.getReverseQ().get(k).getInstrucNum(),6);
			}
		}
		synchronized (ProcessSchedule.getFinishedQ()) {
			for(int k=0;k<ProcessSchedule.getFinishedQ().size();k++) {
				addQueueMessage("����"+ProcessSchedule.getFinishedQ().get(k).getPcb().getPro_ID()+
								",����ʱ�䣺"+ProcessSchedule.getFinishedQ().get(k).getPcb().getInTimes()+
								",����ʱ�䣺"+ProcessSchedule.getFinishedQ().get(k).getPcb().getEndTimes()+
								",��תʱ�䣺"+ProcessSchedule.getFinishedQ().get(k).getPcb().getTurnTimes(),7);
			}
		}
	}
	//ˢ��CPU״̬
	public synchronized void refreshCPU() {
		//���õ�ǰʱ����Ϣ
		CPUtexts[0].setText(ProcessSchedule.getClock().getTime()+"");
		//����CPU����״̬
		setCPUpswMessage();
	}
	
	
	//ˢ�µ�ǰ���н���״̬
	public void refreshRunning() {
		Process p = ProcessSchedule.getRunningProcess();
		if(p==null) {
			for(int i=0;i<runningStrs.length;i++) {
				runningText[i].setText("��");
			}
			return;
		}
		synchronized(p) {
			runningText[0].setText(p.getPcb().getPro_ID()+"");
			runningText[1].setText(p.getPcb().getPriority()+"");
			runningText[2].setText(p.getPcb().getInstruc().getInstruc_ID()+"");
			runningText[3].setText(p.getPcb().getInstrucNum()+"");
			runningText[4].setText(p.getPcb().getInstruc().getInstruc_state()+"");
			runningText[5].setText(p.getPcb().getPc()+"");
		}
	}
	
	//ˢ��ҳ��
	public void refreshPageTable() {
		pageTableInfo.setRowCount(0);
		for(int i=0;i<ProcessSchedule.getPcbList().size();i++) {
			for(int j=0;j<ProcessSchedule.getPcbList().get(i).getPage_items_num();j++) {
				Vector<String> v = new Vector<String>();
				v.add("p"+ProcessSchedule.getPcbList().get(i).getPro_ID()+":"+ProcessSchedule.getPcbList().get(i).getPage_items()[j].getLogicPageNo());
				v.add(ProcessSchedule.getPcbList().get(i).getPage_items()[j].getPageFrameNo()+"");
				v.add(ProcessSchedule.getPcbList().get(i).getPage_items()[j].getDiskBlockNo()+"");
				v.add(ProcessSchedule.getPcbList().get(i).getPage_items()[j].getInMemTimes()+"");
				v.add(ProcessSchedule.getPcbList().get(i).getPage_items()[j].getLastInMemTime()+"");
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
				v.add(ProcessSchedule.getPcbList().get(i).getPage_frame_nums()[j].getLastInMemTime());
				TLBTableInfo.addRow(v);
			}
			
		}
	}
	
	public synchronized void refreshRomArea() {
		boolean[] arr = ROM.getChange_used();
		for(int i=0;i<arr.length;i++) {
			if(arr[i] == true) {
				romUsedLabel[i].setText("��");
			} else {
				romUsedLabel[i].setText("��");
			}
		}
	}
	
	
	@Override
	public void run() {
		
		while(!om.isShutdown()) {
			if(!om.isPause()) {
				refresh();
				OutputMessage();
				
				if(ProcessSchedule.getPcbList().size()==0 && ProcessSchedule.getFinishedQ().size()>0) {
					IOFile.outputMessageToFile(clock.getTime());
				}
				
				clock.passOneSec();
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
