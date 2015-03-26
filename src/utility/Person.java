/**
 * 
 */
package utility;

import java.beans.PropertyChangeSupport;

/**
 * @author Salim AHMED
 *
 */

public class Person extends MyAbstractModelObject{
	private String name;
	private String pswd;
	private String adresse;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Person() {
		name = "";
		pswd = "";
		adresse = "";
	}
		public String getName(){
			return name;
		}
		
		public void setName(String nom) {
			String oldName = this.name;
			this.name = nom;
			System.out.println("Set NAme");
			propertyChangeSupport.firePropertyChange("nom", oldName, name);
		}

		/**
		 * @return the mdp
		 */
		public String getPswd() {
			return this.pswd;
		}

		/**
		 * @param mdp the mdp to set
		 */
		public void setPswd(String mdp) {
			String oldMdp = this.pswd;
			this.pswd = mdp;
			System.out.println("Set pswd");
			propertyChangeSupport.firePropertyChange("mdp", oldMdp, mdp);
		}
		/**
		 * @return the adresse
		 */
		public String getAdresse() {
			return adresse;
		}
		/**
		 * @param adresse the adresse to set
		 */
		public void setAdresse(String adresse) {
			String oldValue = this.adresse;
			this.adresse = adresse;
			firePropertyChange("adresse", oldValue, adresse);
		}
		
	}