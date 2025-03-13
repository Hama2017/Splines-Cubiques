/**
 * @author Hamadou BA
 * Dépôt GitLab : https://www-apps.univ-lehavre.fr/forge/bh243413/tp5-ps-splines-cubiques.git
 * 
 * Exception lancee quand la valeur est en dehors de l'intervalle 
 * des abscisses des points de support.
 * Cette classe etend Exception et permet de gerer proprement les cas
 * ou l'on tente d'evaluer la fonction d'interpolation en dehors
 * de l'intervalle delimite par les points de support.
 */

package tp5.ps;


public class DataOutOfRangeException extends Exception {
    
   
    public DataOutOfRangeException() {
        super("La valeur est hors de l'intervalle des abscisses des points de support");
    }
    
    public DataOutOfRangeException(String message) {
        super(message);
    }
}