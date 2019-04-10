package xyz.stackoverflow.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.stackoverflow.blog.common.Page;
import xyz.stackoverflow.blog.dao.ArticleDao;
import xyz.stackoverflow.blog.dao.CommentDao;
import xyz.stackoverflow.blog.pojo.po.ArticlePO;
import xyz.stackoverflow.blog.pojo.po.CommentPO;
import xyz.stackoverflow.blog.util.cache.RedisCacheUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章服务实现
 *
 * @author 凉衫薄
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private RedisCacheUtils redisCacheUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ArticlePO> selectByPage(Page page) {
        return articleDao.selectByPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ArticlePO> selectByCondition(Map<String, Object> searchMap) {
        return articleDao.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "article", key = "'article:'+#id", unless = "#result == null")
    public ArticlePO selectById(String id) {
        return articleDao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "article", key = "'article:'+#url", unless = "#result == null")
    public ArticlePO selectByUrl(String url) {
        return articleDao.selectByUrl(url);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "article", key = "'article:'+#result.url", condition = "#result!=null")
    public ArticlePO insert(ArticlePO article) {
        articleDao.insert(article);
        redisCacheUtils.set("article:" + article.getId(), article);
        return articleDao.selectById(article.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<ArticlePO> list) {
        for (ArticlePO article : list) {
            redisCacheUtils.set("article:" + article.getId(), article);
            redisCacheUtils.set("article:" + article.getUrl(), article);
        }
        return articleDao.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "article", key = "'article:'+#result.id", condition = "#result!=null", beforeInvocation = false)
    public ArticlePO deleteById(String id) {
        ArticlePO article = articleDao.selectById(id);

        List<CommentPO> commentList = commentDao.selectByCondition(new HashMap<String, Object>() {{
            put("articleId", id);
        }});

        if (commentList.size() != 0) {
            List<String> ids = new ArrayList<String>();
            for (CommentPO comment : commentList) {
                ids.add(comment.getId());
            }
            commentDao.batchDeleteById(ids);
        }

        articleDao.deleteById(id);
        redisCacheUtils.del("article:" + article.getUrl());
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteById(List<String> list) {
        for (String id : list) {
            List<CommentPO> commentList = commentDao.selectByCondition(new HashMap<String, Object>() {{
                put("articleId", id);
            }});

            if (commentList.size() != 0) {
                List<String> ids = new ArrayList<String>();
                for (CommentPO comment : commentList) {
                    ids.add(comment.getId());
                }
                commentDao.batchDeleteById(ids);
            }

            ArticlePO article = articleDao.selectById(id);
            redisCacheUtils.del("article:" + article.getUrl());
            redisCacheUtils.del("article:" + id);
        }
        return articleDao.batchDeleteById(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "article", key = "'article:'+#result.url", condition = "#result!=null")
    public ArticlePO update(ArticlePO article) {
        articleDao.update(article);
        redisCacheUtils.set("article:" + article.getId(), article);
        return articleDao.selectById(article.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<ArticlePO> list) {
        for (ArticlePO article : list) {
            redisCacheUtils.set("article:" + article.getId(), article);
            redisCacheUtils.set("article:" + article.getUrl(), article);
        }
        return articleDao.batchUpdate(list);
    }

}
