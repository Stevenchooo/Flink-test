/*
 * 文 件 名:  ProfileServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.profile.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.beans.res.profile.Profile;
import com.huawei.ide.daos.res.profile.ProfileDao;
import com.huawei.ide.services.res.profile.ProfileService;

/**
 * 系统用户对象数据库服务实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Service(value = "com.huawei.ide.services.res.profile.impl.ProfileServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class ProfileServiceImpl implements ProfileService
{
    @Resource(name = "com.huawei.ide.daos.res.profile.impl.ProfileDaoImpl")
    private ProfileDao profileDao;
    
    /**
     * 新建用户
     * @param profile
     *        profile
     */
    @Override
    public void createProfile(Profile profile)
    {
        profileDao.createProfile(profile);
    }
    
    /**
     * 更新用户
     * @param id
     *        id
     * @param profile
     *        profile
     */
    @Override
    public void updateProfile(int id, Profile profile)
    {
        profileDao.updateProfile(id, profile);
    }
    
    /**
     * 删除用户
     * @param id
     *       id
     */
    @Override
    public void deleteProfile(int id)
    {
        profileDao.deleteProfile(id);
    }
    
    /**
     * 查询用户
     * @param id
     *        id
     * @return  Profile
     */
    @Override
    public Profile queryProfile(int id)
    {
        return profileDao.queryProfile(id);
    }
    
    /**
     * 查询所有用户
     * @return  List<Profile>
     */
    @Override
    public List<Profile> queryProfiles()
    {
        return profileDao.queryProfiles();
    }
    
    /**
     * 查询用户总数
     * @return  int
     */
    @Override
    public int queryProfileTotalNum()
    {
        return profileDao.queryProfileTotalNum();
    }
    
    /**
     * 分页查询用户
     * @param index
     *        index
     * @param pageSize
     *         pageSize
     * @return   List<Profile>
     */
    @Override
    public List<Profile> queryProfilesByPage(int index, int pageSize)
    {
        return profileDao.queryProfilesByPage(index, pageSize);
    }
    
}
