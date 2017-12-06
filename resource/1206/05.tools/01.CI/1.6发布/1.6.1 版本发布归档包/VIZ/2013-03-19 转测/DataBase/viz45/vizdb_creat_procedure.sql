use viz45;

DELIMITER ;;
CREATE TRIGGER `genRootResPUser` AFTER INSERT ON `user` FOR EACH ROW begin
   insert into `resource`(`user`,resourceType,resourceName) values(NEW.user,'folder','Root');
   update resource set fullpath=LAST_INSERT_ID() where resourceId=LAST_INSERT_ID();
end;;
DELIMITER ;