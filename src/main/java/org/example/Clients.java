package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Clients {
//    package org.example;
//
//import java.io .*;
//import java.net.Socket;
//import java.util.Scanner;

    //public class Client {
        private Socket socket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;
        private String userName;

        public Clients(Socket socket, String userName) {
            try {
                this.socket = socket;
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.userName = userName;
            } catch (IOException e) {
                //  throw new RuntimeException(e);
                closeEverything(socket, bufferedReader, bufferedWriter);
            }

        }

        public void sendMessage() {
            try {
                bufferedWriter.write(userName);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                Scanner scanner = new Scanner(System.in);
                while (socket.isConnected()) {
                    String messageTosend = scanner.nextLine();
                    bufferedWriter.write(userName + ": " + messageTosend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                }

            } catch (IOException e) {
                //throw new RuntimeException(e);
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }

        public void listenForMessage() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String messageFromGroupChat;
                    while (socket.isConnected()) {
                        try {
                            messageFromGroupChat = bufferedReader.readLine();
                            System.out.println(messageFromGroupChat);
                        } catch (IOException e) {
                            // throw new RuntimeException(e);
                            closeEverything(socket, bufferedReader, bufferedWriter);
                        }
                    }
                }
            }).start();
        }

        public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
            //removeClientHandler();
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        public static void main(String[] args) throws IOException {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your user name for the chat: ");
            String userName = scanner.nextLine();
            Socket socket = new Socket("192.168.43.216", 1234);
            Clients client = new Clients(socket, userName);
            client.listenForMessage();
            client.sendMessage();
        }

    }

