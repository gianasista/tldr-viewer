package de.gianasista.tldr_viewer.util;

public interface CommandContentDelegate 
{
	void receiveCommandContent(String content, boolean hasFailure);
}
