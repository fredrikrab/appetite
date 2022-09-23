package core;

public class Validate {

    /**
     * Checks if given <code>text</code> is vallid.
     * @param text String of characters
     * @return <code>true</code> if <code>text</code> valid, otherwise <code>false</code>
     */
    protected static Boolean isValidText(String text) {
        if (text == null) {
            return false;
        } else if (text.matches("^[a-zA-ZæøåÆØÅ -]*$")) {
            return true;
        } else {
            return false;
        }  
    }

    /**
     * returns <code>null</code> if <code>text</code>=<code>null</code>.
     * @param text String of characters
     * @return returns <code>text</code> with the first letter capitalized and the rest uncapitalized
     */

    protected static String setSpellingCapitalization(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return text.substring(0, 1).toUpperCase() 
                + text.substring(1).toLowerCase();
    }

}