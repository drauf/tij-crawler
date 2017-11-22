package crawler.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CrawlerUtilsTest {

    private static URI baseUrl;
    private static URI baseUrlWithIndex;
    private static final String URL = "http://example.com/path/";
    private static final String URL_WITH_INDEX = "http://example.com/path/index.html";
    private static final String URL_WITH_SUBDOMAIN = "http://subdomain.example.com/";

    @BeforeAll
    static void initAll() throws URISyntaxException {
        baseUrl = new URI(URL);
        baseUrlWithIndex = new URI(URL_WITH_INDEX);
    }

    @Test
    void validateUrl_hash_ReturnEmptyOptional() {
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, "#");
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void validateUrl_hashWithId_ReturnEmptyOptional() {
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, "#bottomOfPage");
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void validateUrl_mailTo_ReturnEmptyOptional() {
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, "mailto:example@example.com");
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void validateUrl_javascript_ReturnEmptyOptional() {
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, "javascript:void(0)");
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void validateUrl_validFullUrlFromDifferentDomain_ReturnEmptyOptional() {
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, "http://valid.url/test");
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void validateUrl_validFullUrl_ReturnUrl() {
        String url = "http://example.com/test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo(url);
    }

    @Test
    void validateUrl_validFullUrlWithSkippedProtocol_ReturnUrl() {
        String url = "//example.com/test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo("http:" + url);
    }

    @Test
    void validateUrl_validFullUrlWithSubdomain_ReturnUrl() {
        String url = "http://subdomain.example.com/";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo(url);
    }

    @Test
    void validateUrl_validFullUrlWithSubdomainAndIndex_ReturnUrl() {
        String url = "http://subdomain.example.com/index.html";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrlWithIndex, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo(url);
    }

    @Test
    void validateUrl_validRelativeUrl_ReturnUrl() {
        String url = "test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo("http://example.com/path/test");
    }

    @Test
    void validateUrl_validRelativeUrlWithDots_ReturnUrl() {
        String url = "../test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo("http://example.com/test");
    }

    @Test
    void validateUrl_validAbsoluteUrl_ReturnUrl() {
        String url = "/test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrl, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo("http://example.com/test");
    }

    @Test
    void validateUrl_validRelativeUrlWithIndex_ReturnUrl() {
        String url = "test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrlWithIndex, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo("http://example.com/path/test");
    }

    @Test
    void validateUrl_validRelativeUrlWithDotsAndIndex_ReturnUrl() {
        String url = "../test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrlWithIndex, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo("http://example.com/test");
    }

    @Test
    void validateUrl_validAbsoluteUrlWithIndex_ReturnUrl() {
        String url = "/test";
        Optional<URI> result = CrawlerUtils.validateUrl(baseUrlWithIndex, url);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().toString()).isEqualTo("http://example.com/test");
    }
}
