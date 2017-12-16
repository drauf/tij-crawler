package crawler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.StringTokenizer;

public final class CrawlerUtils {

    private static final String DISALLOW = "Disallow:";

    static boolean robotSafe(URL url) {
        String strHost = url.getHost();

        // form URL of the robots.txt file
        String strRobot = "http://" + strHost + "/robots.txt";
        URL urlRobot;
        try {
            urlRobot = new URL(strRobot);
        } catch (MalformedURLException e) {
            // something weird is happening, so don't trust it
            return false;
        }

        String strCommands;
        try {
            InputStream urlRobotStream = urlRobot.openStream();

            // read in entire file
            byte b[] = new byte[1000];
            int numRead = urlRobotStream.read(b);
            strCommands = new String(b, 0, numRead);
            while (numRead != -1) {
                numRead = urlRobotStream.read(b);
                if (numRead != -1) {
                    String newCommands = new String(b, 0, numRead);
                    strCommands += newCommands;
                }
            }
            urlRobotStream.close();
        } catch (IOException e) {
            // if there is no robots.txt file, it is OK to search
            return true;
        }

        // assume that this robots.txt refers to us and
        // search for "Disallow:" commands.
        String strURL = url.getFile();
        int index = 0;
        while ((index = strCommands.indexOf(DISALLOW, index)) != -1) {
            index += DISALLOW.length();
            String strPath = strCommands.substring(index);
            StringTokenizer st = new StringTokenizer(strPath);

            if (!st.hasMoreTokens())
                break;

            String strBadPath = st.nextToken();

            // if the URL starts with a disallowed path, it is not safe
            if (strURL.indexOf(strBadPath) == 0)
                return false;
        }

        return true;
    }

    public static Optional<URI> validateUrl(URI baseUrl, String urlToValidate) {
        if (isInvalid(urlToValidate)) return Optional.empty();
        urlToValidate = addProtocolIfSkipped(urlToValidate);

        if (urlToValidate.startsWith("/")) {
            return handleAbsolutePathUrls(baseUrl, urlToValidate);
        }

        try {
            return handleRemainingUrls(baseUrl, urlToValidate);
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    private static boolean isInvalid(String urlToValidate) {
        return urlToValidate.startsWith("#")
                || urlToValidate.startsWith("javascript:")
                || urlToValidate.startsWith("mailto:");
    }

    private static String addProtocolIfSkipped(String urlToValidate) {
        if (urlToValidate.startsWith("//")) {
            urlToValidate = "http:" + urlToValidate;
        }
        return urlToValidate;
    }

    private static Optional<URI> handleAbsolutePathUrls(URI baseUrl, String urlToValidate) {
        try {
            return Optional.of(new URI("http://" + baseUrl.getHost() + urlToValidate));
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    private static Optional<URI> handleRemainingUrls(URI baseUrl, String urlToValidate) throws URISyntaxException {
        URI url = new URI(urlToValidate);

        if (!url.isAbsolute()) {
            String base = baseUrl.toString();
            url = new URI(base.substring(0, base.lastIndexOf('/') + 1) + url.toString());
            url = url.normalize();
        }

        return (url.getHost() != null && url.getHost().endsWith(baseUrl.getHost())) ? Optional.of(url) : Optional.empty();
    }
}
