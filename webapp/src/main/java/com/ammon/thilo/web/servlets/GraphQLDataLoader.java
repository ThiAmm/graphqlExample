package com.ammon.thilo.web.servlets; 

import graphql.schema.DataFetcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

public class GraphQLDataLoader {
    private static final Logger LOG = LogManager.getLogger();

    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );

    private static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    );

    public GraphQLDataLoader(){
    }

    public DataFetcher getBookByIdDataFetcher() {
        return environment -> {
            String bookId = environment.getArgument("id");
            return books.stream()
		    .filter(book -> book.get("id").equals(bookId))
		    .findFirst()
		    .orElse(null);
        };
    }
    
    public DataFetcher getAuthorDataFetcher() {
        return environment -> {
		Map<String, String> book = environment.getSource();
		String authorId = book.get("authorId");
		return authors
			.stream()
			.filter(author -> author.get("id").equals(authorId))
			.findFirst()
			.orElse(null);
        };
    }
}
