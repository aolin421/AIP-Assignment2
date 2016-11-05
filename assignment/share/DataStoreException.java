/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uts.aip.ams.assignment.share;

/**
 *
 * @author Umair
 */
public class DataStoreException extends Exception {
    
    public DataStoreException(String message) {
        super(message);
    }
    
    public DataStoreException(Throwable cause) {
        super(cause);
    }
    
    public DataStoreException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
