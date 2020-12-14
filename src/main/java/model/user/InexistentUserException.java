package model.user;

public class InexistentUserException extends Exception{
    InexistentUserException(int id) {
        super("This operation is illegal because the requested user does not exist");
    }

    InexistentUserException(String userName) {
        super("This operation is illegal because the requested user with name " + userName +
                        " does not exist");
    }
}
