package com.chenjiajin.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatDeepSeekController {

    @Autowired
    private OpenAiChatModel chatModel;


    // http://localhost/ai/generate?message=你好?
    @GetMapping("/ai/generate")
    public String generate(@RequestParam(defaultValue = "Tell me a joke") String message) {
        System.err.println("问deepseek的问题: \n\t" + message);
        String result = this.chatModel.call(message);
        System.err.println("deepseek的回答 : \n\t" + result);
        return result;
    }






    // http://localhost/ai/generateSql
    @GetMapping("/ai/generateSql")
    public String generateSql(@RequestParam(defaultValue = "Tell me a joke") String message) {
        String data = "需求：" + message + "\n" +
                "表结构：\n" +
                "`order_info`表：`order_no`,`total_amount`,`create_time`\n" +
                "字段create_time是订单创建时间，数据存储格式YYYY-MM-DD HH:MM:SS\n" +
                "补充：生成的sql语句不要中文注释\n" +
                "sql语句中：订单月份数据别名order_date，订单数量别名order_count";
        String response = this.chatModel.call(data);
        System.err.println("deepseek的返回值: " + response);
        //String msg = "要查询2024年每月的订单数量，可以使用以下SQL语句：\n\n```sql\nSELECT \n    DATE_FORMAT(create_time, '%Y-%m') AS month, \n    COUNT(order_no) AS order_count\nFROM \n    order_info\nWHERE \n    YEAR(create_time) = 2024\nGROUP BY \n    DATE_FORMAT(create_time, '%Y-%m')\nORDER BY \n    month;\n```\n\n### 解释：\n1. **DATE_FORMAT(create_time, '%Y-%m')**: 将`create_time`字段格式化为`YYYY-MM`的形式，表示年和月。\n2. **COUNT(order_no)**: 统计每个月的订单数量。\n3. **WHERE YEAR(create_time) = 2024**: 过滤出2024年的订单。\n4. **GROUP BY DATE_FORMAT(create_time, '%Y-%m')**: 按年月分组，以便统计每月的订单数量。\n5. **ORDER BY month**: 按月份排序，确保结果按时间顺序排列。\n\n执行这条SQL语句后，你将得到2024年每个月的订单数量。";

        //从response获取生成sql语句
        int index = response.indexOf("sql");
        int index1 = response.indexOf(";", index);
        return response.substring(index + 3, index1).replaceAll("\n", " ");
    }


}

