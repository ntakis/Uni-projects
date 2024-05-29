import java.io.*;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
public class StatLogger {

    /* Logs user's results from a gpx file that represents a route. (Total Activity Time, Total Distance, Total Ascent, Mean Speed).
    The file that holds the routes is called 'username_route_stats'.*/
    public File logRouteStats(String username, ActivityResults activityResults) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        // Check if \\user_route_stats directory exists, if not create one.
        File dir = new File(System.getProperty("user.dir") + "\\user_route_stats");
        if (!dir.exists()){
            dir.mkdirs();
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc;

        String filepath = System.getProperty("user.dir") + "/user_route_stats/" + username + "_route_stats";
        File xmlFile = new File(filepath);

        if (!xmlFile.exists()){
            // If the XML file does not exist yet, create a new one.
            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("userStatsList");
            doc.appendChild(rootElement);
        }else{
            // If the XML file already exists, parse it and use the existing root element.
            doc = docBuilder.parse(xmlFile);
        }

        /*Element routeNumberElement = doc.createElement("routeNumber"); MELLONTIKA, DEN XREIAZETAI GIA MEROS A'
        8A KANOUME COUNT TA TAGS GIA NA VRISKOUME POIO NOUMERO 8A ANA8ESOUME.*/

        Element userStatsElement = doc.createElement("userStats");
        Element totalDistanceElement = doc.createElement("totalDistance");
        Element totalAscentElement = doc.createElement("totalAscent");
        Element totalTimeElement = doc.createElement("totalTime");
        Element meanSpeedElement = doc.createElement("meanSpeed");

        totalDistanceElement.appendChild(doc.createTextNode(String.valueOf(activityResults.getTotalDistance())));
        totalAscentElement.appendChild(doc.createTextNode(String.valueOf(activityResults.getTotalAscent())));
        totalTimeElement.appendChild(doc.createTextNode(String.valueOf(activityResults.getTotalTime())));
        meanSpeedElement.appendChild(doc.createTextNode(String.valueOf(activityResults.getMeanSpeed())));

        userStatsElement.appendChild(totalDistanceElement);
        userStatsElement.appendChild(totalAscentElement);
        userStatsElement.appendChild(totalTimeElement);
        userStatsElement.appendChild(meanSpeedElement);

        Node rootElement = doc.getDocumentElement();
        rootElement.appendChild(userStatsElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Pretty printing. Not necessary.
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);

        return xmlFile;
    }


    private File updateTotalActivityStats(String statsType, String username, Queue<ActivityResults> activityResultsQueue) throws IOException {

        String filepath = "";
        if (statsType.equals("user_total_stats")){
            filepath = System.getProperty("user.dir") + "/user_total_stats/" + username + "_" + statsType;
        } else{
            filepath = System.getProperty("user.dir") + "\\" + statsType;
        }

        File activityStatsFile = new File(filepath);
        boolean firstEntry = false;

        // Create file if it doesn't exist. (first route log)
        if (!activityStatsFile.exists()){
            activityStatsFile.createNewFile();
            firstEntry = true;
        }

        int totalRoutes = 0;
        double meanActivityTime = 0;
        double meanDistance = 0;
        double meanAscent = 0;


        try {
            // If it is not first entry retrieve previous stats.
            if (!firstEntry){

                BufferedReader br = new BufferedReader(new FileReader(activityStatsFile));
                String line = br.readLine();
                if (line == null) return activityStatsFile;
                String[] parts = line.split(",");

                br.close();

                // Previous stats.
                totalRoutes = Integer.parseInt(parts[0]);
                meanActivityTime = Double.parseDouble(parts[1]);
                meanDistance = Double.parseDouble(parts[2]);
                meanAscent = Double.parseDouble(parts[3]);
            }


            // Update stats.
            double totalTime = 0;
            double totalDistance = 0;
            double totalAscent = 0;
            int routes = 0;
            while (!activityResultsQueue.isEmpty()){
                routes += 1;
                ActivityResults activityResults = activityResultsQueue.remove();
                totalTime += activityResults.getTotalTime();
                totalDistance += activityResults.getTotalDistance();
                totalAscent += activityResults.getTotalAscent();
            }

            totalRoutes += routes;
            meanActivityTime = (meanActivityTime * (totalRoutes-1) + totalTime)/ totalRoutes;
            meanDistance = (meanDistance * (totalRoutes-1) + totalDistance) / totalRoutes;
            meanAscent = (meanAscent * (totalRoutes-1) + totalAscent) / totalRoutes;


            // Update file.
            String updatedStats = totalRoutes + "," + meanActivityTime + "," + meanDistance + "," + meanAscent;
            if (statsType.equals("global_stats")){
                String timeUpdated = ZonedDateTime.now().toString();
                updatedStats += ("," + timeUpdated);
            }
            updatedStats += "\n";

            BufferedWriter bw = new BufferedWriter(new FileWriter(activityStatsFile));
            bw.write(updatedStats);

            bw.close();
        } catch (IOException ex) {
            System.out.println("Error writing file: " + ex.getMessage());
        }

        return activityStatsFile;
    }

    public File updateUserTotalStats(String username, ActivityResults activityResults) throws IOException {
        Queue<ActivityResults> activityResultsQueue = new LinkedList<>();
        activityResultsQueue.add(activityResults);

        // Check if \\user_total_stats directory exists, if not create one.
        File dir = new File(System.getProperty("user.dir") + "\\user_total_stats");
        if (!dir.exists()){
            dir.mkdirs();
        }

        return updateTotalActivityStats("user_total_stats", username, activityResultsQueue);
    }

    public File updateGlobalTotalStats(Queue<ActivityResults> activityResultsQueue) throws IOException{

        return updateTotalActivityStats("global_stats", "", activityResultsQueue);
    }
}












