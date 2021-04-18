
import java.io.*;
import java.util.ArrayList;

public class LempelZiv {
    StringBuffer buffer;

    public LempelZiv() {
        buffer = new StringBuffer(Short.MAX_VALUE);
    }

    public void trimBuffer () {
        if(buffer.length() >= Short.MAX_VALUE)
            buffer = buffer.delete(0, buffer.length() - Short.MAX_VALUE);
    }
    public void compress(String inName, String outName) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(inName));
        DataOutputStream out = new DataOutputStream(new FileOutputStream(outName));

        byte[] bytes = in.readAllBytes();
        String current = "";
        String uncompressed = "";
        int bufferIndex = -1;
        int matchIndex = 0;

        for(int i=0; i < bytes.length; i++)  {
            current += (char)bytes[i];
            bufferIndex = buffer.indexOf(current);
            if(bufferIndex != -1) {
                while(bufferIndex != -1) {
                    i++;
                    if(i == bytes.length || current.length() == Byte.MAX_VALUE)
                        break;
                    matchIndex = bufferIndex;
                    current += (char)bytes[i];
                    bufferIndex = buffer.indexOf(current);
                    if(bufferIndex == -1) {
                        current = current.substring(0,current.length()-1);
                        i--;
                    }
                }
                if(current.length() > 3) {
                    if(uncompressed.length() > 0) {
                        out.write(uncompressed.length() * (-1));
                        out.writeBytes(uncompressed);
                    }
                    out.write(current.length());
                    short s = (short) (buffer.length() - matchIndex);
                    byte b1 = (byte) (s & 0xff);
                    byte b2 = (byte) ((s>>8) & 0xff);
                    out.writeShort(buffer.length() - matchIndex);
                    short s2 = (short)(buffer.length()-matchIndex);
                    buffer.append(current);
                    current = "";
                    uncompressed = "";
                }
            }
            if(uncompressed.length() == Byte.MAX_VALUE) {
                out.write(uncompressed.length() * (-1));
                out.writeBytes(uncompressed);
                uncompressed = "";
            }
            if(bufferIndex == -1 && current.length() > 0) {
                uncompressed += current.charAt(0);
                buffer.append(current.charAt(0));
                current = current.substring(1, current.length());
            }
            trimBuffer();
        }
        if(uncompressed.length() != 0) {
            out.write((uncompressed.length() + current.length()) * (-1));
            out.writeBytes(uncompressed + current);
        }
        in.close();
        out.close();
    }

    public void decompress(String inName, String outName) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(inName));
        DataOutputStream out = new DataOutputStream(new FileOutputStream(outName));

        ArrayList<Byte> tmp = new ArrayList<>();
        byte[] bytes = in.readAllBytes();

        byte current;
        short indexBack;
        int count=0;
        int index=0;
        for(int i=0; i < bytes.length; i++) {
            current = bytes[i];
            if(current < 0) {
                int j=i+1;
                while(j <= Math.abs(current)+i) {
                    tmp.add(bytes[j]);
                    count++;
                    index = j;
                    j++;
                }
                i = index;
            }
            if(current > 0) {
                byte b1 = bytes[i+1];
                byte b2 = bytes[i+2];
                indexBack = (short)((b1 & 0xff) << 8 | b2 & 0xff);
                int interval = ((count - indexBack) + Math.abs(current));
                for(int j = count - indexBack; j < interval; j++) {
                    tmp.add(tmp.get(j));
                    count++;
                }
                i += 2;
            }
        }
        byte[] output = new byte[tmp.size()];
        for(int j=0; j < tmp.size(); j++) {
            output[j] = tmp.get(j);
        }
        out.write(output);
        out.close();
    }

    public static void main(String[] args) {
        try {
            LempelZiv lempelZiv = new LempelZiv();
            lempelZiv.compress("diverse.txt", "compressed.txt");
            lempelZiv.decompress("compressed.txt","decompressed.txt");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
