
public class Simulation {
	int column;
	int row;
	int countAlive; //for test
	int[][] board;

	public Simulation(int column, int row) {
		this.column = column;
		this.row = row;
		this.board = new int[row][column];	
	}

	public void setAlive(int x, int y) {
		if(x<0) {
			x=column+x;
		}else if(x>column-1){
			x=x-column;
		}
		if(y<0) {
			y=row+y;
		}else if(y>row-1) {
			y=y-row;
		}			
		this.board[y][x] = 1;
	}
	public void setDead(int x, int y) {
		if(x<0) {
			x=column+x;
		}else if(x>column-1){
			x=x-column;
		}
		if(y<0) {
			y=row+y;
		}else if(y>row-1) {
			y=y-row;
		}		
		this.board[y][x] = 0;
	}
		
	public void printBoard() {
		System.out.println("---");
		for(int x = 0; x < column; x++)  
		{
			String line = "|";
			for(int y = 0; y < row; y++){
				if(this.board[x][y] == 0) {
					line += ".";
				}else {
					line += "*";
				}
			}
			line += "|";
			System.out.println(line);
		}
		System.out.println("---\n");
	}

//	public void printBoard() {
//		System.out.println("---");
//		for(int x = 0; x < width; x++)  
//		{
//			String line = "|";
//
//			for(int y = 0; y < height; y++){
//				if(this.board[x][y] == 0) {
//					line += ".";
//				}else {
//					line += "*";
//				}
//			}
//			line += "|";
//			System.out.println(line);
//		}
//		System.out.println("---\n");
//	}

	public int getState(int x, int y) {
		//boundary check
//		if (x<0 || x>column-1) {
//			return 0;
//		}
//		if (y<0 || y>row-1) {
//			return 0;
//		}
	
		//casting 3D to 2D
		if(x<0) {
			x=column+x;
		}else if(x>column-1){
			x=x-column;
		}
		if(y<0) {
			y=row+y;
		}else if(y>row-1) {
			y=y-row;
		}			
		return this.board[y][x];
	}

	public int countLiveNeighbours(int x, int y) {

		int count = 0;

		//left
		if (x==0 && y!=0 && y!=row-1) {
			count+= board[y-1][column-1];
			count+= board[y][column-1];
			count+= board[y+1][column-1];
			
			count+= board[y-1][x];
			count+= board[y-1][x+1];
			count+= board[y][x+1];
			count+= board[y+1][x];
			count+= board[y+1][x+1];
		}
		//right
		else if (x==column-1 && y!=0 && y!=row-1) {
			count+= board[y-1][0];
			count+= board[y][0];
			count+= board[y+1][0];
			
			count+= board[y-1][x-1];
			count+= board[y-1][x];
			count+= board[y][x-1];
			count+= board[y+1][x-1];
			count+= board[y+1][x];
		}
		//top
		else if (y==0 && x!=0 && x!= column-1) {
			count+= board[row-1][x-1];
			count+= board[row-1][x];
			count+= board[row-1][x+1];
			
			count+= board[y][x-1];
			count+= board[y][x+1];
			count+= board[y+1][x-1];
			count+= board[y+1][x];
			count+= board[y+1][x+1];
			
		}
		//bottom
		else if (y==row-1 && x!=0 && x!=column-1) {
			count+= board[0][x-1];
			count+= board[0][x];
			count+= board[0][x+1];
			
			count+= board[y-1][x-1];
			count+= board[y-1][x];
			count+= board[y-1][x+1];
			count+= board[y][x-1];
			count+= board[y][x+1];
		}
		
		//corners top left
		else if (x==0 && y==0) {
			count+= board[row-1][column-1];
			count+= board[row-1][0];
			count+= board[row-1][x+1];
			
			count+= board[y][column-1];
			count+= board[y][x+1];
			
			count+= board[y+1][column-1];
			count+= board[y+1][x];
			count+= board[y+1][x+1];	
		}
		
		//corner bottom right
		else if (x==column-1 && y==row-1) {
			count+= board[y-1][x-1];
			count+= board[y-1][x];
			count+= board[y-1][0];
			
			count+= board[y][x-1];
			count+= board[y][0];
			
			count+= board[0][x-1];
			count+= board[0][x];
			count+= board[0][0];
		}
		//corner top right
		else if(x==column-1 && y ==0) {
			count+= board[row-1][x-1];
			count+= board[row-1][x];
			count+= board[row-1][0];
			count+= board[0][0];
			count+= board[y+1][0];
			
			count+= board[y][x-1];
			count+= board[y+1][x-1];
			count+= board[y+1][x];
		}
		//corner bottom left
		else if(x==0 && y==row-1) {
			count+= board[y-1][column-1];
			count+= board[y][column-1];
			count+= board[0][column-1];
			count+= board[0][x];
			count+= board[0][x+1];
			
			count+= board[y-1][x];
			count+= board[y-1][x+1];
			count+= board[y][x+1];
		}
		//center part
		else {
			count+= board[y-1][x-1];
			count+= board[y-1][x];	
			count+= board[y-1][x+1];
			
			count+= board[y][x-1];
			count+= board[y][x+1];
			
			count+= board[y+1][x-1];	
			count+= board[y+1][x];
			count+= board[y+1][x+1];
		}
		
		return count;
	}
	
//	public int countLiveNeighbours(int x, int y) {
//
//		int count = 0;
//		count+= board[y-1][x-1];
//		count+= board[y-1][x];	
//		count+= board[y-1][x+1];
//		
//		count+= board[y][x-1];
//		count+= board[y][x+1];
//		
//		count+= board[y+1][x-1];	
//		count+= board[y+1][x];
//		count+= board[y+1][x+1];
//				
//		return count;
//	}
	
	public void tick() {
		int [][] newboard = new int [row][column];
		for (int y = 0; y < row; y++) {
			for (int x = 0; x < column; x ++) {
				int aliveNeighbours = countLiveNeighbours(x, y);
				if (board[y][x]==1) { 
					if (aliveNeighbours<2) {
						newboard [y][x]=0;
					}else if (aliveNeighbours==2 || aliveNeighbours==3) {
						newboard [y][x]=1;
					}else {
						newboard[y][x]=0;

					}
				}else if(aliveNeighbours==3) {
						newboard[y][x]=1;
				}
			}
		}	
		this.board = newboard;
//		this.countAlive(); //test
	}
	
	//for test only
		public void countAlive() {
			int countAlive=0;
			for(int i = 0; i < column; i++) {
				for(int j = 0; j < row; j++) {
					countAlive+= this.board[j][i];
					System.out.println("live cells: "+countAlive);
				}	
			}
		}
		
}
