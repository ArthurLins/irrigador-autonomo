package me.arthurlins.smartvase.Comm;


import me.arthurlins.smartvase.Arduino.Arduino;

public class Communication {

    private boolean bootstraping = true;
    private Arduino arduino;
    private boolean ready = false;
    private boolean waiting = false;

    public Communication(String port, int bound){
        System.out.print("Inicializando conexão... ");
        Arduino arduino = new Arduino();
        arduino.setPortDescription(port);
        arduino.setBaudRate(bound);
        this.arduino = arduino;
        this.boot();

    }

    private void boot(){
        if (this.getArduino().openConnection()){
            System.out.println("OK.");
            System.out.print("Sincronizando arduino... ");
        } else {
            System.out.println("FAILL");
            System.out.print("Erro ao conectar com o arduino! tentando novamente... ");
            this.boot();
        }
        while (isBootstraping()){
            String line;
            this.getArduino().serialWrite(Command.GET_HANDSHAKE.getValue());
            line = this.getArduino().serialRead().replace("\n","");
            if (line.equals("hi")) {
                this.setBootstraping(false);
                System.out.println("OK.");
                this.setReady(true);
                this.checkComm();
                break;
            }
        }
    }

    private void checkComm(){
        Thread checkComm = new Thread(() -> {
            while (true){
                if (!this.getArduino().getSerialPort().isOpen() && !this.isBootstraping()){
                    System.out.print("\nConexão perdida. Tentando conetar... ");
                    if (this.getArduino().openConnection()){
                        this.setReady(true);
                        System.out.print("OK.");
                    } else {
                        this.setReady(false);
                        System.out.print("FAIL.");
                    }
                }
            }
        });
        checkComm.start();
    }


    public String sendCommand(Command command){
        if (this.isWaiting())
            return "";
        this.setWaiting(true);
        this.getArduino().serialWrite(command.getValue());
        String rd = this.getArduino().serialRead();
        this.setWaiting(false);
        return rd;
    }

    public String sendSetCommand(Command command, String info){
        if (this.isWaiting())
            return "";
        this.setWaiting(true);
        this.getArduino().serialWrite(command.getValue());
        this.getArduino().serialWrite(info);
        String rd = this.getArduino().serialRead();
        this.setWaiting(false);
        return rd;

    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    private Arduino getArduino(){
        return this.arduino;
    }

    public boolean getReady(){
        return this.ready;
    }

    public boolean isBootstraping() {
        return bootstraping;
    }

    public void setBootstraping(boolean bootstraping) {
        this.bootstraping = bootstraping;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }


}
