package com.laofan.strangetask.task.keywordFrequncy.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/7/31
 * Time:9:58
 */
@Service
public class AliYunNLP {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
//        File file = new File("C:\\Users\\sunwukong\\Desktop\\new1.txt");
//        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
//        BufferedReader bufferedReader = new BufferedReader(streamReader);
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String s : bufferedReader.lines().collect(Collectors.toList())) {
//            stringBuilder = stringBuilder.append(s);
//        }
//        //每次调用只使用其中的1024个字节数大小的文件
//        String s1 = stringBuilder.toString();
//        int length = s1.length();
//        for (int i = 0; i < length / 300; i++) {
//            String substring = s1.substring(i * 300, (i + 1) * 300);
//
//
//            String accessKeyId = "LTAIJLJesqvyXNfn";
//            String accessKeySecret = "GVkfmSYNKA7zF5yapWexWcmtIkCcRY";
//            try {
//                DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Nlp", "nlp.cn-shanghai.aliyuncs.com");
//                IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret);
//                IAcsClient client = new DefaultAcsClient(profile);
//                String s = stringBuilder.toString();
//                String postBody = "{\n"
//                        + "  \"text\": \"" + substring + "\",\n"
//                        + "  \"lang\": \"ZH\"\n" + "}";
//                CommonRequest request = new CommonRequest();
//                // 必须设置domain
//                request.setDomain("nlp.cn-shanghai.aliyuncs.com");
//                // 设置所要请求的API路径
//                request.setUriPattern("/nlp/api/wordpos/general");
//                // 设置请求方式，目前只支持POST
//                request.setMethod(MethodType.POST);
//                // 设置请求内容以及格式
//                request.setHttpContent(postBody.getBytes(), "utf-8", FormatType.JSON);
//                request.putHeadParameter("x-acs-signature-method", "HMAC-SHA1");
//                // 设置请求唯一码，防止网络重放攻击
//                request.putHeadParameter("x-acs-signature-nonce", UUID.randomUUID().toString());
//                request.setVersion("2018-04-08");
//                // 请求并获取结果
//                CommonResponse response = client.getCommonResponse(request);
//                System.out.println(response.getData());
//            } catch (ClientException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
