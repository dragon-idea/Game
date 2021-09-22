package 游戏界面;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import 客户端与服务端.*;

public class GameHall_GUI extends JFrame implements ActionListener
{
	Client client = null;
	private boolean isCreatNewTab;
	//选项卡
	JTabbedPane gameHall_tab = new JTabbedPane();				//游戏大厅
	JTabbedPane userInfo_tab = new JTabbedPane();					//个人信息
	JTabbedPane severInfo_tab = new JTabbedPane();				//服务器信息
	
	//分隔面板
	JSplitPane gameHall_split = new JSplitPane();												//水平分隔的游戏大厅面板
	JSplitPane Info_split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);				//垂直分割的信息面板
	
	//每个选项卡的容器
	JPanel userInfo_panel = new JPanel();						//用户信息容器
	JPanel severInfo_panel = new JPanel();					//服务器信息容器
	JPanel match_panel = new JPanel();						//匹配面板容器
	
	JTextArea dialog_area = new JTextArea();
	JTextField message_field = new JTextField();
	
	String userName = null;
	ImageIcon userImage = null;
	ImageIcon null_image = new ImageIcon("./src/图片/资源包/res/img/noone.gif");
	ImageIcon desk_image = new ImageIcon("./src/图片/资源包/res/img/xqnoone.gif");
	
	JLabel userName_lab;
	JLabel userPhoto_lab = new JLabel(new ImageIcon("./src/图片/资源包/res/img/boy1.gif"), JLabel.CENTER);
	JLabel tag1_lab = new JLabel(">>>>匹配大厅<<<<");
	JLabel desk_lab = new JLabel();
	JLabel orderNum_lab;
	JLabel IP_lab = new JLabel("服务器：127.0.0.1");
	JLabel onlineUser_lab = new JLabel();
	JLabel[] leftName_lab = new JLabel[15];
	JLabel[] rightName_lab = new JLabel[15];
	JLabel myState_lab = new JLabel("未准备");
	JLabel oppoState_lab = new JLabel();
	
	JButton match_btn = new JButton("自动匹配");
	JButton exit1_btn = new JButton("退出");
	JButton[] leftImage_btn = new JButton[15];
	JButton[] rightImage_btn = new JButton[15];
	JButton send_btn = new JButton("发送");
	JButton exit2_btn = new JButton("退出");
	JButton start_btn = new JButton("开始");
	JButton request_btn = new JButton("求和");
	JButton admitDefeat_btn = new JButton("认输");
	
	public GameHall_GUI(String userName, String imagePath, Client client)
	{
		isCreatNewTab = false;
		this.client = client;
		this.userName = userName;
		userImage = new ImageIcon(imagePath);
		
		userName_lab = new JLabel("用户名：" + this.userName, JLabel.CENTER);
		
		this.client.setDesk(leftName_lab, rightName_lab, leftImage_btn, rightImage_btn, onlineUser_lab);
		
	}
	
	public void EnterGameRoom()
	{
		JTabbedPane myInfo_tab = new JTabbedPane();					//我的信息
		JTabbedPane opponentInfo_tab = new JTabbedPane();			//对手信息
		JTabbedPane chat_tab = new JTabbedPane();						//聊天窗口
		
		JSplitPane gamerRoom_split = new JSplitPane();													//游戏房间面板
		JSplitPane playerInfo_split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);			//玩家信息面板
		JSplitPane playerInfo_split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);			//玩家信息面板
		
		JPanel chess_panel = new JPanel();							//棋盘面板容器
		JPanel myInfo_panel = new JPanel();						//我的信息容器
		JPanel opponentInfo_panel = new JPanel();			//对手信息容器
		JPanel chat_panel = new JPanel();							//聊天面板容器
		JPanel chatSouth = new JPanel();							//聊天面板南部容器
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JPanel my_north = new JPanel();
		JPanel my_center = new JPanel();
		JPanel oppo_north = new JPanel();
		JPanel oppo_center = new JPanel();
		
		
		JLabel tag2_lab = new JLabel(">>>>五子棋游戏房间<<<<");
		JLabel myName_lab = new JLabel();
		JLabel myImage_lab = new JLabel();
		JLabel opponentName_lab = new JLabel("          ");
		JLabel opponentImage_lab = new JLabel(null_image);
		
		ChessBoard cb = new ChessBoard(client, userName);
