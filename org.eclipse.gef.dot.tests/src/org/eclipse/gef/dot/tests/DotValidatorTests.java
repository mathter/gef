/*******************************************************************************
 * Copyright (c) 2016, 2018 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tamas Miklossy (itemis AG) - initial implementation (bug #477980)		
 *                                - Add support for polygon-based node shapes (bug #441352)
 *     Zoey G. Prigge (itemis AG) - Add support for record-based node shapes (bug #454629)
 *
 *******************************************************************************/

package org.eclipse.gef.dot.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.gef.dot.internal.DotAttributes;
import org.eclipse.gef.dot.internal.DotFileUtils;
import org.eclipse.gef.dot.internal.language.DotInjectorProvider;
import org.eclipse.gef.dot.internal.language.dot.DotAst;
import org.eclipse.gef.dot.internal.language.dot.DotPackage;
import org.eclipse.gef.dot.internal.language.validation.DotRecordLabelJavaValidator;
import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(XtextRunner.class)
@InjectWith(DotInjectorProvider.class)
public class DotValidatorTests {

	@Inject
	ParseHelper<DotAst> parserHelper;

	@Inject
	ValidationTestHelper validationTestHelper;

	@Test
	public void testSingleArrowShapes() {
		DotAst dotAst = parse("arrowshapes_single.dot");
		validationTestHelper.assertNoIssues(dotAst);
	}

	@Test
	public void testMultipleArrowShapes() {
		DotAst dotAst = parse("arrowshapes_multiple.dot");
		validationTestHelper.assertNoIssues(dotAst);
	}

