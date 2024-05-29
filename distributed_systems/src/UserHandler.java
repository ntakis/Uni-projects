import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Queue;

public class UserHandler extends Thread{
    private final Queue<UserPacket> sharedMessageQueue;
    private final HashMap<Integer, ActivityResults> userConnections;
    private final ReadWriteLock lock;

    public UserHandler(Queue<UserPacket> sharedMessageQueue, HashMap<Integer, ActivityResults> userConnections,
    ReadWriteLock lock){
        this.sharedMessageQueue = sharedMessageQueue;
        this.userConnections = userConnections;
        this.lock = lock;
    }

    @Override
    public void run(){
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(6969);
            int connectionCount = 0; // Used to assign CONNECTION_ID to each "User connection thread"

            // Listen for connection requests from users.
            while (true){
                Socket connectionSocket = serverSocket.accept();
                connectionCount += 1;


                // Create shared object in order for results to be passed from "reduce thread" to user connection.
                ActivityResults activityResults = new ActivityResults();

                // Create new thread for new connection socket (user).
                UserConnection userConnection = new UserConnection(connectionCount, connectionSocket, sharedMessageQueue, activityResults, lock);

               // List connection in order for task assigner's "reduce threads" to be able to send results to users.
                this.userConnections.put(userConnection.CONNECTION_ID, activityResults); // NO need for synchronization since Task Assigner only reads from hashmap.
                userConnection.start();

            }


        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
}
