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

import static org.jboss.tools.windup.model.domain.WindupMarker.SEVERITY;
import static org.jboss.tools.windup.model.domain.WindupMarker.TITLE;
import static org.jboss.tools.windup.ui.internal.Messages.issueDeleteError;
import static org.jboss.tools.windup.ui.internal.Messages.operationError;

import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.windup.reporting.model.Severity;

/**
 * Represents a marker grouping.
 */
public class IssueNode extends IssueGroupNode<IMarker> {
	
	private IMarker marker;
	private Issue issue;
	
	@Inject private IEventBroker broker;
	
	@Inject
	public IssueNode(IssueGroupNode<?> parent, IMarker marker, ModelService modelService) {
		super(parent);
		this.marker = marker;
		this.issue = modelService.findIssue(marker);
	}
	
	public Issue getIssue() {
		return issue;
	}
	
	@Override
	public String getLabel() {
		return marker.getAttribute(TITLE, "unknown issue");
	}
	
	@Override
	public IMarker getType() {
		return marker;
	}
	
	public int getSeverity() {
		String severity = marker.getAttribute(SEVERITY, Severity.OPTIONAL.toString());
		return MarkerUtil.convertSeverity(severity);
	}
	
	public boolean hasQuickFix() {
		IMarkerResolution[] resolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);
		return resolutions.length > 0 && !issue.isFixed();
	}
	
	public void markAsFixed() {
		issue.setFixed(true);
		try {
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
		broker.post(WindupConstants.ISSUE_CHANGED, createData());
	}
	
	public boolean isFixed() {
		return issue.isFixed();
	}
	
	public void delete() {
		try {
			marker.delete();
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
			MessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), 
					operationError, issueDeleteError, SWT.NONE);
		}
		broker.post(WindupConstants.ISSUE_DELETED, createData());
	}
}
