package fosu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple file-based persistence helper for Product objects.
 *
 * This class provides two small helpers to save/load a list of Product
 * instances to/from a CSV file. The CSV format is simple and escapes
 * fields by quoting and doubling embedded quotes.
 *
 * Note: This is intentionally lightweight (no external JSON dependency)
 * and intended for demo/testing purposes only.
 */
public class savedatabase {

	private static final String[] HEADERS = new String[]{"id","title","description","price","category","condition","sellerUsername"};

	public static void saveProductsToCsv(List<Product> products, String filePath) throws IOException {
		File f = new File(filePath);
		File parent = f.getParentFile();
		if (parent != null && !parent.exists()) parent.mkdirs();

		try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
			// header
			w.write(String.join(",", HEADERS));
			w.newLine();
			for (Product p : products) {
				w.write(escape(p.getId())); w.write(',');
				w.write(escape(p.getTitle())); w.write(',');
				w.write(escape(p.getDescription())); w.write(',');
				w.write(escape(p.getPrice() == null ? null : p.getPrice().toString())); w.write(',');
				w.write(escape(p.getCategory())); w.write(',');
				w.write(escape(p.getCondition())); w.write(',');
				w.write(escape(p.getSellerUsername()));
				w.newLine();
			}
		}
	}

	public static List<Product> loadProductsFromCsv(String filePath) throws IOException {
		List<Product> list = new ArrayList<>();
		File f = new File(filePath);
		if (!f.exists()) return list;

		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String line = r.readLine(); // header
			if (line == null) return list;
			while ((line = r.readLine()) != null) {
				String[] cols = splitCsvLine(line);
				// ensure at least expected columns
				String id = cols.length > 0 ? unescape(cols[0]) : null;
				String title = cols.length > 1 ? unescape(cols[1]) : null;
				String desc = cols.length > 2 ? unescape(cols[2]) : null;
				String priceS = cols.length > 3 ? unescape(cols[3]) : null;
				BigDecimal price = null;
				if (priceS != null && !priceS.isEmpty()) {
					try { price = new BigDecimal(priceS); } catch (Exception ignored) {}
				}
				String category = cols.length > 4 ? unescape(cols[4]) : null;
				String condition = cols.length > 5 ? unescape(cols[5]) : null;
				String seller = cols.length > 6 ? unescape(cols[6]) : null;
				Product p = new Product(id, title, desc, price, category, condition, seller);
				list.add(p);
			}
		}
		return list;
	}

	// CSV helpers
	private static String escape(String v) {
		if (v == null) return "";
		String s = v.replace("\"", "\"\"");
		return "\"" + s + "\"";
	}

	private static String unescape(String v) {
		if (v == null) return null;
		String s = v;
		if (s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length()-1) == '"') {
			s = s.substring(1, s.length()-1);
		}
		return s.replace("\"\"", "\"");
	}

	private static String[] splitCsvLine(String line) {
		List<String> cols = new ArrayList<>();
		StringBuilder cur = new StringBuilder();
		boolean inQuotes = false;
		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);
			if (ch == '"') {
				// if next is also quote, it's an escaped quote
				if (inQuotes && i + 1 < line.length() && line.charAt(i+1) == '"') {
					cur.append('"');
					i++; // skip next
				} else {
					inQuotes = !inQuotes;
				}
			} else if (ch == ',' && !inQuotes) {
				cols.add(cur.toString());
				cur.setLength(0);
			} else {
				cur.append(ch);
			}
		}
		cols.add(cur.toString());
		return cols.toArray(new String[0]);
	}
}
