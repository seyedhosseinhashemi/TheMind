package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static List<ClientHandler> clientHandlers;
    static int playersCount;
    public Server() {
        playersCount = 1;
        clientHandlers = new ArrayList<>();
        init();
    }
    public void init() {
        System.out.println("Server is running...");
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                addNewClientHandler(socket);
                System.out.println("====> There are " + clientHandlers.size() + " clients on the server!");

            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addNewClientHandler(Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(clientHandlers.size(), socket);
        if (!GameLogic.start && clientHandlers.size() < playersCount) { //todo
            System.out.println("New connection accepted!");
            clientHandlers.add(clientHandler);
            new Thread(clientHandler).start();
        }else if (GameLogic.start){
            startGame();
        }
        else {
            //todo replace client with bot
            clientHandler.sendMessage("game has been started!");
        }

    }
    public void startGame(){
        new GameLogic();
        for (ClientHandler c :
                clientHandlers) {
            c.sendInfo();
        }
    }


}