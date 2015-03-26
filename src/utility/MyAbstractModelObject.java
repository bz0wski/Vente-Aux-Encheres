package utility;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/** Class responsible for implementing JavaBeans support. All classes wishing to use Javabeans may extend this class for 
 * convenience, else may have to manually implement JavaBeans in their code.
 * <br> {@link #hashCode()} and {@link #equals(Object)} overridden.
 *   */
public abstract class MyAbstractModelObject {
	/**
	 * id : To generate a {@code hashcode} for the class.
	 */
	private int id = 1;
	

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName,
				listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}
	
	/*NB: Override equals() and hashCode() so that they may be overridden  correctly 
	 * in the subclasses.
	 * */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MyAbstractModelObject)) {
			return false;
		}
		MyAbstractModelObject other = (MyAbstractModelObject) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
}
