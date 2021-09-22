package 客户端与服务端;

import java.awt.Font;
import java.awt.Label;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import 游戏界面.ChessBoard;

public class Client extends Thread
{
	String name = null;
	String imagePath = null;
	Socket socket = null;
	DataInputStream dataIn = null;
	DataOutputStream dataOut = null;
	
	Timer mytime = null;
	Timer oppotime = null;
	
	JLabel[] leftName_lab = null;
	JLabel[] rightName_lab = null;
	JButton[] leftImage_btn = null;
	JButton[] rightImage_btn = null;
	ChessBoard chessboard = null;
	JLabel onlineUser_lab = null;
	JLabel myState_lab = null;
	JLabel oppoState_lab = null;
	JLabel myName_lab = null;
	JLabel myImage_lab = null;
	JLabel opponentName_lab = null;
	JLabel opponentImage_lab = null;
	JTextArea dialog_area = null;
	JButton start_btn = null;
	JButton request_btn = null;
	JButton admitDefeat_btn = null;
	JTabbedPane gameHall_tab = null;
	
	public void connectServer()											//连接到服务端
	{
		try
		{
			socket = new Socket("127.0.0.1", 12345);
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
			this.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void enterGameRoom()
	{
		ImageIcon userImage = new ImageIcon(imagePath);
		myName_lab.setText(name);
		myImage_lab.setIcon(userImage);
	}
	
	public void setUserInfo(String name, String imagePath)
	{
		this.name = name;
		this.imagePath = imagePath;
		try
		{
			dataOut.writeUTF(this.name + "," + this.imagePath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setDesk(JLabel[] ll, JLabel[] rl, JButton[] lb, JButton[] rb, JLabel ol)
	{
		leftName_lab = ll;
		leftImage_btn = lb;
		rightName_lab = rl;
		rightImage_btn = rb;
		onlineUser_lab = ol;
	}
	
	public void setGameRoom(ChessBoard cb, JTabbedPane gt, JLabel mn, JLabel mi, JLabel on, JLabel oi, JLabel ms, JLabel os)
	{
		chessboard = cb;
		myName_lab = mn;
		myImage_lab = mi;
		gameHall_tab = gt;
		myState_lab = ms;
		oppoState_lab = os;
		opponentName_lab = on;
		opponentImage_lab = oi;
	}
	
	public void setButton(JButton ask, JButton defeat, JButton start)
	{
		start_btn = start;
		request_btn = ask;
		admitDefeat_btn = defeat;
	}
	
	public void setMsgAera(JTextArea da)
	{
		dialog_area = da;
	}
	
	public void run()
	{
		String receMsg = null;
		myTimeThread mytime = new myTimeThread();
		oppoTimeThread oppotime = new oppoTimeThread();
		
		while (true)
		{
			try			//处理信息
			{
				receMsg = dataIn.readUTF();
				String station = receMsg.substring(0, receMsg.indexOf(":"));									//获取状态标识
				String msg = receMsg.substring(receMsg.indexOf(":") + 1, receMsg.length()); 		//获取内容
//				System.out.println( "获取到的标识：" + station);
//				System.out.println("接收到的信息："+msg);
				
				if(station.equals("online"))
					onlineUser_lab.setText("当前在线人数：" + msg);
				
				if(station.equals("setSit"))
					setSit(msg);
				
				if(station.equals("cleanSit"))
					cleanSit(msg);
				
				if(station.equals("enter"))
					setRoomInfo(msg);
					
				if(station.equals("saveChess"))
				{
					saveChess(msg);
//					String[]str = msg.split(":");
//					if(str[0].equals(name))
//					{
//						mytime.stop = true;
//						oppoState_lab.setText("等待对方下棋中~");
//					}
				}
				
				if(station.equals("win") || station.equals("lost"))
				{
					setButton(false);
					chessboard.WinORLost(station);
				}
				
				if(station.equals("start"))
					oppoState_lab.setText("我准备好了~");
				
				if(station.equals("init"))
				{
					setButton(true);
					chessboard.InitChessBoard();
				}
				
//				if(station.equals("time"))
//				{
//					if(msg.equals(name))
//					{
//						mytime.stop = false;
//						mytime.start();
//						oppoState_lab.setText("等待对方下棋中~");
//					}
//					else
//					{
//						oppotime.stop = false;
//						oppotime.start();
//						myState_lab.setText("等待对方下棋中~");
//					}
//				}
				
				if(station.equals("msg"))
					setMessage(msg);
				
				if(station.equals("request"))
					chessboard.Request(station);
				
				if(station.equals("AgreeRequest"))
				{
					setButton(false);
					chessboard.processInfo(station);
				}
				
				if(station.equals("DisagreeRequest"))
					chessboard.processInfo(station);
				
				if(station.equals("defeat"))
					chessboard.Request(station);
				
				if(station.equals("AgreeRequest"))
					setButton(false);
				
				if(station.equals("DisagreeDefeat"))
					chessboard.processInfo(station);
				
				if(station.equals("can'tExit"))
					chessboard.processInfo(station);
				
				if(station.equals("cantExit") || station.equals("oppoExit"))
				{
					setButton(false);
					chessboard.InitChessBoard();
					setRoomInfo(receMsg);
				}
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void setSit(String msg)
	{
//		System.out.println("-" + msg + "-");
		String[]str = msg.split(":");
		int i = Integer.valueOf(str[3]);
		ImageIcon userImage = new ImageIcon(str[2]);
		
		if(str[0].equals("left"))
		{
			leftImage_btn[i].setIcon(userImage);
			leftName_lab[i].setText(str[1]);
		}
		
		if(str[0].equals("right"))
		{
			rightImage_btn[i].setIcon(userImage);
			rightName_lab[i].setText(str[1]);
		}
		
	}
	
	public void cleanSit(String msg)
	{
		String[]str = msg.split(":");
		ImageIcon null_image = new ImageIcon("./src/图片/资源包/res/img/noone.gif");
		
		if(str[0].equals("left"))
		{
			int i = Integer.valueOf(str[1]);
			leftImage_btn[i].setIcon(null_image);
			leftName_lab[i].setText("          ");
		}
		
		else if(str[0].equals("right"))
		{
			int i = Integer.valueOf(str[1]);
			rightImage_btn[i].setIcon(null_image);
			rightName_lab[i].setText("          ");
		}
		
	}
	
	public void setRoomInfo(String msg)
	{
		String[]str = msg.split(":");
			
		if(str[0].equals("opponent"))
		{
			ImageIcon userImage = new ImageIcon(str[2]);
			opponentName_lab.setText(str[1]);
			opponentImage_lab.setIcon(userImage);
			oppoState_lab.setText("未准备");
		}
		
		else if(str[0].equals("cantExit"))
		{
			ImageIcon null_image = new ImageIcon("./src/图片/资源包/res/img/noone.gif");
			myName_lab.setText("          ");
			myImage_lab.setIcon(null_image);
			opponentName_lab.setText("          ");
			opponentImage_lab.setIcon(null_image);
			gameHall_tab.setEnabledAt(1, false);
			gameHall_tab.setSelectedIndex(0);
		}
		
		else if(str[0].equals("oppoExit"))
		{
			ImageIcon null_image = new ImageIcon("./src/图片/资源包/res/img/noone.gif");
			opponentName_lab.setText("          ");
			opponentImage_lab.setIcon(null_image);
		}
		
	}

	public void setButton(boolean isEnable)
	{
		if(isEnable)
		{
			start_btn.setEnabled(false);
			request_btn.setEnabled(true);
			admitDefeat_btn.setEnabled(true);
		}
		else if(!isEnable)
		{
			start_btn.setEnabled(true);
			request_btn.setEnabled(false);
			admitDefeat_btn.setEnabled(false);
		}
	}
	
	public void saveChess(String msg)
	{
		String[]str = msg.split(":");
		int row = Integer.valueOf(str[1]);
		int col = Integer.valueOf(str[2]);
		int chess = Integer.valueOf(str[3]);
		chessboard.SaveChess(row, col, chess);
	}
	
	public void sendMSG(String msg)
	{
		try {
			dataOut.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setMessage(String msg)
	{
		dialog_area.setFont(new Font("微软雅黑", Font.LAYOUT_LEFT_TO_RIGHT, 14));
		dialog_area.append(msg + "\n\n");
	}

//	public void startMyTime()
//	{
//		mytime = new Timer();
//		mytime.schedule(task, time);
//	}
	
	class myTimeThread extends Thread
	{
		boolean stop = false;
		
		public void run()
		{
			int time = 30;
			
			while(time >= 0)
			{
				if(stop)
					break;
				
				//更新界面，显示倒计时
				String text1 = "本步剩余时间：" + time;
				String text2 = "快点啊，对手等到花都谢了~：" + time + "s";
				String text3 = "快点啊，我等到花都谢了~：";
				
				if(time < 5)
					SwingUtilities.invokeLater(() -> { myState_lab.setText(text2); oppoState_lab.setText(text3);});
				else
					SwingUtilities.invokeLater(() -> { myState_lab.setText(text1); });
				
				time = time - 1;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	class oppoTimeThread extends Thread
	{
		boolean stop = false;
		
		public void run()
		{
			int time = 30;
			
			while(time >= 0)
			{
				if(stop)
					break;
				
				//更新界面，显示倒计时
				String text1 = "本步剩余时间：" + time + "s";
				String text2 = "快点啊，对手等到花都谢了~：" + time + "s";
				String text3 = "快点啊，我等到花都谢了~：";
				
				if(time < 5)
					SwingUtilities.invokeLater(() -> { oppoState_lab.setText(text2); myState_lab.setText(text3);});
				else
					SwingUtilities.invokeLater(() -> { oppoState_lab.setText(text1); });
				
				time = time - 1;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	
}







