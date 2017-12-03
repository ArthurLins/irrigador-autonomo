package me.arthurlins.smartvase.Http;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import me.arthurlins.smartvase.Comm.Command;
import me.arthurlins.smartvase.Comm.Communication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class Server {

    private Communication arduinoComm;
    private HttpServer server;



    public Server(Communication communication, int port) throws IOException{
        System.out.print("Inicializando o servidor Http (:"+port+")... ");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server = server;
        this.arduinoComm = communication;
        server.createContext("/", this::index);
        server.createContext("/hi", this::index);
        server.createContext("/get/umidade", this::umidade);
        server.createContext("/get/hora", this::getHora);
        server.createContext("/get/qtdAgua", this::getQtdAgua);
        server.createContext("/get/agenda", this::getAgenda);
        server.createContext("/set/hora/", this::setHora);
        server.createContext("/set/qtdAgua/", this::setQtdAgua);
        server.createContext("/set/agenda/", this::setAgenda);
        server.createContext("/irrigar/", this::irrigar);

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.print("OK.");
    }

    public HttpServer getServer() {
        return server;
    }

    public Communication getArduinoComm() {
        return arduinoComm;
    }

    private void index(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            String response = "hello";
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, response.length());
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }
    private void umidade(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            //String response = arduinoComm.sendCommand(Command.GET_UMIDADE);
            String response = this.getArduinoComm().sendCommand(Command.GET_UMIDADE);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, response.length());
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }
    private void getHora(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            String response =  this.getArduinoComm().sendCommand(Command.GET_HORA);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, response.length());
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }

    private void setHora(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            String res = ex.getRequestURI().getQuery();
            String response;
            this.getArduinoComm().sendSetCommand(Command.SET_HORA, res);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            if (res.length() == 15){
                response = "set";
                ex.sendResponseHeaders(200, response.length());
            } else {
                response = "req error";
                ex.sendResponseHeaders(400, response.length());
            }
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }

    private void setQtdAgua(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            String res = ex.getRequestURI().getQuery();
            String response;
            this.getArduinoComm().sendSetCommand(Command.SET_QTD_AGUA, res);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            if (res.length() <= 5){
                response = "set";
                ex.sendResponseHeaders(200, response.length());
            } else {
                response = "req error..";
                ex.sendResponseHeaders(400, response.length());
            }
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }

    private void getQtdAgua(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            String response;
            response = this.getArduinoComm().sendCommand(Command.GET_QTD_AGUA);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, response.length());
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }

    private void irrigar (HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            int res = (ex.getRequestURI().getQuery() == null)? 100: Integer.parseInt(ex.getRequestURI().getQuery());
            String response;
            String delayTime = this.getArduinoComm().sendSetCommand(Command.IRRIGAR, ""+res);
            response = delayTime.replace("\n","")+"#"+ res+ "ml";
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, response.length());
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }

    private void setAgenda(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            String res = ex.getRequestURI().getQuery();
            String response;
            this.getArduinoComm().sendSetCommand(Command.SET_AGENDA, res);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            if (res.length() == 13){
                response = "set";
                ex.sendResponseHeaders(200, response.length());
            } else {
                response = "req error..";
                ex.sendResponseHeaders(400, response.length());
            }
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }

    private void getAgenda(HttpExchange ex) throws IOException{
        if (this.getArduinoComm().getReady()){
            //String response = arduinoComm.sendCommand(Command.GET_UMIDADE);
            String response = this.getArduinoComm().sendCommand(Command.GET_AGENDA);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, response.length());
            OutputStream os =  ex.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        error(ex);
    }


    private void error(HttpExchange ex) throws IOException{
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(500, 0);
        ex.close();
    }

}