//		client.sendMSG("enter:");
		client.setMsgAera(dialog_area);
		client.setButton(request_btn, admitDefeat_btn, start_btn);
		client.setGameRoom(cb, gameHall_tab, myName_lab, myImage_lab, opponentName_lab, opponentImage_lab, myState_lab, oppoState_lab);
		client.enterGameRoom();
		
		gameHall_tab.addTab("游戏房间", gamerRoom_split);
		opponentInfo_tab.add("对手", opponentInfo_panel);
		myInfo_tab.add("自己", myInfo_panel);
		chat_tab.add("聊天", chat_panel);
		
//		游戏房间
		gamerRoom_split.setDividerSize(3);
		gamerRoom_split.setDividerLocation(270);	
		gamerRoom_split.setContinuousLayout(true);
		gamerRoom_split.setLeftComponent(playerInfo_split);
		gamerRoom_split.setRightComponent(chess_panel);
		playerInfo_split.setBackground(Color.WHITE);
		
		playerInfo_split.setDividerSize(3);
		playerInfo_split.setDividerLocation(490);	
		playerInfo_split.setContinuousLayout(true);
		playerInfo_split.setTopComponent(playerInfo_split1);
		playerInfo_split.setBottomComponent(chat_tab);
		playerInfo_split1.setBackground(Color.WHITE);
		
		playerInfo_split1.setDividerSize(3);
		playerInfo_split1.setDividerLocation(240);	
		playerInfo_split1.setContinuousLayout(true);
		playerInfo_split1.setTopComponent(opponentInfo_tab);
		playerInfo_split1.setBottomComponent(myInfo_tab);
		
		//我的信息和对手信息
		myState_lab.setForeground(Color.BLUE);
		myState_lab.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		oppoState_lab.setForeground(Color.BLUE);
		oppoState_lab.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		
		myName_lab.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		opponentName_lab.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		
		my_north.setLayout(new GridLayout(1, 3, 0, 0));
		my_north.setBackground(Color.WHITE);
		my_north.add(Box.createHorizontalStrut(0));
		my_north.add(myImage_lab);
		my_north.add(myName_lab);
		my_center.add(myState_lab);
		my_center.setBackground(Color.WHITE);
		
		myInfo_panel.setLayout(new BorderLayout());
		myInfo_panel.add(my_north, "North");
		myInfo_panel.add(my_center, "South");
		myInfo_panel.setBackground(Color.WHITE);
		
		oppo_north.setLayout(new GridLayout(1, 3, 0, 0));
		oppo_north.setBackground(Color.WHITE);
		oppo_north.add(Box.createHorizontalStrut(0));
		oppo_north.add(opponentImage_lab);
		oppo_north.add(opponentName_lab);
		oppo_center.add(oppoState_lab);
		oppo_center.setBackground(Color.WHITE);
		
		opponentInfo_panel.setLayout(new BorderLayout());
		opponentInfo_panel.add(oppo_north, "North");
		opponentInfo_panel.add(oppo_center, "South");
		opponentInfo_panel.setBackground(Color.WHITE);
		
		//聊天区域
		chatSouth.setLayout(new BorderLayout());
		chatSouth.add(message_field,"Center");
		chatSouth.add(send_btn,"East");
		chat_panel.setLayout(new BorderLayout());
		chat_panel.add(new JScrollPane(dialog_area),"Center");
		chat_panel.add(chatSouth,"South");
		
