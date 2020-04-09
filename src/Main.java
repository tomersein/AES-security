import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String action = args[0];
        String pathToKey = args[2];
        String pathToInput = args[4];
        String pathToOutput = args [6];
        Aes aes = new Aes();

        //encrypt
        if(action.equals("-e")){
            aes.encrypt(pathToKey,pathToInput,pathToOutput);
        }
        //decrypt
        else if(action.equals("-d")){
            aes.decrypt(pathToKey,pathToInput,pathToOutput);
        }
        //break key
        else if(action.equals("-b")){
            //notice pathToKey is not the key here, it is the message and the Input is the Cipher
            aes.breakKey(pathToKey, pathToInput, pathToOutput);
        }

    }
}
