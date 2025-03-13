package tp5.ps;


public class DataOutOfRangeException extends Exception {
    
   
    public DataOutOfRangeException() {
        super("La valeur est hors de l'intervalle des abscisses des points de support");
    }
    
    public DataOutOfRangeException(String message) {
        super(message);
    }
}