import com.hazelcast.core.*;
import com.hazelcast.config.*;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance clientInstance = HazelcastClient.newHazelcastClient(clientConfig);

        IMap<String, String> distributedMap = clientInstance.getMap("sharedMap");

        new Thread(() -> {
            while (true) {
                if (distributedMap.containsKey("serverMessage")) {
                    String serverMessage = distributedMap.get("serverMessage");
                    System.out.println("[Client] Mensaje recibido del servidor: " + serverMessage);
                    distributedMap.remove("serverMessage");
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[Client] Escribe un mensaje para el servidor: ");
            String clientMessage = scanner.nextLine();
            distributedMap.put("clientMessage", clientMessage);
        }
    }
}