	@Test
	public void testDeprecatedArrowType() throws Exception {
		DotAst dotAst = parse("arrowshapes_deprecated.dot");

		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ediamond' is not semantically correct: The shape 'ediamond' is deprecated.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'open' is not semantically correct: The shape 'open' is deprecated.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'halfopen' is not semantically correct: The shape 'halfopen' is deprecated.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'empty' is not semantically correct: The shape 'empty' is deprecated.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'invempty' is not semantically correct: The shape 'invempty' is deprecated.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ediamondinvempty' is not semantically correct: The shape 'ediamond' is deprecated.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ediamondinvempty' is not semantically correct: The shape 'invempty' is deprecated.");

		int lineDelimiterLength = System.getProperty("line.separator").length();
		assertArrowTypeWarning(dotAst, 1311 + 28 * lineDelimiterLength, 4,
				"The arrowType value 'openbox' is not semantically correct: The shape 'open' is deprecated.");

		// verify that these are the only reported issues
		Assert.assertEquals(8, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testDeprecatedStyle() throws Exception {
		DotAst dotAst = parserHelper.parse(DotTestGraphs.DEPRECATED_STYLES);

		int lineDelimiterLength = System.getProperty("line.separator").length();

		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.STYLE__GCNE,
				113 + 5 * lineDelimiterLength, 12,
				"The style value 'setlinewidth(1)' is not semantically correct: The usage of setlinewidth is deprecated, use the penwidth attribute instead.");

		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.STYLE__GCNE,
				140 + 6 * lineDelimiterLength, 12,
				"The style value 'setlinewidth(2)' is not semantically correct: The usage of setlinewidth is deprecated, use the penwidth attribute instead.");

		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.STYLE__GCNE,
				170 + 7 * lineDelimiterLength, 12,
				"The style value 'setlinewidth(3)' is not semantically correct: The usage of setlinewidth is deprecated, use the penwidth attribute instead.");

		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.STYLE__GCNE,
				222 + 10 * lineDelimiterLength, 12,
				"The style value 'dashed, setlinewidth(4)' is not semantically correct: The usage of setlinewidth is deprecated, use the penwidth attribute instead.");

		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.STYLE__GCNE,
				249 + 11 * lineDelimiterLength, 12,
				"The style value 'setlinewidth(5), dotted' is not semantically correct: The usage of setlinewidth is deprecated, use the penwidth attribute instead.");

		// verify that these are the only reported issues
		Assert.assertEquals(5, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testArrowshapesDirectionBoth() {
		DotAst dotAst = parse("arrowshapes_direction_both.dot");
		validationTestHelper.assertNoIssues(dotAst);
	}

	@Test
	public void testArrowShapesInvalidModifiers() throws Exception {
		DotAst dotAst = parse("arrowshapes_invalid_modifiers.dot");

		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ocrow' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'crow'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'olcrow' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'crow'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'orcrow' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'crow'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'rdot' is not semantically correct: The side modifier 'r' may not be combined with primitive shape 'dot'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ldot' is not semantically correct: The side modifier 'l' may not be combined with primitive shape 'dot'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'oldot' is not semantically correct: The side modifier 'l' may not be combined with primitive shape 'dot'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ordot' is not semantically correct: The side modifier 'r' may not be combined with primitive shape 'dot'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'lnone' is not semantically correct: The side modifier 'l' may not be combined with primitive shape 'none'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'rnone' is not semantically correct: The side modifier 'r' may not be combined with primitive shape 'none'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'onone' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'none'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'olnone' is not semantically correct: The side modifier 'l' may not be combined with primitive shape 'none'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'olnone' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'none'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ornone' is not semantically correct: The side modifier 'r' may not be combined with primitive shape 'none'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ornone' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'none'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'otee' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'tee'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'oltee' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'tee'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ortee' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'tee'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ovee' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'vee'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'olvee' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'vee'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'orvee' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'vee'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'ocurve' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'curve'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'olcurve' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'curve'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'orcurve' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'curve'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'oicurve' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'icurve'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'olicurve' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'icurve'.");
		assertArrowTypeWarning(dotAst,
				"The arrowType value 'oricurve' is not semantically correct: The open modifier 'o' may not be combined with primitive shape 'icurve'.");

		// verify that these are the only reported issues
		Assert.assertEquals(26, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongArrowType() throws Exception {
		String text = "digraph testGraph { 1->2[arrowhead=fooBar arrowtail=fooBar2] }";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.ARROWHEAD__E,
				35, 6,
				"The value 'fooBar' is not a syntactically correct arrowType: No viable alternative at character 'f'. No viable alternative at input 'o'. No viable alternative at character 'B'. No viable alternative at character 'a'. No viable alternative at input '<EOF>'.");

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.ARROWTAIL__E,
				52, 7,
				"The value 'fooBar2' is not a syntactically correct arrowType: No viable alternative at character 'f'. No viable alternative at input 'o'. No viable alternative at character 'B'. No viable alternative at character 'a'. No viable alternative at character '2'.");

		// verify that these are the only reported issues
		Assert.assertEquals(2, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongEdgeDirection() throws Exception {
		String text = "digraph testGraph { 1->2[dir=foo] }";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.DIR__E, 29,
				3,
				"The value 'foo' is not a syntactically correct dirType: Value has to be one of 'forward', 'back', 'both', 'none'.");

		// verify that it is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongEdgeArrowSize() throws Exception {
		String text = "digraph testGraph { 1->2[arrowsize=foo] 3->4[arrowsize=\"-2.0\"]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.ARROWSIZE__E,
				"The value 'foo' is not a syntactically correct double: For input string: \"foo\".");

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.ARROWSIZE__E,
				"The double value '-2.0' is not semantically correct: Value may not be smaller than 0.0.");

		// verify that these are the only reported issues
		Assert.assertEquals(2, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testNoneIsTheLastArrowShape() throws Exception {
		String text = "digraph { 1->2[arrowhead=boxnone] }";

		DotAst dotAst = parserHelper.parse(text);

		assertArrowTypeWarning(dotAst,
				"The arrowType value 'boxnone' is not semantically correct: The shape 'none' may not be the last shape.");

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongGraphBackgroundColor() throws Exception {
		String text = "graph { bgcolor=grsy }";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.BGCOLOR__GC,
				"The colorList value 'grsy' is not semantically correct: The 'grsy' color is not valid within the 'x11' color scheme.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testGraphBackgroundColorDoesNotCorrespondToLocalColorScheme()
			throws Exception {
		String text = "graph { colorscheme=brbg10 bgcolor=blue}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.BGCOLOR__GC,
				"The colorList value 'blue' is not semantically correct: The 'blue' color is not valid within the 'brbg10' color scheme.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testGraphBackgroundColorDoesNotCorrespondToGlobalColorScheme()
			throws Exception {
		String text = "graph { graph[colorscheme=brbg10] bgcolor=blue}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.BGCOLOR__GC,
				"The colorList value 'blue' is not semantically correct: The 'blue' color is not valid within the 'brbg10' color scheme.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongNodeColor() throws Exception {
		String text = "graph { 1[color=\"#fffff\"]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.COLOR__CNE,
				"The value '#fffff' is not a syntactically correct color: Mismatched input '<EOF>' expecting RULE_HEXADECIMAL_DIGIT.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testNodeColorDoesNotCorrespondToLocalColorScheme()
			throws Exception {
		String text = "graph { 1[colorscheme=brbg10 color=blue]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.COLOR__CNE,
				"The color value 'blue' is not semantically correct: The 'blue' color is not valid within the 'brbg10' color scheme.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testNodeColorDoesNotCorrespondToGlobalColorScheme()
			throws Exception {
		String text = "graph { node[colorscheme=brbg10] 1[color=blue]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.COLOR__CNE,
				"The color value 'blue' is not semantically correct: The 'blue' color is not valid within the 'brbg10' color scheme.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongEdgeFillColor() throws Exception {
		String text = "digraph { 1->2[fillcolor=\"#fffff\"]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotAttributes.FILLCOLOR__CNE,
				"The value '#fffff' is not a syntactically correct color: Mismatched input '<EOF>' expecting RULE_HEXADECIMAL_DIGIT.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testEdgeFillColorDoesNotCorrespondToLocalColorScheme()
			throws Exception {
		String text = "digraph { 1->2[colorscheme=brbg10 fillcolor=white]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotAttributes.FILLCOLOR__CNE,
				"The color value 'white' is not semantically correct: The 'white' color is not valid within the 'brbg10' color scheme.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testEdgeFillColorDoesNotCorrespondToGlobalColorScheme()
			throws Exception {
		String text = "digraph { edge[colorscheme=brbg10] 1->2[fillcolor=red]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotAttributes.FILLCOLOR__CNE,
				"The color value 'red' is not semantically correct: The 'red' color is not valid within the 'brbg10' color scheme.");

		// verify that this is the only reported issues
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongNodeDistortion() throws Exception {
		String text = "graph { 1[distortion=foo] 2[distortion=\"-100.0001\"]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotAttributes.DISTORTION__N,
				"The value 'foo' is not a syntactically correct double: For input string: \"foo\".");

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotAttributes.DISTORTION__N,
				"The double value '-100.0001' is not semantically correct: Value may not be smaller than -100.0.");

		// verify that these are the only reported issues
		Assert.assertEquals(2, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongNodeShape() throws Exception {
		String text = "graph { 1[shape=foo] }";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.SHAPE__N,
				"The value 'foo' is not a syntactically correct shape: Extraneous input 'foo' expecting EOF.");

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongNodeSides() throws Exception {
		String text = "graph { 1[sides=foo] 2[sides=\"-1\"]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.SIDES__N,
				"The value 'foo' is not a syntactically correct int: For input string: \"foo\".");

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.SIDES__N,
				"The int value '-1' is not semantically correct: Value may not be smaller than 0.");

		// verify that these are the only reported issues
		Assert.assertEquals(2, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testWrongNodeSkew() throws Exception {
		String text = "graph { 1[skew=foo] 2[skew=\"-100.1\"]}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.SKEW__N,
				"The value 'foo' is not a syntactically correct double: For input string: \"foo\".");

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.SKEW__N,
				"The double value '-100.1' is not semantically correct: Value may not be smaller than -100.0.");

		// verify that these are the only reported issues
		Assert.assertEquals(2, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testDirectedGraphWithNonDirectedEdge() throws Exception {
		String text = "digraph {1--2}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getEdgeRhsNode(), null,
				"EdgeOp '--' may only be used in undirected graphs.");

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testNonDirectedGraphWithDirectedEdge() throws Exception {
		String text = "graph {1->2}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getEdgeRhsNode(), null,
				"EdgeOp '->' may only be used in directed graphs.");

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testInvalidCombinationOfNodeShapeAndStyle() throws Exception {
		/*
		 * The 'striped' node style is only supported with clusters and
		 * rectangularly-shaped nodes('box', 'rect', 'rectangle' and 'square').
		 */

		String text = "graph {1[shape=ellipse style=striped]}";

		DotAst dotAst = parserHelper.parse(text);

		String expectedErrorMessage = "The style 'striped' is only supported with clusters and rectangularly-shaped nodes, such as 'box', 'rect', 'rectangle', 'square'.";

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), null,
				expectedErrorMessage);

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());

		text = "graph {1[style=striped shape=ellipse]}";

		dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), null,
				expectedErrorMessage);

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());

		text = "graph {node[style=striped shape=ellipse]}";

		dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), null,
				expectedErrorMessage);

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());

		text = "graph {1[style=striped]}";

		dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), null,
				expectedErrorMessage);

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());

		// TODO: implement test case
		// text = "graph {node[shape=ellipse] 1[style=striped]}";
	}

	@Test
	public void testInvalidHtmlLikeLabelParserProblem() {
		String text = "graph {1[label = <<BR/><FONT>>]}";
		String errorProneText = "<<BR/><FONT>>";
		String errorMessage = "The value '<BR/><FONT>' is not a syntactically correct htmlLabel: Mismatched input '<EOF>' expecting RULE_TAG_START_CLOSE.";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelTagIsNotClosedProperly() {
		String text = "graph {1[label = <<BR/><FONT/>>]}";
		String errorProneText = "FONT";
		String errorMessage = "The htmlLabel value '<BR/><FONT/>' is not semantically correct: Tag '<FONT/>' cannot be self closing.";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelTagCannotBeSelfClosing() {
		String text = "graph {1[label = <  <FONT></foo>  >]}";
		String errorProneText = "foo";
		String errorMessage = "The htmlLabel value '  <FONT></foo>  ' is not semantically correct: Tag '<FONT>' is not closed (expected '</FONT>' but got '</foo>').";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelStringLiteralIsNotAllowed()
			throws Exception {
		String text = "graph {1[label = <  <BR>string</BR>  >]}";
		String errorProneText = "BR";
		String errorMessage = "The htmlLabel value '  <BR>string</BR>  ' is not semantically correct: Tag '<BR>' cannot contain a string literal.";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelUnsupportedTag() {
		String text = "graph {1[label = <  <test>string</test>  >]}";
		String errorProneText = "test";
		String errorMessage = "The htmlLabel value '  <test>string</test>  ' is not semantically correct: Tag '<test>' is not supported.";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelInvalidParentTag() {
		String text = "graph {1[label = <  <tr></tr>  >]}";
		String errorProneText = "tr";
		String errorMessage = "The htmlLabel value '  <tr></tr>  ' is not semantically correct: Tag '<tr>' is not allowed inside '<ROOT>', but only inside '<TABLE>'.";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelInvalidAttribute() {
		String text = "graph {1[label = <  <table foo=\"bar\"></table>  >]}";
		String errorProneText = "foo";
		String errorMessage = "The htmlLabel value '  <table foo=\"bar\"></table>  ' is not semantically correct: Attribute 'foo' is not allowed inside '<table>'.";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelInvalidAttributeValue() {
		String text = "graph {1[label = <  <table align=\"foo\"></table>  >]}";
		String errorProneText = "\"foo\"";
		String errorMessage = "The htmlLabel value '  <table align=\"foo\"></table>  ' is not semantically correct: The value 'foo' is not a correct align: Value has to be one of 'CENTER', 'LEFT', 'RIGHT'.";
		assertHtmlLikeLabelError(text, errorProneText, errorMessage);
	}

	@Test
	public void testInvalidHtmlLikeLabelInvalidSiblings() {
		// The graphviz DOT HTML-Like Label grammar does not allow text and
		// table or multiple tables on the same (root or nested) level.

		// testDataList[][0]: html label containing invalid siblings
		// testDataList[][1]: text1 to be marked as error prone
		// testDataList[][2]: index to locate the text1 from
		// testDataList[][3]: text2 to be marked as error prone
		// testDataList[][4]: index to locate the text2 from
		// ...
		String[][] testDataList = {
				// root level
				{ "<table></table><b></b>", "table", "0", "b", "15" },
				{ "<table></table><b>text</b>", "table", "0", "b", "15" },
				{ "<table></table><br></br>", "table", "0", "br", "0" },
				{ "<table></table><font></font>", "table", "0", "font", "0" },
				{ "<table></table><font>text</font>", "table", "0", "font",
						"0" },
				{ "<table></table><i></i>", "table", "0", "i", "0" },
				{ "<table></table><i>text</i>", "table", "0", "i", "0" },
				{ "<table></table><o></o>", "table", "0", "o", "0" },
				{ "<table></table><o>text</o>", "table", "0", "o", "0" },
				{ "<table></table><s></s>", "table", "0", "s", "0" },
				{ "<table></table><s>text</s>", "table", "0", "s", "0" },
				{ "<table></table><sub></sub>", "table", "0", "sub", "0" },
				{ "<table></table><sub>text</sub>", "table", "0", "sub", "0" },
				{ "<table></table><sup></sup>", "table", "0", "sup", "0" },
				{ "<table></table><sup>text</sup>", "table", "0", "sup", "0" },
				{ "<table></table><table></table>", "table", "0", "table",
						"15" },
				{ "<table></table><u></u>", "table", "0", "u", "0" },
				{ "<table></table><u>text</u>", "table", "0", "u", "0" },
				{ "<table></table>text", "table", "0", "text", "0" },
				{ "<b></b><table></table>", "b", "0", "table", "0" },
				{ "<b>text</b><table></table>", "b", "0", "table", "0" },
				{ "<br></br><table></table>", "br", "0", "table", "0" },
				{ "<font></font><table></table>", "font", "0", "table", "0" },
				{ "<font>text</font><table></table>", "font", "0", "table",
						"0" },
				{ "<i></i><table></table>", "i", "0", "table", "0" },
				{ "<i>text</i><table></table>", "i", "0", "table", "0" },
				{ "<o></o><table></table>", "o", "0", "table", "0" },
				{ "<o>text</o><table></table>", "o", "0", "table", "0" },
				{ "<s></s><table></table>", "s", "0", "table", "0" },
				{ "<s>text</s><table></table>", "s", "0", "table", "0" },
				{ "<sub></sub><table></table>", "sub", "0", "table", "0" },
				{ "<sub>text</sub><table></table>", "sub", "0", "table", "0" },
				{ "<sup></sup><table></table>", "sup", "0", "table", "0" },
				{ "<sup>text</sup><table></table>", "sup", "0", "table", "0" },
				{ "<u></u><table></table>", "u", "0", "table", "0" },
				{ "<u>text</u><table></table>", "u", "0", "table", "0" },
				{ "text<table></table>", "text", "0", "table", "0" },
				{ "<table></table>text<table></table>", "table", "0", "text",
						"0", "table", "20" },

				// nested level
				{ "<table><tr><td><table></table><b></b></td></tr></table>",
						"table", "15", "b", "30" },
				{ "<table><tr><td><table></table><br></br></td></tr></table>",
						"table", "15", "br", "15" },
				{ "<table><tr><td><table></table><font></font></td></tr></table>",
						"table", "15", "font", "15" },
				{ "<table><tr><td><table></table><i></i></td></tr></table>",
						"table", "15", "i", "15" },
				{ "<table><tr><td><table></table><o></o></td></tr></table>",
						"table", "15", "o", "15" },
				{ "<table><tr><td><table></table><s></s></td></tr></table>",
						"table", "15", "s", "15" },
				{ "<table><tr><td><table></table><sub></sub></td></tr></table>",
						"table", "15", "sub", "15" },
				{ "<table><tr><td><table></table><sup></sup></td></tr></table>",
						"table", "15", "sup", "15" },
				{ "<table><tr><td><table></table><table></table></td></tr></table>",
						"table", "15", "table", "30" },
				{ "<table><tr><td><table></table><u></u></td></tr></table>",
						"table", "15", "u", "15" },
				{ "<table><tr><td><table></table>text</td></tr></table>",
						"table", "15", "text", "15" },
				{ "<table><tr><td><b></b><table></table></td></tr></table>",
						"b", "15", "table", "15" },
				{ "<table><tr><td><br></br><table></table></td></tr></table>",
						"br", "15", "table", "15" },
				{ "<table><tr><td><font></font><table></table></td></tr></table>",
						"font", "15", "table", "15" },
				{ "<table><tr><td><i></i><table></table></td></tr></table>",
						"i", "15", "table", "15" },
				{ "<table><tr><td><o></o><table></table></td></tr></table>",
						"o", "15", "table", "15" },
				{ "<table><tr><td><s></s><table></table></td></tr></table>",
						"s", "15", "table", "15" },
				{ "<table><tr><td><sub></sub><table></table></td></tr></table>",
						"sub", "15", "table", "15" },
				{ "<table><tr><td><sup></sup><table></table></td></tr></table>",
						"sup", "15", "table", "15" },
				{ "<table><tr><td><u></u><table></table></td></tr></table>",
						"u", "15", "table", "15" },
				{ "<table><tr><td>text<table></table></td></tr></table>",
						"text", "15", "table", "15" },
				{ "<table><tr><td><table></table>text<table></table></td></tr></table>",
						"table", "15", "text", "15", "table", "34" } };

		for (String[] testData : testDataList) {
			String htmlLabel = testData[0];
			String text = "graph {1[label = <" + htmlLabel + ">]}";
			int numberOfErrorProneText = (testData.length - 1) / 2;

			String[] errorProneTextList = new String[numberOfErrorProneText];
			int[] errorProneTextIndexList = new int[numberOfErrorProneText];
			String[] errorMessages = new String[numberOfErrorProneText];

			for (int i = 0; i < numberOfErrorProneText; i++) {
				errorProneTextList[i] = testData[2 * i + 1];
				errorProneTextIndexList[i] = Integer
						.valueOf(testData[2 * i + 2]) + 18;
				errorMessages[i] = "The htmlLabel value '" + htmlLabel
						+ "' is not semantically correct: Invalid siblings.";
			}

			assertHtmlLikeLabelErrors(text, errorProneTextList,
					errorProneTextIndexList, errorMessages);
		}
	}

	@Test
	public void testInvalidNodeStyle() {
		String text = "graph {1[style=\"dashed, setlinewidth(4)\"]}";
		String errorProneText = "setlinewidth";
		String message = "The style value 'dashed, setlinewidth(4)' is not semantically correct: The usage of setlinewidth is deprecated, use the penwidth attribute instead.";
		assertStyleWarning(text, errorProneText, message);

		text = "graph {1[style=\"dashed, foo\"]}";
		errorProneText = "foo";
		message = "The style value 'dashed, foo' is not semantically correct: Value should be one of 'bold', 'dashed', 'diagonals', 'dotted', 'filled', 'invis', 'radial', 'rounded', 'solid', 'striped', 'wedged'.";
		assertStyleError(text, errorProneText, message);
	}

	@Test
	public void testInvalidEdgeStyle() throws Exception {
		String text = "graph {1--2[style=\"dashed, setlinewidth(4)\"]}";
		String errorProneText = "setlinewidth";
		String message = "The style value 'dashed, setlinewidth(4)' is not semantically correct: The usage of setlinewidth is deprecated, use the penwidth attribute instead.";
		assertStyleWarning(text, errorProneText, message);

		text = "graph {1--2[style=\"dashed, foo\"]}";
		errorProneText = "foo";
		message = "The style value 'dashed, foo' is not semantically correct: Value should be one of 'bold', 'dashed', 'dotted', 'invis', 'solid', 'tapered'.";
		assertStyleError(text, errorProneText, message);
	}

	@Test
	public void testInvalidSubgraphRankAttribute() throws Exception {
		String text = "graph{subgraph{rank=foo}}";

		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.RANK__S,
				"The value 'foo' is not a syntactically correct rankType: Value has to be one of 'same', 'min', 'source', 'max', 'sink'.");

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	@Test
	public void testRecordShapeLabel() throws Exception {
		DotAst ast = parse("record_shape_node1.dot");
		validationTestHelper.assertNoIssues(ast);
	}

	@Test
	public void testInvalidPortAssignedSameNameRecordLabel() throws Exception {
		String text = "digraph{ node [shape=record]; myNode [label=\"<here> foo | <here> more foo\"]; }";
		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotRecordLabelJavaValidator.PORT_NAME_DUPLICATE, 46, 4,
				"The record-based label '<here> foo | <here> more foo' is not semantically correct: Port name not unique: here");
		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotRecordLabelJavaValidator.PORT_NAME_DUPLICATE, 59, 4,
				"The record-based label '<here> foo | <here> more foo' is not semantically correct: Port name not unique: here");
	}

	@Test
	public void testInvalidPortNotAssignedNameRecordLabel() throws Exception {
		String text = "digraph{ node [shape=record]; myNode [label=\"<> foo | <here> more foo\"]; }";
		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				DotRecordLabelJavaValidator.PORT_NAME_NOT_SET, 45, 6,
				"The record-based label '<> foo | <here> more foo' is not semantically correct: Port unnamed: port cannot be referenced");
	}

	@Test
	public void testInvalidSyntaxErrorRecordLabel() throws Exception {
		String text = "digraph{ node [shape=record]; myNode [label=\"<}> foo | <here> more foo\"]; }";
		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(),
				Diagnostic.SYNTAX_DIAGNOSTIC, 46, 1,
				"The value '<}> foo | <here> more foo' is not a syntactically correct record-based label: extraneous input '}' expecting '>'");
	}

	@Test
	public void testIncompleteModel() throws Exception {
		String text = "graph{1[c]}";
		DotAst dotAst = parserHelper.parse(text);

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttrList(),
				Diagnostic.SYNTAX_DIAGNOSTIC, 9, 1,
				"mismatched input ']' expecting '='");
	}

	private DotAst parse(String fileName) {
		DotAst dotAst = null;
		String fileContents = DotFileUtils
				.read(new File(DotTestUtils.RESOURCES_TESTS + fileName));
		try {
			dotAst = parserHelper.parse(fileContents);
			assertNotNull(dotAst);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return dotAst;
	}

	private void assertArrowTypeWarning(DotAst dotAst, String warningMessage) {
		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.ARROWHEAD__E,
				warningMessage);
	}

	private void assertArrowTypeWarning(DotAst dotAst, int offset, int length,
			String warningMessage) {
		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.ARROWHEAD__E,
				offset, length, warningMessage);
	}

	private void assertHtmlLikeLabelError(String text, String errorProneText,
			String errorMessage) {
		DotAst dotAst = null;
		try {
			dotAst = parserHelper.parse(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(dotAst);
		int offset = text.indexOf(errorProneText);
		int length = errorProneText.length();

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.LABEL__GCNE,
				offset, length, errorMessage);

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	private void assertHtmlLikeLabelErrors(String text,
			String[] errorProneTextList, int[] fromIndexList,
			String errorMessages[]) {
		if (errorProneTextList.length != errorMessages.length
				|| errorProneTextList.length != fromIndexList.length) {
			throw new IllegalArgumentException(
					"Expected as much as errorProneTextList as fromIndexList and as errorMessages!");
		}

		DotAst dotAst = null;
		try {
			dotAst = parserHelper.parse(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(dotAst);

		for (int i = 0; i < errorProneTextList.length; i++) {
			String errorProneText = errorProneTextList[i];
			String errorMessage = errorMessages[i];
			int fromIndex = fromIndexList[i];
			int offset = text.indexOf(errorProneText, fromIndex);
			if (offset < 0) {
				throw new IllegalArgumentException("'" + errorProneText
						+ "' cannot be found in the input string from index "
						+ fromIndex);
			}
			int length = errorProneText.length();
			validationTestHelper.assertError(dotAst,
					DotPackage.eINSTANCE.getAttribute(),
					DotAttributes.LABEL__GCNE, offset, length, errorMessage);
		}

		// verify that these are the only reported issues
		Assert.assertEquals(errorMessages.length,
				validationTestHelper.validate(dotAst).size());
	}

	private void assertStyleWarning(String text, String errorProneText,
			String warningMessage) {
		DotAst dotAst = null;
		try {
			dotAst = parserHelper.parse(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(dotAst);
		int offset = text.indexOf(errorProneText);
		int length = errorProneText.length();

		validationTestHelper.assertWarning(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.STYLE__GCNE,
				offset, length, warningMessage);

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}

	private void assertStyleError(String text, String errorProneText,
			String errorMessage) {
		DotAst dotAst = null;
		try {
			dotAst = parserHelper.parse(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(dotAst);
		int offset = text.indexOf(errorProneText);
		int length = errorProneText.length();

		validationTestHelper.assertError(dotAst,
				DotPackage.eINSTANCE.getAttribute(), DotAttributes.STYLE__GCNE,
				offset, length, errorMessage);

		// verify that this is the only reported issue
		Assert.assertEquals(1, validationTestHelper.validate(dotAst).size());
	}
}