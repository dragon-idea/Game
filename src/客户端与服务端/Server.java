package 客户端与服务端;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server extends Thread
{
	Table tableList = new Table();												//存放User对象的数组
	Vector<User> userList = new Vector<User>();					//存放User对象的数组
	
	public void waitConnect()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(12345);			//12345是端口
			
			while(true)																					//循环监听等待客户端的连接
			{
				Socket server = serverSocket.accept();
//				System.out.println("成功连接服务端");
				DataInputStream dataIn = new DataInputStream(server.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(server.getOutputStream());
				String userInfo = dataIn.readUTF();											//读取新用户的名字
//				System.out.println(userInfo);
				
				if (userInfo.contains(","))
				{
					String[] str = userInfo.split(",");
					String userName = str[0];
					String imagePath = str[1];
					
					User user = new User(userName, imagePath, dataIn, dataOut);				//创建新对象（拥有三个属性）
					userList.add(user);
					
					ReadMsgThread read = new ReadMsgThread(user, userList, tableList);
					read.sendAllUser("online:" + userList.size());
					read.start();
				}
//				Iterator<User> it = userList.iterator();										//使用迭代器逐个遍历已登录的用户
//				while(it.hasNext())																	//如果存在下一个用户
//					System.out.println(it.next().name);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		Server server = new Server();
		server.waitConnect();
	}
	
}

class ReadMsgThread extends Thread
{
	User user;																			//接收新用户
	Table tableList = null;
	Vector<User> userList  = null;										//存放着已登录用户的数组
	
	public ReadMsgThread(User user, Vector<User> userList, Table tableList)
	{
		super();
		this.user = user;
		this.userList = userList;
		this.tableList = tableList;
	}

	public void sendAllUser(String msg)						//给除所有的用户发信息
	{
		try
		{
			for (User uu : userList)
//				if(uu.getName().equals("          "))
					uu.getDataOutput().writeUTF(msg); 				//给已经登录的用户发送有新用户登录的信息
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendTwoOfUs(String msg)					//给自己和对手发送信息
	{
		try
		{
			if(user.LeftOrRight == 1)
			{
				user.getDataOutput().writeUTF(msg);
				for (User uu : userList)
					if(uu.getName().equals(tableList.rightUser[user.number].name))
						uu.getDataOutput().writeUTF(msg);
			}
			else if(user.LeftOrRight == 2)
			{
				user.getDataOutput().writeUTF(msg);
				for (User uu : userList)
					if(uu.getName().equals(tableList.leftUser[user.number].name))
						uu.getDataOutput().writeUTF(msg);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void sendToOpponent(String msg)
	{
		try
		{
			if(user.LeftOrRight == 1)
			{
				for (User uu : userList)
					if(uu.getName().equals(tableList.rightUser[user.number].name))
						uu.getDataOutput().writeUTF(msg);
			}
			else if(user.LeftOrRight == 2)
			{
				for (User uu : userList)
					if(uu.getName().equals(tableList.leftUser[user.number].name))
						uu.getDataOutput().writeUTF(msg);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void sendToMyself(String msg)
	{
		try {
			user.getDataOutput().writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendSitInfo()
	{
		for (int i = 0; i < 15; i++)
		{
			if(tableList.leftSit[i])
			{
				String name = tableList.leftUser[i].name;
				String imagePath = tableList.leftUser[i].imagePath;
				String msg = "setSit:left:" + name + ":" + imagePath + ":" + i;
				sendAllUser(msg);
			}
			
			if(tableList.rightSit[i])
			{
				String name = tableList.rightUser[i].name;
				String imagePath = tableList.rightUser[i].imagePath;
				String msg = "setSit:right:" + name + ":" + imagePath + ":" + i;
				sendAllUser(msg);
			}
		}
	}
	
	public void cleanSit(String msg)
	{
		String[] str = msg.split(":");
		
		if(str[0].equals("left"))
		{
			String info = "cleanSit:" + msg;
			sendAllUser(info);
		}
		
		else if(str[0].equals("right"))
		{
			String info = "cleanSit:" + msg;
			sendAllUser(info);
		}
			
	}
	
	public void sendRoomInfo1()
	{
		int i = user.number;
		if(i >= 0 && i < 15)
		{
			if(tableList.rightSit[i])
			{
				for (User uu : userList)
				{
					if(!uu.getName().equals("          ") && uu.getName().equals(tableList.rightUser[i].name))
					{
						String myName = user.name;
						String myImagePath = user.imagePath;
						String myInfo = "enter:opponent:" + myName + ":" + myImagePath;
						try {
							uu.getDataOutput().writeUTF(myInfo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}
	
	public void sendRoomInfo2()
	{
		int i = user.number;
		if(i >= 0 && i < 15)
		{
			if(tableList.leftSit[i])
			{
				for (User uu : userList)
				{
					if(!uu.getName().equals("          ") && uu.getName().equals(tableList.leftUser[i].name))
					{
						String myName = user.name;
						String myImagePath = user.imagePath;
						String myInfo = "enter:opponent:" + myName + ":" + myImagePath;
						try {
							uu.getDataOutput().writeUTF(myInfo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}
	
	public void run()
	{
		String info = null;
		while(true)
		{
			try {
				info = user.getDataInput().readUTF();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String[] str = info.split(":");									//用冒号作为分隔符
			
			if(!str[0].equals("saveChess"))
			{
				sendSitInfo();
				
				if(str[0].equals("setSit"))
				{
					int i = Integer.valueOf(str[3]);
					
					if (!tableList.leftSit[i])
					{
						if(str[1].equals("left"))
						{
							for (User uu : userList)
							{
								if (uu.getName().equals(str[2]))
								{
									user.number = i;
									user.LeftOrRight = 1;
									tableList.leftSit[i] = true;
									tableList.leftUser[i].setName(str[2]);
									tableList.leftUser[i].setImagePath(uu.getImagePath());
									sendSitInfo();
									sendRoomInfo1();
									break;
								}
							}
						}
					}
					
					if (!tableList.rightSit[i])
					{
						if(str[1].equals("right"))
						{
							for (User uu : userList)
							{
								if (uu.getName().equals(str[2]))
								{
									user.number = i;
									user.LeftOrRight = 2;
									tableList.rightSit[i] = true;
									tableList.rightUser[i].setName(str[2]);
									tableList.rightUser[i].setImagePath(uu.getImagePath());
									sendSitInfo();
									sendRoomInfo2();
									break;
								}
							}
						}
					}
					
				}
				
				if(user.LeftOrRight == 1)
					sendRoomInfo1();
				if(user.LeftOrRight == 2)
					sendRoomInfo2();
				
				if(str[0].equals("cleanSit"))
				{
					int i = Integer.valueOf(str[2]);
					if(str[1].equals("left"))
					{
						tableList.leftSit[i] = false;
						tableList.leftUser[i].setName("          ");
						tableList.leftUser[i].setImagePath(null);
						cleanSit("left:" + i);
					}
					
					if(str[1].equals("right"))
					{
						tableList.rightSit[i] = false;
						tableList.rightUser[i].setName("          ");
						tableList.rightUser[i].setImagePath(null);
						cleanSit("right:" + i);
					}
					
				}
			}
			
			if(str[0].equals("msg"))
				sendTwoOfUs(info);
			
			if(str[0].equals("start"))
			{
				int i = user.number;
				sendToOpponent("start:");
				if(tableList.leftUser[i].name.equals(str[1]))
					tableList.leftUser[i].isReady = true;
				else if(tableList.rightUser[i].name.equals(str[1]))
					tableList.rightUser[i].isReady = true;
					
				if(tableList.leftUser[i].isReady && tableList.rightUser[i].isReady)
				{
					tableList.whiteChessNum[i] = 0;
					tableList.blackChessNum[i] = 0;
					tableList.isGameOver[i] = false;
					tableList.chessArray[i].initMatrix();
					sendTwoOfUs("init:");
//					sendTwoOfUs("time:" + tableList.leftUser[i].name);
				}
			}
			
			if(str[0].equals("request"))
			{
				tableList.isGameOver[user.number] = true;
				sendToOpponent(info);
			}
			
			if(str[0].equals("AgreeRequest"))
			{
				tableList.isGameOver[user.number] = true;
				tableList.leftUser[user.number].isReady = false;
				tableList.rightUser[user.number].isReady = false;
				sendTwoOfUs(info);
			}
			
			if(str[0].equals("DisagreeRequest"))
			{
				tableList.isGameOver[user.number] = false;
				sendToOpponent(info);
			}
			
			if(str[0].equals("defeat"))
			{
				tableList.isGameOver[user.number] = true;
				sendToOpponent(info);
			}
			
			if(str[0].equals("AgreeDefeat"))
			{
				tableList.isGameOver[user.number] = true;
				tableList.leftUser[user.number].isReady = false;
				tableList.rightUser[user.number].isReady = false;
				sendTwoOfUs(info);
				try
				{
					if(user.LeftOrRight == 1)
					{
						user.getDataOutput().writeUTF("win:");
						for (User uu : userList)
							if(uu.getName().equals(tableList.rightUser[user.number].name))
								uu.getDataOutput().writeUTF("lost:");
					}
					else if(user.LeftOrRight == 2)
					{
						user.getDataOutput().writeUTF("win:");
						for (User uu : userList)
							if(uu.getName().equals(tableList.leftUser[user.number].name))
								uu.getDataOutput().writeUTF("lost:");
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			if(str[0].equals("DisagreeDefeat"))
			{
				tableList.isGameOver[user.number] = false;
				sendToOpponent(info);
			}
			
			if(str[0].equals("exitRoom"))
			{
				if(!tableList.isGameOver[user.number])
					sendToMyself("can'tExit:");
				else
				{
					int i = user.number;
					sendToMyself("cantExit:");
					sendToOpponent("oppoExit:");
//					sendTwoOfUs("init:");
					
					if(user.LeftOrRight == 1)
					{
						cleanSit("left:" + i);
						tableList.leftSit[i] = false;
						tableList.leftUser[i].isReady = false;
						tableList.leftUser[i].name = "          ";
						tableList.leftUser[i].imagePath = null;
					}
					
					else if(user.LeftOrRight == 2)
					{
						cleanSit("right:" + i);
						tableList.rightSit[i] = false;
						tableList.rightUser[i].isReady = false;
						tableList.rightUser[i].name = "          ";
						tableList.rightUser[i].imagePath = null;
					}
					
					user.number = -1;
					user.LeftOrRight = 0;
					
				}
			}
			
			if(str[0].equals("saveChess"))
			{
				int i = user.number;
				if(!tableList.isGameOver[i])
				{
					int chess = 0;
					String msg = null;
					int row = Integer.valueOf(str[2]);
					int col = Integer.valueOf(str[3]);
					int white = tableList.whiteChessNum[i];
					int black = tableList.blackChessNum[i];
					
					if(white == black  && tableList.leftUser[i].name.equals(str[1]))					//左边下白棋
					{
						chess = Chess.WHITE;
						tableList.whiteChessNum[i]++;
						tableList.chessArray[i].chessMatrix[row][col] = chess;
//						sendTwoOfUs("time:" + tableList.rightUser[i].name);
					}
					else if(white == (black + 1) && tableList.rightUser[i].name.equals(str[1]))		//右边下黑棋
					{
						chess = Chess.BLACK;
						tableList.blackChessNum[i]++;
						tableList.chessArray[i].chessMatrix[row][col] = chess;
//						sendTwoOfUs("time:" + tableList.leftUser[i].name);
					}
					msg = info + ":" + chess;
					sendTwoOfUs(msg);
					
					int win = tableList.chessArray[i].IsWin(row, col, chess);
					try
					{
						if(win == Chess.WHITE)
						{
							for (User uu : userList)
							{
								if(uu.getName().equals(tableList.leftUser[i].name))
									uu.getDataOutput().writeUTF("win:");
								if(uu.getName().equals(tableList.rightUser[i].name))
									uu.getDataOutput().writeUTF("lost:");
							}
							tableList.isGameOver[i] = true;
							tableList.leftUser[i].isReady = false;
							tableList.rightUser[i].isReady = false;
						}
						else if(win == Chess.BLACK)
						{
							for (User uu : userList)
							{
								if(uu.getName().equals(tableList.leftUser[i].name))
										uu.getDataOutput().writeUTF("lost:");
								if(uu.getName().equals(tableList.rightUser[i].name))
									uu.getDataOutput().writeUTF("win:");
							}
							tableList.isGameOver[i] = true;
							tableList.leftUser[i].isReady = false;
							tableList.rightUser[i].isReady = false;
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			
			
		}
	}
	
}



