package controller;

import model.PropertyChangeObservable;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface CloseablePropertyChangeListener extends PropertyChangeListener {

    default void close() {
        List<PropertyChangeObservable> observables = getPropertyChangeObservables();
        for (PropertyChangeObservable observable : observables) {
            observable.removePropertyChangeListener(this);
        }
    }

    default void setObservables() {
        List<PropertyChangeObservable> observables = getPropertyChangeObservables();
        for (PropertyChangeObservable observable : observables) {
            observable.addPropertyChangeListener(this);
        }
    }

    List<PropertyChangeObservable> getPropertyChangeObservables();
}
