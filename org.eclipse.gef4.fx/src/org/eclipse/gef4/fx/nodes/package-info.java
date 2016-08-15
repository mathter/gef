/*******************************************************************************
 * Copyright (c) 2015 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Matthias Wienand (itemis AG) - initial text
 *
 *******************************************************************************/
/**
 * This package provides:
 * <ul>
 * <li>an adaptation of an {@link org.eclipse.gef4.geometry.planar.IGeometry} to
 * {@link javafx.scene.Node}: {@link org.eclipse.gef4.fx.nodes.GeometryNode}
 * </li>
 * <li>a connection abstraction that is based on
 * {@link org.eclipse.gef4.fx.anchors.IAnchor}:
 * {@link org.eclipse.gef4.fx.nodes.Connection}</li>
 * <li>a visual to display an image which is overlayed by another image on mouse
 * hover: {@link org.eclipse.gef4.fx.nodes.HoverOverlayImageView}</li>
 * <li>a visual providing a scrollable infinite canvas with a background grid:
 * {@link org.eclipse.gef4.fx.nodes.InfiniteCanvas}</li>
 * </ul>
 */
package org.eclipse.gef4.fx.nodes;