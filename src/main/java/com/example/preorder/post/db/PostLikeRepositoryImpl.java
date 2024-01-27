package com.example.preorder.post.db;

import com.example.preorder.post.core.entity.PostLikeEntity;
import com.example.preorder.post.core.repository.PostLikeRepository;
import com.example.preorder.post.core.repository.PostRepository;
import com.example.preorder.post.core.vo.PostLikeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {
    private final PostLikeJpaRepository postLikeJpaRepository;

    @Override
    public Optional<PostLikeEntity> findByUserIdAndPostId(Long userId, Long postId) {
        return postLikeJpaRepository.findByUserIdAndPostId(userId, postId);
    }

    @Override
    public PostLikeEntity save(PostLikeEntity postLikeEntity) {
        return postLikeJpaRepository.save(postLikeEntity);
    }

    @Override
    public List<PostLikeEntity> findByUserIds(List<Long> userIds, PostLikeStatus status) {
        return postLikeJpaRepository.findAllByUserIdInAndStatusOrderByCreatedAtDesc(
                userIds,
                status
        );
    }
}