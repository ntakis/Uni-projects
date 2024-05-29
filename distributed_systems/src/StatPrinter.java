import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StatPrinter {

    // Receives user's total stat file (.txt) and prints contents in a presentable way.
    public void printUserTotalStats(File userTotalStats){

        printTotalStats("user_total_stats", userTotalStats);
    }

    public void printGlobalTotalStats(File globalTotalStats){

        printTotalStats("global_stats", globalTotalStats);
    }


    public void printRouteStats(File routeStats){

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(routeStats);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("userStats");

            System.out.println("STATS FOR EACH ROUTE:");
            for (int i =0; i < nodeList.getLength(); i++){
                Node n = nodeList.item(i);

                if (n.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) n;

                    double totalDistance = Double.parseDouble(element.getElementsByTagName("totalDistance").item(0).getTextContent());
                    double totalAscent = Double.parseDouble(element.getElementsByTagName("totalAscent").item(0).getTextContent());
                    double totalTime = Double.parseDouble(element.getElementsByTagName("totalTime").item(0).getTextContent());
                    double meanSpeed = Double.parseDouble(element.getElementsByTagName("meanSpeed").item(0).getTextContent());

                    // Use Locale.US for dot as decimal separator.
                    System.out.printf(Locale.US, "%d# ROUTE STATS: TOTAL DISTANCE = %.2f | TOTAL ASCENT= %.2f | TOTAL ACTIVITY TIME= %.2f | MEAN SPEED = %.2f",
                            i+1, totalDistance, totalAscent, totalTime, meanSpeed);
                    System.out.println();
                }
            }
            System.out.println();


        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void printTotalStats(String statsType, File totalStats){

        try {
            BufferedReader br = new BufferedReader(new FileReader(totalStats));
            String line = br.readLine();
            String[] parts = line.split(",");

            if (statsType.equals("user_total_stats")){
                System.out.println("USER'S TOTAL STATS:");
            } else{
                System.out.println("GLOBAL TOTAL STATS");
            }

            double meanActivityTime = Double.parseDouble(parts[1]);
            double meanDistance = Double.parseDouble(parts[2]);
            double meanAscent = Double.parseDouble(parts[3]);

            System.out.println("TOTAL ROUTES = " + parts[0]);
            System.out.printf(Locale.US, "MEAN ACTIVITY TIME = %.2f\n", meanActivityTime);
            System.out.printf(Locale.US, "MEAN DISTANCE = %.2f\n", meanDistance);
            System.out.printf(Locale.US, "MEAN ASCENT = %.2f\n", meanAscent);

            if (statsType.equals("global_stats")){
                System.out.println("LAST UPDATED = " + parts[4]);
            }
            System.out.println();

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}