//		chat_panel.setLayout(new GridBagLayout());
//		GridBagConstraints chat_gb = new GridBagConstraints();
//		chat_gb.fill = GridBagConstraints.BOTH;
//		
//		chat_gb.gridy = 0;						//对话区域放在第0行
//		chat_gb.gridx = 0;						//对话区域放在第0列
//		chat_gb.weighty = 1;					//对话区域垂直可拉伸
//		chat_gb.weightx = 1;					//对话区域水平可拉伸
//		chat_gb.gridheight = 2;				//对话区域占用2行
//		chat_gb.gridwidth = 2;				//对话区域占用2列
//		chat_panel.add(new JScrollPane(dialog_area), chat_gb);
//		
//		chat_gb.gridy = 2;
//		chat_gb.gridx = 0;
//		chat_gb.weighty = 0;					//消息框垂直不可拉伸
//		chat_gb.weightx = 1;
//		chat_gb.gridheight = 1;
//		chat_gb.gridwidth = 1;
//		chat_panel.add(message_field, chat_gb);
//		
//		chat_gb.gridy = 2;
//		chat_gb.gridx = 1;
//		chat_gb.weighty = 0;
//		chat_gb.weightx = 1;
//		chat_gb.gridheight = 1;
//		chat_gb.gridwidth = 1;
//		chat_panel.add(send_btn, chat_gb);
//		chat_panel.setBackground(Color.WHITE);
		
		send_btn.setBackground(Color.WHITE);
		exit2_btn.setBackground(Color.WHITE);
		start_btn.setBackground(Color.WHITE);
		request_btn.setBackground(Color.WHITE);
		admitDefeat_btn.setBackground(Color.WHITE);
		
		north.add(tag2_lab, BorderLayout.CENTER);
		north.setBackground(Color.WHITE);
		
		center.setLayout(new GridLayout(1,1));
		center.setBackground(Color.WHITE);
		center.add(cb);
		
		south.add(exit2_btn);
		south.add(start_btn);
		south.add(request_btn);
		south.add(admitDefeat_btn);
		south.setBackground(Color.WHITE);
		
		chess_panel.setLayout(new BorderLayout());
		chess_panel.add(north, "North");
		chess_panel.add(center, "Center");
		chess_panel.add(south, "South");
		
		request_btn.setEnabled(false);
		admitDefeat_btn.setEnabled(false);
		send_btn.addActionListener(this);
		exit2_btn.addActionListener(this);
		start_btn.addActionListener(this);
		request_btn.addActionListener(this);
		admitDefeat_btn.addActionListener(this);
		
	}
	
	public void Display()
	{
		gameHall_tab.addTab("游戏大厅", gameHall_split);
		userInfo_tab.addTab("用户信息", userInfo_panel);
		severInfo_tab.addTab("服务器信息", severInfo_panel);
		
		userName_lab.setForeground(Color.BLUE);
		userName_lab.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		IP_lab.setForeground(Color.BLUE);
		IP_lab.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		onlineUser_lab.setForeground(Color.BLUE);
		onlineUser_lab.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		
		match_panel.setLayout(new BorderLayout());
		userInfo_panel.setLayout(new BorderLayout());
		userInfo_panel.setBackground(Color.WHITE);
		userInfo_panel.add(userPhoto_lab);
		userInfo_panel.add(userName_lab, "South");
		severInfo_panel.setLayout(new GridLayout(8, 1, 40, 0));
		severInfo_panel.add(IP_lab);
		severInfo_panel.add(onlineUser_lab);
		severInfo_panel.setBackground(Color.WHITE);
		
//		游戏大厅
//		gameHall_split.setEnabled(false);												//设置是否可拖拽分隔条
		gameHall_split.setDividerSize(3);												//设置分隔条的宽度
		gameHall_split.setDividerLocation(200);									//设置分隔条的位置
		gameHall_split.setContinuousLayout(true);								//随着分隔条的拖拽，组件大小随着改变
		gameHall_split.setLeftComponent(Info_split);							//设置左边的组件
		gameHall_split.setRightComponent(match_panel);					//设置右边的组件
		Info_split.setBackground(Color.WHITE);
		
//		Info_split.setEnabled(true);
//		Info_split.setOrientation(JSplitPane.VERTICAL_SPLIT);				//将面板更改为垂直方向
		Info_split.setDividerSize(3);
		Info_split.setDividerLocation(350);
		Info_split.setContinuousLayout(true);
		Info_split.setTopComponent(userInfo_tab);
		Info_split.setBottomComponent(severInfo_tab);
		
		//游戏大厅的一些布局
		JPanel north_pan = new JPanel();
		JPanel south_pan = new JPanel();
		
		Box[] box1 = new Box[15];
		Box[] box2 = new Box[15];
		Box[] box3 = new Box[15];
		Box[] box = new Box[15];
		
		south_pan.setLayout(new GridLayout(5, 3, 10, 10));
		
		match_panel.add(north_pan, "North");
		match_panel.add(south_pan);
		
		match_btn.setBackground(Color.WHITE);
		exit1_btn.setBackground(Color.WHITE);
		north_pan.setLayout(new GridLayout(1,5, 0, 0));
		north_pan.add(tag1_lab);
		north_pan.add(Box.createHorizontalStrut(0));
		north_pan.add(Box.createHorizontalStrut(0));
		north_pan.add(match_btn);
		north_pan.add(exit1_btn);
		north_pan.setBackground(Color.WHITE);
		
		south_pan.setBackground(new Color(81, 113, 158));
		
		for (int i = 0; i < 15; i++)
		{
			leftImage_btn[i] = new JButton(null_image);
			leftImage_btn[i].setOpaque(false);										//设置图片透明
			leftImage_btn[i].setContentAreaFilled(false);						//设置图片填满按钮所在的区域
			leftImage_btn[i].setMargin(new Insets(0, 0, 0, 0));				//设置按钮边框和标签文字之间的距离
			leftImage_btn[i].setFocusPainted(false);								//设置这个按钮是不是获得焦点
			leftImage_btn[i].setBorderPainted(true);								//设置是否绘制边框
			leftImage_btn[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
																										//设置粉色宽为2像素的圆角边框
			
			rightImage_btn[i] = new JButton(null_image);
			rightImage_btn[i].setOpaque(false);
			rightImage_btn[i].setContentAreaFilled(false);
			rightImage_btn[i].setMargin(new Insets(0, 0, 0, 0));
			rightImage_btn[i].setFocusPainted(false);
			rightImage_btn[i].setBorderPainted(true);
			rightImage_btn[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
			
			leftImage_btn[i].addActionListener(this);
			rightImage_btn[i].addActionListener(this);
			
			desk_lab = new JLabel(desk_image);
			leftName_lab[i] = new JLabel("          ");
			rightName_lab[i] = new JLabel("          ");
			orderNum_lab = new JLabel("<" + String.valueOf(i+1) + ">");
			
			leftName_lab[i].setForeground(Color.WHITE);
			rightName_lab[i].setForeground(Color.WHITE);
			orderNum_lab.setForeground(Color.WHITE);

			box1[i] = Box.createHorizontalBox();						//返回一个行排列的盒子
			box1[i].add(leftImage_btn[i]);
			box1[i].add(desk_lab);
			box1[i].add(rightImage_btn[i]);
			
			box2[i] = Box.createHorizontalBox();
			box2[i].add(leftName_lab[i]);
			box2[i].add(Box.createHorizontalStrut(60));			//添加宽度为90像素的不可见组件
			box2[i].add(rightName_lab[i]);
			
			box3[i] = Box.createHorizontalBox();
			box3[i].add(orderNum_lab);
			
			box[i] = Box.createVerticalBox();								//返回一个列排列的盒子
			box[i].add(Box.createVerticalStrut(15));					//添加高度为15像素的不可见组件
			box[i].add(box1[i]);
			box[i].add(box2[i]);
			box[i].add(box3[i]);
			box[i].add(Box.createVerticalStrut(15));
			
			south_pan.add(box[i]);
			
		}
		
		match_btn.addActionListener(this);
		exit1_btn.addActionListener(this);
		
		add(gameHall_tab);
		setTitle("五子棋");
		setSize(829, 850);
		setLocation(500, 100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JButton button = (JButton)e.getSource();
		
		for(int i = 0; i < 15; i++)
		{
			if (button == leftImage_btn[i])									//如果点击了左边的头像
			{
				if (leftName_lab[i].getText().equals("          "))			//如果左边的位置为空
				{
					for (int j = 0; j < 15; j++)										//寻找自己是否已在其他的位置
					{
						if (leftName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:left:" + j);
						if (rightName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:right:" + j);
						
						String msg = "setSit:left:" + userName + ":" + i;
						client.sendMSG(msg);
					}
					
					if(!isCreatNewTab)
					{
						isCreatNewTab = true;
						EnterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					else
					{
						client.enterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					
				}
			}
			
			if (button == rightImage_btn[i])
			{
				if (rightName_lab[i].getText().equals("          "))
				{
					for (int j = 0; j < 15; j++)
					{
						if (leftName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:left:" + j);
						if (rightName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:right:" + j);
						
						String msg = "setSit:right:" + userName + ":" + i;
						client.sendMSG(msg);
					}
					
					if(!isCreatNewTab)
					{
						isCreatNewTab = true;
						EnterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					else
					{
						client.enterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					
				}
			}
			
		}
		
		if (button == match_btn)
		{
			for (int i = 0; i < 15; i++)
			{
				if(leftName_lab[i].getText().equals("          "))
				{
					for (int j = 0; j < 15; j++)
					{
						if (leftName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:left:" + j);
						if (rightName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:right:" + j);
						
						String msg = "setSit:left:" + userName + ":" + i;
						client.sendMSG(msg);
					}
					
					if(!isCreatNewTab)
					{
						isCreatNewTab = true;
						EnterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					else
					{
						client.enterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					
				}
				else if(rightName_lab[i].getText().equals("          "))
				{
					for (int j = 0; j < 15; j++)
					{
						if (leftName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:left:" + j);
						if (rightName_lab[j].getText().equals(userName))
							client.sendMSG("cleanSit:right:" + j);
						
						String msg = "setSit:right:" + userName + ":" + i;
						client.sendMSG(msg);
					}
				
					if(!isCreatNewTab)
					{
						isCreatNewTab = true;
						EnterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					else
					{
						client.enterGameRoom();
						gameHall_tab.setEnabledAt(1, true);
						gameHall_tab.setSelectedIndex(1);
						break;
					}
					
				}
			}
		}
		
		if (button == exit1_btn)
		{
			int result = JOptionPane.showConfirmDialog(null, "确认退出?", "确认", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(result == JOptionPane.OK_OPTION)
                System.exit(0);
		}
		
		if (button == exit2_btn)
		{
			int result = JOptionPane.showConfirmDialog(null, "确认退出房间?", "确认", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(result == JOptionPane.OK_OPTION)
            	client.sendMSG("exitRoom:");
		}
		
		if(button == start_btn)
		{
			client.sendMSG("start:" + userName);
			myState_lab.setText("我准备好了~");
		}
		
		if(button == send_btn)
		{
			if(!message_field.getText().equals(""))
			{
				String text = message_field.getText();
				String msg = "msg:" + userName + "：" + text;
				message_field.setText("");
				client.sendMSG(msg);
			}
		}
		
		if(button == request_btn)
			client.sendMSG("request:");
		
		if(button == admitDefeat_btn)
			client.sendMSG("defeat:");
		
		
		
		
	}
	
}












