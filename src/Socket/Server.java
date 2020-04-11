package Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final String QUIT = "quit";
        final int DEFAULT_PORT = 8888;
        ServerSocket serverSocket = null;

        try {
            // 绑定监听端口
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器，监听端口：" + DEFAULT_PORT);

            while (true) {
                // 等待客户端链接
                Socket socket = serverSocket.accept(); // accept是阻塞的
                System.out.println("客户端[" + socket.getPort() + "]已连接");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String msg = null;
                while ((msg = reader.readLine()) != null) { // 如果读到一半客户端宕掉了，取到的就是null
                    // 读取客户端发送的消息
                    System.out.println("客户端[" + socket.getPort() + "]：" + msg);

                    // 回复客户发送的消息
                    writer.write("服务器：" + msg + "\n");
                    writer.flush(); // 防止有消息遗留在缓冲区

                    // 查看客户端是否退出
                    if (QUIT.equals(msg)) {
                        System.out.println("客户端[" + socket.getPort() + "]已断开连接");
                        break; // 不写break也会在下一次的while里读到null而退出
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                    System.out.println("关闭serverSocket");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
