package service;

public class MyException extends Exception{
    private final String message;

    public MyException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
