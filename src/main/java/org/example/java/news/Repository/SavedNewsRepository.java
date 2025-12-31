package org.example.java.news.Repository;

import jakarta.transaction.Transactional;
import org.example.java.news.Model.SavedNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface SavedNewsRepository extends JpaRepository<SavedNews,Long> {
    List<SavedNews> findByUserId(Long userId);
    boolean existsByUrlAndUserId(String url, Long userId);
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteByIdAndUserId(Long id, Long userId);
}
