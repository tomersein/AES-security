import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Aes {

    /**
     * this function gets a path, reads a file and returns an array of bytes
     * @param path to the file
     * @return byte array
     * @throws IOException
     */
    public byte [] readKey (String path) throws IOException {
        Path filePath = Paths.get(path);
        byte [] arrByte = Files.readAllBytes(filePath);
        return arrByte;
    }

    /**
     * this function takes an array and writes it into a file
     * @param path the path we will write the file to
     * @param arrByte the array we will write to a file
     * @throws IOException
     */
    public void writeKeys (String path, byte [] arrByte) throws IOException {
        Path filePath = Paths.get(path);
        Files.write(filePath,arrByte);
    }

    /**
     * The function receives a message and 3 keys and uses the AES algorithm to decvrypt/encrypt the message
     *
     * @param message
     * @param firstKey
     * @param secondKey
     * @param thirdKey
     * @return aesMessage
     */
    public byte[][] advancedEncryptionStandard(byte[][] message, byte[][] firstKey, byte[][] secondKey, byte[][] thirdKey) {

        byte[][] aesMessage = new byte[4][4];
        for (int i = 0; i < aesMessage.length; i++) {
            for (int j = 0; j < aesMessage[0].length; j++) {
                aesMessage[i][j] = message[i][j];
            }
        }
        //first iteration
        aesMessage = shiftColumns(aesMessage);
        aesMessage = addRoundKey(aesMessage, firstKey);
        //second iteration
        aesMessage = shiftColumns(aesMessage);
        aesMessage = addRoundKey(aesMessage, secondKey);
        //third iteration
        aesMessage = shiftColumns(aesMessage);
        aesMessage = addRoundKey(aesMessage, thirdKey);

        return aesMessage;
    }

    /**
     * The function shifts the message's columns upwards (for encryption)
     * @param message
     * @return shiftedMessage
     */
    private byte[][] shiftColumns(byte[][] message) {

        byte[][] shiftedMessage = new byte[4][4];
        //not touching the first column
        for (int i = 0; i < 4; i++) {
            shiftedMessage[i][0] = message[i][0];
        }

        //shifting the second column one cell upwards
        for (int i = 1; i < 4; i++) {
            shiftedMessage[i-1][1] = message[i][1];
        }
        shiftedMessage[3][1] = message[0][1];

        //shifting the third column two cells upwards
        shiftedMessage[0][2] = message[2][2];
        shiftedMessage[1][2] = message[3][2];
        shiftedMessage[2][2] = message[0][2];
        shiftedMessage[3][2] = message[1][2];

        //shifting the fourth column three cells upwards
        for(int i=0; i<3; i++){
            shiftedMessage[i+1][3] = message[i][3];
        }
        shiftedMessage[0][3] = message[3][3];

        return shiftedMessage;
    }

    /**
     * The function shifts the message's columns downwards (for decryption)
     * @param message
     * @return shiftedMessage
     */
    private byte[][] shiftColumnsBackwards(byte[][] message){

        byte[][] shiftedMessage = new byte[4][4];
        //not touching the first column
        for (int i = 0; i < 4; i++) {
            shiftedMessage[i][0] = message[i][0];
        }
        //shifting the second column one cell downwards
        for (int i = 1; i < 4; i++) {
            shiftedMessage[i][1] = message[i-1][1];
        }
        shiftedMessage[0][1] = message[3][1];
        //shifting the third column two cells downwards
        shiftedMessage[0][2] = message[2][2];
        shiftedMessage[1][2] = message[3][2];
        shiftedMessage[2][2] = message[0][2];
        shiftedMessage[3][2] = message[1][2];

        //shifting the fourth column three cells downwards
        for(int i=0; i<3; i++){
            shiftedMessage[i][3] = message[i+1][3];
        }
        shiftedMessage[3][3] = message[0][3];

        return shiftedMessage;
    }

    /**
     * The function adds the key to the message by using the 'xor' function
     * @param message
     * @param key
     * @return addRoundKey
     */
    private byte[][] addRoundKey(byte[][] message, byte[][] key){

        byte[][] roundedMessage = new byte[4][4];
        int messageBit;
        int keyBit;
        int combinedBit;

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                messageBit = (int)message[j][i];
                keyBit = (int)key[j][i];
                combinedBit = messageBit^keyBit;
                roundedMessage[j][i] = (byte)(0xff & combinedBit);
            }
        }
        return roundedMessage;
    }



}

