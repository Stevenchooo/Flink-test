import ConfigParser
import string, os, sys
import paramiko

class UploadTool:
    def __init__ (self, configFile):        
        self.__configFile = configFile
        self.__dictFtpParam = {} ;
        self.cf = ConfigParser.ConfigParser()
        self.cf.read(self.__configFile)
         
        #return all section
        secs = self.cf.sections()
        print 'sections:', secs
         
        opts = self.cf.options("upload")
        print 'options:', opts
         
        kvs = self.cf.items("upload")
        print 'upload:', kvs 

        self.__dictFtpParam ["sftp.host"] = self.cf.get("upload", "sftp.host")
        self.__dictFtpParam ["sftp.port"] = self.cf.getint("upload", "sftp.port")
        self.__dictFtpParam ["sftp.username"] = self.cf.get("upload", "sftp.username")
        self.__dictFtpParam ["sftp.password"] = self.cf.get("upload", "sftp.password") 
        self.__dictFtpParam ["sftp.dest.dir"] = self.cf.get("upload", "sftp.dest.dir") 
        self.__dictFtpParam ["sftp.src.ssh"] = self.cf.get("upload", "sftp.src.ssh")
        self.__dictFtpParam ["sftp.src.src"] = self.cf.get("upload", "sftp.src.src")
        self.__dictFtpParam ["sftp.dest.sshFileName"] = self.cf.get("upload", "sftp.dest.sshFileName")
        self.__dictFtpParam ["sftp.dest.srcFileName"] = self.cf.get("upload", "sftp.dest.srcFileName")

    def run (self):                    
        self.__upload()

    def __upload(self):
            
            #mkdir
            print 'run __upload'

            sshcommand = "mkdir -p "+self.__dictFtpParam ["sftp.dest.dir"] +" ;"+"rm "+self.__dictFtpParam ["sftp.dest.dir"] + "/"+ self.__dictFtpParam ["sftp.dest.sshFileName"]+";"+"rm "+self.__dictFtpParam ["sftp.dest.dir"] + "/"+ self.__dictFtpParam ["sftp.dest.srcFileName"]+";"
          
            print "excute  command :" ,sshcommand
            
            self.__ssh(sshcommand)

            sshdest = self.__dictFtpParam ["sftp.dest.dir"] +"/"+self.__dictFtpParam ["sftp.dest.sshFileName"]

            print "upload sh file to  :" ,self.__dictFtpParam ["sftp.src.ssh"],sshdest
 
            self.__sftp(self.__dictFtpParam ["sftp.src.ssh"] ,sshdest)

            srcdest = self.__dictFtpParam ["sftp.dest.dir"] +"/"+self.__dictFtpParam ["sftp.dest.srcFileName"]

            print "upload src file to  :" ,self.__dictFtpParam ["sftp.src.src"],srcdest

            self.__sftp(self.__dictFtpParam ["sftp.src.src"] ,srcdest)

            runssh = "sh "+self.__dictFtpParam ["sftp.dest.dir"] +"/"+self.__dictFtpParam ["sftp.dest.sshFileName"]
            print "excute  command :" ,runssh

            self.__ssh(runssh)

            #upload
 
    def __ssh (self,command):
            print "ssh2 execute command: %s" %(command)
            ssh = paramiko.SSHClient()
            ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
            ssh.connect(self.__dictFtpParam["sftp.host"], username=self.__dictFtpParam ["sftp.username"], password=self.__dictFtpParam ["sftp.password"])
            stdin, stdout, stderr = ssh.exec_command(command)
            stdin.close()
            for line in stdout.read().splitlines():
                print line
            for line in stderr.read().splitlines():
                print "command error ---%s" %(line)


    def __sftp (self,file_zip, destFile):
            t = paramiko.Transport((self.__dictFtpParam["sftp.host"], self.__dictFtpParam ["sftp.port"]))
            t.connect(username=self.__dictFtpParam ["sftp.username"], password=self.__dictFtpParam ["sftp.password"], hostkey=None)
            sftp = paramiko.SFTPClient.from_transport(t)
            
            # dirlist on remote host
            dirlist = sftp.listdir('.')
            print "Dirlist:", dirlist
            
            sftp.put(file_zip, destFile)

            t.close()
 


if __name__ == "__main__":   
    if len (sys.argv) == 2:               
        mk = UploadTool (sys.argv [1])
        mk.run()
        exit (0)

    print "ERROR: cmd args error."
    print sys.argv
