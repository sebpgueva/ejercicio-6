import com.hazelcast.core.*;
import com.hazelcast.config.*;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) {
        Config config = new Config();
        config.setInstanceName("hazelcast-server");

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        System.out.println("[Server] Servidor iniciado y esperando mensajes del cliente...");

        IMap<String, String> distributedMap = hazelcastInstance.getMap("sharedMap");

        new Thread(() -> {
            while (true) {
                if (distributedMap.containsKey("clientMessage")) {
                    String clientMessage = distributedMap.get("clientMessage");
                    System.out.println("[Server] Mensaje recibido del cliente: " + clientMessage);
                    distributedMap.remove("clientMessage");
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[Server] Escribe un mensaje para el cliente: ");
            String serverMessage = scanner.nextLine();
            distributedMap.put("serverMessage", serverMessage);
        }
    }
}
