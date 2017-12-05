/*
 * 文 件 名:  PersonDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月7日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.person.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.person.Person;
import com.huawei.ide.daos.person.PersonDao;

/**
 * 客户对象数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月7日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.person.impl.PersonDaoImpl")
public class PersonDaoImpl extends JdbcDaoSupport implements PersonDao
{
    @Autowired
    private DataSource dataSource;
    
    /**
     * 初始化
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @PostConstruct
    public void initialize()
    {
        setDataSource(dataSource);
    }
    
    /**
     * 
     * PersonRowMapper
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class PersonRowMapper implements RowMapper<Person>
    {
        @Override
        public Person mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            Person person = new Person();
            person.setName(resultSet.getString("name"));
            person.setSex(resultSet.getString("sex"));
            person.setAge(resultSet.getInt("age"));
            person.setJob(resultSet.getString("job"));
            person.setAddress(resultSet.getString("address"));
            return person;
        }
    }
    
    /**
     * 新建表
     */
    @Override
    public void createTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS person" + "(" + "id int(11) NOT NULL AUTO_INCREMENT,"
            + "name varchar(20) NOT NULL," + "sex varchar(10) NULL," + "age int DEFAULT NULL," + "job varchar(50) NULL,"
            + "address varchar(100) NULL," + "PRIMARY KEY (id)" + ")"
            + "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8";
        this.getJdbcTemplate().execute(sql);
    }
    
    /**
     * 删除表
     */
    @Override
    public void deleteTable()
    {
        String sql = "DROP TABLE IF EXISTS person";
        this.getJdbcTemplate().execute(sql);
    }
    
    /**
     * 新建person
     * @param person
     *        person
     */
    @Override
    public void createPerson(Person person)
    {
        String sql = "INSERT INTO person(name, sex, age, job, address) VALUES(?, ?, ?, ?, ?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {person.getName(), person.getSex(), person.getAge(), person.getJob(), person.getAddress()});
    }
    
    /**
     * 修改person
     * @param name
     *        name
     * @param person
     *        person
     */
    @Override
    public void updatePerson(String name, Person person)
    {
        String sql = "UPDATE person SET sex=?, age=?, job=?, address=? WHERE name=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {person.getSex(), person.getAge(), person.getJob(), person.getAddress(), name});
    }
    
    /**
     * 删除person
     * @param name
     *        name
     */
    @Override
    public void deletePerson(String name)
    {
        String sql = "DELETE FROM person WHERE name=?";
        this.getJdbcTemplate().update(sql, new Object[] {name});
    }
    
    /**
     * 查询person
     * @param name
     *        name
     * @return  List<Person>
     */
    @Override
    public List<Person> queryPerson(String name)
    {
        String sql = "SELECT name, sex, age, job, address FROM person WHERE name=?";
        List<Person> persons = this.getJdbcTemplate().query(sql, new Object[] {name}, new PersonRowMapper());
        return (persons != null && !persons.isEmpty()) ? persons : null;
    }
}
