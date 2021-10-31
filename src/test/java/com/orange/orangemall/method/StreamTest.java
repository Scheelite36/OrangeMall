package com.orange.orangemall.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Scheelite
 * @date 2021/10/6
 * @email jwei.gan@qq.com
 * @description 测试java stream编程
 **/
public class StreamTest {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("abc", "", "abc", "efg", "abcd", "", "jkl", "dsf", "", "gjw", "persimmon");
        // 去掉为空的字符串，保持原有顺序 使用串行流
        List<String> filteredString = strings.stream()
                .filter(string -> !string.isEmpty()).collect(Collectors.toList());
        // 分别打印
        filteredString.forEach(System.out::println);
        System.out.println("-------------");

        // 统计为空字符串的数量 可以使用并行流
        Long count = strings.parallelStream().filter(String::isEmpty).count();
        System.out.println(count);

        // 删除空字符串，用逗号将所有元素合并起来
        String combinedStrings = strings.stream().filter(string -> !string.isEmpty()).
                collect(Collectors.joining(","));
        System.out.println(combinedStrings);

        // 将元素去重去空字符 前后加上减号输出
        List<String> handledStrings = strings.stream().filter(string -> !string.isEmpty()).distinct()
                .map(string -> "-" + string + "-").collect(Collectors.toList());
        handledStrings.forEach(System.out::println);

        // 获取十个5以内的随机整数排序并平方处理后输出
        Random random = new Random();
        random.ints(10, 0, 5)
                .map(x -> x * x).forEach(System.out::println);
        System.out.println(strings.toString());
    }
}
