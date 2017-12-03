package me.arthurlins.smartvase.Comm;

public enum Command {
    //GET_HANDSHAKE('0'),
    GET_UMIDADE ('1'),
    GET_HORA('2'),
    SET_HORA('4'),
    IRRIGAR('3'),
    SET_QTD_AGUA('5'),
    GET_QTD_AGUA('6'),
    GET_AGENDA('7'),
    SET_AGENDA('8'),



    RESET_ALL('r');




    private char value;

    Command(char value){
        this.value = value;
    }

    public char getValue(){
        return value;
    }
}
