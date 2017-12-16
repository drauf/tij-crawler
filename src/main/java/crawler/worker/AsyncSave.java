package crawler.worker;

import logger.GuiLogger;
import logger.Logger;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;

public class AsyncSave implements Callable<Void> {

    static final String saveToPath = "/Projects/tij/";
    static final Logger logger = Logger.getLogger(GuiLogger.class);
    final URI url;
    final Document document;

    AsyncSave(URI url, Document document) {
        this.url = url;
        this.document = document;
    }

    @Override
    public Void call() throws Exception {
        String targetPath = saveToPath + url.toString().replace('/', '_');
        Path file = Paths.get(targetPath);
        try (AsynchronousFileChannel asyncFile = AsynchronousFileChannel.open(file,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE)) {

            asyncFile.write(ByteBuffer.wrap(document.toString().getBytes()), 0);
        } catch (IOException e) {
            logger.error(String.format("Error when saving an asset: %s%n", e.getMessage()));
            throw e;
        }

        return null;
    }
}
