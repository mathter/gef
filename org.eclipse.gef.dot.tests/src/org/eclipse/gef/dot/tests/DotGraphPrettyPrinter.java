/*******************************************************************************
 * Copyright (c) 2018 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tamas Miklossy (itemis AG) - initial API and implementation (bug #518417)
 *
 *******************************************************************************/
package org.eclipse.gef.dot.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.gef.graph.Edge;
import org.eclipse.gef.graph.Graph;
import org.eclipse.gef.graph.Node;

/**
 * A Pretty Printer providing formatted string representations (with line
 * separation and indentation) for {@link Graph}, {@link Node} and {@link Edge}
 * objects.
 *
 */
class DotGraphPrettyPrinter {

	private String indent;
	private String lineSeparator;

	private Map<Node, String> nodeToIdMapper;

	/**
	 * Creates a Pretty Printer with the default settings.
	 */
	public DotGraphPrettyPrinter() {
		this("\t", System.lineSeparator());
	}

	/**
	 * Creates a Pretty Printer with the given indent and lineSeparator
	 * characters.
	 *
	 * @param indent
	 *            characters to use for indenting.
	 * @param lineSeparator
	 *            characters to use for line separations.
	 */
	public DotGraphPrettyPrinter(String indent, String lineSeparator) {
		this.indent = indent;
		this.lineSeparator = lineSeparator;
		this.nodeToIdMapper = new HashMap<>();
	}

	/**
	 * Calculates the position of the given {@link Edge} within the containing
	 * {@link Graph}.
	 *
	 * @param edge
	 *            The {@link Edge} for which to return the position within the
	 *            containing {@link Graph}.
	 *
	 * @return The (1-based) position of the given {@link Edge} within the
	 *         containing {@link Graph} or -1 if the edge is not contained in
	 *         any {@link Graph}.
	 */
	protected int getPosition(Edge edge) {
		Graph graph = edge.getGraph();
		if (graph == null) {
			return -1;
		}
		// the position starts with 1 (not with 0)
		return graph.getEdges().indexOf(edge) + 1;
	}

	/**
	 * Calculates the position of the given {@link Node} within the containing
	 * {@link Graph}.
	 *
	 * @param node
	 *            The {@link Node} for which to return the position within the
	 *            containing {@link Graph}.
	 *
	 * @return The (1-based) position of the given {@link Node} within the
	 *         containing {@link Graph} or -1 if the node is not contained in
	 *         any {@link Graph}.
	 */
	protected int getPosition(Node node) {
		Graph graph = node.getGraph();
		if (graph == null) {
			return -1;
		}
		// the position starts with 1 (not with 0)
		return graph.getNodes().indexOf(node) + 1;
	}

	/**
	 * Creates a formatted string representation of a given {@link Edge}.
	 *
	 * @param edge
	 *            The {@link Edge} for which to create a formatted string
	 *            representation.
	 * @return The formatted string representation (with line separation and
	 *         indentation) of the given {@link Edge}.
	 */
	public String prettyPrint(Edge edge) {
		return prettyPrint(edge, "", "");
	}

	/**
	 * Creates a formatted string representation of a given {@link Edge}.
	 *
	 * @param edge
	 *            The {@link Edge} for which to create a formatted string
	 *            representation.
	 * @param startIndent
	 *            The indentation to use when creating the formatted string
	 *            representation.
	 * @param positionPrefix
	 *            The prefix to prepend the edge position
	 * @return The formatted string representation (with line separation and
	 *         indentation) of the given {@link Edge}.
	 */
	protected String prettyPrint(Edge edge, String startIndent,
			String positionPrefix) {
		StringBuilder sb = new StringBuilder();

		sb.append(startIndent);
		sb.append("Edge");
		int position = getPosition(edge);
		if (position != -1) {
			sb.append(positionPrefix);
			sb.append(position);
		}

		sb.append(String.format(" from Node%s to Node%s {",
				nodeToIdMapper.get(edge.getSource()),
				nodeToIdMapper.get(edge.getTarget())));
		sb.append(lineSeparator);
		TreeMap<String, Object> sortedAttrs = new TreeMap<>();
		sortedAttrs.putAll(edge.attributesProperty());
		for (Object attrKey : sortedAttrs.keySet()) {
			sb.append(startIndent + indent);
			sb.append(attrKey.toString() + " : "
					+ edge.attributesProperty().get(attrKey));
			sb.append(lineSeparator);
		}
		sb.append(startIndent);
		sb.append("}");
		sb.append(lineSeparator);

		return sb.toString();
	}

