import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年10月30日 上午15:56
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class HttpDemo implements Runnable {

    private final static int PORT = 28081;
    private ServerSocket server = null;

    public static void main(String[] args) {
        new HttpDemo();
    }

    public HttpDemo() {
        try {
            server = new ServerSocket(PORT);
            if (server == null)
                System.exit(1);
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket client = null;
                client = server.accept();
                if (client != null) {
                    try {
                        System.out.println("连接服务器成功！！...");

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));

                        // GET /test.jpg /HTTP1.1
                        String line = reader.readLine();

                        System.out.println("line: " + line);

                        String resource = line.substring(line.indexOf('/'),
                                line.lastIndexOf('/') - 5);

                        System.out.println("the resource you request is: "
                                + resource);

                        resource = URLDecoder.decode(resource, "UTF-8");

                        String method = new StringTokenizer(line).nextElement()
                                .toString();

                        System.out.println("the request method you send is: "
                                + method);

                        long contentLength = 0;
                        while ((line = reader.readLine()) != null) {
                            if (line.equals("")) {
                                break;
                            }
                            System.out.println("the Http Header is : " + line);
                            if (line.contains("Content-Length")) {
                                contentLength = Long.valueOf(line.split(":")[1].trim());
                            }
                        }

                        if ("post".equals(method.toLowerCase())) {
                            System.out.println("the post request body is: ");
                            char[] data = new char[1024 * 1024];
                            int length = -1;
                            int cLength = 0;
                            while (cLength < contentLength && (length = reader.read(data, 0, data.length)) != -1) {
                                String str = String.valueOf(data, 0, length);
                                System.out.println(str);
                                cLength += str.length();
                            }
                        }

                        if (resource.endsWith(".md")) {

                            transferFileHandle("./D-ServletRequest.md", client);
                            closeSocket(client);
                            continue;

                        } else {
                            PrintStream writer = new PrintStream(
                                    client.getOutputStream(), true);
                            writer.println("HTTP/1.0 404 Not found");// 返回应答消息,并结束应答
                            writer.println();// 根据 HTTP 协议, 空行将结束头信息
                            writer.close();
                            closeSocket(client);
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("HTTP服务器错误:"
                                + e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(socket + "离开了HTTP服务器");
    }

    private void transferFileHandle(String path, Socket client) {

        File fileToSend = new File(path);

        if (fileToSend.exists() && !fileToSend.isDirectory()) {
            try {
                PrintStream writer = new PrintStream(client.getOutputStream());
                writer.println("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
                writer.println("Content-Type:application/binary");
                writer.println("Content-Length:" + fileToSend.length());// 返回内容字节数
                writer.println();// 根据 HTTP 协议, 空行将结束头信息

                FileInputStream fis = new FileInputStream(fileToSend);
                byte[] buf = new byte[fis.available()];
                fis.read(buf);
                writer.write(buf);
                writer.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}