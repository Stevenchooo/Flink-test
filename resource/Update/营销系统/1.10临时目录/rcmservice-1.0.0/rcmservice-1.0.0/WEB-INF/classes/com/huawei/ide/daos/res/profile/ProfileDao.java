/*
 * 文 件 名:  ProfileDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.profile;

import java.util.List;

import com.huawei.ide.beans.res.profile.Profile;

/**
 * 系统用户对象数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface ProfileDao
{
    /**
     * 创建系统用户对象
     * <功能详细描述>
     * @param profile
     *        profile
     * @see [类、类#方法、类#成员]
     */
    public void createProfile(Profile profile);
    
    /**
     * 更新系统用户对象
     * <功能详细描述>
     * @param id
     *        id
     * @param profile
     *        profile
     * @see [类、类#方法、类#成员]
     */
    public void updateProfile(int id, Profile profile);
    
    /**
     * 删除系统用户对象
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteProfile(int id);
    
    /**
     * 查询指定系统用户对象
     * <功能详细描述>
     * @param id
     *        id
     * @return  Profile
     * @see [类、类#方法、类#成员]
     */
    public Profile queryProfile(int id);
    
    /**
     * 查询所有系统用户对象
     * <功能详细描述>
     * @return  List<Profile>
     * @see [类、类#方法、类#成员]
     */
    public List<Profile> queryProfiles();
    
    /**
     * 查询profile对象总数
     * <功能详细描述>
     * @return  int
     * @see [类、类#方法、类#成员]
     */
    public int queryProfileTotalNum();
    
    /**
     * 分页查询指定profile对象
     * <功能详细描述>
     * @param index
     *        index
     * @param pageSize
     *        pageSize
     * @return   List<Profile> 
     * @see [类、类#方法、类#成员]
     */
    public List<Profile> queryProfilesByPage(int index, int pageSize);
}