	/**
	 * Creates a formatted string representation of a given {@link Graph}.
	 *
	 * @param graph
	 *            The {@link Graph} for which to create a formatted string
	 *            representation.
	 * @return The formatted string representation (with line separation and
	 *         indentation) of the given {@link Graph}.
	 */
	public String prettyPrint(Graph graph) {
		return prettyPrint(graph, "", "");
	}

	/**
	 * Creates a formatted string representation of a given {@link Graph}.
	 *
	 * @param graph
	 *            The {@link Graph} for which to create a formatted string
	 *            representation.
	 * @param startIndent
	 *            The indentation to use when creating the formatted string
	 *            representation.
	 * @param positionPrefix
	 *            The prefix to prepend the graph position
	 * @return The formatted string representation (with line separation and
	 *         indentation) of the given {@link Graph}.
	 */
	protected String prettyPrint(Graph graph, String startIndent,
			String positionPrefix) {
		StringBuilder sb = new StringBuilder();

		sb.append(startIndent);
		sb.append("Graph {");
		sb.append(lineSeparator);

		TreeMap<String, Object> sortedAttrs = new TreeMap<>();
		sortedAttrs.putAll(graph.attributesProperty());
		for (Object attrKey : sortedAttrs.keySet()) {
			sb.append(startIndent + indent);
			sb.append(attrKey.toString() + " : "
					+ graph.attributesProperty().get(attrKey));
			sb.append(lineSeparator);
		}
		for (Node node : graph.getNodes()) {
			sb.append(prettyPrint(node, startIndent + indent, positionPrefix));
		}
		for (Edge edge : graph.getEdges()) {
			sb.append(prettyPrint(edge, startIndent + indent, positionPrefix));
		}
		sb.append(startIndent);
		sb.append("}");
		sb.append(lineSeparator);

		return sb.toString();
	}

	/**
	 * Creates a formatted string representation of a given {@link Node}.
	 *
	 * @param node
	 *            The {@link Node} for which to create a formatted string
	 *            representation.
	 * @return The formatted string representation (with line separation and
	 *         indentation) of the given {@link Node}.
	 */
	public String prettyPrint(Node node) {
		return prettyPrint(node, "", "");
	}

	/**
	 * Creates a formatted string representation of a given {@link Node}.
	 *
	 * @param node
	 *            The {@link Node} for which to create a formatted string
	 *            representation.
	 * @param startIndent
	 *            The indentation to use when creating the formatted string
	 *            representation.
	 * @param positionPrefix
	 *            The prefix to prepend the node position.
	 * @return The formatted string representation (with line separation and
	 *         indentation) of the given {@link Edge}.
	 */
	protected String prettyPrint(Node node, String startIndent,
			String positionPrefix) {
		StringBuilder sb = new StringBuilder();

		sb.append(startIndent);
		sb.append("Node");
		int position = getPosition(node);
		String nodeId = "";
		if (position != -1) {
			nodeId = positionPrefix + position;
			sb.append(nodeId);
		}
		nodeToIdMapper.put(node, nodeId);
		sb.append(" {");
		sb.append(lineSeparator);
		TreeMap<String, Object> sortedAttrs = new TreeMap<>();
		sortedAttrs.putAll(node.attributesProperty());
		for (Object attrKey : sortedAttrs.keySet()) {
			sb.append(startIndent + indent);
			sb.append(attrKey.toString() + " : "
					+ node.attributesProperty().get(attrKey));
			sb.append(lineSeparator);
		}

		Graph nestedGraph = node.getNestedGraph();
		if (nestedGraph != null) {
			String newPositionPrefix = positionPrefix + position + ".";
			String nestedGraphText = prettyPrint(nestedGraph,
					startIndent + indent, newPositionPrefix);
			sb.append(nestedGraphText);
		}
		sb.append(startIndent);
		sb.append("}");
		sb.append(lineSeparator);

		return sb.toString();
	}

}
