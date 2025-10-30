package net.paulweider.exceptions;

public class PetAlreadyAdoptedException extends Throwable
{
    public PetAlreadyAdoptedException(String s)
    {
        super(s);
    }
}
