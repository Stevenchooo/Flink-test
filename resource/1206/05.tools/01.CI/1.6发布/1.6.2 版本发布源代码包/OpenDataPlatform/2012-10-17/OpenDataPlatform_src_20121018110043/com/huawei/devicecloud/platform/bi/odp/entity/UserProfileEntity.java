/*
 * 文 件 名:  UserProfileEntity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  用户信息实体类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.entity;


/**
 * 用户信息实体类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public class UserProfileEntity
{
    //IMEI号
    private String deviceId;
    
    //出生年月
    private String birthday;
    
    //性别
    private String gender;
    
    //注册地域城市
    private Long registerAreaId;
    
    //注册地域省份
    private Long registerParentAreaId;
    
    //注册时间
    private String registerTime;
    
    //账号类型
    private Integer accountType;
    
    //账号状态
    private Integer accountState;
    
    //上周登录次数
    private Integer loginsW;
    
    //上周搜索次数
    private Integer searchsW;
    
    //上周浏览次数
    private Integer viewsW;
    
    //上周下载次数
    private Integer downloadsW;
    
    //上周休闲游戏浏览次数
    private Integer casualGameViewsW;
    
    //上周角色扮演浏览次数
    private Integer roleplayingGameViewsW;
    
    //上周模拟游戏浏览次数
    private Integer simulationGameViewsW;
    
    //上周体育射击浏览次数
    private Integer shootingGameViewsW;
    
    //上周策略游戏浏览次数
    private Integer strategyGameViewsW;
    
    //上周益智棋牌浏览次数
    private Integer chesspuzzleGameViewsW;
    
    //上周动作冒险浏览次数
    private Integer adventureGameViewsW;
    
    //上周网络游戏浏览次数
    private Integer onlineGameViewsW;
    
    //上周娱乐软件浏览次数
    private Integer entertainmentSoftwareViewsW;
    
    //上周财经软件浏览次数
    private Integer financeSoftwareViewsW;
    
    //上周资讯软件浏览次数
    private Integer informationSoftwareViewsW;
    
    //上周生活软件浏览次数
    private Integer lifeSoftwareViewsW;
    
    //上周主题软件浏览次数
    private Integer themeSoftwareViewsW;
    
    //上周阅读软件浏览次数
    private Integer readSoftwareViewsW;
    
    //上周工具软件浏览次数
    private Integer toolsSoftwareViewsW;
    
    //上周社交软件浏览次数
    private Integer socialSoftwareViewsW;
    
    //上周系统软件浏览次数
    private Integer systemSoftwareViewsW;
    
    //上周出行软件浏览次数
    private Integer travelSoftwareViewsW;
    
    //上周办公软件浏览次数
    private Integer officeSoftwareViewsW;
    
    //上周休闲游戏下载次数
    private Integer casualGameDownloadsW;
    
    //上周角色扮演下载次数
    private Integer roleplayingGameDownloadsW;
    
    //上周模拟游戏下载次数
    private Integer simulationGameDownloadsW;
    
    //上周体育射击下载次数
    private Integer shootingGameDownloadsW;
    
    //上周策略游戏下载次数
    private Integer strategyGameDownloadsW;
    
    //上周益智棋牌下载次数
    private Integer chesspuzzleGameDownloadsW;
    
    //上周动作冒险下载次数
    private Integer adventureGameDownloadsW;
    
    //上周网络游戏下载次数
    private Integer onlineGameDownloadsW;
    
    //上周娱乐软件下载次数
    private Integer entertainmentSoftwareDownloadsW;
    
    //上周财经软件下载次数
    private Integer financeSoftwareDownloadsW;
    
    //上周资讯软件下载次数
    private Integer informationSoftwareDownloadsW;
    
    //上周生活软件下载次数
    private Integer lifeSoftwareDownloadsW;
    
    //上周主题软件下载次数
    private Integer themeSoftwareDownloadsW;
    
    //上周阅读软件下载次数
    private Integer readSoftwareDownloadsW;
    
    //上周工具软件下载次数
    private Integer toolsSoftwareDownloadsW;
    
    //上周社交软件下载次数
    private Integer socialSoftwareDownloadsW;
    
    //上周系统软件下载次数
    private Integer systemSoftwareDownloadsW;
    
    //上周出行软件下载次数
    private Integer travelSoftwareDownloadsW;
    
    //上周办公软件下载次数
    private Integer officeSoftwareDownloadsW;
    
    //上月登录次数
    private Integer loginsM;
    
    //上月搜索次数
    private Integer searchsM;
    
    //上月浏览次数
    private Integer viewsM;
    
    //上月下载次数
    private Integer downloadsM;
    
    //上月休闲游戏浏览次数
    private Integer casualGameViewsM;
    
    //上月角色扮演浏览次数
    private Integer roleplayingGameViewsM;
    
    //上月模拟游戏浏览次数
    private Integer simulationGameViewsM;
    
    //上月体育射击浏览次数
    private Integer shootingGameViewsM;
    
    //上月策略游戏浏览次数
    private Integer strategyGameViewsM;
    
    //上月益智棋牌浏览次数
    private Integer chesspuzzleGameViewsM;
    
    //上月动作冒险浏览次数
    private Integer adventureGameViewsM;
    
    //上月网络游戏浏览次数
    private Integer onlineGameViewsM;
    
    //上月娱乐软件浏览次数
    private Integer entertainmentSoftwareViewsM;
    
    //上月财经软件浏览次数
    private Integer financeSoftwareViewsM;
    
    //上月资讯软件浏览次数
    private Integer informationSoftwareViewsM;
    
    //上月生活软件浏览次数
    private Integer lifeSoftwareViewsM;
    
    //上月主题软件浏览次数
    private Integer themeSoftwareViewsM;
    
    //上月阅读软件浏览次数
    private Integer readSoftwareViewsM;
    
    //上月工具软件浏览次数
    private Integer toolsSoftwareViewsM;
    
    //上月社交软件浏览次数
    private Integer socialSoftwareViewsM;
    
    //上月系统软件浏览次数
    private Integer systemSoftwareViewsM;
    
    //上月出行软件浏览次数
    private Integer travelSoftwareViewsM;
    
    //上月办公软件浏览次数
    private Integer officeSoftwareViewsM;
    
    //上月休闲游戏下载次数
    private Integer casualGameDownloadsM;
    
    //上月角色扮演下载次数
    private Integer roleplayingGameDownloadsM;
    
    //上月模拟游戏下载次数
    private Integer simulationGameDownloadsM;
    
    //上月体育射击下载次数
    private Integer shootingGameDownloadsM;
    
    //上月策略游戏下载次数
    private Integer strategyGameDownloadsM;
    
    //上月益智棋牌下载次数
    private Integer chesspuzzleGameDownloadsM;
    
    //上月动作冒险下载次数
    private Integer adventureGameDownloadsM;
    
    //上月网络游戏下载次数
    private Integer onlineGameDownloadsM;
    
    //上月娱乐软件下载次数
    private Integer entertainmentSoftwareDownloadsM;
    
    //上月财经软件下载次数
    private Integer financeSoftwareDownloadsM;
    
    //上月资讯软件下载次数
    private Integer informationSoftwareDownloadsM;
    
    //上月生活软件下载次数
    private Integer lifeSoftwareDownloadsM;
    
    //上月主题软件下载次数
    private Integer themeSoftwareDownloadsM;
    
    //上月阅读软件下载次数
    private Integer readSoftwareDownloadsM;
    
    //上月工具软件下载次数
    private Integer toolsSoftwareDownloadsM;
    
    //上月社交软件下载次数
    private Integer socialSoftwareDownloadsM;
    
    //上月系统软件下载次数
    private Integer systemSoftwareDownloadsM;
    
    //上月出行软件下载次数
    private Integer travelSoftwareDownloadsM;
    
    //上月办公软件下载次数
    private Integer officeSoftwareDownloadsM;
    
    //历史累计登录次数
    private Integer loginsT;
    
    //历史累计搜索次数
    private Integer searchsT;
    
    //历史累计浏览次数
    private Integer viewsT;
    
    //历史累计下载次数
    private Integer downloadsT;
    
    //历史累计休闲游戏浏览次数
    private Integer casualGameViewsT;
    
    //历史累计角色扮演浏览次数
    private Integer roleplayingGameViewsT;
    
    //历史累计模拟游戏浏览次数
    private Integer simulationGameViewsT;
    
    //历史累计体育射击浏览次数
    private Integer shootingGameViewsT;
    
    //历史累计策略游戏浏览次数
    private Integer strategyGameViewsT;
    
    //历史累计益智棋牌浏览次数
    private Integer chesspuzzleGameViewsT;
    
    //历史累计动作冒险浏览次数
    private Integer adventureGameViewsT;
    
    //历史累计网络游戏浏览次数
    private Integer onlineGameViewsT;
    
    //历史累计娱乐软件浏览次数
    private Integer entertainmentSoftwareViewsT;
    
    //历史累计财经软件浏览次数
    private Integer financeSoftwareViewsT;
    
    //历史累计资讯软件浏览次数
    private Integer informationSoftwareViewsT;
    
    //历史累计生活软件浏览次数
    private Integer lifeSoftwareViewsT;
    
    //历史累计主题软件浏览次数
    private Integer themeSoftwareViewsT;
    
    //历史累计阅读软件浏览次数
    private Integer readSoftwareViewsT;
    
    //历史累计工具软件浏览次数
    private Integer toolsSoftwareViewsT;
    
    //历史累计社交软件浏览次数
    private Integer socialSoftwareViewsT;
    
    //历史累计系统软件浏览次数
    private Integer systemSoftwareViewsT;
    
    //历史累计出行软件浏览次数
    private Integer travelSoftwareViewsT;
    
    //历史累计办公软件浏览次数
    private Integer officeSoftwareViewsT;
    
    //历史累计休闲游戏下载次数
    private Integer casualGameDownloadsT;
    
    //历史累计角色扮演下载次数
    private Integer roleplayingGameDownloadsT;
    
    //历史累计模拟游戏下载次数
    private Integer simulationGameDownloadsT;
    
    //历史累计体育射击下载次数
    private Integer shootingGameDownloadsT;
    
    //历史累计策略游戏下载次数
    private Integer strategyGameDownloadsT;
    
    //历史累计益智棋牌下载次数
    private Integer chesspuzzleGameDownloadsT;
    
    //历史累计动作冒险下载次数
    private Integer adventureGameDownloadsT;
    
    //历史累计网络游戏下载次数
    private Integer onlineGameDownloadsT;
    
    //历史累计娱乐软件下载次数
    private Integer entertainmentSoftwareDownloadsT;
    
    //历史累计财经软件下载次数
    private Integer financeSoftwareDownloadsT;
    
    //历史累计资讯软件下载次数
    private Integer informationSoftwareDownloadsT;
    
    //历史累计生活软件下载次数
    private Integer lifeSoftwareDownloadsT;
    
    //历史累计主题软件下载次数
    private Integer themeSoftwareDownloadsT;
    
    //历史累计阅读软件下载次数
    private Integer readSoftwareDownloadsT;
    
    //历史累计工具软件下载次数
    private Integer toolsSoftwareDownloadsT;
    
    //历史累计社交软件下载次数
    private Integer socialSoftwareDownloadsT;
    
    //历史累计系统软件下载次数
    private Integer systemSoftwareDownloadsT;
    
    //历史累计出行软件下载次数
    private Integer travelSoftwareDownloadsT;
    
    //历史累计办公软件下载次数
    private Integer officeSoftwareDownloadsT;
    
    //手机型号
    private String terminalType;
    
    //操作系统
    private String terminalOsVersion;
    
    //屏幕分辨率
    private String screenResolution;
    
    //运营商
    private String mobileOper;
    
    //智汇云版本号
    private String serviceClientVersion;
    
    //push推送标志位
    private Integer pushFlag;
    
    //用户标识
    private Integer userAdFlag;
    
    //push版本
    private String pushVersion;
    
    //是否能发送广告标识
    private Integer adTypeFlag;
    
    /**
     * 获取IMEI号
     * @return IMEI号
     */
    public String getDeviceId()
    {
        return deviceId;
    }
    
    /**
     * 获取出生年月
     * @return 出生年月
     */
    public String getBirthday()
    {
        return birthday;
    }
    
    /**
     * 获取性别
     * @return 性别
     */
    public String getGender()
    {
        return gender;
    }
    
    /**
     * 获取注册地域城市
     * @return 注册地域城市
     */
    public Long getRegisterAreaId()
    {
        return registerAreaId;
    }
    
    /**
     * 获取注册地域省份
     * @return 注册地域省份
     */
    public Long getRegisterParentAreaId()
    {
        return registerParentAreaId;
    }
    
    /**
     * 获取注册时间
     * @return 注册时间
     */
    public String getRegisterTime()
    {
        return registerTime;
    }
    
    /**
     * 获取账号类型
     * @return 账号类型
     */
    public Integer getAccountType()
    {
        return accountType;
    }
    
    /**
     * 获取账号状态
     * @return 账号状态
     */
    public Integer getAccountState()
    {
        return accountState;
    }
    
    /**
     * 获取上周登录次数
     * @return 上周登录次数
     */
    public Integer getLoginsW()
    {
        return loginsW;
    }
    
    /**
     * 获取上周搜索次数
     * @return 上周搜索次数
     */
    public Integer getSearchsW()
    {
        return searchsW;
    }
    
    /**
     * 获取上周浏览次数
     * @return 上周浏览次数
     */
    public Integer getViewsW()
    {
        return viewsW;
    }
    
    /**
     * 获取上周下载次数
     * @return 上周下载次数
     */
    public Integer getDownloadsW()
    {
        return downloadsW;
    }
    
    /**
     * 获取上周休闲游戏浏览次数
     * @return 上周休闲游戏浏览次数
     */
    public Integer getCasualGameViewsW()
    {
        return casualGameViewsW;
    }
    
    /**
     * 获取上周角色扮演浏览次数
     * @return 上周角色扮演浏览次数
     */
    public Integer getRoleplayingGameViewsW()
    {
        return roleplayingGameViewsW;
    }
    
    /**
     * 获取上周模拟游戏浏览次数
     * @return 上周模拟游戏浏览次数
     */
    public Integer getSimulationGameViewsW()
    {
        return simulationGameViewsW;
    }
    
    /**
     * 获取上周体育射击浏览次数
     * @return 上周体育射击浏览次数
     */
    public Integer getShootingGameViewsW()
    {
        return shootingGameViewsW;
    }
    
    /**
     * 获取上周策略游戏浏览次数
     * @return 上周策略游戏浏览次数
     */
    public Integer getStrategyGameViewsW()
    {
        return strategyGameViewsW;
    }
    
    /**
     * 获取上周益智棋牌浏览次数
     * @return 上周益智棋牌浏览次数
     */
    public Integer getChesspuzzleGameViewsW()
    {
        return chesspuzzleGameViewsW;
    }
    
    /**
     * 获取上周动作冒险浏览次数
     * @return 上周动作冒险浏览次数
     */
    public Integer getAdventureGameViewsW()
    {
        return adventureGameViewsW;
    }
    
    /**
     * 获取上周网络游戏浏览次数
     * @return 上周网络游戏浏览次数
     */
    public Integer getOnlineGameViewsW()
    {
        return onlineGameViewsW;
    }
    
    /**
     * 获取上周娱乐软件浏览次数
     * @return 上周娱乐软件浏览次数
     */
    public Integer getEntertainmentSoftwareViewsW()
    {
        return entertainmentSoftwareViewsW;
    }
    
    /**
     * 获取上周财经软件浏览次数
     * @return 上周财经软件浏览次数
     */
    public Integer getFinanceSoftwareViewsW()
    {
        return financeSoftwareViewsW;
    }
    
    /**
     * 获取上周资讯软件浏览次数
     * @return 上周资讯软件浏览次数
     */
    public Integer getInformationSoftwareViewsW()
    {
        return informationSoftwareViewsW;
    }
    
    /**
     * 获取上周生活软件浏览次数
     * @return 上周生活软件浏览次数
     */
    public Integer getLifeSoftwareViewsW()
    {
        return lifeSoftwareViewsW;
    }
    
    /**
     * 获取上周主题软件浏览次数
     * @return 上周主题软件浏览次数
     */
    public Integer getThemeSoftwareViewsW()
    {
        return themeSoftwareViewsW;
    }
    
    /**
     * 获取上周阅读软件浏览次数
     * @return 上周阅读软件浏览次数
     */
    public Integer getReadSoftwareViewsW()
    {
        return readSoftwareViewsW;
    }
    
    /**
     * 获取上周工具软件浏览次数
     * @return 上周工具软件浏览次数
     */
    public Integer getToolsSoftwareViewsW()
    {
        return toolsSoftwareViewsW;
    }
    
    /**
     * 获取上周社交软件浏览次数
     * @return 上周社交软件浏览次数
     */
    public Integer getSocialSoftwareViewsW()
    {
        return socialSoftwareViewsW;
    }
    
    /**
     * 获取上周系统软件浏览次数
     * @return 上周系统软件浏览次数
     */
    public Integer getSystemSoftwareViewsW()
    {
        return systemSoftwareViewsW;
    }
    
    /**
     * 获取上周出行软件浏览次数
     * @return 上周出行软件浏览次数
     */
    public Integer getTravelSoftwareViewsW()
    {
        return travelSoftwareViewsW;
    }
    
    /**
     * 获取上周办公软件浏览次数
     * @return 上周办公软件浏览次数
     */
    public Integer getOfficeSoftwareViewsW()
    {
        return officeSoftwareViewsW;
    }
    
    /**
     * 获取上周休闲游戏下载次数
     * @return 上周休闲游戏下载次数
     */
    public Integer getCasualGameDownloadsW()
    {
        return casualGameDownloadsW;
    }
    
    /**
     * 获取上周角色扮演下载次数
     * @return 上周角色扮演下载次数
     */
    public Integer getRoleplayingGameDownloadsW()
    {
        return roleplayingGameDownloadsW;
    }
    
    /**
     * 获取上周模拟游戏下载次数 
     * @return 上周模拟游戏下载次数 
     */
    public Integer getSimulationGameDownloadsW()
    {
        return simulationGameDownloadsW;
    }
    
    /**
     * 获取上周体育射击下载次数
     * @return 上周体育射击下载次数
     */
    public Integer getShootingGameDownloadsW()
    {
        return shootingGameDownloadsW;
    }
    
    /**
     * 获取上周策略游戏下载次数
     * @return 上周策略游戏下载次数
     */
    public Integer getStrategyGameDownloadsW()
    {
        return strategyGameDownloadsW;
    }
    
    /**
     * 获取上周益智棋牌下载次数
     * @return 上周益智棋牌下载次数
     */
    public Integer getChesspuzzleGameDownloadsW()
    {
        return chesspuzzleGameDownloadsW;
    }
    
    /**
     * 获取上周动作冒险下载次数
     * @return 上周动作冒险下载次数
     */
    public Integer getAdventureGameDownloadsW()
    {
        return adventureGameDownloadsW;
    }
    
    /**
     * 获取上周网络游戏下载次数
     * @return 上周网络游戏下载次数
     */
    public Integer getOnlineGameDownloadsW()
    {
        return onlineGameDownloadsW;
    }
    
    /**
     * 获取上周娱乐软件下载次数 
     * @return 上周娱乐软件下载次数 
     */
    public Integer getEntertainmentSoftwareDownloadsW()
    {
        return entertainmentSoftwareDownloadsW;
    }
    
    /**
     * 获取上周财经软件下载次数
     * @return 上周财经软件下载次数
     */
    public Integer getFinanceSoftwareDownloadsW()
    {
        return financeSoftwareDownloadsW;
    }
    
    /**
     * 获取上周资讯软件下载次数
     * @return 上周资讯软件下载次数
     */
    public Integer getInformationSoftwareDownloadsW()
    {
        return informationSoftwareDownloadsW;
    }
    
    /**
     * 获取上周生活软件下载次数
     * @return 上周生活软件下载次数
     */
    public Integer getLifeSoftwareDownloadsW()
    {
        return lifeSoftwareDownloadsW;
    }
    
    /**
     * 获取上周主题软件下载次数
     * @return 上周主题软件下载次数
     */
    public Integer getThemeSoftwareDownloadsW()
    {
        return themeSoftwareDownloadsW;
    }
    
    /**
     * 获取上周阅读软件下载次数
     * @return 上周阅读软件下载次数
     */
    public Integer getReadSoftwareDownloadsW()
    {
        return readSoftwareDownloadsW;
    }
    
    /**
     * 获取上周工具软件下载次数
     * @return 上周工具软件下载次数
     */
    public Integer getToolsSoftwareDownloadsW()
    {
        return toolsSoftwareDownloadsW;
    }
    
    /**
     * 获取上周社交软件下载次数
     * @return 上周社交软件下载次数
     */
    public Integer getSocialSoftwareDownloadsW()
    {
        return socialSoftwareDownloadsW;
    }
    
    /**
     * 获取上周系统软件下载次数
     * @return 上周系统软件下载次数
     */
    public Integer getSystemSoftwareDownloadsW()
    {
        return systemSoftwareDownloadsW;
    }
    
    /**
     * 获取上周出行软件下载次数
     * @return 上周出行软件下载次数
     */
    public Integer getTravelSoftwareDownloadsW()
    {
        return travelSoftwareDownloadsW;
    }
    
    /**
     * 获取上周办公软件下载次数
     * @return 上周办公软件下载次数
     */
    public Integer getOfficeSoftwareDownloadsW()
    {
        return officeSoftwareDownloadsW;
    }
    
    /**
     * 获取上月登录次数
     * @return 上月登录次数
     */
    public Integer getLoginsM()
    {
        return loginsM;
    }
    
    /**
     * 获取上月搜索次数
     * @return 上月搜索次数
     */
    public Integer getSearchsM()
    {
        return searchsM;
    }
    
    /**
     * 获取上月浏览次数
     * @return 上月浏览次数
     */
    public Integer getViewsM()
    {
        return viewsM;
    }
    
    /**
     * 获取上月下载次数
     * @return 上月下载次数
     */
    public Integer getDownloadsM()
    {
        return downloadsM;
    }
    
    /**
     * 获取上月休闲游戏浏览次数
     * @return 上月休闲游戏浏览次数
     */
    public Integer getCasualGameViewsM()
    {
        return casualGameViewsM;
    }
    
    /**
     * 获取上月角色扮演浏览次数
     * @return 上月角色扮演浏览次数
     */
    public Integer getRoleplayingGameViewsM()
    {
        return roleplayingGameViewsM;
    }
    
    /**
     * 获取上月模拟游戏浏览次数
     * @return 上月模拟游戏浏览次数
     */
    public Integer getSimulationGameViewsM()
    {
        return simulationGameViewsM;
    }
    
    /**
     * 获取上月体育射击浏览次数
     * @return 上月体育射击浏览次数
     */
    public Integer getShootingGameViewsM()
    {
        return shootingGameViewsM;
    }
    
    /**
     * 获取上月策略游戏浏览次数
     * @return 上月策略游戏浏览次数
     */
    public Integer getStrategyGameViewsM()
    {
        return strategyGameViewsM;
    }
    
    /**
     * 获取上月益智棋牌浏览次数
     * @return 上月益智棋牌浏览次数
     */
    public Integer getChesspuzzleGameViewsM()
    {
        return chesspuzzleGameViewsM;
    }
    
    /**
     * 获取上月动作冒险浏览次数
     * @return 上月动作冒险浏览次数
     */
    public Integer getAdventureGameViewsM()
    {
        return adventureGameViewsM;
    }
    
    /**
     * 获取上月网络游戏浏览次数
     * @return 上月网络游戏浏览次数
     */
    public Integer getOnlineGameViewsM()
    {
        return onlineGameViewsM;
    }
    
    /**
     * 获取上月娱乐软件浏览次数
     * @return 上月娱乐软件浏览次数
     */
    public Integer getEntertainmentSoftwareViewsM()
    {
        return entertainmentSoftwareViewsM;
    }
    
    /**
     * 获取上月财经软件浏览次数
     * @return 上月财经软件浏览次数
     */
    public Integer getFinanceSoftwareViewsM()
    {
        return financeSoftwareViewsM;
    }
    
    /**
     * 获取上月资讯软件浏览次数
     * @return 上月资讯软件浏览次数
     */
    public Integer getInformationSoftwareViewsM()
    {
        return informationSoftwareViewsM;
    }
    
    /**
     * 获取上月生活软件浏览次数
     * @return 上月生活软件浏览次数
     */
    public Integer getLifeSoftwareViewsM()
    {
        return lifeSoftwareViewsM;
    }
    
    /**
     * 获取上月主题软件浏览次数
     * @return 上月主题软件浏览次数
     */
    public Integer getThemeSoftwareViewsM()
    {
        return themeSoftwareViewsM;
    }
    
    /**
     * 获取上月阅读软件浏览次数
     * @return 上月阅读软件浏览次数
     */
    public Integer getReadSoftwareViewsM()
    {
        return readSoftwareViewsM;
    }
    
    /**
     * 获取上月工具软件浏览次数
     * @return 上月工具软件浏览次数
     */
    public Integer getToolsSoftwareViewsM()
    {
        return toolsSoftwareViewsM;
    }
    
    /**
     * 获取上月社交软件浏览次数
     * @return 上月社交软件浏览次数
     */
    public Integer getSocialSoftwareViewsM()
    {
        return socialSoftwareViewsM;
    }
    
    /**
     * 获取上月系统软件浏览次数
     * @return 上月系统软件浏览次数
     */
    public Integer getSystemSoftwareViewsM()
    {
        return systemSoftwareViewsM;
    }
    
    /**
     * 获取上月出行软件浏览次数
     * @return 上月出行软件浏览次数
     */
    public Integer getTravelSoftwareViewsM()
    {
        return travelSoftwareViewsM;
    }
    
    /**
     * 获取上月办公软件浏览次数
     * @return 上月办公软件浏览次数
     */
    public Integer getOfficeSoftwareViewsM()
    {
        return officeSoftwareViewsM;
    }
    
    /**
     * 获取上月休闲游戏下载次数
     * @return 上月休闲游戏下载次数
     */
    public Integer getCasualGameDownloadsM()
    {
        return casualGameDownloadsM;
    }
    
    /**
     * 获取上月角色扮演下载次数
     * @return 上月角色扮演下载次数
     */
    public Integer getRoleplayingGameDownloadsM()
    {
        return roleplayingGameDownloadsM;
    }
    
    /**
     * 获取上月模拟游戏下载次数 
     * @return 上月模拟游戏下载次数 
     */
    public Integer getSimulationGameDownloadsM()
    {
        return simulationGameDownloadsM;
    }
    
    /**
     * 获取上月体育射击下载次数
     * @return 上月体育射击下载次数
     */
    public Integer getShootingGameDownloadsM()
    {
        return shootingGameDownloadsM;
    }
    
    /**
     * 获取上月策略游戏下载次数
     * @return 上月策略游戏下载次数
     */
    public Integer getStrategyGameDownloadsM()
    {
        return strategyGameDownloadsM;
    }
    
    /**
     * 获取上月益智棋牌下载次数
     * @return 上月益智棋牌下载次数
     */
    public Integer getChesspuzzleGameDownloadsM()
    {
        return chesspuzzleGameDownloadsM;
    }
    
    /**
     * 获取上月动作冒险下载次数
     * @return 上月动作冒险下载次数
     */
    public Integer getAdventureGameDownloadsM()
    {
        return adventureGameDownloadsM;
    }
    
    /**
     * 获取上月网络游戏下载次数
     * @return 上月网络游戏下载次数
     */
    public Integer getOnlineGameDownloadsM()
    {
        return onlineGameDownloadsM;
    }
    
    /**
     * 获取上月娱乐软件下载次数 
     * @return 上月娱乐软件下载次数 
     */
    public Integer getEntertainmentSoftwareDownloadsM()
    {
        return entertainmentSoftwareDownloadsM;
    }
    
    /**
     * 获取上月财经软件下载次数
     * @return 上月财经软件下载次数
     */
    public Integer getFinanceSoftwareDownloadsM()
    {
        return financeSoftwareDownloadsM;
    }
    
    /**
     * 获取上月资讯软件下载次数
     * @return 上月资讯软件下载次数
     */
    public Integer getInformationSoftwareDownloadsM()
    {
        return informationSoftwareDownloadsM;
    }
    
    /**
     * 获取上月生活软件下载次数
     * @return 上月生活软件下载次数
     */
    public Integer getLifeSoftwareDownloadsM()
    {
        return lifeSoftwareDownloadsM;
    }
    
    /**
     * 获取上月主题软件下载次数
     * @return 上月主题软件下载次数
     */
    public Integer getThemeSoftwareDownloadsM()
    {
        return themeSoftwareDownloadsM;
    }
    
    /**
     * 获取上月阅读软件下载次数
     * @return 上月阅读软件下载次数
     */
    public Integer getReadSoftwareDownloadsM()
    {
        return readSoftwareDownloadsM;
    }
    
    /**
     * 获取上月工具软件下载次数
     * @return 上月工具软件下载次数
     */
    public Integer getToolsSoftwareDownloadsM()
    {
        return toolsSoftwareDownloadsM;
    }
    
    /**
     * 获取上月社交软件下载次数
     * @return 上月社交软件下载次数
     */
    public Integer getSocialSoftwareDownloadsM()
    {
        return socialSoftwareDownloadsM;
    }
    
    /**
     * 获取上月系统软件下载次数
     * @return 上月系统软件下载次数
     */
    public Integer getSystemSoftwareDownloadsM()
    {
        return systemSoftwareDownloadsM;
    }
    
    /**
     * 获取上月出行软件下载次数
     * @return 上月出行软件下载次数
     */
    public Integer getTravelSoftwareDownloadsM()
    {
        return travelSoftwareDownloadsM;
    }
    
    /**
     * 获取上月办公软件下载次数
     * @return 上月办公软件下载次数
     */
    public Integer getOfficeSoftwareDownloadsM()
    {
        return officeSoftwareDownloadsM;
    }
    
    /**
     * 获取历史累计登录次数
     * @return 历史累计登录次数
     */
    public Integer getLoginsT()
    {
        return loginsT;
    }
    
    /**
     * 获取历史累计搜索次数
     * @return 历史累计搜索次数
     */
    public Integer getSearchsT()
    {
        return searchsT;
    }
    
    /**
     * 获取历史累计浏览次数
     * @return 历史累计浏览次数
     */
    public Integer getViewsT()
    {
        return viewsT;
    }
    
    /**
     * 获取历史累计下载次数
     * @return 历史累计下载次数
     */
    public Integer getDownloadsT()
    {
        return downloadsT;
    }
    
    /**
     * 获取历史累计休闲游戏浏览次数
     * @return 历史累计休闲游戏浏览次数
     */
    public Integer getCasualGameViewsT()
    {
        return casualGameViewsT;
    }
    
    /**
     * 获取历史累计角色扮演浏览次数
     * @return 历史累计角色扮演浏览次数
     */
    public Integer getRoleplayingGameViewsT()
    {
        return roleplayingGameViewsT;
    }
    
    /**
     * 获取历史累计模拟游戏浏览次数
     * @return 历史累计模拟游戏浏览次数
     */
    public Integer getSimulationGameViewsT()
    {
        return simulationGameViewsT;
    }
    
    /**
     * 获取历史累计体育射击浏览次数
     * @return 历史累计体育射击浏览次数
     */
    public Integer getShootingGameViewsT()
    {
        return shootingGameViewsT;
    }
    
    /**
     * 获取历史累计策略游戏浏览次数
     * @return 历史累计策略游戏浏览次数
     */
    public Integer getStrategyGameViewsT()
    {
        return strategyGameViewsT;
    }
    
    /**
     * 获取历史累计益智棋牌浏览次数
     * @return 历史累计益智棋牌浏览次数
     */
    public Integer getChesspuzzleGameViewsT()
    {
        return chesspuzzleGameViewsT;
    }
    
    /**
     * 获取历史累计动作冒险浏览次数
     * @return 历史累计动作冒险浏览次数
     */
    public Integer getAdventureGameViewsT()
    {
        return adventureGameViewsT;
    }
    
    /**
     * 获取历史累计网络游戏浏览次数
     * @return 历史累计网络游戏浏览次数
     */
    public Integer getOnlineGameViewsT()
    {
        return onlineGameViewsT;
    }
    
    /**
     * 获取历史累计娱乐软件浏览次数
     * @return 历史累计娱乐软件浏览次数
     */
    public Integer getEntertainmentSoftwareViewsT()
    {
        return entertainmentSoftwareViewsT;
    }
    
    /**
     * 获取历史累计财经软件浏览次数
     * @return 历史累计财经软件浏览次数
     */
    public Integer getFinanceSoftwareViewsT()
    {
        return financeSoftwareViewsT;
    }
    
    /**
     * 获取历史累计资讯软件浏览次数
     * @return 历史累计资讯软件浏览次数
     */
    public Integer getInformationSoftwareViewsT()
    {
        return informationSoftwareViewsT;
    }
    
    /**
     * 获取历史累计生活软件浏览次数
     * @return 历史累计生活软件浏览次数
     */
    public Integer getLifeSoftwareViewsT()
    {
        return lifeSoftwareViewsT;
    }
    
    /**
     * 获取历史累计主题软件浏览次数
     * @return 历史累计主题软件浏览次数
     */
    public Integer getThemeSoftwareViewsT()
    {
        return themeSoftwareViewsT;
    }
    
    /**
     * 获取历史累计阅读软件浏览次数
     * @return 历史累计阅读软件浏览次数
     */
    public Integer getReadSoftwareViewsT()
    {
        return readSoftwareViewsT;
    }
    
    /**
     * 获取历史累计工具软件浏览次数
     * @return 历史累计工具软件浏览次数
     */
    public Integer getToolsSoftwareViewsT()
    {
        return toolsSoftwareViewsT;
    }
    
    /**
     * 获取历史累计社交软件浏览次数
     * @return 历史累计社交软件浏览次数
     */
    public Integer getSocialSoftwareViewsT()
    {
        return socialSoftwareViewsT;
    }
    
    /**
     * 获取历史累计系统软件浏览次数
     * @return 历史累计系统软件浏览次数
     */
    public Integer getSystemSoftwareViewsT()
    {
        return systemSoftwareViewsT;
    }
    
    /**
     * 获取历史累计出行软件浏览次数
     * @return 历史累计出行软件浏览次数
     */
    public Integer getTravelSoftwareViewsT()
    {
        return travelSoftwareViewsT;
    }
    
    /**
     * 获取历史累计办公软件浏览次数
     * @return 历史累计办公软件浏览次数
     */
    public Integer getOfficeSoftwareViewsT()
    {
        return officeSoftwareViewsT;
    }
    
    /**
     * 获取历史累计休闲游戏下载次数
     * @return 历史累计休闲游戏下载次数
     */
    public Integer getCasualGameDownloadsT()
    {
        return casualGameDownloadsT;
    }
    
    /**
     * 获取历史累计角色扮演下载次数
     * @return 历史累计角色扮演下载次数
     */
    public Integer getRoleplayingGameDownloadsT()
    {
        return roleplayingGameDownloadsT;
    }
    
    /**
     * 获取历史累计模拟游戏下载次数
     * @return 历史累计模拟游戏下载次数
     */
    public Integer getSimulationGameDownloadsT()
    {
        return simulationGameDownloadsT;
    }
    
    /**
     * 获取历史累计体育射击下载次数
     * @return 历史累计体育射击下载次数
     */
    public Integer getShootingGameDownloadsT()
    {
        return shootingGameDownloadsT;
    }
    
    /**
     * 获取历史累计策略游戏下载次数
     * @return 历史累计策略游戏下载次数
     */
    public Integer getStrategyGameDownloadsT()
    {
        return strategyGameDownloadsT;
    }
    
    /**
     * 获取历史累计益智棋牌下载次数
     * @return 历史累计益智棋牌下载次数
     */
    public Integer getChesspuzzleGameDownloadsT()
    {
        return chesspuzzleGameDownloadsT;
    }
    
    /**
     * 获取历史累计动作冒险下载次数
     * @return 历史累计动作冒险下载次数
     */
    public Integer getAdventureGameDownloadsT()
    {
        return adventureGameDownloadsT;
    }
    
    /**
     * 获取历史累计网络游戏下载次数
     * @return 历史累计网络游戏下载次数
     */
    public Integer getOnlineGameDownloadsT()
    {
        return onlineGameDownloadsT;
    }
    
    /**
     * 获取历史累计娱乐软件下载次数
     * @return 历史累计娱乐软件下载次数
     */
    public Integer getEntertainmentSoftwareDownloadsT()
    {
        return entertainmentSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计财经软件下载次数
     * @return 历史累计财经软件下载次数
     */
    public Integer getFinanceSoftwareDownloadsT()
    {
        return financeSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计资讯软件下载次数
     * @return 历史累计资讯软件下载次数
     */
    public Integer getInformationSoftwareDownloadsT()
    {
        return informationSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计生活软件下载次数
     * @return 历史累计生活软件下载次数
     */
    public Integer getLifeSoftwareDownloadsT()
    {
        return lifeSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计主题软件下载次数
     * @return 历史累计主题软件下载次数
     */
    public Integer getThemeSoftwareDownloadsT()
    {
        return themeSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计阅读软件下载次数
     * @return 历史累计阅读软件下载次数
     */
    public Integer getReadSoftwareDownloadsT()
    {
        return readSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计工具软件下载次数
     * @return 历史累计工具软件下载次数
     */
    public Integer getToolsSoftwareDownloadsT()
    {
        return toolsSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计社交软件下载次数
     * @return 历史累计社交软件下载次数
     */
    public Integer getSocialSoftwareDownloadsT()
    {
        return socialSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计系统软件下载次数
     * @return 历史累计系统软件下载次数
     */
    public Integer getSystemSoftwareDownloadsT()
    {
        return systemSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计出行软件下载次数
     * @return 历史累计出行软件下载次数
     */
    public Integer getTravelSoftwareDownloadsT()
    {
        return travelSoftwareDownloadsT;
    }
    
    /**
     * 获取历史累计办公软件下载次数
     * @return 历史累计办公软件下载次数
     */
    public Integer getOfficeSoftwareDownloadsT()
    {
        return officeSoftwareDownloadsT;
    }
    
    /**
     * 获取手机型号
     * @return 手机型号
     */
    public String getTerminalType()
    {
        return terminalType;
    }
    
    /**
     * 获取操作系统
     * @return 操作系统
     */
    public String getTerminalOsVersion()
    {
        return terminalOsVersion;
    }
    
    /**
     * 获取屏幕分辨率
     * @return 屏幕分辨率
     */
    public String getScreenResolution()
    {
        return screenResolution;
    }
    
    /**
     * 获取运营商
     * @return 运营商
     */
    public String getMobileOper()
    {
        return mobileOper;
    }
    
    /**
     * 获取智汇云版本号
     * @return 智汇云版本号
     */
    public String getServiceClientVersion()
    {
        return serviceClientVersion;
    }
    
    /**
     * 获取push推送标志位
     * @return push推送标志位
     */
    public Integer getPushFlag()
    {
        return pushFlag;
    }
    
    /**
    * 设置IMEI号
    * @param deviceId IMEI号
    */
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }
    
    /**
    * 设置出生年月
    * @param birthday 出生年月
    */
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }
    
    /**
    * 设置性别
    * @param gender 性别
    */
    public void setGender(String gender)
    {
        this.gender = gender;
    }
    
    /**
    * 设置注册地域城市
    * @param registerAreaId 注册地域城市
    */
    public void setRegisterAreaId(Long registerAreaId)
    {
        this.registerAreaId = registerAreaId;
    }
    
    /**
    * 设置注册地域省份
    * @param registerParentAreaId 注册地域省份
    */
    public void setRegisterParentAreaId(Long registerParentAreaId)
    {
        this.registerParentAreaId = registerParentAreaId;
    }
    
    /**
    * 设置注册时间
    * @param registerTime 注册时间
    */
    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
    }
    
    /**
    * 设置账号类型
    * @param accountType 账号类型
    */
    public void setAccountType(Integer accountType)
    {
        this.accountType = accountType;
    }
    
    /**
    * 设置账号状态
    * @param accountState 账号状态
    */
    public void setAccountState(Integer accountState)
    {
        this.accountState = accountState;
    }
    
    /**
    * 设置上周登录次数
    * @param loginsW 上周登录次数
    */
    public void setLoginsW(Integer loginsW)
    {
        this.loginsW = loginsW;
    }
    
    /**
    * 设置上周搜索次数
    * @param searchsW 上周搜索次数
    */
    public void setSearchsW(Integer searchsW)
    {
        this.searchsW = searchsW;
    }
    
    /**
    * 设置上周浏览次数
    * @param viewsW 上周浏览次数
    */
    public void setViewsW(Integer viewsW)
    {
        this.viewsW = viewsW;
    }
    
    /**
    * 设置上周下载次数
    * @param downloadsW 上周下载次数
    */
    public void setDownloadsW(Integer downloadsW)
    {
        this.downloadsW = downloadsW;
    }
    
    /**
    * 设置上周休闲游戏浏览次数
    * @param casualGameViewsW 上周休闲游戏浏览次数
    */
    public void setCasualGameViewsW(Integer casualGameViewsW)
    {
        this.casualGameViewsW = casualGameViewsW;
    }
    
    /**
    * 设置上周角色扮演浏览次数
    * @param roleplayingGameViewsW 上周角色扮演浏览次数
    */
    public void setRoleplayingGameViewsW(Integer roleplayingGameViewsW)
    {
        this.roleplayingGameViewsW = roleplayingGameViewsW;
    }
    
    /**
    * 设置上周模拟游戏浏览次数
    * @param simulationGameViewsW 上周模拟游戏浏览次数
    */
    public void setSimulationGameViewsW(Integer simulationGameViewsW)
    {
        this.simulationGameViewsW = simulationGameViewsW;
    }
    
    /**
    * 设置上周体育射击浏览次数
    * @param shootingGameViewsW 上周体育射击浏览次数
    */
    public void setShootingGameViewsW(Integer shootingGameViewsW)
    {
        this.shootingGameViewsW = shootingGameViewsW;
    }
    
    /**
    * 设置上周策略游戏浏览次数
    * @param strategyGameViewsW 上周策略游戏浏览次数
    */
    public void setStrategyGameViewsW(Integer strategyGameViewsW)
    {
        this.strategyGameViewsW = strategyGameViewsW;
    }
    
    /**
    * 设置上周益智棋牌浏览次数
    * @param chesspuzzleGameViewsW 上周益智棋牌浏览次数
    */
    public void setChesspuzzleGameViewsW(Integer chesspuzzleGameViewsW)
    {
        this.chesspuzzleGameViewsW = chesspuzzleGameViewsW;
    }
    
    /**
    * 设置上周动作冒险浏览次数
    * @param adventureGameViewsW 上周动作冒险浏览次数
    */
    public void setAdventureGameViewsW(Integer adventureGameViewsW)
    {
        this.adventureGameViewsW = adventureGameViewsW;
    }
    
    /**
    * 设置上周网络游戏浏览次数
    * @param onlineGameViewsW 上周网络游戏浏览次数
    */
    public void setOnlineGameViewsW(Integer onlineGameViewsW)
    {
        this.onlineGameViewsW = onlineGameViewsW;
    }
    
    /**
    * 设置上周娱乐软件浏览次数
    * @param entertainmentSoftwareViewsW 上周娱乐软件浏览次数
    */
    public void setEntertainmentSoftwareViewsW(Integer entertainmentSoftwareViewsW)
    {
        this.entertainmentSoftwareViewsW = entertainmentSoftwareViewsW;
    }
    
    /**
    * 设置上周财经软件浏览次数
    * @param financeSoftwareViewsW 上周财经软件浏览次数
    */
    public void setFinanceSoftwareViewsW(Integer financeSoftwareViewsW)
    {
        this.financeSoftwareViewsW = financeSoftwareViewsW;
    }
    
    /**
    * 设置上周资讯软件浏览次数
    * @param informationSoftwareViewsW 上周资讯软件浏览次数
    */
    public void setInformationSoftwareViewsW(Integer informationSoftwareViewsW)
    {
        this.informationSoftwareViewsW = informationSoftwareViewsW;
    }
    
    /**
    * 设置上周生活软件浏览次数
    * @param lifeSoftwareViewsW 上周生活软件浏览次数
    */
    public void setLifeSoftwareViewsW(Integer lifeSoftwareViewsW)
    {
        this.lifeSoftwareViewsW = lifeSoftwareViewsW;
    }
    
    /**
    * 设置上周主题软件浏览次数
    * @param themeSoftwareViewsW 上周主题软件浏览次数
    */
    public void setThemeSoftwareViewsW(Integer themeSoftwareViewsW)
    {
        this.themeSoftwareViewsW = themeSoftwareViewsW;
    }
    
    /**
    * 设置上周阅读软件浏览次数
    * @param readSoftwareViewsW 上周阅读软件浏览次数
    */
    public void setReadSoftwareViewsW(Integer readSoftwareViewsW)
    {
        this.readSoftwareViewsW = readSoftwareViewsW;
    }
    
    /**
    * 设置上周工具软件浏览次数
    * @param toolsSoftwareViewsW 上周工具软件浏览次数
    */
    public void setToolsSoftwareViewsW(Integer toolsSoftwareViewsW)
    {
        this.toolsSoftwareViewsW = toolsSoftwareViewsW;
    }
    
    /**
    * 设置上周社交软件浏览次数
    * @param socialSoftwareViewsW 上周社交软件浏览次数
    */
    public void setSocialSoftwareViewsW(Integer socialSoftwareViewsW)
    {
        this.socialSoftwareViewsW = socialSoftwareViewsW;
    }
    
    /**
    * 设置上周系统软件浏览次数
    * @param systemSoftwareViewsW 上周系统软件浏览次数
    */
    public void setSystemSoftwareViewsW(Integer systemSoftwareViewsW)
    {
        this.systemSoftwareViewsW = systemSoftwareViewsW;
    }
    
    /**
    * 设置上周出行软件浏览次数
    * @param travelSoftwareViewsW 上周出行软件浏览次数
    */
    public void setTravelSoftwareViewsW(Integer travelSoftwareViewsW)
    {
        this.travelSoftwareViewsW = travelSoftwareViewsW;
    }
    
    /**
    * 设置上周办公软件浏览次数
    * @param officeSoftwareViewsW 上周办公软件浏览次数
    */
    public void setOfficeSoftwareViewsW(Integer officeSoftwareViewsW)
    {
        this.officeSoftwareViewsW = officeSoftwareViewsW;
    }
    
    /**
    * 设置上周休闲游戏下载次数
    * @param casualGameDownloadsW 上周休闲游戏下载次数
    */
    public void setCasualGameDownloadsW(Integer casualGameDownloadsW)
    {
        this.casualGameDownloadsW = casualGameDownloadsW;
    }
    
    /**
    * 设置上周角色扮演下载次数
    * @param roleplayingGameDownloadsW 上周角色扮演下载次数
    */
    public void setRoleplayingGameDownloadsW(Integer roleplayingGameDownloadsW)
    {
        this.roleplayingGameDownloadsW = roleplayingGameDownloadsW;
    }
    
    /**
    * 设置上周模拟游戏下载次数 
    * @param simulationGameDownloadsW 上周模拟游戏下载次数 
    */
    public void setSimulationGameDownloadsW(Integer simulationGameDownloadsW)
    {
        this.simulationGameDownloadsW = simulationGameDownloadsW;
    }
    
    /**
    * 设置上周体育射击下载次数
    * @param shootingGameDownloadsW 上周体育射击下载次数
    */
    public void setShootingGameDownloadsW(Integer shootingGameDownloadsW)
    {
        this.shootingGameDownloadsW = shootingGameDownloadsW;
    }
    
    /**
    * 设置上周策略游戏下载次数
    * @param strategyGameDownloadsW 上周策略游戏下载次数
    */
    public void setStrategyGameDownloadsW(Integer strategyGameDownloadsW)
    {
        this.strategyGameDownloadsW = strategyGameDownloadsW;
    }
    
    /**
    * 设置上周益智棋牌下载次数
    * @param chesspuzzleGameDownloadsW 上周益智棋牌下载次数
    */
    public void setChesspuzzleGameDownloadsW(Integer chesspuzzleGameDownloadsW)
    {
        this.chesspuzzleGameDownloadsW = chesspuzzleGameDownloadsW;
    }
    
    /**
    * 设置上周动作冒险下载次数
    * @param adventureGameDownloadsW 上周动作冒险下载次数
    */
    public void setAdventureGameDownloadsW(Integer adventureGameDownloadsW)
    {
        this.adventureGameDownloadsW = adventureGameDownloadsW;
    }
    
    /**
    * 设置上周网络游戏下载次数
    * @param onlineGameDownloadsW 上周网络游戏下载次数
    */
    public void setOnlineGameDownloadsW(Integer onlineGameDownloadsW)
    {
        this.onlineGameDownloadsW = onlineGameDownloadsW;
    }
    
    /**
    * 设置上周娱乐软件下载次数 
    * @param entertainmentSoftwareDownloadsW 上周娱乐软件下载次数 
    */
    public void setEntertainmentSoftwareDownloadsW(Integer entertainmentSoftwareDownloadsW)
    {
        this.entertainmentSoftwareDownloadsW = entertainmentSoftwareDownloadsW;
    }
    
    /**
    * 设置上周财经软件下载次数
    * @param financeSoftwareDownloadsW 上周财经软件下载次数
    */
    public void setFinanceSoftwareDownloadsW(Integer financeSoftwareDownloadsW)
    {
        this.financeSoftwareDownloadsW = financeSoftwareDownloadsW;
    }
    
    /**
    * 设置上周资讯软件下载次数
    * @param informationSoftwareDownloadsW 上周资讯软件下载次数
    */
    public void setInformationSoftwareDownloadsW(Integer informationSoftwareDownloadsW)
    {
        this.informationSoftwareDownloadsW = informationSoftwareDownloadsW;
    }
    
    /**
    * 设置上周生活软件下载次数
    * @param lifeSoftwareDownloadsW 上周生活软件下载次数
    */
    public void setLifeSoftwareDownloadsW(Integer lifeSoftwareDownloadsW)
    {
        this.lifeSoftwareDownloadsW = lifeSoftwareDownloadsW;
    }
    
    /**
    * 设置上周主题软件下载次数
    * @param themeSoftwareDownloadsW 上周主题软件下载次数
    */
    public void setThemeSoftwareDownloadsW(Integer themeSoftwareDownloadsW)
    {
        this.themeSoftwareDownloadsW = themeSoftwareDownloadsW;
    }
    
    /**
    * 设置上周阅读软件下载次数
    * @param readSoftwareDownloadsW 上周阅读软件下载次数
    */
    public void setReadSoftwareDownloadsW(Integer readSoftwareDownloadsW)
    {
        this.readSoftwareDownloadsW = readSoftwareDownloadsW;
    }
    
    /**
    * 设置上周工具软件下载次数
    * @param toolsSoftwareDownloadsW 上周工具软件下载次数
    */
    public void setToolsSoftwareDownloadsW(Integer toolsSoftwareDownloadsW)
    {
        this.toolsSoftwareDownloadsW = toolsSoftwareDownloadsW;
    }
    
    /**
    * 设置上周社交软件下载次数
    * @param socialSoftwareDownloadsW 上周社交软件下载次数
    */
    public void setSocialSoftwareDownloadsW(Integer socialSoftwareDownloadsW)
    {
        this.socialSoftwareDownloadsW = socialSoftwareDownloadsW;
    }
    
    /**
    * 设置上周系统软件下载次数
    * @param systemSoftwareDownloadsW 上周系统软件下载次数
    */
    public void setSystemSoftwareDownloadsW(Integer systemSoftwareDownloadsW)
    {
        this.systemSoftwareDownloadsW = systemSoftwareDownloadsW;
    }
    
    /**
    * 设置上周出行软件下载次数
    * @param travelSoftwareDownloadsW 上周出行软件下载次数
    */
    public void setTravelSoftwareDownloadsW(Integer travelSoftwareDownloadsW)
    {
        this.travelSoftwareDownloadsW = travelSoftwareDownloadsW;
    }
    
    /**
    * 设置上周办公软件下载次数
    * @param officeSoftwareDownloadsW 上周办公软件下载次数
    */
    public void setOfficeSoftwareDownloadsW(Integer officeSoftwareDownloadsW)
    {
        this.officeSoftwareDownloadsW = officeSoftwareDownloadsW;
    }
    
    /**
    * 设置上月登录次数
    * @param loginsM 上月登录次数
    */
    public void setLoginsM(Integer loginsM)
    {
        this.loginsM = loginsM;
    }
    
    /**
    * 设置上月搜索次数
    * @param searchsM 上月搜索次数
    */
    public void setSearchsM(Integer searchsM)
    {
        this.searchsM = searchsM;
    }
    
    /**
    * 设置上月浏览次数
    * @param viewsM 上月浏览次数
    */
    public void setViewsM(Integer viewsM)
    {
        this.viewsM = viewsM;
    }
    
    /**
    * 设置上月下载次数
    * @param downloadsM 上月下载次数
    */
    public void setDownloadsM(Integer downloadsM)
    {
        this.downloadsM = downloadsM;
    }
    
    /**
    * 设置上月休闲游戏浏览次数
    * @param casualGameViewsM 上月休闲游戏浏览次数
    */
    public void setCasualGameViewsM(Integer casualGameViewsM)
    {
        this.casualGameViewsM = casualGameViewsM;
    }
    
    /**
    * 设置上月角色扮演浏览次数
    * @param roleplayingGameViewsM 上月角色扮演浏览次数
    */
    public void setRoleplayingGameViewsM(Integer roleplayingGameViewsM)
    {
        this.roleplayingGameViewsM = roleplayingGameViewsM;
    }
    
    /**
    * 设置上月模拟游戏浏览次数
    * @param simulationGameViewsM 上月模拟游戏浏览次数
    */
    public void setSimulationGameViewsM(Integer simulationGameViewsM)
    {
        this.simulationGameViewsM = simulationGameViewsM;
    }
    
    /**
    * 设置上月体育射击浏览次数
    * @param shootingGameViewsM 上月体育射击浏览次数
    */
    public void setShootingGameViewsM(Integer shootingGameViewsM)
    {
        this.shootingGameViewsM = shootingGameViewsM;
    }
    
    /**
    * 设置上月策略游戏浏览次数
    * @param strategyGameViewsM 上月策略游戏浏览次数
    */
    public void setStrategyGameViewsM(Integer strategyGameViewsM)
    {
        this.strategyGameViewsM = strategyGameViewsM;
    }
    
    /**
    * 设置上月益智棋牌浏览次数
    * @param chesspuzzleGameViewsM 上月益智棋牌浏览次数
    */
    public void setChesspuzzleGameViewsM(Integer chesspuzzleGameViewsM)
    {
        this.chesspuzzleGameViewsM = chesspuzzleGameViewsM;
    }
    
    /**
    * 设置上月动作冒险浏览次数
    * @param adventureGameViewsM 上月动作冒险浏览次数
    */
    public void setAdventureGameViewsM(Integer adventureGameViewsM)
    {
        this.adventureGameViewsM = adventureGameViewsM;
    }
    
    /**
    * 设置上月网络游戏浏览次数
    * @param onlineGameViewsM 上月网络游戏浏览次数
    */
    public void setOnlineGameViewsM(Integer onlineGameViewsM)
    {
        this.onlineGameViewsM = onlineGameViewsM;
    }
    
    /**
    * 设置上月娱乐软件浏览次数
    * @param entertainmentSoftwareViewsM 上月娱乐软件浏览次数
    */
    public void setEntertainmentSoftwareViewsM(Integer entertainmentSoftwareViewsM)
    {
        this.entertainmentSoftwareViewsM = entertainmentSoftwareViewsM;
    }
    
    /**
    * 设置上月财经软件浏览次数
    * @param financeSoftwareViewsM 上月财经软件浏览次数
    */
    public void setFinanceSoftwareViewsM(Integer financeSoftwareViewsM)
    {
        this.financeSoftwareViewsM = financeSoftwareViewsM;
    }
    
    /**
    * 设置上月资讯软件浏览次数
    * @param informationSoftwareViewsM 上月资讯软件浏览次数
    */
    public void setInformationSoftwareViewsM(Integer informationSoftwareViewsM)
    {
        this.informationSoftwareViewsM = informationSoftwareViewsM;
    }
    
    /**
    * 设置上月生活软件浏览次数
    * @param lifeSoftwareViewsM 上月生活软件浏览次数
    */
    public void setLifeSoftwareViewsM(Integer lifeSoftwareViewsM)
    {
        this.lifeSoftwareViewsM = lifeSoftwareViewsM;
    }
    
    /**
    * 设置上月主题软件浏览次数
    * @param themeSoftwareViewsM 上月主题软件浏览次数
    */
    public void setThemeSoftwareViewsM(Integer themeSoftwareViewsM)
    {
        this.themeSoftwareViewsM = themeSoftwareViewsM;
    }
    
    /**
    * 设置上月阅读软件浏览次数
    * @param readSoftwareViewsM 上月阅读软件浏览次数
    */
    public void setReadSoftwareViewsM(Integer readSoftwareViewsM)
    {
        this.readSoftwareViewsM = readSoftwareViewsM;
    }
    
    /**
    * 设置上月工具软件浏览次数
    * @param toolsSoftwareViewsM 上月工具软件浏览次数
    */
    public void setToolsSoftwareViewsM(Integer toolsSoftwareViewsM)
    {
        this.toolsSoftwareViewsM = toolsSoftwareViewsM;
    }
    
    /**
    * 设置上月社交软件浏览次数
    * @param socialSoftwareViewsM 上月社交软件浏览次数
    */
    public void setSocialSoftwareViewsM(Integer socialSoftwareViewsM)
    {
        this.socialSoftwareViewsM = socialSoftwareViewsM;
    }
    
    /**
    * 设置上月系统软件浏览次数
    * @param systemSoftwareViewsM 上月系统软件浏览次数
    */
    public void setSystemSoftwareViewsM(Integer systemSoftwareViewsM)
    {
        this.systemSoftwareViewsM = systemSoftwareViewsM;
    }
    
    /**
    * 设置上月出行软件浏览次数
    * @param travelSoftwareViewsM 上月出行软件浏览次数
    */
    public void setTravelSoftwareViewsM(Integer travelSoftwareViewsM)
    {
        this.travelSoftwareViewsM = travelSoftwareViewsM;
    }
    
    /**
    * 设置上月办公软件浏览次数
    * @param officeSoftwareViewsM 上月办公软件浏览次数
    */
    public void setOfficeSoftwareViewsM(Integer officeSoftwareViewsM)
    {
        this.officeSoftwareViewsM = officeSoftwareViewsM;
    }
    
    /**
    * 设置上月休闲游戏下载次数
    * @param casualGameDownloadsM 上月休闲游戏下载次数
    */
    public void setCasualGameDownloadsM(Integer casualGameDownloadsM)
    {
        this.casualGameDownloadsM = casualGameDownloadsM;
    }
    
    /**
    * 设置上月角色扮演下载次数
    * @param roleplayingGameDownloadsM 上月角色扮演下载次数
    */
    public void setRoleplayingGameDownloadsM(Integer roleplayingGameDownloadsM)
    {
        this.roleplayingGameDownloadsM = roleplayingGameDownloadsM;
    }
    
    /**
    * 设置上月模拟游戏下载次数 
    * @param simulationGameDownloadsM 上月模拟游戏下载次数 
    */
    public void setSimulationGameDownloadsM(Integer simulationGameDownloadsM)
    {
        this.simulationGameDownloadsM = simulationGameDownloadsM;
    }
    
    /**
    * 设置上月体育射击下载次数
    * @param shootingGameDownloadsM 上月体育射击下载次数
    */
    public void setShootingGameDownloadsM(Integer shootingGameDownloadsM)
    {
        this.shootingGameDownloadsM = shootingGameDownloadsM;
    }
    
    /**
    * 设置上月策略游戏下载次数
    * @param strategyGameDownloadsM 上月策略游戏下载次数
    */
    public void setStrategyGameDownloadsM(Integer strategyGameDownloadsM)
    {
        this.strategyGameDownloadsM = strategyGameDownloadsM;
    }
    
    /**
    * 设置上月益智棋牌下载次数
    * @param chesspuzzleGameDownloadsM 上月益智棋牌下载次数
    */
    public void setChesspuzzleGameDownloadsM(Integer chesspuzzleGameDownloadsM)
    {
        this.chesspuzzleGameDownloadsM = chesspuzzleGameDownloadsM;
    }
    
    /**
    * 设置上月动作冒险下载次数
    * @param adventureGameDownloadsM 上月动作冒险下载次数
    */
    public void setAdventureGameDownloadsM(Integer adventureGameDownloadsM)
    {
        this.adventureGameDownloadsM = adventureGameDownloadsM;
    }
    
    /**
    * 设置上月网络游戏下载次数
    * @param onlineGameDownloadsM 上月网络游戏下载次数
    */
    public void setOnlineGameDownloadsM(Integer onlineGameDownloadsM)
    {
        this.onlineGameDownloadsM = onlineGameDownloadsM;
    }
    
    /**
    * 设置上月娱乐软件下载次数 
    * @param entertainmentSoftwareDownloadsM 上月娱乐软件下载次数 
    */
    public void setEntertainmentSoftwareDownloadsM(Integer entertainmentSoftwareDownloadsM)
    {
        this.entertainmentSoftwareDownloadsM = entertainmentSoftwareDownloadsM;
    }
    
    /**
    * 设置上月财经软件下载次数
    * @param financeSoftwareDownloadsM 上月财经软件下载次数
    */
    public void setFinanceSoftwareDownloadsM(Integer financeSoftwareDownloadsM)
    {
        this.financeSoftwareDownloadsM = financeSoftwareDownloadsM;
    }
    
    /**
    * 设置上月资讯软件下载次数
    * @param informationSoftwareDownloadsM 上月资讯软件下载次数
    */
    public void setInformationSoftwareDownloadsM(Integer informationSoftwareDownloadsM)
    {
        this.informationSoftwareDownloadsM = informationSoftwareDownloadsM;
    }
    
    /**
    * 设置上月生活软件下载次数
    * @param lifeSoftwareDownloadsM 上月生活软件下载次数
    */
    public void setLifeSoftwareDownloadsM(Integer lifeSoftwareDownloadsM)
    {
        this.lifeSoftwareDownloadsM = lifeSoftwareDownloadsM;
    }
    
    /**
    * 设置上月主题软件下载次数
    * @param themeSoftwareDownloadsM 上月主题软件下载次数
    */
    public void setThemeSoftwareDownloadsM(Integer themeSoftwareDownloadsM)
    {
        this.themeSoftwareDownloadsM = themeSoftwareDownloadsM;
    }
    
    /**
    * 设置上月阅读软件下载次数
    * @param readSoftwareDownloadsM 上月阅读软件下载次数
    */
    public void setReadSoftwareDownloadsM(Integer readSoftwareDownloadsM)
    {
        this.readSoftwareDownloadsM = readSoftwareDownloadsM;
    }
    
    /**
    * 设置上月工具软件下载次数
    * @param toolsSoftwareDownloadsM 上月工具软件下载次数
    */
    public void setToolsSoftwareDownloadsM(Integer toolsSoftwareDownloadsM)
    {
        this.toolsSoftwareDownloadsM = toolsSoftwareDownloadsM;
    }
    
    /**
    * 设置上月社交软件下载次数
    * @param socialSoftwareDownloadsM 上月社交软件下载次数
    */
    public void setSocialSoftwareDownloadsM(Integer socialSoftwareDownloadsM)
    {
        this.socialSoftwareDownloadsM = socialSoftwareDownloadsM;
    }
    
    /**
    * 设置上月系统软件下载次数
    * @param systemSoftwareDownloadsM 上月系统软件下载次数
    */
    public void setSystemSoftwareDownloadsM(Integer systemSoftwareDownloadsM)
    {
        this.systemSoftwareDownloadsM = systemSoftwareDownloadsM;
    }
    
    /**
    * 设置上月出行软件下载次数
    * @param travelSoftwareDownloadsM 上月出行软件下载次数
    */
    public void setTravelSoftwareDownloadsM(Integer travelSoftwareDownloadsM)
    {
        this.travelSoftwareDownloadsM = travelSoftwareDownloadsM;
    }
    
    /**
    * 设置上月办公软件下载次数
    * @param officeSoftwareDownloadsM 上月办公软件下载次数
    */
    public void setOfficeSoftwareDownloadsM(Integer officeSoftwareDownloadsM)
    {
        this.officeSoftwareDownloadsM = officeSoftwareDownloadsM;
    }
    
    /**
    * 设置历史累计登录次数
    * @param loginsT 历史累计登录次数
    */
    public void setLoginsT(Integer loginsT)
    {
        this.loginsT = loginsT;
    }
    
    /**
    * 设置历史累计搜索次数
    * @param searchsT 历史累计搜索次数
    */
    public void setSearchsT(Integer searchsT)
    {
        this.searchsT = searchsT;
    }
    
    /**
    * 设置历史累计浏览次数
    * @param viewsT 历史累计浏览次数
    */
    public void setViewsT(Integer viewsT)
    {
        this.viewsT = viewsT;
    }
    
    /**
    * 设置历史累计下载次数
    * @param downloadsT 历史累计下载次数
    */
    public void setDownloadsT(Integer downloadsT)
    {
        this.downloadsT = downloadsT;
    }
    
    /**
    * 设置历史累计休闲游戏浏览次数
    * @param casualGameViewsT 历史累计休闲游戏浏览次数
    */
    public void setCasualGameViewsT(Integer casualGameViewsT)
    {
        this.casualGameViewsT = casualGameViewsT;
    }
    
    /**
    * 设置历史累计角色扮演浏览次数
    * @param roleplayingGameViewsT 历史累计角色扮演浏览次数
    */
    public void setRoleplayingGameViewsT(Integer roleplayingGameViewsT)
    {
        this.roleplayingGameViewsT = roleplayingGameViewsT;
    }
    
    /**
    * 设置历史累计模拟游戏浏览次数
    * @param simulationGameViewsT 历史累计模拟游戏浏览次数
    */
    public void setSimulationGameViewsT(Integer simulationGameViewsT)
    {
        this.simulationGameViewsT = simulationGameViewsT;
    }
    
    /**
    * 设置历史累计体育射击浏览次数
    * @param shootingGameViewsT 历史累计体育射击浏览次数
    */
    public void setShootingGameViewsT(Integer shootingGameViewsT)
    {
        this.shootingGameViewsT = shootingGameViewsT;
    }
    
    /**
    * 设置历史累计策略游戏浏览次数
    * @param strategyGameViewsT 历史累计策略游戏浏览次数
    */
    public void setStrategyGameViewsT(Integer strategyGameViewsT)
    {
        this.strategyGameViewsT = strategyGameViewsT;
    }
    
    /**
    * 设置历史累计益智棋牌浏览次数
    * @param chesspuzzleGameViewsT 历史累计益智棋牌浏览次数
    */
    public void setChesspuzzleGameViewsT(Integer chesspuzzleGameViewsT)
    {
        this.chesspuzzleGameViewsT = chesspuzzleGameViewsT;
    }
    
    /**
    * 设置历史累计动作冒险浏览次数
    * @param adventureGameViewsT 历史累计动作冒险浏览次数
    */
    public void setAdventureGameViewsT(Integer adventureGameViewsT)
    {
        this.adventureGameViewsT = adventureGameViewsT;
    }
    
    /**
    * 设置历史累计网络游戏浏览次数
    * @param onlineGameViewsT 历史累计网络游戏浏览次数
    */
    public void setOnlineGameViewsT(Integer onlineGameViewsT)
    {
        this.onlineGameViewsT = onlineGameViewsT;
    }
    
    /**
    * 设置历史累计娱乐软件浏览次数
    * @param entertainmentSoftwareViewsT 历史累计娱乐软件浏览次数
    */
    public void setEntertainmentSoftwareViewsT(Integer entertainmentSoftwareViewsT)
    {
        this.entertainmentSoftwareViewsT = entertainmentSoftwareViewsT;
    }
    
    /**
    * 设置历史累计财经软件浏览次数
    * @param financeSoftwareViewsT 历史累计财经软件浏览次数
    */
    public void setFinanceSoftwareViewsT(Integer financeSoftwareViewsT)
    {
        this.financeSoftwareViewsT = financeSoftwareViewsT;
    }
    
    /**
    * 设置历史累计资讯软件浏览次数
    * @param informationSoftwareViewsT 历史累计资讯软件浏览次数
    */
    public void setInformationSoftwareViewsT(Integer informationSoftwareViewsT)
    {
        this.informationSoftwareViewsT = informationSoftwareViewsT;
    }
    
    /**
    * 设置历史累计生活软件浏览次数
    * @param lifeSoftwareViewsT 历史累计生活软件浏览次数
    */
    public void setLifeSoftwareViewsT(Integer lifeSoftwareViewsT)
    {
        this.lifeSoftwareViewsT = lifeSoftwareViewsT;
    }
    
    /**
    * 设置历史累计主题软件浏览次数
    * @param themeSoftwareViewsT 历史累计主题软件浏览次数
    */
    public void setThemeSoftwareViewsT(Integer themeSoftwareViewsT)
    {
        this.themeSoftwareViewsT = themeSoftwareViewsT;
    }
    
    /**
    * 设置历史累计阅读软件浏览次数
    * @param readSoftwareViewsT 历史累计阅读软件浏览次数
    */
    public void setReadSoftwareViewsT(Integer readSoftwareViewsT)
    {
        this.readSoftwareViewsT = readSoftwareViewsT;
    }
    
    /**
    * 设置历史累计工具软件浏览次数
    * @param toolsSoftwareViewsT 历史累计工具软件浏览次数
    */
    public void setToolsSoftwareViewsT(Integer toolsSoftwareViewsT)
    {
        this.toolsSoftwareViewsT = toolsSoftwareViewsT;
    }
    
    /**
    * 设置历史累计社交软件浏览次数
    * @param socialSoftwareViewsT 历史累计社交软件浏览次数
    */
    public void setSocialSoftwareViewsT(Integer socialSoftwareViewsT)
    {
        this.socialSoftwareViewsT = socialSoftwareViewsT;
    }
    
    /**
    * 设置历史累计系统软件浏览次数
    * @param systemSoftwareViewsT 历史累计系统软件浏览次数
    */
    public void setSystemSoftwareViewsT(Integer systemSoftwareViewsT)
    {
        this.systemSoftwareViewsT = systemSoftwareViewsT;
    }
    
    /**
    * 设置历史累计出行软件浏览次数
    * @param travelSoftwareViewsT 历史累计出行软件浏览次数
    */
    public void setTravelSoftwareViewsT(Integer travelSoftwareViewsT)
    {
        this.travelSoftwareViewsT = travelSoftwareViewsT;
    }
    
    /**
    * 设置历史累计办公软件浏览次数
    * @param officeSoftwareViewsT 历史累计办公软件浏览次数
    */
    public void setOfficeSoftwareViewsT(Integer officeSoftwareViewsT)
    {
        this.officeSoftwareViewsT = officeSoftwareViewsT;
    }
    
    /**
    * 设置历史累计休闲游戏下载次数
    * @param casualGameDownloadsT 历史累计休闲游戏下载次数
    */
    public void setCasualGameDownloadsT(Integer casualGameDownloadsT)
    {
        this.casualGameDownloadsT = casualGameDownloadsT;
    }
    
    /**
    * 设置历史累计角色扮演下载次数
    * @param roleplayingGameDownloadsT 历史累计角色扮演下载次数
    */
    public void setRoleplayingGameDownloadsT(Integer roleplayingGameDownloadsT)
    {
        this.roleplayingGameDownloadsT = roleplayingGameDownloadsT;
    }
    
    /**
    * 设置历史累计模拟游戏下载次数
    * @param simulationGameDownloadsT 历史累计模拟游戏下载次数
    */
    public void setSimulationGameDownloadsT(Integer simulationGameDownloadsT)
    {
        this.simulationGameDownloadsT = simulationGameDownloadsT;
    }
    
    /**
    * 设置历史累计体育射击下载次数
    * @param shootingGameDownloadsT 历史累计体育射击下载次数
    */
    public void setShootingGameDownloadsT(Integer shootingGameDownloadsT)
    {
        this.shootingGameDownloadsT = shootingGameDownloadsT;
    }
    
    /**
    * 设置历史累计策略游戏下载次数
    * @param strategyGameDownloadsT 历史累计策略游戏下载次数
    */
    public void setStrategyGameDownloadsT(Integer strategyGameDownloadsT)
    {
        this.strategyGameDownloadsT = strategyGameDownloadsT;
    }
    
    /**
    * 设置历史累计益智棋牌下载次数
    * @param chesspuzzleGameDownloadsT 历史累计益智棋牌下载次数
    */
    public void setChesspuzzleGameDownloadsT(Integer chesspuzzleGameDownloadsT)
    {
        this.chesspuzzleGameDownloadsT = chesspuzzleGameDownloadsT;
    }
    
    /**
    * 设置历史累计动作冒险下载次数
    * @param adventureGameDownloadsT 历史累计动作冒险下载次数
    */
    public void setAdventureGameDownloadsT(Integer adventureGameDownloadsT)
    {
        this.adventureGameDownloadsT = adventureGameDownloadsT;
    }
    
    /**
    * 设置历史累计网络游戏下载次数
    * @param onlineGameDownloadsT 历史累计网络游戏下载次数
    */
    public void setOnlineGameDownloadsT(Integer onlineGameDownloadsT)
    {
        this.onlineGameDownloadsT = onlineGameDownloadsT;
    }
    
    /**
    * 设置历史累计娱乐软件下载次数
    * @param entertainmentSoftwareDownloadsT 历史累计娱乐软件下载次数
    */
    public void setEntertainmentSoftwareDownloadsT(Integer entertainmentSoftwareDownloadsT)
    {
        this.entertainmentSoftwareDownloadsT = entertainmentSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计财经软件下载次数
    * @param financeSoftwareDownloadsT 历史累计财经软件下载次数
    */
    public void setFinanceSoftwareDownloadsT(Integer financeSoftwareDownloadsT)
    {
        this.financeSoftwareDownloadsT = financeSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计资讯软件下载次数
    * @param informationSoftwareDownloadsT 历史累计资讯软件下载次数
    */
    public void setInformationSoftwareDownloadsT(Integer informationSoftwareDownloadsT)
    {
        this.informationSoftwareDownloadsT = informationSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计生活软件下载次数
    * @param lifeSoftwareDownloadsT 历史累计生活软件下载次数
    */
    public void setLifeSoftwareDownloadsT(Integer lifeSoftwareDownloadsT)
    {
        this.lifeSoftwareDownloadsT = lifeSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计主题软件下载次数
    * @param themeSoftwareDownloadsT 历史累计主题软件下载次数
    */
    public void setThemeSoftwareDownloadsT(Integer themeSoftwareDownloadsT)
    {
        this.themeSoftwareDownloadsT = themeSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计阅读软件下载次数
    * @param readSoftwareDownloadsT 历史累计阅读软件下载次数
    */
    public void setReadSoftwareDownloadsT(Integer readSoftwareDownloadsT)
    {
        this.readSoftwareDownloadsT = readSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计工具软件下载次数
    * @param toolsSoftwareDownloadsT 历史累计工具软件下载次数
    */
    public void setToolsSoftwareDownloadsT(Integer toolsSoftwareDownloadsT)
    {
        this.toolsSoftwareDownloadsT = toolsSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计社交软件下载次数
    * @param socialSoftwareDownloadsT 历史累计社交软件下载次数
    */
    public void setSocialSoftwareDownloadsT(Integer socialSoftwareDownloadsT)
    {
        this.socialSoftwareDownloadsT = socialSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计系统软件下载次数
    * @param systemSoftwareDownloadsT 历史累计系统软件下载次数
    */
    public void setSystemSoftwareDownloadsT(Integer systemSoftwareDownloadsT)
    {
        this.systemSoftwareDownloadsT = systemSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计出行软件下载次数
    * @param travelSoftwareDownloadsT 历史累计出行软件下载次数
    */
    public void setTravelSoftwareDownloadsT(Integer travelSoftwareDownloadsT)
    {
        this.travelSoftwareDownloadsT = travelSoftwareDownloadsT;
    }
    
    /**
    * 设置历史累计办公软件下载次数
    * @param officeSoftwareDownloadsT 历史累计办公软件下载次数
    */
    public void setOfficeSoftwareDownloadsT(Integer officeSoftwareDownloadsT)
    {
        this.officeSoftwareDownloadsT = officeSoftwareDownloadsT;
    }
    
    /**
    * 设置手机型号
    * @param terminalType 手机型号
    */
    public void setTerminalType(String terminalType)
    {
        this.terminalType = terminalType;
    }
    
    /**
    * 设置操作系统
    * @param terminalOsVersion 操作系统
    */
    public void setTerminalOsVersion(String terminalOsVersion)
    {
        this.terminalOsVersion = terminalOsVersion;
    }
    
    /**
    * 设置屏幕分辨率
    * @param screenResolution 屏幕分辨率
    */
    public void setScreenResolution(String screenResolution)
    {
        this.screenResolution = screenResolution;
    }
    
    /**
    * 设置运营商
    * @param mobileOper 运营商
    */
    public void setMobileOper(String mobileOper)
    {
        this.mobileOper = mobileOper;
    }
    
    /**
    * 设置智汇云版本号
    * @param serviceClientVersion 智汇云版本号
    */
    public void setServiceClientVersion(String serviceClientVersion)
    {
        this.serviceClientVersion = serviceClientVersion;
    }
    
    /**
    * 设置push推送标志位
    * @param pushFlag push推送标志位
    */
    public void setPushFlag(Integer pushFlag)
    {
        this.pushFlag = pushFlag;
    }

    /**
     * 获取用户标识
     * @return 用户标识
     */
    public Integer getUserAdFlag()
    {
        return userAdFlag;
    }

    /**
     * 设置用户标识
     * @param userAdFlag 用户标识
     */
    public void setUserAdFlag(Integer userAdFlag)
    {
        this.userAdFlag = userAdFlag;
    }

    /**
     * 获取push版本
     * @return push版本
     */
    public String getPushVersion()
    {
        return pushVersion;
    }

    /**
     * 设置push版本
     * @param pushVersion push版本
     */
    public void setPushVersion(String pushVersion)
    {
        this.pushVersion = pushVersion;
    }

    /**
     * 获取广告类型标识
     * @return 广告类型标识
     */
    public Integer getAdTypeFlag()
    {
        return adTypeFlag;
    }

    /**
     * 设置广告类型标识
     * @param adTypeFlag 广告类型标识
     */
    public void setAdTypeFlag(Integer adTypeFlag)
    {
        this.adTypeFlag = adTypeFlag;
    }
}