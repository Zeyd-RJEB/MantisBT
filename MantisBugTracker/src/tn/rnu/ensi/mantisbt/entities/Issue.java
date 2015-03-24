package tn.rnu.ensi.mantisbt.entities;

public class Issue {
	Project project;
	User assignTo;
	String reproducibility;
	String severity;
	String priority;
	String summary;
	String description;



	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(User assignTo) {
		this.assignTo = assignTo;
	}

	public String getReproducibility() {
		return reproducibility;
	}

	public void setReproducibility(String reproducibility) {
		this.reproducibility = reproducibility;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
