package sf.alphaBear.neo4j;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Paths;

public class GridMapAsNeo4jVlpy {
	String path ;
	GraphDatabaseService db;
	
	ConcurrentHashMap<String, Long> nodeIdMap ;
	
	public GridMapAsNeo4jVlpy(String path) {
		this.path = path;
		db = new GraphDatabaseFactory().newEmbeddedDatabase(new File(path));
		nodeIdMap = new ConcurrentHashMap<>();
		
		registerShutdownHook();
	}
	
	String name(int x, int y) {
		return "x_" + x + "_y_" + x;
	}
	Node createNode(int x, int y) {
		String nodeName = name(x, y);
		Node node = db.createNode(Label.label(nodeName));

		node.setProperty("n", nodeName);
		node.setProperty("x", x);
		node.setProperty("y", y);
		
		nodeIdMap.put(nodeName, node.getId());
		return node;
	}
	public void init(final int xSize, final int ySize) {
		//node 
		try (Transaction tx = db.beginTx()){
			for(int x=0;x<xSize;x++) {
				for(int y=0;y<ySize;y++) {
					createNode(x, y);
				}
			}
			
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//edges
		try (Transaction tx = db.beginTx()){
			for(int x=0;x<xSize;x++) {
				for(int y=0;y<ySize;y++) {
					Node node = db.getNodeById(nodeIdMap.get(name(x, y)));
					
					//left
					if (x - 1 >= 0) {
						Node ref = db.getNodeById(nodeIdMap.get(name(x, y)));
						Relationship rel = node.createRelationshipTo(ref, RelTypes.LEFT);
						rel.setProperty("w", 1d);
					}
					//right
					if (x + 1 < xSize) {
						Node ref = db.getNodeById(nodeIdMap.get(name(x, y)));
						Relationship rel = node.createRelationshipTo(ref, RelTypes.RIGHT);
						rel.setProperty("w", 1d);
					}
					//up
					if (y - 1 >=0 ) {
						Node ref = db.getNodeById(nodeIdMap.get(name(x, y)));
						Relationship rel = node.createRelationshipTo(ref, RelTypes.UP);
						rel.setProperty("w", 1d);
					}
					//down
					if (y + 1 < ySize) {
						Node ref = db.getNodeById(nodeIdMap.get(name(x, y)));
						Relationship rel = node.createRelationshipTo(ref, RelTypes.DOWN);
						rel.setProperty("w", 1d);
					}
				}
			}
			
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
	}
	
	public void findPrintPath(int x1, int y1, int x2, int y2) {
		try ( Transaction tx = db.beginTx() )
        {
            // So let's find the shortest path between Neo and Agent Smith
            Node neo = db.getNodeById(nodeIdMap.get(name(x1, y1)));
            Node agentSmith = db.getNodeById(nodeIdMap.get(name(x2, y2)));
            // START SNIPPET: shortestPathUsage
            /*PathFinder<Path> finder = GraphAlgoFactory.shortestPath(
                    PathExpanders.forTypesAndDirections(RelTypes.UP, Direction.OUTGOING, 
                    		RelTypes.DOWN, Direction.OUTGOING, 
                    		RelTypes.LEFT, Direction.OUTGOING, 
                    		RelTypes.RIGHT, Direction.OUTGOING )
                    , 4 );*/
            
            PathFinder<Path> finder = GraphAlgoFactory.shortestPath(
                    PathExpanders.allTypesAndDirections()
                    , 4 );
            Path foundPath = finder.findSinglePath( neo, agentSmith );
            String rlt = null; 
            if(foundPath!=null) {
            		rlt = "Path from Neo to Agent Smith: " + Paths.simplePathToString( foundPath, "n" ) ;
            }else {
            		rlt = "not exist";
            }
            System.out.println(rlt);
            // END SNIPPET: shortestPathUsage
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void registerShutdownHook()
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                db.shutdown();
            }
        } );
    }
	
	public static void main(String[] args) {
		GridMapAsNeo4jVlpy map = new GridMapAsNeo4jVlpy("/Users/lupinyin/grid_map");
		map.init(120, 120);
		
		map.findPrintPath(0, 0, 3, 3);
		System.out.println();
	}
}
