/**
 * 
 */
package utility;

import java.beans.PropertyChangeSupport;

/**
 * @author Salim AHMED
 *
 */
public class Proposition extends MyAbstractModelObject{
	private String proposition;
	
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Proposition(){
		proposition = "";
	}
	
	/**
	 * @return the proposition
	 */
	public String getProposition() {
		return proposition;
	}

	/**
	 * @param proposition the proposition to set
	 */
	public void setProposition(String proposition) {
		String oldValue = this.proposition;
		this.proposition = proposition;
		propertyChangeSupport.firePropertyChange("proposition", oldValue, proposition);
	}

	
}
