import java.io.IOException;
import java.util.Arrays;

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

        }


        /*
        Aes aes = new Aes();
        byte [] temp = aes.readKey("C:\\Users\\tomer\\Desktop\\test files\\key_long");
        //aes.writeKeys("C:\\Users\\tomer\\Desktop\\test files\\test.txt",temp);
        byte [][] matrix = aes.convertToMatrix(temp);
        byte [] temp2 = aes.convertMatrixToArr(matrix);
        System.out.println(Arrays.equals(temp,temp2));
        */

    }
}
