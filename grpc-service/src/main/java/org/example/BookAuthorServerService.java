package org.example;

import com.grpcInterface.Author;
import com.grpcInterface.BookAuthorServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.function.Consumer;

@GrpcService
public class BookAuthorServerService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase {
    @Override
    public void getAuthor(Author request, StreamObserver<Author> responseObserver) {
        TempDb.getAuthorsFromTempDb()
                .stream()
                .filter(author -> author.getAuthorId() == request.getAuthorId())
                .findFirst()
                .ifPresent(new Consumer<Author>() {
                    @Override
                    public void accept(Author author) {
                        responseObserver.onNext(author);
                    }
                });
        responseObserver.onCompleted();
    }
}
