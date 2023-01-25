import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    static final int SERVER_PORT = 8989;

    public static void main(String[] args) throws IOException {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Starting server at " + SERVER_PORT + "...");

            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
                    // Принимаем запрос
                    String request = in.readLine();

                    //Запрашиваем результат и отправляем его клиенту
                    String response = engine.search(request).toString();
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}