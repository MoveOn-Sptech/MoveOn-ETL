package br.com.moveon.services;

import br.com.moveon.providers.Logger;
import br.com.moveon.providers.S3Provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3Service {

    private final S3Client s3Client;
    private final Logger logger;

    public S3Service(Logger logger) {
        this.s3Client = new S3Provider().getS3Client();
        this.logger = logger;
    }

    public List<String> downloadAllFiles() {
        String bucketName = System.getenv("AWS_BUCKET_NAME");

        try {
            List<String> files = new ArrayList<>();
            files.add("2020.xlsx");
            files.add("2021.xlsx");
            files.add("2022.xlsx");
            files.add("2023.xlsx");
            files.add("2024.xlsx");

            List<String> filesDownloaded = new ArrayList<>();

            for (String file : files) {
                if (!new File(file).exists())
                    filesDownloaded.add(file);
            }

            for (String file : filesDownloaded) {
                downloadFile(file);
            }

            
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Ops hove um erro em realizar o dowload no bucket: " + bucketName);
            System.exit(0);
            return List.of();
        }
    }

    public void downloadFile(String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(System.getenv("AWS_BUCKET_NAME")).key(fileName).build();
        InputStream stream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
        Files.copy(stream, new File(getObjectRequest.key()).toPath());
    }

    public List<String> downloadFileFromS3(String bucketName)
            throws IOException {


        try {
            ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(bucketName).build();
            List<S3Object> s3Objects = s3Client.listObjects(listObjectsRequest).contents();
            for (S3Object s3Object : s3Objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(s3Object.key()).build();

                InputStream stream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                boolean fileExists = new File(s3Object.key()).exists();

                if (!fileExists)
                    Files.copy(stream, new File(s3Object.key()).toPath());
            }

            return s3Objects.stream().map(o -> o.key()).toList();
        } catch (S3Exception e) {
            logger.error("Erro ao baixar arquivo do S3: " + e.getMessage());
            throw new IOException("Falha no download do S3.", e);
        }
    }
}
