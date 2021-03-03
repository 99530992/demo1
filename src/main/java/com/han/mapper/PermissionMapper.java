package com.han.mapper;

import com.han.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 1Han
 * @since 2021-02-03
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT p.* " +
            "FROM t_user u " +
            "JOIN t_user_role ur on u.id = ur.uid " +
            "JOIN t_role_permission rp on ur.rid = rp.rid  " +
            "JOIN t_permission p on rp.pid = p.id " +
            "WHERE u.id = #{userId}")
    List<Permission> queryMenusByUid(int userId);

    @Select("SELECT r.rolename  " +
            "FROM t_user u  " +
            "JOIN t_user_role ur on u.id = ur.uid  " +
            "JOIN t_role r on r.id = ur.rid  " +
            "WHERE u.id = #{userId}")
    List<String> queryRolesByUid(int userId);

    @Select("SELECT p.* " +
            "FROM t_user u " +
            "JOIN t_user_role ur on u.id = ur.uid " +
            "JOIN t_role_permission rp on ur.rid = rp.rid  " +
            "JOIN t_permission p on rp.pid = p.id " +
            "WHERE u.id = #{userId} AND p.level = 3")
    List<Permission> queryThirdMenusByUid(int userId);

    @Select("SELECT rolename FROM t_role  ")
    List<String> queryRoles();

    @Delete("delete from t_user_role where uid = #{uid}")
    void deleteMsgByUid(Integer uid);

    @Insert("insert into t_user_role(uid,rid) values(#{uid},#{rid})")
    void addMsg(@Param("uid") Integer uid, @Param("rid") Integer rid);

    @Select("select id from t_role where rolename = #{rolename}")
    Integer queryIdByRoleName(String rolename);
}
