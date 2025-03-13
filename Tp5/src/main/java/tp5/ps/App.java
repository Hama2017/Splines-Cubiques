package tp5.ps;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class App {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("TP5 - Interpolation par splines cubiques");
        System.out.print("Entrez le chemin complet du fichier contenant les points: ");
        String nomFichier = scanner.nextLine();
        
        try {

        	List<double[]> listePoints = lireFichierPoints(nomFichier);
            
            if (listePoints.size() < 2) {
                System.err.println("Il faut au moins 2 points pour l'interpolation!");
                return;
            }
            
            int nbPoints = listePoints.size();
            double[] x = new double[nbPoints];
            double[] y = new double[nbPoints];
            
            for (int i = 0; i < nbPoints; i++) {
                x[i] = listePoints.get(i)[0];
                y[i] = listePoints.get(i)[1];
            }
            
            Spline spline = new Spline(x, y);
            
            double min = spline.getMinX();
            double max = spline.getMaxX();
            int nbPointsInterpol = 100;
            double pas = (max - min) / (nbPointsInterpol - 1);
            
            double[] xInterpol = new double[nbPointsInterpol];
            double[] yInterpol = new double[nbPointsInterpol];
            
            for (int i = 0; i < nbPointsInterpol; i++) {
                xInterpol[i] = min + i * pas;
                try {
                    yInterpol[i] = spline.evaluate(xInterpol[i]);
                } catch (DataOutOfRangeException e) {
                    System.err.println("Erreur: " + e.getMessage());
                }
            }
            
            afficherGraphiqueXChart(x, y, xInterpol, yInterpol);
            
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
 
    private static List<double[]> lireFichierPoints(String nomFichier) throws IOException {
        List<double[]> points = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue;  
                }
                
                String[] parties;
                if (ligne.contains(",")) {
                    parties = ligne.split(",");
                } else {
                    parties = ligne.split("\\s+");
                }
                
                if (parties.length >= 2) {
                    try {
                        double x = Double.parseDouble(parties[0].trim());
                        double y = Double.parseDouble(parties[1].trim());
                        points.add(new double[]{x, y});
                    } catch (NumberFormatException e) {
                        System.err.println("Format incorrect sur la ligne: " + ligne);
                    }
                }
            }
        }
        
        return points;
    }
    
  
    private static void afficherGraphiqueXChart(double[] x, double[] y, double[] xInterpol, double[] yInterpol) {

    	XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Interpolation par Splines Cubiques")
                .xAxisTitle("X")
                .yAxisTitle("Y")
                .build();
        
        chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        
        XYSeries pointsSeries = chart.addSeries("Points de support", x, y);
        pointsSeries.setMarker(SeriesMarkers.CIRCLE);
        pointsSeries.setMarkerColor(Color.RED);
        
        XYSeries curveSeries = chart.addSeries("Courbe d'interpolation", xInterpol, yInterpol);
        curveSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        curveSeries.setLineColor(Color.BLUE);
        curveSeries.setMarker(SeriesMarkers.NONE);
        
        new SwingWrapper<>(chart).displayChart();
    }
}