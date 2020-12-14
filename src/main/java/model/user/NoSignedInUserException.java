package model.user;

import model.UnauthorisedOperationException;

public class NoSignedInUserException extends Exception {
    public NoSignedInUserException() {
        super("No user is signed in, this functionality is accesisble to signed-in users only");
    }
}
