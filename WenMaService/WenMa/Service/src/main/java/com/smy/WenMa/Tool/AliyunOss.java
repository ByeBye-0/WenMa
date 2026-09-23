package com.smy.WenMa.Tool;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;

import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * OSS SDK 快速接入示例
 * 演示如何初始化 OSS 客户端并列出所有 Bucket
 */
@Component
public class AliyunOss {
    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    @Value("${Aliyun.endpoint}")
    private String endpoint;

    // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
    @Value("${Aliyun.region}")
    private String region;

    // 填写Bucket名称，例如examplebucket。
    @Value("${Aliyun.bucketName}")
    private  String bucketName;


    public  String upload(byte[] bytes, String originalFileName) throws Exception {
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 填写Object完整路径。Object完整路径中不能包含Bucket名称。
        String objectName = UUID.randomUUID() + "-" + originalFileName.substring(originalFileName.lastIndexOf("."));
        String dir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String Path = dir + "/" + objectName;
        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, Path, new ByteArrayInputStream(bytes));
            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "https://smy-wen-ma.oss-cn-beijing.aliyuncs.com/" + Path;

    }

}