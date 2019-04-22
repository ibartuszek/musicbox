package hu.elte.musicbox;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hu.elte.musicbox.command.CommandFactory;

public class MusicBox {

    public static final int PORT = 40000;

    public static void main(final String[] args) throws Exception {
        final Set<ClientData> otherClients = new HashSet<>();
        final CommandFactory commandFactory = new CommandFactory();
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final ClientData client = new ClientData(serverSocket);
                synchronized (otherClients) {
                    otherClients.add(client);
                    System.out.println("Clinet has accepted");
                }

                new Thread(() -> {
                    List<String> clientInput = new ArrayList<>();
                    while (client.getScanner().hasNextLine()) {
                        String line = client.getScanner().nextLine();
                        clientInput.add(line);

                        synchronized (otherClients) {
                            for (ClientData other : otherClients) {
                                other.getPrintWriter().println(line);
                                other.getPrintWriter().flush();
                            }
                        }

                        if (commandFactory.isReady(clientInput)) {
                            commandFactory.createCommand(clientInput).execute();
                            // commandFactory.createCommand(clientInput).execute();
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
