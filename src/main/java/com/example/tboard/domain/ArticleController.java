package com.example.tboard.domain;

import com.example.tboard.base.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class ArticleController { // Model + Controller

    CommonUtil commonUtil = new CommonUtil();
    ArticleRepository articleRepository = new ArticleRepository();

    @RequestMapping("/search")
    @ResponseBody
    public ArrayList<Article> search(@RequestParam(value="keyword", defaultValue = "") String keyword) {

        ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);
        return searchedList;
    }

    @RequestMapping("/detail")
    public String detail(@RequestParam("articleId") int articleId,
                         Model model) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            throw new NotFoundException("article not found");
        }

        model.addAttribute("article", article);
        return "detail";

//        if (article == null) {
//            return "없는 게시물입니다.";
//        }
//
//        article.increaseHit();
//
//        String jsonString = "";
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            jsonString = objectMapper.writeValueAsString(article);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return jsonString;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("articleId") int articleId) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.deleteArticle(article);
        return "%d 게시물이 삭제되었습니다.".formatted(articleId);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(@RequestParam("articleId") int inputId,
                       @RequestParam("newTitle") String newTitle,
                       @RequestParam("newBody") String newBody
                       ) {

        Article article = articleRepository.findArticleById(inputId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.updateArticle(article, newTitle, newBody);
        return "%d번 게시물이 수정되었습니다.".formatted(inputId);
    }

    @RequestMapping("/list")
    public String list(Model model) {

        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList", articleList);

        return "list";
    }

    @RequestMapping("/create")
    public String create() {
        return "form";
    }

    @RequestMapping("/add")
    public String add(@RequestParam("title") String title,
                      @RequestParam("body") String body,
                      Model model) {

        articleRepository.saveArticle(title, body);
        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList", articleList);

        return "list";
    }
}
