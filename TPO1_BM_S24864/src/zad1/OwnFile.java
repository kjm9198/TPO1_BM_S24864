package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.APPEND;

public class OwnFile extends SimpleFileVisitor <Path> {

    private FileChannel outputChannel;
    private ByteBuffer similarBuffer;

    public Charset in = Charset.forName("Cp1250");
    public Charset out = Charset.forName("UTF-8");


    public OwnFile(Path outputFile) throws IOException {
        this.outputChannel = FileChannel.open(outputFile, EnumSet.of(CREATE, APPEND));
    }

    private void recodeUTF(FileChannel inputChannel, long bufferSize) {

        similarBuffer = ByteBuffer.allocate((int)bufferSize + 1);
        similarBuffer.clear();

        try {
            inputChannel.read(similarBuffer);
            similarBuffer.flip();
            CharBuffer charBuffer = in.decode(similarBuffer);
            ByteBuffer byteBuffer = out.encode(charBuffer);

            while (byteBuffer.hasRemaining())
                this.outputChannel.write(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public FileVisitResult visitFile(Path filePath, BasicFileAttributes attributes) {

        System.out.format("File: %s ", filePath);
        System.out.println("( " + attributes.size() + "bytes )");

        try {
            this.recodeUTF(FileChannel.open(filePath), attributes.size());

        } catch(IOException ex){
            System.out.format("Unable to open file: %s ", filePath);
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path filePath, IOException e) {

        System.err.println("OwnFileVisitor.visitFileNotSuccessful: " + e);

        return CONTINUE;
    }

}
