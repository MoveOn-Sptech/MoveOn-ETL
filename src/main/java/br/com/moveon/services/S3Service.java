package br.com.moveon.services;

import br.com.moveon.providers.Logger;
import br.com.moveon.providers.S3Provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3Service {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;
    private final Logger logger;

    public S3Service(Logger logger) {
        this.s3Client = new S3Provider().getS3Client();
        this.logger = logger;
    }

    public List<String> downloadAllFiles() {
        String bucketName = System.getenv("AWS_BUCKET_NAME");

        try {
            List<String> files = List.of(
                    "2020.xlsx",
                    "2021.xlsx",
                    "2022.xlsx",
                    "2023.xlsx",
                    "2024.xlsx");
            logger.info("Baixando arquivos do bucket: " + bucketName);
            List<String> filesDownloaded = new ArrayList<>();

            for (String file : files) {
                if (!new File(file).exists())
                    filesDownloaded.add(file);
            }

            if(filesDownloaded.isEmpty()) {
                logger.info("Todos arquivos já estão baixados");
                return files;
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
        logger.info("Baixando objeto com chave: " + fileName);
        Files.copy(stream, new File(getObjectRequest.key()).toPath());
    }

}
