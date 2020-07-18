package io.github.trashemail.exceptions;

public class EmailAlreadyExsitExecption extends Exception {
    public EmailAlreadyExsitExecption(){
        super("Email Address already taken");
    }
}
