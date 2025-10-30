package net.paulweider.exceptions;

public class PetNotFoundException extends Throwable
{
    public PetNotFoundException(String message)
    {
        super(message);
    }
}
