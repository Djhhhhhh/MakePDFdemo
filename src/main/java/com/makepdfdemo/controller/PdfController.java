package com.makepdfdemo.controller;

import com.makepdfdemo.service.PdfGenerationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class PdfController {
    @Autowired
    private PdfGenerationService pdfService;
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @PostMapping("/generate-pdf")
    public void generatePdf(@RequestParam String name,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        String userAgent = request.getHeader("User-Agent");
        pdfService.generatePdf(response, name, userAgent);
    }
}