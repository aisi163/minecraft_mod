/*
MIT License

Copyright (c) 2026 aisi163

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.aisi163.npc_builder.NPCEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MinecraftContentManager {
    private static final List<String> MINECRAFT_TIPS = new ArrayList<>();
    private static final List<String> MINECRAFT_POEMS = new ArrayList<>();
    private static final List<String> LIFE_CONSOLATIONS = new ArrayList<>();

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    static {
        MINECRAFT_TIPS.add("§a 挖钻石时记得带水桶，小心掉进岩浆毁了辛苦找到的钻石！");
        MINECRAFT_TIPS.add("§a 晚上插火把不仅防怪，还能阻止刷怪笼生成怪物哦～");
        MINECRAFT_TIPS.add("§a 前期没食物？打几只鸡，生鸡肉烤了比生吃回复更多饱食度！");
        MINECRAFT_TIPS.add("§a 遇到苦力怕别慌，退到开阔处，它爆炸前有3秒倒计时！");
        MINECRAFT_TIPS.add("§a 羊毛可以用剪刀剪羊获得，不用杀羊，可持续发展～");
        MINECRAFT_TIPS.add("§a 熔炉烧东西时，用木炭当燃料和煤炭效果一样，不用省煤炭！");
        MINECRAFT_TIPS.add("§a 下雨天钓鱼成功率更高，还能钓到附魔书哦～");
        MINECRAFT_TIPS.add("§a 僵尸村民用虚弱药水+金苹果能治愈，还能换绿宝石！");
        MINECRAFT_TIPS.add("§a 挖黑曜石必须用钻石镐，其他镐子挖不动的～");
        MINECRAFT_TIPS.add("§a 末影珍珠右键能瞬移，但会扣2点血，记得保持血量！");
        MINECRAFT_TIPS.add("§a 腐肉可以喂狼，不用浪费，还能提升亲密度～");
        MINECRAFT_TIPS.add("§a 种小麦时记得耕地，骨粉催熟能快速收获！");
        MINECRAFT_TIPS.add("§a 用胡萝卜喂猪，繁殖后能获得更多猪肉，实现肉食自由～");
        MINECRAFT_TIPS.add("§a 蜘蛛眼可以酿造药水，别随便丢弃哦～");
        MINECRAFT_TIPS.add("§a 打火石点燃的地狱门，站进去就能前往下界冒险～");
        MINECRAFT_TIPS.add("§a 用雪球攻击烈焰人，能快速击败它获取烈焰棒～");
        MINECRAFT_TIPS.add("§a 末影螨会被末影珍珠召唤，血量低但移动快，小心应对～");

        MINECRAFT_TIPS.add("§9 用楼梯和半砖装饰建筑，能让细节瞬间变丰富！");
        MINECRAFT_TIPS.add("§9 玻璃和彩色玻璃搭配，能做出超美的落地窗～");
        MINECRAFT_TIPS.add("§9 栅栏和栅栏门是绝配，既美观又能防怪进入基地！");
        MINECRAFT_TIPS.add("§9 用不同材质的台阶铺地板，层次感拉满～");
        MINECRAFT_TIPS.add("§9 雪块和冰搭配能做出冰雪城堡，超有氛围感！");
        MINECRAFT_TIPS.add("§9 活板门可以当窗户的遮挡板，实用又好看～");
        MINECRAFT_TIPS.add("§9 用磨制安山岩做建筑外墙，工业风拉满！");
        MINECRAFT_TIPS.add("§9 书架不仅能附魔，还能当装饰，书房必备～");
        MINECRAFT_TIPS.add("§9 地毯铺在台阶上，能做出地毯楼梯的效果哦～");
        MINECRAFT_TIPS.add("§9 用海晶灯当室内照明，亮度高还不刺眼！");
        MINECRAFT_TIPS.add("§9 石英块做的装饰墙，简约又大气～");
        MINECRAFT_TIPS.add("§9 用栅栏做阳台栏杆，安全又美观～");
        MINECRAFT_TIPS.add("§9 圆石搭配木板，能做出复古风的建筑外墙～");
        MINECRAFT_TIPS.add("§9 末地石做的装饰，充满异域风情～");

        MINECRAFT_TIPS.add("§5 红石中继器不仅能延时，还能锁存红石信号哦！");
        MINECRAFT_TIPS.add("§5 比较器可以检测箱子里的物品数量，做自动售货机超实用～");
        MINECRAFT_TIPS.add("§5 红石火把熄灭时会输出信号，反向使用有奇效！");
        MINECRAFT_TIPS.add("§5 发射器可以发射箭、药水，甚至刷怪蛋，玩法超多～");
        MINECRAFT_TIPS.add("§5 活塞推不动黑曜石、基岩和附魔台，别白费力气啦～");
        MINECRAFT_TIPS.add("§5 阳光传感器白天输出信号，晚上关闭，做自动路灯超棒！");
        MINECRAFT_TIPS.add("§5 红石粉最多能传15格，超过要加中继器哦～");
        MINECRAFT_TIPS.add("§5 漏斗能自动传输物品，连接箱子和熔炉超方便！");
        MINECRAFT_TIPS.add("§5 测重压力板能检测物品数量，做自动分类机必备～");
        MINECRAFT_TIPS.add("§5 绊线钩配合线，能做隐形的陷阱，防熊孩子超好用！");
        MINECRAFT_TIPS.add("§5 红石比较器的减法模式，能精准控制信号强度～");
        MINECRAFT_TIPS.add("§5 观察者能检测方块变化，做自动收割农场超实用～");
        MINECRAFT_TIPS.add("§5 1.12.2的命令方块新增了许多指令，能实现更复杂的自动化～");

        MINECRAFT_TIPS.add("§6 末地城的潜影贝会掉落鞘翅，记得小心应对，别被击飞！");
        MINECRAFT_TIPS.add("§6 下界堡垒有烈焰人刷怪笼，掉落的烈焰棒是酿造的关键～");
        MINECRAFT_TIPS.add("§6 丛林神庙里有隐藏的红石陷阱，踩压力板会射箭哦！");
        MINECRAFT_TIPS.add("§6 沙漠神殿的宝箱下有TNT，挖的时候记得先拆陷阱～");
        MINECRAFT_TIPS.add("§6 沉船里的藏宝图能找到埋藏的宝藏，奖励超丰富！");
        MINECRAFT_TIPS.add("§6 蘑菇岛没有怪物，是建基地的绝佳地点～");
        MINECRAFT_TIPS.add("§6 废弃矿井里有铁轨和宝箱，但小心洞穴蜘蛛！");
        MINECRAFT_TIPS.add("§6 要塞里的末地传送门需要12个末影之眼激活～");
        MINECRAFT_TIPS.add("§6 冰刺平原的冰可以做冰道，赶路超快速！");
        MINECRAFT_TIPS.add("§6 恶地的红砂岩，是做红色系建筑的好材料～");
        MINECRAFT_TIPS.add("§6 林地府邸的唤魔者会召唤恼鬼，击败后能获得不死图腾～");
        MINECRAFT_TIPS.add("§6 1.12.2的彩色黏土块，能做出超丰富的建筑配色～");

        MINECRAFT_TIPS.add("§d 对着哞菇使用碗能获得蘑菇煲，饿肚子时的好帮手～");
        MINECRAFT_TIPS.add("§d 骨粉催熟的小麦可以快速获得面包，前期超实用！");
        MINECRAFT_TIPS.add("§d 胡萝卜和马铃薯种在耕地上，成熟后能做炖菜哦～");
        MINECRAFT_TIPS.add("§d 用小麦喂牛/羊/猪，能繁殖后代，实现食物自由！");
        MINECRAFT_TIPS.add("§d 南瓜成熟后可以做南瓜派，回复饱食度超划算～");
        MINECRAFT_TIPS.add("§d 甘蔗只能种在水边，是做纸和书的必备材料～");
        MINECRAFT_TIPS.add("§d 可可豆种在丛林木上，能做曲奇，还能给羊驼染色～");
        MINECRAFT_TIPS.add("§d 鹦鹉会模仿周围怪物的声音，还能站在你的肩膀上～");
        MINECRAFT_TIPS.add("§d 用甜菜根喂兔子，能快速繁殖，获得更多兔子肉～");
        MINECRAFT_TIPS.add("§d 西瓜种在耕地上，成熟后能切西瓜片，解渴又管饱～");
        MINECRAFT_TIPS.add("§d 1.12.2的彩色羊毛，能做出超丰富的装饰地毯～");

        MINECRAFT_TIPS.add("§e 按F3+H能显示物品ID，找材料超方便！");
        MINECRAFT_TIPS.add("§e 用命名牌给生物改名，它们就不会消失啦～");
        MINECRAFT_TIPS.add("§e 附魔台周围放书架，能提升附魔等级上限哦～");
        MINECRAFT_TIPS.add("§e 铁砧可以修复附魔装备，还能合并附魔效果～");
        MINECRAFT_TIPS.add("§e 信标需要钻石/金/铁/绿宝石块激活，能提供增益效果～");
        MINECRAFT_TIPS.add("§e 钓鱼竿能勾住物品和生物，赶路时勾船超好玩～");
        MINECRAFT_TIPS.add("§e 按F3+G能显示区块边界，建基地时超实用～");
        MINECRAFT_TIPS.add("§e 用盾牌能格挡大部分近战攻击，生存必备～");
        MINECRAFT_TIPS.add("§e 附魔“经验修补”的装备，能靠经验自动修复～");
        MINECRAFT_TIPS.add("§e 鞘翅搭配烟花，能实现高空飞行，超炫酷～");
        MINECRAFT_TIPS.add("§e 1.12.2的附魔“冰霜行者”，能在水面上行走不破冰～");
        MINECRAFT_TIPS.add("§a 挖钻石记得带水桶，小心岩浆！");
        MINECRAFT_TIPS.add("§a 晚上插火把能有效防止刷怪～");

        MINECRAFT_POEMS.add("§f 方块筑天地，一剑破苍茫，此界皆吾乡。");
        MINECRAFT_POEMS.add("§f 火把照长夜，锄犁垦大荒，悠然见星光。");
        MINECRAFT_POEMS.add("§f 红石通千机，匠心造万象，此间有华章。");
        MINECRAFT_POEMS.add("§f 下界焚烈焰，末地逐流光，踏遍四方疆。");
        MINECRAFT_POEMS.add("§f 一镐开山石，一庐藏暖阳，清风伴酒香。");
        MINECRAFT_POEMS.add("§f 麦田随风起，牛羊绕屋旁，此间岁月长。");
        MINECRAFT_POEMS.add("§f 附魔凝神力，安山铸高墙，何惧夜未央。");
        MINECRAFT_POEMS.add("§f 舟行碧波上，剑指白云乡，天地任徜徉。");
        MINECRAFT_POEMS.add("§f 雪落冰原静，花开丛林香，步步皆风光。");
        MINECRAFT_POEMS.add("§f 末影寻踪迹，烈焰照前方，勇者不彷徨。");
        MINECRAFT_POEMS.add("§f 垒石成楼阁，引泉入池塘，悠然自疏狂。");
        MINECRAFT_POEMS.add("§f 钓罢江上月，耕罢陇上桑，心闲意自扬。");
        MINECRAFT_POEMS.add("§f 斩尽途中怪，收尽陌上粮，归途满星光。");
        MINECRAFT_POEMS.add("§f 红石连阡陌，机关藏四方，巧思胜天工。");
        MINECRAFT_POEMS.add("§f 登高望远阔，临渊看潮涨，此心向大荒。");
        MINECRAFT_POEMS.add("§f 采得三秋实，酿成一壶浆，快意慰风尘。");
        MINECRAFT_POEMS.add("§f 剑扫群魔散，镐开万石扬，豪气贯穹苍。");
        MINECRAFT_POEMS.add("§f 筑舍依山住，寻幽向水行，人间有清宁。");
        MINECRAFT_POEMS.add("§f 星垂平野阔，火照夜途明，步步踏歌行。");
        MINECRAFT_POEMS.add("§f 一界一乾坤，一方一世界，此间乐无穷。");
        MINECRAFT_POEMS.add("§f 方块筑天地，一剑破苍茫，此界皆吾乡。");
        MINECRAFT_POEMS.add("§f 火把照长夜，锄犁垦大荒，悠然见星光。");

        LIFE_CONSOLATIONS.add("§e 现实里也要好好休息，别太累啦。");
        LIFE_CONSOLATIONS.add("§a 愿你生活顺遂，万事都能顺心如意。");
        LIFE_CONSOLATIONS.add("§e 不管学习还是工作，都要照顾好自己呀。");
        LIFE_CONSOLATIONS.add("§a 累了就歇一歇，游戏和生活都要开心。");
        LIFE_CONSOLATIONS.add("§e 愿你每天都有小幸运，烦恼少一点。");
        LIFE_CONSOLATIONS.add("§a 三餐要按时吃，身体才是最重要的。");
        LIFE_CONSOLATIONS.add("§e 生活难免有小波折，一切都会慢慢好起来。");
        LIFE_CONSOLATIONS.add("§a 保持好心情，你超棒的，继续加油！");
        LIFE_CONSOLATIONS.add("§e 愿你所盼皆如愿，所行皆坦途。");
        LIFE_CONSOLATIONS.add("§a 不管多忙，都要留一点时间给自己。");
        LIFE_CONSOLATIONS.add("§e 天黑有灯，下雨有伞，生活有人挂念。");
        LIFE_CONSOLATIONS.add("§a 不必焦虑，慢慢来，你会越来越好。");
        LIFE_CONSOLATIONS.add("§e 开心就笑，累了就睡，生活简单就好。");
        LIFE_CONSOLATIONS.add("§a 愿你日子清净，抬头皆是温柔。");
        LIFE_CONSOLATIONS.add("§e 生活或许平凡，但你永远值得被善待。");
        LIFE_CONSOLATIONS.add("§a 所有努力都不会白费，未来一定闪闪发光。");
        LIFE_CONSOLATIONS.add("§e 少一点烦恼，多一点快乐，平安就好。");
        LIFE_CONSOLATIONS.add("§a 愿你生活温暖，内心丰盈，岁岁常安。");
        LIFE_CONSOLATIONS.add("§e 不管遇到什么，都要记得照顾好情绪。");
        LIFE_CONSOLATIONS.add("§a 祝你游戏愉快，生活更愉快，万事皆胜意。");
    }


    public static String getRandomTip() {
        if (MINECRAFT_TIPS.isEmpty()) {
            return "§f 探索世界，总能发现新惊喜！"; // 兜底句子
        }
        int randomIndex = RANDOM.nextInt(MINECRAFT_TIPS.size());
        return MINECRAFT_TIPS.get(randomIndex);
    }

    public static String getRandomPoem() {
        if (MINECRAFT_POEMS.isEmpty()) {
            return "§f 方块绘山河，此间皆星河。"; // 兜底句子
        }
        int randomIndex = RANDOM.nextInt(MINECRAFT_POEMS.size());
        return MINECRAFT_POEMS.get(randomIndex);
    }

    public static String getRandomConsolation() {
        return LIFE_CONSOLATIONS.get(RANDOM.nextInt(LIFE_CONSOLATIONS.size()));
    }

    public static String getRandomContent() {
        int type = RANDOM.nextInt(3);
        if (type == 0) return getRandomTip();
        else if (type == 1) return getRandomPoem();
        else return getRandomConsolation();
    }

}