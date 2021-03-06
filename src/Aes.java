import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

public class Aes {


    /**
     * this function gets a path, reads a file and returns an array of bytes
     *
     * @param path to the file
     * @return byte array
     * @throws IOException
     */
    public byte[] readKey(String path) throws IOException {
        Path filePath = Paths.get(path);
        byte[] arrByte = Files.readAllBytes(filePath);
        return arrByte;
    }

    /**
     * this function takes an array and writes it into a file
     *
     * @param path    the path we will write the file to
     * @param arrByte the array we will write to a file
     * @throws IOException
     */
    public void writeKeys(String path, byte[] arrByte) throws IOException {
        Path filePath = Paths.get(path);
        Files.write(filePath, arrByte);
    }

    /**
     * this function takes 3 keys in one array and separates it to 3 arrays
     * @param keys the array that contains 3 keys
     * @return a list of three arrays of keys
     */
    public ArrayList<byte []> separateKeys (byte [] keys){
        ArrayList <byte []> separatedKeys = new ArrayList();
        int numOfKey = 0;
        int indexInMainArr=0;
        while (numOfKey<3){
            byte [] tempArr = new byte[(keys.length/3)];
            for(int i=0;i<tempArr.length;i++){
                tempArr[i]=keys[indexInMainArr];
                indexInMainArr++;
            }
            separatedKeys.add(tempArr);
            numOfKey++;
        }
        return separatedKeys;
    }

    /**
     * main function that encrypts the file
     * @param pathToKey the location of the keys
     * @param pathToInput the location of the input file (plaintext)
     * @param pathToOutput the location to write the cipher
     *@throws IOException
     */
    public void encrypt (String pathToKey, String pathToInput, String pathToOutput) throws IOException {
        byte [] keys = readKey(pathToKey);
        ArrayList separateKeys = separateKeys(keys);
        byte [] message = readKey(pathToInput);
        byte [][] firstKey = convertToMatrix((byte[]) separateKeys.get(0));
        byte [][] secondKey = convertToMatrix((byte[]) separateKeys.get(1));
        byte [][] thirdKey = convertToMatrix((byte[]) separateKeys.get(2));
        int index = 0;
        byte [] finalToDisk = new byte[message.length];
        while(index<message.length){
            byte [] toEncrypt = new byte [16];
            for(int i=0;i<16;i++){
                toEncrypt[i]=message[i+index];
            }
            byte [][] matrixMessage = convertToMatrix(toEncrypt);
            byte [][] encryptedMessage = encryptMessage(matrixMessage,firstKey,secondKey,thirdKey);
            byte [] tempMessageToDisk = convertMatrixToArr(encryptedMessage);
            for(int i=0;i<tempMessageToDisk.length;i++){
                finalToDisk[i+index]=tempMessageToDisk[i];
            }
            index=index+16;
        }

        //FOR TEST ONLY

        /*
        byte [] forTEST = readKey("D:\\AES3_test_files\\test files\\cipher_short");
        System.out.println(Arrays.equals(forTEST,finalToDisk));
        */

        writeKeys(pathToOutput,finalToDisk);
    }

    /**
     *
     * main function that decrypts the file
     * @param pathToKey the location of the keys
     * @param pathToInput the location of the input file (cipher text)
     * @param pathToOutput the location to write the message
     * @throws IOException
     */
    public void decrypt (String pathToKey, String pathToInput, String pathToOutput) throws IOException {
        /*
        byte [] keys = readKey(pathToKey);
        ArrayList separateKeys = separateKeys(keys);
        byte [][] firstKey = convertToMatrix((byte[]) separateKeys.get(0));
        byte [][] secondKey = convertToMatrix((byte[]) separateKeys.get(1));
        byte [][] thirdKey = convertToMatrix((byte[]) separateKeys.get(2));
        byte [] message = readKey(pathToInput);
        byte [][] matrixMessage = convertToMatrix(message);
        byte [][] decMessage = decryptMessage(matrixMessage,firstKey,secondKey,thirdKey);
        byte [] messageToDisk = convertMatrixToArr(decMessage);
        writeKeys(pathToOutput,messageToDisk);
        */
        byte [] keys = readKey(pathToKey);
        ArrayList separateKeys = separateKeys(keys);
        byte [] message = readKey(pathToInput);
        byte [][] firstKey = convertToMatrix((byte[]) separateKeys.get(0));
        byte [][] secondKey = convertToMatrix((byte[]) separateKeys.get(1));
        byte [][] thirdKey = convertToMatrix((byte[]) separateKeys.get(2));
        int index = 0;
        byte [] finalToDisk = new byte[message.length];
        while(index<message.length){
            byte [] toDecrypt = new byte [16];
            for(int i=0;i<16;i++){
                toDecrypt[i]=message[i+index];
            }
            byte [][] matrixMessage = convertToMatrix(toDecrypt);
            byte [][] encryptedMessage = decryptMessage(matrixMessage,firstKey,secondKey,thirdKey);
            byte [] tempMessageToDisk = convertMatrixToArr(encryptedMessage);
            for(int i=0;i<tempMessageToDisk.length;i++){
                finalToDisk[i+index]=tempMessageToDisk[i];
            }
            index=index+16;
        }
        //FOR TEST ONLY

        /*
        byte [] forTEST = readKey("D:\\AES3_test_files\\test files\\message_long");
        System.out.println(Arrays.equals(forTEST,finalToDisk));
        */

        writeKeys(pathToOutput,finalToDisk);
    }

