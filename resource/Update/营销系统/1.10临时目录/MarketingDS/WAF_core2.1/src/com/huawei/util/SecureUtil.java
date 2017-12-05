package com.huawei.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import com.huawei.waf.core.config.sys.SecurityConfig;
import com.huawei.waf.protocol.RetCode;

public class SecureUtil {
    private static final String chars4 = "!@#$%^&*_+=-~`\"';:()<>{}[].,?/ \t";
    private static final SecureRandom rand = new SecureRandom();
    
    /**
     * 判断密码是否足够安全
     * @param account 账号
     * @param pwd 密码
     * @param min 密码最小长度
     * @param max 密码最大长度
     * @param charTypeNum 密码中必须包括几种字符 |0-9|a-z|A-Z|chars4|其他|
     * @param diffCharNum 密码中不同字符的数量，重复字符数不可太多
     * @return
     */
    public static final int isValidPassword(String account, String pwd, int min, int max, int charTypeNum, int diffCharNum) {
        int pwdLen = pwd.length();
        
        if(pwdLen < min) {
            return RetCode.TOOSHORT_PASSWORD;
        }
        if(pwdLen > max) {
            return RetCode.TOOSHORT_PASSWORD;
        }

        int accLen = account.length();
        String lAcc = account.toLowerCase();
        String lPwd = pwd.toLowerCase();
        if(lAcc.indexOf(lPwd) >= 0 || lPwd.indexOf(lAcc) >= 0) {
            return RetCode.PASSWORD_LIKE_ACCOUNT;
        }
        
        //防止密码与账号反向相同
        if(accLen == pwdLen) {
            boolean isOK = false;
            for(int i = 0; i < accLen; i++) {
                if(lAcc.charAt(i) != lPwd.charAt(pwdLen - i - 1)) {
                    isOK = true;
                    break;
                }
            }
            if(!isOK) {
                return RetCode.PASSWORD_LIKE_ACCOUNT;
            }
        }
        
        
        int charTypeRec = 0;
        char ch;
        Set<Character> chars = new HashSet<Character>();
        
        for(int i = 0; i < pwdLen; i++) {
            ch = pwd.charAt(i);
            if(ch >= '0' && ch <= '9') {
                charTypeRec |= 0x01;
            } else if(ch >= 'a' && ch <= 'z') {
                charTypeRec |= 0x02;
            } else if(ch >= 'A' && ch <= 'Z') {
                charTypeRec |= 0x04;
            } else if(chars4.indexOf(ch) >= 0) {
                charTypeRec |= 0x08;
            } else {
                charTypeRec |= 0x10;
            }
            chars.add(ch);
        }
        
        if(Integer.bitCount(charTypeRec) < charTypeNum) {
            return RetCode.TOOSIMPLE_PASSWORD;
        }
        
        if(chars.size() < diffCharNum) {
            return RetCode.TOOFEW_CHARTYPE_PASSWORD;
        }
        return RetCode.OK;
    }
    
    public static final int isValidPassword(String account, String pwd) {
        return isValidPassword(account, pwd,
                SecurityConfig.getMinPasswordLen(),
                SecurityConfig.getMaxPasswordLen(),
                SecurityConfig.getMinCharTypeNum(),
                SecurityConfig.getMinDiffCharNum());
    }
    
    /**
     * 数据库中不可以记录源密码，只可以记录不可逆加密后的密码
     * @param account
     * @param pwd
     * @return
     */
    public static final String encodePassword(String account, String pwd) {
        return Utils.bin2base64(Utils.sha256(account + pwd));
    }
    
    public static final String generatePassword(int len) {
        StringBuilder s = new StringBuilder(len);
        int val;
        int charNum = chars4.length() + 62;
        
        for(int j = 0; j < len; j++) {
            val = rand.nextInt(charNum);
            if(val >= 0 && val < 26) {
                s.append((char)('a' + val));
            } else if(val >= 26 && val < 52) {
                s.append((char)('A' + (val - 26)));
            } else if(val >= 52 && val < 62) {
                s.append((char)('0' + (val - 52)));
            } else {
                s.append(chars4.charAt(val - 62));
            }
        }
        
        return s.toString();
    }
    /**
     * 根据要求产生一个符合安全要求的密码
     * 如果10次仍然不能得到足够安全的密码，则使用最后一次产生的密码
     * 通常不会超过2次尝试就可以得到符合要求的密码
     * @param account
     * @param len
     * @param charTypeNum
     * @param diffCharNum
     * @return
     */
    public static final String generatePassword(String account, int len, int charTypeNum, int diffCharNum) {
        String pwd = null;
        
        for(int i = 0; i < 10; i++) {
            pwd = generatePassword(len);
            if(isValidPassword(account, pwd, len, len, charTypeNum, diffCharNum) == RetCode.OK) {
                return pwd;
            }
        }
        return pwd;
    }
    
