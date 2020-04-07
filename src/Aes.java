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
}
