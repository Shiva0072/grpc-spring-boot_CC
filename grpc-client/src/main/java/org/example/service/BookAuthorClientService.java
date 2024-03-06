package org.example.service;

import com.google.protobuf.Descriptors;
import com.grpcInterface.Author;
import com.grpcInterface.BookAuthorServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookAuthorClientService {
    @GrpcClient("grpc-BookAuthorService-Server-channel")
    BookAuthorServiceGrpc.BookAuthorServiceBlockingStub sychronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getAuthorByGivenId(int authorId){
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        Author authorResponse = sychronousClient.getAuthor(authorRequest);
        return authorResponse.getAllFields();
    }

}
