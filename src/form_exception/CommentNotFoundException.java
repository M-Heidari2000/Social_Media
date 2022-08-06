package form_exception;

public class CommentNotFoundException extends Exception{
    public CommentNotFoundException(String errorMessage){
        super(errorMessage);
    }    
}
