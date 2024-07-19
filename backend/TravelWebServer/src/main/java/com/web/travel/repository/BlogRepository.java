package com.web.travel.repository;

import com.web.travel.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("DELETE FROM Blog p WHERE p.id = ?1")
    void deleteByBlogId(long id);
}
