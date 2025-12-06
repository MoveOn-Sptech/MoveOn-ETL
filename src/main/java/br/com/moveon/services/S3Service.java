package br.com.moveon.services;

import br.com.moveon.services.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

public class S3Service extends AbstractService{
    private final AwsCredentialsProvider credentials;

    public S3Service() {
        this.credentials = DefaultCredentialsProvider.create();
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

            if (filesDownloaded.isEmpty()) {
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

    public void downloadFile(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(System.getenv("AWS_BUCKET_NAME")).key(fileName).build();
        InputStream stream = this.getS3Client().getObject(getObjectRequest, ResponseTransformer.toInputStream());
        logger.info("Baixando objeto com chave: " + fileName);

        try {
            Files.copy(stream, new File(getObjectRequest.key()).toPath());

        } catch (IOException e) {
            e.printStackTrace();
            logger.fatal(e.getMessage());
        }
    }

    public S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(credentials)
                .build();
    }

}
