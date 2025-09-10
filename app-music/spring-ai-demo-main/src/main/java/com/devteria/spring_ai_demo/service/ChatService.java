package com.devteria.spring_ai_demo.service;

import com.devteria.spring_ai_demo.dto.ChatRequest;
import com.devteria.spring_ai_demo.dto.SongRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final PromptTemplate promptTemplate;

    // Constructor
    public ChatService(ChatClient.Builder chatClientBuilder,
                       @Value("classpath:song-lyrics-prompt.st") Resource promptResource) throws IOException {
        this.chatClient = chatClientBuilder.build();

        // Đọc nội dung từ file prompt template
        String templateContent = StreamUtils.copyToString(
                promptResource.getInputStream(),
                StandardCharsets.UTF_8
        );

        // Khởi tạo PromptTemplate từ nội dung file
        this.promptTemplate = new PromptTemplate(templateContent);
    }

    /**
     * Gửi tin nhắn đơn giản cho AI để chat
     */
    public String chat(ChatRequest request) {
        try {
            return chatClient
                    .prompt(request.message())
                    .call()
                    .content();
        } catch (Exception e) {
            return "Đã xảy ra lỗi khi gửi tin nhắn: " + e.getMessage();
        }
    }

    /**
     * Tạo lời bài hát dựa trên ý tưởng và thể loại nhạc từ request
     */
    public String generateLyrics(SongRequest request) {
        try {
            // Tạo map chứa dữ liệu để gán vào template
            Map<String, Object> model = Map.of(
                    "idea", request.idea(),
                    "genre", request.genre()
            );

            // Tạo prompt hoàn chỉnh từ template và dữ liệu
            Prompt prompt = promptTemplate.create(model);

            // Gọi AI và lấy kết quả
            ChatResponse response = chatClient
                    .prompt(prompt)
                    .call()
                    .chatResponse();

            // Kiểm tra null và trả về kết quả
            if (response != null
                    && response.getResult() != null
                    && response.getResult().getOutput() != null) {
                return response.getResult().getOutput().getText();
            } else {
                return "Không nhận được phản hồi từ AI.";
            }

        } catch (Exception e) {
            return "Lỗi khi tạo lời bài hát: " + e.getMessage();
        }
    }
}
