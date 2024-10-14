package ru.gb.timesheet.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectServiceTest {

  @Mock
  MinioClient minioClient;

  @InjectMocks
  ObjectService objectService;

  @Test
  void saveObject() {
    objectService.saveObject("objectId");

    verify(minioClient).saveObject(eq("objectId"));
  }
}