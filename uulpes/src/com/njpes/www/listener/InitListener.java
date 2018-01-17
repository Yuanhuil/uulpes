package com.njpes.www.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.njpes.www.constant.Constants;
import com.njpes.www.utils.PropertiesUtil;

public class InitListener implements ServletContextListener {

    public static final String vendorCode = new String(
            "SeNG1zqFHw1K06wLoBQAXRpp1yjxAFMFiQVKl6a6/SlXqSevMfh8kI0ELE8BBh8sXv7Ivpl1R05kn3d5QU8ejFkZkoTCqPRK/3FaIkCo3yUjOYO6BIyF2gAE/9ICK51N19gctLJYTqfFPd0+DftZ/d2YsYaO88NAiauOX2sco90F3i20QxVVoCk5JPY6CMlPqKSIdMBPjFnlyo82683fgi3tWanFty1w2EhbY15Xe+kYQFqGBAXF5xTKERDjq4xKdwuftdOtHWBbnrSGKzD4AZdlCjiz+x/yLa8igmanzzHE1zXMrprFB7cx+m4HGzXbrX2/wjOMFYGns+DouALvXEhpvEN66n2Q9jTi3E/4G85UumFMShowa06lWeQV296u2fSWg9+/oNM3idnC+4Kc+02DrJOxIu5RQfuxhvXbB/CAbdoa1CZGwaJowrXEd0abOen5iiGKMrY7yRQNlskqRUXBM9GMxhlGxuSzEU9qeI/FBf735r5+HcotiJbT7316/Jk8aV0qeoaNftDhVWflMvDZ+BFtqKDJX7tjlzDqQqwM00ZhroofkpEd/f19w9EkkbQiYxviJvreAWZZEUgLjeZBdw7O7GpULOcULboaqM/Lpc6r/p3NOE3KQKymJrkj0g+a5+M1rgRB2YstmVZnsLtjwLNDxv+25xh7DtVePd29pn00EBq6wMNL1NsBMxep6xgt2DzTNjUzv2n1xRkrok6Tlc37vqZNGrd3haacdDNm2qYdmyOerTgGgZkaYGHRm0JhbaktMpCSkz2AU5pFiAfYsBl2Dz41DN4/emLrQn9Tfin2EQhEYjY58h6pBzHfgIVTr74rrib4Ox50VEPvc8wDTj243whVyxMEC8g745I6NC9oHyQkT8LcDNPr/fjTdWFel77swgEf8j7tD5PL9ej7/J7pdHa8Yc+Bn1cK5JbEPRRaekVIJUi2PWMmPrwWOIoe6yZFQgKrtWOFAMVsew==");

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        PropertiesUtil p = new PropertiesUtil("resource/application.properties");

        Constants.UPLOAD_SERVER_PATH = p.readProperty("upload.file.server.path");
        Constants.READ_FILE_SERVER_URL = p.readProperty("read.file.server");
        Constants.APPLICATION_USERLEVEL = p.readProperty("application.userlevel");
        Constants.APPLICATION_PROVINCECODE = p.readProperty("application.provincecode");
        Constants.APPLICATION_CITYCODE = p.readProperty("application.citycode");
        Constants.APPLICATION_APPHEADTITLE = p.readProperty("application.appheadtitle");
        Constants.APPLICATION_APPFOOTER = p.readProperty("application.appfooter");

        /*------------------------------------------------------------
        
         Dog curDog = new Dog(Dog.DOG_DEFAULT_FID);
         DogApiVersion version = curDog.getVersion(vendorCode);
         int status = version.getLastError();
        switch (status) {
        case DogStatus.DOG_STATUS_OK:
        	break;
        case DogStatus.DOG_NO_API_DYLIB:
        	System.out.println("缺少动态链接库文件");
        	System.exit(0);
        	return;
        case DogStatus.DOG_INV_API_DYLIB:
        	System.out.println("动态链接库文件损坏");
        	System.exit(0);
        	return;
        default:
        	System.out.println("未知错误");
        	System.exit(0);
        	return;
        }
        System.out.println("API Version: " + version.majorVersion() + "." 
                + version.minorVersion()
                + "." + version.buildNumber() + "\n");
        
        curDog.login(vendorCode);
        status = curDog.getLastError();
        
        switch (status) {
        case DogStatus.DOG_STATUS_OK:
        	System.out.println("OK");
        	break;
        case DogStatus.DOG_FEATURE_NOT_FOUND:
        	System.out.println("no SuperDog DEMOMA key found");
        	System.exit(0);
        	break;
        case DogStatus.DOG_NOT_FOUND:
        	System.out.println("没有插入授权的加密狗");
        	System.exit(0);
        	break;
        case DogStatus.DOG_INV_VCODE:
        	System.out.println("开发公司授权码不正确");
        	System.exit(0);
        	break;
        case DogStatus.DOG_LOCAL_COMM_ERR:
        	System.exit(0);
        	System.out.println("异常");
        	System.exit(0);
        	break;
        default:
        	System.out.println("加密狗登录错误");
        	System.exit(0);
        } */
        // -------------------------------------------------------------
    }

}
