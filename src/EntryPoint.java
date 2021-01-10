import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EntryPoint extends Application {

	Board mainView;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Conway's Game of Life");
		BorderPane border = new BorderPane();
		
		this.mainView = new Board();
		
		HBox hbox = addHBox(mainView);
		border.setTop(hbox);
		
		border.setCenter(mainView);	
		mainView.drawWorld();
		
		VBox vbox = addVBox();
		border.setRight(vbox);
			
		border.setBottom(addBottomPane());	
		
		Scene scene = new Scene(border,800,600);

		primaryStage.setScene(scene);
		primaryStage.show();

		// bound test		
//		Bounds boundsInScene = mainView.localToScene(mainView.getBoundsInLocal());
//		Bounds boundsInScreen = mainView.localToScreen(mainView.getBoundsInLocal());
//		System.out.println(mainView.getLayoutBounds());
//		System.out.println(boundsInScene);
//		System.out.println(boundsInScreen);
//		
//		System.out.println("---- ");
//		Bounds boundsScene = hbox.localToScene(hbox.getBoundsInLocal());
//		Bounds boundsScreen = hbox.localToScreen(hbox.getBoundsInLocal());
//		System.out.println(hbox.getLayoutBounds());
//		System.out.println(boundsScene);
//		System.out.println(boundsScreen);	
//		
//		System.out.println("---- ");
//		Bounds boundsV = vbox.localToScene(vbox.getBoundsInLocal());		
//		System.out.println(vbox.getLayoutBounds());
//		System.out.println(boundsV);
//
//		System.out.println("---- ");
//		Bounds boundsHB = vbox.localToScene(vbox.getBoundsInLocal());		
//		System.out.println(vbox.getLayoutBounds());
//		System.out.println(boundsHB);
	}
	
	public HBox addHBox(Board mainView) {
		
	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");
	    
	    Button btnStart = new Button("Start");
	    btnStart.setPrefSize(100, 20);
	    
	    AnimationTimer runAnimation = new AnimationTimer() {
	    	private long lastUpdate = 0;
	    	@Override
            public void handle(long now) {
	    		if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos(200)) {
	    			mainView.simulation.tick();
	    			mainView.drawWorld();
	    			lastUpdate = now;
	    		}
	    	}
	    };
	    
	    btnStart.setOnAction(actionEvent -> {
	    	runAnimation.start();
		});

	    Button btnStop = new Button("Stop");
	    btnStop.setPrefSize(100, 20);	    
	    btnStop.setOnAction(actionEvent -> {runAnimation.stop();});
	    	    
	    Button btnStep = new Button("Step");
	    btnStep.setPrefSize(100, 20);	    
	    btnStep.setOnAction(actionEvent -> {
	    	mainView.simulation.tick();
	    	mainView.drawWorld();
	    });
	    
	    Button btnDraw = new Button("Draw");
	    btnDraw.setPrefSize(100, 20);
	    btnDraw.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mainView.mouseDraw(btnDraw));
	    		    
	    Button btnClear = new Button("Clear");
	    btnClear.setPrefSize(100, 20);
	    btnClear.setOnAction(actionEvent ->{
	    	mainView.clearBoard();
	    	mainView.drawWorld();
	    });
	     
	    hbox.getChildren().addAll(btnStart, btnStop, btnStep, btnDraw, btnClear);
	    
	    return hbox;
	}
	
	public VBox addVBox(){
	    VBox vbox = new VBox();
	    vbox.setPadding(new Insets(10));
	    vbox.setSpacing(10);
	    vbox.setStyle("-fx-background-color: #336699;");
	       
	    vbox.getChildren().add(this.addGridPane()); //add radio buttons for patterns

	    return vbox;
	}

	public GridPane addGridPane () {
	    GridPane grid = new GridPane();
	    grid.setStyle("-fx-background-color: #999999;");
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 0, 10));
	    
	    Image glider = new Image(getClass().getResourceAsStream("glider.png"));
	    ImageView imgViewGlider = new ImageView(glider);
	    
	    Image lwss = new Image(getClass().getResourceAsStream("lwss.png"));
	    ImageView imgViewLwss = new ImageView(lwss);
	    
	    Image f = new Image(getClass().getResourceAsStream("f.png"));
	    ImageView imgViewF = new ImageView(f);
	    
	    final ToggleGroup groupPattern = new ToggleGroup();
	    RadioButton rbGlider = new RadioButton("Glider");
	    rbGlider.setGraphic(imgViewGlider);
	    RadioButton rbLwss = new RadioButton("Light Weight \nSpace Ship");
	    rbLwss.setGraphic(imgViewLwss);
	    RadioButton rbF = new RadioButton("F");
	    rbF.setGraphic(imgViewF);
	    
	    grid.add(rbGlider, 0, 1);
	    grid.add(rbF, 0, 2);
	    grid.add(rbLwss, 0, 3);
	    
	    rbGlider.setToggleGroup(groupPattern);
	    rbLwss.setToggleGroup(groupPattern);
	    rbF.setToggleGroup(groupPattern);
	    
	    rbGlider.setUserData(1);
	    rbLwss.setUserData(2);
	    rbF.setUserData(3);
	    
	    groupPattern.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	    	public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, 
		    	    Toggle new_toggle) {
	    		if (groupPattern.getSelectedToggle() != null) {
	    	        int drawMode=(int) groupPattern.getSelectedToggle().getUserData();
	    	        //System.out.println(drawMode);
	    	        mainView.setDrawMode(drawMode);
	    		}
	    	}
	    });
	    
	    return grid;
	}
	
	private HBox addBottomPane()
	{
		HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");
	    Text category = new Text("Conway's Game of Life");
	    category.setFill(Color.WHITE);
	    category.setFont(Font.font("Arial", FontWeight.BOLD, 25));
	    hbox.getChildren().add(category); 
	    
	    System.out.println("---- ");
		Bounds boundsHB = hbox.localToScene(hbox.getBoundsInLocal());		
		System.out.println("Bottom: "+hbox.getLayoutBounds());
		System.out.println(boundsHB);
		System.out.println();
		
	    return hbox;		
	}
	
	public static void main(String[] args) {
		launch();		
	}

}
