package crawler.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public final class CrawlerUtils {

    public static Optional<URI> validateUrl(URI baseUrl, String urlToValidate) {
        if (urlToValidate.startsWith("#")
                || urlToValidate.startsWith("javascript:")
                || urlToValidate.startsWith("mailto:")) {
            return Optional.empty();
        }

        if (urlToValidate.startsWith("//")) {
            urlToValidate = "http:" + urlToValidate;
        }

        if (urlToValidate.startsWith("/")) {
            try {
                return Optional.of(new URI("http://" + baseUrl.getHost() + urlToValidate));
            } catch (URISyntaxException e) {
                return Optional.empty();
            }
        }

        try {
            URI url = new URI(urlToValidate);

            if (!url.isAbsolute()) {
                String base = baseUrl.toString();
                url = new URI(base.substring(0, base.lastIndexOf('/') + 1) + url.toString());
                url = url.normalize();
            }

            return (baseUrl.getHost().equals(url.getHost())) ? Optional.of(url) : Optional.empty();
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }
}