    private static final char[] verifyCodeChars = new char[] {
        'A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z',
        'a','b','c','d','e','f','h','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z',
        '2','3','4','5','6','7','8'
    };
    
    public static final String generateVerifyCode(int len) {
        String code = "";
        int num = verifyCodeChars.length;
        for(int i = 0; i < len; i++) {
            code += verifyCodeChars[rand.nextInt(num)];
        }
        return code;
    }
    
    private static final Font DEFAULT_FONT = new Font("System", Font.PLAIN, 1);

    private static final int getRGB(int v, int r) {
        int vv = v + rand.nextInt(r);
        if(vv > 255) {
            return 255;
        } 
        return vv;
    }
    
    public static final void printVerifyCode(OutputStream out, String code,
        int width, int height, int lineNum, int lineWeight) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);    
        Graphics2D g = image.createGraphics();

        int num = code.length();
        
        int w = width / num - 1;
        int halfH = height >> 1;
        int halfW = width >> 1;
        int fontSize = halfH;
        int fontStyle = Font.PLAIN;
        if(fontSize < 16) {
            fontSize = 16;
            fontStyle = Font.BOLD;
        }
        AffineTransform at = g.getTransform(); 

        int x0 = w >> 2, y0 = halfH + (fontSize >> 1);

        //画背景
        g.setColor(Color.WHITE);    
        g.fillRect(0, 0, width, height);
        
        g.setColor(new Color(getRGB(120, 50), getRGB(120, 50), getRGB(120, 50), 150));    
        g.fillArc(rand.nextInt(x0), rand.nextInt(fontSize), rand.nextInt(halfW) + (halfW >> 1), rand.nextInt(halfH) + (halfH >> 1), 0, 360);
        g.setColor(new Color(getRGB(100, 50), getRGB(100, 50), getRGB(100, 50), 150));    
        g.fillArc(halfW + rand.nextInt() % w, halfH + rand.nextInt()%fontSize, rand.nextInt(halfW) + halfW, rand.nextInt(halfH) + halfH, 0, 360);
        
        //输出大字符
        g.setFont(DEFAULT_FONT.deriveFont(fontStyle, fontSize));    
        for(int i = 0; i < num; i++) {
            g.setTransform(at);
            g.setColor(new Color(getRGB(20, 80), getRGB(20, 80), getRGB(20, 80)));
            g.rotate((rand.nextInt() % 30) * Math.PI / 180, x0, y0); 
            g.drawString(code.substring(i, i + 1), x0, y0);
            x0 += w;
        }

        g.setTransform(at);
        if(fontSize >= 24) {
            //输出干扰小字符
            num *= 3;
            String smallCode = generateVerifyCode(num);
            g.setFont(DEFAULT_FONT.deriveFont(Font.PLAIN, fontSize >> 1));    
            for (int i = 0 ; i < num; i++){    
                g.setColor(new Color(getRGB(80, 80), getRGB(80, 80), getRGB(80, 80)));    
                x0 = rand.nextInt(width);    
                y0 = rand.nextInt(height);    
                g.drawString(smallCode.substring(i, i + 1), x0, y0);
            }
        }
        
        int[] xPoints = new int[] {0, width>>2, width>>1, (width>>1)+(width>>2), width};
        int[] yPoints = new int[5];
        int flag;
        
        //输出干扰折线
        for(int i = 0; i < lineNum; i++) {
            g.setStroke(new BasicStroke(lineWeight));
            for(int j = 0; j < 5; j++) {
                flag = ((i & 0x01) == 0 ? 1 : -1) * ((j & 0x01) == 0 ? 1 : -1);
                yPoints[j] = halfH;
                if(flag > 0) {
                    yPoints[j] += rand.nextInt(halfH);
                } else {
                    yPoints[j] -= rand.nextInt(halfH);
                }
            }
            g.setColor(new Color(getRGB(50, 80), getRGB(50, 80), getRGB(50, 80)));
            g.drawPolyline(xPoints, yPoints, xPoints.length);
            if(lineWeight > 1) {
                lineWeight--;
            }
        }
    
        g.dispose();
        //ImageIO需要写tmpdir，当此目录不存在时，会发生io异常
        ImageIO.write(image, "JPEG", out);    
        
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        encoder.encode(image); 
    }
}
