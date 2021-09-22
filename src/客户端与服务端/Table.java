package 客户端与服务端;

public class Table
{
	User[] leftUser;
	User[] rightUser;
	boolean[] leftSit = null;
	boolean[] rightSit = null;
	boolean[] isGameOver = null;
	int[] whiteChessNum = null;
	int[] blackChessNum = null;
	ChessMatrix[] chessArray = null;
	
	public Table()
	{
		leftUser = new User[15];
		rightUser = new User[15];
		leftSit = new boolean[15];
		rightSit = new boolean[15];
		isGameOver = new boolean[15];
		whiteChessNum = new int[15];
		blackChessNum = new int[15];
		chessArray = new ChessMatrix[15];
		
		for (int i = 0; i < 15; i++)
		{
			leftSit[i] = false;
			rightSit[i] = false;
			isGameOver[i] = true;
			leftUser[i] = new User();
			rightUser[i] = new User();
			leftUser[i].isReady = false;
			rightUser[i].isReady = false;
			whiteChessNum[i] = 0;
			blackChessNum[i] = 0;
			chessArray[i] = new ChessMatrix();
		}
		
	}
	
}	

	
	
		
		
	
	
