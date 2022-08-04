package form_exception;

public class UsernameAlreadyExists extends Exception{
    public UsernameAlreadyExists(String errorMessage){
        super(errorMessage);
    }
}
