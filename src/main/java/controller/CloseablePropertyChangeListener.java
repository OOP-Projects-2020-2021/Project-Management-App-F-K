package controller;

import model.PropertyChangeObservable;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * CloseablePropertyChangeListener is an extension of PropertyChangeListener, intended to solve the
 * problem of memory leaks caused by the listeners: if a listener is registered for an observable,
 * as long as it is not unregistered, the garbage collector will not collect it due to the remaining
 * reference to it in the PropertyChangeObservable's listener list.
 *
 * <p>To solve this issue, whenever the CloseableComponent corresponding to a
 * CloseablePropertyChangeListener is closed, it closes the CloseablePropertyChangeListener, which
 * will thus be removed from the listener list of the observables, so that the garbage collector can
 * successfully collect it.
 *
 * @author Bori Fazakas
 */
public interface CloseablePropertyChangeListener extends PropertyChangeListener {

  /** When closed, the CloseablePropertyChangeListener will be unregistered from the observables. */
  default void close() {
    List<PropertyChangeObservable> observables = getPropertyChangeObservables();
    for (PropertyChangeObservable observable : observables) {
      observable.removePropertyChangeListener(this);
    }
  }

  /**
   * After constructing a CloseablePropertyChangeListener, it must be registered at the Observables
   * as a listener.
   */
  default void setObservables() {
    List<PropertyChangeObservable> observables = getPropertyChangeObservables();
    for (PropertyChangeObservable observable : observables) {
      observable.addPropertyChangeListener(this);
    }
  }

  List<PropertyChangeObservable> getPropertyChangeObservables();
}
