import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;

public class DummyUser {
    private String username;

    public static void main(String[] args){
        DummyUser du = new DummyUser();

        du.sendGPX(Integer.parseInt(args[0]), args[1]);
    }

    // REMOVE METHOD PARAMETER
    public void sendGPX(int fileNumber, String address){

        Socket requestSocket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            requestSocket = new Socket(address, 6969);
            dos = new DataOutputStream(requestSocket.getOutputStream());
            dis = new DataInputStream(requestSocket.getInputStream());

            File gpxFile = new File(System.getProperty("user.dir") + "/route" + fileNumber + ".gpx");
            username = extractUsername(gpxFile);
            sendFile(gpxFile, dos); // send gpx file.


            // Receive stats files from server.

            /* First receive file that contains stats/results for each past route + stats/results for new route.
            (Total Distance, Total Ascent, Total Activity Time, Mean Speed). */
            File routeStats = receiveFile(username, dis);

            /* Then, receive file that contains user's total stats from all past routes + new.
            (Total Routes, Mean Activity Time, Mean Distance, Mean Ascent). */
            File userTotalStats = receiveFile(username, dis);

            /* Lastly, receive global total activity stats file.
            (Total Routes, Mean Activity Time, Mean Distance, Mean Ascent, Time Last Updated)*/
            File globalTotalStats = receiveFile(username, dis);

            
            System.out.println("User #" + username + " stats.");
            StatPrinter statPrinter = new StatPrinter();

            // Print contents of temporary files.
            statPrinter.printRouteStats(routeStats);
            statPrinter.printUserTotalStats(userTotalStats);
            statPrinter.printGlobalTotalStats(globalTotalStats);

            // Delete temporary files from user's directory.
            routeStats.delete();
            userTotalStats.delete();
            globalTotalStats.delete();

            System.out.println("User #" + username + " stats complete.");


        }catch (UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }catch (IOException ioException){
            ioException.printStackTrace();
        } finally{
            try{ // release resources.
                dis.close();
                dos.close();
                requestSocket.close();
            } catch (IOException ioException){
                ioException.printStackTrace();
            }
        }
    }


    private File receiveFile(String username, DataInputStream dis) throws IOException{
        long fileSize = dis.readLong(); // read file size.
        String fileName = dis.readUTF(); // read file name.

        /* Adding the username infront is a measure if multiple users are operating on the same system.
        In real application it's not necessary since each user can only have one instance of the app installed.
        But if we want multiple DummyUser threads running on the same system it is essential otherwise threads may
        overwrite/delete the global_stats file causing exceptions.
         */
        String newFileName = username + "_temp_" + fileName;
        String filePath = System.getProperty("user.dir") + "\\" + newFileName;

        File tempFile = new File(filePath);
        FileOutputStream output = new FileOutputStream(tempFile);

        int read = 0;
        while (read < fileSize){
            output.write(dis.readByte());
            read++;
        }

        output.close();
        return tempFile;
    }

    private void sendFile(File fileToSend, DataOutputStream dos) throws IOException {
        FileInputStream input = new FileInputStream(fileToSend);
        dos.writeLong(fileToSend.length()); // send file size.
        dos.writeUTF(fileToSend.getName()); // send file name.

        int read = 0;
        while ((read = input.read()) != -1){
            dos.writeByte(read);
        }
        dos.flush();
        input.close();

    }

    private String extractUsername(File gpxFile){

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(gpxFile);

            Element rootElement = doc.getDocumentElement();

            return rootElement.getAttribute("creator"); // Return username.

        } catch (ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }

        return "";
    }
}


