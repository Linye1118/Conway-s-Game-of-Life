
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class Board extends Canvas {
	private Affine affine;
	public Simulation simulation;
	
	private boolean drawFlag = false;
	public int drawMode;
	
	public int width = 540;
	public int height = 540;
	
	private int column=100;
	private int row=100;
	
	private float scaleFactor = 100f;//good
//	private float scaleFactor = 540f;//good
		
	private final List<Color> colors = Arrays.asList(Color.BLUE, Color.ORANGERED, Color.DARKGOLDENROD, 
            Color.DARKCYAN, Color.PURPLE, Color.CHOCOLATE, Color.AQUA, Color.ORANGE,Color.RED, 
            Color.FUCHSIA, Color.HOTPINK, Color.TURQUOISE, Color.AQUAMARINE, Color.CRIMSON,
            Color.BLANCHEDALMOND, Color.THISTLE, Color.DARKORCHID, Color.GREEN);
	
	public Board() {
		super();

		this.setWidth(width);
		this.setHeight(height);
		
		this.affine = new Affine();		
		//only use height to keep it square
		this.affine.appendScale(height/scaleFactor,height/scaleFactor );	
		this.simulation = new Simulation(column, row);
		
		this.randomStart();
		
//		simulation.setAlive(2,1);
//		simulation.setAlive(2,2);
//		simulation.setAlive(2,3);
	} 	

	public void randomStart(){	
		for(int i = 0; i < 500; i++) {
			int randomX = (int) Math.abs(column/4 + Math.random()*column/2);
			int randomY = (int) Math.abs(row/4 + Math.random()*row/2);

			simulation.setAlive(randomX, randomY);	
		}
	}
	
	public void clearBoard() {
		for(int i = 0; i < column-1; i++) {
			for(int j = 0; j < row-1; j++) {
				simulation.setDead(i, j);
			}	
		}
	}

//	public void testRow(){
//		simulation.setAlive(0, 4);
//		simulation.setAlive(0, 5);
//		simulation.setAlive(0, 6);
//	}
//	
//	public void testGlider() {
//		simulation.setAlive(1, 2);
//		simulation.setAlive(2, 2);
//		simulation.setAlive(3, 2);
//		simulation.setAlive(2, 0);
//		simulation.setAlive(3, 1);
//	}
	
	public void drawWorld() {
		GraphicsContext g = this.getGraphicsContext2D();
		//draw background grid
		g.setTransform(this.affine);
//		g.setFill(Color.LIGHTGRAY);
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, column, row);
		
		//draw live squares
		//g.setFill(Color.ORANGERED);
		int randomColor = (int) Math.abs(Math.random()*10);
		g.setFill(colors.get(randomColor));
		for(int x = 0; x <= this.simulation.column; x++) {
			for(int y = 0; y < this.simulation.row; y++) {
				if(this.simulation.getState(x, y)==1) {
					g.fillRect(x,  y, 1, 1); 
				}
			}
		}
		//drawing the grid lines
		g.setStroke(Color.GRAY);
		g.setLineWidth(0.05f);

		for(int x = 0; x <= column+1; x++) {
			g.strokeLine(x, 0, x, column);
		}
		for(int y = 0; y <= row; y++) {
			g.strokeLine(0, y, column+1, y);
		}
	}
	
	public void switchDrawBtn(Button btnDraw) {
		this.drawFlag = (!this.drawFlag);
		if (drawFlag == true) {
			btnDraw.setText("Erase");
		}else {
			btnDraw.setText("Draw");
		}
	}	
	public void mouseDraw(Button btnDraw) {
		this.switchDrawBtn(btnDraw);	
		this.setOnMouseClicked(event -> {
			double mouseX = event.getX();
			double mouseY = event.getY();	
			try {
				Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
				//System.out.println(simCoord); //test
				int simX = (int)simCoord.getX();
				int simY = (int)simCoord.getY();
				//System.out.println("X: "+ simX+ "Y: "+ simY);
				
				if (drawFlag == true) {	
					switch (drawMode) {
						case 1:
							this.drawGlider(simX, simY);
							break;
						case 2:
							this.drawLWSS(simX, simY);
							break;
						case 3:
							this.drawF(simX, simY);
							break;
						default:
							this.simulation.setAlive(simX, simY);	
					}	
				}else {
					this.simulation.setDead(simX, simY);
				}
				this.drawWorld();
				
			} catch (NonInvertibleTransformException e) {
				System.out.println("Cound not invert transform"); //test
			}
		});	
	}
	
	public void drawGlider(int x, int y) {
		this.simulation.setAlive(x-1, y-1);
		this.simulation.setAlive(x, y-1);
		this.simulation.setAlive(x+1, y-1);
		
		this.simulation.setAlive(x-1, y);
		this.simulation.setDead(x, y);//if any thing alive in the centerpoint kill it!
		this.simulation.setDead(x+1, y);
		
		this.simulation.setDead(x-1, y+1);
		this.simulation.setAlive(x, y+1);
		this.simulation.setDead(x+1, y+1);
	}
	
	public void drawLWSS(int x, int y) {
		this.simulation.setDead(x-2, y-1);
		this.simulation.setAlive(x-1, y-1);
		this.simulation.setDead(x, y-1);
		this.simulation.setDead(x+1, y-1);
		this.simulation.setAlive(x+2, y-1);
		
		this.simulation.setAlive(x-2, y);
		this.simulation.setDead(x-1, y);
		this.simulation.setDead(x, y);
		this.simulation.setDead(x+1, y);
		this.simulation.setDead(x+2, y);
		
		this.simulation.setAlive(x-2, y+1);
		this.simulation.setDead(x-1, y+1);
		this.simulation.setDead(x, y+1);
		this.simulation.setDead(x+1, y+1);
		this.simulation.setAlive(x+2, y+1);
		
		this.simulation.setAlive(x-2, y+2);
		this.simulation.setAlive(x-1, y+2);
		this.simulation.setAlive(x, y+2);
		this.simulation.setAlive(x+1, y+2);
		this.simulation.setDead(x+2, y+2);
	}
	
	public void drawF(int x, int y) {
		this.simulation.setDead(x-1, y-1);
		this.simulation.setAlive(x, y-1);
		this.simulation.setAlive(x+1, y-1);
		
		this.simulation.setAlive(x-1, y);
		this.simulation.setAlive(x, y);
		this.simulation.setDead(x+1, y);
		
		this.simulation.setDead(x-1, y+1);
		this.simulation.setAlive(x, y+1);
		this.simulation.setDead(x+1, y+1);
	}

	public int getDrawMode() {
		return drawMode;
	}

	public void setDrawMode(int drawMode) {
		this.drawMode = drawMode;
	}

}
