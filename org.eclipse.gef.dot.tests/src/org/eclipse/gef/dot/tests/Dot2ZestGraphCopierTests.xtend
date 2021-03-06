/*******************************************************************************
 * Copyright (c) 2018 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tamas Miklossy (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.gef.dot.tests;

import org.eclipse.gef.dot.internal.ui.Dot2ZestGraphCopier
import org.eclipse.gef.graph.Edge
import org.eclipse.gef.graph.Graph
import org.eclipse.gef.graph.Node
import org.eclipse.gef.mvc.tests.fx.rules.FXApplicationThreadRule
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

import static extension org.junit.Assert.*
import org.junit.Ignore

/*
 * Test class containing test cases for the {@link Dot2ZestGraphCopier} class.
 */
@Ignore("The CI Server (Travis/Hudson) calculates other position information")
class Dot2ZestGraphCopierTests {

	/**
	 * Ensure all tests are executed on the JavaFX application thread (and the
	 * JavaFX toolkit is properly initialized).
	 */
	@Rule
	public FXApplicationThreadRule fxApplicationThreadRule = new FXApplicationThreadRule

	static extension Dot2ZestGraphCopier dot2ZestGraphCopier
	static extension DotGraphPrettyPrinter prettyPrinter

	@BeforeClass
	def static void beforeClass(){
		dot2ZestGraphCopier = new Dot2ZestGraphCopier
		dot2ZestGraphCopier.attributeCopier.options.emulateLayout = false
		
		prettyPrinter = new DotGraphPrettyPrinter
	}

	@Test
	def void simpleGraph() {
		val dot = DotTestUtils.simpleGraph
		val zest = dot.copy

		// test graph
		zest.test('''
			Graph {
				Node1 {
					element-label : 1
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node2 {
					element-label : 2
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
				}
				Edge2 from Node1 to Node3 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
				}
			}
		''')
		
		// test node
		zest.nodes.get(0).test('''
			Node1 {
				element-label : 1
				node-shape : GeometryNode
				node-size : Dimension(54.0, 36.0)
			}
		''')
		
		// test edge
		zest.edges.get(0).test('''
			Edge1 from Node1 to Node2 {
				edge-curve : GeometryNode
				edge-curve-css-style : -fx-stroke-line-cap: butt;
			}
		''')
	}

	@Test
	def directedGraph() {
		val dot = DotTestUtils.simpleDiGraph
		val zest = dot.copy

		zest.test('''
			Graph {
				Node1 {
					element-label : 1
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node2 {
					element-label : 2
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge2 from Node2 to Node3 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
			}
		''')
	}

	@Test
	def labeledGraph() {
		val dot = DotTestUtils.labeledGraph
		val zest = dot.copy

		zest.test('''
			Graph {
				Node1 {
					element-label : one "1"
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node2 {
					element-label : two
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node4 {
					element-label : 4
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
					element-label : +1
				}
				Edge2 from Node1 to Node3 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
					element-label : +2
				}
				Edge3 from Node3 to Node4 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
			}
		''')
	}

	@Test
	def styledGraph() {
		val dot = DotTestUtils.styledGraph
		val zest = dot.copy

		zest.test('''
			Graph {
				Node1 {
					element-label : 1
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node2 {
					element-label : 2
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node4 {
					element-label : 4
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node5 {
					element-label : 5
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 7 7;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge2 from Node2 to Node3 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 1 7;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge3 from Node3 to Node4 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 7 7;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge4 from Node3 to Node5 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 7 7;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge5 from Node4 to Node5 {
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
			}
		''')
	}
	
	@Test
	def dotClusterGraphPrettyPrintTest() {
		val dot = DotTestUtils.clusteredGraph
		'''
			Graph {
				_type : digraph
				Node1 {
					Graph {
						_name : cluster1
						Node1.1 {
							_name : a
						}
						Node1.2 {
							_name : b
						}
						Edge1.1 from Node1.1 to Node1.2 {
						}
					}
				}
				Node2 {
					Graph {
						_name : cluster2
						Node2.1 {
							_name : p
						}
						Node2.2 {
							_name : q
						}
						Node2.3 {
							_name : r
						}
						Node2.4 {
							_name : s
						}
						Node2.5 {
							_name : t
						}
						Edge2.1 from Node2.1 to Node2.2 {
						}
						Edge2.2 from Node2.2 to Node2.3 {
						}
						Edge2.3 from Node2.3 to Node2.4 {
						}
						Edge2.4 from Node2.4 to Node2.5 {
						}
						Edge2.5 from Node2.5 to Node2.1 {
						}
					}
				}
				Edge1 from Node1.2 to Node2.2 {
				}
				Edge2 from Node2.5 to Node1.1 {
				}
			}
		'''.toString.assertEquals(dot.prettyPrint)
	}
	
