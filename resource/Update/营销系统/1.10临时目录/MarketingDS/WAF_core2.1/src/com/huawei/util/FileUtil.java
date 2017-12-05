package com.huawei.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import com.huawei.waf.protocol.Const;

public class FileUtil {
    private static final Logger LOG = LogUtil.getInstance();
    private static final int BUFF_SIZE =  1024 * 4;
    
    public static final boolean remove(File f) throws IOException {
        if(!f.exists()){//判断是否存在
            return true;
        }
        
        if(f.isDirectory()) {
	        File files[] = f.listFiles();
	        if(files != null) {
		        //若有则把文件放进数组，并判断是否有下级目录  
		        for(File ff : files) {  
		            if(ff.isDirectory()){  
		                remove(ff);//递归调用remove方法并取得子目录路径  
		            } else {  
		            	ff.delete();//删除文件
		            }
		        }
	        }
        }
        
        return f.delete();
    }
    
    private static final int BUFF_LEN = 128 * 1024;
    
    public static final boolean zipFile(String zipFile, String[] files){
    	String entryName;
    	int readLen;
    	ZipOutputStream zos = null;
    	byte[] buff = new byte[BUFF_LEN];
    	
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			for(String file : files) {
				entryName = getFileName(file);
				
	    		try {
					zos.putNextEntry(new ZipEntry(entryName));
				} catch (IOException e) {
					LOG.error("Fail to put entry {}", entryName);
					return false;
				}
	    		
	    		FileInputStream fis = new FileInputStream(file);
    			try {
		    		do{
		    			readLen = fis.read(buff);
						zos.write(buff, 0, readLen);
		    		} while(readLen >= BUFF_LEN);
				} catch (IOException e) {
					LOG.error("Fail to write {}", file, e);
					return false;
				} finally {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Fail to create zip file {}", zipFile, e);
			return false;
		} finally {
            IOUtils.closeQuietly(zos);
		}
		return true;
	}     
    
    public static final byte[] zipFile(String file){
    	int readLen;
    	ZipOutputStream zos = null;
    	byte[] buff = new byte[BUFF_LEN];
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	
		try {
			zos = new ZipOutputStream(baos);
    		FileInputStream fis = new FileInputStream(file);
			try {
	    		do{
	    			readLen = fis.read(buff);
					zos.write(buff, 0, readLen);
	    		} while(readLen >= BUFF_LEN);
			} catch (IOException e) {
				LOG.error("Fail to write {}", file, e);
				return null;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Fail to create zip file {}", file, e);
			return null;
		} finally {
            IOUtils.closeQuietly(zos);
		}
		return baos.toByteArray();
	}     
    
    public static final String addPath(String path, String addPath) {
        return addPath(path, addPath, File.separatorChar);
    }
    
    public static final String addPath(String path, String addPath, char separatorChar) {
    	int len = path.length();
    	char ch = path.charAt(len - 1);
    	
    	if(ch == separatorChar) {
    		return path + addPath;
    	}
    	
    	if(ch == '/' || ch == '\\') {
    		return path.substring(0, len - 2) + separatorChar + addPath;
    	}
    	
    	return path + separatorChar + addPath;
    } 
    
    public static final String getFileExtension(String fileName) {
    	int pos = fileName.lastIndexOf('.');
    	if(pos >= 0) {
    		return fileName.substring(pos + 1);
    	}
    	return "";
    }
    
    public static final String getFileName(String pathName) {
        int pos = pathName.lastIndexOf('/');
        if(pos < 0) {
            pos = pathName.lastIndexOf('\\');
        }
        
        if(pos >= 0) {
            return pathName.substring(pos + 1);
        }
        
    	return pathName;
    }
    
    /***
     * 读取XML文件
     * @param strPath
     * @return Document
     */
    public static final Document readXml(String strPath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document xmldoc = null;
        try {
            factory.setIgnoringElementContentWhitespace(true);
            
            DocumentBuilder db;
            db = factory.newDocumentBuilder();
            xmldoc = db.parse(new File(strPath));
        } catch (Exception e) {
            LOG.error("Fail to readXml {}", strPath, e);
        }
        return xmldoc;
    }
    
    /***
     * 删除文件
     * @param filePath
     */
    public static final boolean removeFile(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            return true;
        }
        return file.delete();
    }
    
    /**
     * 复制文件(以超快的速度复制文件)
     * 
     * @param srcFile 源文件File
     * @param destDir 目标目录File
     * @param newFileName 新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    public static final long copyFile(File srcFile, File destDir, String newFileName) {
        if(srcFile == null || !srcFile.exists()) {
            return -1;
        } 
        
        if(destDir == null || !destDir.exists()) {
            return -1;
        }
        
        if(newFileName == null) {
            return -1;
        }
        
        FileChannel fcin = null;
        FileChannel fcout = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        
        try {
            fis = new FileInputStream(srcFile);
            fcin = fis.getChannel();
            fos = new FileOutputStream(new File(destDir, newFileName));
            fcout = fos.getChannel();
            long size = fcin.size();
            fcin.transferTo(0, size, fcout);
            
            return size;
        } catch (Exception e) {
            LOG.error("Fail to copy file to {}", newFileName, e);
            return -1;
        } finally {
            IOUtils.closeQuietly(fcin);
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fcout);
            IOUtils.closeQuietly(fos);
        }
    }
    
    public static final long copyFile(File srcFile, File destDir) {
    	String srcFileName = getFileName(srcFile.getAbsolutePath());
    	return copyFile(srcFile, destDir, srcFileName);
    }
    
    /***
     * 检查文件是否存在
     * @param filePath
     * @return
     */
    public static final boolean isFileExist(String filePath) {
        File file = new File(filePath);
        if(file.exists()) {
            return true;
        }
        return false;
    }
    
    public static final byte[] readStream(InputStream in) {
    	int readLen;
        byte[] content = new byte[BUFF_SIZE];
        ByteArrayOutputStream bos = new ByteArrayOutputStream(BUFF_SIZE * 2);
        
        try {
			while((readLen = in.read(content)) > 0) {
				bos.write(content, 0, readLen);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			LOG.error("Fail to read stream", e);
		} finally {
		    IOUtils.closeQuietly(bos);
		}
        return null;
    }
    
    public static final String readFile(String path, String fileName) {
        byte[] content = readFile(new File(path, fileName));
        if(content == null) {
        	return null;
        }
        return new String(content, Const.DEFAULT_CHARSET);
    }

    public static final byte[] readFile(File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return bytes;
        } catch (FileNotFoundException e) {
            LOG.error("File not found {}", file, e);
        } catch (IOException e) {
            LOG.error("Fail to read {}", file, e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
    }
    
    /**
     * 写内容到文件
     * @param file 文件路径
     * @param value 字节数组
     */
    public static final boolean writeFile(File file, byte[] value) {
        OutputStream out = null;
        
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (IOException e) {
            LOG.error("Fail to writeFile {}", file, e);
            return false;
        } finally {
            IOUtils.closeQuietly(out);
        }
        return true;
    }
    
    public static final boolean writeFile(File file, String value) {
    	return writeFile(file, value.getBytes(Const.DEFAULT_CHARSET));
    }
    
    /**
     * 将输入流输出到本地文件中
     * @param saveDir
     * @param fileName
     * @param in
     * @return
     */
    public static final boolean writeFile(String saveDir, String fileName, InputStream in) {
        OutputStream out = null;
        File file = new File(saveDir, fileName);
        byte[] buffer = new byte[10 * 1024];
        int readLen;
        
        try {
            out = new FileOutputStream(file);
            while((readLen = in.read(buffer)) > 0) {
            	out.write(buffer, 0, readLen);
            }
        } catch (IOException e) {
            LOG.error("Fail to writeFile {}", file, e);
            return false;
        } finally {
            IOUtils.closeQuietly(out);
        }
        
        return true;
    }
    
    /**
     * 写内容到文件
     * @param path 文件路径
     * @param fileName 文件名
     * @param value 写入内容，utf8编码
     */
    public static final boolean writeFile(String path, String fileName, String value) {
    	return writeFile(new File(path, fileName), value);
    }
    
    /**
     * 写可序列化的对象到文件中
     * @param file
     * @param value
     * @return
     */
    public static final boolean writeObject(File file, Serializable value) {
        ObjectOutputStream oos = null; 
        FileOutputStream fos = null;
        
        try {
            if (file.exists()) {
                if (!file.delete()) {
                    LOG.error("Fail to delete {}", file);
                    return false;
                }
            }

            file.createNewFile();
            
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);  
            oos.writeObject(value);  
            oos.flush();  
        } catch (Exception e) {
            LOG.error("Fail to write file {}", file, e);
            return false;
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(fos);
        }
        return true;
    }
    
    public static final boolean writeObject(String path, String fileName, Serializable value) {
        return writeObject(new File(path, fileName), value);
    }

    public static final boolean writeObject(String fileName, Serializable value) {
        return writeObject(new File(fileName), value);
    }

    /**
     * 读取序列化的对象文件，与writeObject配合使用
     * @param file
     * @return
     */
    public static final Serializable readObject(File file) {
        ObjectInputStream ois = null;
        FileInputStream fis = null;

        try {
            if (!file.exists()) {
                LOG.debug("read file fail! {} is not exist", file);
                return null;
            }
            
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);  
            Serializable so = (Serializable)ois.readObject();
            ois.close();
            
            return so;
        } catch (Exception e) {
            LOG.error("Fail to read file {}", file, e);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }
        return null;
    }
    
    public static final Serializable readObject(String path, String fileName) {
        return readObject(new File(path, fileName));
    }
    
    public static final Serializable readObject(String fileName) {
        return readObject(new File(fileName));
    }

//    public static void main(String[] args) throws Exception {
//    	//zipFile("d:/temp/aa.zip", new String[] {"d:/temp/bksuz1MNtmLzN1VGcFxpvD", "d:/temp/kx_GUi1YwNIKMVT64g0LnD"});
//    	remove(new File("F:\\Tools\\Develop\\MATLAB_R2012a\\"));
//    }
}
