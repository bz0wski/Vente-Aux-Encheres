/**
 * 
 */
package serveurs;

import java.util.Arrays;

import Enchere.Utilisateur;

/**
 * This class contains arbitrary functions that'll be used project wide 
 * @author Salim AHMED, Lila Medjber, Riantsoa Razafindramaka
 *
 */
public class UtilityClass {

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	private static final int minCapacity = 0;
	
	public static Utilisateur[] expandAndInsertUser(Utilisateur[] inUsers, Utilisateur newUser) {
		
		//Grow the array and append the new user to the end
		int oldCapacity = inUsers.length;
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		if (newCapacity - minCapacity < 0)
			newCapacity = minCapacity;
		if (newCapacity - MAX_ARRAY_SIZE > 0)
			newCapacity = hugeCapacity(minCapacity);
		// minCapacity is usually close to size, so this is a win:
		inUsers = Arrays.copyOf(inUsers, newCapacity);
		//System.arraycopy(inUsers, srcPos, dest, destPos, length);
		//Array successfully grown
		return null;
	}


	   private static int hugeCapacity(int minCapacity) {
	        if (minCapacity < 0) // overflow
	            throw new OutOfMemoryError();
	        return (minCapacity > MAX_ARRAY_SIZE) ?
	            Integer.MAX_VALUE :
	            MAX_ARRAY_SIZE;
	    }
	/*
	 * 
	 *  public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);  // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }
    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }


	 *   public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }
    /*
    // * Private remove method that skips bounds checking and does not
    // * return the value removed.
    // *
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }
	 */
}
