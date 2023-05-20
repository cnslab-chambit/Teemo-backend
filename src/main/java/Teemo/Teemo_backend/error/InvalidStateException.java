package Teemo.Teemo_backend.error;

public class InvalidStateException extends RuntimeException{
    private String field;
    public InvalidStateException(String field,String message){
        super(message);
        this.field = field;
    }
    public String getField() {
        return field;
    }
}
