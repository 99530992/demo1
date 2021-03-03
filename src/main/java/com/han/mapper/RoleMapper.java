package com.han.mapper;

import com.han.dto.Result;
import com.han.model.Permission;
import com.han.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 1Han
 * @since 2021-02-04
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT p.* FROM t_role r " +
            "JOIN t_role_permission rp ON rp.rid = r.id " +
            "JOIN t_permission p on p.id = rp.pid " +
            "WHERE r.id = #{id}")
    public List<Permission> getPermissionsById(int id);

    @Delete("DELETE FROM t_role_permission where rid = #{rid}")
    void delById(int rid);

    @Insert("insert into t_role_permission(rid,pid) values(#{rid},#{pid})")
    void insertById(@Param("rid") int rid,@Param("pid") int pid);
}
