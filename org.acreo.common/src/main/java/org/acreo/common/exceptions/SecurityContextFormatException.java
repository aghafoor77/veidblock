package org.acreo.common.exceptions;

public class SecurityContextFormatException extends IllegalArgumentException {
    static final long serialVersionUID = -23243564364567234L;

    /**
     * Constructs a <code>SecurityContextFormatException</code> with no detail message.
     */
    public SecurityContextFormatException () {
        super();
    }

    /**
     * Constructs a <code>SecurityContextFormatException</code> with the
     * specified detail message to provide infor about exception.
     *
     * @param   s   the detail message.
     */
    public SecurityContextFormatException (String s) {
        super (s);
    }

    /**
     * Factory method for making a <code>SecurityContextFormatException</code>
     * given the specified input which caused the error.
     *
     * @param   s   the input causing the error
     */
    static SecurityContextFormatException forInputString(String s) {
        return new SecurityContextFormatException("For input string: \"" + s + "\"");
    }
}