	@Test
	def dotNestedClusterGraphPrettyPrintTest() {
		val dot = DotTestUtils.nestedClusteredGraph
		'''
			Graph {
				_type : digraph
				Node1 {
					Graph {
						_name : cluster1
						Node1.1 {
							Graph {
								_name : cluster1_1
								Node1.1.1 {
									_name : a
								}
								Node1.1.2 {
									_name : b
								}
								Edge1.1.1 from Node1.1.1 to Node1.1.2 {
								}
							}
						}
					}
				}
				Node2 {
					Graph {
						_name : cluster2
						Node2.1 {
							_name : p
						}
						Node2.2 {
							_name : q
						}
						Node2.3 {
							_name : r
						}
						Node2.4 {
							_name : s
						}
						Node2.5 {
							_name : t
						}
						Edge2.1 from Node2.1 to Node2.2 {
						}
						Edge2.2 from Node2.2 to Node2.3 {
						}
						Edge2.3 from Node2.3 to Node2.4 {
						}
						Edge2.4 from Node2.4 to Node2.5 {
						}
						Edge2.5 from Node2.5 to Node2.1 {
						}
					}
				}
				Edge1 from Node1.1.2 to Node2.2 {
				}
				Edge2 from Node2.5 to Node1.1.1 {
				}
			}
		'''.toString.assertEquals(dot.prettyPrint)
	}
	
	@Test
	def void simpleGraphWithAdditionalInformation() {
		val dot = DotTestUtils.simpleGraphWithAdditionalInformation
		val zest = dot.copy

		zest.test('''
			Graph {
				Node1 {
					element-label : 1
					element-layout-irrelevant : false
					node-position : Point(36.0, 0.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node2 {
					element-label : 2
					element-layout-irrelevant : false
					node-position : Point(0.0, 72.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					element-layout-irrelevant : false
					node-position : Point(72.0, 72.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-control-points : [Point(54.65, 35.235), Point(48.835, 46.544), Point(41.11, 61.563), Point(35.304, 72.853)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(35.304, 72.853)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(54.65, 35.235)
				}
				Edge2 from Node1 to Node3 {
					edge-control-points : [Point(71.35, 35.235), Point(77.165, 46.544), Point(84.89, 61.563), Point(90.696, 72.853)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(90.696, 72.853)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(71.35, 35.235)
				}
			}
		''')
	}

	@Test
	def directedGraphWithAdditionalInformation() {
		val dot = DotTestUtils.simpleDiGraphWithAdditionalInformation
		val zest = dot.copy

		zest.test('''
			Graph {
				Node1 {
					element-label : 1
					element-layout-irrelevant : false
					node-position : Point(0.0, 0.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node2 {
					element-label : 2
					element-layout-irrelevant : false
					node-position : Point(0.0, 72.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					element-layout-irrelevant : false
					node-position : Point(0.0, 144.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-control-points : [Point(27.0, 36.303), Point(27.0, 44.017), Point(27.0, 53.288), Point(27.0, 61.888)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(27.0, 71.896)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(27.0, 36.303)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge2 from Node2 to Node3 {
					edge-control-points : [Point(27.0, 108.3), Point(27.0, 116.02), Point(27.0, 125.29), Point(27.0, 133.89)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(27.0, 143.9)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(27.0, 108.3)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
			}
		''')
	}

	@Test
	def labeledGraphWithAdditionalInformation() {
		val dot = DotTestUtils.labeledGraphWithAdditionalInformation
		val zest = dot.copy

		zest.test('''
			Graph {
				Node1 {
					element-label : one "1"
					element-layout-irrelevant : false
					node-position : Point(15.6528, 0.0)
					node-shape : GeometryNode
					node-size : Dimension(76.6944, 36.0)
				}
				Node2 {
					element-label : two
					element-layout-irrelevant : false
					node-position : Point(0.0, 87.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					element-layout-irrelevant : false
					node-position : Point(72.0, 87.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node4 {
					element-label : 4
					element-layout-irrelevant : false
					node-position : Point(72.0, 160.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-control-points : [Point(48.536, 36.201), Point(44.779, 48.03), Point(39.715, 63.97), Point(35.442, 77.422)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(32.364, 87.115)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-label-position : Point(42.6611328125, 53.51953125)
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(48.536, 36.201)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
					element-label : +1
				}
				Edge2 from Node1 to Node3 {
					edge-control-points : [Point(62.891, 35.793), Point(69.384, 48.058), Point(78.299, 64.898), Point(85.646, 78.776)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(90.433, 87.818)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-label-position : Point(79.6611328125, 53.51953125)
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(62.891, 35.793)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
					element-label : +2
				}
				Edge3 from Node3 to Node4 {
					edge-control-points : [Point(99.0, 123.19), Point(99.0, 131.21), Point(99.0, 140.95), Point(99.0, 149.93)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(99.0, 159.97)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(99.0, 123.19)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
			}
		''')
	}

