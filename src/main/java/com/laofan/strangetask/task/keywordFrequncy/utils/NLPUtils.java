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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/7/31
 * Time:10:55
 * 阿里云词性注解
 */
public class NLPUtils {

    public static List<String> keywords(String filesPath) throws Exception{
        File[] files = new File(filesPath).listFiles();
        for (File file : files) {
            String content;
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : bufferedReader.lines().collect(Collectors.toList())) {
                stringBuilder = stringBuilder.append(s);
            }
            content = stringBuilder.toString();
            for (int i = 0 ; i < content.length()/300 ; i ++){
                String substring = content.substring(i * 300, (i + 1) * 300);
                aliyunnlp(substring);
            }

        }
        return null;
    }

    static String aliyunnlp(String content){
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
        return data;
    }

}
