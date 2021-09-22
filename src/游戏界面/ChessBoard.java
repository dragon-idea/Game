package 游戏界面;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import 客户端与服务端.Chess;
import 客户端与服务端.Client;

public class ChessBoard extends JPanel implements MouseMotionListener, MouseListener
{
	static final int ROWS = 15;				//定义棋盘的行数，是一个常量
	static final int COLS = 15;					//定义棋盘的列数，是一个常量
	static final int MARGIN = 23;			//棋盘的边距
	static final int GRID_SPAN = 35;		//网格间距
	
	Client client;
	String userName;
	
	Image chessBoardImage;
	Image whiteChess;
	Image blackChess;
	Image position;
	
	int mouseX;											//鼠标的x值
    int mouseY;											//鼠标的y值
    int[][] chessMatrix = new int[ROWS][COLS];				//把棋盘的所有交点看做一个矩阵
    
    int chessNum;										//棋子个数
    boolean isWhite;
    boolean isGameOver;
	
	public ChessBoard(Client client, String userName)
	{
		//获取图片
		chessBoardImage = Toolkit.getDefaultToolkit().getImage("./src/图片/资源包/res/wuzi/board1.gif");
		whiteChess = Toolkit.getDefaultToolkit().getImage("./src/图片/资源包/res/wuzi/baiqi.gif");
		blackChess = Toolkit.getDefaultToolkit().getImage("./src/图片/资源包/res/wuzi/heiqi.gif");
		position = Toolkit.getDefaultToolkit().getImage("./src/图片/资源包/res/wuzi/position1.gif");
	
		this.client = client;
		this.userName = userName;
		InitChessBoard();
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
        this.repaint();
        
	}
	
	public void InitChessBoard()							//将棋盘初始化为空，没有棋子
	{
		for(int row = 0; row < chessMatrix.length; row++)
			for(int col = 0; col < chessMatrix[row].length; col++)
				this.chessMatrix[row][col]= Chess.BLANK;
		
		repaint();
	}
	
	private void showPosition(Graphics g)
	{
		int px = mouseX - mouseX % GRID_SPAN;
		int py = mouseY - mouseY % GRID_SPAN;

		g.drawImage(position, px + 5, py + 5, 34, 34, this);
			
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(chessBoardImage, 0, 0, this);
		
		if (mouseX > 5 && mouseX < 525 && mouseY > 5 && mouseY < 525)			//在一定范围内才出现定位框
		{
			int i = (mouseX - mouseX % GRID_SPAN) / GRID_SPAN;								//转化为矩阵的行数
			int j = (mouseY - mouseY % GRID_SPAN) / GRID_SPAN;								//转化为矩阵的列数
			
			if(chessMatrix[j][i] == Chess.BLANK)																//如果棋盘的交点没有棋子
				showPosition(g);

		}
		
		for (int row = 0; row < chessMatrix.length; row++)
		{
			for (int col = 0; col < chessMatrix[row].length; col++)
			{
				if (chessMatrix[row][col] == Chess.BLACK)
				{
					int chessX = col*GRID_SPAN + 10;
					int chessY = row*GRID_SPAN + 10;
					g.drawImage(blackChess, chessX, chessY, 25, 25, this);
				}
				else if(chessMatrix[row][col] == Chess.WHITE)
				{
					int chessX = col*GRID_SPAN + 10;
					int chessY = row*GRID_SPAN + 10;
					g.drawImage(whiteChess, chessX, chessY, 25, 25, this);
				}
				
			}
		}
		repaint();
		
	}
	
	public void SaveChess(int row, int col, int chess)
	{
		chessNum++;
		this.chessMatrix[row][col] = chess;
		repaint();
	}

	public void WinORLost(String msg)
	{
		if(msg.equals("win"))
			JOptionPane.showMessageDialog(this, "恭喜，你赢啦~", "结束", JOptionPane.INFORMATION_MESSAGE);
		else if(msg.equals("lost"))
			JOptionPane.showMessageDialog(this, "真遗憾，对手太强了~", "结束", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void Request(String msg)
	{
		if(msg.equals("request"))
		{
			int result = JOptionPane.showConfirmDialog(null, "对手请求平局，是否同意？", "确认", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(result == JOptionPane.OK_OPTION)
				client.sendMSG("AgreeRequest:");
			else
				client.sendMSG("DisagreeRequest:");
		}
		
		else if(msg.equals("defeat"))
		{
			int result = JOptionPane.showConfirmDialog(null, "对手请求认输，是否同意？", "确认", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(result == JOptionPane.OK_OPTION)
				client.sendMSG("AgreeDefeat:");
			else
				client.sendMSG("DisagreeDefeat:");
		}
		
	}
	
	public void processInfo(String msg)
	{
		if(msg.equals("AgreeRequest"))
			JOptionPane.showMessageDialog(this, "平局~", "结束", JOptionPane.INFORMATION_MESSAGE);
		
		else if(msg.equals("DisagreeRequest"))
			JOptionPane.showMessageDialog(this, "对手不同意平局~", "继续", JOptionPane.INFORMATION_MESSAGE);
		
		else if(msg.equals("DisagreeDefeat"))
			JOptionPane.showMessageDialog(this, "对手不同意你认输~", "继续", JOptionPane.INFORMATION_MESSAGE);
		
		else if(msg.equals("can'tExit"))
			JOptionPane.showMessageDialog(this, "不能走，决战到天亮~", "错误", JOptionPane.ERROR_MESSAGE);
		
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if (mouseX > 5 && mouseX < 525 && mouseY > 5 && mouseY < 525)			//在一定范围内才出现定位框
		{
			int row = (mouseY - mouseY % GRID_SPAN) / GRID_SPAN;						//转化为矩阵的行数
			int col = (mouseX - mouseX % GRID_SPAN) / GRID_SPAN;							//转化为矩阵的列数

//			System.out.println("行：" + row);
//			System.out.println("列：" + col);

			if(chessMatrix[row][col] == Chess.BLANK)												//如果棋盘的交点没有棋子
			{
				String msg = "saveChess:" + userName + ":" + row + ":" + col;
//				System.out.println(msg);
				client.sendMSG(msg);
			}
					
		}
		
	}
	
	public void mouseMoved(MouseEvent e)
	{
		if(!isGameOver)
		{
			mouseX=e.getX();
			mouseY=e.getY();
//			System.out.println("鼠标的X值：" + mouseX);
//			System.out.println("鼠标的Y值：" + mouseY);
			repaint();
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent arg0) {}
	
	public void mouseReleased(MouseEvent arg0) {}
	
	public void mouseDragged(MouseEvent arg0) {}
	
}
