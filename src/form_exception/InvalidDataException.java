package form_exception;

public class InvalidDataException extends Exception{
    public InvalidDataException(String errorMessage){
        super(errorMessage);
    }
}
