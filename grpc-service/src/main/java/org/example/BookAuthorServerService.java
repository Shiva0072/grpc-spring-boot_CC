package org.example;

import com.grpcInterface.Author;
import com.grpcInterface.Book;
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

    @Override
    public void getBooksByAuthor(Author request, StreamObserver<Book> responseObserver) {
        TempDb.getBooksFromTempDb()
                .stream()
                .filter(book -> book.getAuthorId() == request.getAuthorId())
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Book> getExpensiveBook(StreamObserver<Book> responseObserver) {
        return new StreamObserver<Book>() {
            Book expensiveBook = null;
            float priceTrack = 0;
            @Override
            public void onNext(Book book) {
                if(book.getPrice()>priceTrack){
                    expensiveBook = book;
                    priceTrack = book.getPrice();
                }
                //forEach on client-side access this onNext
            }
            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }
            @Override
            public void onCompleted() {
                responseObserver.onNext(expensiveBook);
                responseObserver.onCompleted();
                //above lines accesses the onNext of client-side
            }
        };
    }

    @Override
    public StreamObserver<Book> getBookByAuthorGender(StreamObserver<Book> responseObserver) {

    }
}
