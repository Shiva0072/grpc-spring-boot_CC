package org.example.controller;

import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import org.example.service.BookAuthorClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class BookAuthorController {
    BookAuthorClientService bookAuthorClientService;

    @GetMapping("/author/{authorId}")
    public Map<Descriptors.FieldDescriptor, Object> getAuthor(@PathVariable String authorId){
        return bookAuthorClientService.getAuthorByGivenId(Integer.parseInt(authorId));
    }

    @GetMapping("/book/{authorId}")
    public List<Map<Descriptors.FieldDescriptor,Object>> getBooksByAuthorId(@PathVariable String authorId) throws InterruptedException {
        return bookAuthorClientService.getBooksByAuthorIdViaStreaming(Integer.parseInt(authorId));
    }

    @GetMapping("/book/getMostExpensiveBook")
    public Map<String,Map<Descriptors.FieldDescriptor,Object>> getExpensiveBook() throws InterruptedException {
        return bookAuthorClientService.getExpensiveBookViaClientStream();
    }

}
