package com.vogella.eclipse.mcp.core;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;

/**
 * Which tools the server leaves out, from the {@code disabledTools} preference of the server's node.
 * <p>
 * Every tool a client lists costs it context on every turn, so a user who never debugs or profiles can drop those
 * tools instead of paying for their descriptions. The value is a list of names or globs separated by commas, spaces
 * or line breaks, for example {@code eclipse_*_flight_recording, eclipse_debug_*}.
 */
public final class ToolFilter {

	private static final String QUALIFIER = "com.vogella.eclipse.mcp.server"; //$NON-NLS-1$

	/** The preference key, in the server's preference node. */
	public static final String DISABLED_TOOLS_KEY = "disabledTools"; //$NON-NLS-1$

	private static final ToolFilter NONE = new ToolFilter(List.of());

	private final List<Pattern> disabled;

	private ToolFilter(List<Pattern> disabled) {
		this.disabled = disabled;
	}

	/** A filter that leaves every tool in. */
	public static ToolFilter none() {
		return NONE;
	}

	/** Reads the preference, which is read once per server start because the tool list is built then. */
	public static ToolFilter fromPreferences() {
		return parse(Platform.getPreferencesService().getString(QUALIFIER, DISABLED_TOOLS_KEY, "", null)); //$NON-NLS-1$
	}

	/** Parses a list of names or globs; a blank or null value disables nothing. */
	public static ToolFilter parse(String value) {
		if (value == null || value.isBlank()) {
			return NONE;
		}
		List<Pattern> patterns = Arrays.stream(value.split("[,\\s]+")) //$NON-NLS-1$
				.filter(entry -> !entry.isEmpty()).map(Globs::compile).toList();
		return patterns.isEmpty() ? NONE : new ToolFilter(patterns);
	}

	public boolean isDisabled(String toolName) {
		return disabled.stream().anyMatch(pattern -> pattern.matcher(toolName).matches());
	}
}
