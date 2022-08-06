package form_exception;

public class PostNotFoundException extends Exception{
    public PostNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
