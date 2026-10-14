package com.vogella.eclipse.mcp.core.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.vogella.eclipse.mcp.core.ToolFilter;

class ToolFilterTest {

	@Test
	void aBlankValueDisablesNothing() {
		assertFalse(ToolFilter.parse(null).isDisabled("eclipse_build"));
		assertFalse(ToolFilter.parse("  \n ").isDisabled("eclipse_build"));
	}

	@Test
	void anExactNameDisablesOnlyThatTool() {
		ToolFilter filter = ToolFilter.parse("eclipse_screenshot");
		assertTrue(filter.isDisabled("eclipse_screenshot"));
		assertFalse(filter.isDisabled("eclipse_screenshot_all"));
		assertFalse(filter.isDisabled("eclipse_build"));
	}

	@Test
	void globsAndMixedSeparatorsAreAccepted() {
		ToolFilter filter = ToolFilter.parse("eclipse_debug_*, eclipse_*_flight_recording\neclipse_install ECLIPSE_UPDATE");
		assertTrue(filter.isDisabled("eclipse_debug_launch"));
		assertTrue(filter.isDisabled("eclipse_start_flight_recording"));
		assertTrue(filter.isDisabled("eclipse_install"));
		assertTrue(filter.isDisabled("eclipse_update"));
		assertFalse(filter.isDisabled("eclipse_get_problems"));
	}
}