	@Test
	def styledGraphWithAdditionalInformation() {
		val dot = DotTestUtils.styledGraphWithAdditionalInformation
		val zest = dot.copy

		zest.test('''
			Graph {
				Node1 {
					element-label : 1
					element-layout-irrelevant : false
					node-position : Point(27.0, 0.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node2 {
					element-label : 2
					element-layout-irrelevant : false
					node-position : Point(27.0, 72.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node3 {
					element-label : 3
					element-layout-irrelevant : false
					node-position : Point(27.0, 144.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node4 {
					element-label : 4
					element-layout-irrelevant : false
					node-position : Point(0.0, 216.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Node5 {
					element-label : 5
					element-layout-irrelevant : false
					node-position : Point(27.0, 288.0)
					node-shape : GeometryNode
					node-size : Dimension(54.0, 36.0)
				}
				Edge1 from Node1 to Node2 {
					edge-control-points : [Point(54.0, 36.303), Point(54.0, 44.017), Point(54.0, 53.288), Point(54.0, 61.888)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 7 7;
					edge-end-point : Point(54.0, 71.896)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(54.0, 36.303)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge2 from Node2 to Node3 {
					edge-control-points : [Point(54.0, 108.3), Point(54.0, 116.02), Point(54.0, 125.29), Point(54.0, 133.89)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 1 7;
					edge-end-point : Point(54.0, 143.9)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(54.0, 108.3)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge3 from Node3 to Node4 {
					edge-control-points : [Point(47.601, 179.59), Point(44.486, 187.66), Point(40.666, 197.57), Point(37.165, 206.65)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 7 7;
					edge-end-point : Point(33.54, 216.04)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(47.601, 179.59)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge4 from Node3 to Node5 {
					edge-control-points : [Point(57.654, 180.09), Point(59.676, 190.43), Point(61.981, 203.91), Point(63.0, 216.0), Point(64.344, 231.94), Point(64.344, 236.06), Point(63.0, 252.0), Point(62.283, 260.5), Point(60.931, 269.69), Point(59.488, 277.99)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-dash-array: 7 7;
					edge-end-point : Point(57.654, 287.91)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(57.654, 180.09)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
				Edge5 from Node4 to Node5 {
					edge-control-points : [Point(33.399, 251.59), Point(36.514, 259.66), Point(40.334, 269.57), Point(43.835, 278.65)]
					edge-curve : GeometryNode
					edge-curve-css-style : -fx-stroke-line-cap: butt;
					edge-end-point : Point(47.46, 288.04)
					edge-interpolator : org.eclipse.gef.dot.internal.ui.DotBSplineInterpolator
					edge-router : org.eclipse.gef.fx.nodes.StraightRouter
					edge-start-point : Point(33.399, 251.59)
					edge-target-decoration : Polygon[points=[0.0, 0.0, 10.0, -3.3333333333333335, 10.0, 3.3333333333333335], fill=0x000000ff, stroke=0x000000ff, strokeWidth=1.0]
				}
			}
		''')
	}

	private def test(Graph actual, CharSequence expected) {
		// compare the string representation removing the objectIDs
		expected.toString.assertEquals(actual.prettyPrint.removeObjectIDs)
	}
	
	private def test(Node actual, CharSequence expected) {
		// compare the string representation removing the objectIDs
		expected.toString.assertEquals(actual.prettyPrint.removeObjectIDs)
	}
	
	private def test(Edge actual, CharSequence expected) {
		// compare the string representation removing the objectIDs
		expected.toString.assertEquals(actual.prettyPrint.removeObjectIDs)
	}
	
	private def removeObjectIDs(String text){
		// recognize substrings between '@' and the end of the line
		val nl = System.lineSeparator
		val regex = '''(@[^\\«nl»]*)'''
		
		text.replaceAll(regex, "")
	}
}