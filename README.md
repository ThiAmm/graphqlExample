# Build the application

To build the application, run 

```
docker-compose build
``` 

# Run the application

To run the application, run 
```
docker-compose up
```

# Graphql

To send a query, do a post on the adress <http://localhost:4000/graphqlexample/graphql>
with the following body
``` 
{
  bookById(id: "book-2"){
    id
    name
  }
}
```

