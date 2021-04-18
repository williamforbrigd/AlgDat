import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    static LempelZiv lz = new LempelZiv();
    static Huffman huffman = new Huffman();


    public static void main(String[] args) {
        try {
            compress("diverse.txt", "compressed.txt", "compressed1.txt");
            decompress("compressed1.txt", "decompressed.txt", "decompressed1.txt");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Først legges inn filen som skal komprimeres.
    //Deretter legg inn filen som skal ha outputen til filen som er komprimert med lempel ziv.
    //Siste er for huffman komprimerte filen
    public static void compress(String fileToCompress, String lzOutName, String huffmanOutName) throws IOException {
        try (
                DataInputStream in1 = new DataInputStream(new FileInputStream(fileToCompress));
                DataInputStream in2 = new DataInputStream(new FileInputStream(huffmanOutName));
        ) {
            int originalLength = in1.available();
            lz.compress(fileToCompress, lzOutName);
            huffman.compress(lzOutName, huffmanOutName);
            byte[] compress = in2.readAllBytes();
            System.out.println("Size before compression: " + originalLength);
            System.out.println("Size after compression lempel ziv and huffman: " + compress.length);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    //Først en path til filen som skal dekomprimeres. Dette er filen som ble komprimert av huffman algoritmen.
    //Deretter en path til dekomprimerte huffman filen.
    //Siste er path til den lempel ziv dekomprimerte filen.
    public static void decompress(String fileToDecompress, String huffOutName, String lzOutName) {
        try (
                DataInputStream in1 = new DataInputStream(new FileInputStream(huffOutName));
                DataInputStream in2 = new DataInputStream(new FileInputStream(lzOutName));
        ) {
            huffman.decompress(fileToDecompress, huffOutName);
            lz.decompress(huffOutName,lzOutName);
            byte[] decompressed = in2.readAllBytes();
            int halfDecompressed = in1.available();
            System.out.println("Size after one decompression: " + halfDecompressed);
            System.out.println("Size after done with compression: " + decompressed.length);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