    public void breakKey (String pathToMessage, String pathToCipher, String pathToOutput) throws IOException {
        byte [] messageArr = readKey(pathToMessage);
        byte [] cipherArr = readKey(pathToCipher);
        byte [][] messageMatrix = convertToMatrix(messageArr);
        byte [][] cipherMatrix = convertToMatrix(cipherArr);
        byte [] keys = breakAesAlgorithm(messageMatrix,cipherMatrix);

        writeKeys(pathToOutput,keys);

    }

    /**
     * The function receives a message and 3 keys and encrypts the message using the 3 keys
     *
     * @param message
     * @param firstKey
     * @param secondKey
     * @param thirdKey
     * @return blockToEncrypt
     */
    public byte[][] encryptMessage(byte[][] message, byte[][] firstKey, byte[][] secondKey, byte[][] thirdKey) {

        byte[][] blockToEncrypt = new byte[4][4];
        for (int i = 0; i < blockToEncrypt.length; i++) {
            for (int j = 0; j < blockToEncrypt[0].length; j++) {
                blockToEncrypt[i][j] = message[i][j];
            }
        }
        //first iteration
        blockToEncrypt = shiftColumns(blockToEncrypt);
        blockToEncrypt = addRoundKey(blockToEncrypt, firstKey);
        //second iteration
        blockToEncrypt = shiftColumns(blockToEncrypt);
        blockToEncrypt = addRoundKey(blockToEncrypt, secondKey);
        //third iteration
        blockToEncrypt = shiftColumns(blockToEncrypt);
        blockToEncrypt = addRoundKey(blockToEncrypt, thirdKey);

        return blockToEncrypt;
    }

    /**
     * The function receives a message and 3 keys and decrypts the message using the 3 keys
     *
     * @param message
     * @param firstKey
     * @param secondKey
     * @param thirdKey
     * @return
     */
    public byte[][] decryptMessage(byte[][] message, byte[][] firstKey, byte[][] secondKey, byte[][] thirdKey) {

        byte[][] blockToEncrypt = new byte[4][4];
        for (int i = 0; i < blockToEncrypt.length; i++) {
            for (int j = 0; j < blockToEncrypt[0].length; j++) {
                blockToEncrypt[i][j] = message[i][j];
            }
        }
        //first iteration
        blockToEncrypt = addRoundKey(blockToEncrypt, thirdKey);
        blockToEncrypt = shiftColumnsBackwards(blockToEncrypt);

        //second iteration
        blockToEncrypt = addRoundKey(blockToEncrypt, secondKey);
        blockToEncrypt = shiftColumnsBackwards(blockToEncrypt);

        //third iteration
        blockToEncrypt = addRoundKey(blockToEncrypt, firstKey);
        blockToEncrypt = shiftColumnsBackwards(blockToEncrypt);


        return blockToEncrypt;
    }


