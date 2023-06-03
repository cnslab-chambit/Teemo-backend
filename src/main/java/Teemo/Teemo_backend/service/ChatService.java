package Teemo.Teemo_backend.service;

public interface ChatService {
    void saveToMemory(Object obj); // 메모리에 저장
    void saveToDB(); // DB에 한꺼번에 저장
}
