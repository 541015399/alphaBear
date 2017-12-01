/*
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package sf.alphaBear.neo4j;

import java.io.File;

import org.neo4j.graphalgo.CommonEvaluators;
import org.neo4j.graphalgo.EstimateEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Paths;

public class CalculateAstarPath
{
    private static final File databaseDirectory = new File( "target/neo4j-shortest-path" );
    private static final String NAME_KEY = "name";
    private static RelationshipType KNOWS = RelationshipType.withName( "KNOWS" );

    private static GraphDatabaseService graphDb;
    private static Index<Node> indexService;

    public final static void main(String[] args )
    {
        deleteFileOrDirectory( databaseDirectory );
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( databaseDirectory );
        registerShutdownHook();
        try ( Transaction tx = graphDb.beginTx() )
        {
            indexService = graphDb.index().forNodes( "nodes" );
            /*
             *  (Neo) --> (Trinity)
             *     \       ^
             *      v     /
             *    (Morpheus) --> (Cypher)
             *            \         |
             *             v        v
             *            (Agent Smith)
             */
            createChain( "Neo", "Trinity" );
            createChain( "Neo", "Morpheus", "Trinity" );
            createChain( "Morpheus", "Cypher", "Agent Smith" );
            createChain( "Morpheus", "Agent Smith" );
            tx.success();
        }
        
        EstimateEvaluator<Double> estimateEvaluator = new EstimateEvaluator<Double>()  
		{  
		    public Double getCost( final Node node, final Node goal )  
		    {  
		        /*double dx = (Double) node.getProperty( "x" ) - (Double) goal.getProperty( "x" );  
		        double dy = (Double) node.getProperty( "y" ) - (Double) goal.getProperty( "y" );  
		        double result = Math.sqrt( Math.pow( dx, 2 ) + Math.pow( dy, 2 ) );  
		        return result;  */
		    	return 1d;
		    }  
		};  

        try ( Transaction tx = graphDb.beginTx() )
        {
            // So let's find the shortest path between Neo and Agent Smith
            Node neo = getOrCreateNode( "Neo" );
            Node agentSmith = getOrCreateNode( "Agent Smith" );
            // START SNIPPET: shortestPathUsage
            PathFinder<WeightedPath> finder = null;
            finder = GraphAlgoFactory.aStar(  
    				PathExpanders.forTypeAndDirection(KNOWS, Direction.OUTGOING ),  
    		        CommonEvaluators.doubleCostEvaluator("length"), estimateEvaluator );  
            // finder = GraphAlgoFactory.aStar(
            //         PathExpanders.forTypeAndDirection( KNOWS, Direction.BOTH ), 4 );
            Path foundPath = finder.findSinglePath( neo, agentSmith );
            System.out.println( "Path from Neo to Agent Smith: "
                                + Paths.simplePathToString( foundPath, NAME_KEY ) );
            // END SNIPPET: shortestPathUsage
        }

        System.out.println( "Shutting down database ..." );
        graphDb.shutdown();
    }

    private static void createChain( String... names )
    {
        for ( int i = 0; i < names.length - 1; i++ )
        {
            Node firstNode = getOrCreateNode( names[i] );
            Node secondNode = getOrCreateNode( names[i + 1] );
            Relationship rel = firstNode.createRelationshipTo( secondNode, KNOWS );
            rel.setProperty("length", 1d);
        }
    }

    private static Node getOrCreateNode( String name )
    {
        Node node = indexService.get( NAME_KEY, name ).getSingle();
        if ( node == null )
        {
            node = graphDb.createNode();
            node.setProperty( NAME_KEY, name );
            indexService.add( node, NAME_KEY, name );
        }
        return node;
    }

    private static void registerShutdownHook()
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }

    private static void deleteFileOrDirectory( File file )
    {
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                for ( File child : file.listFiles() )
                {
                    deleteFileOrDirectory( child );
                }
            }
            file.delete();
        }
    }
}