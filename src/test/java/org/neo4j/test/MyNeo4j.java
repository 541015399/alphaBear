package org.neo4j.test;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class MyNeo4j {
	public final static void main(String[] args) {
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("/Users/lupinyin")); 
		
		
		   Transaction tx = db.beginTx(); 
		   try { 
		       Node node1 = db.createNode(); 
		       node1.setProperty("name", "歌手 1"); 
		       Node node2 = db.createNode(); 
		       node2.setProperty("name", "专辑 1"); 
		       node1.createRelationshipTo(node2, RelTypes.KNOWS); 
		       Node node3 = db.createNode(); 
		       node3.setProperty("name", "歌曲 1"); 
		       node2.createRelationshipTo(node3, RelTypes.KNOWS); 
		       tx.success(); 
		   } finally { 
		       tx.close(); 
		   } 
	}
	
	private static enum RelTypes implements RelationshipType
    {
        KNOWS
    }
}
