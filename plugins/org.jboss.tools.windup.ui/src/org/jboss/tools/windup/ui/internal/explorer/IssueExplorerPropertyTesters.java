/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.explorer;

import org.eclipse.core.expressions.PropertyTester;

/**
 * Property testers for the Issue Explorer.
 */
public class IssueExplorerPropertyTesters {
	
	public static final String QUICKFIX = "hasQuickFix";
	public static final String FIXED = "isFixed";

	public static class QuickFixPropertyTester extends PropertyTester {
		@Override
		public boolean test(Object element, String property, Object[] args, Object expectedValue) {
			if (QUICKFIX.equals(property)) {
				if (element instanceof IssueNode) {
					return ((IssueNode)element).hasQuickFix();
				}
			}
			else if (FIXED.equals(property)) {
				if (element instanceof IssueNode) {
					return !((IssueNode)element).isFixed();
				}
			}
			return false;
		}
	}
}
