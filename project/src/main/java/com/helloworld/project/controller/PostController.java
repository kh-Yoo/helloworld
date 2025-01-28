package com.helloworld.project.controller;

import com.helloworld.project.entity.Post;
import com.helloworld.project.entity.User;
import com.helloworld.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    // 게시판 목록 보기 (홈에서도 접근 가능)
    @GetMapping("/posts")
    public String posts(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        model.addAttribute("posts", postService.getPostsByUserId(user.getId()));
        return "posts";
    }

    // 게시글 작성 폼
    @GetMapping("/posts/create")
    public String createPostForm() {
        return "create-post";
    }

    // 게시글 작성 처리
    @PostMapping("/posts/create")
    public String createPost(@RequestParam String title, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        postService.createPost(post);
        return "redirect:/posts";
    }
}