package hu.elte.musicbox;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class MusicBox {

    public static final int PORT = 40000;

    public static void main(String[] args) throws Exception {
        Set<ClientData> otherClients = new HashSet<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                ClientData client = new ClientData(serverSocket);
                synchronized (otherClients) {
                    otherClients.add(client);
                    System.out.println("Clinet has accepted");
                }

                new Thread(() -> {
                    while (client.getScanner().hasNextLine()) {
                        String line = client.getScanner().nextLine();

                        synchronized (otherClients) {
                            for (ClientData other : otherClients) {
                                other.getPrintWriter().println(line);
                                other.getPrintWriter().flush();
                            }
                        }
                    }

                    synchronized (otherClients) {
                        otherClients.remove(client);
                        try {
                            client.close();
                        } catch (Exception e) {
                            // won't happen
                        }
                    }
                }).start();
            }
        }

    }

}
