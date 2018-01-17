package edutec.scale.digester;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StopWatch;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edutec.scale.exception.DimensionException;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.Scale;
import heracles.digester.DigesterUtils;
import heracles.util.OrderValueMap;
import jodd.io.StringInputStream;
import net.sf.json.JSONObject;

public class ScaleBuilder {
    static private final Log logger = LogFactory.getLog(ScaleBuilder.class);

    static public Scale doBuild(InputStream input, Scale scale) throws IOException, SAXException, DimensionException {
        StopWatch stopWatch = new StopWatch("ScaleBuilder");
        stopWatch.start("doBuild");
        Digester digester = new Digester();
        QuestionBuilder qb = QuestionBuilder.newInstance(digester);
        DimensionBuilder db = DimensionBuilder.newInstance(digester);

        if (scale == null)
            digester.addObjectCreate("scale", Scale.class);
        else
            digester.push(scale);

        DigesterUtils.addBeanPropertySetters(digester, new String[] { "scale/title", "scale/id", "scale/descn",
                "scale/category", "scale/guidance", "scale/testType", "scale/source", "scale/scaleType",
                "scale/warningOrNot", "scale/applicablePerson", "scale/reportGraph", "scale/reportImageParam1",
                "scale/reportImageParam2", "scale/questions/reversequestions", "scale/questions/questiontype0",
                "scale/questions/questiontype1", "scale/questions/questiontype2", "scale/questions/questiontype3" });

        digester.addSetProperties("*/questions");

        /* 问题信息 */
        qb.buileQuestion();
        /* 维度信息 */
        db.buildDimension();

        Scale s = (Scale) digester.parse(input);
        // QuestionDesc.postScaleProcess(s);//这里暂时注释掉，赵万锋
        s.mountDimensions();

        stopWatch.stop();
        logger.info(s.getTitle() + "-" + s.getQuestionSize());
        logger.info(stopWatch.prettyPrint());
        return s;
    }

    static public Scale doInvestBuild(InputStream input, Scale scale)
            throws IOException, SAXException, DimensionException {
        StopWatch stopWatch = new StopWatch("ScaleBuilder");
        stopWatch.start("doBuild");
        Digester digester = new Digester();
        QuestionBuilder qb = QuestionBuilder.newInstance(digester);

        if (scale == null)
            digester.addObjectCreate("scale", Scale.class);
        else
            digester.push(scale);

        DigesterUtils.addBeanPropertySetters(digester,
                new String[] { "scale/title", "scale/id", "scale/descn", "scale/category", "scale/guidance",
                        "scale/descn", "scale/questions/questiontype0", "scale/questions/questiontype1",
                        "scale/questions/questiontype2", "scale/questions/questiontype3" });

        digester.addSetProperties("*/questions");

        /* 问题信息 */
        qb.buileQuestion();
        Scale s = (Scale) digester.parse(input);
        // QuestionDesc.postScaleProcess(s);//这里暂时注释掉，赵万锋
        stopWatch.stop();
        logger.info(s.getTitle() + "-" + s.getQuestionSize());
        logger.info(stopWatch.prettyPrint());
        return s;
    }

