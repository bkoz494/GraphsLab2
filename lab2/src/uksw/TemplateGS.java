package uksw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.PriorityBlockingQueue;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;

public class TemplateGS {
	
	SingleGraph myGraph;
	
	/**
	 * Constructor of the class
	 * @param args
	 */
	public TemplateGS(String[] args) {
		myGraph = new SingleGraph("template graph");
		try {
			myGraph.read("./dgs/firstgraphlab2.dgs");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		exercise1(30);
//		exercise2();
//		exercise3();
//		exercise4Dijkstr();
//		exercise5();
		exercise6DFS();
	}
	
	
	private void exercise1 (int threshold) {
		
		myGraph.display();
		float avgDeg = 0;
		int sumOfDegrees = 0;
		for(Node n:myGraph.getNodeSet()) {
			sumOfDegrees += n.getDegree();
			n.addAttribute("ui.style", "fill-color:#cccccc;");
		}
		avgDeg = sumOfDegrees/myGraph.getNodeCount();
		System.out.println("average degree = "+avgDeg);
		//part2
		for(Node n:myGraph.getNodeSet()) {
			// get a structure gathering all a's neighbours
			int sumOfCost = 0;
			Iterator<Node> neighbours = n.getNeighborNodeIterator();
			while(neighbours.hasNext()) {
				Node v = neighbours.next();
				sumOfCost += (int)v.getAttribute("cost");
			}
			n.addAttribute("ui.label", "C:"+sumOfCost);
			if (sumOfCost > threshold) {
				//change node's style
				n.addAttribute("ui.style", "fill-color:green; size:10px;");
			}
		}
	}
	
	
	public void exercise2() {
		try {
			myGraph.read("./dgs/completegrid_30.dgs");
		} catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GraphParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myGraph.display(false);
		hitakey("Before BFS");
		ArrayList<Node> nodesToBeProcessed = new ArrayList<>();
		Node v = Toolkit.randomNode(myGraph);
		nodesToBeProcessed.add(v);
		while(!nodesToBeProcessed.isEmpty()) {
			Node w = nodesToBeProcessed.remove(0);
			mark(w);
			try {
				java.util.concurrent.TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator<Node> neighbours = w.getNeighborNodeIterator();
			while(neighbours.hasNext()) {
				Node t = neighbours.next();
				if(!t.hasAttribute("marked") && (!nodesToBeProcessed.contains(t))) {
					nodesToBeProcessed.add(t);
				}
			}
		}

	}
	
	
	public void exercise3() {
		try {
			myGraph.read("./dgs/completegrid_30.dgs");
		} catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GraphParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myGraph.display(false);
		hitakey("Before DFS");
		Stack<Node> nodesToBeProcessed = new Stack<>();
		Node v = Toolkit.randomNode(myGraph);
		nodesToBeProcessed.push(v);
		while(!nodesToBeProcessed.isEmpty()) {
			Node w = nodesToBeProcessed.pop();
			mark(w);
			try {
				java.util.concurrent.TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator<Node> neighbours = w.getNeighborNodeIterator();
			while(neighbours.hasNext()) {
				Node t = neighbours.next();
				if(!t.hasAttribute("marked") && (!nodesToBeProcessed.contains(t))) {
					nodesToBeProcessed.push(t);
				}
			}
		}

	}
	
	private void exercise4Dijkstr(){
		try {
			myGraph.read("./dgs/gridvaluated_10_220.dgs");
		} catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GraphParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myGraph.display(false);
		ArrayList<Node> priorityList = new ArrayList<>();
		hitakey("start Dijkstra");
		Node v = Toolkit.randomNode(myGraph);
		v.addAttribute("distance", 0);
		priorityList.add(v);
		while(!priorityList.isEmpty()) {
			Node w = priorityList.remove(0);
			mark(w);
			w.addAttribute("ui.label", "D: "+w.getAttribute("distance"));
			try {
				java.util.concurrent.TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator<Node> neighbours = w.getNeighborNodeIterator();
			while(neighbours.hasNext()) {
				Node t = neighbours.next();
				if(!t.hasAttribute("marked")) {
					Edge link = w.getEdgeBetween(t);
					int newDistance = (int)w.getAttribute("distance")+
							(int)link.getAttribute("distance");
					if(priorityList.contains(t)) {
						int currentDistance = t.getAttribute("distance");
						if(newDistance < currentDistance) {
							priorityList.remove(t);
							t.addAttribute("distance", newDistance);
							insertIntoPriorityList(priorityList, t);
						}
						
					} else {
						t.addAttribute("distance", newDistance);
						insertIntoPriorityList(priorityList, t);
					}
				}
			}
		}
	}
	
	private int dijkstra(Node v) {
		ArrayList<Node> priorityList = new ArrayList<>();
		v.addAttribute("distance", 0);
		priorityList.add(v);
		int lastDistance = 0;
		while(!priorityList.isEmpty()) {
			Node w = priorityList.remove(0);
			mark(w);
			lastDistance = (int)w.getAttribute("distance");
//			w.addAttribute("ui.label", "D: "+w.getAttribute("distance"));
			Iterator<Node> neighbours = w.getNeighborNodeIterator();
			while(neighbours.hasNext()) {
				Node t = neighbours.next();
				if(!t.hasAttribute("marked")) {
					Edge link = w.getEdgeBetween(t);
					int newDistance = (int)w.getAttribute("distance")+
							(int)link.getAttribute("distance");
					if(priorityList.contains(t)) {
						int currentDistance = t.getAttribute("distance");
						if(newDistance < currentDistance) {
							priorityList.remove(t);
							t.addAttribute("distance", newDistance);
							insertIntoPriorityList(priorityList, t);
						}
						
					} else {
						t.addAttribute("distance", newDistance);
						insertIntoPriorityList(priorityList, t);
					}
				}
			}
		}
		return lastDistance;
	}
	
	class Element{
		Node n;
		Integer eccentricity;
		public Element(Node n, Integer eccentricity) {
			this.n = n;
			this.eccentricity = eccentricity;
		}
	}
	
	class SortByEccentricity implements Comparator<Element>{

		@Override
		public int compare(Element arg0, Element arg1) {
			return arg0.eccentricity.compareTo(arg1.eccentricity);
		}
		
	}
	
	private void exercise5() {
		//part1
		myGraph = Tools.read("./dgs/gridvaluated_30_120.dgs");
		ArrayList<Element> eccentricities = new ArrayList<>();
		for (Node n: myGraph.getNodeSet()) {
			eccentricities.add(new Element(n,dijkstra(n)));
			for(Node m: myGraph.getNodeSet()) unmark(m);
		}
		Collections.sort(eccentricities, new SortByEccentricity());
		Element diameter = eccentricities.get(eccentricities.size()-1);
		Element radius = eccentricities.get(0);
		System.out.println("diameter: "+diameter.eccentricity);
		System.out.println("radius: "+radius.eccentricity);
		
		//part2
		Integer maxDifference = diameter.eccentricity - radius.eccentricity;
		for(Element e: eccentricities) {
			e.n.removeAttribute("ui.label");
			float red = (1-((float)(diameter.eccentricity-e.eccentricity)/(float)maxDifference))*255;
			float blue = (1-((float)(e.eccentricity-radius.eccentricity)/(float)maxDifference))*255;
			Integer redInt = (int)red;
			Integer blueInt = (int)blue;
			String redString = Integer.toHexString(redInt);
			if(redString.length() == 1) redString = "0"+redString;
			String blueString = Integer.toHexString(blueInt);
			if(blueString.length() == 1) blueString = "0"+blueString;
			String color = redString+"00"+blueString;
			e.n.addAttribute("ui.style", "fill-color:#"+color+";size:20px;");
		}
		myGraph.display(false);
	}
	

	
	
	private void insertIntoPriorityList(ArrayList<Node> list, Node v) {
		boolean inserted = false;
		int position = 0;
		if(list.size() == 0) {
			list.add(v);
			inserted = true;
		}
		int referenceDistance = v.getAttribute("distance");
		while(!inserted && position < list.size()) {
			int currentDistance = (list.get(position)).getAttribute("distance");
			if(currentDistance > referenceDistance) {
				list.add(position,v);
				inserted = true;
			} else {
				position++;
			}
		}
		if(!inserted) list.add(v);
	}


	private void mark(Node n) {
		n.addAttribute("marked", true);
		n.addAttribute("ui.style", "fill-color:black;size:10px;");
	}
	
	private void unmark(Node n) {
		n.removeAttribute("marked");
		n.removeAttribute("ui.style");
	}
	
	
	private void exercise6DFS() {
		myGraph = Tools.read("./dgs/gridvaluated_30_120.dgs");
		myGraph.display(false);
		hitakey("Start spanning tree");
		Stack<Node> nodesToBeProcessed = new Stack<>();
		Node v = Toolkit.randomNode(myGraph);
		nodesToBeProcessed.push(v);
		while(!nodesToBeProcessed.isEmpty()) {
			Node w = nodesToBeProcessed.pop();
			mark(w);
			Tools.pause(10);
			Iterator<Node> neighbours = w.getNeighborNodeIterator();
			while(neighbours.hasNext()) {
				Node t = neighbours.next();
				if(!t.hasAttribute("marked") && (!nodesToBeProcessed.contains(t))) {
					nodesToBeProcessed.push(t);
					Edge link = t.getEdgeBetween(w);
					link.addAttribute("ui.style", "fill-color:black;size:10px;");
				}
			}
		}
	}
	
	private void exercise6BFS() {
		myGraph = Tools.read("./dgs/gridvaluated_30_120.dgs");
		myGraph.display(false);
		hitakey("Start spanning tree");
		ArrayList<Node> nodesToBeProcessed = new ArrayList<>();
		Node v = Toolkit.randomNode(myGraph);
		nodesToBeProcessed.add(v);
		while(!nodesToBeProcessed.isEmpty()) {
			Node w = nodesToBeProcessed.remove(0);
			mark(w);
//			Tools.pause(10);
			Iterator<Node> neighbours = w.getNeighborNodeIterator();
			while(neighbours.hasNext()) {
				Node t = neighbours.next();
				if(!t.hasAttribute("marked") && (!nodesToBeProcessed.contains(t))) {
					nodesToBeProcessed.add(t);
					Edge link = t.getEdgeBetween(w);
					link.addAttribute("ui.style", "fill-color:black;size:10px;");
				}
			}
		}
	}
	
	/**
	 * the main just chooses the viewer and instantiates the class
	 * @param args
	 */
	public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        new TemplateGS(args);
	}
	
	private void hitakey(String msg) {
		System.err.println("\n----------------------");
		System.err.println("\t"+msg);
		System.err.println("\n----------------------");
		try {
			System.in.read();
		} catch(IOException ioe) {
			
		}
	}

}