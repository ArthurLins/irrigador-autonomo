package me.arthurlins.smartvase;


import me.arthurlins.smartvase.Comm.Communication;
import me.arthurlins.smartvase.Http.Server;

public class Main {



    static public void main(String[] args) throws Exception
    {
        if (args.length < 3){
            System.out.println("Uso correto:  <bound> <serialPort> <httpPort>");
            System.exit(1);
        }
        System.out.println("\nServidor smartvase v0.0.1");
        System.out.println("\nBound: " + args[0] + "\nPorta serial: " + args[1] + "\nPorta http: " + args[2]);
        System.out.println("---------------------------------------------------------------------");
        Communication communication = new Communication(args[1], Integer.parseInt(args[0]));
        new Server(communication, Integer.parseInt(args[2]));
    }
}