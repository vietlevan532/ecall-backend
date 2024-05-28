package vn.ecall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.ecall.a3.AmazonS3Service;
import vn.ecall.a3.BucketName;
import vn.ecall.domain.businessmodel.News;
import vn.ecall.repository.NewsRepository;
import vn.ecall.service.dto.NewsDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NewsService {

    private final Logger log = LoggerFactory.getLogger(NewsService.class);

    private final NewsRepository newsRepository;

    private final AmazonS3Service amazonS3Service;

    public NewsService(NewsRepository newsRepository, AmazonS3Service amazonS3Service) {
        this.newsRepository = newsRepository;
        this.amazonS3Service = amazonS3Service;
    }

    @Transactional(readOnly = true)
    public List<NewsDTO> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .stream()
                .map(NewsDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<NewsDTO> getNewsById(Integer id) {
        return newsRepository.findOneById(id)
                .map(NewsDTO::new);
    }

    public News createNews(News news) {
        News newNews = newsRepository.save(news);
        log.debug("Created Information for News: {}", newNews);
        return newNews;
    }

    public void deleteNews(Integer id) {
        newsRepository
                .findOneById(id)
                .ifPresent(
                        news -> {
                            newsRepository.delete(news);
                            log.debug("Deleted News: {}", news);
                        }
                );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public Optional<News> updateNews(News news) {
        return Optional
                .of(newsRepository.findOneById(news.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(
                        updateNews -> {
                            updateNews.setName(news.getName());
                            updateNews.setLink(news.getLink());
                            updateNews.setImageLink(news.getImageLink());
                            newsRepository.save(updateNews);
                            log.debug("Changed Information for News: {}", updateNews);
                            return updateNews;
                        }
                );
    }

    public String uploadNewsImage(MultipartFile file) {
        String key = "news/images/" + file.getOriginalFilename();
        try {
            amazonS3Service.putObject(
                    BucketName.BUCKET_NAME,
                    key,
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "https://"
                + BucketName.BUCKET_NAME
                + ".s3.ap-southeast-2.amazonaws.com/"
                + key;
    }
}
