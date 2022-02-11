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
	static JLabel[] queueLabels={new JLabel("��������"),new JLabel("��������1"),new JLabel("��������2"),new JLabel("��������3")};
	static JTextArea[] queueText;  //�����ı���
	
	static JScrollPane messageScrollPane;  //��Ϣ�ı��������
	static JTextArea messageText;  //��Ϣ�ı���
	static JLabel messageLabel = new JLabel("�¼���Ϣ");
	
	static JScrollPane processCreateScrollPane;  //���̴����˳���Ϣ�ı��������
	static JTextArea processCreateText;  //���̴����˳���Ϣ�ı���
	static JLabel processCreateLabel = new JLabel("����������");
	
	static JPanel CPUPane;  //CPU״̬����
	static JLabel CPULabel = new JLabel("CPU״̬��Ϣ");
	static String[] strs = {"����ʱ�䣺","psw״̬��","���ж�״̬��","��ǰ���н��̣�","��ǰ����ָ�","ָ��״̬��"};
	static JLabel[] labels = new JLabel[6];
	static JTextField[] texts = new JTextField[6];
	
	static JPanel devicePane;  //�豸ʹ������
	static JLabel deviceLabel = new JLabel("�豸ʹ�����");
	static String[] deviceStrs = {"�����豸","��Ļ�豸","�����豸"};
	static JLabel[] deviceLabels = new JLabel[3];
	static JTextField[] deviceText = new JTextField[3];
	
	static JPanel buttonPane;   //��ť����
	static String[] buttonStrs = {"��ʼ","��ͣ","����","�����½���","�ر�"};
	static JButton[] buttons = new JButton[5];
	
	ProcessSchedule ps;
	JobsRequest jr;
	static boolean pause = false;  //��ͣ�ź�
	
	public ProcessUI() {
		super("���̵ͼ�����");
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
	public void setQueueTextArea(int x,int y) {  //���ö�����ʾλ��
		for(int i=0;i<4;i++) {
			queueLabels[i].setBounds(x+ 20+i*150, y+20, 100, 20);
			con.add(queueLabels[i]);
			queueText[i] = new JTextArea();
			queueText[i].setBounds(x+ 20+i*150, y+50, 100, 200);
			queueText[i].setEditable(false);
			queueText[i].setFont(new Font("����", 0, 18));
			con.add(queueText[i]);
		}
	}
	public static void addMessage(String s) {  //����Ϣ���м�����Ϣ
		messageText.append(s+"\r\n");
		messageText.setCaretPosition(messageText.getText().length());  //����궨λ�����һ���ַ���ʹ��������ʾ�������������Ϣ
	}
	public static void addProcessMessage(String s)  {  //�ڽ����������м�����Ϣ
		processCreateText.append(s+"\r\n");
		processCreateText.setCaretPosition(processCreateText.getText().length());
	}
	public static void addQueueMessage(String s,int queueID) {  //�ڶ�������ʾ��Ϣ��queueIDΪ��ʾ��Ϣ�Ķ���ID
		queueText[queueID].append(s+"\r\n");
	}
	public static void clearQueueMessage(int queueID) {  //��ն�����Ϣ
		queueText[queueID].setText("");
	}
	public static void setRunningProcessMessage()  {  //�������н�����Ϣ������ָ����Ϣ
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
	public static void setCPUpswMessage(int psw) {  //0Ϊ�ں�̬��1Ϊ�û�̬
		if(psw==0) {
			texts[1].setText("�ں�̬");
		} else {
			texts[1].setText("�û�̬");
		}
	}
	public static void setCPUbreakMessage(String s) {  //���ù��ж���Ϣ״̬
		texts[2].setText(s);
	}
	public static void setDeviceMessage() {  //�����豸��Ϣ
		if(ProcessSchedule.BlockQ1.size()>0) {
			deviceText[0].setText("����"+ProcessSchedule.BlockQ1.get(0).getPcb().getPro_ID());
		} else {
			deviceText[0].setText("");
		}
		if(ProcessSchedule.BlockQ2.size()>0) {
			deviceText[1].setText("����"+ProcessSchedule.BlockQ2.get(0).getPcb().getPro_ID());
		} else {
			deviceText[1].setText("");
		}
		if(ProcessSchedule.BlockQ3.size()>0) {
			deviceText[2].setText("����"+ProcessSchedule.BlockQ3.get(0).getPcb().getPro_ID());
		} else {
			deviceText[2].setText("");
		}
	}
	public void setMessageTextArea(int x,int y) {  //������Ϣ��ʾλ��
		messageText = new JTextArea();
		messageText.setFont(new Font("����", 0, 18));
		messageText.setEditable(false);
		messageScrollPane = new JScrollPane(messageText);
		messageScrollPane.setBounds(x+20,y+50, 700, 450);
		messageLabel.setBounds(x+20,y+20, 100, 20);
		con.add(messageScrollPane);
		con.add(messageLabel);
	}
	public void setProcessCreateTextArea(int x,int y) {  //���ý��̴����˳���Ϣ��ʾλ��
		processCreateText = new JTextArea();
		processCreateText.setFont(new Font("����", 0, 18));
		processCreateText.setEditable(false);
		processCreateScrollPane = new JScrollPane(processCreateText);
		processCreateScrollPane.setBounds(x+20,y+100, 700, 200);
		processCreateLabel.setBounds(x+20,y+70, 100, 20);
		con.add(processCreateScrollPane);
		con.add(processCreateLabel);
	}
	public void setCPUArea(int x,int y) {  //����CPU״̬��Ϣ��λ��
		CPULabel.setBounds(x+20, y+20, 100, 20);  //���û���
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
		texts[2].setText("���ж�");
		con.add(CPUPane);
	}
	
	public void setDeviceArea(int x,int y) {  //�����豸ʹ����Ϣ����
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
				ps = new ProcessSchedule();
				jr = new JobsRequest();
				ps.start();
				jr.start();
			}
		});
		
		//��ͣ��ť
		buttons[1].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				ps.suspend();
				jr.suspend();
			}
			
		});
		
		//������ť
		buttons[2].addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ps.resume();
				jr.resume();
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
				JobsRequest.addJob(JobsRequest.createANewJob(20));
			}
		});
		
	}
	public static void OutputMessage() {
		if(CPU.getIr()==null) {
			System.out.println(clock.getTime()+":[����]");  //�������̨
			IOFile.writeMessageInData(clock.getTime()+":[����]");  //����ַ�������
			addMessage(clock.getTime()+":[����]");  //�����ͼ�λ�����
			setRunningProcessMessage();
			
		} else {
			String s = clock.getTime()+":[���н���"+ProcessSchedule.getRunningProcess().getPcb().getPro_ID()+"ָ��"+CPU.getIr().getInstruc_ID()+"����"+CPU.getIr().getInstruc_state()+"]";
			System.out.println(s);
			IOFile.writeMessageInData(s);
			addMessage(s);
			setRunningProcessMessage();
		}
		StringBuffer ss = new StringBuffer(clock.getTime()+":[��������1");
		clearQueueMessage(0);
		for(int i=0;i<ProcessSchedule.ReadyQ.size();i++) {
			ss = ss.append(" ����"+ProcessSchedule.ReadyQ.get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.ReadyQ.get(i).getPcb().getPro_ID(), 0);
		}
		ss = ss.append("]");
		System.out.println(ss.toString());
		IOFile.writeMessageInData(ss.toString());
		addMessage(ss.toString());
		
		StringBuffer sq1 = new StringBuffer(clock.getTime()+":[��������1");
		clearQueueMessage(1);
		for(int i=0;i<ProcessSchedule.BlockQ1.size();i++) {
			sq1.append(" ����"+ProcessSchedule.BlockQ1.get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.BlockQ1.get(i).getPcb().getPro_ID(), 1);
		}
		sq1.append("]");
		System.out.println(sq1.toString());
		IOFile.writeMessageInData(sq1.toString());
		addMessage(sq1.toString());
		
		StringBuffer sq2 = new StringBuffer(clock.getTime()+":[��������2");
		clearQueueMessage(2);
		for(int i=0;i<ProcessSchedule.BlockQ2.size();i++) {
			sq2.append(" ����"+ProcessSchedule.BlockQ2.get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.BlockQ2.get(i).getPcb().getPro_ID(), 2);
		}
		sq2.append("]");
		System.out.println(sq2.toString());
		IOFile.writeMessageInData(sq2.toString());
		addMessage(sq2.toString());
		
		StringBuffer sq3 = new StringBuffer(clock.getTime()+":[��������3");
		clearQueueMessage(3);
		for(int i=0;i<ProcessSchedule.BlockQ3.size();i++) {
			sq3.append(" ����"+ProcessSchedule.BlockQ3.get(i).getPcb().getPro_ID());
			addQueueMessage("����"+ProcessSchedule.BlockQ3.get(i).getPcb().getPro_ID(), 3);
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
