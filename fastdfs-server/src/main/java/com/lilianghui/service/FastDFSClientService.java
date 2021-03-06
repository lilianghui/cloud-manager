package com.lilianghui.service;


import com.github.tobato.fastdfs.domain.MataData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: FastDFS文件上传下载包装类</p>
 */
@Service
public class FastDFSClientService {

    private final Logger logger = LoggerFactory.getLogger(FastDFSClientService.class);

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException io异常
     */
    public String uploadFile(MultipartFile file) throws IOException {
        Set<MataData> data = new HashSet<>();
        data.add(new MataData("name", file.getOriginalFilename()));
        data.add(new MataData("contentType", file.getContentType()));
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), data);
        return getResAccessUrl(storePath, file.getOriginalFilename());
    }

    /**
     * 将一段字符串生成一个文件上传
     *
     * @param content       文件内容
     * @param fileExtension 文件扩展名
     * @return 图片地址
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = fastFileStorageClient.uploadFile(stream, buff.length, fileExtension, null);
        return getResAccessUrl(storePath, "");
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath, String fileName) {
        String fileUrl = storePath.getFullPath() + "?attname=" + fileName;
        return fileUrl;
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
    }

    public InputStream downFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return null;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            InputStream inputStream = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), ins -> ins);
            return inputStream;
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }


}