package simm.learning.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import simm.learning.biz.dao.UserBiz;
import simm.learning.biz.entity.authority.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    UserBiz userBiz;

    @GetMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response){
        try {
            Map data=new HashMap();
            List<User> users = userBiz.selectAll();
            data.put("users",users);
            // 导出
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download;");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String("test.xls".getBytes("gb2312"), "ISO8859-1"));
            freeMarkerConfigurer.getConfiguration().getTemplate("excelTest.ftl").process(data,response.getWriter());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
