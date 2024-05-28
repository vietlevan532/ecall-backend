package vn.ecall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import vn.ecall.domain.businessmodel.News;
import vn.ecall.security.AuthoritiesConstants;
import vn.ecall.service.NewsService;
import vn.ecall.service.dto.NewsDTO;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NewsResource {

    public static class NewsResourceException extends RuntimeException {
        public NewsResourceException(String message) { super(message); }
    }

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
            Arrays.asList("id", "name")
    );

    private final Logger log = LoggerFactory.getLogger(NewsResource.class);

    private final NewsService newsService;

    public NewsResource(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Get all news
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all news
     */
    @GetMapping(value = "/news")
    public ResponseEntity<List<NewsDTO>> getAllNews(Pageable pageable) {
        //log right after entering a method
        log.debug("REST request to get all News for an admin");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final List<NewsDTO> page = newsService.getAllNews(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(value="/news/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Integer id) {
        log.debug("REST request to get User by id");
        NewsDTO news =  newsService
                .getNewsById(id)
                .orElseThrow(() -> new NewsResourceException("News with id " + id + " not found"));
        return new ResponseEntity<>(news, HttpStatus.FOUND);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code POST /admin/news}: Create new news
     *
     * @param news news to be save
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new news, or with status {@code 400 (Bad Request)}
     * @throws URISyntaxException if the location URI is incorrect
     */
    @PostMapping(path = "/admin/news")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<News> createNews(@Valid @RequestBody News news) throws URISyntaxException {
        log.debug("REST request to save News: {}", news);
        News newNews = newsService.createNews(news);
        return ResponseEntity
                .created(new URI("api/admin/news" + news.getId()))
                .body(newNews);
    }

    /**
     * {@code @PUT /admin/news} update news
     *
     * @param news news to update
     * @return update news
     * @throws ResponseStatusException
     */
    @PutMapping("/admin/news")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<News> updateNews(@Valid @RequestBody News news) {
        Optional<News> updateNews = newsService.updateNews(news);
        log.debug("updateNews: {}", updateNews);
        return updateNews
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * {@code DELETE /provider/products/:id}: delete product with this id.
     *
     * @param id the id of the product to delete
     * @return
     */
    @DeleteMapping("/admin/news/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteNews(@PathVariable Integer id) {
        log.debug("REST request to delete Product: {}", id);
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/news/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadNewsImageToAWSS3(@RequestParam("file")MultipartFile file) {
        return ResponseEntity
                .ok()
                .body(newsService.uploadNewsImage(file));
    }
}
