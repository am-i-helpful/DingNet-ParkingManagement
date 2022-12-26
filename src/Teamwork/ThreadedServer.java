package Teamwork;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {
    int port;
    ServerSocket serverSocket;
    Socket clientSocket;
    boolean breakCondition;
    public ThreadedServer(){
        this.port = 5000;
    }

    public Socket createConnection() throws IOException {
        this.breakCondition = true;
        if(this.serverSocket == null)
            this.serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);
        if(this.clientSocket == null)
            this.clientSocket = this.serverSocket.accept();
        // respondToMessage();
        return this.clientSocket;
    }

    public boolean releaseConnectionResources(){
        try{
            if(this.clientSocket != null)
                this.clientSocket.close();
            if(this.serverSocket != null)
                this.serverSocket.close();
            this.clientSocket = null;
            this.serverSocket = null;
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false; // Can't do anything
        }
    }

    protected void respondToMessage(){
        try {
            ServerOutputSender sos = new ServerOutputSender(this.clientSocket);
            sos.start();
            ServerInputReader sir = new ServerInputReader(this.clientSocket);
            sir.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

class ServerOutputSender extends Thread{
    Socket clientSocket;
    public ServerOutputSender(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    public void run(){
        PrintWriter out;
        try {
            out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            int userInput = 0;
            while (true) {
                out.println(userInput);
                userInput+=5;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerInputReader extends Thread{
    Socket clientSocket;
    public ServerInputReader(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            String outputFromServer="";
            while((outputFromServer=in.readLine())!= null){
                System.out.println(outputFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
