import java.io.*;
import java.util.Arrays;

public class Huffman {
    static final int LENGTH = 256;
    int[] frequencies = new int[LENGTH];
    int totalFrequencies;
    BitString[] huffmanTable = new BitString[LENGTH];
    byte[] bytes;


    public void compress(String inName, String outName) throws IOException {
        readNewFile(inName);
        totalFrequencies = totalFrequencies();
        generateHuffmanTable();
        byte[] compressed = createCompressedBytes();

        FileOutputStream fileOutputStream = new FileOutputStream(outName);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);


        for(int i = 0; i < frequencies.length; i ++){
            dataOutputStream.writeInt(frequencies[i]);
        }
        dataOutputStream.write(compressed);
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    public void decompress(String inName, String outName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(inName);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        byte[] compressedData;

        for(int i = 0; i < frequencies.length; i ++){
            frequencies[i] = dataInputStream.readInt();
        }

        int bytesLeft = fileInputStream.available();
        compressedData = new byte[bytesLeft];
        dataInputStream.read(compressedData);

        totalFrequencies = totalFrequencies();
        Heap huffmanTree = createHuffmanTree();
        byte[] decompressedText = new byte[Arrays.stream(frequencies).sum()];

        int byteIndexCounter = 0;

        Node root = huffmanTree.node[0];
        for(int i = 0; i < compressedData.length - 1; i ++){
            int bits;
            if(i == compressedData.length - 2){
                bits = compressedData[compressedData.length-1]-1;
            }else{
                bits = 7;
            }
            for(int b = bits; b > -1; b --){
                int bit = ((compressedData[i] + 256) >> b) & (0b00000001);
                if(bit == 0){
                    root = root.l;
                    if(root.l== null && root.r== null){
                        decompressedText[byteIndexCounter] = (byte) root.character;
                        byteIndexCounter++;
                        root = huffmanTree.node[0];
                    }
                }else{
                    root = root.r;
                    if(root.l== null && root.r== null){
                        decompressedText[byteIndexCounter] = (byte) root.character;
                        byteIndexCounter++;
                        root = huffmanTree.node[0];
                    }
                }
            }
        }

        FileOutputStream output = new FileOutputStream(outName);
        DataOutputStream outputStream = new DataOutputStream(output);
        outputStream.write(decompressedText);
        outputStream.flush();
        outputStream.close();
    }


    private byte[] createCompressedBytes(){
        double bitLength = 0;
        for(int i = 0; i < bytes.length; i ++){
            bitLength += huffmanTable[bytes[i] & 0xff].bitsAmount;
        }

        int byteSize;
        if((bitLength/8) % 1 == 0){
            byteSize = (int) (bitLength/8);
        }else{
            byteSize = (int) (bitLength/8) + 1;
        }

        byte[] byteToBeSent = new byte[byteSize+1];

        int byteCounter = 0;
        int spaceLeft = 8;
        int lastByteCounter = 0;

        for(int i = 0; i < bytes.length; i ++) {
            BitString bitString = huffmanTable[bytes[i] & 0xff];
            for (int b = bitString.bitsAmount - 1; b > -1; b--) {
                byte bitToAdd = (byte) ((bitString.bitString >> b) & 1);
                if (spaceLeft == 0) {
                    byteCounter++;
                    byteToBeSent[byteCounter] = bitToAdd;
                    spaceLeft = 8;
                } else {
                    byteToBeSent[byteCounter] = (byte) (byteToBeSent[byteCounter] << 1);
                    byteToBeSent[byteCounter] |= bitToAdd;
                }
                if(byteCounter == byteSize - 1){
                    lastByteCounter ++;
                }
                spaceLeft--;
            }
        }

        byteToBeSent[byteToBeSent.length-1] = (byte) lastByteCounter;
        return byteToBeSent;
    }

    private Heap createHuffmanTree(){
        Heap huffmanHeap = fillHeap();

        while(huffmanHeap.length != 1){
            Node node1 = huffmanHeap.getMin();
            Node node2 = huffmanHeap.getMin();
            Node newNode = new Node(0, node1.freq+ node2.freq);
            newNode.l= node1;
            newNode.r= node2;
            huffmanHeap.addNode(newNode);
        }
        return huffmanHeap;
    }

    private void generateHuffmanTable(){
        Heap huffmanHeap = createHuffmanTree();
        generateHuffmanTable(huffmanHeap.node[0], "");
    }


    private void generateHuffmanTable(Node root, String s){
        if(root.l== null && root.r== null){
            huffmanTable[root.character] = new BitString(s.length(), root.character, Long.parseLong(s, 2));
            return;
        }

        generateHuffmanTable(root.l, s + "0");
        generateHuffmanTable(root.r, s + "1");
    }


    private void readNewFile(String path) throws IOException {
        resetFrequencyArray();

        InputStream input = new FileInputStream(path);
        DataInputStream dataInputStream = new DataInputStream(input);
        int count = input.available();
        bytes = new byte[count];

        dataInputStream.read(bytes);
        for(byte b : bytes){
            frequencies[b & 0xff]++;
        }
    }

    private Heap fillHeap(){
        Heap heap = new Heap(totalFrequencies);
        int counter = 0;
        for(int i = 0; i < frequencies.length; i ++){
            if(frequencies[i] != 0){
                Node node = new Node(i, frequencies[i]);
                heap.node[counter] = node;
                counter++;
            }
        }
        heap.createHeap();
        return heap;
    }

    private int totalFrequencies(){
        int count = 0;
        for(int i = 0; i < frequencies.length; i ++){
            if(frequencies[i] != 0){
                count++;
            }
        }
        return count;
    }

    private void resetFrequencyArray(){
        frequencies = new int[LENGTH];
    }

}
