package dev.upcraft.sparkweave.api.serialization;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CSVWriter implements AutoCloseable {

	public static final String DEFAULT_SEPARATOR = ",";
	private final String[] columns;
	private final List<String[]> rows = new LinkedList<>();
	private final String separator;
	private final AtomicBoolean hasRowBuilder = new AtomicBoolean(false);
	private final PrintWriter output;

	private CSVWriter(OutputStream output, String[] columns, String separator) {
		Preconditions.checkArgument(columns.length > 0, "no columns provided");
		Preconditions.checkArgument(!separator.isEmpty(), "separator must not be empty");
		Preconditions.checkArgument(!"\"".equals(separator), "separator must not be double quotes");
		this.columns = columns;
		this.output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8)));
		this.separator = separator;

		printRow(columns);
	}

	public CSVWriter addRow(Object... values) {
		Preconditions.checkArgument(values.length == columns.length, "expected %s columns but got %s", columns.length, values.length);
		Preconditions.checkArgument(!hasRowBuilder.get(), "already building row");
		printRow(values);
		return this;
	}

	private void printRow(Object[] row) {
		StringJoiner result = new StringJoiner(separator);
		for (Object entry : row) {
			result.add(quoteCSV(String.valueOf(entry), separator));
		}
		output.println(result);
	}

	/**
	 * creates a new {@link RowBuilder} at the current position<br>
	 * the row position is preserved even if further rows are added to the parent builder
	 */
	public RowBuilder beginRow() {
		Preconditions.checkState(hasRowBuilder.getAndSet(true), "already building row");
		return new RowBuilder(this, columns.length);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("columns", columns.length)
			.add("rows", rows.size())
			.toString();
	}

	public static CSVWriter create(OutputStream destination, String firstColumn, String... otherColumns) {
		String[] columns = new String[otherColumns.length + 1];
		System.arraycopy(otherColumns, 0, columns, 1, otherColumns.length);
		columns[0] = firstColumn;
		return new CSVWriter(destination, columns, DEFAULT_SEPARATOR);
	}

	public static CSVWriter create(OutputStream destination, String[] columns) {
		return new CSVWriter(destination, columns, DEFAULT_SEPARATOR);
	}

	public static CSVWriter withSeparator(OutputStream destination, String separator, String firstColumn, String... otherColumns) {
		String[] columns = new String[otherColumns.length + 1];
		System.arraycopy(otherColumns, 0, columns, 1, otherColumns.length);
		columns[0] = firstColumn;
		return new CSVWriter(destination, columns, separator);
	}

	public static CSVWriter withSeparator(OutputStream destination, String separator, String[] columns) {
		return new CSVWriter(destination, columns, separator);
	}

	private static String quoteCSV(String input, String separator) {
		var result = input;
		var needsQuote = input.contains(separator);
		if (input.contains("\"") && !"\"".equals(separator)) {
			needsQuote = true;
			result = result.replace("\"", "\"\"");
		}
		if (needsQuote) {
			result = "\"" + result + "\"";
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		IOUtils.close(output);
	}

	public static class RowBuilder {
		private final CSVWriter csv;
		private final String[] rowData;
		private int idx = 0;

		private RowBuilder(CSVWriter csv, int columns) {
			this.csv = csv;
			this.rowData = new String[columns];
		}

		public RowBuilder add(String value) {
			Preconditions.checkState(idx < rowData.length - 1, "too many columns");
			rowData[idx++] = value;
			return this;
		}

		public RowBuilder add(String first, String... rest) {
			Preconditions.checkState(idx + rest.length < rowData.length - 1, "too many columns");
			add(first);
			for (String value : rest) {
				add(value);
			}
			return this;
		}

		public CSVWriter end() {
			csv.printRow(rowData);
			csv.hasRowBuilder.set(false);
			return csv;
		}
	}
}
