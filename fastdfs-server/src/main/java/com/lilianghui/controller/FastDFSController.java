package com.lilianghui.controller;

import com.lilianghui.service.FastDFSClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FastDFSController {


    @RequestMapping("/")
    public ModelAndView index(Model model) {
        return new ModelAndView("index");
    }

    @Resource
    private FastDFSClientService fastDFSClientService;

    /**
     * 原始的excel导入，数据上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView("success");
        try {
            String fileName = fastDFSClientService.uploadFile(file);
            modelAndView.addObject("fileName", fileName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/download")
    public void download(HttpServletResponse response, String file) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\""+file+"\"");
            InputStream inputStream = fastDFSClientService.downFile(file);
            IOUtils.write(IOUtils.toByteArray(inputStream), response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
