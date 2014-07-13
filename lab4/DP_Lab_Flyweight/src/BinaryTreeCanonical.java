import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class BinaryTreeCanonical {

    /*** START ***/
    static Map<Integer, BinaryTreeNode>  binaryTreeCachedHashMap =  new HashMap<Integer, BinaryTreeNode>();
    /*** END *****/

	static class BinaryTreeNode {
    int key;
    BinaryTreeNode left, right;
    Integer cachedHash; // can speed up hash function computation hugely
    
    public BinaryTreeNode(int k, BinaryTreeNode l, BinaryTreeNode r) {
      this.key = k;
      this.left = l;
      this.right = r;
      this.cachedHash = hashCode();
    }
    
    /*** START **/
    public int hashCode(){
    	
    	int result = 17;
    	if(this.left != null && this.right != null){
    		result = 31*result + 17*this.left.hashCode() + 31*this.right.hashCode() + 51*this.key;
    	}else{ 
           	result = 31*result + 51*this.key;
	    	if(this.left != null){
	    		result = 31*result + 17*this.left.hashCode();
	    	}
	    	
	    	if(this.right != null){
	    		result = 31*result + 31*this.right.hashCode();
	    	}
    	}
    	return result;
    }

    public boolean equals(Object obj){
    	if(!(obj instanceof BinaryTreeNode)) return false;
    	BinaryTreeNode binaryTreeNode = (BinaryTreeNode)obj;
    	return key == binaryTreeNode.key 
    			&& left == binaryTreeNode.left
    			&& right == binaryTreeNode.right 
    			&& cachedHash == binaryTreeNode.cachedHash;
    }
    /** END **/
  }

  static BinaryTreeNode getCanonical(BinaryTreeNode n) {
	/*** START ***/
	BinaryTreeNode btNode = new BinaryTreeNode(n.key,n.left,n.right);
	if(!binaryTreeCachedHashMap.containsKey(btNode.cachedHash)){
		binaryTreeCachedHashMap.put(btNode.cachedHash, btNode);
	}else{
		btNode = binaryTreeCachedHashMap.get(btNode.cachedHash);
	}
	return btNode;
	/*** END ***/
  } 
  
  static int numberOfFlyweightNodes() {
	/*** START ****/
	return binaryTreeCachedHashMap.size();
	/*** END *****/
  }
  
}
