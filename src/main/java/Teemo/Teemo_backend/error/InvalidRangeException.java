package Teemo.Teemo_backend.error;

public class InvalidRangeException extends RuntimeException{
    private String field;
    public InvalidRangeException(String field, String message){
        super(message);
        this.field = field;
    }
    public String getField() {
        return field;
    }
}
