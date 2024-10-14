package ru.gb.timesheet.example;

public class ObjectService {

  private final MinioClient minioClient;

  public ObjectService(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  public void saveObject(String objectId) {
    //
    minioClient.saveObject(objectId);
    //
  }

}
