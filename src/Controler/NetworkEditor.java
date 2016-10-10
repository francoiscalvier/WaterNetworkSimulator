package Controler;


import Model.Controler;
import Model.Network;
import Model.PrivateSector;
import Model.PublicSector;
import Model.Reservoir;
import Model.WaterResource;
/**
 * main class responsible for creating a water network without GUI
 * @author fcalvier
 *
 */
public class NetworkEditor {

	
	public static void main(String[] args) throws InterruptedException {
		//40 000 000 m3
		WaterResource lavalette = new WaterResource();
		Network.add(lavalette);
		//1 100 000 m3 + 900 000 m3
		WaterResource riot = new WaterResource();
		Network.add(riot);
		/*ArrayList<PrivateSector> clients = new ArrayList<PrivateSector>();
		for( int i = 0; i <90000; i++ ){
			clients.add(new PrivateSector());
		}
		*/
		//6258m section inconnue
		//103439m entre 0 et 80 mm
		//210339m entre 80 et 150 mm
		//180911m entre 150 et 250 mm
		//122077m entre 250 et 500 mm
		//82316m entre 500 et 1300 mm
		// soit 705340 m de reseau lineaire
		
		
		/*conduite forcée 1300 mm sur 32000 m
		* 2,6 m3/s
		*/
		//lavalette.getStream().setOutgoingFlow(2.6f);
		PublicSector lignon =  new PublicSector();
		lignon.addToNetwork(lavalette);
		//cheminee d'equilibre à 1/3 de la conduite forcee
		Controler cheminee = new Controler();
		cheminee.addToNetwork(lignon);
		lignon =  new PublicSector();
		lignon.addToNetwork(cheminee);
		
		
		/*10 000 m  0,9 m3/s
		* suppose 1300 mm
		*/
		//riot.getStream().setOutgoingFlow(0.9f);
		PublicSector furan =  new PublicSector();
		furan.addToNetwork(riot);
		
		/*
		for( int i = 0; i <580000; i++ ){
			network.add(new PublicSector());
		}
		*/
		
		
		/*20 Reservoirs
		 * 400000m3
		 */
		
		Reservoir solaure = new Reservoir();
		solaure.addToNetwork(lignon);
		solaure.addToNetwork(furan);
		Reservoir vionne = new Reservoir();
		PublicSector ps = new PublicSector();
		ps.addToNetwork(solaure);
		vionne.addToNetwork(ps);
		
		Reservoir rejaillere = new Reservoir();
		ps = new PublicSector();
		ps.addToNetwork(vionne);
		
		rejaillere.addToNetwork(ps);
		
		
		Reservoir michon = new Reservoir();
		ps = new PublicSector();
		ps.addToNetwork(vionne);
		michon.addToNetwork(ps);
		
		/*raccordement St Victor*/
		ps = new PublicSector();
		ps.addToNetwork(michon);
		for( int i = 0; i <9; i++ ){
			new PrivateSector().addToNetwork(ps);;
		}

		
		//100000 m3
		Reservoir plantes = new Reservoir();
		ps = new PublicSector();
		ps.addToNetwork(solaure);
		
		plantes.addToNetwork(ps);
		
		
		
		/*ArrayList<Reservoir> stock = new ArrayList<Reservoir>();
		for( int i = 0; i <15 ; i++ ){
			stock.add(new Reservoir());
		}
		Controler pepiniere = new Controler();
		Controler musee = new Controler();
		*/
		
		for( int i = 0; i <225; i++ ){
			new PrivateSector().addToNetwork(plantes);;
		}
		for( int i = 0; i <166; i++ ){
			new PrivateSector().addToNetwork(vionne);;
		}
		for( int i = 0; i <500; i++ ){
			new PrivateSector().addToNetwork(solaure);;
		}		
		new Consumer();
		new SensorPool();
	}
	
}
