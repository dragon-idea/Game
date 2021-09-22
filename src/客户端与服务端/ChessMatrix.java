package 客户端与服务端;

public class ChessMatrix
{
	int[][] chessMatrix;
	static final int ROWS = 15;				//定义棋盘的行数，是一个常量
	static final int COLS = 15;					//定义棋盘的列数，是一个常量
	
	public ChessMatrix()
	{
		chessMatrix = new int[ROWS][COLS];
		initMatrix();
	}
	
	public void initMatrix()
	{
		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLS; j++)
				chessMatrix[i][j] = Chess.BLANK;
	}
	
	public int IsWin(int row, int col, int chess)
	{
		int chessLinkedCount = 1;							// 连接棋子数 
		
		//判断一行中是否有5颗相同的棋子
		for(int x = row - 1; x >= 0; x--)					//横向向左寻找 
		{
			if(chessMatrix[x][col] == chess)
				chessLinkedCount++; 
			else
				break; 
		}
		for(int x = row + 1; x < ROWS; x++)			//横向向右寻找
		{ 
			if(chessMatrix[x][col] == chess)
				chessLinkedCount++;
			else
				break;
		}
		if(chessLinkedCount < 5)								//判断记录数<5，重新判断
			chessLinkedCount=1;

		//判断一列中是否有5颗相同的棋子
		for(int y = col - 1; y >= 0; y--)					//纵向向上寻找
		{
			if(chessMatrix[row][y] == chess)
				chessLinkedCount++;
			else
				break;
		}
		for(int  y = col+1; y < COLS; y++)				//纵向向下寻找
		{
			if(chessMatrix[row][y] == chess)
				chessLinkedCount++;
			else
				break;
		}
		if(chessLinkedCount < 5)								//判断记录数<5，重新判断
			chessLinkedCount=1;
		
		//判断左上-右下方向是否有5颗棋子
		for(int x = row + 1, y = col + 1; x < ROWS && y < COLS; x++, y++)			//右下寻找
		{
			if(chessMatrix[x][y] == chess)
				chessLinkedCount++;
			else
				break;
		} 
		for(int x = row - 1, y = col - 1; x >= 0 && y >= 0; x--, y--)						//左上寻找
		{
			if(chessMatrix[x][y] == chess)
				chessLinkedCount++;
			else
				break;
		} 
		if(chessLinkedCount < 5)								//判断记录数<5，重新判断
			chessLinkedCount=1;
		
		//判断左下-右上方向是否有5颗棋子
		for(int x = row - 1, y = col + 1; x >= 0 && y < COLS; x--, y++)				//左下寻找
		{
			if(chessMatrix[x][y] == chess)
				chessLinkedCount++;
			else
				break;
		} 
		for(int x = row + 1, y = col - 1; x < ROWS && y >= 0; x++, y--)				//右上寻找
		{
			if(chessMatrix[x][y] == chess)
				chessLinkedCount++;
			else
				break;
		}
		if(chessLinkedCount < 5)								//判断记录数<5，重新判断
			chessLinkedCount=1;
		
		if(chessLinkedCount >= 5)																			//判断记录数大于等于五，即表示此方获胜
		{
			if(chess == Chess.BLACK)
				return Chess.BLACK;
			else if(chess == Chess.WHITE)
				return Chess.WHITE;
		}
		else
			chessLinkedCount = 1;
		
		return Chess.BLANK;
		
	}
	
}