    /**
     * The function shifts the message's columns
     *
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
            shiftedMessage[i - 1][1] = message[i][1];
        }
        shiftedMessage[3][1] = message[0][1];
        //shifting the third column two cells upwards
        shiftedMessage[0][2] = message[2][2];
        shiftedMessage[1][2] = message[3][2];
        shiftedMessage[2][2] = message[0][2];
        shiftedMessage[3][2] = message[1][2];
        //shifting the fourth column three cells upwards
        for (int i = 0; i < 3; i++) {
            shiftedMessage[i + 1][3] = message[i][3];
        }
        shiftedMessage[0][3] = message[3][3];

        return shiftedMessage;
    }

    private byte[][] shiftColumnsBackwards(byte[][] message) {

        byte[][] shiftedMessage = new byte[4][4];
        //not touching the first column
        for (int i = 0; i < 4; i++) {
            shiftedMessage[i][0] = message[i][0];
        }
        //shifting the second column one cell downwards
        for (int i = 1; i < 4; i++) {
            shiftedMessage[i][1] = message[i - 1][1];
        }
        shiftedMessage[0][1] = message[3][1];

        //shifting the third column two cells downwards
        shiftedMessage[0][2] = message[2][2];
        shiftedMessage[1][2] = message[3][2];
        shiftedMessage[2][2] = message[0][2];
        shiftedMessage[3][2] = message[1][2];
        //shifting the fourth column three cells downwards
        for (int i = 0; i < 3; i++) {
            shiftedMessage[i][3] = message[i+1][3];
        }
        shiftedMessage[3][3] = message[0][3];

        return shiftedMessage;
    }

    /**
     * The function adds the key to the message by using the 'xor' function
     *
     * @param message
     * @param key
     * @return addRoundKey
     */
    private byte[][] addRoundKey(byte[][] message, byte[][] key) {

        byte[][] roundedMessage = new byte[4][4];
        int messageBit;
        int keyBit;
        int combinedBit;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                messageBit = (int) message[j][i];
                keyBit = (int) key[j][i];
                combinedBit = messageBit ^ keyBit;
                roundedMessage[j][i] = (byte) (0xff & combinedBit);
            }
        }
        return roundedMessage;
    }

    /**
     * this function takes an array of bytes and converts it into a matrix
     *
     * @param byteArr
     * @return
     */
    public byte[][] convertToMatrix(byte[] byteArr) {
        int numOfCol = byteArr.length / 4;
        byte[][] matrix = new byte[4][numOfCol];
        int indexOfByteArray = 0;
        for (int i = 0; i < numOfCol; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[j][i] = byteArr[indexOfByteArray];
                indexOfByteArray++;
            }
        }
        return matrix;
    }

    /**
     * this function takes a matrix and converts it into an array
     *
     * @param matrix of bytes
     * @return the array
     */
    public byte[] convertMatrixToArr(byte[][] matrix) {
        int arrLength = matrix.length * matrix[0].length;
        byte[] arrByte = new byte[arrLength];
        int counter = 0;
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                arrByte[counter] = matrix[j][i];
                counter++;
            }
        }
        return arrByte;
    }


    /**
     * this function breaks the keys
     * @param message the plaintext
     * @param cipher the cipher text
     * @return he array of the keys
     */
    public byte [] breakAesAlgorithm(byte[][] message, byte[][] cipher) {

        byte[][] attackingBlock;
        byte[][] thirdKey;


        //first, randomize two keys that will aid in finding the third key
        byte[] firstKeyVal = new byte[16];
        byte[] secondKeyVal = new byte[16];
        for (int i = 0; i < firstKeyVal.length; i++) {
            firstKeyVal[i] = (byte) ((int) (Math.random() * 255)-127);
            secondKeyVal[i] = (byte) ((int) (Math.random() * 255)-127);
        }
        byte[][] firstRandomKey = convertToMatrix(firstKeyVal);
        byte[][] secondRandomKey = convertToMatrix(secondKeyVal);

        //now we will generate a new cipher-text using the 2 random
        attackingBlock = shiftColumns(message);
        attackingBlock = addRoundKey(attackingBlock, firstRandomKey);
        attackingBlock = shiftColumns(attackingBlock);
        attackingBlock = addRoundKey(attackingBlock, secondRandomKey);

        //with the two cipher-texts we can now find the actual third key:
        thirdKey = retrieveKey(attackingBlock, cipher);
        byte [] thirdKeyArr = convertMatrixToArr(thirdKey);
        byte [] firstKeyArr = convertMatrixToArr(firstRandomKey);
        byte [] secondKeyArr = convertMatrixToArr(secondRandomKey);

        byte [] finalKeyArr = new byte [3*16];
        LinkedList<byte []> mergedArr = new LinkedList<>();
        mergedArr.add(firstKeyArr);
        mergedArr.add(secondKeyArr);
        mergedArr.add(thirdKeyArr);
        int counter=0;
        for (byte [] arr : mergedArr){
            for (int i=0;i<arr.length;i++){
                finalKeyArr[i+counter]=arr[i];
            }
            counter=counter+16;
        }
        return finalKeyArr;
    }

    private byte[][] retrieveKey(byte[][] message, byte[][] cipher) {

        byte[][] key = new byte[4][4];
        message = shiftColumns(message);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                key[i][j] = (byte) (message[i][j] ^ cipher[i][j]);
            }
        }
        return key;
    }
}

