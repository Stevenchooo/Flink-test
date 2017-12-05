/*
 * 文 件 名:  RuleEngineSystemMVCController.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月17日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.controllers.res;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * RuleEngineSystem Mvc Controller
 * @author  z00219375
 * @version  [版本号, 2015年12月17日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Controller
@RequestMapping("/res")
public class RuleEngineSystemMVCController
{
    /**
     * 近规则引擎主页
     * <功能详细描述>
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/index")
    public String index()
    {
        return "rule_engine_system/index/index";
    }
    
}
