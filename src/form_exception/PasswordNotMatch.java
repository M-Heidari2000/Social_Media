package form_exception;

public class PasswordNotMatch extends Exception{
    public PasswordNotMatch(String errorMessage){
        super(errorMessage);
    }
}
