package xyz.stackoverflow.blog.dao;

import org.springframework.stereotype.Repository;
import xyz.stackoverflow.blog.pojo.entity.UserRole;
import xyz.stackoverflow.blog.util.db.PageParameter;

import java.util.List;
import java.util.Map;

/**
 * 用户-角色表DAO
 *
 * @author 凉衫薄
 */
@Repository
public interface UserRoleDao {

    List<UserRole> selectByPage(PageParameter pageParameter);

    List<UserRole> selectByCondition(Map<String, String> searchMap);

    UserRole selectById(String id);

    int insert(UserRole userRole);

    int batchInsert(List<UserRole> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(UserRole userRole);

    int batchUpdate(List<UserRole> list);


    List<UserRole> getUserRoleByUserId(String userId);

    int insertUserRole(UserRole userRole);
}
