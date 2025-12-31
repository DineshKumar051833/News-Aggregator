package org.example.java.news.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.java.news.Model.NewsArticle;
import org.example.java.news.Model.NewsArticlesResponse;
import org.example.java.news.Model.SavedNews;
import org.example.java.news.Model.User;
import org.example.java.news.Service.NewsApiService;
import org.example.java.news.Service.SavedNewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NewsController {

    private final NewsApiService newsService;
    private final SavedNewsService savedNewsService;
    private final HttpSession httpSession;

    public NewsController(NewsApiService newsService, SavedNewsService savedNewsService, HttpSession httpSession) {
        this.newsService = newsService;
        this.savedNewsService = savedNewsService;
        this.httpSession = httpSession;
    }

    @GetMapping("/api/news")
    public ResponseEntity<List<NewsArticle>> publicHome(){
        NewsArticlesResponse response = newsService.getTopHeadLines("general","en",50);
        List<NewsArticle> articles = response.getArticles()
                                        .stream()
                                        .filter(news -> news.getImageUrl() != null && !news.getImageUrl().trim().isEmpty())
                                        .toList();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/home")
    public ResponseEntity<?> privateHome() {
        User user = (User) httpSession.getAttribute("user");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first");
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("headlines", newsService.getTopHeadLines("latest", "en", 15).getArticles());
        response.put("everything", newsService.getEverything("general", "en", 15).getArticles());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchArticles(@RequestParam String query,
                                 @RequestParam(defaultValue = "en") String language,
                                 @RequestParam(required = false) String fromDate,
                                 @RequestParam(required = false) String toDate,
                                 @RequestParam(defaultValue = "publishedAt") String sortBy,
                                 @RequestParam(defaultValue = "15") int max){
        try {
            NewsArticlesResponse response = newsService.searchArticles(query,language,fromDate,toDate,sortBy,max);
            return ResponseEntity.ok(response.getArticles());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching search results: " + e.getMessage());
        }
    }

    @PostMapping("/saved")
    public ResponseEntity<?> saveNews(@RequestBody NewsArticle article){
        User user = (User) httpSession.getAttribute("user");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not Logged in");
        if (savedNewsService.checkNewsByUrlAndUserId(article.getUrl(),user.getId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("News Already Saved");
        else
            savedNewsService.saveNews(article, user.getId());
        return ResponseEntity.ok("News Saved Successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(){
        User user = (User) httpSession.getAttribute("user");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not Logged in");
        List<SavedNews> news = savedNewsService.findNewsByUserId(user.getId());
        Map<String,Object> response = new HashMap<>();
        response.put("user",user);
        response.put("articles",news);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile/delete/{newsId}")
    public ResponseEntity<String> deleteSavedNews(@PathVariable Long newsId){
        User user = (User) httpSession.getAttribute("user");
        if (user == null)
            return ResponseEntity.status(401).body("UnAuthorized");
        savedNewsService.deleteSavedNews(newsId, user.getId());
        return ResponseEntity.ok("Deleted");
    }
}