package io.github.trashemail.exceptions;

public class EmailNotFoundExecption extends Exception {
    public EmailNotFoundExecption(){
        super("Email not allocated to anyone.");
    }
}
