package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.PostLike;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.LikeRepository;
import com.example.BackEndSocial.repository.PostRepository;
import com.example.BackEndSocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImp implements LikeService{
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public PostLike toggleLike(User user, Post post) {
        Optional<PostLike> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            PostLike like = existingLike.get();
            boolean newStatus = !like.isLike(); // Đảo trạng thái
            like.setLike(newStatus);
            likeRepository.save(like);

            // Cập nhật totalLike
            int delta = newStatus ? 1 : -1;
            post.setTotalLike(Math.max(0, post.getTotalLike() + delta));
            postRepository.save(post);

            return newStatus ? like : null;
        } else {
            PostLike newLike = new PostLike();
            newLike.setLike(true);
            newLike.setUser(user);
            newLike.setPost(post);
            likeRepository.save(newLike);

            // +1 like
            post.setTotalLike(post.getTotalLike() + 1);
            postRepository.save(post);

            return newLike;
        }
    }

}
