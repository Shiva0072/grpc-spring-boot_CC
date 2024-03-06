package org.example.service;

import com.google.protobuf.Descriptors;
import com.grpcInterface.Author;
import com.grpcInterface.Book;
import com.grpcInterface.BookAuthorServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class BookAuthorClientService {
    @GrpcClient("grpc-BookAuthorService-Server-channel")
    BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-BookAuthorService-Server-channel")
    BookAuthorServiceGrpc.BookAuthorServiceStub asynchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getAuthorByGivenId(int authorId){
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        Author authorResponse = synchronousClient.getAuthor(authorRequest);
        return authorResponse.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor,Object>> getBooksByAuthorIdViaStreaming(int authorId) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        final List<Map<Descriptors.FieldDescriptor,Object>> response = new ArrayList<>();
        asynchronousClient.getBooksByAuthor(authorRequest, new StreamObserver<Book>() {
            @Override
            public void onNext(Book book) {
                response.add(book.getAllFields());
            }
            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }
            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        boolean awaited = countDownLatch.await(1, TimeUnit.MINUTES);

        return awaited ? response: Collections.emptyList();
    }


}
