package crawler.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public final class CrawlerUtils {

    public static boolean isDocument(URI uri) {
        try {
            URL url = new URL(uri.toString());
            URLConnection u = url.openConnection();
            String type = u.getHeaderField("Content-Type");
            return type != null && type.contains("text/html");
        } catch (IOException ignored) {
        }
        return false;
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

        return (url.getHost().endsWith(baseUrl.getHost())) ? Optional.of(url) : Optional.empty();
    }
}
