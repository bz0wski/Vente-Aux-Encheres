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

	//List<Utilisateur> userList = new ArrayList<>();
	//List<Produit> produitList = new ArrayList<>();

	@Override
	public void mettreAuxEnchere() {
		// TODO Auto-generated method stub

	}


	@Override
	public void statistiques() {
		// TODO Auto-generated method stub

	}

	@Override
	public void archiverVenteAchevee(Vente venteAcheve) {
		ventesAcheve.add(venteAcheve);
	}

	public boolean archiverProduits(Produit[] listeproduits){
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),"Produits.ser" );
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(ventesAcheve);
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}

	public boolean archiverUtilisateurs( Utilisateur[] listeUtilisateurs){
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),"Utilisateurs.ser" );
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(ventesAcheve);
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
				if (ventesAcheve.isEmpty()) {
					throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
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
				if (ventesAcheve.isEmpty()) {
					throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return userList.toArray(new Utilisateur[0]);
			}
		}
		return userList.toArray(new Utilisateur[0]);
	}

	//Load serailized data to run application
	@SuppressWarnings("unchecked")
	private boolean recupererVentesNonTerminees() {
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			System.err.println("BD introuvable");
		}
		File file = new File(db.getAbsolutePath(),"VentesEnCours.ser" );

		if (file.exists() && file.canRead()) {
			try(FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
				ventesAcheve = (List<Vente>)inputStream.readObject();
				if (ventesAcheve.isEmpty()) {
					throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	//Write data to disk
	private boolean archivageVentesNonTerminees() {
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),"VentesEnCours.ser" );
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			outputStream.writeObject(ventesAcheve);
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}
	//Load serailized data to run application
	@SuppressWarnings("unchecked")
	private boolean recupererVentesTerminees() {
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
				return false;
			}
		}
		return true;

	}

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
		return null;

	}

}
