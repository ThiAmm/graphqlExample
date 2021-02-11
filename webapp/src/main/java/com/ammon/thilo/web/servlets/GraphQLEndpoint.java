package com.ammon.thilo.web.servlets;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.execution.instrumentation.Instrumentation;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.servlet.DefaultGraphQLSchemaProvider;
import graphql.servlet.ExecutionStrategyProvider;
import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLSchemaProvider;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration;

@WebListener
public class GraphQLEndpoint implements ServletContextListener {
	private static final Logger LOG = LogManager.getLogger();	
	private GraphQLSchema graphqlschema;

	public GraphQLEndpoint() throws IOException {
		this.graphqlschema = buildSchema();
	}

	@Override
	public void contextInitialized(final ServletContextEvent event) {
		final ServletRegistration.Dynamic dynamicGraphQLServlet
			= event.getServletContext().addServlet("GraphQLEndpoint", SimpleGraphQLHttpServlet.newBuilder(this.graphqlschema).build());
		dynamicGraphQLServlet.addMapping("/graphql");
	}

	@Override
	public void contextDestroyed(final ServletContextEvent event) {}

	private GraphQLSchema buildSchema() throws IOException {
		LOG.info("Schema-Datei einlesen");
		URL url = Resources.getResource("schema.graphqls");
		String sdl = Resources.toString(url, Charsets.UTF_8);
		TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = buildWiring();
		LOG.info("Schema generieren");
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		return schemaGenerator.makeExecutableSchema(
				typeDefinitionRegistry, runtimeWiring);
	}

	private RuntimeWiring buildWiring() {
		GraphQLDataLoader graphQLDataLoader = new GraphQLDataLoader();
		return RuntimeWiring.newRuntimeWiring()
			.type(TypeRuntimeWiring.newTypeWiring("Query")
					.dataFetcher("bookById",
						graphQLDataLoader.getBookByIdDataFetcher())
			     )
			.type(TypeRuntimeWiring.newTypeWiring("Book")
					.dataFetcher("author", 
						graphQLDataLoader.getAuthorDataFetcher())
			     )
			.build();
	}
}

