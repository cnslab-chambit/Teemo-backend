package Teemo.Teemo_backend.error;

public class CustomInvalidValueException extends RuntimeException{
    private String field;
    public CustomInvalidValueException(String field, String message){
        super(message);
        this.field = field;
    }
    public String getField() {
        return field;
    }
}
