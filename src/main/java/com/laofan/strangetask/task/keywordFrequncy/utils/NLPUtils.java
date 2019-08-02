package com.laofan.strangetask.task.keywordFrequncy.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/7/31
 * Time:10:55
 * 阿里云词性注解
 */
@Slf4j
public class NLPUtils {

    public static List<String> keywords(File file) throws Exception{
        List<String> list = new ArrayList<>();
            String content;
            content = readFile(file);
            for (int i = 0 ; i < content.length()/300 ; i ++){
                String substring = content.substring(i * 300, (i + 1) * 300);
                // 替换其中的字符串中的空白字符和非汉字字符
                String s = substring.replaceAll("\\s*", "");
                String s1 = s.replaceAll("\\[u4e00-u9fa5]*", "");
                List<String> aliyunnlp = aliyunnlp(s1);
                list.addAll(aliyunnlp);
            }
        return list;
    }





    static List<String> aliyunnlp(String content) throws Exception{
        String accessKeyId = "LTAIJLJesqvyXNfn";
        String accessKeySecret = "GVkfmSYNKA7zF5yapWexWcmtIkCcRY";
        CommonResponse response = null;
         try {
            DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Nlp", "nlp.cn-shanghai.aliyuncs.com");
            IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);
            String postBody = "{\n"
                    + "  \"text\": \"" + content + "\",\n"
                    + "  \"lang\": \"ZH\"\n" + "}";
            CommonRequest request = new CommonRequest();
            // 必须设置domain
            request.setDomain("nlp.cn-shanghai.aliyuncs.com");
            // 设置所要请求的API路径
            request.setUriPattern("/nlp/api/wordpos/general");
            // 设置请求方式，目前只支持POST
            request.setMethod(MethodType.POST);
            // 设置请求内容以及格式
            request.setHttpContent(postBody.getBytes(), "utf-8", FormatType.JSON);
            request.putHeadParameter("x-acs-signature-method", "HMAC-SHA1");
            // 设置请求唯一码，防止网络重放攻击
            request.putHeadParameter("x-acs-signature-nonce", UUID.randomUUID().toString());
            request.setVersion("2018-04-08");
            // 请求并获取结果
            response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        String data = response.getData();
        ObjectMapper objectMapper = new ObjectMapper();
        onData data1 = objectMapper.readValue(response.getData(), onData.class);
        List<String> vv = data1.getData().stream().filter(inData -> inData.getPos().equals("VV") || inData.getPos().startsWith("N")).map(inData::getWord).collect(Collectors.toList());
        return vv;
    }

    private static String readFile(File file) throws Exception{
        StringBuilder stringBuilder = new StringBuilder();
        if (file.getName().endsWith(".doc")) {
            InputStream fis = new FileInputStream(file);
            WordExtractor wordExtractor = new WordExtractor(fis);
            for (String s : wordExtractor.getParagraphText()) {
                stringBuilder.append(s);
            }
        }
        if (file.getName().endsWith(".docx")) {
            OPCPackage opcPackage = POIXMLDocument.openPackage(file.getPath());
            XWPFDocument xwpfDocument = new XWPFDocument(opcPackage);
            for (XWPFParagraph paragraph : xwpfDocument.getParagraphs()) {
                stringBuilder.append(paragraph.getText());
            }
        }
        return stringBuilder.toString();
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class inData {
    private String pos;
    private String word;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class onData{
    private List<inData> data;
}