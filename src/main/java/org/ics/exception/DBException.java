package org.ics.exception;

public class DBException extends RuntimeException
{
    public DBException()
    {
        super();
    }

    public DBException(String message)
    {
        super(message);
    }
}
