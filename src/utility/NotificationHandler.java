/**
 * 
 */
package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ui.MainClientWindow;

/**
 * @author Salim AHMED
 *
 */
public final class NotificationHandler {

	private static List<MainClientWindow> windows = new ArrayList<>();
	private static int i = 0;
	private static String OS = null;
	private static NotificationHandler notificationHandler = null;

	private NotificationHandler(){

	}
	public static void addElement(MainClientWindow window) {
		
		/*System.out.println("Added new window");
		long start = System.currentTimeMillis();
		setClientWindows(chargerListClients());
		List<MainClientWindow> list = getClientWindows();
		list.add(window);
		setClientWindows(list);
		archiverListClients();
		long end = System.currentTimeMillis();
		
		System.out.printf("Time taken :%d",end-start);
		
		for (MainClientWindow mainClientWindow : windows) {
			i++;
			System.out.println("Window number "+i);*/
	//	}
		//}
	}

	public static void removeElement(MainClientWindow window) {
	/*	setClientWindows(chargerListClients());
		if (windows.contains(window)) {
			System.out.println("removed  the window");
			windows.remove(window);
			archiverListClients();*/
		//}
	}

	public static synchronized List<MainClientWindow> getClientWindows() {
		return windows;
	}

	public synchronized static void setClientWindows(List<MainClientWindow> list) {
		windows = list;
	}
	
	public static NotificationHandler getInstance() {
		if (notificationHandler == null) {
			notificationHandler = new NotificationHandler();
			return notificationHandler;
		}
		return notificationHandler;
	}

	public static boolean archiverListClients(){
		if (windows.isEmpty()) {
			return false;
		}
		
		System.out.println("Writing utilisateurs");
		for (int i = 0; i < windows.size(); i++) {
			System.out.println(windows.get(i));
		}
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			try {
				db.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = new File(db.getAbsolutePath(),".NotificationHandler.ser" );
		//List<MainClientWindow> list = new ArrayList<MainClientWindow>();
		try(FileOutputStream fileout = new FileOutputStream(file);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileout)){
			synchronized (windows) {
				outputStream.writeObject(windows);
			}	
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
		return true;
	}


	@SuppressWarnings("unchecked")
	public static List<MainClientWindow> chargerListClients(){ 
		List<MainClientWindow> windows = new ArrayList<>();
		File db = new File(getAppDataDirectory(),"org.ups.ProjetCORBA"+File.separator+"db");
		if (!db.exists()) {
			System.err.println("BD introuvable");
		}
		File file = new File(db.getAbsolutePath(),".NotificationHandler.ser" );

		if (file.exists() && file.canRead()) {
			try(FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
			
					windows = (List<MainClientWindow>)inputStream.readObject();
				
				if (windows.isEmpty()) {
					System.out.println("EMPTY LIST FOUND!!");
					return new ArrayList<MainClientWindow>();
					//throw new IllegalArgumentException("Corrupted Database File, couldn't recover any useful data");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<MainClientWindow>();
			}
		}
		return windows;
	}

	private static String getOSName()
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
