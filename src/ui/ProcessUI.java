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
	static JLabel[] queueLabels={new JLabel("��������"),new JLabel("��������1"),new JLabel("��������2"),new JLabel("��������3"),new JLabel("��������4"),new JLabel("��������5"),new JLabel("��ҵ�󱸶���"),new JLabel("��ɶ���") };
	static JTextArea[] queueText;  //�����ı���
	
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
	static JLabel memLabel = new JLabel("�ڴ��ʹ�����0-31");
	static JLabel[] memBlock;  //�ڴ���ʾ��"��" ��ʾ��ռ�ã�"��"��ʾδռ��
	
	
	static boolean pause = false;  //��ͣ�ź�
	
	public ProcessUI() {
		super("���̵ͼ�����");
		
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
	public void setQueueTextArea(int x,int y) {  //���ö�����ʾλ��

		for(int i=0;i<8;i++) {
			queueLabels[i].setBounds(x+ 20+i*100, y+20, 80, 20);
			con.add(queueLabels[i]);
			queueText[i] = new JTextArea();
			queueText[i].setBounds(x+ 20+i*100, y+50, 80, 200);
			queueText[i].setEditable(false);
			queueText[i].setFont(new Font("����", 0, 18));
			con.add(queueText[i]);
		}
	}
	
	public static boolean isPause() {
		return pause;
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
		
        String[] pageTableHeader = new String[]{"�߼�ҳ��", "�ڴ���", "�����","�������"};
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
		
        String[] TLBHeader = new String[]{"���̹���","�߼�ҳ��", "�ڴ���","�������"};
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
		memLabel.setBounds(x+20, y+20, 200, 20);
		con.add(memLabel);
		memPane = new JPanel();
		memPane.setBounds(x+20, y+50, 500, 200);
		memPane.setBackground(Color.PINK);
		memPane.setLayout(null);
		con.add(memPane);
		
		//�ڴ���ʼ��
		memBlock = new JLabel[RAM.PAGE_NUM];
		for(int i=0;i<RAM.PAGE_NUM;i++) {
			memBlock[i] = new JLabel();
			memBlock[i].setBounds(i%8*60+20,(i/8)*50,30,30);
			memBlock[i].setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
			memPane.add(memBlock[i]);
		}
		refreshMemArea();

		
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
			}
		});
		
		//��ͣ��ť
		buttons[1].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				//ps.suspend();
				//jr.suspend();
			}
			
		});
		
		//������ť
		buttons[2].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//ps.resume();
				//jr.resume();
			}
		});
		
		//�رհ�ť
		buttons[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pause = true;
				dispose();
			}
		});
		
		//�������̰�ť
		buttons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//JobsRequest.addJob(JobsRequest.createANewJob(20));
			}
		});
		
	}
	public static void OutputMessage() {
		if(CPU.getIr()==null) {
			System.out.println(clock.getTime()+":[����]");  //�������̨
			IOFile.writeMessageInData(clock.getTime()+":[����]");  //����ַ�������
			addMessage(clock.getTime()+":[����]");  //�����ͼ�λ�����
			//setRunningProcessMessage();
			
		} else {
			String s = clock.getTime()+":[���н���"+ProcessSchedule.getRunningProcess().getPcb().getPro_ID()+"ָ��"+CPU.getIr().getInstruc_ID()+"����"+CPU.getIr().getInstruc_state()+"]";
			System.out.println(s);
			IOFile.writeMessageInData(s);
			addMessage(s);
			//setRunningProcessMessage();
		}
		StringBuffer ss = new StringBuffer(clock.getTime()+":[��������1");
		clearQueueMessage(0);
		for(int i=0;i<ProcessSchedule.getReadyQ().size();i++) {
			ss = ss.append(" ����"+ProcessSchedule.getReadyQ().get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.getReadyQ().get(i).getPcb().getPro_ID(), 0);
		}
		ss = ss.append("]");
		System.out.println(ss.toString());
		IOFile.writeMessageInData(ss.toString());
		addMessage(ss.toString());
		
		StringBuffer sq1 = new StringBuffer(clock.getTime()+":[��������1");
		clearQueueMessage(1);
		for(int i=0;i<ProcessSchedule.getBlockQ1().size();i++) {
			sq1.append(" ����"+ProcessSchedule.getBlockQ1().get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.getBlockQ1().get(i).getPcb().getPro_ID(), 1);
		}
		sq1.append("]");
		System.out.println(sq1.toString());
		IOFile.writeMessageInData(sq1.toString());
		addMessage(sq1.toString());
		
		StringBuffer sq2 = new StringBuffer(clock.getTime()+":[��������2");
		clearQueueMessage(2);
		for(int i=0;i<ProcessSchedule.getBlockQ2().size();i++) {
			sq2.append(" ����"+ProcessSchedule.getBlockQ2().get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.getBlockQ2().get(i).getPcb().getPro_ID(), 2);
		}
		sq2.append("]");
		System.out.println(sq2.toString());
		IOFile.writeMessageInData(sq2.toString());
		addMessage(sq2.toString());
		
		StringBuffer sq3 = new StringBuffer(clock.getTime()+":[��������3");
		clearQueueMessage(3);
		for(int i=0;i<ProcessSchedule.getBlockQ3().size();i++) {
			sq3.append(" ����"+ProcessSchedule.getBlockQ3().get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.getBlockQ3().get(i).getPcb().getPro_ID(), 3);
		}
		sq3.append("]");
		System.out.println(sq3.toString());
		IOFile.writeMessageInData(sq3.toString());
		addMessage(sq3.toString());
		//setDeviceMessage();
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
		for(int k=0;k<ProcessSchedule.getReadyQ().size();k++) {
			addQueueMessage("����"+ProcessSchedule.getReadyQ().get(k).getPcb().getPro_ID(),0);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ1().size();k++) {
			addQueueMessage("����"+ProcessSchedule.getBlockQ1().get(k).getPcb().getPro_ID(),1);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ2().size();k++) {
			addQueueMessage("����"+ProcessSchedule.getBlockQ2().get(k).getPcb().getPro_ID(),2);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ3().size();k++) {
			addQueueMessage("����"+ProcessSchedule.getBlockQ3().get(k).getPcb().getPro_ID(),3);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ4().size();k++) {
			addQueueMessage("����"+ProcessSchedule.getBlockQ4().get(k).getPcb().getPro_ID(),4);
		}
		for(int k=0;k<ProcessSchedule.getBlockQ5().size();k++) {
			addQueueMessage("����"+ProcessSchedule.getBlockQ5().get(k).getPcb().getPro_ID(),5);
		}
		for(int k=0;k<ProcessSchedule.getReverseQ().size();k++) {
			addQueueMessage("��ҵ"+ProcessSchedule.getReverseQ().get(k).getJobsID(),6);
		}
		for(int k=0;k<ProcessSchedule.getFinishedQ().size();k++) {
			addQueueMessage("����"+ProcessSchedule.getFinishedQ().get(k).getPcb().getPro_ID(),7);
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
					runningText[i].setText("��");
				}
			}
		}
	}
	
	//ˢ��ҳ��
	public void refreshPageTable() {
		pageTableInfo.setRowCount(0);
		for(int i=0;i<ProcessSchedule.getPcbList().size();i++) {
			for(int j=0;j<ProcessSchedule.getPcbList().get(i).getPage_items_num();j++) {
				Vector<String> v = new Vector<String>();
				v.add("����"+ProcessSchedule.getPcbList().get(i).getPro_ID()+":"+ProcessSchedule.getPcbList().get(i).getPage_items()[j].getLogicPageNo());
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
