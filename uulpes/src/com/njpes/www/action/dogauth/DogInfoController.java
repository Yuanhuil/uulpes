package com.njpes.www.action.dogauth;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.DogEntity;
import com.superdog.auth.Authentication;

@Controller
@RequestMapping(value = "/doginfo")
public class DogInfoController extends BaseController {

    @RequestMapping(value = "/getconfiginfo", method = RequestMethod.POST)
    @ResponseBody
    public DogEntity getconfiginfo(HttpServletRequest request) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            String authcodefilename = request.getSession().getServletContext().getRealPath("/")
                    + "/WEB-INF/auth_code.xml";
            String factorfilename = request.getSession().getServletContext().getRealPath("/")
                    + "/WEB-INF/auth_code.xml";
            // Open and parse the .xml
            File file = new File(authcodefilename);
            Document doc = db.parse(file);
            NodeList nl = doc.getElementsByTagName("dogauth");
            Element elAuth = (Element) nl.item(0);

            // Get the attribute of "vendor" element
            Element elVendorID = (Element) elAuth.getElementsByTagName("vendor").item(0);
            String sID = elVendorID.getAttribute("id");

            // Get node value of "initdata"
            Node elData = elAuth.getElementsByTagName("authcode").item(0);
            String sData = elData.getFirstChild().getNodeValue();

            // Open and parse the .xml
            file = new File(factorfilename);
            doc = db.parse(file);
            nl = doc.getElementsByTagName("dogauth");
            elAuth = (Element) nl.item(0);

            // Get node value of "factor"
            String sFactor = null;
            Node elFactor = elAuth.getElementsByTagName("factor").item(0);
            if (elFactor == null) {
                sFactor = "00000000";
            } else {
                sFactor = elFactor.getFirstChild().getNodeValue();
            }
            String dog = Authentication.getDog();

            DogEntity entity = new DogEntity();
            if (dog != null) {
                entity.setDogid(Integer.parseInt(dog));
            }
            entity.setVendorID(sID);
            entity.setAuthCode(sData);
            entity.setFactor(sFactor);
            return entity;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/doAuth", method = RequestMethod.POST)
    @ResponseBody
    public String doAuth(HttpServletRequest request, int dogid, String result, String chal) {
        int iVendorID = 0;
        String sFactor;

        DogEntity dogentity = getconfiginfo(request);
        iVendorID = Integer.parseInt(dogentity.getVendorID());
        sFactor = dogentity.getFactor();

        int ret = Authentication.verifyDigest(iVendorID, dogid, chal, result, sFactor);
        if (ret == 0)
            return "0";
        else
            return Integer.toString(ret);
    }
}
