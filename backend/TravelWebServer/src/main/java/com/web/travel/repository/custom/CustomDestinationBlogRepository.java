package com.web.travel.repository.custom;

import com.web.travel.model.DestinationBlog;
import com.web.travel.model.User;
import com.web.travel.repository.custom.enumeration.ESortType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomDestinationBlogRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<DestinationBlog> findTopLatestPosts(int top){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DestinationBlog> criteriaQuery = builder.createQuery(DestinationBlog.class);
        Root<DestinationBlog> root = criteriaQuery.from(DestinationBlog.class);

        criteriaQuery.select(root).orderBy(builder.desc(root.get("postDate")));
        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(top)
                .getResultList();
    }

    public Page<DestinationBlog> findAllDestinationBlogDiffLatest(Pageable pageable){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DestinationBlog> criteriaQuery = builder.createQuery(DestinationBlog.class);
        Root<DestinationBlog> root = criteriaQuery.from(DestinationBlog.class);

        criteriaQuery.select(root).orderBy(builder.desc(root.get("postDate")));

        List<DestinationBlog> result = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset() + 4)
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<DestinationBlog> blogRoot = countQuery.from(DestinationBlog.class);
        countQuery.select(builder.count(blogRoot));

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(result, pageable, count - 4);
    }

    public List<User> findTopAuthor(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        root.join("destinationBlogs", JoinType.LEFT);
        criteriaQuery.select(root)
                .groupBy(root.get("id"))
                .orderBy(builder.desc(builder.count(root)));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<DestinationBlog> getRelevantBlogs(DestinationBlog currentBlog, int maxResult){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DestinationBlog> criteriaQuery = builder.createQuery(DestinationBlog.class);
        Root<DestinationBlog> root = criteriaQuery.from(DestinationBlog.class);

        Predicate idNotEqual = builder.notEqual(root.get("id"), currentBlog.getId());
        Predicate titleLike = builder.like(builder.lower(root.get("title")), "%" + currentBlog.getTitle().toLowerCase() + "%");
        Predicate typeLike = builder.like(builder.lower(root.get("type")), "%" + currentBlog.getType().toLowerCase() + "%");
        Predicate titleOrTypeLike = builder.or(titleLike, typeLike);

        criteriaQuery.select(root).where(idNotEqual, titleOrTypeLike);

        List<DestinationBlog> result = entityManager.createQuery(criteriaQuery)
                .setMaxResults(maxResult)
                .getResultList();
        if(result.size() > 0){
            return result;
        }

        criteriaQuery.select(root).where(builder.like(root.get("type"), "%"));
        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(maxResult)
                .getResultList();
    }
}
