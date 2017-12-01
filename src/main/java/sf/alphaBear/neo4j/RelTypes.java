package sf.alphaBear.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType
{
    UP, DOWN, LEFT, RIGHT, KNOWNS
}
