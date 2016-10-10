package Controler;

import Gui.CameraGroup;
import Gui.HandlingInputScene;
import Model.Reservoir;
import Model.Sector;
import Model.WaterResource;
import View.Controler;
import View.EndPipe;
import View.Lake;
import View.Pipe;
import View.StateBox;
import View.WaterCylinder;
import View.WaterTowerPipe;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * main class responsible for creating a water network with a GUI
 * @author fcalvier
 *
 */

public class NetworkDisplay extends Application{
	
	
	public Scene getScene() {
		return root.getScene();
	}



	public CameraGroup getCameraGroup() {
		return cameraGroup;
	}


	//the group containing both the camera and the elements to show
	private Group root;
	//the camera handling group
    private CameraGroup cameraGroup;
 	//set of all elements to show
    public static Group world;
 	//viewing scale
	private double scale =0.1;
    
	@Override
	public void start(Stage primaryStage) {
		root = new Group();
		cameraGroup = new CameraGroup();
		//make objects opaque
		root.setDepthTest(DepthTest.ENABLE);
		HandlingInputScene scene = new HandlingInputScene(root);
        
		
        
        primaryStage.setTitle("Water Network Simulation");
        primaryStage.setScene(scene);
        root.getChildren().add(cameraGroup);
		scene.setCamera(cameraGroup);

		initPosition();

		scene.setFill(Color.GREY);
		
		//creation of the world
		world = new Group();
		world.setDisable(false);
		//first group of the world is a transformation for scaling
		world.getTransforms().add(new Scale(scale, scale, scale));
		
		//binding the world to the camera
		root.getChildren().add(world);
		
		WaterCylinder.initColor();
		StateBox.initColor();
		primaryStage.show();
		initWorld();
	}

	
	/**
	 * init camera position
	 */
	public void initPosition() {
		float Zcamera = -300;
		cameraGroup.reset();
		cameraGroup.setRotateX(-90.0);
		cameraGroup.getCamera().setTranslateZ(Zcamera);
		
	

		
	}
	
	/**
	 * A more complete version of the demo. Hard to test because of the graphical resources
	 * should be tested on a newer machine
	 */
	public void initWorlds(){

		
		
		Lake  lavalette = new Lake("lavalette");
		world.getChildren().add(lavalette);
		((WaterResource)lavalette.getUserData()).addToNetwork();
		//40 000 000 m3
		//1 100 000 m3 + 900 000 m3
		
		//Lake riot = new Lake("riot");
		//world.getChildren().add(riot);
		//((WaterRessource)riot.getUserData()).addToNetwork();
		
		/*conduite forcée 1300 mm sur 32000 m
		* 2,6 m3/s
		*/
		//((WaterRessource)lavalette.getUserData()).getStream().setOutgoingFlow(2.6f);
		//((WaterRessource)lavalette.getUserData()).updatedStream();
		
		Pipe p =  new Pipe("lignon");
		lavalette.add(p);
		//cheminee d'equilibre à 1/3 de la conduite forcee
		/*Controler cheminee = new Controler();*/
		Controler cheminee = new Controler("cheminee");
		p.add(cheminee);
		p =  new Pipe("lignon");
		cheminee.add(p);
				
				
				/*10 000 m  0,9 m3/s
				* suppose 1300 mm
				*/
		/*((WaterRessource)riot.getUserData()).getStream().outgoingFlow = 0.9f;
				Pipe furan =  new Pipe();
				riot.add(furan);
			*/	
				
				
				WaterTowerPipe solaure = new WaterTowerPipe("Solaure");
				p.add(solaure);
				// furan.add(solaure);
				WaterTowerPipe vionne = new WaterTowerPipe("vionne");
				p = new Pipe();
				solaure.add(p);
				p.add(vionne);
				
				WaterTowerPipe rejaillere = new WaterTowerPipe("rejaillere");
				p = new Pipe();
				vionne.add(p);
				
				p.add(rejaillere);
				
				
				WaterTowerPipe michon = new WaterTowerPipe("michon");
				p = new Pipe();
				vionne.add(p);
				p.add(michon);
				
				/*raccordement St Victor*/
				p = new Pipe();
				michon.add(p);
				for( int i = 0; i <9; i++ ){
					p.add(new EndPipe());
				}
				p = new Pipe();
				solaure.add(p);
				
				//100000 m3
				WaterTowerPipe plantes = new WaterTowerPipe("plantes");
				p.add(plantes);
				((Reservoir)plantes.getUserData()).setCapacity(100000);
				
				
				
				
				
				
				/*ArrayList<Reservoir> stock = new ArrayList<Reservoir>();
				for( int i = 0; i <15 ; i++ ){
					stock.add(new Reservoir());
				}
				Controler pepiniere = new Controler();
				Controler musee = new Controler();
				*/
				for( int i = 0; i <225; i++ ){
					plantes.add(new EndPipe());
				}
				for( int i = 0; i <166; i++ ){
					vionne.add(new EndPipe());
				}
				p = new Pipe();
				solaure.add(p);
				for( int i = 0; i <500; i++ ){
					p.add(new EndPipe());
				}		
				
				
				new SensorPool();
				new Consumer();
				
	}
	/**
	 * simplified version for demo 
	 */
	private void initWorld(){

	
		
		Lake  lavalette = new Lake("lavalette");
		world.getChildren().add(lavalette);
		((WaterResource)lavalette.getUserData()).addToNetwork();
		
		
		Pipe ps =  new Pipe("lignon");
		lavalette.add(ps);
		((Sector)ps.getUserData()).setDiameter(1300);
		Controler c = new Controler("cheminee");
		ps.add(c);
		((Model.Controler)c.getUserData()).setDiameter(1300);
		ps =  new Pipe("lignon");
		c.add(ps);
		((Sector)ps.getUserData()).setDiameter(1300);
		
		WaterTowerPipe wt = new WaterTowerPipe("Solaure");
		ps.add(wt);
		((Reservoir)wt.getUserData()).setCapacity(100000);
		
		ps = new Pipe();
		wt.add(ps);
		for( int i = 0; i <20; i++ ){
			ps.add(new EndPipe());
		}		
		new Consumer();
		new SensorPool();
	}
	
	
	
	 /**
     * The main() method is ignored in correctly deployed JavaFX 
     * application. main() serves only as fallback in case the 
     * application can not be launched through deployment artifacts, 
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
  
}
