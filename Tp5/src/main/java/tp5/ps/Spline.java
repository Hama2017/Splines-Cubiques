/**
 * @author Hamadou BA
 * Dépôt GitLab : https://www-apps.univ-lehavre.fr/forge/bh243413/tp5-ps-splines-cubiques.git
 * 
 * Classe Spline pour interpolation par splines cubiques
 * pour des fonctions reelles d'une variable reelle.
 * Cette classe permet de construire une interpolation par splines cubiques
 * a partir d'un ensemble de points de support, puis d'evaluer
 * la fonction d'interpolation en n'importe quel point de l'intervalle
 * delimite par les abscisses des points de support.
 */

package tp5.ps;

public class Spline {
    
    private double[] x; 
    private double[] y;
    private double[] y2; 
    private int n; 

    public Spline(double[] x, double[] y) {
        if (x == null || y == null || x.length != y.length || x.length < 2) {
            throw new IllegalArgumentException("Les tableaux doivent etre de meme taille et avoir au moins 2 points");
        }
        
        this.n = x.length;
        this.x = new double[n];
        this.y = new double[n];
        this.y2 = new double[n];
        
        for (int i = 0; i < n; i++) {
            this.x[i] = x[i];
            this.y[i] = y[i];
        }
        
        calculeDeriveeSeconde();
    }
    
 
    private void calculeDeriveeSeconde() {
        
    	double[] u = new double[n - 1];
        
        y2[0] = 0.0;
        y2[n - 1] = 0.0;
        
        for (int i = 1; i < n - 1; i++) {
            double sig = (x[i] - x[i - 1]) / (x[i + 1] - x[i - 1]);
            double p = sig * y2[i - 1] + 2.0;
            y2[i] = (sig - 1.0) / p;
            
            double TermeDroite = 6.0 * ((y[i + 1] - y[i]) / (x[i + 1] - x[i]) - (y[i] - y[i - 1]) / (x[i] - x[i - 1])) 
                             / (x[i + 1] - x[i - 1]);
                             
            if (i == 1) {
                u[i - 1] = TermeDroite / p;
            } else {
                u[i - 1] = (TermeDroite - sig * u[i - 2]) / p;
            }
        }
        
        for (int i = n - 2; i >= 1; i--) {
            y2[i] = y2[i] * y2[i + 1] + u[i - 1];
        }
    }
    

    public double evaluate(double val) throws DataOutOfRangeException {

    	if (val < x[0] || val > x[n - 1]) {
            throw new DataOutOfRangeException(
                "La valeur " + val + " est en dehors de l'intervalle [" + x[0] + ", " + x[n - 1] + "]");
        }
        
        int klo = 0;
        int khi = n - 1;
        
        while (khi - klo > 1) {
            int k = (khi + klo) / 2;
            if (x[k] > val) {
                khi = k;
            } else {
                klo = k;
            }
        }
        
        double h = x[khi] - x[klo];
        if (h == 0.0) {
            throw new IllegalStateException("Il y a deux points avec la meme abscisse");
        }
        
        double a = (x[khi] - val) / h;
        double b = (val - x[klo]) / h;
        
        double resultat = a * y[klo] + b * y[khi] + 
                ((a * a * a - a) * y2[klo] + (b * b * b - b) * y2[khi]) * (h * h) / 6.0;
        
        return resultat;
    }
    
  
    public double getMinX() {
        return x[0];
    }
    
   
    public double getMaxX() {
        return x[n - 1];
    }
}