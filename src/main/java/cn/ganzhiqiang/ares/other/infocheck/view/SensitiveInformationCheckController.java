package cn.ganzhiqiang.ares.other.infocheck.view;

import cn.ganzhiqiang.ares.common.dto.WxSimpleDto;
import cn.ganzhiqiang.ares.common.serivce.WeChatService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zq_gan
 * @since 2020/8/3
 * 参考：https://blog.csdn.net/u010651369/article/details/101697940
 **/

@Controller
@RequestMapping("infocheck")
@ResponseBody
public class SensitiveInformationCheckController {


    @Resource
    private WeChatService weChatService;
    /**
     * 图片过滤检测
     * @param file 图片文件
     * @return
     */
    @RequestMapping(value = "/checkimage", method = {RequestMethod.POST})
    @ResponseBody
    public WxSimpleDto checkPic(@RequestParam(value = "file") MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        WxSimpleDto wxSimpleDto = new WxSimpleDto();
        //自己写一个定时任务或其他方式 获取AccessToken
        try {
            String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + weChatService.getAccessToken();
            String result = uploadFile(url, file);
            wxSimpleDto = JSON.parseObject(result, WxSimpleDto.class);
            System.out.println("图片检测结果 = " + result);
            return wxSimpleDto;
        } catch (Exception e) {
            System.out.println("----------------调用腾讯内容过滤系统出错------------------" + e.getMessage());
            wxSimpleDto.setErrcode(500);
            wxSimpleDto.setErrMsg("system错误");
            return wxSimpleDto;
        }
    }

    @RequestMapping(value = "/checktext", method = {RequestMethod.POST})
    @ResponseBody
    public WxSimpleDto checkText(@RequestParam(value = "content") String content) {
        WxSimpleDto wxSimpleDto = new WxSimpleDto();
        //自己写一个定时任务或其他方式 获取AccessToken
        try {
            Integer result = weChatService.msgSecCheck(content);
            wxSimpleDto.setErrcode(result);
            System.out.println("文本检测结果 = " + result);
            return wxSimpleDto;
        } catch (Exception e) {
            System.out.println("----------------调用腾讯内容过滤系统出错------------------" + e.getMessage());
            wxSimpleDto.setErrcode(500);
            wxSimpleDto.setErrMsg("system错误");
            return wxSimpleDto;
        }
    }

    /**
     * 上传二进制文件
     * @param graphurl 接口地址
     * @param file 图片文件
     * @return
     */
    public static String uploadFile(String graphurl, MultipartFile file) {
        String line = null;//接口返回的结果
        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL url = new URL(graphurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // 上传文件
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"image\";filename=\""
                    + "https://api.weixin.qq.com" + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);

            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());

            // 读取文件数据
            out.write(file.getBytes());
            // 最后添加换行
            out.write(newLine.getBytes());

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY
                    + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
        }
        return line;
    }

}
