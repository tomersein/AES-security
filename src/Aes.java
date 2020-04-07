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
     * this function takes an array of bytes and converts it into a matrix
     * @param byteArr
     * @return
     */
    public byte [][] convertToMatrix (byte [] byteArr){
        int numOfCol = byteArr.length/4;
        byte [][] matrix = new byte [4][numOfCol];
        int indexOfByteArray = 0;
        for(int i=0; i<numOfCol;i++){
            for(int j=0;j<4; j++){
                matrix[j][i]=byteArr[indexOfByteArray];
                indexOfByteArray++;
            }
        }
        return matrix;
    }

    /**
     * this function takes a matrix and converts it into an array
     * @param matrix of bytes
     * @return the array
     */
    public byte [] convertMatrixToArr (byte [][] matrix){
        int arrLength = matrix.length * matrix[0].length;
        byte [] arrByte = new byte [arrLength];
        int counter = 0;
        for(int i=0; i<matrix[0].length;i++){
            for(int j=0; j<matrix.length; j++){
                arrByte[counter]=matrix[j][i];
                counter++;
            }
        }
        return arrByte;
    }

}
