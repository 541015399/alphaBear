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
package sf.alphaBear.agent.db;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import sf.alphaBear.Point;
import sf.alphaBear.httpio.pojo.Wall;

public class GridStateDb {
	private static final File databaseDirectory = new File("target/map_neo4j_data");
	public static final String NAME_KEY = "name";
	public static RelationshipType KNOWS = RelationshipType.withName("KNOWS");
	
	private GraphDatabaseService graphDb;
	private Index<Node> indexService;
	private ConcurrentHashMap<String, String> wallSet ;
	
	private Set<Point> allValidPoints = new HashSet<Point>(); 
	
	public static String nodeKey(final int x,final int y) {
		return "x=" + x + ",y=" + y;  
	}

	public GridStateDb(final int maxX, final int maxY, List<Wall> walls) {
		init(maxX, maxY, walls);
	}
	
	public void init(final int maxX, final int maxY, List<Wall> walls) {
		wallSet = new ConcurrentHashMap<String, String>();
		if (walls!=null) {
			walls.forEach(w->wallSet.put(w.myWallKey(), w.myWallKey()));
		}
		
		deleteFileOrDirectory(databaseDirectory);
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(databaseDirectory);
		registerShutdownHook();
		try (Transaction tx = graphDb.beginTx()) {
			indexService = graphDb.index().forNodes("nodes");
			for(int x=0;x<=maxX;x++) {
				for(int y=0;y<=maxY;y++) {
					String key = Wall.wallKey(x, y);
					// 不要把墙加进去
					if (!wallSet.contains(key)) {
						createChain(x, y, maxX, maxY);
					}else {
						System.out.println("a wall");
					}
				}
			}
			
			tx.success();
		}
	}
	public Transaction beginTx() {
		return graphDb.beginTx();
	}
	public void closeDb() {
		System.out.println("Shutting down database ...");
		graphDb.shutdown();
	}

	public static void testData() {
		Wall wall = new Wall();
		wall.setX(1);
		wall.setY(1);
		
		List<Wall> walls = new ArrayList<Wall>();
		walls.add(wall);
		GridStateDb mapStateByNeo4j = new GridStateDb(12, 12, walls);
		
		mapStateByNeo4j.closeDb();
	}

	public final static void main(String[] args) {
		testData();
	}

	private void createChain(int x, int y, int maxX, int maxY) {
		Node f = getOrCreateNode(x, y);
		allValidPoints.add(new Point(x, y));
		
		Node l = x-1 >= 0 ? getOrCreateNode(x-1, y) : null;
		Node r = x+1 <= maxX ? getOrCreateNode(x+1, y) : null;
		Node u = y+1 <= maxY ? getOrCreateNode(x, y+1) : null;
		Node d = y-1 >=0? getOrCreateNode(x, y - 1) : null;
		
		checkAndAddEdge(f, l);
		checkAndAddEdge(f, r);
		checkAndAddEdge(f, u);
		checkAndAddEdge(f, d);
	}
	//边界问题， 墙的问题
	private void checkAndAddEdge(Node f, Node t) {
		if (f!=null && t!=null 
				&& (!wallSet.containsKey(Wall.wallKey(f.getProperty("x").toString(), f.getProperty("y").toString())))
				&& (!wallSet.containsKey(Wall.wallKey(t.getProperty("x").toString(), t.getProperty("y").toString()))) ){
			Relationship rel = f.createRelationshipTo(t, KNOWS);
			rel.setProperty("w", 1d);	
		}else {
			if(f!=null && t!=null) {
				System.out.println("a wall");
			}
		}
	}

	public Node getOrCreateNode(int x, int y) {
		String name = nodeKey(x, y);
		Node node = indexService.get(NAME_KEY, name).getSingle();
		if (node == null) {
			node = graphDb.createNode();
			node.setProperty(NAME_KEY, name);
			node.setProperty("x", x);
			node.setProperty("y", y);
			
			indexService.add(node, NAME_KEY, name);
		}
		return node;
	}

	private void registerShutdownHook() {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	public Set<Point> getAllValidPoints() {
		return allValidPoints;
	}

	private void deleteFileOrDirectory(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File child : file.listFiles()) {
					deleteFileOrDirectory(child);
				}
			}
			file.delete();
		}
	}
}
