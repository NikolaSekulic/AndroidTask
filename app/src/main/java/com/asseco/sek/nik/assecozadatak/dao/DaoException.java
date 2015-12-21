package com.asseco.sek.nik.assecozadatak.dao;

/**
 * Exception that is thrown when error occurs in DAO interface.
 * <p/>
 * Created by sekul on 21.12.2015..
 */
public class DaoException extends Exception {

    /**
     * Constructor
     */
    public DaoException() {
    }

    /**
     * Creates new exception.
     *
     * @param detailMessage error description.
     */
    public DaoException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Creates new exception.
     *
     * @param detailMessage error  descriptoion
     * @param throwable     cause
     */
    public DaoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Creates new excpetion
     *
     * @param throwable cause
     */
    public DaoException(Throwable throwable) {
        super(throwable);
    }
}
