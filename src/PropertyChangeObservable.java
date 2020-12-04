import java.beans.PropertyChangeListener;

/**
 * This interface creates a contract for all classes which can have PropertyChangeListeners and
 * fire PropertyChangeEvents.
 *
 * If a class implements this interface, then it should have a PropertyChangeSupport component.
 *
 * @author Bori Fazakas
 */
public interface PropertyChangeObservable {

    /**
     * The registered listeners will be notified whenever a change occurs in the Observable's
     * properties.
     * @param listener is a newly registered listener.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Unregisters a listener.
     * @param listener will not be notified if a change occurs.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
}
