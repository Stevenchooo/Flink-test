/*
 * 文 件 名:  PersonController.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年11月27日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.controllers.person;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.huawei.ide.beans.person.Person;
import com.huawei.ide.services.person.PersonService;

/**
 * PersonController
 * @author  z00219375
 * @version  [版本号, 2015年11月27日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Controller
@RequestMapping("/rule")
public class PersonController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    
    @SuppressWarnings("unused")
    private PersonService personService;
    
    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }
    
    /**
     * 处理请求:/rule/person?id=123
     * <功能详细描述>
     * @param id
     *        id
     * @param model
     *        model
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public String viewPerson(@RequestParam("id") Integer id, Model model)
    {
        LOGGER.debug("In viewPerson,id={}", id);
        //Person person = personService.getPerson();
        Person person = new Person();
        person.setAge(id);
        model.addAttribute(person);
        return "person";
    }
    
    /**
     * 处理请求:/rule/person2/{id}
     * <功能详细描述>
     * @param id
     *        id
     * @param model
     *        model
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/person2/{id}", method = RequestMethod.GET)
    public String viewPerson2(@PathVariable("id") Integer id, Map<String, Object> model)
    {
        //Person person = personService.getPerson();
        Person person = new Person();
        person.setAge(id);
        model.put("person", person);
        return "person";
    }
    
    /**
     * 处理请求:/rule/person3?id=123
     * <功能详细描述>
     * @param request
     *        request
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("person3")
    public String viewPerson3(HttpServletRequest request)
    {
        Integer id = Integer.parseInt(request.getParameter("id"));
        //Person person = personService.getPerson();
        Person person = new Person();
        person.setAge(id);
        request.setAttribute("person", person);
        return "person";
    }
    
    /**
     * 处理请求:/rule/admin?add
     * <功能详细描述>
     * @return   String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET, params = "add")
    public String addPerson()
    {
        return "admin/edit";
    }
    
    /**
     * 处理请求:/rule/save
     * <功能详细描述>
     * @param person
     *        person
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String doSave(@ModelAttribute Person person)
    {
        LOGGER.debug("In PersonController doSave");
        LOGGER.debug(ReflectionToStringBuilder.toString(person));
        
        //业务操作，例如数据库持久化
        person.setAge(555);
        return "redirect:person2/" + person.getAge();
    }
    
    /**
     * 处理请求:/rule/upload
     * <功能详细描述>
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String showUploadPage()
    {
        return "admin/file";
    }
    
    /**
     * 处理请求:/rule/doUpload
     * <功能详细描述>
     * @param file
     *        file
     * @return   String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/doUpload", method = RequestMethod.POST)
    public String doUploadFile(@RequestParam("file") MultipartFile file)
    {
        if (!file.isEmpty())
        {
            LOGGER.debug("Process file:{}", file.getOriginalFilename());
            try
            {
                FileUtils.copyInputStreamToFile(file.getInputStream(),
                    new File("D:\\apache-tomcat-8.0.24\\temp",
                        System.currentTimeMillis() + file.getOriginalFilename()));
            }
            catch (IOException e)
            {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return "admin/success";
    }
    
    /**
     * 多文件上传
     *处理请求:/rule/upload2
     * <功能详细描述>
     * @return    String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/upload2", method = RequestMethod.GET)
    public String showUploadPage2()
    {
        return "admin/multifile";
    }
    
    /**
     * 处理请求：/rule/doUpload2
     * <功能详细描述>
     * @param multiRequest
     *         multiRequest
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/doUpload2", method = RequestMethod.POST)
    public String doUploadFile2(MultipartHttpServletRequest multiRequest)
    {
        Iterator<String> filesNames = multiRequest.getFileNames();
        while (filesNames.hasNext())
        {
            String fileName = filesNames.next();
            MultipartFile file = multiRequest.getFile(fileName);
            if (!file.isEmpty())
            {
                LOGGER.debug("Process file: {}", file.getOriginalFilename());
                try
                {
                    FileUtils.copyInputStreamToFile(file.getInputStream(),
                        new File("D:\\apache-tomcat-8.0.24\\temp",
                            System.currentTimeMillis() + file.getOriginalFilename()));
                }
                catch (IOException e)
                {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return "admin/success";
    }
    
    /**
     * 处理请求:/rule/{id}
     * <功能详细描述>
     * @param id
     *        id
     * @return  Person
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Person getPersonJson(@PathVariable("id") Integer id)
    {
        //Person person = personService.getPerson();
        Person person = new Person();
        return person;
    }
    
    /**
     * 处理请求:/rule/jsontype/{id}
     * <功能详细描述>
     * @param id
     *        id
     * @return   ResponseEntity<Person>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/jsontype/{id}", method = RequestMethod.GET)
    public ResponseEntity<Person> getPersonJson2(@PathVariable("id") Integer id)
    {
        //Person person = personService.getPerson();
        Person person = new Person();
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }
    
}
