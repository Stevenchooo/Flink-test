
connect 'jdbc:derby:database';
select host, containerid, count(*)
from
(
 select t1.host, t1.containerid, t1.sub_containerid, t2.action
 from 
 (
   select host, containerid, sub_containerid 
   from  YARN_CONTAINER 
   where happen_time > '2017-03-31 15:40:00,000' and happen_time<'2017-03-31 16:25:00,000' and action='start' and host='lf2-bi-fihadoop-71-41-5' 
  ) t1
  left outer join 
 (
   select host, containerid, sub_containerid  ,action
   from  YARN_CONTAINER 
   where happen_time > '2017-03-31 15:40:00,000' and happen_time<'2017-03-31 16:25:00,000' and action='stop' and host='lf2-bi-fihadoop-71-41-5' 
 ) t2
 on(t1.host = t1.host and t1.containerid=t2.containerid and t1.sub_containerid =t2.sub_containerid) 
 where t2.action is null
) t3
group by host, containerid
having count(*) > 1;
