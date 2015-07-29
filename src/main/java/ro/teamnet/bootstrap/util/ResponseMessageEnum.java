package ro.teamnet.bootstrap.util;

/**
 * Created by Radu.Hoaghe on 7/28/2015.
 *
 * Enum folosit pentru stocarea mesajelor care trebuiesc returnate in raspuns in urma unui request de tip POST
 * prin care se trimite tokenul aferent unui fisier uploadat pe server.
 */
public enum ResponseMessageEnum {
    FILE_NOT_FOUND_ON_SERVER("file not found on server's file system"),
    FILE_SAVED("file saved successfully"),
    FILE_NOT_DELETED_FROM_SERVER("file could not be deleted from server's file system"),
    FILE_WAS_SAVED("file was already saved or file token has expired"),
    FILE_TOKEN_EXPIRED("file token expired");

    private final String message;

    ResponseMessageEnum(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
