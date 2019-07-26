import jieba
import jieba.posseg as pseg
import sys

string = "前不久，朋友因健康问题需住院治疗。床期间，我经历了这样一幕：隔壁床病人是一位60多岁的妇女，被推进来的时候，医生嘱咐家属，病人术后身体虚弱，" \
         "需要静养 陪护她的是女儿和10岁的外孙女，听医嘱的时候，鸡啄米似的点头。可是到了晚上，画风突变。母女竟然一起玩起了直播！"
"手机声音开的巨大，女孩口中念念有词，配合着肢体运动，卖力地表演，妈妈则在一旁帮忙录制，还严格地「提示」女儿，动作怎样更规范、更好看……" \
"接连三天，画风一直持续，我和朋友被吵得不行，也不好意思大吵大闹，只能在外面走廊来回踱步，直到她们玩累了，变安静为止。" \
"后来，病人由于伤口感染，被移到高级病房，我和朋友终于迎来难得的清净。" \
"这位妈妈的做法很让人费解。" \
"作为女儿，入戏太深，完全忽略了病床上还有个需要休养的老妈；" \
"作为家长，引导错误，正带着女儿渐行渐远，越走越偏。" \
"名人卢梭曾言：「儿童第一步走向邪恶，大抵是由于他那善良的本性被人引入歧途的缘故。」" \
"如果让孩子的天真和善良，一直沉浸在直播里，就等于扼杀了孩子的未来。" \
"2" \
"同事李姐最近很苦恼，因为她发现8岁的儿子不知道什么原因，变得油腔滑调，还脏话连篇。" \
"直到有一天，她发现儿子一边兴致勃勃地看手机，一边爆着粗口，原来儿子一直在看网络直播，长期耳濡目染，学会了这些不良习惯。" \
"李姐对儿子进行批评教育后，本以为儿子会改，不曾想，儿子越来越痴迷。" \
"前几天，李姐攒的30000块钱不翼而飞，李姐以为遭遇诈骗，于是报了警。" \
"经过民警详细盘问，真相是儿子给主播刷礼物，最高一次刷了3000！这些钱，李姐攒了大半年，就这么被儿子无情「挥霍」了。" \
"深中直播剧毒的，不仅仅是李姐的儿子" \
"某视频平台上，一个小女学生对着屏幕哭诉，下面配着文字：「直播死妈——妈妈被车撞死了，去医院晚了」，利用观众的同情心博取同情，求赞。" \
"一个六岁左右的男孩，在家里喜滋滋地玩某视频平台自拍，身后换衣服的妈妈无辜躺枪，被拍进视频，视频被恶意转发近百万次，堪称史上最「坑妈」实力娃。" \
"某视频平台上，十四、五岁的女孩上传自己的怀孕视频，炫耀自己的孕照。" \
"还有一些爱慕虚荣的女大学生，为了名牌衣服化妆品，忘了自己该读书的身份，为了挣快钱，宁愿去做一些不该做的直播。" \
"这些孩子，都有一个目的：圈住更多的粉丝，获取更多的流量，成为挣钱最快的网红。" \
"有数据统计：网络视频平台里85%的用户在24岁以下，95后居多，甚至00后也在其中。" \
"这是一个很好的时代，好到很多人利用网络平台找到了自身价值；" \
"这也是一个最坏的时代，坏到下一代的价值观被歪曲到分崩离析。" \
"难以想象，如果孩子们的大脑，长期被奢靡、浮华、及时行乐的思想霸占，他们还有什么未来可言？"

# file_object = open("C:\Users\sunwukong\Desktop\new 1.txt","r")
# file_content = file_object.read()
with open('C:\\Users\\sunwukong\\Desktop\\new.txt', 'r')as f:
    data = f.read()


def function(String):
    var = jieba.cut_for_search(String)
    words = pseg.cut(String)
    l = list()
    for word in words:
        l.append(word.flag + word.word)
    print(l)
    return l


function(data)
# function(sys.argv[1])
