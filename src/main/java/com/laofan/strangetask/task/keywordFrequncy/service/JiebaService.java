package com.laofan.strangetask.task.keywordFrequncy.service;

import com.laofan.strangetask.task.keywordFrequncy.entity.Article;
import com.laofan.strangetask.task.keywordFrequncy.entity.Frequency;
import com.laofan.strangetask.task.keywordFrequncy.repository.FrequencyRepository;
import com.laofan.strangetask.task.keywordFrequncy.utils.JiebaUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author:pan le
 * Date:2019/7/19
 * Time:11:17
 */
@Service
public class JiebaService {

    private final FrequencyRepository frequencyRepository;

    public String basePath = JiebaService.class.getResource("").getPath();
    static String string = "前不久，朋友因健康问题需住院治疗。陪床期间，我经历了这样一幕：隔壁床病人是一位60多岁的妇女，被推进来的时候，医生嘱咐家属，病人术后身体虚弱，" +
            "需要静养。陪护她的是女儿和10岁的外孙女，听医嘱的时候，鸡啄米似的点头。可是到了晚上，画风突变。母女竟然一起玩起了直播！手机声音开的巨大，女孩口中念念有词，配合着肢体运动，" +
            "卖力地表演，妈妈则在一旁帮忙录制，还严格地「提示」女儿，动作怎样更规范、更好看……";

    public JiebaService(FrequencyRepository frequencyRepository) {
        this.frequencyRepository = frequencyRepository;
    }

    String fileName = "C:\\Users\\sunwukong\\Desktop\\new 1.txt";

    public void frequency() throws FileNotFoundException, UnsupportedEncodingException {
        List<String> list = JiebaUtils.stringFromPython(fileName);
        Map<String, Long> collect = list.stream().distinct().collect(Collectors.toMap(str -> str, s -> list.stream().filter(s::equals).count()));
        List<Frequency> frequencies = new ArrayList<>();
        Frequency frequency = new Frequency();
        Article artcle = new Article();
        for (String c : collect.keySet()) {
            frequency = Frequency.builder()
                    .keyword(c)
                    .frequency(collect.get(c))
                    .articles(null)
                    .build();
            frequencies.add(frequency);
        }
        frequencyRepository.saveAll(frequencies);


    }

}
