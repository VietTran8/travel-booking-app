package com.web.travel.repository;

import com.web.travel.model.DestinationBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationBlogRepository extends JpaRepository<DestinationBlog, Long> {
    public Page<DestinationBlog> findAllByOrderByPostDateDesc(Pageable pageable);
    public Page<DestinationBlog> findByTitleContaining(String keyword, Pageable pageable);
    public List<DestinationBlog> findTop6ByOrderByViewsDesc();
    public List<DestinationBlog> findByTitleContaining(String keyword);
}