    public static void main(String[] args) {
        // String s = "<?xml version=\"1.0\"?>"+
        // "<scale>"+
        // "<id>11</id>"+
        // "<title>小学生情绪适应问卷</title>"+
        // "<descn>《小学生心理健康检测系统》是全国教育科学“八五”、“九五”、“十五”规划教育部重点课题研究成果，由北京师范大学心理学院郑日昌教授主持，历经15年完成。有35位心理学专家参与本系统的研究工作，其中22位拥有高级职称和博士学位。"+
        // "《小学生情绪适应问卷》是《小学生心理健康检测系统》14个量表中的一个，用于测量小学生的情绪适应水平，量表具有良好的信度和效度。"+
        // "情绪适应能力是一种社会智力，主要是指认识和表达个人情绪、调整或改变自己的不良情绪、设身处地地体会他人情绪并与之进行良性情感交流的能力。Goleman在《情绪智力》一书中指出：预测一个人未来成就的高低，与其测量他在传统智力测验及标准化成就测验上的表现，倒不如观察与他生活各层面息息相关的“情商”来得实用和有效。</descn>"+
        // "<guidance>请你仔细阅读每一个题目，然后根据你的实际情况选择合适的答案，请以自己的第一感觉为准，不必过多思考。"+
        // "例题：每天，我都看电视。"+
        // "A)完全不符合B)多数不符合C)一般/不确定D)多数符合E)完全符合"+
        // "对这个句子："+
        // "如果这种描述完全不符合你的现实生活，你根本不看电视，就选择“完全不符合”。"+
        // "如果这种描述多数不符合你的现实生活，你一周中只有一、两天看了电视，就选择“多数不符合”；"+
        // "如果你不能确定你是否每天都看电视，就选择“一般/不确定”；"+
        // "如果这种描述多数情况下符合你的现实生活，你不是每天都看电视，但一周里有四、五天看了电视，就选择“多数符合”；"+
        // "如果这种描述完全符合你的现实生活，你确实每天都看电视，就选择“完全符合”；</guidance>"+
        // "<testType>自评</testType>"+
        // "<source>国际量表</source>"+
        // "<scaleType>心理健康</scaleType>"+
        // "<applicablePerson>小学</applicablePerson>"+
        // "<reportGraph>柱-2边</reportGraph>"+
        // "<dimensions>"+
        // "</dimensions>"+
        // "<questions
        // questiontype=\"selection|A,2,完全不符合|B,3,多数不符合|C,4,一般/不确定|D,5,多数符合|E,5,完全符合\">"+
        // "<q id=\"Q1\">"+
        // "<t>我能很快意识到自己的情绪状态。</t>"+
        // "<o id=\"A\" value=\"2\">完全不符合</o>"+
        // "<o id=\"B\" value=\"3\">多数不符合</o>"+
        // "<o id=\"C\" value=\"4\">一般/不确定</o>"+
        // "<o id=\"D\" value=\"5\">多数符合</o>"+
        // "<o id=\"E\" value=\"5\">完全符合</o>"+
        // "</q>"+
        // "<q id=\"Q2\">"+
        // "<t>我生气的时候我知道。</t>"+
        // "<o id=\"A\" value=\"2\">完全不符合</o>"+
        // "<o id=\"B\" value=\"3\">多数不符合</o>"+
        // "<o id=\"C\" value=\"4\">一般/不确定</o>"+
        // "<o id=\"D\" value=\"5\">多数符合</o>"+
        // "<o id=\"E\" value=\"5\">完全符合</o>"+
        // "</q>"+
        // "</questions>"+
        // "</scale>";
        // String s = "<?xml
        // version=\"1.0\"?><scale><id>1</id><title>test</title></scale>";
        String s = "<?xml version=\"1.0\"?>" + "<scale>" + "<id>1</id>" + "<title>efq</title>" + "<descn></descn>"
                + "<guidance></guidance>" + "<testType></testType>" + "<source></source>" + "<scaleType></scaleType>"
                + "<applicablePerson></applicablePerson>" + "<reportGraph></reportGraph>" + "<dimensions>"
                + "</dimensions>" + "<questions questiontype=\"selection|0,0,feq|1,1,fq|2,2,fewq|1,1,fq\">"
                + "<q  id=\"Q0\">" + "<t>feq</t>" + "<o id=\"0\" value=\"\">feq</o>" + "<o id=\"1\" value=\"\">fq</o>"
                + "<o id=\"2\" value=\"\">fewq</o>" + "<o id=\"1\" value=\"\">fq</o>" + "</q>" + "<q  id=\"Q1\">"
                + "<t>feq</t>" + "<o id=\"0\" value=\"\">feq</o>" + "<o id=\"1\" value=\"\">fq</o>"
                + "<o id=\"2\" value=\"\">fewq</o>" + "<o id=\"1\" value=\"\">fq</o>" + "</q>" + "</questions>"
                + "</scale>";
        s = s.replaceAll("&", "&amp;");
        InputStream str = new StringInputStream(s);
        Scale scale = new Scale();
        try {
            ScaleBuilder.doBuild(str, scale);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SAXException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (DimensionException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // Digester digester = new Digester();
        // digester.addObjectCreate("scale", Scale.class);
        // digester.addBeanPropertySetter("scale/id", "id");
        // digester.addBeanPropertySetter("scale/title", "title");
        // digester.addBeanPropertySetter("scale/questions", "questionMap");
        // digester.addObjectCreate("scale/questions", new
        // OrderValueMap<String,SelectionQuestion>().getClass());
        // digester.addObjectCreate("scale/questions/q",
        // SelectionQuestion.class);
        // digester.addObjectCreate("scale/questions/o", Option.class);
        // DigesterUtils.addBeanPropertySetters(digester, new String[] {
        // "scale/title",
        // "scale/id",
        // "scale/descn",
        // "scale/category",
        // "scale/guidance",
        // "scale/testType",
        // "scale/source",
        // "scale/scaleType",
        // "scale/applicablePerson",
        // "scale/reportGraph",
        // "scale/questions/reversequestions",
        // "scale/questions/questiontype0",
        // "scale/questions/questiontype1",
        // "scale/questions/questiontype2",
        // "scale/questions/questiontype3"});
        //
        // digester.addSetProperties("*/questions");
        // try {
        // Scale sc = (Scale) digester.parse(str);
        // System.out.println(sc.getId());
        // System.out.println(sc.getTitle());
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (SAXException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        String[] encodings = { "UTF-8", "UTF-16", "ISO-8859-1" };
        for (String actual : encodings) {
            for (String declared : encodings) {
                if (actual != declared) {
                    // String xml = "<?xml version='1.0' encoding='" + declared
                    // + "'?><x/>";
                    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><scale><id>1</id></scale>";
                    byte[] encoded = xml.getBytes(Charset.forName(actual));
                    try {
                        SAXParserFactory.newInstance().newSAXParser().parse(new ByteArrayInputStream(encoded),
                                new DefaultHandler());
                        // System.out.println("HIDDEN ERROR! actual:" + actual +
                        // " " + xml);
                    } catch (Exception e) {
                        // System.out.println(e.getMessage() + " actual:" +
                        // actual + " xml:"
                        // + xml);
                    }
                }
            }
        }

        JSONObject scale1 = new JSONObject();
        scale1.accumulate("id", "11");
        scale1.accumulate("title", "test");
        JSONObject q = new JSONObject();
        q.accumulate("id", "1");
        q.accumulate("title", "tttt");
        q.accumulate("choice", QuestionConsts.CHOICE_SINGLE);
        JSONObject o = new JSONObject();
        o.accumulate("id", "1");
        o.accumulate("title", "6666");
        JSONObject om = new JSONObject();
        om.accumulate("1", o);
        q.accumulate("optionMap", om);
        JSONObject qm = new JSONObject();
        qm.accumulate("1", q);
        scale1.accumulate("questionMap", qm);
        Map<String, Class> classMap = new HashMap<String, Class>();

        classMap.put("questionMap", HashMap.class);
        classMap.put("optionMap", HashMap.class);
        Scale sc = (Scale) JSONObject.toBean(scale1, Scale.class, classMap);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Scale s1 = objectMapper.readValue(scale.toString(), Scale.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(scale.toString());
        System.out.println(sc.getId());
        OrderValueMap<String, Question> ti = (OrderValueMap<String, Question>) sc.getQuestionMap();
    }
}
