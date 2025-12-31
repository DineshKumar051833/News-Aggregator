package org.example.java.news.Service;

import jakarta.transaction.Transactional;
import org.example.java.news.Model.NewsArticle;
import org.example.java.news.Model.SavedNews;
import org.example.java.news.Model.User;
import org.example.java.news.Repository.SavedNewsRepository;
import org.example.java.news.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedNewsService {

    private final SavedNewsRepository savedNewsRepository;
    private final UserRepository userRepository;

    public SavedNewsService(SavedNewsRepository savedNewsRepository, UserRepository userRepository){
        this.savedNewsRepository = savedNewsRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveNews(NewsArticle newsArticle, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        SavedNews savedNews = new SavedNews();
        savedNews.setCategory("General");
        savedNews.setTitle(newsArticle.getTitle());
        savedNews.setDescription(newsArticle.getDescription());
        savedNews.setImageUrl(newsArticle.getImageUrl());
        savedNews.setUrl(newsArticle.getUrl());
        savedNews.setUser(user);
        savedNewsRepository.save(savedNews);
    }

    @Transactional
    public List<SavedNews> findNewsByUserId(Long id){
        return savedNewsRepository.findByUserId(id);
    }

    @Transactional
    public boolean checkNewsByUrlAndUserId(String url, Long userId){
        return savedNewsRepository.existsByUrlAndUserId(url, userId);
    }

    @Transactional
    public void deleteSavedNews(Long newsId, Long userId){
        savedNewsRepository.deleteByIdAndUserId(newsId, userId);
    }
}