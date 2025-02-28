package com.makepdfdemo.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service
public class PdfGenerationService {

    //自动注入Thymeleaf模板引擎，用于处理HTML模板
    @Autowired
    private TemplateEngine templateEngine;

    public void generatePdf(HttpServletResponse response, String name, String userAgent) throws IOException {
        Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("timestamp", new Date());
        ctx.setVariable("userAgent", userAgent);

        try {
            String processedHtml = templateEngine.process("pdf-template", ctx);
            //设置响应头信息
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"generated-pdf.pdf\"");

            //创建PDF渲染器
            ITextRenderer renderer = new ITextRenderer();

            //添加字体解析
            configureChineseFont(renderer);

            // 添加图片路径解析
            ClassPathResource imgResource = new ClassPathResource("static/images/");
            String baseUrl = imgResource.getURL().toString();
            renderer.getSharedContext().setBaseURL(baseUrl);

            //将HTML转化为PDF
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(response.getOutputStream());
            renderer.finishPDF();
            
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }

    /**
     * 注册字体文件
     * @param renderer
     * @throws DocumentException
     * @throws IOException
     */
    private void configureChineseFont(ITextRenderer renderer) throws DocumentException, IOException {
        try {
            ClassPathResource fontResource = new ClassPathResource("static/fonts/simhei.ttf");
            String fontPath = fontResource.getURL().toString();
            renderer.getFontResolver().addFont(
                    fontPath,
                    BaseFont.IDENTITY_H,
                    BaseFont.NOT_EMBEDDED
            );
        } catch (Exception e) {
            throw new DocumentException("字体加载失败: " + e.getMessage());
        }
    }
}