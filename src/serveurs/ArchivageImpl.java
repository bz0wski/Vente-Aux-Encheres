/**
 * 
 */
package serveurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Enchere.ArchivagePOA;
import Enchere.Produit;
import Enchere.Utilisateur;
import Enchere.Vente;

/**
 * @author Salim AHMED
 *
 */
public class ArchivageImpl extends ArchivagePOA {

	private static String OS = null;
	List<Vente> ventesAcheve = new ArrayList<Vente>();

	@Override
	public void mettreAuxEnchere() {
		System.out.println("Mettre au encheres");
	}


	@Override
	public void archiverVenteAchevee(Vente venteAcheve) {
		ventesAcheve.add(venteAcheve);
	}

	public boolean archiverProduits(Produit[] listeproduits){
		if (listeproduits.length == 0) {
			return false;
		}
		System.out.println("Writing produits");
		for (int i = 0; i < listeproduits.length; i++) {
			System.out.println(listeproduits[i].nom);
		}
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),"Produits.ser" );
		List<Produit> list = new ArrayList<Produit>(Arrays.asList(listeproduits));
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(list);
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}

	public boolean archiverUtilisateurs( Utilisateur[] listeUtilisateurs){
		if (listeUtilisateurs.length == 0) {
			return false;
		}
		System.out.println("Writing utilisateurs");
		for (int i = 0; i < listeUtilisateurs.length; i++) {
			System.out.println(listeUtilisateurs[i].nom);
		}
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),"Utilisateurs.ser" );
		List<Utilisateur> list = new ArrayList<Utilisateur>(Arrays.asList(listeUtilisateurs));
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(list);
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}


	@SuppressWarnings("unchecked")
	public Produit[] chargerProduits(){ 
		List<Produit> produitList = new ArrayList<>();
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			System.err.println("BD introuvable");
		}
		File file = new File(db.getAbsolutePath(),"Produits.ser" );

		if (file.exists() && file.canRead()) {
			try(FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
				produitList = (List<Produit>)inputStream.readObject();
				if (produitList.isEmpty()) {
					//throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return produitList.toArray(new Produit[0]);
			}
		}
		return produitList.toArray(new Produit[0]);
	}


	@SuppressWarnings("unchecked")
	public Utilisateur[] chargerUtilisateurs(){
		List<Utilisateur> userList = new ArrayList<>();
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			System.err.println("BD introuvable");
		}
		File file = new File(db.getAbsolutePath(),"Utilisateurs.ser" );

		if (file.exists() && file.canRead()) {
			
			try(FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
				userList = (List<Utilisateur>)inputStream.readObject();
				if (userList.isEmpty()) {
					//throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return userList.toArray(new Utilisateur[0]);
			}
		}
		return userList.toArray(new Utilisateur[0]);
	}

	//Load serailized data to run application


	//Write data to disk
	
	//Load serailized data to run application

/*
	//Write data to disk
	private boolean archivageVentesTerminees() {
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),"VentesTerminees.ser" );
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(ventesAcheve);
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}*/

	
	


	@Override
	public boolean archiverVenteEncours(Vente[] listeVentes) {
		if (listeVentes.length == 0) {
			return false;
		}
		System.out.println("Writing Ventes");
		for (int i = 0; i < listeVentes.length; i++) {
			System.out.println(listeVentes[i].idVente);
		}
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Vente> list = new ArrayList<Vente>(Arrays.asList(listeVentes));
		File file = new File(db.getAbsolutePath(),"VentesEnCours.ser" );
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(list);
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}


	@Override
	public Vente[] chargerVentesEncours() {
		List<Vente> ventes = new ArrayList<>();
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			System.err.println("BD introuvable");
		}
		File file = new File(db.getAbsolutePath(),"VentesEnCours.ser" );

		if (file.exists() && file.canRead()) {
			try(FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
				ventes = (List<Vente>)inputStream.readObject();
				if (ventes.isEmpty()) {
					//throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		return ventes.toArray(new Vente[0]);
	}


	@Override
	public boolean archiverVenteTerminees(Vente[] listeVentes) {
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),"VentesTerminees.ser" );
		List<Vente> list = new ArrayList<Vente>(Arrays.asList(listeVentes));

		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(list);
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}



	@Override
	public Vente[] chargerVenteTerminees() {
		
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			System.err.println("BD introuvable");
		}
		File file = new File(db.getAbsolutePath(),"VentesTerminees.ser" );

		if (file.exists() && file.canRead()) {
			try(FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
				ventesAcheve = (List<Vente>)inputStream.readObject();
				if (ventesAcheve.isEmpty()) {
					throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		return ventesAcheve.toArray(new Vente[0]);
	}

	
	public static String getOSName()
	{
		if(OS == null) { OS = System.getProperty("os.name"); }    
		return OS;
	}

	private static String getAppDataDirectory(){
		if (getOSName().startsWith("Windows")) {
			return System.getenv("APPDATA");
		}else if (getOSName().startsWith("Mac")) {
			return System.getProperty("user.home")+"/Library/Application Support";
		}
		return "";

	}
}
