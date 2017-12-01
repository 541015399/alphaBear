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
package sf.alphaBear.agent.algo;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphalgo.CommonEvaluators;
import org.neo4j.graphalgo.EstimateEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Paths;

import sf.alphaBear.agent.db.GridStateDb;
import sf.alphaBear.httpio.pojo.Wall;

public class AstarByNeo4j {
	GridStateDb db;
	
	public AstarByNeo4j(GridStateDb db) {
		this.db = db;
	}

	public void doAlgo(int fx, int fy, int tox, int toy) {
		EstimateEvaluator<Double> estimateEvaluator = new EstimateEvaluator<Double>() {
			public Double getCost(final Node node, final Node goal) {
				/*
				 * double dx = (Double) node.getProperty( "x" ) - (Double) goal.getProperty( "x"
				 * ); double dy = (Double) node.getProperty( "y" ) - (Double) goal.getProperty(
				 * "y" ); double result = Math.sqrt( Math.pow( dx, 2 ) + Math.pow( dy, 2 ) );
				 * return result;
				 */
				return 1d;
			}
		};

		try (Transaction tx = db.beginTx()) {
			// So let's find the shortest path between Neo and Agent Smith
			Node fromNode = db.getOrCreateNode(fx, fy);
			Node toNode = db.getOrCreateNode(tox, toy);
			// START SNIPPET: shortestPathUsage
			PathFinder<WeightedPath> finder = null;
			finder = GraphAlgoFactory.aStar(PathExpanders.forTypeAndDirection(GridStateDb.KNOWS, Direction.OUTGOING),
					CommonEvaluators.doubleCostEvaluator("w"), estimateEvaluator);
			// finder = GraphAlgoFactory.aStar(
			// PathExpanders.forTypeAndDirection( KNOWS, Direction.BOTH ), 4 );
			Path foundPath = finder.findSinglePath(fromNode, toNode);
			System.out.println("Path from (0,0) to (,3): " + Paths.simplePathToString(foundPath, GridStateDb.NAME_KEY));
			// END SNIPPET: shortestPathUsage
		}
	}

	public final static void main(String[] args) {
		Wall wall = new Wall();
		wall.setX(0);
		wall.setY(1);
		
		List<Wall> walls = new ArrayList<Wall>();
		walls.add(wall);
		
		GridStateDb db = new GridStateDb(12, 12, walls);
		AstarByNeo4j mapStateByNeo4j = new AstarByNeo4j(db);

		mapStateByNeo4j.doAlgo(0,0, 2,2);
		db.closeDb();
	}

}
