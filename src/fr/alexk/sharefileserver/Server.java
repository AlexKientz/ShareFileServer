package fr.alexk.sharefileserver;

import fr.alexk.sharefileserver.Connection.MySQL;
import fr.alexk.sharefileserver.utils.GetOs;
import fr.alexk.sharefileserver.utils.User;
import sun.net.ftp.impl.FtpClient;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public Map<String, User> userMap = new HashMap<>();
    public Map<String, String> UserConnected = new HashMap<>();
    //           IP      USER
    private DatagramSocket datagramSocket;
    private DatagramSocket datagramSocketFile;


    private byte[] buffer = new byte[2048];
    private byte[] bufferFile = new byte[2048];

    public Server(DatagramSocket datagramSocket, DatagramSocket datagramSocketFile) {
        this.datagramSocket = datagramSocket;
        this.datagramSocketFile = datagramSocketFile;
    }

    String ip;
    public void receiveThenSend(){

        while (true){
            try {

                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                String user = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                MySQL sql = new MySQL();
                sql.mysqlSetup();
                sql.getData(user);
                ip = datagramPacket.getAddress().toString();
                UserConnected.put(ip, sql.userTemporaly.get(user).getPasswd());
                System.out.println(ip);
                buffer = sql.userTemporaly.get(user).getPasswd().getBytes();
                datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        //if(GetOs.isWindows() || GetOs.isMac() || GetOs.isSolaris()){
            System.out.println("[Erreur] Cette OS n'est pas compatible avec la version : Server");
            System.out.println("Une version arrivera peut-être sur votre plateforme.");
            //System.exit(0);
        //}
        System.out.println("[ShareFile] Server Started Version : 0.1");
        MySQL sql = new MySQL();
        sql.mysqlSetup();
        DatagramSocket datagramSocket = new DatagramSocket(1234);
        DatagramSocket datagramSocket1 = new DatagramSocket(12345);
        Server server = new Server(datagramSocket, datagramSocket1);
        server.receiveThenSend();

    }
}
