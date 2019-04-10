package xyz.stackoverflow.blog.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.stackoverflow.blog.common.BaseController;
import xyz.stackoverflow.blog.common.BaseDTO;
import xyz.stackoverflow.blog.common.Response;
import xyz.stackoverflow.blog.exception.BusinessException;
import xyz.stackoverflow.blog.pojo.dto.ArticleDTO;
import xyz.stackoverflow.blog.pojo.dto.CommentDTO;
import xyz.stackoverflow.blog.pojo.po.ArticlePO;
import xyz.stackoverflow.blog.pojo.po.CommentPO;
import xyz.stackoverflow.blog.service.ArticleService;
import xyz.stackoverflow.blog.service.CommentService;
import xyz.stackoverflow.blog.util.CollectionUtils;
import xyz.stackoverflow.blog.util.TransferUtils;
import xyz.stackoverflow.blog.util.ValidationUtils;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 评论与点赞接口Controller
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/api")
public class CommentAndLikeController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ValidatorFactory validatorFactory;

    /**
     * 评论接口 /api/comment
     * 方法 POST
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Response insertComment(@RequestBody BaseDTO dto) {
        Response response = new Response();

        List<CommentDTO> dtos = (List<CommentDTO>) (Object) getDTO("comment", CommentDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        CommentDTO commentDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO, CommentDTO.InsertGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        ArticlePO article = articleService.selectByUrl(commentDTO.getUrl());
        if (article == null) {
            throw new BusinessException("找不到该文章");
        }

        CommentPO comment = (CommentPO) TransferUtils.dto2po(CommentPO.class, commentDTO);
        comment.setDate(new Date());
        comment.setArticleId(article.getId());
        comment.setReview(0);
        commentService.insert(comment);

        response.setStatus(Response.SUCCESS);
        response.setMessage("评论成功");

        return response;
    }

    /**
     * 点赞接口 /api/like
     * 方法 POST
     *
     * @param dto
     * @param session
     * @return
     */
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Response like(@RequestBody BaseDTO dto, HttpSession session) {
        Response response = new Response();

        List<ArticleDTO> dtos = (List<ArticleDTO>) (Object) getDTO("article", ArticleDTO.class, dto);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new BusinessException("找不到请求数据");
        }
        ArticleDTO articleDTO = dtos.get(0);

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(articleDTO, ArticleDTO.LikeGroup.class);
        Map<String, String> map = ValidationUtils.errorMap(violations);

        if (!CollectionUtils.isEmpty(map)) {
            throw new BusinessException("字段格式错误", map);
        }

        Boolean isLike = (Boolean) session.getAttribute(articleDTO.getUrl());

        if (isLike != null && !isLike) {
            ArticlePO article = articleService.selectByUrl(articleDTO.getUrl());
            article.setLikes(article.getLikes() + 1);
            articleService.update(article);
            session.setAttribute(articleDTO.getUrl(), true);
            response.setStatus(Response.SUCCESS);
            response.setMessage("点赞成功");
            response.setData(article.getLikes());
        } else {
            throw new BusinessException("不能重复点赞或找不到该url对应文章");
        }

        return response;
    }
}
