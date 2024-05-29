import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.Socket;
import java.util.Queue;

public class UserConnection extends Thread{
    public final int  CONNECTION_ID;
    private final Socket connectionSocket;
    public final Queue<UserPacket> sharedMessageQueue;
    public final ActivityResults activityResults;
    private final ReadWriteLock lock;

    public UserConnection(Integer connection_id, Socket connectionSocket, Queue<UserPacket> sharedMessageQueue,
                          ActivityResults activityResults, ReadWriteLock lock){
        this.CONNECTION_ID = connection_id;
        this.connectionSocket = connectionSocket;
        this.sharedMessageQueue = sharedMessageQueue;
        this.activityResults = activityResults;
        this.lock = lock;
    }

    @Override
    public void run(){
        DataInputStream dis = null;
        DataOutputStream dos = null;


        try {
            dis = new DataInputStream(connectionSocket.getInputStream());
            dos = new DataOutputStream(connectionSocket.getOutputStream());

            // Receive gpx file from user.
            File gpxFile = receiveFile(dis);

            // Extract username from gpx file.
            String username = extractUsername(gpxFile);

            /* Create user packet that contains connection_id (in order to be able to send back the results to the
            corresponding user) along with the gpx file to send to task assigner. */
            UserPacket userPacket = new UserPacket(this.CONNECTION_ID, gpxFile);



            // Send user packet to task assigner.
            synchronized (sharedMessageQueue){
                sharedMessageQueue.add(userPacket);
                sharedMessageQueue.notifyAll(); // Notify Task Assigner that a new message has been forwarded.
            }


            /* Wait for activity results (from the corresponding "reduce thread").
            Use variable activityResults for guarded suspension */

            synchronized (activityResults){
                // Use while loop check instead of 'if' statement to prevent spurious wake up of thread.
                while (activityResults.isEmpty()){
                    try{
                        activityResults.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }


            // Once activity results have been received, forward statistic files to the user by using the socket's output stream.
            StatLogger statLogger = new StatLogger();

            /* Log user's route stats in xml file and send xml file containing all his route stats along with the new
             activity results. */
            sendFile(statLogger.logRouteStats(username, activityResults), dos);

            // Update user's total stats file and send him file containing the data.
            sendFile(statLogger.updateUserTotalStats(username, activityResults), dos);

            /* Send users_stats file which contains mean activity time, mean distance, mean ascent for all users.
            file also contains time last updated.
             */
            lock.lockRead();
            sendFile(new File(System.getProperty("user.dir") + "\\" + "global_stats"), dos);
            lock.unlockRead();


        } catch (IOException | ParserConfigurationException | TransformerException |
                 SAXException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
                dos.close();
                connectionSocket.close(); // Terminate user connection.
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

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


    private void sendFile(File fileToSend, DataOutputStream dos) throws IOException{
        FileInputStream input = new FileInputStream(fileToSend);
        dos.writeLong(fileToSend.length()); // send size of file.
        dos.writeUTF(fileToSend.getName()); // send file name.

        int read = 0;
        while ((read = input.read()) != -1){
            dos.writeByte(read);
        }
        dos.flush();
        input.close();
    }


    private File receiveFile(DataInputStream dis) throws IOException{
        long fileSize = dis.readLong(); // read file size.
        String fileName = dis.readUTF(); // read file name.

        String newFileName = "temp_" + fileName;
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
}












