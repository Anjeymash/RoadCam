package by.anjeymash.tasks.roadcam.dao.exception;
/**
 * @author Mashkouski Andrei
 * @version 1.0 
 */
public class ConnectionPoolException extends Exception {
private static final long serialVersionUID = 1L;
public ConnectionPoolException(String message, Exception e){
super(message, e);
}
}